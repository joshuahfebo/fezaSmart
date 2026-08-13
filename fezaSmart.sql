CREATE DATABASE fezasmart;
USE fezasmart;
CREATE TABLE `schools` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `location` varchar(255),
  `is_active` boolean DEFAULT true,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp,
  `deleted_by` integer,
  `restore_token` varchar(255)
);

CREATE TABLE `users` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255) UNIQUE NOT NULL,
  `hashed_password` varchar(255) NOT NULL,
  `email` varchar(255) UNIQUE,
  `phone` varchar(255),
  `is_active` boolean DEFAULT true,
  `email_verified` boolean DEFAULT false,
  `phone_verified` boolean DEFAULT false,
  `two_factor_enabled` boolean DEFAULT false,
  `two_factor_method` ENUM ('SMS', 'EMAIL', 'TOTP'),
  `two_factor_secret` varchar(255),
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `last_login_at` timestamp,
  `deleted_at` timestamp,
  `deleted_by` integer,
  `restore_token` varchar(255)
);

CREATE TABLE `roles` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` ENUM ('SUPER_ADMIN', 'SCHOOL_ADMIN', 'HEADMASTER', 'SECONDMASTER', 'DORMITORY', 'ACADEMIC', 'TEACHER', 'GUARD', 'PARENT', 'STUDENT') UNIQUE NOT NULL
);

CREATE TABLE `user_roles` (
  `user_id` integer NOT NULL,
  `role_id` integer NOT NULL
);

CREATE TABLE `departments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `school_id` integer NOT NULL,
  `head_staff_id` integer,
  `description` text,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `staff` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer UNIQUE NOT NULL,
  `school_id` integer NOT NULL,
  `department_id` integer,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `dob` date,
  `gender` ENUM ('MALE', 'FEMALE'),
  `staff_number` varchar(255) UNIQUE,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp,
  `deleted_by` integer,
  `restore_token` varchar(255)
);

CREATE TABLE `staff_roles` (
  `staff_id` integer NOT NULL,
  `role_id` integer NOT NULL,
  `assigned_at` timestamp DEFAULT (now())
);

CREATE TABLE `teacher_subjects` (
  `staff_id` integer NOT NULL,
  `subject_id` integer NOT NULL
);

CREATE TABLE `students` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer UNIQUE,
  `school_id` integer NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255),
  `last_name` varchar(255) NOT NULL,
  `control_number` varchar(255) UNIQUE NOT NULL,
  `dob` date,
  `gender` ENUM ('MALE', 'FEMALE'),
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp,
  `deleted_by` integer,
  `restore_token` varchar(255)
);

CREATE TABLE `student_enrollments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `class_id` integer NOT NULL,
  `academic_year_id` integer NOT NULL,
  `enrollment_date` date DEFAULT (now()),
  `is_current` boolean DEFAULT true
);

CREATE TABLE `parents` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer UNIQUE NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `relationship_type` ENUM ('FATHER', 'MOTHER', 'GUARDIAN', 'OTHER') NOT NULL,
  `gender` ENUM ('MALE', 'FEMALE'),
  `dob` date,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp,
  `deleted_by` integer,
  `restore_token` varchar(255)
);

CREATE TABLE `student_parents` (
  `student_id` integer NOT NULL,
  `parent_id` integer NOT NULL
);

CREATE TABLE `academic_years` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_current` boolean DEFAULT false
);

CREATE TABLE `terms` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `academic_year_id` integer NOT NULL,
  `term_number` integer NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL
);

CREATE TABLE `classes` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `school_id` integer NOT NULL,
  `academic_year_id` integer NOT NULL,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `class_assignments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `class_id` integer NOT NULL,
  `staff_id` integer NOT NULL,
  `role_in_class` ENUM ('PATRON', 'CLASS_TEACHER') NOT NULL,
  `assigned_date` timestamp DEFAULT (now())
);

CREATE TABLE `subjects` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,
  `type` ENUM ('CORE', 'SUBSIDIARY') NOT NULL
);

CREATE TABLE `combinations` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `class_id` integer NOT NULL,
  `timetable_id` integer,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `combination_subjects` (
  `combination_id` integer NOT NULL,
  `subject_id` integer NOT NULL
);

CREATE TABLE `timetables` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `school_id` integer NOT NULL,
  `academic_year_id` integer NOT NULL,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `lessons` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `timetable_id` integer NOT NULL,
  `subject_id` integer NOT NULL,
  `teacher_id` integer NOT NULL,
  `day_of_week` ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `room` varchar(255)
);

CREATE TABLE `attendance_records` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `class_id` integer NOT NULL,
  `date` date NOT NULL,
  `status` ENUM ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED') NOT NULL,
  `marked_by_staff_id` integer,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `leave_requests` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `requester_user_id` integer NOT NULL,
  `reason` text,
  `status` ENUM ('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
  `requested_at` timestamp DEFAULT (now()),
  `processed_by_staff_id` integer,
  `processed_at` timestamp
);

CREATE TABLE `permissions` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `leave_request_id` integer UNIQUE NOT NULL,
  `student_id` integer NOT NULL,
  `time_out_limit` timestamp NOT NULL,
  `time_in_limit` timestamp NOT NULL,
  `issued_by_staff_id` integer,
  `actual_time_out` timestamp,
  `actual_time_in` timestamp,
  `guard_out_staff_id` integer,
  `guard_in_staff_id` integer,
  `returned` boolean DEFAULT false
);

CREATE TABLE `guard_shifts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `staff_id` integer NOT NULL,
  `shift_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `is_active` boolean DEFAULT true
);

CREATE TABLE `exams` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `term_id` integer NOT NULL,
  `name` varchar(255) NOT NULL,
  `exam_date` date
);

CREATE TABLE `exam_subjects` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `exam_id` integer NOT NULL,
  `subject_id` integer NOT NULL,
  `max_score` decimal(7,2) NOT NULL
);

CREATE TABLE `student_scores` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `exam_subject_id` integer NOT NULL,
  `score` decimal(7,2) NOT NULL
);

CREATE TABLE `grade_boundaries` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `school_id` integer,
  `subject_id` integer,
  `exam_id` integer,
  `min_percentage` decimal(5,2) NOT NULL,
  `max_percentage` decimal(5,2) NOT NULL,
  `letter_grade` varchar(5),
  `point_grade` decimal(3,1),
  `remark` varchar(255),
  `type` ENUM ('LETTER', 'POINT', 'REMARK') NOT NULL
);

CREATE TABLE `results` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `exam_id` integer NOT NULL,
  `total_score` decimal(7,2),
  `average_percentage` decimal(5,2),
  `total_points` decimal(5,2),
  `division` varchar(255),
  `rank_in_class` integer,
  `remark` text,
  `computed_at` timestamp DEFAULT (now())
);

CREATE TABLE `transcripts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `academic_year_id` integer NOT NULL,
  `generated_by` integer,
  `transcript_data` text,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `fee_structures` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `school_id` integer NOT NULL,
  `academic_year_id` integer NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text
);

CREATE TABLE `fee_items` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `fee_structure_id` integer NOT NULL,
  `subject_id` integer,
  `class_id` integer,
  `name` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `is_optional` boolean DEFAULT false
);

CREATE TABLE `student_fee_assignments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `fee_structure_id` integer NOT NULL,
  `assigned_by` integer,
  `assigned_at` timestamp DEFAULT (now())
);

CREATE TABLE `discounts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `discount_type` varchar(255) NOT NULL,
  `value` decimal(10,2) NOT NULL,
  `start_date` date,
  `end_date` date,
  `is_active` boolean DEFAULT true
);

CREATE TABLE `scholarships` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `discount_id` integer,
  `start_date` date,
  `end_date` date,
  `is_active` boolean DEFAULT true
);

CREATE TABLE `student_scholarships` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `scholarship_id` integer NOT NULL,
  `awarded_by` integer,
  `awarded_date` date,
  `valid_until` date
);

CREATE TABLE `invoices` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `invoice_number` varchar(255) UNIQUE NOT NULL,
  `student_id` integer NOT NULL,
  `fee_structure_id` integer,
  `academic_year_id` integer,
  `term_id` integer,
  `total_amount` decimal(10,2) NOT NULL,
  `discount_amount` decimal(10,2) DEFAULT 0,
  `paid_amount` decimal(10,2) DEFAULT 0,
  `balance` decimal(10,2) DEFAULT 0,
  `status` ENUM ('DRAFT', 'ISSUED', 'PARTIAL', 'PAID', 'OVERDUE', 'CANCELLED') DEFAULT 'DRAFT',
  `issued_by` integer,
  `issued_date` date,
  `due_date` date
);

CREATE TABLE `invoice_items` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `invoice_id` integer NOT NULL,
  `fee_item_id` integer,
  `description` varchar(255),
  `quantity` decimal(7,2) DEFAULT 1,
  `unit_price` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL
);

CREATE TABLE `payments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `payment_number` varchar(255) UNIQUE NOT NULL,
  `invoice_id` integer,
  `student_id` integer NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_method` ENUM ('CASH', 'BANK_TRANSFER', 'MOBILE_MONEY', 'CARD', 'CHEQUE'),
  `transaction_reference` varchar(255),
  `payer_user_id` integer,
  `payment_date` timestamp DEFAULT (now()),
  `status` ENUM ('PENDING', 'PARTIAL', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
  `verified_by` integer,
  `verified_at` timestamp
);

CREATE TABLE `payment_allocations` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `payment_id` integer NOT NULL,
  `invoice_id` integer NOT NULL,
  `amount_allocated` decimal(10,2) NOT NULL
);

CREATE TABLE `receipts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `receipt_number` varchar(255) UNIQUE NOT NULL,
  `payment_id` integer UNIQUE NOT NULL,
  `receipt_date` timestamp DEFAULT (now()),
  `generated_by` integer,
  `receipt_data` text
);

CREATE TABLE `clubs` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `school_id` integer,
  `patron_staff_id` integer,
  `created_at` timestamp DEFAULT (now()),
  `is_active` boolean DEFAULT true
);

CREATE TABLE `club_members` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `club_id` integer NOT NULL,
  `student_id` integer NOT NULL,
  `joined_date` date DEFAULT (now()),
  `role_in_club` varchar(255),
  `is_active` boolean DEFAULT true
);

CREATE TABLE `violations` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `point_deduction` decimal(5,1) NOT NULL,
  `point_type` ENUM ('DORMITORY', 'CLASS') NOT NULL
);

CREATE TABLE `student_points` (
  `student_id` integer NOT NULL,
  `point_type` ENUM ('DORMITORY', 'CLASS') NOT NULL,
  `current_points` decimal(5,1) DEFAULT 0
);

CREATE TABLE `discipline_records` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `student_id` integer NOT NULL,
  `violation_id` integer NOT NULL,
  `staff_id` integer,
  `points_deducted` decimal(5,1) NOT NULL,
  `comment` text,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `password_reset_tokens` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `token` varchar(255) UNIQUE NOT NULL,
  `expires_at` timestamp NOT NULL,
  `used` boolean DEFAULT false,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `email_verification_tokens` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `token` varchar(255) UNIQUE NOT NULL,
  `expires_at` timestamp NOT NULL,
  `used` boolean DEFAULT false,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `two_factor_codes` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `code` varchar(6) NOT NULL,
  `expires_at` timestamp NOT NULL,
  `used` boolean DEFAULT false,
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `login_attempts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer,
  `ip_address` varchar(45),
  `attempted_at` timestamp DEFAULT (now()),
  `success` boolean NOT NULL
);

CREATE TABLE `sessions` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `token` varchar(255) UNIQUE NOT NULL,
  `refresh_token` varchar(255) UNIQUE,
  `token_type` ENUM ('ACCESS', 'REFRESH', 'API_KEY') DEFAULT 'ACCESS',
  `device_info` varchar(255),
  `ip_address` varchar(45),
  `expires_at` timestamp NOT NULL,
  `created_at` timestamp DEFAULT (now()),
  `revoked` boolean DEFAULT false
);

CREATE TABLE `api_keys` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `key_hash` varchar(255) NOT NULL,
  `school_id` integer,
  `permissions` text,
  `expires_at` timestamp,
  `created_at` timestamp DEFAULT (now()),
  `revoked` boolean DEFAULT false
);

CREATE TABLE `audit_logs` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer,
  `entity` varchar(255) NOT NULL,
  `entity_id` integer,
  `action` varchar(255) NOT NULL,
  `old_value` text,
  `new_value` text,
  `ip_address` varchar(45),
  `created_at` timestamp DEFAULT (now())
);

CREATE TABLE `notifications` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `recipient_user_id` integer NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text,
  `type` varchar(255),
  `is_read` boolean DEFAULT false,
  `created_at` timestamp DEFAULT (now())
);

CREATE UNIQUE INDEX `users_index_0` ON `users` (`username`);

CREATE UNIQUE INDEX `users_index_1` ON `users` (`email`);

CREATE INDEX `users_index_2` ON `users` (`deleted_at`);

CREATE UNIQUE INDEX `user_roles_index_3` ON `user_roles` (`user_id`, `role_id`);

CREATE INDEX `user_roles_index_4` ON `user_roles` (`user_id`);

CREATE INDEX `user_roles_index_5` ON `user_roles` (`role_id`);

CREATE INDEX `departments_index_6` ON `departments` (`school_id`);

CREATE INDEX `departments_index_7` ON `departments` (`head_staff_id`);

CREATE INDEX `staff_index_8` ON `staff` (`school_id`);

CREATE INDEX `staff_index_9` ON `staff` (`staff_number`);

CREATE INDEX `staff_index_10` ON `staff` (`department_id`);

CREATE UNIQUE INDEX `staff_roles_index_11` ON `staff_roles` (`staff_id`, `role_id`);

CREATE INDEX `staff_roles_index_12` ON `staff_roles` (`staff_id`);

CREATE INDEX `staff_roles_index_13` ON `staff_roles` (`role_id`);

CREATE UNIQUE INDEX `teacher_subjects_index_14` ON `teacher_subjects` (`staff_id`, `subject_id`);

CREATE INDEX `teacher_subjects_index_15` ON `teacher_subjects` (`staff_id`);

CREATE INDEX `teacher_subjects_index_16` ON `teacher_subjects` (`subject_id`);

CREATE INDEX `students_index_17` ON `students` (`control_number`);

CREATE INDEX `students_index_18` ON `students` (`school_id`);

CREATE INDEX `students_index_19` ON `students` (`last_name`, `first_name`);

CREATE INDEX `student_enrollments_index_20` ON `student_enrollments` (`student_id`);

CREATE INDEX `student_enrollments_index_21` ON `student_enrollments` (`class_id`);

CREATE INDEX `student_enrollments_index_22` ON `student_enrollments` (`academic_year_id`);

CREATE UNIQUE INDEX `student_enrollments_index_23` ON `student_enrollments` (`student_id`, `academic_year_id`);

CREATE UNIQUE INDEX `student_parents_index_24` ON `student_parents` (`student_id`, `parent_id`);

CREATE INDEX `student_parents_index_25` ON `student_parents` (`student_id`);

CREATE INDEX `student_parents_index_26` ON `student_parents` (`parent_id`);

CREATE UNIQUE INDEX `academic_years_index_27` ON `academic_years` (`name`);

CREATE INDEX `terms_index_28` ON `terms` (`academic_year_id`);

CREATE UNIQUE INDEX `terms_index_29` ON `terms` (`academic_year_id`, `term_number`);

CREATE INDEX `classes_index_30` ON `classes` (`school_id`);

CREATE INDEX `classes_index_31` ON `classes` (`academic_year_id`);

CREATE UNIQUE INDEX `class_assignments_index_32` ON `class_assignments` (`class_id`, `role_in_class`);

CREATE INDEX `class_assignments_index_33` ON `class_assignments` (`class_id`);

CREATE INDEX `class_assignments_index_34` ON `class_assignments` (`staff_id`);

CREATE INDEX `combinations_index_35` ON `combinations` (`class_id`);

CREATE INDEX `combinations_index_36` ON `combinations` (`timetable_id`);

CREATE UNIQUE INDEX `combination_subjects_index_37` ON `combination_subjects` (`combination_id`, `subject_id`);

CREATE INDEX `combination_subjects_index_38` ON `combination_subjects` (`combination_id`);

CREATE INDEX `combination_subjects_index_39` ON `combination_subjects` (`subject_id`);

CREATE INDEX `timetables_index_40` ON `timetables` (`school_id`);

CREATE INDEX `timetables_index_41` ON `timetables` (`academic_year_id`);

CREATE INDEX `lessons_index_42` ON `lessons` (`timetable_id`);

CREATE INDEX `lessons_index_43` ON `lessons` (`teacher_id`);

CREATE INDEX `lessons_index_44` ON `lessons` (`timetable_id`, `day_of_week`, `start_time`);

CREATE UNIQUE INDEX `attendance_records_index_45` ON `attendance_records` (`student_id`, `date`);

CREATE INDEX `attendance_records_index_46` ON `attendance_records` (`class_id`);

CREATE INDEX `attendance_records_index_47` ON `attendance_records` (`date`);

CREATE INDEX `leave_requests_index_48` ON `leave_requests` (`student_id`);

CREATE INDEX `leave_requests_index_49` ON `leave_requests` (`requester_user_id`);

CREATE INDEX `leave_requests_index_50` ON `leave_requests` (`processed_by_staff_id`);

CREATE INDEX `permissions_index_51` ON `permissions` (`student_id`);

CREATE INDEX `permissions_index_52` ON `permissions` (`leave_request_id`);

CREATE INDEX `permissions_index_53` ON `permissions` (`guard_out_staff_id`);

CREATE INDEX `permissions_index_54` ON `permissions` (`guard_in_staff_id`);

CREATE INDEX `guard_shifts_index_55` ON `guard_shifts` (`staff_id`);

CREATE UNIQUE INDEX `guard_shifts_index_56` ON `guard_shifts` (`staff_id`, `shift_date`);

CREATE INDEX `exams_index_57` ON `exams` (`term_id`);

CREATE UNIQUE INDEX `exam_subjects_index_58` ON `exam_subjects` (`exam_id`, `subject_id`);

CREATE INDEX `exam_subjects_index_59` ON `exam_subjects` (`exam_id`);

CREATE INDEX `exam_subjects_index_60` ON `exam_subjects` (`subject_id`);

CREATE UNIQUE INDEX `student_scores_index_61` ON `student_scores` (`student_id`, `exam_subject_id`);

CREATE INDEX `student_scores_index_62` ON `student_scores` (`student_id`);

CREATE INDEX `student_scores_index_63` ON `student_scores` (`exam_subject_id`);

CREATE INDEX `grade_boundaries_index_64` ON `grade_boundaries` (`school_id`);

CREATE INDEX `grade_boundaries_index_65` ON `grade_boundaries` (`subject_id`);

CREATE INDEX `grade_boundaries_index_66` ON `grade_boundaries` (`exam_id`);

CREATE INDEX `grade_boundaries_index_67` ON `grade_boundaries` (`min_percentage`, `max_percentage`);

CREATE UNIQUE INDEX `results_index_68` ON `results` (`student_id`, `exam_id`);

CREATE INDEX `results_index_69` ON `results` (`exam_id`);

CREATE UNIQUE INDEX `transcripts_index_70` ON `transcripts` (`student_id`, `academic_year_id`);

CREATE INDEX `fee_structures_index_71` ON `fee_structures` (`school_id`);

CREATE INDEX `fee_structures_index_72` ON `fee_structures` (`academic_year_id`);

CREATE INDEX `fee_items_index_73` ON `fee_items` (`fee_structure_id`);

CREATE INDEX `fee_items_index_74` ON `fee_items` (`subject_id`);

CREATE INDEX `fee_items_index_75` ON `fee_items` (`class_id`);

CREATE UNIQUE INDEX `student_fee_assignments_index_76` ON `student_fee_assignments` (`student_id`, `fee_structure_id`);

CREATE INDEX `scholarships_index_77` ON `scholarships` (`discount_id`);

CREATE UNIQUE INDEX `student_scholarships_index_78` ON `student_scholarships` (`student_id`, `scholarship_id`);

CREATE INDEX `invoices_index_79` ON `invoices` (`student_id`);

CREATE INDEX `invoices_index_80` ON `invoices` (`academic_year_id`);

CREATE INDEX `invoices_index_81` ON `invoices` (`term_id`);

CREATE UNIQUE INDEX `invoices_index_82` ON `invoices` (`invoice_number`);

CREATE INDEX `invoice_items_index_83` ON `invoice_items` (`invoice_id`);

CREATE INDEX `payments_index_84` ON `payments` (`invoice_id`);

CREATE INDEX `payments_index_85` ON `payments` (`student_id`);

CREATE UNIQUE INDEX `payments_index_86` ON `payments` (`payment_number`);

CREATE INDEX `payment_allocations_index_87` ON `payment_allocations` (`payment_id`);

CREATE UNIQUE INDEX `payment_allocations_index_88` ON `payment_allocations` (`payment_id`, `invoice_id`);

CREATE UNIQUE INDEX `receipts_index_89` ON `receipts` (`payment_id`);

CREATE INDEX `clubs_index_90` ON `clubs` (`school_id`);

CREATE INDEX `clubs_index_91` ON `clubs` (`patron_staff_id`);

CREATE UNIQUE INDEX `club_members_index_92` ON `club_members` (`club_id`, `student_id`);

CREATE INDEX `violations_index_93` ON `violations` (`point_type`);

CREATE UNIQUE INDEX `student_points_index_94` ON `student_points` (`student_id`, `point_type`);

CREATE INDEX `discipline_records_index_95` ON `discipline_records` (`student_id`);

CREATE INDEX `discipline_records_index_96` ON `discipline_records` (`violation_id`);

CREATE INDEX `discipline_records_index_97` ON `discipline_records` (`staff_id`);

CREATE UNIQUE INDEX `password_reset_tokens_index_98` ON `password_reset_tokens` (`token`);

CREATE INDEX `password_reset_tokens_index_99` ON `password_reset_tokens` (`user_id`);

CREATE UNIQUE INDEX `email_verification_tokens_index_100` ON `email_verification_tokens` (`token`);

CREATE INDEX `email_verification_tokens_index_101` ON `email_verification_tokens` (`user_id`);

CREATE INDEX `two_factor_codes_index_102` ON `two_factor_codes` (`user_id`);

CREATE INDEX `two_factor_codes_index_103` ON `two_factor_codes` (`user_id`, `code`);

CREATE INDEX `login_attempts_index_104` ON `login_attempts` (`user_id`);

CREATE INDEX `login_attempts_index_105` ON `login_attempts` (`ip_address`);

CREATE INDEX `login_attempts_index_106` ON `login_attempts` (`attempted_at`);

CREATE UNIQUE INDEX `sessions_index_107` ON `sessions` (`token`);

CREATE UNIQUE INDEX `sessions_index_108` ON `sessions` (`refresh_token`);

CREATE INDEX `sessions_index_109` ON `sessions` (`user_id`);

CREATE UNIQUE INDEX `api_keys_index_110` ON `api_keys` (`key_hash`);

CREATE INDEX `api_keys_index_111` ON `api_keys` (`school_id`);

CREATE INDEX `audit_logs_index_112` ON `audit_logs` (`user_id`);

CREATE INDEX `audit_logs_index_113` ON `audit_logs` (`entity`);

CREATE INDEX `audit_logs_index_114` ON `audit_logs` (`entity`, `entity_id`);

CREATE INDEX `audit_logs_index_115` ON `audit_logs` (`action`);

CREATE INDEX `audit_logs_index_116` ON `audit_logs` (`created_at`);

CREATE INDEX `notifications_index_117` ON `notifications` (`recipient_user_id`);

CREATE INDEX `notifications_index_118` ON `notifications` (`recipient_user_id`, `is_read`);

ALTER TABLE `staff` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `students` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `fee_structures` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `clubs` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `departments` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `staff` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `students` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `parents` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

ALTER TABLE `departments` ADD FOREIGN KEY (`head_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `staff` ADD FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`);

ALTER TABLE `staff_roles` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `staff_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

ALTER TABLE `teacher_subjects` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `teacher_subjects` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `classes` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `classes` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `class_assignments` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `class_assignments` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `student_enrollments` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_enrollments` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `student_enrollments` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `student_parents` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_parents` ADD FOREIGN KEY (`parent_id`) REFERENCES `parents` (`id`);

ALTER TABLE `combinations` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `combinations` ADD FOREIGN KEY (`timetable_id`) REFERENCES `timetables` (`id`);

ALTER TABLE `combination_subjects` ADD FOREIGN KEY (`combination_id`) REFERENCES `combinations` (`id`);

ALTER TABLE `combination_subjects` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `timetables` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `lessons` ADD FOREIGN KEY (`timetable_id`) REFERENCES `timetables` (`id`);

ALTER TABLE `lessons` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `lessons` ADD FOREIGN KEY (`teacher_id`) REFERENCES `staff` (`id`);

ALTER TABLE `attendance_records` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `attendance_records` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `attendance_records` ADD FOREIGN KEY (`marked_by_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `leave_requests` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `leave_requests` ADD FOREIGN KEY (`requester_user_id`) REFERENCES `users` (`id`);

ALTER TABLE `leave_requests` ADD FOREIGN KEY (`processed_by_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `leave_requests` ADD FOREIGN KEY (`id`) REFERENCES `permissions` (`leave_request_id`);

ALTER TABLE `permissions` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `permissions` ADD FOREIGN KEY (`issued_by_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `permissions` ADD FOREIGN KEY (`guard_out_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `permissions` ADD FOREIGN KEY (`guard_in_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `guard_shifts` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `exams` ADD FOREIGN KEY (`term_id`) REFERENCES `terms` (`id`);

ALTER TABLE `exam_subjects` ADD FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`);

ALTER TABLE `exam_subjects` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `student_scores` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_scores` ADD FOREIGN KEY (`exam_subject_id`) REFERENCES `exam_subjects` (`id`);

ALTER TABLE `grade_boundaries` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `grade_boundaries` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `grade_boundaries` ADD FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`);

ALTER TABLE `results` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `results` ADD FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`);

ALTER TABLE `transcripts` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `transcripts` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `transcripts` ADD FOREIGN KEY (`generated_by`) REFERENCES `staff` (`id`);

ALTER TABLE `fee_structures` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `fee_items` ADD FOREIGN KEY (`fee_structure_id`) REFERENCES `fee_structures` (`id`);

ALTER TABLE `fee_items` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `fee_items` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `student_fee_assignments` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_fee_assignments` ADD FOREIGN KEY (`fee_structure_id`) REFERENCES `fee_structures` (`id`);

ALTER TABLE `student_fee_assignments` ADD FOREIGN KEY (`assigned_by`) REFERENCES `staff` (`id`);

ALTER TABLE `scholarships` ADD FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`id`);

ALTER TABLE `student_scholarships` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_scholarships` ADD FOREIGN KEY (`scholarship_id`) REFERENCES `scholarships` (`id`);

ALTER TABLE `student_scholarships` ADD FOREIGN KEY (`awarded_by`) REFERENCES `staff` (`id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`fee_structure_id`) REFERENCES `fee_structures` (`id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`term_id`) REFERENCES `terms` (`id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`issued_by`) REFERENCES `staff` (`id`);

ALTER TABLE `invoice_items` ADD FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`);

ALTER TABLE `invoice_items` ADD FOREIGN KEY (`fee_item_id`) REFERENCES `fee_items` (`id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`payer_user_id`) REFERENCES `users` (`id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`verified_by`) REFERENCES `staff` (`id`);

ALTER TABLE `payment_allocations` ADD FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`);

ALTER TABLE `payment_allocations` ADD FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`);

ALTER TABLE `receipts` ADD FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`);

ALTER TABLE `receipts` ADD FOREIGN KEY (`generated_by`) REFERENCES `staff` (`id`);

ALTER TABLE `clubs` ADD FOREIGN KEY (`patron_staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `club_members` ADD FOREIGN KEY (`club_id`) REFERENCES `clubs` (`id`);

ALTER TABLE `club_members` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `student_points` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `discipline_records` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `discipline_records` ADD FOREIGN KEY (`violation_id`) REFERENCES `violations` (`id`);

ALTER TABLE `discipline_records` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`);

ALTER TABLE `password_reset_tokens` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `email_verification_tokens` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `two_factor_codes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `login_attempts` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `sessions` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `api_keys` ADD FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);

ALTER TABLE `audit_logs` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`recipient_user_id`) REFERENCES `users` (`id`);
