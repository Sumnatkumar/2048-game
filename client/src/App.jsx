import "./App.css";
import React, { useState, useEffect, useCallback } from "react";
import GameBoard from "./components/GameBoard.jsx";
import ScoreBoard from "./components/ScoreBoard.jsx";
import GameControls from "./components/GameControls.jsx";
import gameService from "./assets/gameService.js";

function App() {
  const [gameState, setGameState] = useState(null);
  const [gameId, setGameId] = useState(null);
  const [boardSize, setBoardSize] = useState(4);

  const initializeGame = useCallback(async (size = 4) => {
    try {
      const response = await gameService.createNewGame(size);
      setGameState(response.gameState);
      setGameId(response.gameId);
    } catch (error) {
      console.error('Failed to initialize game:', error);
    }
  }, []);

  useEffect(() => {
    initializeGame(boardSize);
  }, [initializeGame, boardSize]);

  const handleKeyPress = useCallback(async (event) => {
    if (!gameId || !gameState || gameState.gameOver) return;

    let direction;
    switch (event.key) {
      case 'ArrowUp':
        direction = 'UP';
        break;
      case 'ArrowDown':
        direction = 'DOWN';
        break;
      case 'ArrowLeft':
        direction = 'LEFT';
        break;
      case 'ArrowRight':
        direction = 'RIGHT';
        break;
      default:
        return;
    }

    try {
      const newState = await gameService.makeMove(gameId, direction);
      setGameState(newState);
    } catch (error) {
      console.error('Move failed:', error);
    }
  }, [gameId, gameState]);

  useEffect(() => {
    window.addEventListener('keydown', handleKeyPress);
    return () => {
      window.removeEventListener('keydown', handleKeyPress);
    };
  }, [handleKeyPress]);

  const handleRestart = async () => {
    await initializeGame(boardSize);
  };

  const handleBoardSizeChange = (newSize) => {
    setBoardSize(newSize);
    initializeGame(newSize);
  };

  if (!gameState) {
    return <div className="loading">Loading game...</div>;
  }

  return (
    <div className="app">
      <div className="game-container">
        <header className="game-header">
          <h1>2048</h1>
          <p>Join the numbers and get to the <strong>2048 tile!</strong></p>
        </header>

        <div className="game-info">
          <ScoreBoard 
            score={gameState.score} 
            gameOver={gameState.gameOver}
            won={gameState.won}
          />
          <GameControls 
            onRestart={handleRestart}
            onBoardSizeChange={handleBoardSizeChange}
          />
        </div>

        <div className="game-area">
          <GameBoard board={gameState.board} />
        </div>

        <div className="game-instructions">
          <p><strong>How to play:</strong> Use your arrow keys to move the tiles. 
          When two tiles with the same number touch, they merge into one!</p>
        </div>
      </div>
    </div>
  );
}

export default App;
