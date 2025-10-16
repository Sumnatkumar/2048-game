import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/game';

const gameService = {
  async createNewGame(boardSize = 4) {
    const response = await axios.post(`${API_BASE_URL}/new?boardSize=${boardSize}`);
    return response.data;
  },

  async makeMove(gameId, direction) {
    const response = await axios.post(`${API_BASE_URL}/${gameId}/move?direction=${direction}`);
    return response.data;
  },

  async getGameState(gameId) {
    const response = await axios.get(`${API_BASE_URL}/${gameId}`);
    return response.data;
  },

  async restartGame(gameId, boardSize = 4) {
    const response = await axios.post(`${API_BASE_URL}/${gameId}/restart?boardSize=${boardSize}`);
    return response.data;
  }
};

export default gameService;