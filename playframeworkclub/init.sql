DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `loginName` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `weibo` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT '0',
  `topic_count` int(11) DEFAULT '0',
  `reply_count` int(11) DEFAULT '0',
  `follower_count` int(11) DEFAULT '0',
  `following_count` int(11) DEFAULT '0',
  `collect_tag_count` int(11) DEFAULT '0',
  `collect_topic_count` int(11) DEFAULT '0',
  `create_at` timestamp NOT NULL,
  `update_at` timestamp NOT NULL,
  `is_star` tinyint(1) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `receive_mail` tinyint(1) DEFAULT '1',
  `isAdmin` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `Relation`;
CREATE TABLE `Relation` (
  `user_id` bigint(20) NOT NULL,
  `follow_id` bigint(20) NOT NULL,
  `create_at` timestamp NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 	
DROP TABLE IF EXISTS `Tag`;
CREATE TABLE `Tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `background` varchar(255),
  `topic_count` int(11) DEFAULT '0',
  `collect_count` int(11) DEFAULT '0',
  `tag_order` int(11) DEFAULT '1',
  `create_at` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `TagCollect`; 
CREATE TABLE `TagCollect` (
  `user_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `create_at` timestamp NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 
DROP TABLE IF EXISTS `Topic`;
CREATE TABLE `Topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` varchar(4000) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `top` tinyint(1) DEFAULT '0',
  `reply_count` int(11) DEFAULT '0',
  `visit_count` int(11) DEFAULT '0',
  `collect_count` int(11) DEFAULT '0',
  `create_at` timestamp NOT NULL,
  `update_at` timestamp NOT NULL,
  `last_reply` bigint(20),
  `last_reply_at` timestamp,
  `content_is_html` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `TopicTag`;
CREATE TABLE `TopicTag` (
  `topic_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `create_at` timestamp NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 
DROP TABLE IF EXISTS `TopicCollect`;
CREATE TABLE `TopicCollect` (
  `topic_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_at` timestamp NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 
DROP TABLE IF EXISTS `Reply`;
CREATE TABLE `Reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(4000) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  `reply_id` bigint(20),
  `create_at` timestamp NOT NULL,
  `update_at` timestamp NOT NULL,
  `content_is_html` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
