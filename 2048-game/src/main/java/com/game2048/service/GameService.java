package com.game2048.service;

import com.game2048.model.Direction;
import com.game2048.model.GameState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    private final Map<String, GameState> games = new HashMap<>();

    public String createNewGame(int boardSize) {
        String gameId = UUID.randomUUID().toString();
        GameState initialState = GameState.initialize(boardSize);
        games.put(gameId, initialState);
        return gameId;
    }

    public GameState makeMove(String gameId, Direction direction) {
        GameState currentState = games.get(gameId);
        if (currentState == null) {
            throw new IllegalArgumentException("Game not found");
        }

        GameState newState = currentState.move(direction);
        games.put(gameId, newState);
        return newState;
    }

    public GameState getGameState(String gameId) {
        GameState state = games.get(gameId);
        if (state == null) {
            throw new IllegalArgumentException("Game not found");
        }
        return state;
    }

    public void restartGame(String gameId, int boardSize) {
        GameState newState = GameState.initialize(boardSize);
        games.put(gameId, newState);
    }

    public boolean gameExists(String gameId) {
        return games.containsKey(gameId);
    }
}
