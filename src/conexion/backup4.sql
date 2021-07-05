CREATE DATABASE  IF NOT EXISTS `login` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `login`;
-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: login
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `idempleados` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `edad` int DEFAULT NULL,
  `genero` varchar(45) DEFAULT NULL,
  `num_telefono` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idempleados`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'Jared','Puga',20,'M','9861202078'),(2,'Ingrid','Maritnez',19,'F','9861005899');
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario` varchar(45) DEFAULT NULL,
  `contrasenia` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (1,'Jared','mrrobot2001'),(2,'Ingrid','ig15');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `idproductos` int NOT NULL AUTO_INCREMENT,
  `codigobarras` int NOT NULL,
  `nombre_producto` varchar(45) DEFAULT NULL,
  `contenido` varchar(45) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `categoria` varchar(45) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  PRIMARY KEY (`idproductos`,`codigobarras`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,89250,'Montejo','750',30.5,'Mega',192),(2,23165,'Carta Clara','750',35,'Mega',46),(3,28590,'Michelob Ultra','350',18.5,'Lata',41),(4,57778,'Modelo Especial','473',17,'Lata',50),(5,32132,'Victoria','473',23,'Laton',42),(6,98798,'Barrilito','325',11,'Botella',28),(7,74173,'Six Barrilito','325',65,'Six',10),(8,51151,'Corona','355',18,'Lata',47),(9,15353,'Negra Modelo','355',17,'Lata',43),(10,46779,'Modelo Especial','355',16,'Lata',43),(12,77727,'Corona Ligth','355',15,'Lata',46),(13,65456,'Corona Ligera','355',13,'Lata',45),(14,18395,'Corona Extra','743',17,'Laton',48),(15,42050,'Corona Extra','355',15,'Lata',48),(16,68468,'Carta Clara','355',15,'Lata',50),(17,23468,'Bud Light','355',25,'Botella',41),(18,26264,'Pacifico Suave','355',15,'Lata',33),(19,60363,'Victoria Chamoy','473',21,'Laton',44),(20,90181,'Bud Light','355',18,'Lata',41),(21,32375,'Bud Light','1.02',30,'Mega',44),(22,14881,'Victoria','355',15,'Lata',50),(23,47956,'Tecate','1.05',37,'Mega',33),(24,63827,'Sol','355',35,'Laton',45),(25,41388,'Agua','500',12,'Bebidas',38),(26,35497,'Vaso','0',1,'Producto',42),(27,43595,'Chicle','0',1.5,'Producto',77),(29,84566,'Coca-Cola','1',29,'Producto',28),(30,89250,'Montejo PROMO','750',51,'Promo',192),(31,11111,'Victoria PROMO','1.02',57,'Promo',100),(32,23165,'Carta Clara PROMO','1.02',57,'Promo',46),(33,32375,'Bud Ligth PROMO','1.02',58,'Promo',44),(34,1,'Deposito Mega','0',10,'Deposito',-2),(35,2,'Deposito Dev. Mega','0',-10,'Deposito',-3);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promos`
--

DROP TABLE IF EXISTS `promos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promos` (
  `idpromos` int NOT NULL AUTO_INCREMENT,
  `codigobarras` int DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `categoria` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idpromos`),
  UNIQUE KEY `idpromos_UNIQUE` (`idpromos`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promos`
--

LOCK TABLES `promos` WRITE;
/*!40000 ALTER TABLE `promos` DISABLE KEYS */;
INSERT INTO `promos` VALUES (1,74173,'Barrilito','Six');
/*!40000 ALTER TABLE `promos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `idventa` int NOT NULL AUTO_INCREMENT,
  `producto` varchar(45) DEFAULT NULL,
  `categoria` varchar(45) DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`idventa`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES (1,'Agua','Bebidas',1,12),(2,'Victoria','Lata',2,30),(3,'Negra Modelo','Lata',3,51),(4,'Montejo','Mega',4,122),(5,'Pacifico Suave','Lata',4,60),(6,'Victoria','Lata',2,30),(7,'Pacifico Suave','Lata',3,45),(8,'Vaso','Producto',2,2),(9,'Modelo Especial','Lata',2,32),(10,'Victoria Chamoy','Laton',5,105),(11,'Corona','Lata',2,36),(12,'Six Barrilito','Promo',2,130),(13,'Tecate','Mega',8,296),(14,'Pacifico Suave','Lata',1,15),(15,'Vaso','Producto',2,2),(16,'Corona Ligth','Lata',1,15),(17,'Coca-Cola','Producto',2,58),(18,'Negra Modelo','Lata',2,34),(19,'Victoria','Laton',2,46),(20,'Agua','Bebidas',2,24),(21,'Michelob Ultra','Lata',2,37),(22,'Modelo Especial','Lata',1,16),(23,'Pacifico Suave','Lata',2,30),(24,'Carta Clara','Mega',2,70),(25,'Tecate','Mega',2,74),(26,'Pacifico Suave','Lata',2,30),(27,'Modelo Especial','Lata',2,32),(28,'Corona Extra','Lata',2,30),(29,'Victoria','Lata',2,30),(30,'Pacifico Suave','Lata',1,15),(31,'Barrilito','Botella',3,33),(32,'Victoria Chamoy','Laton',1,21),(33,'Carta Clara','Mega',1,35),(34,'Corona Ligera','Lata',2,26),(35,'Negra Modelo','Lata',1,17),(36,'Michelob Ultra','Lata',1,18.5),(37,'Barrilito','Botella',2,22),(38,'Victoria PROMO','Promo',2,114),(39,'Victoria PROMO','Promo',1,57),(40,'Montejo','Mega',1,30.5),(41,'Montejo','Mega',1,30.5),(42,'Montejo PROMO','Mega',2,102),(43,'Montejo','Mega',3,91.5),(44,'Montejo PROMO','Promo',2,102),(45,'Montejo PROMO','Promo',4,102),(46,'Carta Clara PROMO','Promo',4,114),(47,'Six Barrilito','Six',2,65),(48,'Six Barrilito','Six',12,130),(49,'Six Barrilito','Six',6,65),(50,'Six Barrilito','Six',12,130),(51,'Deposito Mega','Deposito',12,20),(52,'Deposito Dev. Mega','Deposito',2,-20),(53,'Deposito Dev. Mega','Deposito',1,-10),(54,'Bud Light','Lata',2,36);
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'login'
--

--
-- Dumping routines for database 'login'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-05  1:40:23
