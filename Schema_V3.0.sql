-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- 主機: 127.0.0.1
-- 產生時間： 2018-10-29 06:24:19
-- 伺服器版本: 10.1.32-MariaDB
-- PHP 版本： 5.6.36

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `zerojudge`
--
CREATE DATABASE if NOT EXISTS zerojudge CHARACTER SET utf8 COLLATE utf8_general_ci;

-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vclasstemplates` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `ownerid` int(11) NOT NULL,
  `descript` text NOT NULL,
  `problemids` text NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




--
-- 資料表結構 `appconfigs`
--

CREATE TABLE IF NOT EXISTS `appconfigs` (
  `id` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `header` varchar(200) NOT NULL,
  `pageSize` int(11) NOT NULL,
  `manager_ip` varchar(255) NOT NULL,
  `rankingmode_HSIC` varchar(255) NOT NULL,
  `EnableMailer` tinyint(1) NOT NULL,
  `CandidateManager` varchar(255) NOT NULL,
  `systemMode` varchar(50) NOT NULL,
  `systemModeContestid` int(11) NOT NULL,
  `consolePath` varchar(255) NOT NULL,
  `system_closed_message` varchar(255) NOT NULL,
  `problemid_prefix` varchar(10) NOT NULL,
  `problemtabs` text NOT NULL,
  `serverUrl` varchar(100) NOT NULL,
  `cryptKey` varchar(10) NOT NULL,
  `rsyncAccount` varchar(100) NOT NULL,
  `system_monitor_ip` varchar(255) NOT NULL,
  `competition_mode` varchar(255) NOT NULL,
  `rankingmode_NPSC` varchar(255) NOT NULL,
  `rankingmode_CSAPC` varchar(255) NOT NULL,
  `SystemMonitorAccount` varchar(255) NOT NULL,
  `titleImage` mediumblob NOT NULL,
  `logo` mediumblob NOT NULL,
  `threshold` double NOT NULL,
  `Locales` varchar(255) NOT NULL,
  `schemas` text NOT NULL,
  `exclusiveSchoolids` varchar(255) NOT NULL,
  `cachedUser` tinyint(1) NOT NULL,
  `cachedContest` tinyint(1) NOT NULL,
  `cachedProblem` tinyint(1) NOT NULL,
  `rejudgeable` tinyint(1) NOT NULL,
  `allowedIP` varchar(255) NOT NULL,
  `judgeQueueSize` int(11) NOT NULL,
  `maxConnectionByIP` int(11) NOT NULL,
  `maxCodeLength` int(11) NOT NULL,
  `maxTestdata` int(11) NOT NULL,
  `SearchEngines` text NOT NULL,
  `bannedIPSet` text NOT NULL,
  `managers` varchar(200) NOT NULL,
  `banners` text NOT NULL,
  `SystemMail` varchar(255) NOT NULL,
  `SystemMailPassword` varchar(255) NOT NULL,
  `JVM` int(11) NOT NULL,
  `last_solutionid_for_RecountProblemidset` int(11) NOT NULL DEFAULT '0',
  `httpscrt` mediumblob NOT NULL,
  `client_id` varchar(255) NOT NULL DEFAULT '',
  `client_secret` varchar(255) NOT NULL DEFAULT '',
  `redirect_uri` varchar(255) NOT NULL DEFAULT '',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `contestants`
--

CREATE TABLE IF NOT EXISTS `contestants` (
  `id` int(11) NOT NULL,
  `contestid` int(11) DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `teamaccount` varchar(100) NOT NULL DEFAULT '',
  `teamname` varchar(200) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `school` varchar(100) NOT NULL,
  `ipset` varchar(50) NOT NULL,
  `language` varchar(20) NOT NULL,
  `ac` int(11) DEFAULT '0',
  `aclist` varchar(255) NOT NULL,
  `submits` int(11) NOT NULL DEFAULT '0',
  `penalty` int(11) NOT NULL DEFAULT '0',
  `score` int(11) NOT NULL DEFAULT '0',
  `status` varchar(20) NOT NULL DEFAULT 'registed',
  `finishtime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `contests`
--

CREATE TABLE IF NOT EXISTS `contests` (
  `id` int(11) NOT NULL,
  `problemids` varchar(255) NOT NULL,
  `scores` varchar(255) NOT NULL,
  `removedsolutionids` text NOT NULL,
  `ownerid` int(11) NOT NULL DEFAULT '0',
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `userrules` text NOT NULL,
  `starttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `timelimit` bigint(20) DEFAULT '6000000',
  `pausepoints` text NOT NULL,
  `title` varchar(200) NOT NULL DEFAULT '',
  `subtitle` text NOT NULL,
  `taskid` int(11) DEFAULT '0',
  `conteststatus` varchar(20) NOT NULL DEFAULT 'Contest.NotStart',
  `addonprivilege` varchar(100) NOT NULL,
  `rankingmode` varchar(20) NOT NULL DEFAULT 'NPSC',
  `freezelimit` int(11) NOT NULL DEFAULT '0',
  `config` int(11) NOT NULL DEFAULT '0',
  `visible` varchar(20) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `difflog`
--

CREATE TABLE IF NOT EXISTS `difflog` (
  `difflogid` bigint(20) NOT NULL,
  `basecodeid` bigint(20) DEFAULT '0',
  `comparecodeid` bigint(20) DEFAULT '0',
  `contestid` int(11) NOT NULL DEFAULT '0',
  `compareresult` text,
  `baselinenum` int(11) DEFAULT '0',
  `resultlinenum` int(11) DEFAULT '0',
  `comparestatus` varchar(20) DEFAULT 'Waiting...'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `downloads`
--

CREATE TABLE IF NOT EXISTS `downloads` (
  `id` int(11) NOT NULL,
  `account` varchar(50) NOT NULL,
  `ipfrom` varchar(150) NOT NULL DEFAULT '0.0.0.0',
  `downloadtime` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `forum`
--

CREATE TABLE IF NOT EXISTS `forum` (
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `account` varchar(100) NOT NULL DEFAULT '',
  `reply` int(11) DEFAULT '0',
  `problemid` varchar(10) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `articletype` varchar(50) NOT NULL DEFAULT 'forum',
  `content` text,
  `ipfrom` varchar(150) NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `clicknum` int(11) NOT NULL DEFAULT '0',
  `hidden` varchar(50) DEFAULT 'open'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `imessages`
--

CREATE TABLE IF NOT EXISTS `imessages` (
  `id` int(11) NOT NULL,
  `sender` varchar(200) NOT NULL DEFAULT '',
  `receiver` varchar(200) NOT NULL DEFAULT '',
  `subject` varchar(200) NOT NULL DEFAULT '',
  `content` text,
  `senttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `sender_status` varchar(10) NOT NULL DEFAULT 'open',
  `receiver_status` varchar(10) NOT NULL DEFAULT 'unread'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `loginlog`
--
-- 讀取資料表 zerojudge.loginlog 時出現錯誤： #144 - Table '.\zerojudge\loginlog' is marked as crashed and last (automatic?) repair failed

-- --------------------------------------------------------

--
-- 資料表結構 `logs`
--

CREATE TABLE IF NOT EXISTS `logs` (
  `id` int(11) NOT NULL,
  `method` varchar(20) NOT NULL,
  `uri` varchar(100) NOT NULL DEFAULT '',
  `session_account` varchar(100) NOT NULL,
  `ipaddr` varchar(150) NOT NULL,
  `tabid` varchar(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `stacktrace` text NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT '2006-06-08 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `pages`
--

CREATE TABLE IF NOT EXISTS `pages` (
  `id` bigint(20) NOT NULL,
  `userid` int(11) NOT NULL,
  `account` varchar(20) NOT NULL,
  `method` varchar(20) DEFAULT 'GET',
  `currentpage` varchar(255) NOT NULL,
  `querystring` text,
  `ip` varchar(150) NOT NULL,
  `timestamp` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `problemimages`
--

CREATE TABLE IF NOT EXISTS `problemimages` (
  `id` int(11) NOT NULL,
  `problemid` varchar(100) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `filetype` varchar(255) NOT NULL,
  `filesize` int(11) NOT NULL,
  `file` longblob NOT NULL,
  `descript` varchar(255) NOT NULL,
  `visible` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `problemlocales`
--

CREATE TABLE IF NOT EXISTS `problemlocales` (
  `id` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `problemid` varchar(20) NOT NULL,
  `locale` varchar(10) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `theinput` text NOT NULL,
  `theoutput` text NOT NULL,
  `hint` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `problems`
--

CREATE TABLE IF NOT EXISTS `problems` (
  `id` int(11) NOT NULL,
  `problemid` varchar(20) NOT NULL DEFAULT '',
  `title` varchar(200) NOT NULL DEFAULT '',
  `testfilelength` int(11) NOT NULL,
  `timelimits` varchar(1024) NOT NULL DEFAULT '',
  `memorylimit` int(11) NOT NULL DEFAULT '65536',
  `backgrounds` varchar(255) NOT NULL DEFAULT '',
  `locale` varchar(20) NOT NULL DEFAULT 'zh_TW',
  `content` text NOT NULL,
  `theinput` text NOT NULL,
  `theoutput` text NOT NULL,
  `sampleinput` text NOT NULL,
  `sampleoutput` text NOT NULL,
  `hint` text NOT NULL,
  `language` varchar(25) NOT NULL DEFAULT 'CPP',
  `samplecode` text NOT NULL,
  `prejudgement` varchar(25) NOT NULL DEFAULT '',
  `serveroutputs` mediumtext NOT NULL,
  `comment` text NOT NULL,
  `scores` text NOT NULL,
  `alteroutdata` text NOT NULL,
  `judgemode` varchar(500) NOT NULL DEFAULT '',
  `difficulty` int(11) NOT NULL DEFAULT '0',
  `acnum` int(11) NOT NULL DEFAULT '0',
  `submitnum` bigint(20) NOT NULL DEFAULT '0',
  `clicknum` bigint(20) NOT NULL DEFAULT '0',
  `acusers` int(11) NOT NULL DEFAULT '0',
  `submitusers` int(11) NOT NULL DEFAULT '0',
  `lastsolutionid` int(11) NOT NULL DEFAULT '0',
  `ownerid` int(11) NOT NULL DEFAULT '0',
  `reference` varchar(200) NOT NULL,
  `sortable` varchar(255) NOT NULL,
  `keywords` varchar(255) NOT NULL,
  `inserttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `updatetime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `tabid` varchar(100) NOT NULL,
  `wa_visible` int(11) NOT NULL DEFAULT '1',
  `display` varchar(10) NOT NULL DEFAULT 'hide',
  `reserved_text1` text NOT NULL,
  `reserved_text2` text NOT NULL,
  `reserved_text3` text NOT NULL,
  `reserved_text4` text NOT NULL,
  `reserved_text5` text NOT NULL,
  `reserved_text6` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `schools`
--

CREATE TABLE IF NOT EXISTS `schools` (
  `id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `schoolname` varchar(100) DEFAULT NULL,
  `imgsrc` varchar(255) NOT NULL,
  `descript` text NOT NULL,
  `checkid` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `solutions`
--

CREATE TABLE IF NOT EXISTS `solutions` (
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL,
  `language` varchar(10) NOT NULL DEFAULT 'C++',
  `timeusage` bigint(20) NOT NULL DEFAULT '0',
  `memoryusage` int(11) NOT NULL DEFAULT '0',
  `exefilesize` int(11) NOT NULL DEFAULT '-1',
  `code` text NOT NULL,
  `codelength` int(11) NOT NULL DEFAULT '0',
  `judgement` int(11) NOT NULL DEFAULT '0',
  `codelocker` int(11) NOT NULL DEFAULT '1',
  `contestid` int(11) NOT NULL DEFAULT '0',
  `score` int(11) NOT NULL DEFAULT '0',
  `details` text NOT NULL,
  `serveroutputs` mediumtext NOT NULL,
  `ipfrom` varchar(150) NOT NULL,
  `submittime` datetime DEFAULT '2006-06-08 00:00:00',
  `visible` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `tasks`
--

CREATE TABLE IF NOT EXISTS `tasks` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `useraccount` varchar(30) NOT NULL DEFAULT '',
  `firststart` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `period` bigint(20) NOT NULL DEFAULT '0',
  `threadid` bigint(20) NOT NULL DEFAULT '0',
  `parameter` varchar(100) NOT NULL DEFAULT '',
  `starttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `stoptime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `status` varchar(20) NOT NULL DEFAULT 'running'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `testcodes`
--

CREATE TABLE IF NOT EXISTS `testcodes` (
  `id` int(11) NOT NULL,
  `code` text NOT NULL,
  `language` varchar(50) NOT NULL,
  `indata` text NOT NULL,
  `outdata` text NOT NULL,
  `expected_status` varchar(50) NOT NULL,
  `actual_status` varchar(50) NOT NULL,
  `actual_detail` text NOT NULL,
  `descript` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `tomcat_sessions`
--

CREATE TABLE IF NOT EXISTS `tomcat_sessions` (
  `session_id` varchar(100) NOT NULL,
  `valid_session` char(1) NOT NULL,
  `max_inactive` int(11) NOT NULL,
  `last_access` bigint(20) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `session_data` mediumblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `upfiles`
--

CREATE TABLE IF NOT EXISTS `upfiles` (
  `id` int(11) NOT NULL,
  `ipfrom` varchar(150) NOT NULL DEFAULT '0.0.0.0',
  `filename` varchar(255) NOT NULL,
  `filetype` varchar(100) NOT NULL,
  `bytes` longblob NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `solutionid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL,
  `account` varchar(255) NOT NULL DEFAULT '',
  `authhost` varchar(255) DEFAULT 'localhost',
  `username` varchar(100) NOT NULL DEFAULT '',
  `md5passwd` varchar(50) NOT NULL DEFAULT '',
  `truename` varchar(20) NOT NULL DEFAULT '',
  `birthyear` int(5) NOT NULL DEFAULT '0',
  `schoolid` int(11) NOT NULL DEFAULT '0',
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `email` varchar(50) NOT NULL DEFAULT '',
  `picture` text NOT NULL,
  `pictureblob` mediumblob NOT NULL,
  `picturetype` varchar(200) NOT NULL,
  `sessionid` varchar(50) NOT NULL DEFAULT '',
  `comment` varchar(255) NOT NULL DEFAULT '',
  `role` varchar(100) NOT NULL DEFAULT 'GUEST',
  `createby` int(11) NOT NULL,
  `label` varchar(200) NOT NULL DEFAULT '',
  `joinedcontestid` int(20) DEFAULT '0',
  `extraprivilege` varchar(255) NOT NULL DEFAULT '',
  `ipset` varchar(100) NOT NULL,
  `country` varchar(10) NOT NULL DEFAULT 'Unknown',
  `ipinfo` varchar(100) NOT NULL DEFAULT 'Unknown',
  `registertime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `lastlogin` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `lastsolutionid` int(11) NOT NULL DEFAULT '0',
  `userlanguage` varchar(10) NOT NULL DEFAULT 'CPP',
  `ac` int(11) NOT NULL DEFAULT '0',
  `aclist` text NOT NULL,
  `triedset` text NOT NULL,
  `wa` int(11) NOT NULL DEFAULT '0',
  `tle` int(11) NOT NULL DEFAULT '0',
  `mle` int(11) NOT NULL DEFAULT '0',
  `ole` int(11) NOT NULL DEFAULT '0',
  `re` int(11) NOT NULL DEFAULT '0',
  `ce` int(11) NOT NULL DEFAULT '0',
  `config` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `vclasses`
--

CREATE TABLE IF NOT EXISTS `vclasses` (
  `id` int(11) NOT NULL,
  `vclassname` varchar(100) NOT NULL,
  `vclasscode` varchar(50) NOT NULL,
  `ownerid` int(11) NOT NULL DEFAULT '0',
  `firstclasstime` datetime NOT NULL,
  `problemids` text NOT NULL,
  `descript` text NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `vclassstudents`
--

CREATE TABLE IF NOT EXISTS `vclassstudents` (
  `id` int(11) NOT NULL,
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL,
  `account` varchar(30) NOT NULL,
  `vclassaclist` text NOT NULL,
  `ac` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `tokens` (
  `id` int(11) NOT NULL,
  `base64` varchar(255) NOT NULL,
  `userid` int(11) NOT NULL,
  `descript` varchar(255) NOT NULL,
  `isdone` tinyint(1) NOT NULL DEFAULT '1',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `vclasstemplates`
--
ALTER TABLE `vclasstemplates`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`base64`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;


--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `appconfigs`
--
ALTER TABLE `appconfigs`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `contestants`
--
ALTER TABLE `contestants`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `contests`
--
ALTER TABLE `contests`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `difflog`
--
ALTER TABLE `difflog`
  ADD PRIMARY KEY (`difflogid`),
  ADD UNIQUE KEY `basecodeid` (`basecodeid`,`comparecodeid`);

--
-- 資料表索引 `downloads`
--
ALTER TABLE `downloads`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `forum`
--
ALTER TABLE `forum`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userid` (`userid`,`account`,`reply`,`problemid`,`hidden`);

--
-- 資料表索引 `imessages`
--
ALTER TABLE `imessages`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `timestamp` (`timestamp`);

--
-- 資料表索引 `pages`
--
ALTER TABLE `pages`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `problemimages`
--
ALTER TABLE `problemimages`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `problemlocales`
--
ALTER TABLE `problemlocales`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`,`problemid`,`locale`);

--
-- 資料表索引 `problems`
--
ALTER TABLE `problems`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `problemid` (`problemid`),
  ADD KEY `problemid_2` (`display`,`locale`,`problemid`),
  ADD KEY `display_tabid_problemid` (`display`,`tabid`,`problemid`),
  ADD KEY `problemid_display_tabid` (`problemid`,`display`,`tabid`),
  ADD KEY `display_updatetime` (`display`,`updatetime`);

--
-- 資料表索引 `schools`
--
ALTER TABLE `schools`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `url` (`url`),
  ADD UNIQUE KEY `name` (`schoolname`);

--
-- 資料表索引 `solutions`
--
ALTER TABLE `solutions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userid` (`userid`),
  ADD KEY `contestid` (`contestid`),
  ADD KEY `visible` (`visible`),
  ADD KEY `statusid` (`judgement`),
  ADD KEY `codelength` (`codelength`),
  ADD KEY `pid` (`pid`),
  ADD KEY `language` (`language`),
  ADD KEY `visible_solutionid` (`visible`,`id`),
  ADD KEY `contestid_solutionid` (`contestid`,`id`),
  ADD KEY `judgement_solutionid` (`judgement`,`id`),
  ADD KEY `judgement_visible_solutionid` (`judgement`,`visible`,`id`),
  ADD KEY `pid_judgement_visible_solutionid` (`pid`,`judgement`,`visible`,`id`),
  ADD KEY `pid_userid_visible_solutionid` (`pid`,`userid`,`visible`,`id`);

--
-- 資料表索引 `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `testcodes`
--
ALTER TABLE `testcodes`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `tomcat_sessions`
--
ALTER TABLE `tomcat_sessions`
  ADD PRIMARY KEY (`session_id`),
  ADD KEY `kapp_name` (`app_name`);

--
-- 資料表索引 `upfiles`
--
ALTER TABLE `upfiles`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `NONCLUSTERED` (`account`),
  ADD KEY `account` (`schoolid`,`vclassid`,`joinedcontestid`,`config`,`account`);

--
-- 資料表索引 `vclasses`
--
ALTER TABLE `vclasses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `vclasscode` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_2` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_3` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_4` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_5` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_6` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_7` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_8` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_9` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_10` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_11` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_12` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_13` (`vclasscode`),
  ADD UNIQUE KEY `vclasscode_14` (`vclasscode`);

--
-- 資料表索引 `vclassstudents`
--
ALTER TABLE `vclassstudents`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `vclassid` (`vclassid`,`userid`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `vclasstemplates`
--
ALTER TABLE `vclasstemplates`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `appconfigs`
--
ALTER TABLE `appconfigs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `contestants`
--
ALTER TABLE `contestants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `contests`
--
ALTER TABLE `contests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `difflog`
--
ALTER TABLE `difflog`
  MODIFY `difflogid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `downloads`
--
ALTER TABLE `downloads`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `forum`
--
ALTER TABLE `forum`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `imessages`
--
ALTER TABLE `imessages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `logs`
--
ALTER TABLE `logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `pages`
--
ALTER TABLE `pages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `problemimages`
--
ALTER TABLE `problemimages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `problemlocales`
--
ALTER TABLE `problemlocales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `problems`
--
ALTER TABLE `problems`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `schools`
--
ALTER TABLE `schools`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `solutions`
--
ALTER TABLE `solutions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `tasks`
--
ALTER TABLE `tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `testcodes`
--
ALTER TABLE `testcodes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `upfiles`
--
ALTER TABLE `upfiles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `vclasses`
--
ALTER TABLE `vclasses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `vclassstudents`
--
ALTER TABLE `vclassstudents`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
