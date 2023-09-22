export default (status: number) => {
  switch (status) {
    case 0:
      return "等待中";
    case 1:
      return "判题中";
    case 2:
      return "成功";
    case 3:
      return "失败";
  }
};
