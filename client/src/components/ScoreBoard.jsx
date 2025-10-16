import "../App.css";
import React from 'react';

const ScoreBoard = ({ score, gameOver, won }) => {
  return (
    <div className="score-board">
      <div className="score-container">
        <div className="score-label">SCORE</div>
        <div className="score-value">{score}</div>
      </div>
      
      {gameOver && !won && (
        <div className="game-status game-over">Game Over!</div>
      )}
      
      {won && (
        <div className="game-status game-won">You Win!</div>
      )}
    </div>
  );
};

export default ScoreBoard;