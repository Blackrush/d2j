/*
Navicat MySQL Data Transfer

Source Server         : Huge
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : d2j_game1

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-12-29 21:12:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `rights` tinyint(4) NOT NULL,
  `community` smallint(6) NOT NULL,
  `enabledChannels` varchar(20) NOT NULL DEFAULT '',
  `lastConnection` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastAddress` varchar(15) NOT NULL DEFAULT '',
  `muted` tinyint(1) NOT NULL DEFAULT '0',
  `notifyFriendsOnConnect` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', 'test', 'test', '1', '0', 'i*#$p%', '2011-12-24 18:59:58', '127.0.0.1', '0', '0');
INSERT INTO `accounts` VALUES ('2', 'test', 'test', '1', '0', 'i*#$p%', '2011-11-27 16:50:04', '127.0.0.1', '0', '0');
INSERT INTO `accounts` VALUES ('3', 'Blackrush', 'test', '3', '0', 'i*#$p%@', '2011-11-27 16:17:48', '127.0.0.1', '0', '0');

-- ----------------------------
-- Table structure for `characters`
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ownerId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `breed` int(11) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `color1` int(11) NOT NULL,
  `color2` int(11) NOT NULL,
  `color3` int(11) NOT NULL,
  `skin` smallint(6) NOT NULL,
  `size` smallint(6) NOT NULL,
  `level` smallint(6) NOT NULL,
  `experience` bigint(20) NOT NULL,
  `energy` smallint(6) NOT NULL,
  `kamas` int(11) NOT NULL,
  `statsPoints` smallint(6) NOT NULL,
  `spellsPoints` smallint(6) NOT NULL,
  `currentMap` int(11) NOT NULL,
  `currentOrientation` tinyint(4) NOT NULL,
  `currentCell` smallint(6) NOT NULL,
  `memorizedMap` int(11) NOT NULL,
  `life` smallint(6) NOT NULL,
  `actionPoints` smallint(6) NOT NULL,
  `movementPoints` smallint(6) NOT NULL,
  `vitality` smallint(6) NOT NULL,
  `wisdom` smallint(6) NOT NULL,
  `strength` smallint(6) NOT NULL,
  `intelligence` smallint(6) NOT NULL,
  `chance` smallint(6) NOT NULL,
  `agility` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_c_ownid` (`ownerId`),
  CONSTRAINT `fk_c_ownid` FOREIGN KEY (`ownerId`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of characters
-- ----------------------------
INSERT INTO `characters` VALUES ('1', '1', 'Hozoxojehy', '8', '1', '-1', '-1', '-1', '81', '100', '1', '0', '10000', '1000000', '0', '0', '7411', '1', '335', '7411', '48', '6', '3', '0', '0', '0', '0', '0', '0');
INSERT INTO `characters` VALUES ('2', '2', 'Vokaqaluzat', '8', '0', '-1', '-1', '-1', '80', '100', '1', '0', '10000', '1000000', '0', '0', '7411', '1', '335', '7411', '48', '6', '3', '0', '0', '0', '0', '0', '0');
INSERT INTO `characters` VALUES ('3', '3', 'quqom', '10', '0', '-1', '-1', '-1', '100', '100', '1', '0', '10000', '1000000', '0', '0', '7411', '1', '335', '7411', '42', '6', '3', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `items`
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` bigint(20) NOT NULL,
  `template` int(11) NOT NULL,
  `owner` bigint(20) NOT NULL,
  `effects` text NOT NULL,
  `position` smallint(6) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of items
-- ----------------------------
INSERT INTO `items` VALUES ('1', '39', '1', '7e,2', '-1', '1');

-- ----------------------------
-- Table structure for `spell_characters`
-- ----------------------------
DROP TABLE IF EXISTS `spell_characters`;
CREATE TABLE `spell_characters` (
  `id` bigint(20) NOT NULL,
  `character` bigint(20) NOT NULL,
  `spell` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of spell_characters
-- ----------------------------
INSERT INTO `spell_characters` VALUES ('1', '1', '141', '1', '1');
INSERT INTO `spell_characters` VALUES ('2', '1', '143', '3', '1');
INSERT INTO `spell_characters` VALUES ('3', '1', '142', '2', '1');
INSERT INTO `spell_characters` VALUES ('4', '2', '141', '1', '1');
INSERT INTO `spell_characters` VALUES ('5', '2', '143', '3', '1');
INSERT INTO `spell_characters` VALUES ('6', '2', '142', '2', '1');
INSERT INTO `spell_characters` VALUES ('7', '3', '200', '3', '1');
INSERT INTO `spell_characters` VALUES ('8', '3', '193', '2', '1');
INSERT INTO `spell_characters` VALUES ('9', '3', '183', '1', '1');
