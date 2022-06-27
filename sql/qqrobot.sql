/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : qqrobot

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 26/06/2022 17:54:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bot
-- ----------------------------
DROP TABLE IF EXISTS `bot`;
CREATE TABLE `bot`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `friend_qq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq账号',
  `bot_qq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'bot账号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `QQ`(`friend_qq`) USING BTREE COMMENT '唯一QQ'
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for genshin
-- ----------------------------
DROP TABLE IF EXISTS `genshin`;
CREATE TABLE `genshin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `qq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq号',
  `uid` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原神uid',
  `cookie` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '米游社cookie',
  `automatic` int NULL DEFAULT NULL COMMENT '0：不自动  1：自动签到',
  `time` bigint NULL DEFAULT NULL COMMENT '绑定时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UID`(`uid`) USING BTREE,
  INDEX `QQ`(`qq`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mihoyo
-- ----------------------------
DROP TABLE IF EXISTS `mihoyo`;
CREATE TABLE `mihoyo`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `qq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq号',
  `uid` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '米游社通行证id',
  `cookie` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '米哈游账号管理cookie https://user.mihoyo.com/#/login/captcha',
  `token` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '米哈游账号管理：服务器验证码',
  `automatic` int NULL DEFAULT NULL COMMENT '0：不自动  1：自动签到',
  `time` bigint NULL DEFAULT NULL COMMENT '绑定时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `QQ`(`qq`) USING BTREE,
  UNIQUE INDEX `UID`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
