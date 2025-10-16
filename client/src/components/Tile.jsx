import "../App.css";
import React from 'react';

const Tile = ({ value }) => {
  const getTileClass = (value) => {
    if (value === 0) return 'tile tile-empty';
    return `tile tile-${value}`;
  };

  const getTileText = (value) => {
    return value === 0 ? '' : value;
  };

  return (
    <div className={getTileClass(value)}>
      <span className="tile-text">{getTileText(value)}</span>
    </div>
  );
};

export default Tile;