/*
 Navicat Premium Data Transfer

 Source Server         : 低代码
 Source Server Type    : MySQL
 Source Server Version : 90100 (9.1.0)
 Source Host           : 139.196.147.52:3306
 Source Schema         : lowcodeDemo

 Target Server Type    : MySQL
 Target Server Version : 90100 (9.1.0)
 File Encoding         : 65001

 Date: 20/02/2025 15:38:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Compatibility
-- ----------------------------
DROP TABLE IF EXISTS `Compatibility`;
CREATE TABLE `Compatibility`  (
  `deviceId` int NOT NULL,
  `versionId` int NOT NULL,
  PRIMARY KEY (`deviceId`, `versionId`) USING BTREE,
  INDEX `fk_version`(`versionId` ASC) USING BTREE,
  CONSTRAINT `fk_device` FOREIGN KEY (`deviceId`) REFERENCES `device` (`deviceId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_version` FOREIGN KEY (`versionId`) REFERENCES `devicetypeversion` (`versionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of Compatibility
-- ----------------------------

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `applicationId` int NOT NULL,
  `applicationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `applicationDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sceneId` int NULL DEFAULT NULL,
  `domainId` int NULL DEFAULT NULL,
  PRIMARY KEY (`applicationId`) USING BTREE,
  INDEX `fk_application_domain`(`domainId` ASC) USING BTREE,
  INDEX `fk_application_scene`(`sceneId` ASC) USING BTREE,
  CONSTRAINT `fk_application_domain` FOREIGN KEY (`domainId`) REFERENCES `domain` (`domainId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_application_scene` FOREIGN KEY (`sceneId`) REFERENCES `scene` (`sceneId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application
-- ----------------------------

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `deviceId` int NOT NULL AUTO_INCREMENT,
  `deviceCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceTypeId` int NULL DEFAULT NULL,
  `versionNumber` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `manufacturer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`deviceId`) USING BTREE,
  INDEX `fk_devicetype_device`(`deviceTypeId` ASC) USING BTREE,
  CONSTRAINT `fk_devicetype_device` FOREIGN KEY (`deviceTypeId`) REFERENCES `devicetype` (`deviceTypeId`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (31, 'CoffeeMakerA', '咖啡机器人A', 38, '1.0.0', 'AService');
INSERT INTO `device` VALUES (32, 'CoffeeMakerB', '咖啡机器人B', 38, '1.0.0', 'BService');

-- ----------------------------
-- Table structure for deviceBinding
-- ----------------------------
DROP TABLE IF EXISTS `deviceBinding`;
CREATE TABLE `deviceBinding`  (
  `deviceTypeId` int NOT NULL,
  `domainId` int NOT NULL,
  PRIMARY KEY (`deviceTypeId`, `domainId`) USING BTREE,
  INDEX `fk_domain_biding`(`domainId` ASC) USING BTREE,
  CONSTRAINT `fk_devicetype_biding` FOREIGN KEY (`deviceTypeId`) REFERENCES `devicetype` (`deviceTypeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_domain_biding` FOREIGN KEY (`domainId`) REFERENCES `domain` (`domainId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of deviceBinding
-- ----------------------------
INSERT INTO `deviceBinding` VALUES (20, 1);
INSERT INTO `deviceBinding` VALUES (38, 1);
INSERT INTO `deviceBinding` VALUES (40, 2);

-- ----------------------------
-- Table structure for deviceOperation
-- ----------------------------
DROP TABLE IF EXISTS `deviceOperation`;
CREATE TABLE `deviceOperation`  (
  `operationNumber` int NOT NULL,
  `operationCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `operationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceTypeNumber` int NOT NULL,
  PRIMARY KEY (`operationNumber`) USING BTREE,
  INDEX `fk_device_operation`(`deviceTypeNumber` ASC) USING BTREE,
  CONSTRAINT `fk_device_operation` FOREIGN KEY (`deviceTypeNumber`) REFERENCES `devicetype` (`deviceTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of deviceOperation
-- ----------------------------

-- ----------------------------
-- Table structure for deviceService
-- ----------------------------
DROP TABLE IF EXISTS `deviceService`;
CREATE TABLE `deviceService`  (
  `serviceNumber` int NOT NULL,
  `serviceCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `serviceName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceTypeNumber` int NOT NULL,
  PRIMARY KEY (`serviceNumber`) USING BTREE,
  INDEX `fk_device_service`(`deviceTypeNumber` ASC) USING BTREE,
  CONSTRAINT `fk_device_service` FOREIGN KEY (`deviceTypeNumber`) REFERENCES `devicetype` (`deviceTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of deviceService
-- ----------------------------

-- ----------------------------
-- Table structure for devicetype
-- ----------------------------
DROP TABLE IF EXISTS `devicetype`;
CREATE TABLE `devicetype`  (
  `deviceTypeId` int NOT NULL AUTO_INCREMENT,
  `deviceTypeCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceTypeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `imgPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isPublish` tinyint(1) NOT NULL,
  PRIMARY KEY (`deviceTypeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devicetype
-- ----------------------------
INSERT INTO `devicetype` VALUES (20, 'Conditioner', '空调', '/images/deviceType/Conditioner/Conditioner.png', 1);
INSERT INTO `devicetype` VALUES (21, 'SmokeDetector', '烟感器', '/images/deviceType/SmokeDetector/SmokeDetector.png', 1);
INSERT INTO `devicetype` VALUES (38, 'CoffeeMaker', '咖啡机器人', '/images/deviceType/CoffeeMaker/CoffeeMaker.png', 1);
INSERT INTO `devicetype` VALUES (40, 'PUMP', '水泵', '/images/deviceType/PUMP/PUMP.png', 1);

-- ----------------------------
-- Table structure for devicetypeversion
-- ----------------------------
DROP TABLE IF EXISTS `devicetypeversion`;
CREATE TABLE `devicetypeversion`  (
  `versionId` int NOT NULL,
  `versionCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deviceTypeId` int NULL DEFAULT NULL,
  PRIMARY KEY (`versionId`) USING BTREE,
  INDEX `fk_devicetype_version`(`deviceTypeId` ASC) USING BTREE,
  CONSTRAINT `fk_devicetype_version` FOREIGN KEY (`deviceTypeId`) REFERENCES `devicetype` (`deviceTypeId`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devicetypeversion
-- ----------------------------

-- ----------------------------
-- Table structure for domain
-- ----------------------------
DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain`  (
  `domainId` int NOT NULL AUTO_INCREMENT,
  `domainCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `domainName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `domainDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`domainId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domain
-- ----------------------------
INSERT INTO `domain` VALUES (1, 'SmartBuilding', '智慧楼宇', 'This is a smart building');
INSERT INTO `domain` VALUES (2, 'SmartMine', '智慧矿山', 'This is a smart mine');

-- ----------------------------
-- Table structure for register
-- ----------------------------
DROP TABLE IF EXISTS `register`;
CREATE TABLE `register`  (
  `deviceId` int NOT NULL,
  `sceneId` int NOT NULL,
  `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IPaddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `port` int NULL DEFAULT NULL,
  PRIMARY KEY (`deviceId`, `sceneId`) USING BTREE,
  INDEX `fk_scene_register`(`sceneId` ASC) USING BTREE,
  CONSTRAINT `fk_device_register` FOREIGN KEY (`deviceId`) REFERENCES `device` (`deviceId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_scene_register` FOREIGN KEY (`sceneId`) REFERENCES `scene` (`sceneId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of register
-- ----------------------------
INSERT INTO `register` VALUES (31, 2, 'HTTP', '10.128.101.23', 8080);
INSERT INTO `register` VALUES (32, 2, 'TCP/IP', '10.128.101.26', 7270);

-- ----------------------------
-- Table structure for scene
-- ----------------------------
DROP TABLE IF EXISTS `scene`;
CREATE TABLE `scene`  (
  `sceneId` int NOT NULL,
  `sceneCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sceneName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sceneDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `domainId` int NOT NULL,
  PRIMARY KEY (`sceneId`) USING BTREE,
  INDEX `fk_scene_domain`(`domainId` ASC) USING BTREE,
  CONSTRAINT `fk_scene_domain` FOREIGN KEY (`domainId`) REFERENCES `domain` (`domainId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scene
-- ----------------------------
INSERT INTO `scene` VALUES (1, 'CrossingBuilding', '第二学科交叉楼', 'This is The second interdisciplinary building', 1);
INSERT INTO `scene` VALUES (2, 'PhysicalBuilding', '物理楼', 'This is physical building', 1);

SET FOREIGN_KEY_CHECKS = 1;
