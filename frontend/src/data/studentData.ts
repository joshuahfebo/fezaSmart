// Mock student data for the Feza Smart student portal
import { ImageSourcePropType } from 'react-native';

export interface Student {
  id: string;
  name: string;
  username: string;
  password: string;
  grade: string;
  school: string;
  avatar: any;
}

export interface Course {
  id: string;
  name: string;
  code: string;
  teacher: string;
  color: string;
  icon: string;
  progress: number;
  grade: string;
  credits: number;
}

export interface Grade {
  id: string;
  courseId: string;
  courseName: string;
  score: number;
  maxScore: number;
  percentage: number;
  grade: string;
  date: string;
  type: 'quiz' | 'exam' | 'assignment' | 'project';
}

export interface Attendance {
  id: string;
  date: string;
  status: 'present' | 'absent' | 'late' | 'excused';
  course: string;
}

// Main demo student
export const demoStudent: Student = {
  id: '1',
  name: 'User',
  username: 'user',
  password: 'feza2024', // Will be configurable
  grade: 'Form 4',
  school: 'Feza Schools - Smart Campus',
  avatar: require('@/assets/images/expo-logo.png'),
};

// Courses data
export const courses: Course[] = [
  {
    id: 'math-101',
    name: 'Mathematics',
    code: 'MATH 101',
    teacher: 'Mr. Johnson',
    color: '#4CAF50',
    icon: 'calculator-outline',
    progress: 85,
    grade: 'A',
    credits: 4,
  },
  {
    id: 'eng-101',
    name: 'English Language',
    code: 'ENG 101',
    teacher: 'Ms. Amina',
    color: '#2196F3',
    icon: 'book-outline',
    progress: 92,
    grade: 'A+',
    credits: 3,
  },
  {
    id: 'phy-101',
    name: 'Physics',
    code: 'PHY 101',
    teacher: 'Dr. Kimani',
    color: '#9C27B0',
    icon: 'flask-outline',
    progress: 78,
    grade: 'B+',
    credits: 4,
  },
  {
    id: 'chem-101',
    name: 'Chemistry',
    code: 'CHEM 101',
    teacher: 'Ms. Wanjiku',
    color: '#FF5722',
    icon: 'beaker-outline',
    progress: 88,
    grade: 'A',
    credits: 4,
  },
  {
    id: 'bio-101',
    name: 'Biology',
    code: 'BIO 101',
    teacher: 'Mr. Omondi',
    color: '#4CAF50',
    icon: 'leaf-outline',
    progress: 95,
    grade: 'A+',
    credits: 3,
  },
  {
    id: 'his-101',
    name: 'History',
    code: 'HIS 101',
    teacher: 'Ms. Njeri',
    color: '#FF9800',
    icon: 'library-outline',
    progress: 82,
    grade: 'B',
    credits: 2,
  },
];

// Recent grades
export const recentGrades: Grade[] = [
  {
    id: 'g1',
    courseId: 'math-101',
    courseName: 'Mathematics',
    score: 85,
    maxScore: 100,
    percentage: 85,
    grade: 'A',
    date: '2024-07-15',
    type: 'exam',
  },
  {
    id: 'g2',
    courseId: 'eng-101',
    courseName: 'English Language',
    score: 92,
    maxScore: 100,
    percentage: 92,
    grade: 'A+',
    date: '2024-07-12',
    type: 'exam',
  },
  {
    id: 'g3',
    courseId: 'phy-101',
    courseName: 'Physics',
    score: 78,
    maxScore: 100,
    percentage: 78,
    grade: 'B+',
    date: '2024-07-10',
    type: 'quiz',
  },
  {
    id: 'g4',
    courseId: 'chem-101',
    courseName: 'Chemistry',
    score: 88,
    maxScore: 100,
    percentage: 88,
    grade: 'A',
    date: '2024-07-08',
    type: 'assignment',
  },
  {
    id: 'g5',
    courseId: 'bio-101',
    courseName: 'Biology',
    score: 95,
    maxScore: 100,
    percentage: 95,
    grade: 'A+',
    date: '2024-07-05',
    type: 'project',
  },
];

// Attendance data for the week
export const weeklyAttendance: Attendance[] = [
  { id: 'a1', date: '2024-07-15', status: 'present', course: 'Mathematics' },
  { id: 'a2', date: '2024-07-14', status: 'present', course: 'English' },
  { id: 'a3', date: '2024-07-13', status: 'present', course: 'Physics' },
  { id: 'a4', date: '2024-07-12', status: 'present', course: 'Chemistry' },
  { id: 'a5', date: '2024-07-11', status: 'present', course: 'Biology' },
  { id: 'a6', date: '2024-07-10', status: 'present', course: 'History' },
  { id: 'a7', date: '2024-07-09', status: 'present', course: 'Mathematics' },
];

// Calculate overall stats
export const getStudentStats = () => {
  const totalCourses = courses.length;
  const totalCredits = courses.reduce((sum, course) => sum + course.credits, 0);
  const averageGrade = (
    courses.reduce((sum, course) => {
      const gradeValue = course.grade === 'A+' ? 4.0 : 
                        course.grade === 'A' ? 4.0 :
                        course.grade === 'B+' ? 3.5 :
                        course.grade === 'B' ? 3.0 : 2.0;
      return sum + gradeValue * course.credits;
    }, 0) / totalCredits
  ).toFixed(2);
  
  const attendanceRate = (
    weeklyAttendance.filter(a => a.status === 'present').length / weeklyAttendance.length * 100
  ).toFixed(1);

  return {
    gpa: parseFloat(averageGrade),
    totalCourses,
    totalCredits,
    attendanceRate: parseFloat(attendanceRate),
    completedCourses: courses.filter(c => c.progress >= 80).length,
  };
};

export type { Student as StudentType, Course as CourseType, Grade as GradeType, Attendance as AttendanceType };
