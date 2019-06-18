# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: educontest_test
# Generation Time: 2016-10-14 20:57:48 +0000
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
  `billing_address_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) NOT NULL,
  `address1` varchar(100) DEFAULT '',
  `address2` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `zip` varchar(20) DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`billing_address_id`),
  KEY `school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `billing_address` WRITE;
/*!40000 ALTER TABLE `billing_address` DISABLE KEYS */;

INSERT INTO `billing_address` (`billing_address_id`, `school_id`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
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
  `competition_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `year` varchar(255) DEFAULT NULL,
  `grade_id` int(255) DEFAULT NULL,
  `division_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table division
# ------------------------------------------------------------

DROP TABLE IF EXISTS `division`;

CREATE TABLE `division` (
  `division_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `number` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`division_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table grade
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `grade_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table league
# ------------------------------------------------------------

DROP TABLE IF EXISTS `league`;

CREATE TABLE `league` (
  `league_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`league_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `league` WRITE;
/*!40000 ALTER TABLE `league` DISABLE KEYS */;

INSERT INTO `league` (`league_id`, `description`, `created_at`, `updated_at`)
VALUES
	(1,'Catholic Math League','',''),
	(2,'Educontest','','');

/*!40000 ALTER TABLE `league` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school`;

CREATE TABLE `school` (
  `school_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`school_id`),
  KEY `league_id` (`league_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;

INSERT INTO `school` (`school_id`, `name`, `contact`, `billing_name`, `purchase_order`, `school_email`, `phone`, `fax`, `league_id`, `old_school_id`, `created_at`, `updated_at`)
VALUES
	(1,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:08','2016-10-12 15:09:08'),
	(2,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:39','2016-10-12 15:09:39'),
	(3,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:09:43','2016-10-12 15:09:43'),
	(4,'Greenville High','Davey McNight','Sarah','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(5,'Greenville High','Justin Pidcock','Justin Pidcock','55673','davidmcnight@gmail.com','864-654-256','864-223-1234',2,NULL,'2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table school_competition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `school_competition`;

CREATE TABLE `school_competition` (
  `school_competition_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) DEFAULT NULL,
  `competition_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`school_competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table shipping_address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `shipping_address`;

CREATE TABLE `shipping_address` (
  `shipping_address_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`shipping_address_id`),
  KEY `school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `shipping_address` WRITE;
/*!40000 ALTER TABLE `shipping_address` DISABLE KEYS */;

INSERT INTO `shipping_address` (`shipping_address_id`, `school_id`, `address1`, `address2`, `city`, `state`, `zip`, `created_at`, `updated_at`)
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



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `admin` tinyint(2) DEFAULT NULL,
  `reset_password_token` varchar(255) DEFAULT NULL,
  `reset_password_sent_at` datetime DEFAULT NULL,
  `remember_password_created_at` datetime DEFAULT NULL,
  `sign_in_count` int(11) NOT NULL,
  `last_sign_in_at` datetime NOT NULL,
  `current_sign_in_ip` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`user_id`, `school_id`, `email`, `password`, `admin`, `reset_password_token`, `reset_password_sent_at`, `remember_password_created_at`, `sign_in_count`, `last_sign_in_at`, `current_sign_in_ip`, `created_at`, `updated_at`)
VALUES
	(1,4,'davidmcnight@gmail.com','wMHSz5GPHftqJ7AaHwf9yCgyZWn+e+XS5iB/uTfevno=',0,NULL,NULL,NULL,0,'0000-00-00 00:00:00',NULL,'2016-10-12 15:10:04','2016-10-12 15:10:04'),
	(2,4,'justin.pidcock@3foldx.com','vSKkTZNfA9wrqYDX93Cbuhqddyqbt17MMU/9Rnzzlz0=',0,NULL,NULL,NULL,0,'0000-00-00 00:00:00',NULL,'2016-10-14 16:39:27','2016-10-14 16:39:27');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
