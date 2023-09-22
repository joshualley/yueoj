export interface State {
  user: UserState;
}

/**
 * 用户
 */
export class User {
  createTime?: string;
  id?: number;
  updateTime?: string;
  userAvatar?: string;
  userName?: string;
  userProfile?: string;
  userRole?: string;
}

/**
 * 状态
 */
export interface UserState {
  loginUser: User;
}
