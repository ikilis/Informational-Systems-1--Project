-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: baza3
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `filijala`
--

DROP TABLE IF EXISTS `filijala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filijala` (
  `IdFil` int NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdFil`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filijala`
--

LOCK TABLES `filijala` WRITE;
/*!40000 ALTER TABLE `filijala` DISABLE KEYS */;
INSERT INTO `filijala` VALUES (1,'Fil1','1',1),(2,'Fil2','adresa2',2),(3,'Fil3','adresa 3',3),(4,'Fil4','adr4',4);
/*!40000 ALTER TABLE `filijala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `komitent`
--

DROP TABLE IF EXISTS `komitent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `komitent` (
  `IdKom` int NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komitent`
--

LOCK TABLES `komitent` WRITE;
/*!40000 ALTER TABLE `komitent` DISABLE KEYS */;
INSERT INTO `komitent` VALUES (1,'Kom1','Nnova',3),(2,'k2','2',1),(3,'Kom3','adresaK3',2),(4,'Kom4','adresa 4',3),(5,'adr5','Kom5',3);
/*!40000 ALTER TABLE `komitent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `IdMes` int NOT NULL,
  `PostanskiBroj` int NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdMes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
INSERT INTO `mesto` VALUES (1,12000,'Pozarevac'),(2,11000,'Beograd'),(3,13000,'Novi Sad'),(4,16000,'Leskovac');
/*!40000 ALTER TABLE `mesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racun`
--

DROP TABLE IF EXISTS `racun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racun` (
  `IdRac` int NOT NULL,
  `Stanje` float NOT NULL,
  `DozvMinus` float NOT NULL,
  `Status` char(1) NOT NULL,
  `Datum` date NOT NULL,
  `Vreme` time NOT NULL,
  `BrTransakcija` int NOT NULL,
  `IdKom` int NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdRac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racun`
--

LOCK TABLES `racun` WRITE;
/*!40000 ALTER TABLE `racun` DISABLE KEYS */;
INSERT INTO `racun` VALUES (1,0,10,'U','2022-02-24','19:27:20',0,1,1),(2,-212,100,'U','2022-02-24','19:28:01',24,1,1),(3,-710,1000,'A','2022-02-24','19:36:22',8,1,1),(4,110,10,'A','2022-02-24','19:36:52',2,2,1),(5,10,100,'A','2022-02-24','19:36:57',1,2,1),(6,20,1000,'A','2022-02-24','19:37:00',1,2,1);
/*!40000 ALTER TABLE `racun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `IdTrans` int NOT NULL,
  `Datum` date NOT NULL,
  `Vreme` time NOT NULL,
  `Iznos` float NOT NULL,
  `Tip` char(1) NOT NULL,
  `RedniBr` int NOT NULL,
  `Svrha` varchar(45) NOT NULL,
  `IdFil` int DEFAULT NULL,
  `RacunSa` int DEFAULT NULL,
  `RacunNa` int DEFAULT NULL,
  PRIMARY KEY (`IdTrans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
INSERT INTO `transakcija` VALUES (1,'2022-02-24','19:28:43',100,'I',1,'Isplata1',1,2,NULL),(2,'2022-02-24','19:38:02',10,'P',1,'pronos1',NULL,3,4),(3,'2022-02-24','19:39:10',100,'U',2,'uplata1',1,NULL,2),(4,'2022-02-24','20:46:37',100,'I',2,'sI2',1,3,NULL),(5,'2022-02-24','20:46:39',100,'I',3,'sI2',1,3,NULL),(6,'2022-02-24','20:46:40',100,'I',4,'sI2',1,3,NULL),(7,'2022-02-24','20:46:41',100,'I',5,'sI2',1,3,NULL),(8,'2022-02-24','20:46:54',100,'I',3,'sI2',1,2,NULL),(9,'2022-02-24','20:46:55',100,'I',4,'sI2',1,2,NULL),(10,'2022-02-24','20:46:56',100,'I',5,'sI2',1,2,NULL),(11,'2022-02-24','20:46:57',100,'I',6,'sI2',1,2,NULL),(12,'2022-02-24','20:46:59',100,'I',7,'sI2',1,2,NULL),(13,'2022-02-24','20:47:01',100,'I',8,'sI2',1,2,NULL),(14,'2022-02-24','20:47:02',100,'I',9,'sI2',1,2,NULL),(15,'2022-02-24','20:47:02',100,'I',10,'sI2',1,2,NULL),(16,'2022-02-24','20:47:02',100,'I',11,'sI2',1,2,NULL),(17,'2022-02-24','20:47:21',100,'I',12,'sI2',1,2,NULL),(18,'2022-02-24','20:47:46',100,'I',6,'sI3',1,3,NULL),(19,'2022-02-24','20:47:55',100,'I',13,'sI3',1,2,NULL),(20,'2022-02-24','20:48:04',100,'I',14,'sI3',1,2,NULL),(21,'2022-02-24','20:49:45',100,'I',7,'onako',2,3,NULL),(22,'2022-02-24','20:49:56',100,'I',15,'onako',2,2,NULL),(23,'2022-02-24','20:50:07',10,'I',16,'onako',2,2,NULL),(24,'2022-02-24','20:50:08',10,'I',17,'onako',2,2,NULL),(25,'2022-02-24','20:50:08',10,'I',18,'onako',2,2,NULL),(26,'2022-02-24','20:50:09',10,'I',19,'onako',2,2,NULL),(27,'2022-02-24','20:50:09',10,'I',20,'onako',2,2,NULL),(28,'2022-02-24','20:50:19',20,'I',21,'onako',2,2,NULL),(29,'2022-02-24','20:52:23',140,'I',22,'uBlok',1,2,NULL),(30,'2022-02-24','20:52:24',140,'I',23,'uBlok',1,2,NULL),(31,'2022-02-25','10:50:16',2,'U',24,'2',2,NULL,2),(32,'2022-02-25','13:18:37',20,'U',1,'svrha',1,NULL,6),(33,'2022-02-25','13:19:02',10,'U',1,'svrha',2,NULL,5),(34,'2022-02-25','14:09:20',100,'P',8,'pren',NULL,3,4);
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-25 14:47:32
