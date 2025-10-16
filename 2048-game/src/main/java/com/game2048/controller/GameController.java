package com.game2048.controller;

import com.game2048.model.Direction;
import com.game2048.model.GameState;
import com.game2048.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> createNewGame(
            @RequestParam(defaultValue = "4") int boardSize) {
        String gameId = gameService.createNewGame(boardSize);
        GameState state = gameService.getGameState(gameId);

        return ResponseEntity.ok(Map.of(
                "gameId", gameId,
                "gameState", state
        ));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameState> makeMove(
            @PathVariable String gameId,
            @RequestParam Direction direction) {

        if (!gameService.gameExists(gameId)) {
            return ResponseEntity.notFound().build();
        }

        GameState newState = gameService.makeMove(gameId, direction);
        return ResponseEntity.ok(newState);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGameState(@PathVariable String gameId) {
        try {
            GameState state = gameService.getGameState(gameId);
            return ResponseEntity.ok(state);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{gameId}/restart")
    public ResponseEntity<GameState> restartGame(
            @PathVariable String gameId,
            @RequestParam(defaultValue = "4") int boardSize) {

        if (!gameService.gameExists(gameId)) {
            return ResponseEntity.notFound().build();
        }

        gameService.restartGame(gameId, boardSize);
        GameState state = gameService.getGameState(gameId);
        return ResponseEntity.ok(state);
    }
}
