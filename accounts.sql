/*
Navicat MySQL Data Transfer

Source Server         : Huge
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : d2j_login

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-12-11 16:06:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `question` varchar(255) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `rights` smallint(6) NOT NULL DEFAULT '1',
  `community` smallint(6) NOT NULL,
  `refreshNeeded` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', 'test', 'test', 'test', 'test', 'test', '1', '0', '0');
INSERT INTO `accounts` VALUES ('2', 'test2', 'test', 'test', 'test', 'test', '1', '0', '0');
INSERT INTO `accounts` VALUES ('3', 'blackrush', 'test', 'Blackrush', 'test', 'test', '3', '0', '0');
