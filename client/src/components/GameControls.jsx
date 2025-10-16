import React, { useState } from 'react';
import "../App.css";

const GameControls = ({ onRestart, onBoardSizeChange }) => {
  const [boardSize, setBoardSize] = useState(4);

  const handleSizeChange = (event) => {
    const newSize = parseInt(event.target.value);
    setBoardSize(newSize);
    onBoardSizeChange(newSize);
  };

  return (
    <div className="game-controls">
      <button className="restart-button" onClick={onRestart}>
        New Game
      </button>
      
      <div className="board-size-control">
        <label htmlFor="board-size">Board Size:</label>
        <select 
          id="board-size"
          value={boardSize} 
          onChange={handleSizeChange}
          className="size-select"
        >
          <option value={3}>3x3</option>
          <option value={4}>4x4</option>
          <option value={5}>5x5</option>
          <option value={6}>6x6</option>
        </select>
      </div>
    </div>
  );
};

export default GameControls;