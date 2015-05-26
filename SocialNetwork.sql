-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.6.21-log - MySQL Community Server (GPL)
-- ОС Сервера:                   Win64
-- HeidiSQL Версия:              9.1.0.4913
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Дамп структуры базы данных social_network
DROP DATABASE IF EXISTS `social_network`;
CREATE DATABASE IF NOT EXISTS `social_network` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `social_network`;


-- Дамп структуры для таблица social_network.albums
DROP TABLE IF EXISTS `albums`;
CREATE TABLE IF NOT EXISTS `albums` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) unsigned NOT NULL,
  `creation_date` bigint(20) unsigned NOT NULL,
  `photos_count` int(10) unsigned NOT NULL DEFAULT '0',
  `title` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_albums_users` (`owner_id`),
  CONSTRAINT `FK_albums_users` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.albums: ~0 rows (приблизительно)
DELETE FROM `albums`;
/*!40000 ALTER TABLE `albums` DISABLE KEYS */;
/*!40000 ALTER TABLE `albums` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.countries
DROP TABLE IF EXISTS `countries`;
CREATE TABLE IF NOT EXISTS `countries` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.countries: ~250 rows (приблизительно)
DELETE FROM `countries`;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` (`id`, `name`) VALUES
	(1, 'Andorra'),
	(2, 'United Arab Emirates'),
	(3, 'Afghanistan'),
	(4, 'Antigua and Barbuda'),
	(5, 'Anguilla'),
	(6, 'Albania'),
	(7, 'Armenia'),
	(8, 'Angola'),
	(9, 'Antarctica'),
	(10, 'Argentina'),
	(11, 'American Samoa'),
	(12, 'Austria'),
	(13, 'Australia'),
	(14, 'Aruba'),
	(15, 'Åland'),
	(16, 'Azerbaijan'),
	(17, 'Bosnia and Herzegovina'),
	(18, 'Barbados'),
	(19, 'Bangladesh'),
	(20, 'Belgium'),
	(21, 'Burkina Faso'),
	(22, 'Bulgaria'),
	(23, 'Bahrain'),
	(24, 'Burundi'),
	(25, 'Benin'),
	(26, 'Saint Barthélemy'),
	(27, 'Bermuda'),
	(28, 'Brunei'),
	(29, 'Bolivia'),
	(30, 'Bonaire'),
	(31, 'Brazil'),
	(32, 'Bahamas'),
	(33, 'Bhutan'),
	(34, 'Bouvet Island'),
	(35, 'Botswana'),
	(36, 'Belarus'),
	(37, 'Belize'),
	(38, 'Canada'),
	(39, 'Cocos [Keeling] Islands'),
	(40, 'Democratic Republic of the Congo'),
	(41, 'Central African Republic'),
	(42, 'Republic of the Congo'),
	(43, 'Switzerland'),
	(44, 'Ivory Coast'),
	(45, 'Cook Islands'),
	(46, 'Chile'),
	(47, 'Cameroon'),
	(48, 'China'),
	(49, 'Colombia'),
	(50, 'Costa Rica'),
	(51, 'Cuba'),
	(52, 'Cape Verde'),
	(53, 'Curacao'),
	(54, 'Christmas Island'),
	(55, 'Cyprus'),
	(56, 'Czech Republic'),
	(57, 'Germany'),
	(58, 'Djibouti'),
	(59, 'Denmark'),
	(60, 'Dominica'),
	(61, 'Dominican Republic'),
	(62, 'Algeria'),
	(63, 'Ecuador'),
	(64, 'Estonia'),
	(65, 'Egypt'),
	(66, 'Western Sahara'),
	(67, 'Eritrea'),
	(68, 'Spain'),
	(69, 'Ethiopia'),
	(70, 'Finland'),
	(71, 'Fiji'),
	(72, 'Falkland Islands'),
	(73, 'Micronesia'),
	(74, 'Faroe Islands'),
	(75, 'France'),
	(76, 'Gabon'),
	(77, 'United Kingdom'),
	(78, 'Grenada'),
	(79, 'Georgia'),
	(80, 'French Guiana'),
	(81, 'Guernsey'),
	(82, 'Ghana'),
	(83, 'Gibraltar'),
	(84, 'Greenland'),
	(85, 'Gambia'),
	(86, 'Guinea'),
	(87, 'Guadeloupe'),
	(88, 'Equatorial Guinea'),
	(89, 'Greece'),
	(90, 'South Georgia and the South Sandwich Islands'),
	(91, 'Guatemala'),
	(92, 'Guam'),
	(93, 'Guinea-Bissau'),
	(94, 'Guyana'),
	(95, 'Hong Kong'),
	(96, 'Heard Island and McDonald Islands'),
	(97, 'Honduras'),
	(98, 'Croatia'),
	(99, 'Haiti'),
	(100, 'Hungary'),
	(101, 'Indonesia'),
	(102, 'Ireland'),
	(103, 'Israel'),
	(104, 'Isle of Man'),
	(105, 'India'),
	(106, 'British Indian Ocean Territory'),
	(107, 'Iraq'),
	(108, 'Iran'),
	(109, 'Iceland'),
	(110, 'Italy'),
	(111, 'Jersey'),
	(112, 'Jamaica'),
	(113, 'Jordan'),
	(114, 'Japan'),
	(115, 'Kenya'),
	(116, 'Kyrgyzstan'),
	(117, 'Cambodia'),
	(118, 'Kiribati'),
	(119, 'Comoros'),
	(120, 'Saint Kitts and Nevis'),
	(121, 'North Korea'),
	(122, 'South Korea'),
	(123, 'Kuwait'),
	(124, 'Cayman Islands'),
	(125, 'Kazakhstan'),
	(126, 'Laos'),
	(127, 'Lebanon'),
	(128, 'Saint Lucia'),
	(129, 'Liechtenstein'),
	(130, 'Sri Lanka'),
	(131, 'Liberia'),
	(132, 'Lesotho'),
	(133, 'Lithuania'),
	(134, 'Luxembourg'),
	(135, 'Latvia'),
	(136, 'Libya'),
	(137, 'Morocco'),
	(138, 'Monaco'),
	(139, 'Moldova'),
	(140, 'Montenegro'),
	(141, 'Saint Martin'),
	(142, 'Madagascar'),
	(143, 'Marshall Islands'),
	(144, 'Macedonia'),
	(145, 'Mali'),
	(146, 'Myanmar [Burma]'),
	(147, 'Mongolia'),
	(148, 'Macao'),
	(149, 'Northern Mariana Islands'),
	(150, 'Martinique'),
	(151, 'Mauritania'),
	(152, 'Montserrat'),
	(153, 'Malta'),
	(154, 'Mauritius'),
	(155, 'Maldives'),
	(156, 'Malawi'),
	(157, 'Mexico'),
	(158, 'Malaysia'),
	(159, 'Mozambique'),
	(160, 'Namibia'),
	(161, 'New Caledonia'),
	(162, 'Niger'),
	(163, 'Norfolk Island'),
	(164, 'Nigeria'),
	(165, 'Nicaragua'),
	(166, 'Netherlands'),
	(167, 'Norway'),
	(168, 'Nepal'),
	(169, 'Nauru'),
	(170, 'Niue'),
	(171, 'New Zealand'),
	(172, 'Oman'),
	(173, 'Panama'),
	(174, 'Peru'),
	(175, 'French Polynesia'),
	(176, 'Papua New Guinea'),
	(177, 'Philippines'),
	(178, 'Pakistan'),
	(179, 'Poland'),
	(180, 'Saint Pierre and Miquelon'),
	(181, 'Pitcairn Islands'),
	(182, 'Puerto Rico'),
	(183, 'Palestine'),
	(184, 'Portugal'),
	(185, 'Palau'),
	(186, 'Paraguay'),
	(187, 'Qatar'),
	(188, 'Réunion'),
	(189, 'Romania'),
	(190, 'Serbia'),
	(191, 'Russia'),
	(192, 'Rwanda'),
	(193, 'Saudi Arabia'),
	(194, 'Solomon Islands'),
	(195, 'Seychelles'),
	(196, 'Sudan'),
	(197, 'Sweden'),
	(198, 'Singapore'),
	(199, 'Saint Helena'),
	(200, 'Slovenia'),
	(201, 'Svalbard and Jan Mayen'),
	(202, 'Slovakia'),
	(203, 'Sierra Leone'),
	(204, 'San Marino'),
	(205, 'Senegal'),
	(206, 'Somalia'),
	(207, 'Suriname'),
	(208, 'South Sudan'),
	(209, 'São Tomé and Príncipe'),
	(210, 'El Salvador'),
	(211, 'Sint Maarten'),
	(212, 'Syria'),
	(213, 'Swaziland'),
	(214, 'Turks and Caicos Islands'),
	(215, 'Chad'),
	(216, 'French Southern Territories'),
	(217, 'Togo'),
	(218, 'Thailand'),
	(219, 'Tajikistan'),
	(220, 'Tokelau'),
	(221, 'East Timor'),
	(222, 'Turkmenistan'),
	(223, 'Tunisia'),
	(224, 'Tonga'),
	(225, 'Turkey'),
	(226, 'Trinidad and Tobago'),
	(227, 'Tuvalu'),
	(228, 'Taiwan'),
	(229, 'Tanzania'),
	(230, 'Ukraine'),
	(231, 'Uganda'),
	(232, 'U.S. Minor Outlying Islands'),
	(233, 'United States'),
	(234, 'Uruguay'),
	(235, 'Uzbekistan'),
	(236, 'Vatican City'),
	(237, 'Saint Vincent and the Grenadines'),
	(238, 'Venezuela'),
	(239, 'British Virgin Islands'),
	(240, 'U.S. Virgin Islands'),
	(241, 'Vietnam'),
	(242, 'Vanuatu'),
	(243, 'Wallis and Futuna'),
	(244, 'Samoa'),
	(245, 'Kosovo'),
	(246, 'Yemen'),
	(247, 'Mayotte'),
	(248, 'South Africa'),
	(249, 'Zambia'),
	(250, 'Zimbabwe');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.friends
DROP TABLE IF EXISTS `friends`;
CREATE TABLE IF NOT EXISTS `friends` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) unsigned NOT NULL DEFAULT '0',
  `edited_user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `status` int(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_friends_users` (`owner_id`),
  KEY `FK_friends_users_2` (`edited_user_id`),
  CONSTRAINT `FK_friends_users` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_friends_users_2` FOREIGN KEY (`edited_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.friends: ~1 rows (приблизительно)
DELETE FROM `friends`;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
INSERT INTO `friends` (`id`, `owner_id`, `edited_user_id`, `status`) VALUES
	(78, 24, 23, 1);
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.images
DROP TABLE IF EXISTS `images`;
CREATE TABLE IF NOT EXISTS `images` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) unsigned NOT NULL,
  `album_id` int(10) unsigned NOT NULL,
  `creation_date` bigint(20) unsigned NOT NULL,
  `filename` varchar(200) NOT NULL,
  `title` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_images_users` (`owner_id`),
  KEY `FK_images_albums` (`album_id`),
  CONSTRAINT `FK_images_albums` FOREIGN KEY (`album_id`) REFERENCES `albums` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_images_users` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.images: ~0 rows (приблизительно)
DELETE FROM `images`;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.messages
DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned NOT NULL,
  `receiver_id` int(10) unsigned NOT NULL,
  `creation_date` bigint(20) unsigned NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `message` varchar(1000) NOT NULL,
  `is_readed` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1` (`sender_id`),
  KEY `FK_messages_users` (`receiver_id`),
  CONSTRAINT `FK1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_messages_users` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=420 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.messages: ~0 rows (приблизительно)
DELETE FROM `messages`;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.news
DROP TABLE IF EXISTS `news`;
CREATE TABLE IF NOT EXISTS `news` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned NOT NULL,
  `message_type` int(10) unsigned NOT NULL,
  `object_id` int(11) NOT NULL,
  `creation_date` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_news_users` (`sender_id`),
  CONSTRAINT `FK_news_users` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.news: ~2 rows (приблизительно)
DELETE FROM `news`;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` (`id`, `sender_id`, `message_type`, `object_id`, `creation_date`) VALUES
	(36, 23, 1, 24, '1428848428456'),
	(37, 24, 1, 23, '1428848428465');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(20) NOT NULL,
  `second_name` varchar(20) NOT NULL,
  `gender` int(1) unsigned NOT NULL,
  `country_id` int(10) unsigned NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `school` varchar(20) NOT NULL,
  `university` varchar(20) NOT NULL,
  `hash` varchar(100) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `date_of_birth` varchar(10) NOT NULL,
  `city` varchar(30) DEFAULT NULL,
  `avatar_path` varchar(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_users_countries` (`country_id`),
  FULLTEXT KEY `second_name` (`second_name`),
  FULLTEXT KEY `first_name` (`first_name`),
  FULLTEXT KEY `phone_number` (`phone_number`),
  FULLTEXT KEY `school` (`school`),
  FULLTEXT KEY `university` (`university`),
  FULLTEXT KEY `emailfulltext` (`email`),
  FULLTEXT KEY `city` (`city`),
  CONSTRAINT `FK_users_countries` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы social_network.users: ~7 rows (приблизительно)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `first_name`, `second_name`, `gender`, `country_id`, `phone_number`, `email`, `school`, `university`, `hash`, `password`, `date_of_birth`, `city`, `avatar_path`) VALUES
	(11, 'test1', 'test1', 1, 191, '+3456463', 'test1@test1.com', '11', 'ХНУРИ', '', 'test1', '03-03-1978', 'Москва', 'galleries/avatars/default_avatar.jpg'),
	(12, 'test2', 'test2', 1, 230, '+384353845', 'test2@test2.com', '657', 'ЧБД', '', 'test2', '23-03-1979', 'ЧК', 'galleries/avatars/default_avatar.jpg'),
	(13, 'test3', 'test3', 1, 191, '+48235253', 'test3@test3.com', '1', 'ЧИБИС', '', 'test3', '12-03-1977', 'Воронеж', 'galleries/avatars/default_avatar.jpg'),
	(14, 'test4', 'test4', 2, 233, '+38067387', 'test4@test4.com', '66', 'MIT', '', 'test4', '01-11-1956', 'Putnam', 'galleries/avatars/default_avatar.jpg'),
	(17, 'test5', 'test5', 1, 230, '+097564664', 'test5@test5.com', '235', 'ЧДТУ', '', 'test5', '15-02-1976', 'Киев', 'galleries/avatars/default_avatar.jpg'),
	(23, 'Іван', 'Іванов', 1, 191, '+784353845', 'ivanov@ivanov.com', '25', 'ЧИБИС', '8f07940d-4b17-40d1-820b-931c10116fb6', 'IVANOV', '12-09-1978', NULL, 'galleries/avatars/default_avatar.jpg'),
	(24, 'Петр', 'Петров', 1, 230, '+380979124', 'petrov@petrov.com', '34', 'ЧДТУ', 'f7e6f54e-1e18-4cd9-863b-cf5b07495cba', 'petrov', '23-03-1979', NULL, 'galleries/avatars/default_avatar.jpg');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;


-- Дамп структуры для таблица social_network.wall_messages
DROP TABLE IF EXISTS `wall_messages`;
CREATE TABLE IF NOT EXISTS `wall_messages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned NOT NULL,
  `receiver_id` int(10) unsigned NOT NULL,
  `creation_date` bigint(20) unsigned NOT NULL,
  `message` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1` (`sender_id`),
  KEY `FK_messages_users` (`receiver_id`),
  CONSTRAINT `wall_messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `wall_messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- Дамп данных таблицы social_network.wall_messages: ~0 rows (приблизительно)
DELETE FROM `wall_messages`;
/*!40000 ALTER TABLE `wall_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `wall_messages` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
