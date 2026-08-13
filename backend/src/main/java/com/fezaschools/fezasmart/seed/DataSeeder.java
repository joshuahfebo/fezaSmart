package com.fezaschools.fezasmart.seed;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.attendance_record.AttendanceRecord;
import com.fezaschools.fezasmart.attendance_record.AttendanceRecordRepository;
import com.fezaschools.fezasmart.class_assignment.ClassAssignment;
import com.fezaschools.fezasmart.class_assignment.ClassAssignmentRepository;
import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.combination.Combination;
import com.fezaschools.fezasmart.combination.CombinationRepository;
import com.fezaschools.fezasmart.department.Department;
import com.fezaschools.fezasmart.department.DepartmentRepository;
import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.exam.ExamRepository;
import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.exam_subject.ExamSubjectRepository;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundary;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundaryRepository;
import com.fezaschools.fezasmart.role.Role;
import com.fezaschools.fezasmart.role.RoleRepository;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.student_enrollment.StudentEnrollment;
import com.fezaschools.fezasmart.student_enrollment.StudentEnrollmentRepository;
import com.fezaschools.fezasmart.student_score.StudentScore;
import com.fezaschools.fezasmart.student_score.StudentScoreRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.term.Term;
import com.fezaschools.fezasmart.term.TermRepository;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;


@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final SchoolRepository schoolRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TermRepository termRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final ClasssRepository classsRepository;
    private final CombinationRepository combinationRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final ExamRepository examRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final StudentScoreRepository studentScoreRepository;
    private final GradeBoundaryRepository gradeBoundaryRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ClassAssignmentRepository classAssignmentRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public DataSeeder(SchoolRepository schoolRepository, AcademicYearRepository academicYearRepository,
            TermRepository termRepository, SubjectRepository subjectRepository,
            UserRepository userRepository, RoleRepository roleRepository,
            StaffRepository staffRepository, DepartmentRepository departmentRepository,
            ClasssRepository classsRepository, CombinationRepository combinationRepository,
            StudentRepository studentRepository, StudentEnrollmentRepository studentEnrollmentRepository,
            ExamRepository examRepository, ExamSubjectRepository examSubjectRepository,
            StudentScoreRepository studentScoreRepository, GradeBoundaryRepository gradeBoundaryRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            ClassAssignmentRepository classAssignmentRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.schoolRepository = schoolRepository;
        this.academicYearRepository = academicYearRepository;
        this.termRepository = termRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
        this.classsRepository = classsRepository;
        this.combinationRepository = combinationRepository;
        this.studentRepository = studentRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.examRepository = examRepository;
        this.examSubjectRepository = examSubjectRepository;
        this.studentScoreRepository = studentScoreRepository;
        this.gradeBoundaryRepository = gradeBoundaryRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.classAssignmentRepository = classAssignmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (schoolRepository.count() > 0) {
            log.info("Database already seeded, skipping.");
            return;
        }

        log.info("Seeding database with mock data...");

        School school = createSchool();
        AcademicYear academicYear = createAcademicYear();
        List<Term> terms = createTerms(academicYear);
        List<Subject> subjects = createSubjects();
        List<Role> roles = createRoles();
        Department dept = createDepartment(school);
        List<User> users = createUsers(roles);
        List<Staff> staff = createStaff(school, users, dept, roles);
        List<Classs> classes = createClasses(school, academicYear);
        List<Combination> combinations = createCombinations(classes, subjects);
        List<Student> students = createStudents(school);
        assignParentRoles(students, users, roles);
        createEnrollments(students, classes, academicYear);
        assignStaffToClasses(staff, classes);
        List<GradeBoundary> boundaries = createGradeBoundaries(school);
        List<Exam> exams = createExams(terms);
        createExamSubjects(exams, subjects);
        createStudentScores(students, exams);
        createAttendanceRecords(students, classes);

        log.info("Database seeding completed successfully!");
    }

    private School createSchool() {
        School school = new School();
        school.setName("Feza Secondary School");
        school.setLocation("Dar es Salaam, Tanzania");
        school.setIsActive(true);
        school.setCreatedAt(OffsetDateTime.now());
        return schoolRepository.save(school);
    }

    private AcademicYear createAcademicYear() {
        AcademicYear ay = new AcademicYear();
        ay.setName("2025/2026");
        ay.setStartDate(LocalDate.of(2025, 9, 8));
        ay.setEndDate(LocalDate.of(2026, 7, 3));
        ay.setIsCurrent(true);
        return academicYearRepository.save(ay);
    }

    private List<Term> createTerms(AcademicYear ay) {
        List<Term> terms = new ArrayList<>();

        Term t1 = new Term();
        t1.setAcademicYearId(ay.getId());
        t1.setTermNumber(1);
        t1.setStartDate(LocalDate.of(2025, 9, 8));
        t1.setEndDate(LocalDate.of(2025, 12, 19));
        terms.add(termRepository.save(t1));

        Term t2 = new Term();
        t2.setAcademicYearId(ay.getId());
        t2.setTermNumber(2);
        t2.setStartDate(LocalDate.of(2026, 1, 5));
        t2.setEndDate(LocalDate.of(2026, 4, 10));
        terms.add(termRepository.save(t2));

        Term t3 = new Term();
        t3.setAcademicYearId(ay.getId());
        t3.setTermNumber(3);
        t3.setStartDate(LocalDate.of(2026, 4, 27));
        t3.setEndDate(LocalDate.of(2026, 7, 3));
        terms.add(termRepository.save(t3));

        return terms;
    }

    private List<Subject> createSubjects() {
        String[][] subjectData = {
            {"Mathematics", "CORE"},
            {"English Language", "CORE"},
            {"Kiswahili", "CORE"},
            {"Physics", "SCIENCE"},
            {"Chemistry", "SCIENCE"},
            {"Biology", "SCIENCE"},
            {"History", "HUMANITIES"},
            {"Geography", "HUMANITIES"},
            {"Civic Education", "HUMANITIES"},
            {"Computer Studies", "TECHNICAL"},
            {"French", "LANGUAGE"},
            {"Fine Art", "ARTS"},
        };

        List<Subject> subjects = new ArrayList<>();
        for (String[] data : subjectData) {
            Subject s = new Subject();
            s.setName(data[0]);
            s.setType(data[1]);
            subjects.add(subjectRepository.save(s));
        }
        return subjects;
    }

    private List<Role> createRoles() {
        String[] roleNames = {
            "SUPER_ADMIN", "HEAD_MASTER", "GENERAL_SECOND_MASTER",
            "HUMAN_RESOURCE", "SECOND_MASTER", "PARENT", "TEACHER", "STUDENT"
        };
        List<Role> roles = new ArrayList<>();
        for (String name : roleNames) {
            Role r = new Role();
            r.setName(name);
            roles.add(roleRepository.save(r));
        }
        return roles;
    }

    private Department createDepartment(School school) {
        Department dept = new Department();
        dept.setName("Academic Department");
        dept.setSchool(school);
        return departmentRepository.save(dept);
    }

    private List<User> createUsers(List<Role> roles) {
        List<User> users = new ArrayList<>();
        String[][] userData = {
            // Head Master
            {"headmaster", "hashed_pw_1", "headmaster@feza.ac.tz", "+255700000001"},
            // Deputy
            {"deputy_hm", "hashed_pw_2", "deputy@feza.ac.tz", "+255700000002"},
            // HODs
            {"hod_science", "hashed_pw_3", "science@feza.ac.tz", "+255700000003"},
            {"hod_humanities", "hashed_pw_4", "humanities@feza.ac.tz", "+255700000004"},
            // Teachers
            {"teacher_maths", "hashed_pw_5", "maths@feza.ac.tz", "+255700000005"},
            {"teacher_english", "hashed_pw_6", "english@feza.ac.tz", "+255700000006"},
            {"teacher_kiswahili", "hashed_pw_7", "kiswahili@feza.ac.tz", "+255700000007"},
            {"teacher_physics", "hashed_pw_8", "physics@feza.ac.tz", "+255700000008"},
            {"teacher_chemistry", "hashed_pw_9", "chemistry@feza.ac.tz", "+255700000009"},
            {"teacher_biology", "hashed_pw_10", "biology@feza.ac.tz", "+255700000010"},
            {"teacher_history", "hashed_pw_11", "history@feza.ac.tz", "+255700000011"},
            {"teacher_geo", "hashed_pw_12", "geo@feza.ac.tz", "+255700000012"},
            // Parent users
            {"parent_mwamba", "hashed_pw_13", "mwamba@email.com", "+255710000001"},
            {"parent_omary", "hashed_pw_14", "omary@email.com", "+255710000002"},
            {"parent_fatma", "hashed_pw_15", "fatma@email.com", "+255710000003"},
            {"parent_juma", "hashed_pw_16", "juma@email.com", "+255710000004"},
            {"parent_asha", "hashed_pw_17", "asha@email.com", "+255710000005"},
            {"parent_ishmail", "hashed_pw_18", "ishmail@email.com", "+255710000006"},
        };

        for (String[] data : userData) {
            User u = new User();
            u.setUsername(data[0]);
            u.setHashedPassword(passwordEncoder.encode("password123"));
            u.setEmail(data[2]);
            u.setPhone(data[3]);
            u.setIsActive(true);
            u.setEmailVerified(true);
            u.setCreatedAt(OffsetDateTime.now());
            users.add(userRepository.save(u));
        }
        return users;
    }

    private List<Staff> createStaff(School school, List<User> users, Department dept, List<Role> roles) {
        List<Staff> staffList = new ArrayList<>();

        // Staff data: firstName, lastName, gender, staffNumber, userIndex
        String[][] staffData = {
            {"John", "Mwakasege", "MALE", "STF001", "0"},
            {"Grace", "Kimaro", "FEMALE", "STF002", "1"},
            {"David", "Lugendo", "MALE", "STF003", "2"},
            {"Mary", "Nkwama", "FEMALE", "STF004", "3"},
            {"Peter", "Mushi", "MALE", "STF005", "4"},
            {"Sarah", "Kilangi", "FEMALE", "STF006", "5"},
            {"Emmanuel", "Mtweve", "MALE", "STF007", "6"},
            {"Agnes", "Lukumay", "FEMALE", "STF008", "7"},
            {"Joseph", "Kamata", "MALE", "STF009", "8"},
            {"Happiness", "Mponda", "FEMALE", "STF010", "9"},
            {"Robert", "Sangwa", "MALE", "STF011", "10"},
            {"Fatima", "Nuru", "FEMALE", "STF012", "11"},
        };

        for (String[] data : staffData) {
            Staff s = new Staff();
            s.setFirstName(data[0]);
            s.setLastName(data[1]);
            s.setGender(data[2]);
            s.setStaffNumber(data[3]);
            s.setSchool(school);
            s.setUser(users.get(Integer.parseInt(data[4])));
            s.setDepartment(dept);
            s.setCreatedAt(OffsetDateTime.now());
            staffList.add(staffRepository.save(s));
        }

        // Assign roles to staff users: index 0 -> HEAD_MASTER, 1 -> GENERAL_SECOND_MASTER,
        // 2 -> HUMAN_RESOURCE, 3 -> SECOND_MASTER, 4-11 -> TEACHER
        String[][] staffRoleMap = {
            {"0", "HEAD_MASTER"},
            {"1", "GENERAL_SECOND_MASTER"},
            {"2", "HUMAN_RESOURCE"},
            {"3", "SECOND_MASTER"},
            {"4", "TEACHER"},
            {"5", "TEACHER"},
            {"6", "TEACHER"},
            {"7", "TEACHER"},
            {"8", "TEACHER"},
            {"9", "TEACHER"},
            {"10", "TEACHER"},
            {"11", "TEACHER"},
        };
        for (String[] m : staffRoleMap) {
            User u = users.get(Integer.parseInt(m[0]));
            Role role = findRole(roles, m[1]);
            u.getUserRoleRoles().add(role);
            userRepository.save(u);
        }

        return staffList;
    }

    private void assignParentRoles(List<Student> students, List<User> users, List<Role> roles) {
        Role parentRole = findRole(roles, "PARENT");
        // Parent user accounts are at indices 12-17 (created in createUsers), one per parent
        for (int i = 12; i < Math.min(users.size(), 18); i++) {
            User u = users.get(i);
            u.getUserRoleRoles().add(parentRole);
            userRepository.save(u);
        }
    }

    private Role findRole(List<Role> roles, String name) {
        return roles.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Role not found: " + name));
    }

    private List<Classs> createClasses(School school, AcademicYear ay) {
        String[] classNames = {"Form 1", "Form 2", "Form 3", "Form 4"};
        List<Classs> classes = new ArrayList<>();
        for (String name : classNames) {
            Classs c = new Classs();
            c.setName(name);
            c.setSchool(school);
            c.setAcademicYear(ay);
            c.setCreatedAt(OffsetDateTime.now());
            classes.add(classsRepository.save(c));
        }
        return classes;
    }

    private List<Combination> createCombinations(List<Classs> classes, List<Subject> subjects) {
        List<Combination> combos = new ArrayList<>();

        // PCM: Physics(3), Chemistry(4), Mathematics(0)
        Combination pcm = new Combination();
        pcm.setName("PCM");
        pcm.setClasss(classes.get(2)); // Form 3
        pcm.setCreatedAt(OffsetDateTime.now());
        pcm.setCombinationSubjectSubjects(new HashSet<>(List.of(subjects.get(3), subjects.get(4), subjects.get(0))));
        combos.add(combinationRepository.save(pcm));

        // PCB: Physics(3), Chemistry(4), Biology(5)
        Combination pcb = new Combination();
        pcb.setName("PCB");
        pcb.setClasss(classes.get(2));
        pcb.setCreatedAt(OffsetDateTime.now());
        pcb.setCombinationSubjectSubjects(new HashSet<>(List.of(subjects.get(3), subjects.get(4), subjects.get(5))));
        combos.add(combinationRepository.save(pcb));

        // ECA: English(1), Civics(8), Geography(7)
        Combination eca = new Combination();
        eca.setName("EGA");
        eca.setClasss(classes.get(2));
        eca.setCreatedAt(OffsetDateTime.now());
        eca.setCombinationSubjectSubjects(new HashSet<>(List.of(subjects.get(1), subjects.get(8), subjects.get(7))));
        combos.add(combinationRepository.save(eca));

        // HGL: History(6), Geography(7), Kiswahili(2)
        Combination hgl = new Combination();
        hgl.setName("HKL");
        hgl.setClasss(classes.get(3)); // Form 4
        hgl.setCreatedAt(OffsetDateTime.now());
        hgl.setCombinationSubjectSubjects(new HashSet<>(List.of(subjects.get(6), subjects.get(7), subjects.get(2))));
        combos.add(combinationRepository.save(hgl));

        return combos;
    }

    private List<Student> createStudents(School school) {
        String[][] studentData = {
            // firstName, middleName, lastName, controlNumber, gender, dob
            {"Amina", "Ibrahim", "Mwamba", "CTRL001", "FEMALE", "2008-03-15"},
            {"Brian", "Charles", "Omary", "CTRL002", "MALE", "2007-08-22"},
            {"Catherine", "John", "Fatma", "CTRL003", "FEMALE", "2008-01-10"},
            {"Daniel", "Peter", "Juma", "CTRL004", "MALE", "2007-11-05"},
            {"Esther", "Emmanuel", "Asha", "CTRL005", "FEMALE", "2008-06-18"},
            {"Felix", "David", "Ishmail", "CTRL006", "MALE", "2007-04-30"},
            {"Grace", "Joseph", "Mwakasege", "CTRL007", "FEMALE", "2008-09-12"},
            {"Henry", "Robert", "Kimaro", "CTRL008", "MALE", "2007-02-28"},
            {"Irene", "Emmanuel", "Lugendo", "CTRL009", "FEMALE", "2008-07-07"},
            {"James", "Peter", "Nkwama", "CTRL010", "MALE", "2007-12-20"},
            {"Kezia", "David", "Mushi", "CTRL011", "FEMALE", "2008-05-14"},
            {"Lilian", "Joseph", "Kilangi", "CTRL012", "FEMALE", "2007-10-03"},
            {"Michael", "Robert", "Mtweve", "CTRL013", "MALE", "2008-02-25"},
            {"Nancy", "Emmanuel", "Lukumay", "CTRL014", "FEMALE", "2007-06-08"},
            {"Oscar", "David", "Kamata", "CTRL015", "MALE", "2008-08-01"},
            {"Priscilla", "John", "Mponda", "CTRL016", "FEMALE", "2007-09-17"},
            {"Quincy", "Peter", "Sangwa", "CTRL017", "MALE", "2008-04-22"},
            {"Ruth", "Joseph", "Nuru", "CTRL018", "FEMALE", "2007-07-11"},
            {"Samuel", "Robert", "Mwakasege", "CTRL019", "MALE", "2008-11-30"},
            {"Teresa", "David", "Kimaro", "CTRL020", "FEMALE", "2007-03-25"},
            {"Ulises", "Peter", "Lugendo", "CTRL021", "MALE", "2008-01-19"},
            {"Violet", "Joseph", "Nkwama", "CTRL022", "FEMALE", "2007-05-16"},
            {"William", "Emmanuel", "Mushi", "CTRL023", "MALE", "2008-10-09"},
            {"Xena", "David", "Kilangi", "CTRL024", "FEMALE", "2007-08-04"},
            {"Yusuf", "Robert", "Mtweve", "CTRL025", "MALE", "2008-03-28"},
            {"Zainab", "John", "Lukumay", "CTRL026", "FEMALE", "2007-11-21"},
            {"Adam", "Peter", "Kamata", "CTRL027", "MALE", "2008-06-15"},
            {"Beatrice", "Joseph", "Mponda", "CTRL028", "FEMALE", "2007-04-02"},
            {"Charles", "Emmanuel", "Sangwa", "CTRL029", "MALE", "2008-09-26"},
            {"Diana", "David", "Nuru", "CTRL030", "FEMALE", "2007-01-13"},
        };

        List<Student> students = new ArrayList<>();
        for (String[] data : studentData) {
            Student s = new Student();
            s.setFirstName(data[0]);
            s.setMiddleName(data[1]);
            s.setLastName(data[2]);
            s.setControlNumber(data[3]);
            s.setGender(data[4]);
            s.setDob(LocalDate.parse(data[5]));
            s.setSchool(school);
            s.setCreatedAt(OffsetDateTime.now());
            students.add(studentRepository.save(s));
        }
        return students;
    }

    private void createEnrollments(List<Student> students, List<Classs> classes, AcademicYear ay) {
        // Distribute 30 students across 4 classes (roughly 7-8 per class)
        int[] enrollmentCounts = {8, 8, 7, 7};
        int studentIdx = 0;

        for (int classIdx = 0; classIdx < classes.size(); classIdx++) {
            for (int i = 0; i < enrollmentCounts[classIdx] && studentIdx < students.size(); i++) {
                StudentEnrollment se = new StudentEnrollment();
                se.setStudent(students.get(studentIdx));
                se.setClasss(classes.get(classIdx));
                se.setAcademicYear(ay);
                se.setEnrollmentDate(LocalDate.of(2025, 9, 8));
                se.setIsCurrent(true);
                studentEnrollmentRepository.save(se);
                studentIdx++;
            }
        }
    }

    private void assignStaffToClasses(List<Staff> staffList, List<Classs> classes) {
        // Head master -> Form 4, Deputy -> Form 3, etc.
        int[][] assignments = {
            {0, 3}, // staff[0] -> Form 4
            {1, 2}, // staff[1] -> Form 3
            {4, 0}, // maths teacher -> Form 1
            {5, 1}, // english teacher -> Form 2
        };

        for (int[] a : assignments) {
            ClassAssignment ca = new ClassAssignment();
            ca.setStaff(staffList.get(a[0]));
            ca.setClasss(classes.get(a[1]));
            ca.setRoleInClass("CLASS_TEACHER");
            ca.setAssignedDate(OffsetDateTime.of(2025, 9, 8, 0, 0, 0, 0, java.time.ZoneOffset.UTC));
            classAssignmentRepository.save(ca);
        }
    }

    private List<GradeBoundary> createGradeBoundaries(School school) {
        String[][] boundaries = {
            // letterGrade, minPercentage, maxPercentage, pointGrade, type
            {"A", "75", "100", "1", "LETTER"},
            {"B", "65", "74.99", "2", "LETTER"},
            {"C", "50", "64.99", "3", "LETTER"},
            {"D", "40", "49.99", "4", "LETTER"},
            {"E", "30", "39.99", "5", "LETTER"},
            {"S", "20", "29.99", "6", "LETTER"},
            {"F", "0", "19.99", "7", "LETTER"},
            // Point grade boundaries
            {"A", "75", "100", "1", "POINT"},
            {"B", "65", "74.99", "2", "POINT"},
            {"C", "50", "64.99", "3", "POINT"},
            {"D", "40", "49.99", "4", "POINT"},
            {"E", "30", "39.99", "5", "POINT"},
            {"S", "20", "29.99", "6", "POINT"},
            {"F", "0", "19.99", "7", "POINT"},
        };

        List<GradeBoundary> gbs = new ArrayList<>();
        for (String[] b : boundaries) {
            GradeBoundary gb = new GradeBoundary();
            gb.setLetterGrade(b[0]);
            gb.setMinPercentage(new BigDecimal(b[1]));
            gb.setMaxPercentage(new BigDecimal(b[2]));
            gb.setPointGrade(new BigDecimal(b[3]));
            gb.setType(b[4]);
            gb.setSchool(school);
            gb.setRemark(gradeRemark(b[0]));
            gbs.add(gradeBoundaryRepository.save(gb));
        }
        return gbs;
    }

    private String gradeRemark(String grade) {
        return switch (grade) {
            case "A" -> "Excellent";
            case "B" -> "Very Good";
            case "C" -> "Good";
            case "D" -> "Average";
            case "E" -> "Below Average";
            case "S" -> "Poor";
            case "F" -> "Fail";
            default -> "";
        };
    }

    private List<Exam> createExams(List<Term> terms) {
        List<Exam> exams = new ArrayList<>();

        Exam midTerm1 = new Exam();
        midTerm1.setName("Mid-Term 1");
        midTerm1.setExamDate(LocalDate.of(2025, 11, 10));
        midTerm1.setTerm(terms.get(0));
        exams.add(examRepository.save(midTerm1));

        Exam endTerm1 = new Exam();
        endTerm1.setName("End-Term 1");
        endTerm1.setExamDate(LocalDate.of(2025, 12, 15));
        endTerm1.setTerm(terms.get(0));
        exams.add(examRepository.save(endTerm1));

        Exam midTerm2 = new Exam();
        midTerm2.setName("Mid-Term 2");
        midTerm2.setExamDate(LocalDate.of(2026, 3, 2));
        midTerm2.setTerm(terms.get(1));
        exams.add(examRepository.save(midTerm2));

        return exams;
    }

    private void createExamSubjects(List<Exam> exams, List<Subject> subjects) {
        BigDecimal maxScore = new BigDecimal("100");
        for (Exam exam : exams) {
            for (Subject subject : subjects) {
                ExamSubject es = new ExamSubject();
                es.setExam(exam);
                es.setSubject(subject);
                es.setMaxScore(maxScore);
                examSubjectRepository.save(es);
            }
        }
    }

    private void createStudentScores(List<Student> students, List<Exam> exams) {
        Random random = new Random(42);

        for (Exam exam : exams) {
            List<ExamSubject> examSubjects = examSubjectRepository.findByExamId(exam.getId());

            for (Student student : students) {
                for (ExamSubject es : examSubjects) {
                    // Generate realistic scores (some subjects harder than others)
                    int baseScore = 40 + random.nextInt(45); // 40-84
                    // Some variation per subject
                    if (es.getSubject().getName().contains("Math")) {
                        baseScore = 30 + random.nextInt(55); // 30-84 (Math harder)
                    } else if (es.getSubject().getName().contains("Art") || es.getSubject().getName().contains("PE")) {
                        baseScore = 60 + random.nextInt(35); // 60-94 (easier)
                    }

                    StudentScore score = new StudentScore();
                    score.setStudent(student);
                    score.setExamSubject(es);
                    score.setScore(new BigDecimal(baseScore));
                    studentScoreRepository.save(score);
                }
            }
        }
    }

    private void createAttendanceRecords(List<Student> students, List<Classs> classes) {
        Random random = new Random(42);
        String[] statuses = {"PRESENT", "PRESENT", "PRESENT", "PRESENT", "PRESENT",
                             "PRESENT", "PRESENT", "LATE", "ABSENT", "EXCUSED"};

        // Create attendance for 30 school days
        LocalDate startDate = LocalDate.of(2025, 9, 8);
        for (int day = 0; day < 30; day++) {
            LocalDate date = startDate.plusDays(day);
            if (date.getDayOfWeek().getValue() > 5) continue; // Skip weekends

            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                Classs classs = classes.get(i / 8); // Roughly distribute

                AttendanceRecord ar = new AttendanceRecord();
                ar.setStudent(student);
                ar.setClasss(classs);
                ar.setDate(date);
                ar.setStatus(statuses[random.nextInt(statuses.length)]);
                ar.setCreatedAt(OffsetDateTime.now());
                attendanceRecordRepository.save(ar);
            }
        }
    }
}
