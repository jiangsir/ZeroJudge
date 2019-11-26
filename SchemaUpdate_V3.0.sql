ALTER TABLE `appconfigs` ADD `site_key` VARCHAR(255) NOT NULL DEFAULT '' AFTER `httpscrt`; 
ALTER TABLE `appconfigs` ADD `secret_key` VARCHAR(255) NOT NULL DEFAULT '' AFTER `site_key`; 
ALTER TABLE `problems` CHANGE `timelimits` `timelimits` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `tokens` CHANGE `alive` `isdone` TINYINT(1) NOT NULL DEFAULT '0'; 
ALTER TABLE `appconfigs` ADD `enableReCaptcha` BOOLEAN NOT NULL AFTER `httpscrt`; 
ALTER TABLE `appconfigs` ADD `enableGoogleLogin` BOOLEAN NOT NULL AFTER `secret_key`;
ALTER TABLE `appconfigs` CHANGE `EnableMailer` `enableMailer` TINYINT(1) NOT NULL; 
