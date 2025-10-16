package com.game2048.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record GameState(
        int[][] board,
        int score,
        boolean gameOver,
        boolean won,
        int boardSize
) {

    public static GameState initialize(int boardSize) {
        int[][] board = new int[boardSize][boardSize];
        GameState initialState = new GameState(board, 0, false, false, boardSize);
        return addRandomTile(addRandomTile(initialState));
    }

    public GameState move(Direction direction) {
        int[][] newBoard = copyBoard();
        int newScore = this.score;
        boolean moved = false;

        switch (direction) {
            case UP -> {
                for (int col = 0; col < boardSize; col++) {
                    int[] column = getColumn(newBoard, col);
                    MergeResult result = mergeAndShift(column);
                    setColumn(newBoard, col, result.tiles());
                    newScore += result.score();
                    moved = moved || result.moved();
                }
            }
            case DOWN -> {
                for (int col = 0; col < boardSize; col++) {
                    int[] column = reverseArray(getColumn(newBoard, col));
                    MergeResult result = mergeAndShift(column);
                    setColumn(newBoard, col, reverseArray(result.tiles()));
                    newScore += result.score();
                    moved = moved || result.moved();
                }
            }
            case LEFT -> {
                for (int row = 0; row < boardSize; row++) {
                    MergeResult result = mergeAndShift(newBoard[row]);
                    newBoard[row] = result.tiles();
                    newScore += result.score();
                    moved = moved || result.moved();
                }
            }
            case RIGHT -> {
                for (int row = 0; row < boardSize; row++) {
                    int[] reversedRow = reverseArray(newBoard[row]);
                    MergeResult result = mergeAndShift(reversedRow);
                    newBoard[row] = reverseArray(result.tiles());
                    newScore += result.score();
                    moved = moved || result.moved();
                }
            }
        }

        if (!moved) {
            return this;
        }

        GameState newState = new GameState(newBoard, newScore, false, false, boardSize);
        GameState withNewTile = addRandomTile(newState);
        boolean isGameOver = isGameOver(withNewTile);
        boolean hasWon = hasWon(withNewTile);

        return new GameState(
                withNewTile.board(),
                withNewTile.score(),
                isGameOver,
                hasWon,
                boardSize
        );
    }

    private static MergeResult mergeAndShift(int[] tiles) {
        List<Integer> nonZeroTiles = IntStream.of(tiles)
                .filter(tile -> tile != 0)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> mergedTiles = new ArrayList<>();
        int score = 0;
        boolean moved = false;
        int i = 0;

        while (i < nonZeroTiles.size()) {
            if (i < nonZeroTiles.size() - 1 &&
                    nonZeroTiles.get(i).equals(nonZeroTiles.get(i + 1))) {
                int mergedValue = nonZeroTiles.get(i) * 2;
                mergedTiles.add(mergedValue);
                score += mergedValue;
                i += 2;
                moved = true;
            } else {
                mergedTiles.add(nonZeroTiles.get(i));
                i++;
            }
        }

        // Fill remaining spaces with zeros
        while (mergedTiles.size() < tiles.length) {
            mergedTiles.add(0);
        }

        // Check if any movement occurred
        if (!moved) {
            moved = !IntStream.of(tiles)
                    .boxed()
                    .collect(Collectors.toList())
                    .equals(mergedTiles);
        }

        return new MergeResult(
                mergedTiles.stream().mapToInt(Integer::intValue).toArray(),
                score,
                moved
        );
    }

    private static GameState addRandomTile(GameState state) {
        List<Position> emptyPositions = getEmptyPositions(state.board());

        if (emptyPositions.isEmpty()) {
            return state;
        }

        Random random = new Random();
        Position randomPosition = emptyPositions.get(random.nextInt(emptyPositions.size()));
        int newValue = random.nextDouble() < 0.9 ? 2 : 4;

        int[][] newBoard = state.copyBoard();
        newBoard[randomPosition.row()][randomPosition.col()] = newValue;

        return new GameState(
                newBoard,
                state.score(),
                state.gameOver(),
                state.won(),
                state.boardSize()
        );
    }

    private static List<Position> getEmptyPositions(int[][] board) {
        List<Position> emptyPositions = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    emptyPositions.add(new Position(row, col));
                }
            }
        }
        return emptyPositions;
    }

    private boolean isGameOver(GameState state) {
        // Check for empty spaces
        if (getEmptyPositions(state.board()).size() > 0) {
            return false;
        }

        // Check for possible merges
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int current = state.board()[row][col];
                // Check right neighbor
                if (col < boardSize - 1 && state.board()[row][col + 1] == current) {
                    return false;
                }
                // Check bottom neighbor
                if (row < boardSize - 1 && state.board()[row + 1][col] == current) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasWon(GameState state) {
        for (int[] row : state.board()) {
            for (int tile : row) {
                if (tile == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] copyBoard() {
        int[][] newBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, boardSize);
        }
        return newBoard;
    }

    private int[] getColumn(int[][] board, int col) {
        return IntStream.range(0, boardSize)
                .map(i -> board[i][col])
                .toArray();
    }

    private void setColumn(int[][] board, int col, int[] column) {
        for (int i = 0; i < boardSize; i++) {
            board[i][col] = column[i];
        }
    }

    private int[] reverseArray(int[] array) {
        return IntStream.range(0, array.length)
                .map(i -> array[array.length - 1 - i])
                .toArray();
    }
}

record Position(int row, int col) {}
record MergeResult(int[] tiles, int score, boolean moved) {}