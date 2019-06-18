# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: educontest_test
# Generation Time: 2016-12-27 21:45:11 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table billing_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `billing_address`;

CREATE TABLE `billing_address` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) NOT NULL,
  `address1` varchar(100) DEFAULT '',
  `address2` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `zip` varchar(20) DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `billing_address` WRITE;
/*!40000 ALTER TABLE `billing_address` DISABLE KEYS */;

INSERT INTO `billing_address` (`id`, `school_id`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
VALUES
	(1,1,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,2,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,3,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,4,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,5,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-14 16:39:27','2016-10-14 16:39:27'),
	(6,16,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-11-30 10:22:16','2016-11-30 10:22:16'),
	(7,17,'125 N. Main St. Biling','STE #2 Biling','Greenville Biling','SC Biling','29601 Biling','2016-11-30 10:23:02','2016-11-30 10:23:02'),
	(8,18,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 14:59:56','2016-12-19 14:59:56'),
	(9,19,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 15:00:08','2016-12-19 15:00:08'),
	(10,20,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 15:03:21','2016-12-19 15:03:21');

/*!40000 ALTER TABLE `billing_address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table competition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `competition`;

CREATE TABLE `competition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `grade_id` int(11) DEFAULT NULL,
  `year_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;

INSERT INTO `competition` (`id`, `grade_id`, `year_id`, `created_at`, `updated_at`)
VALUES
	(11,1,10,NULL,NULL),
	(12,2,10,NULL,NULL),
	(13,3,10,NULL,NULL),
	(14,4,10,NULL,NULL),
	(15,5,10,NULL,NULL),
	(16,6,10,NULL,NULL),
	(17,7,10,NULL,NULL),
	(18,8,10,NULL,NULL),
	(19,9,10,NULL,NULL),
	(20,10,10,NULL,NULL);

/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table grade
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `order` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;

INSERT INTO `grade` (`id`, `name`, `order`, `created_at`, `updated_at`)
VALUES
	(1,'Math 3',0,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(2,'Math 4',1,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(3,'Math 5',2,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(4,'Math 6',3,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(5,'Math 7',4,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(6,'Pre-Algebra',5,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(7,'Algebra 1',6,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(8,'Algebra 2',7,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(9,'Geometry',8,'2015-05-07 13:11:07','2015-05-07 13:11:07'),
	(10,'Advanced Math',9,'2015-05-07 13:11:07','2015-05-07 13:11:07');

/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table invoice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `invoice`;

CREATE TABLE `invoice` (
  `invoiceId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolYearId` int(11) DEFAULT NULL,
  `amount_paid` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  PRIMARY KEY (`invoiceId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table invoice_item_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `invoice_item_type`;

CREATE TABLE `invoice_item_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table league
# ------------------------------------------------------------

DROP TABLE IF EXISTS `league`;

CREATE TABLE `league` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `league` WRITE;
/*!40000 ALTER TABLE `league` DISABLE KEYS */;

INSERT INTO `league` (`id`, `description`)
VALUES
	(1,'Catholic Math League'),
	(2,'Educontest');

/*!40000 ALTER TABLE `league` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table migrations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `migrations`;

CREATE TABLE `migrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



# Dump of table school
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school`;

CREATE TABLE `school` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `contact` varchar(100) DEFAULT NULL,
  `billing_name` varchar(100) DEFAULT NULL,
  `purchase_order` varchar(10) DEFAULT NULL,
  `school_email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `league_id` int(11) DEFAULT NULL,
  `old_school_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `league_id` (`league_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;

INSERT INTO `school` (`id`, `name`, `contact`, `billing_name`, `purchase_order`, `school_email`, `phone`, `fax`, `league_id`, `old_school_id`, `created_at`, `updated_at`)
VALUES
	(1,'Riverside High','Jim','Jim','12312','dmcnight@d.com','12321','123',1,12,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(4,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(7,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 09:55:59','2016-11-30 09:55:59'),
	(8,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',1,NULL,'2016-11-30 09:56:24','2016-11-30 09:56:24'),
	(9,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:04:51','2016-11-30 10:04:51'),
	(10,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:08:21','2016-11-30 10:08:21'),
	(11,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:08:32','2016-11-30 10:08:32'),
	(12,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:13:58','2016-11-30 10:13:58'),
	(13,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:14:11','2016-11-30 10:14:11'),
	(14,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:16:22','2016-11-30 10:16:22'),
	(15,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:16:33','2016-11-30 10:16:33'),
	(16,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:22:16','2016-11-30 10:22:16'),
	(17,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-11-30 10:23:02','2016-11-30 10:23:02'),
	(18,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',1,NULL,'2016-12-19 14:59:56','2016-12-19 14:59:56'),
	(19,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',1,NULL,'2016-12-19 15:00:08','2016-12-19 15:00:08'),
	(20,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',1,NULL,'2016-12-19 15:03:21','2016-12-19 15:03:21');

/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school_competition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_competition`;

CREATE TABLE `school_competition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_year_id` int(11) DEFAULT NULL,
  `competition_id` int(11) DEFAULT NULL,
  `division` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comp-fk` (`competition_id`),
  KEY `sy_index` (`school_year_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school_competition` WRITE;
/*!40000 ALTER TABLE `school_competition` DISABLE KEYS */;

INSERT INTO `school_competition` (`id`, `school_year_id`, `competition_id`, `division`, `created_at`, `updated_at`)
VALUES
	(1,4,11,NULL,'2016-12-21 10:11:45','2016-12-21 10:11:45'),
	(2,4,14,NULL,'2016-12-21 10:11:47','2016-12-21 10:11:47');

/*!40000 ALTER TABLE `school_competition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school_test
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_test`;

CREATE TABLE `school_test` (
  `schoolTesttId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) DEFAULT NULL,
  `testId` int(11) DEFAULT NULL,
  PRIMARY KEY (`schoolTesttId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table school_year
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_year`;

CREATE TABLE `school_year` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) DEFAULT NULL,
  `year_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school_year` WRITE;
/*!40000 ALTER TABLE `school_year` DISABLE KEYS */;

INSERT INTO `school_year` (`id`, `school_id`, `year_id`, `created_at`, `updated_at`)
VALUES
	(1,4,9,NULL,NULL),
	(2,4,8,NULL,NULL),
	(4,4,10,'2016-12-20 10:38:25','2016-12-20 10:38:25');

/*!40000 ALTER TABLE `school_year` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table shipping_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `shipping_address`;

CREATE TABLE `shipping_address` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `shipping_address` WRITE;
/*!40000 ALTER TABLE `shipping_address` DISABLE KEYS */;

INSERT INTO `shipping_address` (`id`, `school_id`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
VALUES
	(1,1,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,2,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,3,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,4,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,5,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-14 16:39:27','2016-10-14 16:39:27'),
	(6,16,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-11-30 10:22:16','2016-11-30 10:22:16'),
	(7,17,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-11-30 10:23:02','2016-11-30 10:23:02'),
	(8,18,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 14:59:56','2016-12-19 14:59:56'),
	(9,19,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 15:00:08','2016-12-19 15:00:08'),
	(10,20,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-12-19 15:03:21','2016-12-19 15:03:21');

/*!40000 ALTER TABLE `shipping_address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table student
# ------------------------------------------------------------

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `studentId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolCompetitionId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `public` tinyint(1) DEFAULT NULL,
  `test1_score` int(2) DEFAULT '0',
  `test2_score` int(2) DEFAULT '0',
  `test3_score` int(2) DEFAULT '0',
  `test4_score` int(2) DEFAULT '0',
  `total_score` int(2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`studentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;

INSERT INTO `student` (`studentId`, `schoolCompetitionId`, `name`, `public`, `test1_score`, `test2_score`, `test3_score`, `test4_score`, `total_score`, `created_at`, `updated_at`)
VALUES
	(2,1,'Davey Smart really smart --',1,25,0,0,0,NULL,'2016-11-18 11:49:34','2016-11-18 11:49:49'),
	(3,1,'Justin',1,12,0,0,0,NULL,'2016-11-21 15:53:14','2016-11-21 15:53:14'),
	(5,1,'Davey Not smart',0,12,0,0,0,NULL,'2016-11-29 14:54:57','2016-11-29 14:54:57'),
	(6,1,'Davey Not smart',0,25,0,0,0,NULL,'2016-11-29 14:55:16','2016-11-29 14:55:16');

/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table test
# ------------------------------------------------------------

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `testId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `yearId` int(11) DEFAULT NULL,
  `gradeId` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`testId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;

INSERT INTO `test` (`testId`, `yearId`, `gradeId`, `created_at`, `updated_at`)
VALUES
	(1,10,1,NULL,NULL),
	(2,10,2,NULL,NULL),
	(3,10,3,NULL,NULL),
	(4,10,4,NULL,NULL),
	(5,10,5,NULL,NULL),
	(6,10,6,NULL,NULL),
	(7,10,7,NULL,NULL),
	(8,10,8,NULL,NULL),
	(9,10,9,NULL,NULL),
	(10,10,10,NULL,NULL);

/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `admin` tinyint(2) DEFAULT NULL,
  `sign_in_count` int(11) NOT NULL,
  `last_sign_in_at` datetime NOT NULL,
  `current_sign_in_ip` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`userId`),
  KEY `school_id` (`schoolId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`userId`, `schoolId`, `email`, `password`, `admin`, `sign_in_count`, `last_sign_in_at`, `current_sign_in_ip`, `created_at`, `updated_at`)
VALUES
	(1,4,'davidmcnight@gmail.com','wMHSz5GPHftqJ7AaHwf9yCgyZWn+e+XS5iB/uTfevno=',0,0,'0000-00-00 00:00:00',NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(2,4,'justin.pidcock@3foldx.com','vSKkTZNfA9wrqYDX93Cbuhqddyqbt17MMU/9Rnzzlz0=',0,0,'0000-00-00 00:00:00',NULL,'2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table year
# ------------------------------------------------------------

DROP TABLE IF EXISTS `year`;

CREATE TABLE `year` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `year` int(4) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `open` tinyint(1) DEFAULT NULL,
  `openTest` tinyint(1) DEFAULT NULL,
  `resultsArePublic` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `year` WRITE;
/*!40000 ALTER TABLE `year` DISABLE KEYS */;

INSERT INTO `year` (`id`, `description`, `year`, `created_at`, `updated_at`, `open`, `openTest`, `resultsArePublic`)
VALUES
	(1,'2007-2008',2007,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(2,'2008-2009',2008,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(3,'2009-2010',2009,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(4,'2010-2011',2010,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(5,'2011-2012',2011,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(6,'2012-2013',2012,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(7,'2013-2014',2013,'2015-05-07 13:11:07','2015-05-07 13:11:07',0,NULL,0),
	(8,'2014-2015',2014,'2015-05-07 13:11:07','2015-11-02 04:43:49',0,NULL,0),
	(9,'2015-2016',2015,'2015-05-09 04:19:46','2016-03-22 03:43:28',0,NULL,1),
	(10,'2016-2017',2016,'2016-04-24 15:28:44','2016-04-24 15:28:44',1,1,0);

/*!40000 ALTER TABLE `year` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
