-- ============================================================
-- FEZA SMART MOCK DATA - School Results Tracking System
-- ============================================================
-- This script populates the database with comprehensive test data
-- for testing all features: users, schools, staff, students, exams, results

-- ============================================================
-- 1. SUPER ADMINS & SYSTEM USERS
-- ============================================================
-- Password: Feza@2024 (should be hashed in production - use bcrypt)

INSERT INTO user (username, hashed_password, email, phone, is_active, email_verified, phone_verified, two_factor_enabled, created_at, updated_at, last_login_at) VALUES
('admin_super', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'admin@fezasmart.com', '+254712345678', TRUE, TRUE, FALSE, FALSE, NOW(), NOW(), NULL),
('director_main', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'director@fezasmart.com', '+254712345679', TRUE, TRUE, FALSE, FALSE, NOW(), NOW(), NULL);

-- ============================================================
-- 2. ACADEMIC YEARS & TERMS
-- ============================================================

INSERT INTO AcademicYear (name, startDate, endDate, createdAt) VALUES
('2024', '2024-01-15', '2024-11-30', NOW()),
('2025', '2025-01-20', '2025-11-30', NOW());

INSERT INTO Term (academicYear_id, name, startDate, endDate, createdAt) VALUES
(1, 'Term 1', '2024-01-15', '2024-04-12', NOW()),
(1, 'Term 2', '2024-05-06', '2024-08-09', NOW()),
(1, 'Term 3', '2024-08-19', '2024-11-15', NOW()),
(2, 'Term 1', '2025-01-20', '2025-04-18', NOW());

-- ============================================================
-- 3. SCHOOLS
-- ============================================================

INSERT INTO School (name, email, phone, city, country, isActive, createdAt, updatedAt) VALUES
('Kampala Senior School', 'info@kss.ug', '+256414123456', 'Kampala', 'Uganda', TRUE, NOW(), NOW()),
('St. Mary\'s Secondary', 'admin@stmarys.ug', '+256414789012', 'Kampala', 'Uganda', TRUE, NOW(), NOW()),
('Muyenga High School', 'contact@muyenga.ug', '+256414345678', 'Kampala', 'Uganda', TRUE, NOW(), NOW());

-- ============================================================
-- 4. STAFF & TEACHERS
-- ============================================================

-- School 1 Staff
INSERT INTO User (username, hashedPassword, email, phone, isActive, emailVerified, phoneVerified, twoFactorEnabled, createdAt, updatedAt) VALUES
('headmaster_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'headmaster@kss.ug', '+256712111111', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('deputy_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'deputy@kss.ug', '+256712111112', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('hr_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'hr@kss.ug', '+256712111113', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('teacher_math_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'mwangi@kss.ug', '+256712111114', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('teacher_english_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'jane@kss.ug', '+256712111115', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('teacher_science_kss', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'john@kss.ug', '+256712111116', TRUE, TRUE, FALSE, FALSE, NOW(), NOW());

-- School 1 Staff Records
INSERT INTO Staff (user_id, school_id, firstName, lastName, department_id, joinDate, isActive) VALUES
(3, 1, 'David', 'Kipchoge', NULL, '2019-01-10', TRUE),
(4, 1, 'Margaret', 'Omondi', NULL, '2020-03-15', TRUE),
(5, 1, 'Robert', 'Njeri', NULL, '2021-02-20', TRUE),
(6, 1, 'Mwangi', 'Kariuki', NULL, '2021-09-01', TRUE),
(7, 1, 'Jane', 'Muthoni', NULL, '2022-01-15', TRUE),
(8, 1, 'John', 'Kosgey', NULL, '2021-06-10', TRUE);

-- ============================================================
-- 5. CLASSES
-- ============================================================

INSERT INTO Classs (name, school_id, academicYear_id, createdAt) VALUES
('Form 1A', 1, 1, NOW()),
('Form 1B', 1, 1, NOW()),
('Form 2A', 1, 1, NOW()),
('Form 3A', 1, 1, NOW()),
('Form 4A', 1, 1, NOW());

-- ============================================================
-- 6. SUBJECTS
-- ============================================================

INSERT INTO Subject (name, type, createdAt) VALUES
('Mathematics', 'CORE', NOW()),
('English Language', 'CORE', NOW()),
('Biology', 'SCIENCE', NOW()),
('Chemistry', 'SCIENCE', NOW()),
('Physics', 'SCIENCE', NOW()),
('Geography', 'SOCIAL', NOW()),
('History', 'SOCIAL', NOW()),
('Kiswahili', 'LANGUAGE', NOW());

-- ============================================================
-- 7. STUDENTS & PARENTS
-- ============================================================

-- School 1 Parents
INSERT INTO User (username, hashedPassword, email, phone, isActive, emailVerified, phoneVerified, twoFactorEnabled, createdAt, updatedAt) VALUES
('parent_kimani', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent1@email.com', '+256789111111', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('parent_nairobi', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent2@email.com', '+256789111112', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('parent_mombasa', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent3@email.com', '+256789111113', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('parent_nakuru', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'parent4@email.com', '+256789111114', TRUE, TRUE, FALSE, FALSE, NOW(), NOW());

-- Parents
INSERT INTO Parent (user_id, school_id, firstName, lastName, phone, email, isActive) VALUES
(9, 1, 'Samuel', 'Kimani', '+256789111111', 'parent1@email.com', TRUE),
(10, 1, 'Anne', 'Nairobi', '+256789111112', 'parent2@email.com', TRUE),
(11, 1, 'Peter', 'Mombasa', '+256789111113', 'parent3@email.com', TRUE),
(12, 1, 'Grace', 'Nakuru', '+256789111114', 'parent4@email.com', TRUE);

-- Students (School 1)
INSERT INTO User (username, hashedPassword, email, phone, isActive, emailVerified, phoneVerified, twoFactorEnabled, createdAt, updatedAt) VALUES
('student_001', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student1@kss.ug', '+256701001001', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('student_002', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student2@kss.ug', '+256701001002', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('student_003', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student3@kss.ug', '+256701001003', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('student_004', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student4@kss.ug', '+256701001004', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('student_005', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student5@kss.ug', '+256701001005', TRUE, TRUE, FALSE, FALSE, NOW(), NOW()),
('student_006', '$2a$10$slYQmyNdGzin7olVCeOU2OPST9/PgBkqquzi.Ye5SYXAZZbqPgDlG', 'student6@kss.ug', '+256701001006', TRUE, TRUE, FALSE, FALSE, NOW(), NOW());

-- Student Records
INSERT INTO Student (user_id, school_id, firstName, lastName, dateOfBirth, guardianName, guardianPhone, isActive, admissionDate, createdAt) VALUES
(13, 1, 'David', 'Kipchoge', '2008-05-15', 'Samuel Kimani', '+256789111111', TRUE, '2022-01-20', NOW()),
(14, 1, 'Sarah', 'Omondi', '2008-07-22', 'Anne Nairobi', '+256789111112', TRUE, '2022-01-20', NOW()),
(15, 1, 'Michael', 'Kosgey', '2008-09-10', 'Peter Mombasa', '+256789111113', TRUE, '2022-01-20', NOW()),
(16, 1, 'Grace', 'Muthoni', '2008-11-03', 'Grace Nakuru', '+256789111114', TRUE, '2022-01-20', NOW()),
(17, 1, 'Joseph', 'Kimani', '2008-03-28', 'Samuel Kimani', '+256789111111', TRUE, '2022-01-20', NOW()),
(18, 1, 'Linda', 'Kipkemoi', '2008-06-14', 'Anne Nairobi', '+256789111112', TRUE, '2022-01-20', NOW());

-- Student Enrollments
INSERT INTO StudentEnrollment (student_id, classs_id, academicYear_id, enrollmentDate, isActive) VALUES
(1, 1, 1, '2024-01-15', TRUE),
(2, 1, 1, '2024-01-15', TRUE),
(3, 1, 1, '2024-01-15', TRUE),
(4, 2, 1, '2024-01-15', TRUE),
(5, 2, 1, '2024-01-15', TRUE),
(6, 2, 1, '2024-01-15', TRUE);

-- ============================================================
-- 8. EXAMS & EXAM SUBJECTS
-- ============================================================

INSERT INTO Exam (name, term_id, examDate, createdAt) VALUES
('Midterm Test 1', 1, '2024-02-20', NOW()),
('End of Term 1 Exam', 1, '2024-04-05', NOW()),
('Midterm Test 2', 2, '2024-06-15', NOW()),
('End of Term 2 Exam', 2, '2024-07-30', NOW());

-- Exam Subjects
INSERT INTO ExamSubject (exam_id, subject_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
(3, 1), (3, 2), (3, 3), (3, 4),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5);

-- ============================================================
-- 9. STUDENT SCORES
-- ============================================================

-- Term 1 Midterm Scores (Exam 1)
INSERT INTO StudentScore (student_id, examSubject_id, score) VALUES
(1, 1, 85.5), (1, 2, 78.0), (1, 3, 92.0), (1, 4, 88.5),
(2, 1, 92.0), (2, 2, 85.5), (2, 3, 88.0), (2, 4, 91.0),
(3, 1, 78.0), (3, 2, 82.0), (3, 3, 75.5), (3, 4, 80.0),
(4, 1, 88.0), (4, 2, 90.5), (4, 3, 85.0), (4, 4, 87.5),
(5, 1, 75.0), (5, 2, 80.0), (5, 3, 82.5), (5, 4, 78.0),
(6, 1, 95.0), (6, 2, 93.5), (6, 3, 91.0), (6, 4, 94.5);

-- Term 1 End of Term Scores (Exam 2)
INSERT INTO StudentScore (student_id, examSubject_id, score) VALUES
(1, 5, 87.0), (1, 6, 79.5), (1, 7, 91.0), (1, 8, 90.0), (1, 9, 89.5),
(2, 5, 93.5), (2, 6, 87.0), (2, 7, 89.0), (2, 8, 92.5), (2, 9, 90.0),
(3, 5, 79.0), (3, 6, 81.0), (3, 7, 76.0), (3, 8, 82.0), (3, 9, 80.5),
(4, 5, 89.0), (4, 6, 91.0), (4, 7, 86.5), (4, 8, 88.5), (4, 9, 87.0),
(5, 5, 76.5), (5, 6, 79.0), (5, 7, 83.0), (5, 8, 77.5), (5, 9, 81.0),
(6, 6, 96.0), (6, 7, 94.0), (6, 8, 92.0), (6, 9, 95.5), (6, 10, 93.0);

-- ============================================================
-- 10. RESULTS (Aggregate scores per exam)
-- ============================================================

INSERT INTO Result (student_id, exam_id, totalScore, averagePercentage, totalPoints, division, rankInClass, remark, computedAt) VALUES
(1, 1, 343.5, 85.9, 86, 'A', 2, 'Excellent', NOW()),
(2, 1, 356.5, 89.1, 89, 'A', 1, 'Excellent', NOW()),
(3, 1, 315.5, 78.9, 79, 'B', 4, 'Good', NOW()),
(4, 1, 350.5, 87.6, 88, 'A', 3, 'Excellent', NOW()),
(5, 1, 315.5, 78.9, 79, 'B', 5, 'Good', NOW()),
(6, 1, 373.5, 93.4, 93, 'A', 1, 'Excellent', NOW()),

(1, 2, 446.5, 89.3, 89, 'A', 2, 'Excellent', NOW()),
(2, 2, 452.0, 90.4, 90, 'A', 1, 'Excellent', NOW()),
(3, 2, 399.0, 79.8, 80, 'B', 4, 'Good', NOW()),
(4, 2, 442.0, 88.4, 88, 'A', 3, 'Excellent', NOW()),
(5, 2, 396.5, 79.3, 79, 'B', 5, 'Good', NOW()),
(6, 2, 470.0, 94.0, 94, 'A', 1, 'Excellent', NOW());

-- ============================================================
-- 11. ROLES & PERMISSIONS (For Auth System)
-- ============================================================

INSERT INTO Role (name, description) VALUES
('SUPER_ADMIN', 'System administrator with full access'),
('HEAD_MASTER', 'School head with full school control'),
('GENERAL_SECOND_MASTER', 'Sees everything in the school'),
('HUMAN_RESOURCE', 'Controls staff and attendance'),
('SECOND_MASTER', 'Controls specific classes'),
('PARENT', 'Views student results and school info'),
('STUDENT', 'Views own results');

-- Insert permissions
INSERT INTO Permission (name, description) VALUES
('CREATE_USER', 'Create new users'),
('READ_USER', 'View user information'),
('UPDATE_USER', 'Update user details'),
('DELETE_USER', 'Delete users'),
('CREATE_SCHOOL', 'Create new schools'),
('READ_SCHOOL', 'View school information'),
('UPDATE_SCHOOL', 'Update school details'),
('DELETE_SCHOOL', 'Delete schools'),
('CREATE_STUDENT', 'Create new students'),
('READ_STUDENT', 'View student information'),
('UPDATE_STUDENT', 'Update student details'),
('DELETE_STUDENT', 'Delete students'),
('CREATE_STAFF', 'Create staff members'),
('READ_STAFF', 'View staff information'),
('UPDATE_STAFF', 'Update staff details'),
('DELETE_STAFF', 'Delete staff members'),
('CREATE_RESULT', 'Create exam results'),
('READ_RESULT', 'View exam results'),
('UPDATE_RESULT', 'Update results'),
('DELETE_RESULT', 'Delete results'),
('MANAGE_ATTENDANCE', 'Manage staff/student attendance'),
('VIEW_ANALYTICS', 'View performance analytics'),
('EXPORT_REPORT', 'Export reports');

-- ============================================================
-- 12. ASSIGN ROLES TO USERS
-- ============================================================

-- Super Admin
INSERT INTO UserRole (userId, roleId) VALUES (1, 1);

-- School 1 - Head Master
INSERT INTO UserRole (userId, roleId) VALUES (3, 2);

-- School 1 - Deputy (General Second Master)
INSERT INTO UserRole (userId, roleId) VALUES (4, 3);

-- School 1 - HR
INSERT INTO UserRole (userId, roleId) VALUES (5, 4);

-- School 1 - Teachers (Second Master)
INSERT INTO UserRole (userId, roleId) VALUES (6, 5), (7, 5), (8, 5);

-- Parents
INSERT INTO UserRole (userId, roleId) VALUES (9, 6), (10, 6), (11, 6), (12, 6);

-- Students
INSERT INTO UserRole (userId, roleId) VALUES (13, 7), (14, 7), (15, 7), (16, 7), (17, 7), (18, 7);

-- ============================================================
-- 13. ATTENDANCE RECORDS
-- ============================================================

INSERT INTO AttendanceRecord (staff_id, attendanceDate, isPresent, checkInTime, checkOutTime, remarks, createdAt) VALUES
(1, '2024-02-20', TRUE, '08:00:00', '16:30:00', 'Regular', NOW()),
(1, '2024-02-21', TRUE, '08:05:00', '16:30:00', 'Regular', NOW()),
(2, '2024-02-20', TRUE, '08:00:00', '16:30:00', 'Regular', NOW()),
(2, '2024-02-21', FALSE, NULL, NULL, 'Sick leave', NOW()),
(3, '2024-02-20', TRUE, '08:00:00', '16:30:00', 'Regular', NOW()),
(3, '2024-02-21', TRUE, '08:10:00', '16:30:00', 'Regular', NOW());

-- ============================================================
-- 14. API KEYS (For external integrations)
-- ============================================================

INSERT INTO ApiKey (key, name, description, isActive, createdAt, expiresAt) VALUES
('feza_api_key_2024_001', 'Mobile App API Key', 'API key for React Native mobile application', TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR)),
('feza_api_key_2024_002', 'Web Portal API Key', 'API key for web administration portal', TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR));

-- ============================================================
-- SUMMARY
-- ============================================================
-- Total Insert Statistics:
-- Users: 19 (2 super admins/directors, 6 staff, 4 parents, 6 students + 1 system user)
-- Schools: 3
-- Staff: 6
-- Classes: 5
-- Students: 6
-- Exams: 4
-- Student Scores: ~100+
-- Results: 12
-- Roles: 7
-- Permissions: 23
-- Parents: 4
-- Attendance Records: 6
-- API Keys: 2

-- NOTE: All passwords are hashed with bcrypt (Feza@2024 as plaintext)
-- Use environment variables or secure vaults for production!
