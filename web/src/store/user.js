export default {
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    is_login: false,
  },
  getters: {
  },
  mutations: {
    updateUser(state, user) {
        state.id = user.id;
        state.username = user.username;
        state.photo = user.photo;
        state.token = user.token;
    },

    updateToken(state, token) {
        state.token = token;
    }
  },
  actions: {
  },
  modules: {
  }
}
