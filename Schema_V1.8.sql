-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- 主機: localhost
-- 產生時間： 2017 年 02 月 04 日 00:22
-- 伺服器版本: 5.5.54-0+deb8u1-log
-- PHP 版本： 5.6.29-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- 資料庫： `zerojudge`
--
-- CREATE DATABASE IF NOT EXISTS `zerojudge` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- USE `zerojudge`;

-- --------------------------------------------------------

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
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `contestants`
--

CREATE TABLE IF NOT EXISTS `contestants` (
`id` int(11) NOT NULL,
  `contestid` int(11) DEFAULT '0',
  `teamaccount` varchar(100) NOT NULL,
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `contests`
--

CREATE TABLE IF NOT EXISTS `contests` (
`id` int(11) NOT NULL,
  `problemids` varchar(255) NOT NULL,
  `scores` varchar(255) NOT NULL,
  `removedsolutionids` text NOT NULL,
  `creater` varchar(200) DEFAULT NULL,
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `forum`
--

CREATE TABLE IF NOT EXISTS `forum` (
`id` int(11) NOT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `account` varchar(20) NOT NULL DEFAULT '',
  `reply` int(11) DEFAULT '0',
  `problemid` varchar(10) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `articletype` varchar(50) NOT NULL DEFAULT 'forum',
  `content` text,
  `ipfrom` varchar(150) NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `clicknum` int(11) NOT NULL DEFAULT '0',
  `hidden` varchar(50) DEFAULT 'open'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `imessages`
--

CREATE TABLE IF NOT EXISTS `imessages` (
`id` int(11) NOT NULL,
  `accountfrom` varchar(20) NOT NULL DEFAULT '',
  `accountto` varchar(20) NOT NULL DEFAULT '',
  `subject` varchar(100) NOT NULL DEFAULT '',
  `content` text,
  `senttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `sender_visible` int(11) NOT NULL DEFAULT '1',
  `status` varchar(10) NOT NULL DEFAULT 'unread'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `loginlog`
--

CREATE TABLE IF NOT EXISTS `loginlog` (
`id` bigint(20) NOT NULL,
  `userid` varchar(12) NOT NULL DEFAULT '',
  `useraccount` varchar(20) NOT NULL DEFAULT '',
  `ipfrom` varchar(150) NOT NULL,
  `ipinfo` varchar(100) NOT NULL DEFAULT '',
  `message` varchar(100) NOT NULL DEFAULT 'Unknown',
  `logintime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `logouttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `staymin` varchar(20) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `problems`
--

CREATE TABLE IF NOT EXISTS `problems` (
`id` int(11) NOT NULL,
  `problemid` varchar(20) NOT NULL DEFAULT '',
  `title` varchar(200) NOT NULL DEFAULT '',
  `testfilelength` int(11) NOT NULL,
  `timelimits` varchar(200) NOT NULL DEFAULT '',
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
  `serveroutputs` text NOT NULL,
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
  `author` varchar(200) NOT NULL,
  `reference` varchar(200) NOT NULL,
  `sortable` varchar(255) NOT NULL,
  `keywords` varchar(255) NOT NULL,
  `inserttime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `updatetime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  `tabid` varchar(100) NOT NULL,
  `errmsg_visible` int(11) NOT NULL DEFAULT '1',
  `display` varchar(10) NOT NULL DEFAULT 'hide',
  `reserved_text1` text NOT NULL,
  `reserved_text2` text NOT NULL,
  `reserved_text3` text NOT NULL,
  `reserved_text4` text NOT NULL,
  `reserved_text5` text NOT NULL,
  `reserved_text6` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `schools`
--

CREATE TABLE IF NOT EXISTS `schools` (
`schoolid` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `schoolname` varchar(100) DEFAULT NULL,
  `imgsrc` varchar(255) NOT NULL,
  `descript` text NOT NULL,
  `checkid` int(11) DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `solutions`
--

CREATE TABLE IF NOT EXISTS `solutions` (
`solutionid` int(11) NOT NULL,
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
  `serveroutputs` text NOT NULL,
  `ipfrom` varchar(150) NOT NULL,
  `submittime` datetime DEFAULT '2006-06-08 00:00:00',
  `visible` int(11) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `users`
--

CREATE TABLE IF NOT EXISTS `users` (
`userid` int(11) NOT NULL,
  `account` varchar(20) NOT NULL DEFAULT '',
  `authhost` varchar(50) DEFAULT '127.0.0.1',
  `username` varchar(100) NOT NULL DEFAULT '',
  `md5passwd` varchar(50) NOT NULL DEFAULT '',
  `truename` varchar(20) NOT NULL DEFAULT '',
  `birthyear` int(5) NOT NULL DEFAULT '0',
  `schoolid` int(11) NOT NULL DEFAULT '0',
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `email` varchar(50) NOT NULL DEFAULT '',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `vclasses`
--

CREATE TABLE IF NOT EXISTS `vclasses` (
`vclassid` int(11) NOT NULL,
  `vclassname` varchar(100) NOT NULL,
  `teacher` varchar(100) NOT NULL,
  `firstclasstime` datetime NOT NULL,
  `problemids` text NOT NULL,
  `descript` text NOT NULL,
  `visible` int(11) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `vclassstudents`
--

CREATE TABLE IF NOT EXISTS `vclassstudents` (
`vclassstudentid` int(11) NOT NULL,
  `vclassid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL,
  `account` varchar(30) NOT NULL,
  `vclassaclist` text NOT NULL,
  `ac` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `contestid_2` (`contestid`,`teamaccount`);

--
-- 資料表索引 `contests`
--
ALTER TABLE `contests`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `difflog`
--
ALTER TABLE `difflog`
 ADD PRIMARY KEY (`difflogid`), ADD UNIQUE KEY `basecodeid` (`basecodeid`,`comparecodeid`);

--
-- 資料表索引 `downloads`
--
ALTER TABLE `downloads`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `forum`
--
ALTER TABLE `forum`
 ADD PRIMARY KEY (`id`), ADD KEY `userid` (`userid`,`account`,`reply`,`problemid`,`hidden`);

--
-- 資料表索引 `imessages`
--
ALTER TABLE `imessages`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `loginlog`
--
ALTER TABLE `loginlog`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `logs`
--
ALTER TABLE `logs`
 ADD PRIMARY KEY (`id`), ADD KEY `timestamp` (`timestamp`);

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
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `pid` (`pid`,`problemid`,`locale`);

--
-- 資料表索引 `problems`
--
ALTER TABLE `problems`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `problemid` (`problemid`), ADD KEY `problemid_2` (`display`,`locale`,`problemid`), ADD KEY `display_tabid_problemid` (`display`,`tabid`,`problemid`), ADD KEY `problemid_display_tabid` (`problemid`,`display`,`tabid`), ADD KEY `display_updatetime` (`display`,`updatetime`);

--
-- 資料表索引 `schools`
--
ALTER TABLE `schools`
 ADD PRIMARY KEY (`schoolid`), ADD UNIQUE KEY `url` (`url`), ADD UNIQUE KEY `name` (`schoolname`);

--
-- 資料表索引 `solutions`
--
ALTER TABLE `solutions`
 ADD PRIMARY KEY (`solutionid`), ADD KEY `userid` (`userid`), ADD KEY `contestid` (`contestid`), ADD KEY `visible` (`visible`), ADD KEY `statusid` (`judgement`), ADD KEY `codelength` (`codelength`), ADD KEY `pid` (`pid`), ADD KEY `language` (`language`), ADD KEY `visible_solutionid` (`visible`,`solutionid`), ADD KEY `contestid_solutionid` (`contestid`,`solutionid`), ADD KEY `judgement_solutionid` (`judgement`,`solutionid`), ADD KEY `judgement_visible_solutionid` (`judgement`,`visible`,`solutionid`), ADD KEY `pid_judgement_visible_solutionid` (`pid`,`judgement`,`visible`,`solutionid`), ADD KEY `pid_userid_visible_solutionid` (`pid`,`userid`,`visible`,`solutionid`);

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
 ADD PRIMARY KEY (`session_id`), ADD KEY `kapp_name` (`app_name`);

--
-- 資料表索引 `upfiles`
--
ALTER TABLE `upfiles`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`userid`), ADD UNIQUE KEY `NONCLUSTERED` (`account`), ADD KEY `account` (`schoolid`,`vclassid`,`joinedcontestid`,`config`,`account`);

--
-- 資料表索引 `vclasses`
--
ALTER TABLE `vclasses`
 ADD PRIMARY KEY (`vclassid`);

--
-- 資料表索引 `vclassstudents`
--
ALTER TABLE `vclassstudents`
 ADD PRIMARY KEY (`vclassstudentid`), ADD UNIQUE KEY `vclassid` (`vclassid`,`userid`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

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
-- 使用資料表 AUTO_INCREMENT `loginlog`
--
ALTER TABLE `loginlog`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
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
MODIFY `schoolid` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用資料表 AUTO_INCREMENT `solutions`
--
ALTER TABLE `solutions`
MODIFY `solutionid` int(11) NOT NULL AUTO_INCREMENT;
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
MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用資料表 AUTO_INCREMENT `vclasses`
--
ALTER TABLE `vclasses`
MODIFY `vclassid` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用資料表 AUTO_INCREMENT `vclassstudents`
--
ALTER TABLE `vclassstudents`
MODIFY `vclassstudentid` int(11) NOT NULL AUTO_INCREMENT;