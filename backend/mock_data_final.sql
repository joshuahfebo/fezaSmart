-- ============================================================
-- FEZA SMART MOCK DATA - Minimal Test Dataset
-- ============================================================
-- Using exact column names from Hibernate-generated schema

-- Password: Feza@2024 (hashed with BCrypt)
-- Hash: $2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG

-- ============================================================
-- 1. SUPER ADMINS
-- ============================================================
INSERT INTO user (username, hashed_password, email, phone, is_active, email_verified) VALUES
('admin_super', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'admin@fezasmart.com', '+254712345678', TRUE, TRUE),
('director_main', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'director@fezasmart.com', '+254712345679', TRUE, TRUE);

-- ============================================================
-- 2. ACADEMIC YEARS
-- ============================================================
INSERT INTO academic_year (name, start_date, end_date, is_current) VALUES
('2024', '2024-01-15', '2024-11-30', TRUE),
('2025', '2025-01-20', '2025-11-30', FALSE);

-- ============================================================
-- 3. TERMS
-- ============================================================
INSERT INTO term (academic_year_id, term_number, start_date, end_date) VALUES
(1, 1, '2024-01-15', '2024-04-12'),
(1, 2, '2024-05-06', '2024-08-09'),
(1, 3, '2024-08-19', '2024-11-15'),
(2, 1, '2025-01-20', '2025-04-18');

-- ============================================================
-- 4. SCHOOLS
-- ============================================================
INSERT INTO school (name, location, is_active) VALUES
('Kampala Senior School', 'Kampala, Uganda', TRUE),
('St. Mary''s Secondary', 'Kampala, Uganda', TRUE),
('Muyenga High School', 'Kampala, Uganda', TRUE);

-- ============================================================
-- 5. STAFF USERS (6 teachers + admin staff)
-- ============================================================
INSERT INTO user (username, hashed_password, email, phone, is_active, email_verified) VALUES
('headmaster_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'headmaster@kss.ug', '+256712111111', TRUE, TRUE),
('deputy_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'deputy@kss.ug', '+256712111112', TRUE, TRUE),
('hr_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'hr@kss.ug', '+256712111113', TRUE, TRUE),
('teacher_math', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'mwangi@kss.ug', '+256712111114', TRUE, TRUE),
('teacher_english', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'jane@kss.ug', '+256712111115', TRUE, TRUE),
('teacher_science', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'john@kss.ug', '+256712111116', TRUE, TRUE);

-- ============================================================
-- 6. STAFF RECORDS
-- ============================================================
INSERT INTO staff (user_id, school_id, first_name, last_name, gender) VALUES
(3, 1, 'David', 'Kipchoge', 'M'),
(4, 1, 'Margaret', 'Omondi', 'F'),
(5, 1, 'Robert', 'Njeri', 'M'),
(6, 1, 'Mwangi', 'Kariuki', 'M'),
(7, 1, 'Jane', 'Muthoni', 'F'),
(8, 1, 'John', 'Kosgey', 'M');

-- ============================================================
-- 7. CLASSES
-- ============================================================
INSERT INTO classs (name, school_id, academic_year_id) VALUES
('Form 1A', 1, 1),
('Form 1B', 1, 1),
('Form 2A', 1, 1),
('Form 3A', 1, 1),
('Form 4A', 1, 1);

-- ============================================================
-- 8. SUBJECTS
-- ============================================================
INSERT INTO subject (name, type) VALUES
('Mathematics', 'CORE'),
('English Language', 'CORE'),
('Biology', 'SCIENCE'),
('Chemistry', 'SCIENCE'),
('Physics', 'SCIENCE'),
('Geography', 'SOCIAL'),
('History', 'SOCIAL'),
('Kiswahili', 'LANGUAGE');

-- ============================================================
-- 9. PARENT USERS (4 parents)
-- ============================================================
INSERT INTO user (username, hashed_password, email, phone, is_active, email_verified) VALUES
('parent_kimani', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent1@email.com', '+256789111111', TRUE, TRUE),
('parent_nairobi', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent2@email.com', '+256789111112', TRUE, TRUE),
('parent_mombasa', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent3@email.com', '+256789111113', TRUE, TRUE),
('parent_nakuru', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent4@email.com', '+256789111114', TRUE, TRUE);

-- ============================================================
-- 10. PARENT RECORDS
-- ============================================================
INSERT INTO parent (user_id, first_name, last_name, relationship_type, gender) VALUES
(9, 'Samuel', 'Kimani', 'Father', 'M'),
(10, 'Anne', 'Nairobi', 'Mother', 'F'),
(11, 'Peter', 'Mombasa', 'Father', 'M'),
(12, 'Grace', 'Nakuru', 'Mother', 'F');

-- ============================================================
-- 11. STUDENT USERS (6 students)
-- ============================================================
INSERT INTO user (username, hashed_password, email, phone, is_active, email_verified) VALUES
('student_001', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student1@kss.ug', '+256701001001', TRUE, TRUE),
('student_002', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student2@kss.ug', '+256701001002', TRUE, TRUE),
('student_003', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student3@kss.ug', '+256701001003', TRUE, TRUE),
('student_004', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student4@kss.ug', '+256701001004', TRUE, TRUE),
('student_005', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student5@kss.ug', '+256701001005', TRUE, TRUE),
('student_006', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student6@kss.ug', '+256701001006', TRUE, TRUE);

-- ============================================================
-- 12. STUDENT RECORDS
-- ============================================================
INSERT INTO student (user_id, school_id, first_name, last_name, control_number, gender, dob) VALUES
(13, 1, 'David', 'Kipchoge', 'STU001', 'M', '2008-05-15'),
(14, 1, 'Sarah', 'Omondi', 'STU002', 'F', '2008-07-22'),
(15, 1, 'Michael', 'Kosgey', 'STU003', 'M', '2008-09-10'),
(16, 1, 'Grace', 'Muthoni', 'STU004', 'F', '2008-11-03'),
(17, 1, 'Joseph', 'Kimani', 'STU005', 'M', '2008-03-28'),
(18, 1, 'Linda', 'Kipkemoi', 'STU006', 'F', '2008-06-14');

-- ============================================================
-- 13. STUDENT ENROLLMENTS
-- ============================================================
INSERT INTO student_enrollment (student_id, classs_id, academic_year_id) VALUES
(1, 1, 1),
(2, 1, 1),
(3, 1, 1),
(4, 2, 1),
(5, 2, 1),
(6, 2, 1);

-- ============================================================
-- 14. EXAMS
-- ============================================================
INSERT INTO exam (name, term_id, exam_date) VALUES
('Midterm Test 1', 1, '2024-02-20'),
('End of Term 1 Exam', 1, '2024-04-05'),
('Midterm Test 2', 2, '2024-06-15'),
('End of Term 2 Exam', 2, '2024-07-30');

-- ============================================================
-- 15. EXAM SUBJECTS
-- ============================================================
INSERT INTO exam_subject (exam_id, subject_id, max_score) VALUES
(1, 1, 100), (1, 2, 100), (1, 3, 100), (1, 4, 100),
(2, 1, 100), (2, 2, 100), (2, 3, 100), (2, 4, 100), (2, 5, 100),
(3, 1, 100), (3, 2, 100), (3, 3, 100), (3, 4, 100),
(4, 1, 100), (4, 2, 100), (4, 3, 100), (4, 4, 100), (4, 5, 100);

-- ============================================================
-- 16. STUDENT SCORES
-- ============================================================
INSERT INTO student_score (student_id, exam_subject_id, score) VALUES
-- Student 1 Scores
(1, 1, 85.5), (1, 2, 78.0), (1, 3, 92.0), (1, 4, 88.5),
-- Student 2 Scores
(2, 1, 92.0), (2, 2, 85.5), (2, 3, 88.0), (2, 4, 91.0),
-- Student 3 Scores
(3, 1, 78.0), (3, 2, 82.0), (3, 3, 75.5), (3, 4, 80.0),
-- Student 4 Scores
(4, 1, 88.0), (4, 2, 90.5), (4, 3, 85.0), (4, 4, 87.5),
-- Student 5 Scores
(5, 1, 75.0), (5, 2, 80.0), (5, 3, 82.5), (5, 4, 78.0),
-- Student 6 Scores
(6, 1, 95.0), (6, 2, 93.5), (6, 3, 91.0), (6, 4, 94.5);

-- ============================================================
-- 17. RESULTS (Aggregated per Exam)
-- ============================================================
INSERT INTO result (student_id, exam_id, total_score, average_percentage, total_points, division, rank_in_class, remark, computed_at) VALUES
(1, 1, 343.5, 85.9, 86, 'A', 2, 'Excellent', NOW()),
(2, 1, 356.5, 89.1, 89, 'A', 1, 'Excellent', NOW()),
(3, 1, 315.5, 78.9, 79, 'B', 4, 'Good', NOW()),
(4, 1, 350.5, 87.6, 88, 'A', 3, 'Excellent', NOW()),
(5, 1, 315.5, 78.9, 79, 'B', 5, 'Good', NOW()),
(6, 1, 373.5, 93.4, 93, 'A+', 1, 'Excellent', NOW());

-- ============================================================
-- 18. ROLES
-- ============================================================
INSERT INTO role (name) VALUES
('SUPER_ADMIN'),
('HEAD_MASTER'),
('GENERAL_SECOND_MASTER'),
('HUMAN_RESOURCE'),
('SECOND_MASTER'),
('PARENT'),
('STUDENT');

-- ============================================================
-- 19. USER ROLES
-- ============================================================
INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),   -- admin_super -> SUPER_ADMIN
(3, 2),   -- headmaster -> HEAD_MASTER
(4, 3),   -- deputy -> GENERAL_SECOND_MASTER
(5, 4),   -- hr -> HUMAN_RESOURCE
(6, 5), (7, 5), (8, 5), -- teachers -> SECOND_MASTER
(9, 6), (10, 6), (11, 6), (12, 6), -- parents -> PARENT
(13, 7), (14, 7), (15, 7), (16, 7), (17, 7), (18, 7); -- students -> STUDENT

-- ============================================================
-- Data loaded successfully!
-- Users: 18 (2 admins, 6 staff, 4 parents, 6 students)
-- Schools: 3
-- Staff Records: 6
-- Classes: 5
-- Subjects: 8
-- Students: 6
-- Enrollments: 6
-- Academic Years: 2
-- Terms: 4
-- Exams: 4
-- Exam Subjects: 18 (all with max_score=100)
-- Student Scores: 24
-- Results: 6
-- Roles: 7
-- User Roles: 18 assignments

