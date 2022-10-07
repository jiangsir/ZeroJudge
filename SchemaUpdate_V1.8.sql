ALTER TABLE `schools` CHANGE `schoolid` `id` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `vclasses` CHANGE `vclassid` `id` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `vclassstudents` CHANGE `vclassstudentid` `id` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `solutions` CHANGE `solutionid` `id` INT(11) NOT NULL AUTO_INCREMENT;

-- errmsg_visible 改成 wa_visible 
ALTER TABLE `problems` CHANGE `errmsg_visible` `wa_visible` INT(11) NOT NULL DEFAULT '1';

-- google login 在 appconfigs 加入 client_id, client_secret, redirect_url 等 3 個欄位。預設內容為空
ALTER TABLE `appconfigs` ADD `client_id` VARCHAR(255) NOT NULL AFTER `httpscrt`, ADD `client_secret` VARCHAR(255) NOT NULL AFTER `client_id`, ADD `redirect_uri` VARCHAR(255) NOT NULL AFTER `client_secret`;
ALTER TABLE `appconfigs` CHANGE `client_id` `client_id` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `appconfigs` CHANGE `client_secret` `client_secret` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `appconfigs` CHANGE `redirect_uri` `redirect_uri` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';

-- users table 加入 picture 紀錄相片 url 
ALTER TABLE `users` ADD `picture` TEXT NOT NULL AFTER `email`;

-- 將 account 的長度加到 255 以便容納 google account
ALTER TABLE `users` CHANGE `account` `account` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
-- authhost 長度加到 255
ALTER TABLE `users` CHANGE `authhost` `authhost` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'localhost';
-- authhost 127.0.0.1 改成 localhost
UPDATE users SET authhost = "localhost" WHERE authhost = "127.0.0.1";
UPDATE users SET authhost = "localhost" WHERE authhost = "nknush.kh.edu.tw";

-- 處理掉錯誤的 account
UPDATE users SET account='ERROR'+userid WHERE account='';


-- problems 增加一個 ownerid 編輯者的 userid.  
ALTER TABLE `problems` ADD `ownerid` INT NOT NULL DEFAULT '0' AFTER `author`;
-- 再 將 author 轉換為 ownerid
UPDATE problems,users SET problems.ownerid = users.userid WHERE problems.author = users.account;
-- author default value 設為 ''
ALTER TABLE `problems` CHANGE `author` `author` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
-- 特別處理掉 admin
UPDATE problems SET ownerid=0 WHERE problems.author='admin';
-- 刪除 author
ALTER TABLE `problems` DROP `author`;

-- contests 增加一個 ownerid 代替 creater
ALTER TABLE `contests` ADD `ownerid` INT NOT NULL DEFAULT '0' AFTER `creater`;
-- 再 將 creater 轉換為 ownerid
UPDATE contests,users SET contests.ownerid = users.userid WHERE contests.creater = users.account;
-- 特別處理掉 admin
UPDATE contests SET ownerid=0 WHERE creater='admin';
-- 刪除 creater
ALTER TABLE `contests` DROP `creater`;

-- vclasses 增加一個 ownerid 代替 teacher
ALTER TABLE `vclasses` ADD `ownerid` INT NOT NULL DEFAULT '0' AFTER `teacher`;
-- 再 將 creater 轉換為 ownerid
UPDATE vclasses,users SET vclasses.ownerid = users.userid WHERE vclasses.teacher = users.account;
-- 
ALTER TABLE `contests` CHANGE `creater` `creater` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '';
-- 特別處理掉 admin
UPDATE vclasses SET ownerid=0 WHERE teacher='admin';
-- 刪除 teacher
ALTER TABLE `vclasses` DROP `teacher`;
-- 新增 pictureblob 來存 google account 的相片檔。
ALTER TABLE `users` ADD `pictureblob` MEDIUMBLOB NOT NULL AFTER `picture`;
-- 增加 picturetype 作為轉換 base64時使用。
ALTER TABLE `users` ADD `picturetype` VARCHAR(200) NOT NULL AFTER `pictureblob`;
-- 將 userid 改為 id
ALTER TABLE `users` CHANGE `userid` `id` INT(11) NOT NULL AUTO_INCREMENT;
-- contestants 改用 userid 紀錄。
ALTER TABLE `contestants` ADD `userid` INT NOT NULL DEFAULT '0' AFTER `contestid`;
-- 將 teamaccount 轉換成 userid
UPDATE contestants,users SET contestants.userid = users.id WHERE contestants.teamaccount = users.account;
-- teamaccount default value = '' 以免錯誤。
ALTER TABLE `contestants` CHANGE `teamaccount` `teamaccount` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
-- 增加 logo 欄位。
ALTER TABLE `appconfigs` ADD `logo` MEDIUMBLOB NOT NULL AFTER `titleImage`;
-- teamaccount 取消後， UNIQUE 限制要剔除。
ALTER TABLE contestants DROP INDEX contestid_2;

ALTER TABLE `imessages` CHANGE `accountfrom` `sender` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `imessages` CHANGE `accountto` `receiver` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `imessages` CHANGE `sender_visible` `sender_status` INT(11) NOT NULL DEFAULT '1';
ALTER TABLE `imessages` CHANGE `status` `receiver_status` VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'unread';
ALTER TABLE `imessages` CHANGE `subject` `subject` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '';
ALTER TABLE `imessages` CHANGE `sender_status` `sender_status` VARCHAR(10) NOT NULL DEFAULT 'open';
UPDATE imessages SET sender_status='open' WHERE sender_status='1';
UPDATE imessages SET sender_status='deleted' WHERE sender_status='0';


-- 新增一個 vclass code 讓 user 直接輸入 code 來加入 vclass
ALTER TABLE  `vclasses` ADD  `vclasscode` VARCHAR( 50 ) NOT NULL AFTER  `vclassname`;
-- UPDATE vclasses SET vclasscode = MD5(now()+vclasses.id) ;
ALTER TABLE  `vclasses` ADD UNIQUE (`vclasscode`);

-- 將通過 30%的人加上「公開」題目的權限。因此教師沒有公開題目的權限。
UPDATE `users` SET config=(config | (1 << 1)) WHERE (config & (1 << 2)) && (config & (1 << 3)) && (config & (1 << 4)) && !(config & (1 << 5));
-- 將 forum 的 account 拉長，以便容納 google account
ALTER TABLE  `forum` CHANGE  `account`  `account` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `problems` CHANGE  `timelimits`  `timelimits` VARCHAR(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `problems` CHANGE  `serveroutputs`  `serveroutputs` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
-- ALTER TABLE  `solutions` CHANGE  `serveroutputs`  `serveroutputs` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


