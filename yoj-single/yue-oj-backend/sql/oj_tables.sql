-- 创建库
create database if not exists yue_oj_db;

-- 切换库
use yue_oj_db;

-- 问题表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    answer      text                               null comment '题目答案',
    submitNumb  int      default 0                 not null comment '提交数',
    acceptedNum int      default 0                 not null comment '通过数',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    judgeConfig text                               null comment '判题配置 json对象',
    judgeCase   text                               null comment '判题用例 json数组',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

/**
  判题信息：
  judgeInfo: {
    "message": "程序执行信息",
    "time": 1000, // 执行时间
    "memory": 1000 // 内存消耗
  }
  判题枚举：
  Accepted: 成功
  Wrong Answer: 答案错误
  Compile Error: 编译错误
  Memory Limit Exceeded: 内存溢出
  Time Limit Exceeded: 超时
  Presentation Error: 展示错误
  Output Exceeded: 输出溢出
  Waiting: 等待中
  Dangerous Operation: 危险操作
  Runtime Error: 运行错误
  System Error: 系统错误
 */
-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(255)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    status     int      default 0                 not null comment '状态，0-待判题、1-判题中、2-成功、3-失败',
    judgeInfo  text                               not null comment '判题信息(json 对象)',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交' collate = utf8mb4_unicode_ci;
