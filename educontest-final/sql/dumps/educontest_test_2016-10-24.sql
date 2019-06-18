# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: educontest_test
# Generation Time: 2016-10-24 20:38:15 +0000
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
  `billingAddressId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) NOT NULL,
  `address1` varchar(100) DEFAULT '',
  `address2` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `zip` varchar(20) DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`billingAddressId`),
  KEY `school_id` (`schoolId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `billing_address` WRITE;
/*!40000 ALTER TABLE `billing_address` DISABLE KEYS */;

INSERT INTO `billing_address` (`billingAddressId`, `schoolId`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
VALUES
	(1,1,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,2,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,3,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,4,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,5,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `billing_address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table competition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `competition`;

CREATE TABLE `competition` (
  `competitionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `yearId` int(11) DEFAULT NULL,
  `gradeId` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`competitionId`),
  KEY `year-fk` (`yearId`),
  KEY `grade-fk` (`gradeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;

INSERT INTO `competition` (`competitionId`, `yearId`, `gradeId`, `created_at`, `updated_at`)
VALUES
	(1,10,1,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(2,10,2,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(3,10,3,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(4,10,4,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(5,10,5,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(6,10,6,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(7,10,7,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(8,10,8,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(9,10,9,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(10,10,10,'0000-00-00 00:00:00','0000-00-00 00:00:00'),
	(11,8,10,'0000-00-00 00:00:00','0000-00-00 00:00:00');

/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table grade
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `gradeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `order` int(11) NOT NULL,
  PRIMARY KEY (`gradeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;

INSERT INTO `grade` (`gradeId`, `created_at`, `updated_at`, `name`, `order`)
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
  `leagueId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`leagueId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `league` WRITE;
/*!40000 ALTER TABLE `league` DISABLE KEYS */;

INSERT INTO `league` (`leagueId`, `description`, `created_at`, `updated_at`)
VALUES
	(1,'Catholic Math League','',''),
	(2,'Educontest','','');

/*!40000 ALTER TABLE `league` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school`;

CREATE TABLE `school` (
  `schoolId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `contact` varchar(100) DEFAULT NULL,
  `billingName` varchar(100) DEFAULT NULL,
  `purchaseOrder` varchar(10) DEFAULT NULL,
  `schoolEmail` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `leagueId` int(11) DEFAULT NULL,
  `old_school_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`schoolId`),
  KEY `league_id` (`leagueId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;

INSERT INTO `school` (`schoolId`, `name`, `contact`, `billingName`, `purchaseOrder`, `schoolEmail`, `phone`, `fax`, `leagueId`, `old_school_id`, `created_at`, `updated_at`)
VALUES
	(1,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,'Greenville High 2','Justin Pidcock','Justin Pidcock','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school_competition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_competition`;

CREATE TABLE `school_competition` (
  `schoolCompetitionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) DEFAULT NULL,
  `competitionId` int(11) DEFAULT NULL,
  `division` int(11) DEFAULT NULL,
  PRIMARY KEY (`schoolCompetitionId`),
  KEY `school-fk` (`schoolId`),
  KEY `comp-fk` (`competitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school_competition` WRITE;
/*!40000 ALTER TABLE `school_competition` DISABLE KEYS */;

INSERT INTO `school_competition` (`schoolCompetitionId`, `schoolId`, `competitionId`, `division`)
VALUES
	(135,5,3,NULL),
	(214,4,4,NULL),
	(215,4,6,NULL),
	(216,4,1,NULL),
	(217,4,2,NULL),
	(218,4,3,NULL),
	(219,4,7,NULL),
	(220,4,5,NULL),
	(221,4,8,NULL);

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

LOCK TABLES `school_test` WRITE;
/*!40000 ALTER TABLE `school_test` DISABLE KEYS */;

INSERT INTO `school_test` (`schoolTesttId`, `schoolId`, `testId`)
VALUES
	(11,4,4),
	(12,4,5),
	(13,4,6),
	(14,4,7),
	(15,4,8),
	(16,4,9),
	(18,4,1);

/*!40000 ALTER TABLE `school_test` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school_year
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_year`;

CREATE TABLE `school_year` (
  `schoolYearId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) DEFAULT NULL,
  `yearId` int(11) DEFAULT NULL,
  PRIMARY KEY (`schoolYearId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table shipping_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `shipping_address`;

CREATE TABLE `shipping_address` (
  `shippingAddressId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`shippingAddressId`),
  KEY `school_id` (`schoolId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `shipping_address` WRITE;
/*!40000 ALTER TABLE `shipping_address` DISABLE KEYS */;

INSERT INTO `shipping_address` (`shippingAddressId`, `schoolId`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
VALUES
	(1,1,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,2,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,3,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,4,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,5,'125 N. Main St.','STE #2','Greenville','SC','29601','2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `shipping_address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table student
# ------------------------------------------------------------

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `student_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `show_name` tinyint(1) DEFAULT NULL,
  `test1_score` int(2) DEFAULT NULL,
  `test2_score` int(2) DEFAULT NULL,
  `test3_score` int(2) DEFAULT NULL,
  `test4_score` int(2) DEFAULT NULL,
  `total_score` int(2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



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
	(1,4,'davidmcnight@gmail.com','wMHSz5GPHftqJ7AaHwf9yCgyZWn+e+XS5iB/uTfevno=',1,0,'0000-00-00 00:00:00',NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(2,4,'justin.pidcock@3foldx.com','vSKkTZNfA9wrqYDX93Cbuhqddyqbt17MMU/9Rnzzlz0=',0,0,'0000-00-00 00:00:00',NULL,'2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table year
# ------------------------------------------------------------

DROP TABLE IF EXISTS `year`;

CREATE TABLE `year` (
  `yearId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `year` int(4) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `open` tinyint(1) DEFAULT NULL,
  `openTest` tinyint(1) DEFAULT NULL,
  `resultsArePublic` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`yearId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `year` WRITE;
/*!40000 ALTER TABLE `year` DISABLE KEYS */;

INSERT INTO `year` (`yearId`, `description`, `year`, `created_at`, `updated_at`, `open`, `openTest`, `resultsArePublic`)
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
