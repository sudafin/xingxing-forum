-- 创建数据库
DROP DATABASE IF EXISTS xingxingforum;
CREATE DATABASE xingxingforum;
USE xingxingforum;

-- 用户表
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  avatar VARCHAR(255),
  nickname VARCHAR(50),
  bio VARCHAR(255),
  last_login_at TIMESTAMP,
  is_active TINYINT(1) DEFAULT 1,
  is_admin TINYINT(1) DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 版块表
CREATE TABLE forums (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  parent_id BIGINT,
  post_count INT DEFAULT 0,
  thread_count INT DEFAULT 0,
  last_post_id BIGINT,
  last_post_time TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_parent_id (parent_id)
);

-- 主题表
CREATE TABLE threads (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  forum_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  subject VARCHAR(100) NOT NULL,
  content LONGTEXT NOT NULL,
  images JSON,
  replies INT DEFAULT 0,
  views INT DEFAULT 0,
  likes INT DEFAULT 0,
  is_top TINYINT(1) DEFAULT 0,
  is_essence TINYINT(1) DEFAULT 0,
  is_locked TINYINT(1) DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_forum_id (forum_id),
  INDEX idx_user_id (user_id)
);

-- 评论表
CREATE TABLE comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  thread_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  parent_id BIGINT,
  content LONGTEXT NOT NULL,
  images JSON,
  likes INT DEFAULT 0,
  is_first TINYINT(1) DEFAULT 0,
  is_deleted TINYINT(1) DEFAULT 0,
  is_edited TINYINT(1) DEFAULT 0,
  edit_time TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_thread_id (thread_id),
  INDEX idx_user_id (user_id),
  INDEX idx_parent_id (parent_id)
);

-- 点赞表
CREATE TABLE likes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  thread_id BIGINT,
  comment_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id),
  INDEX idx_thread_id (thread_id),
  INDEX idx_comment_id (comment_id)
);

-- 关注表
CREATE TABLE follows (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  follower_id BIGINT NOT NULL,
  followed_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_follower_id (follower_id),
  INDEX idx_followed_id (followed_id)
);

-- 会话表
CREATE TABLE conversations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type ENUM('private', 'group') NOT NULL,
  name VARCHAR(100),
  avatar VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 会话成员表
CREATE TABLE conversation_members (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  conversation_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role ENUM('admin', 'member') DEFAULT 'member',
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_conversation_id (conversation_id),
  INDEX idx_user_id (user_id)
);

-- 消息表
CREATE TABLE messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  conversation_id BIGINT NOT NULL,
  from_user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  type ENUM('text', 'image', 'file') DEFAULT 'text',
  is_read TINYINT(1) DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_conversation_id (conversation_id),
  INDEX idx_from_user_id (from_user_id)
);

-- 通知表  
CREATE TABLE notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  type ENUM('reply', 'like', 'system') NOT NULL,
  source_id BIGINT NOT NULL,
  source_type ENUM('thread', 'comment') NOT NULL,
  is_read TINYINT(1) DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id)
);

-- 收藏表
CREATE TABLE favorites (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  thread_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id),
  INDEX idx_thread_id (thread_id)
);

-- 浏览历史表
CREATE TABLE histories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  thread_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id),
  INDEX idx_thread_id (thread_id)
);

-- 草稿表
CREATE TABLE drafts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  subject VARCHAR(100),
  content LONGTEXT,
  images JSON,
  type ENUM('thread', 'post') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id)
);



-- 插入用户数据
INSERT INTO users (username, email, password_hash, avatar, nickname, bio, last_login_at, is_active, is_admin) VALUES
('john', 'john@example.com', 'hash1', 'avatar1.jpg', 'John', 'I am John.', '2023-06-01 10:00:00', 1, 0),
('jane', 'jane@example.com', 'hash2', 'avatar2.jpg', 'Jane', 'I am Jane.', '2023-06-02 11:00:00', 1, 0),
('admin', 'admin@example.com', 'hash3', 'avatar3.jpg', 'Admin', 'I am an admin.', '2023-06-03 12:00:00', 1, 1);

-- 插入版块数据
INSERT INTO forums (name, description, parent_id, post_count, thread_count, last_post_id, last_post_time) VALUES
('General', 'General discussion', NULL, 0, 0, NULL, NULL),
('News', 'Latest news', NULL, 0, 0, NULL, NULL),
('Sports', 'Sports discussion', 1, 0, 0, NULL, NULL),
('Technology', 'Technology discussion', 1, 0, 0, NULL, NULL);

-- 插入主题数据
INSERT INTO threads (forum_id, user_id, subject, content, images, replies, views, likes, is_top, is_essence, is_locked) VALUES
(1, 1, 'Hello world!', 'This is my first post.', NULL, 0, 0, 0, 0, 0, 0),
(1, 2, 'Welcome!', 'Welcome to our forum!', NULL, 0, 0, 0, 0, 0, 0),
(3, 1, 'About football', 'Let''s talk about football.', '["image1.jpg", "image2.jpg"]', 0, 0, 0, 0, 0, 0),
(4, 2, 'New iPhone', 'What do you think of the new iPhone?', NULL, 0, 0, 0, 0, 0, 0);

-- 插入评论数据
INSERT INTO comments (thread_id, user_id, parent_id, content, images, likes, is_first, is_deleted, is_edited, edit_time) VALUES
(1, 2, NULL, 'Great post!', NULL, 0, 0, 0, 0, NULL),
(1, 1, 1, 'Thank you!', NULL, 0, 0, 0, 0, NULL),
(3, 2, NULL, 'I love football!', NULL, 0, 0, 0, 0, NULL),
(4, 1, NULL, 'I think it''s overpriced.', NULL, 0, 0, 0, 0, NULL);

-- 插入点赞数据
INSERT INTO likes (user_id, thread_id, comment_id) VALUES
(1, 2, NULL),
(2, 1, NULL),
(1, NULL, 1),
(2, NULL, 3);

-- 插入关注数据
INSERT INTO follows (follower_id, followed_id) VALUES
(1, 2),
(2, 1);

-- 插入会话数据
INSERT INTO conversations (type, name, avatar) VALUES
('private', NULL, NULL),
('group', 'Group chat', 'group_avatar.jpg');

-- 插入会话成员数据
INSERT INTO conversation_members (conversation_id, user_id, role) VALUES
(1, 1, 'member'),
(1, 2, 'member'),
(2, 1, 'admin'),
(2, 2, 'member'),
(2, 3, 'member');

-- 插入消息数据
INSERT INTO messages (conversation_id, from_user_id, content, type, is_read) VALUES
(1, 1, 'Hello Jane!', 'text', 1),
(1, 2, 'Hi John!', 'text', 1),
(2, 1, 'Welcome to the group chat!', 'text', 0),
(2, 2, 'Thanks for adding me!', 'text', 0);

-- 插入通知数据
INSERT INTO notifications (user_id, type, source_id, source_type, is_read) VALUES  
(1, 'reply', 1, 'comment', 1),
(2, 'like', 1, 'thread', 1),
(1, 'like', 1, 'comment', 0),
(2, 'reply', 3, 'comment', 0);

-- 插入收藏数据
INSERT INTO favorites (user_id, thread_id) VALUES
(1, 2),
(2, 3);

-- 插入浏览历史数据
INSERT INTO histories (user_id, thread_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);

-- 插入草稿数据
INSERT INTO drafts (user_id, subject, content, images, type) VALUES
(1, 'Draft subject 1', 'This is a thread draft.', NULL, 'thread'),
(2, NULL, 'This is a post draft.', '["draft_image.jpg"]', 'post');