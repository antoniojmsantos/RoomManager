CREATE DATABASE  IF NOT EXISTS `BDRoomManager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `BDRoomManager`;
-- MySQL dump 10.13  Distrib 8.0.22, for macos10.15 (x86_64)
--
-- Host: localhost    Database: BDRoomManager
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
-- Table structure for table `tbEvento`
--

DROP TABLE IF EXISTS `tbEvento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbEvento` (
  `iID` int NOT NULL,
  `vcNome` varchar(45) NOT NULL,
  `iID_Sala` int NOT NULL,
  `vc_Nome_Grupo` varchar(50) NOT NULL,
  `dDataInicio` datetime NOT NULL,
  `tDuracao` time NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_ID_SALA_idx` (`iID_Sala`),
  KEY `FK_NOME_GRUPO_idx` (`vc_Nome_Grupo`),
  CONSTRAINT `FK_ID_SALA` FOREIGN KEY (`iID_Sala`) REFERENCES `tbSala` (`iID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_NOME_GRUPO` FOREIGN KEY (`vc_Nome_Grupo`) REFERENCES `tbGrupo` (`vcNome`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbGrupo`
--

DROP TABLE IF EXISTS `tbGrupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbGrupo` (
  `vcNome` varchar(50) NOT NULL,
  PRIMARY KEY (`vcNome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbInscricaoEvento`
--

DROP TABLE IF EXISTS `tbInscricaoEvento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbInscricaoEvento` (
  `iID` int NOT NULL,
  `iID_Evento` int NOT NULL,
  `vcUsername` varchar(50) NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_USERNAME_idx` (`vcUsername`),
  KEY `FK_ID_EVENTO_idx` (`iID_Evento`),
  CONSTRAINT `FK_ID_EVENTO` FOREIGN KEY (`iID_Evento`) REFERENCES `tbEvento` (`iID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_USERNAME` FOREIGN KEY (`vcUsername`) REFERENCES `tbUser` (`vcUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbMembroGrupo`
--

DROP TABLE IF EXISTS `tbMembroGrupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbMembroGrupo` (
  `iID` int NOT NULL,
  `vcNome_Grupo` varchar(50) NOT NULL,
  `vcUsername` varchar(50) NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_M_NOME_GRUPO_idx` (`vcNome_Grupo`),
  KEY `FK_M_USERNAME_idx` (`vcUsername`),
  CONSTRAINT `FK_M_NOME_GRUPO` FOREIGN KEY (`vcNome_Grupo`) REFERENCES `tbGrupo` (`vcNome`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_M_USERNAME` FOREIGN KEY (`vcUsername`) REFERENCES `tbUser` (`vcUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbSala`
--

DROP TABLE IF EXISTS `tbSala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbSala` (
  `iID` int NOT NULL,
  `vcNome` varchar(45) NOT NULL,
  `vcCaracteristicas` varchar(500) DEFAULT NULL,
  `iLotacao` int NOT NULL,
  PRIMARY KEY (`iID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbUser`
--

DROP TABLE IF EXISTS `tbUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbUser` (
  `vcUsername` varchar(50) NOT NULL,
  `vcNome` varchar(50) NOT NULL,
  `vcPassword` varchar(50) NOT NULL,
  `bNivel` bit(1) NOT NULL,
  PRIMARY KEY (`vcUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-14 15:08:52
