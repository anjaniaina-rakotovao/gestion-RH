import axios from "axios";

const API_URL = "http://localhost:8080/api/rh";

export default {
  getPresences(id) {
    return axios.get(`${API_URL}/presences/${id}`);
  },

  postConge(data) {
    return axios.post(
      "http://localhost:8085/api/rh/conge",
      null,
      {
        params: {
          employeId: data.employeId,
          typeCongeId: data.typeCongeId,
          dateDebut: data.dateDebut,
          dateFin: data.dateFin
        }
      }
    );
  }


};
