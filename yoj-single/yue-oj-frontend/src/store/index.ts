import { createStore } from "vuex";
import user from "./user";
import { State } from "./type";

const store = createStore<State>({
  modules: {
    user: user,
  },
});

export default store;
