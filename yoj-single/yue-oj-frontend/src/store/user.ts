import AccessEnum from "@/access/AccessEnum";
import { UserControllerService } from "@/api";
import { State, User, UserState } from "./type";
import { ActionContext } from "vuex";

const state: UserState = {
  loginUser: {
    userName: "未登录",
    userRole: AccessEnum.NotLogin,
  } as User,
};

const getters = {};

const mutations = {
  updateUser(state: UserState, payload: User) {
    // console.log("更新用户信息为：", payload);
    state.loginUser = payload;
  },
};
const actions = {
  async getLoginUser(context: ActionContext<UserState, State>) {
    // 远程请求获取登录用户信息
    const resp = await UserControllerService.getLoginUserUsingGet();
    if (resp.code === 0) {
      // console.log(resp.data);
      context.commit("updateUser", resp.data);
    } else {
      context.commit("updateUser", {
        ...state.loginUser,
        role: AccessEnum.NotLogin,
      });
    }
  },
  updateLoginUser(context: ActionContext<UserState, State>, payload: User) {
    context.commit("updateUser", payload);
  },
};

const modules = {};

export default {
  state,
  getters,
  mutations,
  actions,
  modules,
};
