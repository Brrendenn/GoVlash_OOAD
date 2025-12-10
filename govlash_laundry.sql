-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 10, 2025 at 02:01 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `govlash_laundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` varchar(10) NOT NULL,
  `recipient_id` int(11) NOT NULL,
  `message` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_read` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`notification_id`, `recipient_id`, `message`, `created_at`, `is_read`) VALUES
('NT001', 2, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-04 02:58:41', 0),
('NT002', 5, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-05 01:07:40', 0),
('NT003', 7, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-05 01:10:09', 1),
('NT004', 7, 'Your order is finished and ready for pickup. Thank you for choosing our service!', '2025-12-05 01:25:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `service_id` varchar(10) NOT NULL,
  `service_name` varchar(50) NOT NULL,
  `service_desc` varchar(250) NOT NULL,
  `service_price` double NOT NULL,
  `service_duration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`service_id`, `service_name`, `service_desc`, `service_price`, `service_duration`) VALUES
('SV001', 'Wash & Fold', 'Basic washing and folding service', 5, 2),
('SV002', 'Dry Clean', 'Chemical cleaning for suits', 15.5, 3),
('SV999', 'Premium Wash', 'Deep Clean', 20, 2);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` varchar(10) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `service_id` varchar(10) NOT NULL,
  `receptionist_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `transaction_date` date NOT NULL,
  `total_weight` int(11) NOT NULL,
  `transaction_notes` varchar(250) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `customer_id`, `service_id`, `receptionist_id`, `staff_id`, `transaction_date`, `total_weight`, `transaction_notes`, `status`) VALUES
('TR001', 2, 'SV001', 6, 3, '2023-10-01', 5, 'Please separate whites', 'Pending'),
('TR002', 5, 'SV999', 6, 2, '2025-12-04', 5, 'Extra soft please', 'Finished'),
('TR003', 7, 'SV002', 6, 2, '2025-12-05', 10, 'Extra Soft Please', 'Finished'),
('TR004', 7, 'SV002', 6, 2, '2025-12-05', 20, 'Make it super dry', 'Finished'),
('TR005', 7, 'SV001', 6, 3, '2025-12-05', 20, 'Wash cleanly', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `dob` date NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `gender`, `dob`, `role`) VALUES
(1, 'admin', 'admin@govlash.com', 'admin123', 'Male', '1990-01-01', 'Admin'),
(2, 'staff01', 'staff01@govlash.com', '123456', 'Male', '2000-12-07', 'Laundry Staff'),
(3, 'staff02', 'staff02@govlash.com', '1234567', 'Male', '1999-12-16', 'Laundry Staff'),
(4, 'staff03', 'staff@govlash.com', '123456', 'Male', '1995-12-01', 'Laundry Staff'),
(5, 'newuser', 'user@email.com', '123456', 'Male', '2000-12-01', 'Customer'),
(6, 'recep01', 'recep@govlash.com', '123456', 'Male', '1999-12-01', 'Receptionist'),
(7, 'cust2', 'customer@email.com', '123456', 'Female', '2003-12-18', 'Customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `recipient_id` (`recipient_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`service_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `service_id` (`service_id`),
  ADD KEY `receptionist_id` (`receptionist_id`),
  ADD KEY `staff_id` (`staff_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`receptionist_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `transactions_ibfk_4` FOREIGN KEY (`staff_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
