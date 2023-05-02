-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 02, 2023 at 05:06 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `life_helper`
--

-- --------------------------------------------------------

--
-- Table structure for table `chat`
--

CREATE TABLE `chat` (
  `id` int(255) NOT NULL,
  `from` varchar(255) NOT NULL,
  `to` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `reply_to` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `notified` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `interacted`
--

CREATE TABLE `interacted` (
  `id` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `interacted_with` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  `interaction_type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `interacted`
--

INSERT INTO `interacted` (`id`, `username`, `interacted_with`, `date`, `interaction_type`) VALUES
(1, 'Berta', 'Erik', '2023-02-25 14:16:53', 'img');

-- --------------------------------------------------------

--
-- Table structure for table `message_between`
--

CREATE TABLE `message_between` (
  `id` int(255) NOT NULL,
  `user_1` varchar(255) NOT NULL,
  `user_2` varchar(255) NOT NULL,
  `last_message` varchar(255) NOT NULL,
  `last_message_from` int(255) NOT NULL,
  `last_message_timestamp` datetime NOT NULL,
  `read` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `past_date`
--

CREATE TABLE `past_date` (
  `id` int(255) NOT NULL,
  `partner_finder` int(255) NOT NULL,
  `partner_found` datetime NOT NULL,
  `date` datetime NOT NULL,
  `available` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `restaurants`
--

CREATE TABLE `restaurants` (
  `id` int(255) NOT NULL,
  `restaurant_name` varchar(255) NOT NULL,
  `creator_username` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `opening_hours` varchar(255) NOT NULL,
  `closing_hours` varchar(255) NOT NULL,
  `map_link_address` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `logo` varchar(255) NOT NULL,
  `current_status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`id`, `restaurant_name`, `creator_username`, `address`, `city`, `country`, `opening_hours`, `closing_hours`, `map_link_address`, `phone_number`, `logo`, `current_status`) VALUES
(1, 'The Drunken Taco', 'Berta', 'Agrabad', 'Chittagong', 'Bangladesh', '10:30', '20:30', 'https://maps.google.com', '05646816541685', 'specific_restaurants_data/Berta/logo.jpg', 'active'),
(2, 'Sammies', 'Mark', 'Agrabad', 'Chittagong', 'Bangladesh', '10:30', '20:30', 'https://maps.google.com', '05646816541685', 'specific_restaurants_data/Mark/logo.jpg', 'active'),
(3, 'un·cooked', 'Brady', 'Agrabad', 'Chittagong', 'Bangladesh', '10:30', '20:30', 'https://maps.google.com', '05646816541685', 'specific_restaurants_data/Brady/logo.jpg', 'active'),
(4, 'Water Pig BBQ', 'Erica', 'Agrabad', 'Chittagong', 'Bangladesh', '10:30', '20:30', 'https://maps.google.com', '05646816541685', 'specific_restaurants_data/Erica/logo.jpg', 'active');

-- --------------------------------------------------------

--
-- Table structure for table `restaurant_feed`
--

CREATE TABLE `restaurant_feed` (
  `id` int(255) NOT NULL,
  `restaurant_id` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `latitude` varchar(255) NOT NULL,
  `longitude` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `img` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `hobby` varchar(255) NOT NULL,
  `char_type` varchar(255) NOT NULL,
  `partner` varchar(255) NOT NULL,
  `last_active` varchar(255) NOT NULL,
  `verification` varchar(255) NOT NULL,
  `birth_year` varchar(255) NOT NULL,
  `height` varchar(255) NOT NULL,
  `money` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `mail`, `password`, `full_name`, `phone`, `latitude`, `longitude`, `city`, `country`, `img`, `user_type`, `gender`, `hobby`, `char_type`, `partner`, `last_active`, `verification`, `birth_year`, `height`, `money`) VALUES
(2, 'Berta', 'berta@gmail.com', '202cb962ac59075b964b07152d234b70', 'Berta', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'reading,writing', 'introvert', '', '', 'true', '2005', '56', 100),
(3, 'Mark', 'makr@gmail.com', '202cb962ac59075b964b07152d234b70', 'Mark', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading,writing', 'introvert', '', '', 'true', '2004', '56', 0),
(4, 'Brady', 'brady@gmail.com', '202cb962ac59075b964b07152d234b70', 'Brady', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'playing', 'extrovert', '', '', 'true', '2002', '46', 0),
(5, 'Erica', 'erica@gmail.com', '202cb962ac59075b964b07152d234b70', 'Erica', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'playing', 'extrovert', '', '', 'true', '2007', '57', 0),
(6, 'Erik', 'erik@gmail.com', '202cb962ac59075b964b07152d234b70', 'Erik', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'playing', 'introvert', '', '', 'true', '2006', '63', 0),
(7, 'Olivia', 'olivia@gmail.com', '202cb962ac59075b964b07152d234b70', 'Olivia', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'playing', 'introvert', '', '', 'true', '2003', '60', 0),
(8, 'Lesa', 'lesa@gmail.com', '202cb962ac59075b964b07152d234b70', 'Lesa', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'writing', 'extrovert', '', '', 'true', '2007', '60', 0),
(9, 'Pam', 'pam@gmail.com', '202cb962ac59075b964b07152d234b70', 'Pam', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'writing', 'extrovert', '', '', 'true', '2004', '72', 0),
(10, 'Roger', 'roger@gmail.com', '202cb962ac59075b964b07152d234b70', 'Roger', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading', 'extrovert', '', '', 'true', '2006', '67', 0),
(11, 'Roy', 'roy@gmail.com', '202cb962ac59075b964b07152d234b70', 'Roy', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading', 'introvert', '', '', 'true', '2005', '68', 0),
(12, 'Sofia', 'sofia@gmail.com', '202cb962ac59075b964b07152d234b70', 'Sofia', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'reading,writing', 'introvert', '', '', 'true', '2001', '63', 0),
(13, 'stephen', 'stephen@gmail.com', '202cb962ac59075b964b07152d234b70', 'Stephen', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading,writing', 'introvert', '', '', 'true', '2002', '65', 0),
(14, 'Timmy', 'timmy@gmail.com', '202cb962ac59075b964b07152d234b70', 'Timmy', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading', 'introvert', '', '', 'true', '2003', '55', 0),
(15, 'Wilford', 'wilford@gmail.com', '202cb962ac59075b964b07152d234b70', 'Wilford', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Male', 'reading', 'extrovert', '', '', 'true', '2003', '59', 0),
(16, 'Trisha', 'trisha@gmail.com', '202cb962ac59075b964b07152d234b70', 'Trisha', '16546532126', '0.0', '0.0', 'Chittagong', 'Bangladesh', 'profile.jpg', 'member', 'Female', 'reading', 'extrovert', '', '', 'true', '2004', '57', 0);

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `id` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `amount` varchar(255) NOT NULL,
  `datetime` datetime NOT NULL,
  `timezone` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `username`, `amount`, `datetime`, `timezone`, `type`) VALUES
(1, 'Berta', '100', '2023-03-09 18:21:57', 'libcore.util.ZoneInfo[mDstSavings=0,mUseDst=false,mDelegate=[id=\"Asia/Dhaka\",mRawOffset=21600000,mEarliestRawOffset=21700000,transitions=8]]', 'add');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `interacted`
--
ALTER TABLE `interacted`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `message_between`
--
ALTER TABLE `message_between`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `past_date`
--
ALTER TABLE `past_date`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `restaurants`
--
ALTER TABLE `restaurants`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `restaurant_feed`
--
ALTER TABLE `restaurant_feed`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wallet`
--
ALTER TABLE `wallet`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chat`
--
ALTER TABLE `chat`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `interacted`
--
ALTER TABLE `interacted`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `message_between`
--
ALTER TABLE `message_between`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `past_date`
--
ALTER TABLE `past_date`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `restaurants`
--
ALTER TABLE `restaurants`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `restaurant_feed`
--
ALTER TABLE `restaurant_feed`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `wallet`
--
ALTER TABLE `wallet`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
