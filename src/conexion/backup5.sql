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
INSERT INTO `log` VALUES (1,'Jared','mrrobot2001'),(2,'Ingrid','ingrid19');
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
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,4009736,'Montejo','1.2',31,'Mega',1),(2,4009736,'Montejo PROMO','1.2',51,'Promo',1),(3,9286857,'Carta Clara','1.2',33,'Mega',32),(4,9286857,'Carta Clara PROMO','1.2',57,'Promo',32),(5,3410763,'Corona Extra','1.2',33,'Mega',9),(6,3410763,'Corona Extra PROMO','1.2',57,'Promo',9),(7,8472745,'Victoria','1.2',33,'Mega',13),(8,8472745,'Victoria PROMO','1.2',57,'Promo',13),(9,6647562,'Modelo Especial','1.2',32,'Mega',7),(10,2151104,'Leon Mega','1.2',31,'Mega',0),(11,6004261,'Bud Light','1.2',33,'Mega',50),(12,6004261,'Bud Light PROMO','1.2',57,'Promo',50),(13,6963739,'Corona Light','1.2',32,'Mega',4),(14,6963739,'Corona Light PROMO','1.2',57,'Promo',4),(15,1067703,'Barril Retornable','325',10,'Medias',82),(16,1067703,'Barril R Six','325',50,'Six',82),(17,1410218,'Barril No Retornable','325',11,'Medias',0),(18,1410218,'Barril NR Six','325',65,'Six',0),(19,9961620,'Michelob Ultra','355',17.5,'Medias',24),(20,9961620,'Michellob Ultra Six','355',100,'Six',24),(21,9739285,'Modelo Especial','355',16.5,'Medias',16),(22,9739285,'Modelo Especia Six','355',96,'Six',16),(23,3273134,'Modelo Noche Especial','355',17,'Medias',0),(24,3273134,'M Noche Especial Six','355',95,'Six',0),(25,6957075,'Modelo Negra Especial','355',16.5,'Medias',23),(26,6957075,'Modelo NE Six','355',96,'Six',23),(27,1621072,'Modelo Trigo','355',16.5,'Medias',19),(28,1621072,'Modelo Trigo Six','355',96,'Six',19),(29,5688085,'Corona Familiar','940',28,'Familiar',13),(30,5688085,'Corona Fam Promo','940',54,'Promo',13),(31,5137062,'Victoria Famiiar','940',28,'Familiar',10),(32,5137062,'Victoria Fam Promo','940',54,'Promo',10),(33,6552965,'Victoria ','210',8,'Cuartitas',139),(34,6552965,'Victoria Doce','210',75,'Doce',139),(35,6552965,'Victoria 24','210',150,'Caja',139),(36,9920227,'Corona Cuartitas','210',8,'Cuartitas',19),(37,9920227,'Corona Doce','210',75,'Doce',19),(38,9920227,'Corona 24','210',150,'Caja',19),(39,6228567,'Bud Light Bote','355',14,'Bote',17),(40,6228567,'Bud Light Six','355',72,'Six',17),(41,6443651,'Pacifico Suave','355',16,'Bote',13),(42,6443651,'Pacifico Suave Six','355',85,'Six',13),(43,8417882,'Corona Extra','355',15,'Bote',38),(44,8417882,'Corona Extra Six','355',76,'Six',38),(45,4130558,'Corona Light','355',15,'Bote',37),(46,4130558,'Corona Light Six','355',76,'Six',37),(47,8691137,'Michelob Ultra Bote','355',17.5,'Bote',7),(48,8691137,'Michelob Ultra Six','355',100,'Six',7),(49,3318688,'Modelo Especial','355',16,'Bote',30),(50,3318688,'Modelo Especial Six','355',88,'Six',30),(51,8217862,'Modelo NE','355',17,'Bote',13),(52,8217862,'Modelo NE Six','355',89,'Six',13),(53,5449586,'Montejo Bote','355',14,'Bote',14),(54,5449586,'Montejo Six','355',70,'Six',14),(55,9092130,'Corona Ligera','355',13,'Bote',15),(56,7847221,'Modelo Negra','355',16,'Bote',0),(57,9964385,'Ultra Piña Hard Seltzer ','355',25,'Bote',5),(58,1838743,'Ultra Mango Hard Seltzer','355',25,'Bote',6),(59,1472466,'Ultra Fresa Hard Seltzer','355',25,'Bote',5),(60,9951334,'Ultra Pepino Hard Setzer','355',25,'Bote',6),(61,8963857,'Bud Light Laton','473',17,'Latones',31),(62,8963857,'Bud Light Six','473',100,'Six',31),(63,8230930,'Corona Extra Laton','473',18,'Latones',38),(64,8230930,'Corona Extra Six','473',100,'Six',38),(65,3487720,'Modelo Especial Laton','473',18,'Latones',30),(66,3487720,'Modelo Especial Six','473',100,'Six',30),(67,3132952,'Victoria Laton','473',18,'Latones',48),(68,3132952,'Victoria Six','473',100,'Six',48),(69,4680359,'Victoria Chelada R','473',21,'Latones',1),(70,4680359,'Victoria CR Six ','473',100,'Six',1),(71,3244804,'Vicky Chamoy ','473',21,'Latones',13),(72,3244804,'Vicky Chamoy Six','473',100,'Six',13),(73,6896466,'Vicky Chelada A','473',21,'Latones',21),(74,6896466,'Vicky CA Six','473',100,'Six',21),(75,8417882,'Corona Extra Doce','355',139,'Doce',38),(76,4130558,'Corona Light Doce','355',129,'Doce',37),(77,2619488,'Stella Artois','330',19,'Media',0),(78,2619488,'Stella Artois Six','330',110,'Six',0),(79,2799066,'Modelo Esp 710','710',26,'710',1),(80,2799066,'Mod E Promo 710','710',50,'Promo',1),(81,5385283,'Bud Light 710','710',26,'710',7),(82,6227220,'Squirt 400','400',9,'Bebidas',0),(83,8105585,'Pepsi 400','400',9,'Bebidas',0),(84,4532068,'Jugo Bida 500','500',11,'Bebidas',0),(85,9415804,'Jugo Bida 250','250',10,'Bebidas',0),(86,7079132,'Red bull ','250',29,'Bebidas',0),(87,4802492,'Triples','500',10,'Bebidas',0),(88,1927398,'Agua 1.5','1.5',11,'Bebidas',0),(89,9698394,'Agua 1','1',10,'Bebidas',0),(90,4435103,'Agua 500','500',8,'Bebidas',0),(91,4083488,'Pepsi 2.5','2.5',30,'Bebidas',0),(92,7289758,'Pepsi 1.5','1.5',20,'Bebidas',0),(93,1313687,'Coca 1.5','1.5',20,'Bebidas',0),(94,6421813,'Coca 2.5','2.5',31,'Bebidas',0),(95,2668055,'7up 1.5','1.5',20,'Bebidas',0),(96,3746862,'Manzanita 1.5','1.5',20,'Bebidas',0),(97,7937963,'Squirt 1.5','1.5',20,'Bebidas',0),(98,6252810,'Agua Mineral 500','500',13,'Bebidas',0),(99,2962043,'Manzanita 400','400',9,'Bebidas',0),(100,2785582,'7up 400','400',9,'Bebidas',0),(101,4421792,'Cigarro ','0',6,'Cigarro',0),(102,6521364,'Sabritas Flaming Hot','45',14,'Botanas',0),(103,1504131,'Sabritas Adobadas','45',14,'Botanas',0),(104,5556627,'Sabritas Originales','45',14,'Botanas',0),(105,1954639,'Rufles Mega Crunch','45',14,'Botanas',0),(106,6946209,'Rufles Queso','45',14,'Botanas',0),(107,4383903,'Rufles Jalapeño','45',14,'Botanas',0),(108,6012692,'Rufles Originales','45',14,'Botanas',0),(109,5637889,'Doritos Incognitas','45',13,'Botanas',0),(110,7270214,'Doritos Diablos','45',13,'Botanas',0),(111,6349786,'Doritos Nacho','45',13,'Botanas',0),(112,3568109,'Doritos Flaming Hot','45',13,'Botanas',0),(113,3330722,'Doritos Pizzerolas','45',13,'Botanas',0),(114,5487295,'Rancheritos','45',13,'Botanas',0),(115,7221444,'Chicharron','45',13,'Botanas',0),(116,8735867,'Churrumaiz ','45',10,'Botanas',0),(117,7135096,'Churrumaiz Flaming Hot','45',10,'Botanas',0),(118,4718247,'Cheetos Nacho','45',10,'Botanas',0),(119,6124369,'Cheetos Flaming Hot','45',10,'Botanas',0),(120,2365113,'Cheetos Torciditos','45',10,'Botanas',0),(121,4360513,'Cheetos Bolita','45',10,'Botanas',0),(122,3695286,'Fritos Sal y Limon','45',12,'Botanas',0),(123,6886188,'Fritos Chorizo','45',12,'Botanas',0),(124,9077701,'Charritos con Jalapeño','300',38,'Botanas',0),(125,8191815,'Palomitas','23',4.5,'Botanas',0),(126,1146596,'Big Mix Flaming','75',14,'Botanas',0),(127,7227137,'Big Mix Clasico','75',14,'Botanas',0),(128,6871851,'Chips Jalapeño','46',15,'Botanas',0),(129,8148725,'Chips Flaming','46',15,'Botanas',0),(130,9030466,'Takis Original','280',14,'Botanas',0),(131,1623754,'Takis Flaming Hot','280',14,'Botanas',0),(132,5144955,'Takis Salsa Brava','280',14,'Botanas',0),(133,2051031,'Palomitas Carameladas','160',10,'Botanas',0),(134,8775296,'Cacahuates Ench Diamante','50',8,'Botanas',0),(135,4166706,'Cacahuates Ench Golden','78',10,'Botanas',0),(136,2246787,'Cacahuate Misaki','50',10,'Botanas',0),(137,6456096,'Cacahuate Casero','20',8,'Botanas',0),(138,5268968,'Pasitas','55',12,'Botanas',0),(139,4838853,'Luneta','55',12,'Botanas',0),(140,1847629,'Gomitas','55',12,'Botanas',0),(141,1193610,'Chocoretas','65',12,'Botanas',0),(142,4563229,'Paleta Payaso','45',12,'Botanas',0),(143,3312407,'Kranky','55',12,'Botanas',0),(144,6141943,'Bubulubu','35',12,'Botanas',0),(145,6288862,'Rellerindos','11',1,'Botanas',0),(146,5340164,'Chicles','0',1,'Botanas',0),(147,8378249,'Paleta Takis','24',3.5,'Botanas',0),(148,2307253,'Agua 4','4',25,'Bebidas',0),(149,1,'Deposito Mega','0',10,'Deposito',0),(150,2,'Deposito Dev. Mega','0',-10,'Deposito Dev',0),(151,3,'Deposito Coca Vidrio','0',10,'Deposito',0),(152,4,'Deposito Dev. Coca Vidrio','0',-10,'Deposito Dev',0),(156,5,'Pago Coca-Cola','0',-50,'Pago',0),(157,6,'Pago Cacahuates','0',-40,'Pago',0),(158,7,'Pago Sabritas','0',-70,'Pago',0),(159,8,'Pago Cigarros','0',-120,'Pago',0);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES (1,'Montejo PROMO','Promo',1,51),(2,'Luneta','Botanas',2,24),(3,'Big Mix Clasico','Botanas',1,14),(4,'Bud Light 710','710',1,26),(5,'Agua Mineral 500','Bebidas',2,26),(6,'Carta Clara PROMO','Promo',1,57),(7,'Modelo Especial','Latones',2,36),(8,'Cigarro ','Cigarro',1,6),(9,'Stella Artois Six','Six',1,110),(10,'Red bull ','Bebidas',1,29),(11,'Mod E Promo 710','Promo',2,100),(12,'Victoria Famiiar','Familiar',2,56),(13,'Deposito Dev. Mega','Deposito Dev',3,-30),(14,'Deposito Dev. Coca Vidrio','Deposito Dev',2,-20),(15,'Bud Light 710','710',2,52),(16,'Corona Extra','Bote',1,15),(17,'Corona Extra Six','Six',1,100),(18,'Jugo Bida 250','Bebidas',2,20),(19,'Doritos Nacho','Botanas',2,26),(20,'Montejo PROMO','Promo',2,102),(21,'Carta Clara PROMO','Promo',1,57),(22,'Corona 24','Caja',1,150),(23,'Corona 24','Caja',1,150),(24,'Corona 24','Caja',1,150),(25,'Corona Doce','Doce',1,75),(26,'7up 1.5','Bebidas',1,20),(27,'Barril R Six','Six',1,50),(28,'Corona 24','Caja',8,1200),(29,'7up 1.5','Bebidas',2,40),(30,'7up 1.5','Bebidas',2,40),(31,'7up 1.5','Bebidas',2,40),(32,'7up 1.5','Bebidas',2,40),(33,'Doritos Diablos','Botanas',1,13),(34,'Doritos Diablos','Botanas',1,13),(35,'7up 1.5','Bebidas',1,20),(36,'Doritos Diablos','Botanas',1,13),(37,'Doritos Diablos','Botanas',1,13),(38,'Mod E Promo 710','Promo',6,300),(39,'Modelo Esp 710','710',1,26),(40,'Agua 1','Bebidas',3,30),(41,'Big Mix Clasico','Botanas',2,28),(42,'Big Mix Clasico','Botanas',3,42),(43,'Cigarro ','Cigarro',115,690),(44,'Cigarro ','Cigarro',1,6),(45,'Bud Light 710','710',2,52),(46,'Bud Light 710','710',2,52),(47,'Barril Retornable','Medias',40,400),(48,'Barril Retornable','Medias',2,20),(49,'Victoria Famiiar','Familiar',7,196),(50,'Victoria Famiiar','Familiar',1,28),(51,'Corona','Cuartitas',10,80),(52,'Corona','Cuartitas',5,40),(53,'Michelob Ultra','Bote',5,87.5),(54,'Michelob Ultra','Bote',2,35),(55,'Victoria Chelada R','Latones',1,21),(56,'Carta Clara','Mega',25,825),(57,'Carta Clara','Mega',1,33),(58,'Carta Clara PROMO','Promo',1,57),(59,'Montejo PROMO','Promo',21,1071),(60,'Montejo PROMO','Promo',1,51),(61,'M Noche Especial Six','Six',1,95),(62,'Modelo Noche Especial','Medias',1,17),(63,'Victoria Doce','Doce',11,825),(64,'Victoria ','Cuartitas',7,56),(65,'Deposito Mega','Deposito',1,10),(66,'Deposito Mega','Deposito',2,20),(67,'Deposito Mega','Deposito',2,20),(71,'Pago Cigarros','Pago',1,-40),(72,'Pago Sabritas','Pago',1,-70),(73,'Pago Cacahuates','Pago',1,-40),(74,'Pago Coca-Cola','Pago',1,-50),(75,'Pago Sabritas','Pago',1,-70),(76,'Pago Cigarros','Pago',1,-120),(77,'Deposito Mega','Deposito',2,20);
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

-- Dump completed on 2021-07-08  5:14:12
