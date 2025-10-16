import "../App.css";
import React from 'react';
import Tile from './Tile';

const GameBoard = ({ board }) => {
  return (
    <div className="game-board">
      {board.map((row, rowIndex) => (
        <div key={rowIndex} className="board-row">
          {row.map((tileValue, colIndex) => (
            <Tile key={`${rowIndex}-${colIndex}`} value={tileValue} />
          ))}
        </div>
      ))}
    </div>
  );
};

export default GameBoard;