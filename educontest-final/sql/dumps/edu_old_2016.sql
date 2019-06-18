# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: educontest
# Generation Time: 2016-10-17 15:12:09 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table grade
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `grade_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `order` int(11) NOT NULL,
  PRIMARY KEY (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;

INSERT INTO `grade` (`grade_id`, `created_at`, `updated_at`, `name`, `order`)
VALUES
	(1,'2015-05-07 13:11:07','2015-05-07 13:11:07','Math 3',0),
	(2,'2015-05-07 13:11:07','2015-05-07 13:11:07','Math 4',1),
	(3,'2015-05-07 13:11:07','2015-05-07 13:11:07','Math 5',2),
	(4,'2015-05-07 13:11:07','2015-05-07 13:11:07','Math 6',3),
	(5,'2015-05-07 13:11:07','2015-05-07 13:11:07','Math 7',4),
	(6,'2015-05-07 13:11:07','2015-05-07 13:11:07','Pre-Algebra',5),
	(7,'2015-05-07 13:11:07','2015-05-07 13:11:07','Algebra 1',6),
	(8,'2015-05-07 13:11:07','2015-05-07 13:11:07','Algebra 2',7),
	(9,'2015-05-07 13:11:07','2015-05-07 13:11:07','Geometry',8),
	(10,'2015-05-07 13:11:07','2015-05-07 13:11:07','Advanced Math',9);

/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
