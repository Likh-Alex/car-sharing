CREATE SCHEMA `taxi_service` DEFAULT CHARACTER SET utf8;

CREATE TABLE `manufacturers`
(
    `manufacturer_id`      bigint       NOT NULL AUTO_INCREMENT,
    `manufacturer_name`    varchar(100) NOT NULL,
    `manufacturer_country` varchar(100) NOT NULL,
    `deleted`              tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`manufacturer_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;
