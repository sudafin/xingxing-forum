-- 创建数据库
DROP DATABASE IF EXISTS xingxingforum;
CREATE DATABASE xingxingforum;
USE xingxingforum;

-- 用户表
create table users
(
    id            bigint auto_increment
        primary key,
    email         varchar(100)                         not null,
    password      varchar(255)                         null,
    avatar        varchar(255)                         null,
    nickname      varchar(50)                          null,
    bio           varchar(255)                         null,
    last_login_at timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    is_active     tinyint(1) default 1                 null,
    is_admin      tinyint(1) default 0                 null,
    created_at    timestamp  default CURRENT_TIMESTAMP not null,
    updated_at    timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    sex           tinyint                              null comment '性别',
    ip_address    varchar(10)                          null comment 'ip地址',
    address       varchar(50)                          null comment '个人地址',
    level         tinyint                              null comment '等级',
    birthday      timestamp                            null comment '生日',
    profession    varchar(20)                          null comment '职业',
    school        varchar(20)                          null,
    constraint email
        unique (email)
);
-- 版块表
CREATE TABLE forums
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(50) NOT NULL,
    description    VARCHAR(255),
    parent_id      BIGINT,
    post_count     INT       DEFAULT 0,
    thread_count   INT       DEFAULT 0,
    last_post_id   BIGINT,
    last_post_time TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id)
);

-- 新增用户-版块关系表
CREATE TABLE user_forum_relations
(
    user_id       BIGINT NOT NULL COMMENT '用户ID',
    forum_id      BIGINT NOT NULL COMMENT '版块ID',
    relation_type TINYINT   DEFAULT 1 COMMENT '关系类型（1: 普通关注 2: 版主等）',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, forum_id),
    INDEX idx_user (user_id),
    INDEX idx_forum (forum_id)
) COMMENT ='用户与版块的关联关系表（支持关注/版主等关系）';

-- 主题表
CREATE TABLE threads
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    forum_id   BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    subject    VARCHAR(100) NOT NULL,
    content    LONGTEXT     NOT NULL,
    images     JSON,
    replies    INT        DEFAULT 0,
    views      INT        DEFAULT 0,
    likes      INT        DEFAULT 0,
    is_top     TINYINT(1) DEFAULT 0,
    is_essence TINYINT(1) DEFAULT 0,
    is_locked  TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_forum_id (forum_id),
    INDEX idx_user_id (user_id)
);

-- 评论表
CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    thread_id  BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    parent_id  BIGINT,
    content    LONGTEXT NOT NULL,
    images     JSON,
    likes      INT        DEFAULT 0,
    is_first   TINYINT(1) DEFAULT 0,
    is_deleted TINYINT(1) DEFAULT 0,
    is_edited  TINYINT(1) DEFAULT 0,
    edit_time  TIMESTAMP,
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_thread_id (thread_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id)
);

-- 点赞表
CREATE TABLE likes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    thread_id  BIGINT,
    comment_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_thread_id (thread_id),
    INDEX idx_comment_id (comment_id)
);

-- 关注表
CREATE TABLE follows
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    followed_id BIGINT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_follower_id (follower_id),
    INDEX idx_followed_id (followed_id)
);

-- 会话表
CREATE TABLE conversations
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    type       ENUM ('private', 'group') NOT NULL,
    name       VARCHAR(100),
    avatar     VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 会话成员表
CREATE TABLE conversation_members
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    role            ENUM ('admin', 'member') DEFAULT 'member',
    joined_at       TIMESTAMP                DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_user_id (user_id)
);

-- 消息表
CREATE TABLE messages
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT NOT NULL,
    from_user_id    BIGINT NOT NULL,
    content         TEXT   NOT NULL,
    type            ENUM ('text', 'image', 'file') DEFAULT 'text',
    is_read         TINYINT(1)                     DEFAULT 0,
    created_at      TIMESTAMP                      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_from_user_id (from_user_id)
);

-- 通知表  
CREATE TABLE notifications
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT                           NOT NULL,
    type        ENUM ('reply', 'like', 'system') NOT NULL,
    source_id   BIGINT                           NOT NULL,
    source_type ENUM ('thread', 'comment')       NOT NULL,
    is_read     TINYINT(1) DEFAULT 0,
    created_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
);

-- 收藏表
CREATE TABLE favorites
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    thread_id  BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_thread_id (thread_id)
);

-- 浏览历史表
CREATE TABLE histories
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    thread_id  BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_thread_id (thread_id)
);

-- 草稿表
CREATE TABLE drafts
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT                  NOT NULL,
    subject    VARCHAR(100),
    content    LONGTEXT,
    images     JSON,
    type       ENUM ('thread', 'post') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
);
