SET FOREIGN_KEY_CHECKS = 0;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Khoa học máy tính'),(2,'Tài liệu văn phòng'),(3,'Thiết kế & UX');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:25
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpo8h7vwck3icylwdgbhs04jf8` (`user_id`),
  CONSTRAINT `FKpo8h7vwck3icylwdgbhs04jf8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
INSERT INTO `collection` VALUES (1,1),(2,2),(4,6),(5,7);
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `collection_documents`
--

DROP TABLE IF EXISTS `collection_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection_documents` (
  `collection_id` bigint NOT NULL,
  `document_id` bigint NOT NULL,
  PRIMARY KEY (`collection_id`,`document_id`),
  KEY `FKspj0stgy43mw1k15xxtujc6sn` (`document_id`),
  CONSTRAINT `FK12wq1wott7sem9qf2qosvs29l` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `FKspj0stgy43mw1k15xxtujc6sn` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection_documents`
--

LOCK TABLES `collection_documents` WRITE;
/*!40000 ALTER TABLE `collection_documents` DISABLE KEYS */;
INSERT INTO `collection_documents` VALUES (1,1),(4,1),(5,1),(4,3),(1,5),(2,5),(5,5);
/*!40000 ALTER TABLE `collection_documents` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:25
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `document_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKooerxu4oy4q0s0duwk3vtk74q` (`document_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKooerxu4oy4q0s0duwk3vtk74q` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'Bài viết rất hữu ích, cảm ơn bạn!','2026-09-01 13:00:00.000000',1,2),(2,'Có thể cập nhật thêm phần auth không?','2026-09-01 14:10:00.000000',1,3),(3,'Template đẹp và chuyên nghiệp.','2026-09-02 15:00:00.000000',2,1),(4,'Ai đã thiết kế wireframe này?','2026-08-26 09:20:00.000000',4,5),(5,'Mình dùng slide này cho buổi training hôm qua, rất ok.','2026-09-04 10:30:00.000000',6,4),(6,'first cmt','2026-09-06 11:42:55.291792',5,6),(7,'first cmt','2026-09-06 11:49:44.696992',5,6),(8,'first cmt','2026-09-06 11:50:41.402600',5,6),(10,'abc','2026-09-06 12:25:19.337074',5,7),(11,'f','2026-09-06 12:26:20.414709',5,7),(12,'a','2026-09-06 16:07:03.688041',5,6),(13,'p','2026-09-06 16:07:06.069429',5,6),(14,'p','2026-09-06 16:07:06.740820',5,6),(20,'a','2026-09-06 16:16:28.717244',5,6),(21,'a','2026-09-06 16:16:30.604904',5,6),(22,'a','2026-09-06 16:16:34.289425',5,6),(23,'12','2026-09-06 16:16:38.995612',5,6);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `file_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_like` bigint DEFAULT NULL,
  `total_view` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1vwugdy4y8ivgpikjcuojibc0` (`category_id`),
  KEY `FKjhdxdv9sijhujiynqbb5jc010` (`user_id`),
  CONSTRAINT `FK1vwugdy4y8ivgpikjcuojibc0` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKjhdxdv9sijhujiynqbb5jc010` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (1,'2026-09-01 12:00:00.000000','Tổng quan và ví dụ Spring Boot cho người mới','pdf','https://files.example.com/spring-boot-guide.pdf','https://files.example.com/thumbnails/spring-boot-guide.png','Hướng dẫn Spring Boot',21,150,1,1),(2,'2026-09-02 14:20:00.000000','Template PowerPoint cho báo cáo nội bộ','pptx','https://files.example.com/company-presentation.pptx','https://files.example.com/thumbnails/company-presentation.png','Mẫu trình bày công ty',40,243,2,2),(3,'2026-09-03 09:30:00.000000','Các khái niệm cơ bản và ví dụ component','pdf','https://files.example.com/vue-guide.pdf','https://files.example.com/thumbnails/vue-guide.png','Hướng dẫn Vue.js',10,83,1,3),(4,'2026-08-25 16:00:00.000000','Wireframe và hướng dẫn thiết kế','png','https://files.example.com/wireframe-design.png','https://files.example.com/thumbnails/wireframe-design.png','Wireframe UX mẫu',5,60,3,4),(5,'2026-07-10 10:00:00.000000','Hợp đồng mẫu cho dự án nội bộ','docx','https://files.example.com/contract-template.docx','https://files.example.com/thumbnails/contract-template.png','Mẫu hợp đồng',55,363,2,2),(6,'2026-09-04 08:00:00.000000','Slide cho buổi đào tạo kỹ thuật','pptx','https://files.example.com/training-slides.pptx','https://files.example.com/thumbnails/training-slides.png','Bộ slide đào tạo',3,45,2,1),(7,'2026-09-06 04:34:00.259009','','docx','https://res.cloudinary.com/dc5reshvw/raw/upload/v1788644071/i1xadm6b3gspfxmgbu6y','https://res.cloudinary.com/dc5reshvw/image/upload/v1788634633/ppt_xt8skp.png','abc',0,29,1,6),(8,'2026-09-06 13:43:43.390391','','docx','https://res.cloudinary.com/dc5reshvw/raw/upload/v1788677055/cth8oyjqm0vpbr4awxeu','https://res.cloudinary.com/dc5reshvw/image/upload/v1788634633/ppt_xt8skp.png','doccx',0,2,1,6);
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `document_tags`
--

DROP TABLE IF EXISTS `document_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_tags` (
  `document_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`document_id`,`tag_id`),
  KEY `FKl8pxq2mt0yxvg6ukrcx1aijsq` (`tag_id`),
  CONSTRAINT `FK79jlxh3y4p439h000aq1ged5f` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`),
  CONSTRAINT `FKl8pxq2mt0yxvg6ukrcx1aijsq` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_tags`
--

LOCK TABLES `document_tags` WRITE;
/*!40000 ALTER TABLE `document_tags` DISABLE KEYS */;
INSERT INTO `document_tags` VALUES (1,1),(3,1),(7,1),(8,1),(1,2),(6,2),(3,3),(4,4),(7,4),(2,5),(5,5),(6,5),(8,5),(7,6),(8,7);
/*!40000 ALTER TABLE `document_tags` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `document_view`
--

DROP TABLE IF EXISTS `document_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_view` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `document_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs9350fbqlhvmyc4rob5v01o5n` (`document_id`),
  KEY `FK2c5wow1mcwc03w6mxlencpwm` (`user_id`),
  CONSTRAINT `FK2c5wow1mcwc03w6mxlencpwm` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKs9350fbqlhvmyc4rob5v01o5n` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_view`
--

LOCK TABLES `document_view` WRITE;
/*!40000 ALTER TABLE `document_view` DISABLE KEYS */;
INSERT INTO `document_view` VALUES (1,'2026-09-01 12:05:00.000000',1,2),(2,'2026-09-01 12:10:00.000000',1,3),(3,'2026-09-02 14:25:00.000000',2,4),(4,'2026-07-10 11:00:00.000000',5,5),(5,'2026-09-04 08:15:00.000000',6,1),(6,'2026-09-03 09:45:00.000000',3,3),(7,'2026-09-06 04:12:43.599190',5,6),(8,'2026-09-06 04:13:01.963121',5,6),(9,'2026-09-06 04:15:22.127740',5,6),(10,'2026-09-06 04:15:48.595561',5,6),(11,'2026-09-06 04:15:57.086846',5,6),(12,'2026-09-06 04:17:34.772554',5,6),(13,'2026-09-06 04:22:15.736324',5,6),(14,'2026-09-06 04:22:25.500503',5,6),(15,'2026-09-06 04:28:52.332267',5,6),(16,'2026-09-06 04:29:01.969567',1,6),(17,'2026-09-06 04:29:12.831470',1,6),(18,'2026-09-06 04:30:36.092089',1,6),(19,'2026-09-06 04:30:47.334121',1,6),(20,'2026-09-06 04:31:59.515236',1,6),(21,'2026-09-06 04:32:00.648290',1,6),(22,'2026-09-06 04:32:07.783627',1,6),(23,'2026-09-06 04:32:16.732375',1,6),(24,'2026-09-06 04:32:47.355728',1,6),(25,'2026-09-06 04:32:49.550856',1,6),(26,'2026-09-06 04:32:53.771130',1,6),(27,'2026-09-06 04:32:55.630462',1,6),(28,'2026-09-06 04:32:57.631968',1,6),(29,'2026-09-06 04:33:00.265196',1,6),(30,'2026-09-06 04:33:07.977974',1,6),(31,'2026-09-06 04:33:10.986949',1,6),(32,'2026-09-06 04:34:03.469971',7,6),(33,'2026-09-06 04:34:10.868493',7,6),(34,'2026-09-06 04:34:33.279234',7,6),(35,'2026-09-06 04:35:17.449849',7,6),(36,'2026-09-06 04:35:20.695209',7,6),(37,'2026-09-06 04:35:34.029251',7,6),(38,'2026-09-06 04:35:40.023016',7,6),(39,'2026-09-06 04:36:37.283310',7,6),(40,'2026-09-06 04:36:39.101447',7,6),(41,'2026-09-06 04:36:46.932999',7,6),(42,'2026-09-06 04:36:52.959458',7,6),(43,'2026-09-06 04:37:32.623504',7,6),(44,'2026-09-06 04:42:29.226029',7,6),(45,'2026-09-06 04:42:32.540200',7,6),(46,'2026-09-06 04:42:35.731519',7,6),(47,'2026-09-06 04:42:39.598259',7,6),(48,'2026-09-06 04:42:41.413982',7,6),(49,'2026-09-06 04:43:12.397443',7,6),(50,'2026-09-06 04:44:15.937469',7,6),(51,'2026-09-06 04:44:21.879834',7,6),(52,'2026-09-06 04:45:37.284568',7,6),(53,'2026-09-06 04:47:56.128586',7,6),(54,'2026-09-06 04:49:01.134406',7,6),(55,'2026-09-06 04:49:11.943106',7,6),(56,'2026-09-06 04:49:35.211914',7,6),(57,'2026-09-06 11:01:13.150766',5,6),(58,'2026-09-06 11:26:36.622653',5,6),(59,'2026-09-06 11:27:13.001763',5,6),(60,'2026-09-06 11:27:21.353909',5,6),(61,'2026-09-06 11:28:25.933556',5,6),(62,'2026-09-06 11:28:32.830133',5,6),(63,'2026-09-06 11:33:03.298718',5,6),(64,'2026-09-06 11:42:24.886759',5,6),(65,'2026-09-06 11:49:30.230923',5,6),(66,'2026-09-06 11:50:29.839088',5,6),(67,'2026-09-06 12:24:18.780518',1,7),(68,'2026-09-06 12:24:21.015008',3,7),(69,'2026-09-06 12:24:22.706708',5,7),(70,'2026-09-06 12:24:24.380616',2,7),(71,'2026-09-06 12:24:42.565184',5,7),(72,'2026-09-06 12:25:24.614107',5,7),(73,'2026-09-06 12:26:05.230391',5,7),(74,'2026-09-06 12:49:45.371396',1,7),(75,'2026-09-06 12:57:52.186475',1,7),(76,'2026-09-06 13:04:44.891869',1,7),(77,'2026-09-06 13:40:59.612444',5,6),(78,'2026-09-06 13:41:06.719785',2,6),(79,'2026-09-06 13:41:09.247688',1,6),(80,'2026-09-06 13:41:12.374010',3,6),(81,'2026-09-06 13:43:46.845755',8,6),(82,'2026-09-06 14:02:41.591554',5,6),(83,'2026-09-06 14:02:43.553986',5,6),(84,'2026-09-06 14:03:09.585182',5,6),(85,'2026-09-06 14:03:17.585382',5,6),(86,'2026-09-06 14:04:09.135216',2,6),(87,'2026-09-06 14:16:45.193649',1,6),(88,'2026-09-06 14:16:51.469867',1,6),(89,'2026-09-06 14:17:10.018175',1,6),(90,'2026-09-06 14:43:16.146127',7,6),(91,'2026-09-06 15:30:02.003968',7,6),(92,'2026-09-06 15:30:40.725532',7,6),(93,'2026-09-06 15:33:59.237093',1,6),(94,'2026-09-06 15:34:24.980697',7,6),(95,'2026-09-06 15:34:26.851052',3,6),(96,'2026-09-06 15:35:24.937327',8,6),(97,'2026-09-06 16:03:44.376805',5,6),(98,'2026-09-06 16:07:13.983130',5,6),(99,'2026-09-06 16:08:49.328454',5,6),(100,'2026-09-06 16:09:06.039188',5,6),(101,'2026-09-06 16:09:07.533712',5,6),(102,'2026-09-06 16:09:40.772008',5,6),(103,'2026-09-06 16:10:28.769605',5,6),(104,'2026-09-06 16:10:32.901081',5,6),(105,'2026-09-06 16:10:36.206273',5,6),(106,'2026-09-06 16:10:39.458489',5,6),(107,'2026-09-06 16:10:43.780490',5,6),(108,'2026-09-06 16:11:29.034162',5,6),(109,'2026-09-06 16:11:35.559582',5,6),(110,'2026-09-06 16:11:48.063085',5,6),(111,'2026-09-06 16:11:49.651620',5,6),(112,'2026-09-06 16:11:52.220814',5,6),(113,'2026-09-06 16:12:03.871902',5,6),(114,'2026-09-06 16:12:05.430946',5,6),(115,'2026-09-06 16:12:19.972696',5,6),(116,'2026-09-06 16:12:23.857737',5,6),(117,'2026-09-06 16:12:27.299908',5,6),(118,'2026-09-06 16:13:03.796484',5,6),(119,'2026-09-06 16:13:58.390627',5,6),(120,'2026-09-06 16:14:00.380294',5,6),(121,'2026-09-06 16:14:08.064767',5,6),(122,'2026-09-06 16:14:09.608901',5,6),(123,'2026-09-06 16:14:18.795510',5,6),(124,'2026-09-06 16:14:25.229535',5,6),(125,'2026-09-06 16:14:53.032410',5,6),(126,'2026-09-06 16:15:25.123852',5,6),(127,'2026-09-06 16:15:36.515294',5,6),(128,'2026-09-06 16:16:11.574260',5,6),(129,'2026-09-06 16:16:12.862864',5,6),(130,'2026-09-06 16:16:22.053918',5,6),(131,'2026-09-06 16:16:41.772035',5,6);
/*!40000 ALTER TABLE `document_view` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `document_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrwul9kfjdkx3gvhts122lle8k` (`user_id`,`document_id`),
  KEY `FK1v85bhmpskddbt91s345l4ntm` (`document_id`),
  CONSTRAINT `FK1v85bhmpskddbt91s345l4ntm` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`),
  CONSTRAINT `FKi2wo4dyk4rok7v4kak8sgkwx0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (1,'2026-09-01 13:05:00.000000',1,2),(2,'2026-09-01 14:12:00.000000',1,3),(3,'2026-09-02 15:05:00.000000',2,1),(4,'2026-07-11 08:00:00.000000',5,4),(5,'2026-07-11 09:15:00.000000',5,2),(6,'2026-09-04 09:00:00.000000',6,3),(18,'2026-09-06 04:32:13.644767',1,6);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: documentdb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ho` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `ten` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_role` enum('ROLE_ADMIN','ROLE_USER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'https://example.com/avatars/1.png','2026-09-01 09:00:00.000000','minh@example.com','Nguyễn','$2a$10$hashedpass1',_binary '','Minh','ROLE_ADMIN','minh'),(2,'https://example.com/avatars/2.png','2026-09-02 10:15:00.000000','trananh@example.com','Trần','$2a$10$hashedpass2',_binary '','Anh','ROLE_USER','trananh'),(3,'https://example.com/avatars/3.png','2026-09-03 11:30:00.000000','lehoa@example.com','Lê','$2a$10$hashedpass3',_binary '','Hoa','ROLE_USER','lehoa'),(4,'https://example.com/avatars/4.png','2026-09-04 08:45:00.000000','dung@example.com','Phạm','$2a$10$hashedpass4',_binary '','Dung','ROLE_USER','phamdung'),(5,'https://example.com/avatars/5.png','2026-08-20 15:20:00.000000','long@example.com','Võ','$2a$10$hashedpass5',_binary '\0','Long','ROLE_USER','volong'),(6,'https://res.cloudinary.com/dc5reshvw/image/upload/v1788685352/pvrfiwwfbp5mgakn9tmg.jpg','2026-09-06 04:12:38.353513','tranminhanh7825@gmail.com','Minh','$2a$10$MyVMN9NiWA5cuAMPsvbSTuXbPnwESe22MALFlAIYkASTtopAc5i8O',_binary '','Ánh','ROLE_USER','minhanh'),(7,NULL,'2026-09-06 12:23:56.573657','usera@gmail.com','User','$2a$10$DOyNj4aQwMOG1rf40pxOhu7wvoUcuJCviXwviD/YzyyIYCGmU5RuG',_binary '','A','ROLE_USER','usera');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  1:05:26
SET FOREIGN_KEY_CHECKS = 1;
