ALTER TABLE `appconfigs` ADD `site_key` VARCHAR(255) NOT NULL DEFAULT '' AFTER `httpscrt`; 
ALTER TABLE `appconfigs` ADD `secret_key` VARCHAR(255) NOT NULL DEFAULT '' AFTER `site_key`; 
ALTER TABLE `problems` CHANGE `timelimits` `timelimits` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
