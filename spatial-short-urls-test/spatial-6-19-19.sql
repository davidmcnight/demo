# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.23)
# Database: spatial
# Generation Time: 2019-06-19 23:50:13 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table url
# ------------------------------------------------------------

DROP TABLE IF EXISTS `url`;

CREATE TABLE `url` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `short_url` varchar(255) DEFAULT NULL,
  `original_url` varchar(1000) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `url` WRITE;
/*!40000 ALTER TABLE `url` DISABLE KEYS */;

INSERT INTO `url` (`id`, `short_url`, `original_url`, `createdAt`, `updatedAt`)
VALUES
	(2,'b83017fc:::dmcsl','https://www.mlbshop.com/t-3080+d-15024+c-909?_s=EML19_MLB1_108_09A_1_FSNM_1764674_188_&utm_medium=email&vap=1&utm_source=MKTG&utm_position=BC1MS_B&utm_content=&SFMC=1764674_3514_50_154739c2bc9e2e5dded744925a67246c','2019-06-20 06:17:35','2019-06-20 06:17:35');

/*!40000 ALTER TABLE `url` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table url_click
# ------------------------------------------------------------

DROP TABLE IF EXISTS `url_click`;

CREATE TABLE `url_click` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url_id` int(11) DEFAULT NULL,
  `user_agent` varchar(1000) DEFAULT NULL,
  `host` varchar(1000) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `url_click` WRITE;
/*!40000 ALTER TABLE `url_click` DISABLE KEYS */;

INSERT INTO `url_click` (`id`, `url_id`, `user_agent`, `host`, `ip`, `createdAt`, `updatedAt`)
VALUES
	(1,2,NULL,'localhost:5000','::1','2019-06-20 06:48:20','2019-06-20 06:48:20'),
	(2,2,'PostmanRuntime/7.6.0','localhost:5000','::1','2019-06-20 06:48:57','2019-06-20 06:48:57'),
	(3,2,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36','localhost:5000','::1','2019-06-20 06:49:18','2019-06-20 06:49:18');

/*!40000 ALTER TABLE `url_click` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
