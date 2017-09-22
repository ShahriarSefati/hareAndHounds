package com.oose2017.ssefati2.hareandhounds;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static spark.Spark.*;

public class GameController {

    private static final String API_CONTEXT = "/hareandhounds/api";
    private final GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);


    public GameController(GameService gameService){
        this.gameService = gameService;
        setupEndpoints();
    }

    private void setupEndpoints(){

        // POST: Start the game//
        post(API_CONTEXT + "/games", "application/json", (request, response) -> {
            try {
                GamePlayer player = gameService.createNewGame(request.body());
                response.status(201);
                return player;
            } catch(GameService.GameServiceException ex){
                response.status(400);
                logger.error("Failed to create new game", ex);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        // PUT: Join the game//
        put(API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
            try {
                GamePlayer gamePlayer = gameService.joinGame(request.params(":gameId"));
                response.status(200);
                return gamePlayer;
            } catch (GameService.GameServiceException ex) {
                if (ex.getStatusCode() == 1) {
                    logger.error(String.format("Failed to join the game with the given id: %s", request.params(":gameId")), ex);
                    response.status(404);
                } else if (ex.getStatusCode() == 2) {
                    logger.error(String.format("Second player already joined to given id: %s", request.params(":gameId")), ex);
                    response.status(410);
                }
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        // POST: Play the game//
        post(API_CONTEXT + "/games/:gameId/turns", "application/json", (request, response) -> {
            try {
                String playerId = gameService.playGame(request.params(":gameId"), request.body());
                response.status(200);
                return playerId;
            } catch (GameService.GameServiceException ex) {
                JsonObject responseBody = new JsonObject();
                if (ex.getStatusCode() == 1) {
                    logger.error(String.format("Invalid game id: %s",
                            request.params(":gameId")));
                    response.status(404);
                    responseBody.addProperty("reason", "INVALID_GAME_ID");
                } else if (ex.getStatusCode() == 2) {
                    logger.error(String.format("Invalid player id: %s",
                            request.params(":gameId")));
                    response.status(404);
                    responseBody.addProperty("reason", "INVALID_PLAYER_ID");
                } else if (ex.getStatusCode() == 3) {
                    logger.error(String.format("It's not your turn: %s",
                            request.params(":gameId")));
                    response.status(422);
                    responseBody.addProperty("reason", "INCORRECT_TURN");
                } else if (ex.getStatusCode() == 4) {
                    logger.error(String.format("Illegal move: %s",
                            request.params(":gameId")));
                    response.status(422);
                    responseBody.addProperty("reason", "ILLEGAL_MOVE");
                }
                return responseBody;
            }
        }, new JsonTransformer());

        // GET: Get the current state of the board//
        get(API_CONTEXT + "/games/:gameId/board", "application/json", (request, response) -> {
            try {
                List<GamePieces> boardState = gameService.getBoardState(request.params(":gameId"));
                response.status(200);
                return boardState;
            } catch (GameService.GameServiceException ex) {
                logger.error("Invalid gameId");
                response.status(404);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        // GET: Get the current state of the game//
        get(API_CONTEXT + "/games/:gameId/state", "application/json", (request, response) -> {
            try {
               GameState gameState = gameService.getGameState(request.params(":gameId"));
                response.status(200);
                return gameState;
            } catch (GameService.GameServiceException ex) {
                logger.error("Invalid gameId");
                response.status(404);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

    }
}
