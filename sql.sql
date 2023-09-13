/*
SQLyog Community Edition- MySQL GUI v8.03 
MySQL - 5.6.12-log : Database - 22_packer_and_mover
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`22_packer_and_mover` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `22_packer_and_mover`;

/*Table structure for table `bank` */

DROP TABLE IF EXISTS `bank`;

CREATE TABLE `bank` (
  `p_id` int(50) NOT NULL AUTO_INCREMENT,
  `u_id` int(4) DEFAULT NULL,
  `bank` varchar(50) DEFAULT NULL,
  `ifsc_code` varchar(50) DEFAULT NULL,
  `account_no` bigint(20) DEFAULT NULL,
  `balance` bigint(20) DEFAULT '100000',
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bank` */

/*Table structure for table `driver_bill` */

DROP TABLE IF EXISTS `driver_bill`;

CREATE TABLE `driver_bill` (
  `db_id` int(11) NOT NULL AUTO_INCREMENT,
  `w_id` int(11) DEFAULT NULL,
  `date` varchar(50) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `km` varchar(50) DEFAULT NULL,
  `charge` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`db_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `driver_bill` */

/*Table structure for table `item` */

DROP TABLE IF EXISTS `item`;

CREATE TABLE `item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `itemname` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `item` */

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `loc_id` int(50) NOT NULL AUTO_INCREMENT,
  `u_id` int(50) DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  PRIMARY KEY (`loc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `location` */

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(50) DEFAULT NULL,
  `ur_id` int(11) DEFAULT NULL,
  `acc_no` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `payment` */

/*Table structure for table `rating` */

DROP TABLE IF EXISTS `rating`;

CREATE TABLE `rating` (
  `rtg_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `rate` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`rtg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `rating` */

/*Table structure for table `request_details` */

DROP TABLE IF EXISTS `request_details`;

CREATE TABLE `request_details` (
  `rd_id` int(11) NOT NULL AUTO_INCREMENT,
  `ur_id` varchar(50) DEFAULT NULL,
  `item_id` int(50) DEFAULT NULL,
  `item_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`rd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `request_details` */

/*Table structure for table `requset_worker` */

DROP TABLE IF EXISTS `requset_worker`;

CREATE TABLE `requset_worker` (
  `wr_id` int(50) NOT NULL AUTO_INCREMENT,
  `u_id` int(50) DEFAULT NULL,
  `w_id` int(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `no_of_worker_required` int(11) DEFAULT NULL,
  `charge` int(11) DEFAULT NULL,
  PRIMARY KEY (`wr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `requset_worker` */

/*Table structure for table `resheduled_request` */

DROP TABLE IF EXISTS `resheduled_request`;

CREATE TABLE `resheduled_request` (
  `resh_id` int(50) NOT NULL AUTO_INCREMENT,
  `w_id` int(50) DEFAULT NULL,
  `u_id` int(50) DEFAULT NULL,
  `reason` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`resh_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `resheduled_request` */

/*Table structure for table `user_registration` */

DROP TABLE IF EXISTS `user_registration`;

CREATE TABLE `user_registration` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `phone_no` bigint(20) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `license_no` varchar(50) DEFAULT NULL,
  `house_name` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `post` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `pin` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user_registration` */

/*Table structure for table `user_request` */

DROP TABLE IF EXISTS `user_request`;

CREATE TABLE `user_request` (
  `ur_id` int(50) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `date` varchar(20) DEFAULT NULL,
  `latitude` varchar(20) DEFAULT NULL,
  `longitude` varchar(20) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user_request` */

/*Table structure for table `vehicle` */

DROP TABLE IF EXISTS `vehicle`;

CREATE TABLE `vehicle` (
  `v_id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicle_type` varchar(50) DEFAULT NULL,
  `rc_details` varchar(50) DEFAULT NULL,
  `photo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`v_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `vehicle` */

/*Table structure for table `work` */

DROP TABLE IF EXISTS `work`;

CREATE TABLE `work` (
  `w_id` int(50) NOT NULL AUTO_INCREMENT,
  `ur_id` int(50) DEFAULT NULL,
  `u_id` int(50) DEFAULT NULL,
  `v_id` int(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`w_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `work` */

/*Table structure for table `worker_bill` */

DROP TABLE IF EXISTS `worker_bill`;

CREATE TABLE `worker_bill` (
  `wb_id` int(50) NOT NULL AUTO_INCREMENT,
  `wr_id` int(4) DEFAULT NULL,
  `date` varchar(50) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `charge` varchar(50) DEFAULT NULL,
  `work_details` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`wb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `worker_bill` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
