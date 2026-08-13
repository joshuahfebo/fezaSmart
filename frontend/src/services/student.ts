import { apiClient } from './api';

export interface StudentProfile {
  id: number;
  firstName: string;
  middleName?: string;
  lastName: string;
  controlNumber: string;
  dob?: string;
  gender?: string;
  school: number;
  user?: number;
  studentParentParents?: number[];
  createdAt: string;
  updatedAt: string;
}

export interface StudentEnrollment {
  id: number;
  studentId: number;
  classId: number;
  academicYearId: number;
  enrollmentDate: string;
}

export interface StudentAttendance {
  totalDays: number;
  presentDays: number;
  absentDays: number;
  lateDays: number;
  percentage: number;
}

export interface StudentParent {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  relationshipType: string;
}

export interface PagedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

class StudentService {
  /**
   * Get all students with optional filters
   */
  async getAllStudents(options?: {
    page?: number;
    size?: number;
    searchTerm?: string;
    classId?: number;
    schoolId?: number;
    sort?: string;
  }): Promise<PagedResponse<StudentProfile>> {
    try {
      return await apiClient.get<PagedResponse<StudentProfile>>(
        '/students',
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student by ID
   */
  async getStudent(id: number): Promise<StudentProfile> {
    try {
      return await apiClient.get<StudentProfile>(`/students/${id}`);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get current user's student profile
   * First fetches user/me, then gets student record
   */
  async getCurrentStudentProfile(): Promise<StudentProfile> {
    try {
      // Get current user
      const user = await apiClient.get<any>('/users/me');

      // Get student record using the user-specific endpoint
      const student = await apiClient.get<StudentProfile>(
        `/students/user/${user.id}`
      );

      return student;
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get students in a class
   */
  async getStudentsByClass(
    classId: number,
    options?: { page?: number; size?: number }
  ): Promise<PagedResponse<StudentProfile>> {
    try {
      return await apiClient.get<PagedResponse<StudentProfile>>(
        `/classes/${classId}/students`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get students in a school
   */
  async getStudentsBySchool(
    schoolId: number,
    options?: { page?: number; size?: number }
  ): Promise<PagedResponse<StudentProfile>> {
    try {
      return await apiClient.get<PagedResponse<StudentProfile>>(
        `/schools/${schoolId}/students`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student's parents
   */
  async getStudentParents(studentId: number): Promise<StudentParent[]> {
    try {
      return await apiClient.get<StudentParent[]>(
        `/students/${studentId}/parents`
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student's attendance summary
   */
  async getStudentAttendance(
    studentId: number,
    options?: { month?: number; year?: number }
  ): Promise<StudentAttendance> {
    try {
      const params = new URLSearchParams();
      if (options?.month) params.append('month', options.month.toString());
      if (options?.year) params.append('year', options.year.toString());

      return await apiClient.get<StudentAttendance>(
        `/attendance/student/${studentId}/summary`,
        params ? Object.fromEntries(params) : {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student's enrollment history
   */
  async getStudentEnrollment(
    studentId: number
  ): Promise<StudentEnrollment[]> {
    try {
      return await apiClient.get<StudentEnrollment[]>(
        `/students/${studentId}/enrollments`
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Create a new student
   */
  async createStudent(data: {
    firstName: string;
    lastName: string;
    email: string;
    controlNumber: string;
    gender: string;
    classId: number;
    dateOfBirth?: string;
  }): Promise<StudentProfile> {
    try {
      return await apiClient.post<StudentProfile>('/students', data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Update student profile
   */
  async updateStudent(
    id: number,
    data: Partial<StudentProfile>
  ): Promise<StudentProfile> {
    try {
      return await apiClient.put<StudentProfile>(`/students/${id}`, data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Delete a student
   */
  async deleteStudent(id: number): Promise<void> {
    try {
      await apiClient.delete(`/students/${id}`);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Enroll student in a class
   */
  async enrollStudent(
    studentId: number,
    classId: number,
    academicYearId: number
  ): Promise<StudentEnrollment> {
    try {
      return await apiClient.post<StudentEnrollment>(
        `/students/${studentId}/enroll`,
        {
          classId,
          academicYearId,
        }
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Handle API errors
   */
  private handleError(error: any): Error {
    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 400:
          return new Error(data.message || 'Invalid request');
        case 401:
          return new Error('Unauthorized. Please login again');
        case 403:
          return new Error('You do not have permission to access this');
        case 404:
          return new Error('Student not found');
        case 500:
          return new Error('Server error. Please try again later');
        default:
          return new Error(data.message || 'An error occurred');
      }
    }

    if (error.message === 'Network Error') {
      return new Error(
        'Network error. Please check your internet connection'
      );
    }

    return error;
  }
}

export const studentService = new StudentService();
