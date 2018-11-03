-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 27 Paź 2018, 17:33
-- Wersja serwera: 10.1.36-MariaDB
-- Wersja PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `apartamenty`
--
CREATE DATABASE IF NOT EXISTS `apartamenty` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `apartamenty`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `apartment`
--

DROP TABLE IF EXISTS `apartment`;
CREATE TABLE IF NOT EXISTS `apartment` (
  `id_apartment` int(10) NOT NULL AUTO_INCREMENT,
  `id_hotel` int(11) DEFAULT NULL,
  `name` varchar(60) COLLATE utf8_polish_ci DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_apartment`),
  UNIQUE KEY `id_UNIQUE` (`id_apartment`),
  KEY `room_hotel_fk_idx` (`id_hotel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `city`
--

DROP TABLE IF EXISTS `city`;
CREATE TABLE IF NOT EXISTS `city` (
  `id_city` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_polish_ci DEFAULT NULL,
  `country_code` varchar(3) COLLATE utf8_polish_ci DEFAULT NULL,
  `state` varchar(100) COLLATE utf8_polish_ci DEFAULT NULL,
  `postal_code` varchar(10) COLLATE utf8_polish_ci DEFAULT NULL,
  PRIMARY KEY (`id_city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `city`
--

INSERT INTO `city` (`id_city`, `name`, `country_code`, `state`, `postal_code`) VALUES
(1, 'Warszawa', 'PL', 'Mazowieckie', '00-300');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hotel`
--

DROP TABLE IF EXISTS `hotel`;
CREATE TABLE IF NOT EXISTS `hotel` (
  `id_hotel` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_polish_ci DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `description` varchar(2000) COLLATE utf8_polish_ci DEFAULT NULL,
  `id_owner` int(10) DEFAULT NULL,
  `id_city` int(11) DEFAULT NULL,
  `street` varchar(200) COLLATE utf8_polish_ci DEFAULT NULL,
  PRIMARY KEY (`id_hotel`),
  UNIQUE KEY `id_UNIQUE` (`id_hotel`),
  KEY `hotel_user_fk_idx` (`id_owner`),
  KEY `hotel_city_fk_idx` (`id_city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `profile`
--

DROP TABLE IF EXISTS `profile`;
CREATE TABLE IF NOT EXISTS `profile` (
  `id_profile` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) COLLATE utf8_polish_ci DEFAULT NULL,
  PRIMARY KEY (`id_profile`),
  UNIQUE KEY `id_UNIQUE` (`id_profile`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `id_reservation` int(10) NOT NULL  AUTO_INCREMENT,
  `date_start` date DEFAULT NULL,
  `date_end` date DEFAULT NULL,
  `price` float DEFAULT NULL,
  `id_apartment` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_reservation`),
  UNIQUE KEY `id_UNIQUE` (`id_reservation`),
  KEY `reservation_room_fk_idx` (`id_apartment`),
  KEY `reservation_user_fk_idx` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(40) COLLATE utf8_polish_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci DEFAULT NULL,
  `lastname` varchar(45) COLLATE utf8_polish_ci DEFAULT NULL,
  `phone` varchar(45) COLLATE utf8_polish_ci DEFAULT NULL,
  `id_profile` int(11) DEFAULT NULL,
  `id_city` int(11) DEFAULT NULL,
  `street` varchar(200) COLLATE utf8_polish_ci DEFAULT NULL,
  `enabled` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_UNIQUE` (`id_user`),
  KEY `user_profile_fk_idx` (`id_profile`),
  KEY `user_city_fk_idx` (`id_city`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;



--
-- Ograniczenia dla tabeli `apartment`
--
ALTER TABLE `apartment`
  ADD CONSTRAINT `room_hotel_fk` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id_hotel`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `hotel`
--
ALTER TABLE `hotel`
  ADD CONSTRAINT `hotel_city_fk` FOREIGN KEY (`id_city`) REFERENCES `city` (`id_city`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `hotel_user_owner_fk` FOREIGN KEY (`id_owner`) REFERENCES `user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_apartment_fk` FOREIGN KEY (`id_apartment`) REFERENCES `apartment` (`id_apartment`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `reservation_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_city_fk` FOREIGN KEY (`id_city`) REFERENCES `city` (`id_city`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_profile_fk` FOREIGN KEY (`id_profile`) REFERENCES `profile` (`id_profile`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
