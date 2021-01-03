CREATE DATABASE  IF NOT EXISTS `db_room_manager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_room_manager`;
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: db_room_manager
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_account`
--

DROP TABLE IF EXISTS `tb_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_account` (
  `i_id` int NOT NULL AUTO_INCREMENT,
  `vc_domain` varchar(50) NOT NULL,
  `b_has_permissions` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`i_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account`
--

LOCK TABLES `tb_account` WRITE;
/*!40000 ALTER TABLE `tb_account` DISABLE KEYS */;
INSERT INTO `tb_account` VALUES (1,'@isec.alunos.pt',_binary '\0'),(2,'@isec.docentes.pt',_binary '');
/*!40000 ALTER TABLE `tb_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_event`
--

DROP TABLE IF EXISTS `tb_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_event` (
  `i_id` int NOT NULL AUTO_INCREMENT,
  `vc_name` varchar(45) NOT NULL,
  `i_room_id` int NOT NULL,
  `vc_group_name` varchar(50) NOT NULL,
  `vc_creator_username` varchar(50) NOT NULL,
  `d_date_start` datetime NOT NULL,
  `i_duration` int NOT NULL,
  PRIMARY KEY (`i_id`),
  KEY `event_group_idx` (`vc_group_name`),
  KEY `event_room_idx` (`i_room_id`),
  KEY `event_creator_idx` (`vc_creator_username`),
  CONSTRAINT `event_creator` FOREIGN KEY (`vc_creator_username`) REFERENCES `tb_user` (`vc_username`) ON DELETE CASCADE,
  CONSTRAINT `event_group` FOREIGN KEY (`vc_group_name`) REFERENCES `tb_group` (`vc_name`) ON DELETE CASCADE,
  CONSTRAINT `event_room` FOREIGN KEY (`i_room_id`) REFERENCES `tb_room` (`i_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_event`
--

LOCK TABLES `tb_event` WRITE;
/*!40000 ALTER TABLE `tb_event` DISABLE KEYS */;
INSERT INTO `tb_event` VALUES (2,'Conferência',2,'Grupo 2','pedrito@hotmail.com','2021-01-05 20:00:00',60),(11,'Aula GPS',3,'Grupo 3','pedrito@hotmail.com','2021-01-05 18:57:29',120),(12,'Code Academy I',2,'Grupo 1','pedrito@hotmail.com','2021-01-13 18:59:03',45);
/*!40000 ALTER TABLE `tb_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_event_subscription`
--

DROP TABLE IF EXISTS `tb_event_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_event_subscription` (
  `i_id` int NOT NULL AUTO_INCREMENT,
  `i_event_id` int NOT NULL,
  `vc_user_username` varchar(50) NOT NULL,
  `vc_state` varchar(20) NOT NULL,
  PRIMARY KEY (`i_id`),
  KEY `event_idx` (`i_event_id`),
  KEY `user_idx` (`vc_user_username`),
  CONSTRAINT `event` FOREIGN KEY (`i_event_id`) REFERENCES `tb_event` (`i_id`) ON DELETE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`vc_user_username`) REFERENCES `tb_user` (`vc_username`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_event_subscription`
--

LOCK TABLES `tb_event_subscription` WRITE;
/*!40000 ALTER TABLE `tb_event_subscription` DISABLE KEYS */;
INSERT INTO `tb_event_subscription` VALUES (3,2,'asdas','pending'),(4,2,'pedrito','pending'),(5,12,'ajms@hotmail.com','pending'),(6,12,'pedrito','pending'),(7,12,'a21270451@isec.alunos.pt','pending');
/*!40000 ALTER TABLE `tb_event_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_group`
--

DROP TABLE IF EXISTS `tb_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_group` (
  `vc_name` varchar(50) NOT NULL,
  PRIMARY KEY (`vc_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_group`
--

LOCK TABLES `tb_group` WRITE;
/*!40000 ALTER TABLE `tb_group` DISABLE KEYS */;
INSERT INTO `tb_group` VALUES ('Grupo 1'),('Grupo 2'),('Grupo 3');
/*!40000 ALTER TABLE `tb_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_group_member`
--

DROP TABLE IF EXISTS `tb_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_group_member` (
  `vc_group_name` varchar(50) NOT NULL,
  `vc_user_username` varchar(50) NOT NULL,
  PRIMARY KEY (`vc_group_name`,`vc_user_username`),
  KEY `group_idx` (`vc_group_name`),
  KEY `member_idx` (`vc_user_username`),
  CONSTRAINT `group` FOREIGN KEY (`vc_group_name`) REFERENCES `tb_group` (`vc_name`) ON DELETE CASCADE,
  CONSTRAINT `member` FOREIGN KEY (`vc_user_username`) REFERENCES `tb_user` (`vc_username`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_group_member`
--

LOCK TABLES `tb_group_member` WRITE;
/*!40000 ALTER TABLE `tb_group_member` DISABLE KEYS */;
INSERT INTO `tb_group_member` VALUES ('Grupo 1','a21270451@isec.alunos.pt'),('Grupo 1','ajms@hotmail.com'),('Grupo 1','pedrito'),('Grupo 2','asdas'),('Grupo 2','pedrito');
/*!40000 ALTER TABLE `tb_group_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_room`
--

DROP TABLE IF EXISTS `tb_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_room` (
  `i_id` int NOT NULL AUTO_INCREMENT,
  `vc_name` varchar(45) NOT NULL,
  `vc_type` varchar(45) NOT NULL,
  `i_capacity` int NOT NULL,
  PRIMARY KEY (`i_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_room`
--

LOCK TABLES `tb_room` WRITE;
/*!40000 ALTER TABLE `tb_room` DISABLE KEYS */;
INSERT INTO `tb_room` VALUES (1,'Guerra Pratas','Auditório',100),(2,'Rui Borges','Anfiteatro',150),(3,'L3.1','Laboratório',20);
/*!40000 ALTER TABLE `tb_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_room_feature`
--

DROP TABLE IF EXISTS `tb_room_feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_room_feature` (
  `i_id` int NOT NULL AUTO_INCREMENT,
  `i_room_id` int NOT NULL,
  `vc_name` varchar(50) NOT NULL,
  PRIMARY KEY (`i_id`),
  UNIQUE KEY `RoomFeature` (`i_room_id`,`vc_name`),
  KEY `Room_idx` (`i_room_id`),
  CONSTRAINT `Room` FOREIGN KEY (`i_room_id`) REFERENCES `tb_room` (`i_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_room_feature`
--

LOCK TABLES `tb_room_feature` WRITE;
/*!40000 ALTER TABLE `tb_room_feature` DISABLE KEYS */;
INSERT INTO `tb_room_feature` VALUES (1,1,'Ar Condicionado'),(2,1,'Projetor'),(3,2,'Projetor'),(6,3,'Computadores MacOS'),(5,3,'Computadores Windows'),(4,3,'Quadro Interativo');
/*!40000 ALTER TABLE `tb_room_feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `vc_username` varchar(50) NOT NULL,
  `vc_name` varchar(50) NOT NULL,
  `vc_password` varchar(50) NOT NULL,
  `b_permissions` bit(1) NOT NULL,
  PRIMARY KEY (`vc_username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES ('a21270451@isec.alunos.pt','Érica Walberg','823da4223e46ec671a10ea13d7823534',_binary '\0'),('a21270455@isec.alunos.pt','Pedro Afonso','823da4223e46ec671a10ea13d7823534',_binary '\0'),('aaaa@hotmail.com','João','81dc9bdb52d04dc20036dbd8313ed055',_binary ''),('ajms@hotmail.com','António','10f6872a03e0d9ef6281545c2402af86',_binary ''),('ans@isec.docentes.pt','Álvaro Santos','823da4223e46ec671a10ea13d7823534',_binary ''),('asd','aadsa','88a8d500a8761091f721a7ef8348eba1',_binary ''),('asdas','asdas','81dc9bdb52d04dc20036dbd8313ed055',_binary ''),('pedrito','Pedro','81dc9bdb52d04dc20036dbd8313ed055',_binary '\0'),('pedrito@hotmail.com','Pedro','81dc9bdb52d04dc20036dbd8313ed055',_binary '');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-03 20:38:29
