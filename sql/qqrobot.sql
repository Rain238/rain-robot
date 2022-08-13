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

 Date: 10/08/2022 17:27:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alicia_all
-- ----------------------------
DROP TABLE IF EXISTS `alicia_all`;
CREATE TABLE `alicia_all`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19349 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_cat
-- ----------------------------
DROP TABLE IF EXISTS `alicia_cat`;
CREATE TABLE `alicia_cat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2061 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_mp
-- ----------------------------
DROP TABLE IF EXISTS `alicia_mp`;
CREATE TABLE `alicia_mp`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1829 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_pc
-- ----------------------------
DROP TABLE IF EXISTS `alicia_pc`;
CREATE TABLE `alicia_pc`  (
  `id` mediumint NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1084 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_top
-- ----------------------------
DROP TABLE IF EXISTS `alicia_top`;
CREATE TABLE `alicia_top`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3397 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_xing
-- ----------------------------
DROP TABLE IF EXISTS `alicia_xing`;
CREATE TABLE `alicia_xing`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 505 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alicia_yin
-- ----------------------------
DROP TABLE IF EXISTS `alicia_yin`;
CREATE TABLE `alicia_yin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `U`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1820 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
