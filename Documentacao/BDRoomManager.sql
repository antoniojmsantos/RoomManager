CREATE DATABASE  IF NOT EXISTS `BDRoomManager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `BDRoomManager`;

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


DROP TABLE IF EXISTS `tbEvent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbEvent` (
  `iID` int NOT NULL,
  `vcName` varchar(45) NOT NULL,
  `iID_Room` int NOT NULL,
  `vcName_Group` varchar(50) NOT NULL,
  `dDateStart` datetime NOT NULL,
  `tDuration` time NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_ID_ROOM_idx` (`iID_Room`),
  KEY `FK_NAME_GROUP_idx` (`vcName_Group`),
  CONSTRAINT `FK_ID_ROOM` FOREIGN KEY (`iID_Room`) REFERENCES `tbRoom` (`iID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_NAME_GRUPO` FOREIGN KEY (`vcName_Group`) REFERENCES `tbGroup` (`vcName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `tbGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbGroup` (
  `vcName` varchar(50) NOT NULL,
  PRIMARY KEY (`vcName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `tbEventSubscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbEventSubscription` (
  `iID` int NOT NULL,
  `iID_Event` int NOT NULL,
  `vcUsername` varchar(50) NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_USERNAME_idx` (`vcUsername`),
  KEY `FK_ID_EVENT_idx` (`iID_Event`),
  CONSTRAINT `FK_ID_EVENT` FOREIGN KEY (`iID_Event`) REFERENCES `tbEvent` (`iID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_USERNAME` FOREIGN KEY (`vcUsername`) REFERENCES `tbUser` (`vcUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `tbGroupMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbGroupMember` (
  `iID` int NOT NULL,
  `vcName_Group` varchar(50) NOT NULL,
  `vcUsername` varchar(50) NOT NULL,
  PRIMARY KEY (`iID`),
  KEY `FK_M_NAME_GROUP_idx` (vcName_Group),
  KEY `FK_M_USERNAME_idx` (`vcUsername`),
  CONSTRAINT `FK_M_NAME_GROUP` FOREIGN KEY (`vcName_Group`) REFERENCES `tbGroup` (`vcName`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_M_USERNAME` FOREIGN KEY (`vcUsername`) REFERENCES `tbUser` (`vcUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `tbRoom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbRoom` (
  `iID` int NOT NULL,
  `vcName` varchar(45) NOT NULL,
  `vcDetails` varchar(500) DEFAULT NULL,
  `iCapacity` int NOT NULL,
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
