import { User } from "@/store/type";
import AccessEnum from "./AccessEnum";

/**
 * 用于检测权限
 * @param loginUser
 * @param needAccess
 * @returns
 */
export default function checkAccess(
  loginUser: User,
  needAccess: AccessEnum,
): boolean {
  const access = loginUser.userRole ?? AccessEnum.NotLogin;
  if (needAccess === AccessEnum.NotLogin) {
    return true;
  }
  if (needAccess === AccessEnum.User) {
    // 只要登陆即可
    return !(access === AccessEnum.NotLogin);
  }
  if (needAccess === AccessEnum.Admin) {
    // 需要为管理员
    return access === AccessEnum.Admin;
  }
  return true;
}
