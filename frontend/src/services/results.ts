import { apiClient } from './api';

export interface Result {
  id: number;
  studentId: number;
  examId: number;
  totalScore: number;
  averagePercentage: number;
  totalPoints: number;
  division?: string;
  ranking?: number;
  gradeId?: number;
  createdAt: string;
  updatedAt: string;
}

export interface StudentScore {
  id: number;
  studentId: number;
  examSubjectId: number;
  score: number;
  remarks?: string;
  createdAt: string;
}

export interface ExamResult {
  id: number;
  examName: string;
  averageScore: number;
  highestScore: number;
  lowestScore: number;
  passRate: number;
  totalStudents: number;
}

export interface PagedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

class ResultsService {
  /**
   * Get all results with optional filters
   */
  async getAllResults(filters?: {
    page?: number;
    size?: number;
    examId?: number;
    classId?: number;
    minScore?: number;
    maxScore?: number;
    sort?: string;
  }): Promise<PagedResponse<Result>> {
    try {
      return await apiClient.get<PagedResponse<Result>>(
        '/results',
        filters || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get result by ID
   */
  async getResult(id: number): Promise<Result> {
    try {
      return await apiClient.get<Result>(`/results/${id}`);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student's results
   */
  async getStudentResults(
    studentId: number,
    options?: { page?: number; size?: number; termId?: number }
  ): Promise<PagedResponse<Result>> {
    try {
      return await apiClient.get<PagedResponse<Result>>(
        `/students/${studentId}/results`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get class results
   */
  async getClassResults(
    classId: number,
    options?: { page?: number; size?: number; examId?: number }
  ): Promise<PagedResponse<Result>> {
    try {
      return await apiClient.get<PagedResponse<Result>>(
        `/classes/${classId}/results`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get exam results
   */
  async getExamResults(
    examId: number,
    options?: { page?: number; size?: number; classId?: number }
  ): Promise<PagedResponse<Result>> {
    try {
      return await apiClient.get<PagedResponse<Result>>(
        `/exams/${examId}/results`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get school-wide results
   */
  async getSchoolResults(
    schoolId: number,
    options?: { page?: number; size?: number; academicYearId?: number }
  ): Promise<PagedResponse<Result>> {
    try {
      return await apiClient.get<PagedResponse<Result>>(
        `/schools/${schoolId}/results`,
        options || {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Compute results for an exam
   * Generates Result records from StudentScore data
   */
  async computeExamResults(examId: number): Promise<Result[]> {
    try {
      return await apiClient.post<Result[]>(
        `/results/compute/exam/${examId}`,
        {}
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get exam statistics
   */
  async getExamStats(examId: number): Promise<ExamResult> {
    try {
      return await apiClient.get<ExamResult>(`/analytics/exam/${examId}/stats`);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get class performance analytics
   */
  async getClassPerformance(
    classId: number
  ): Promise<{ averageScore: number; passRate: number; students: Result[] }> {
    try {
      return await apiClient.get(
        `/analytics/class/${classId}/performance`
      );
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Create a result (admin only)
   */
  async createResult(data: {
    studentId: number;
    examId: number;
    totalScore: number;
    totalPoints: number;
  }): Promise<Result> {
    try {
      return await apiClient.post<Result>('/results', data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Update a result
   */
  async updateResult(
    id: number,
    data: Partial<Result>
  ): Promise<Result> {
    try {
      return await apiClient.put<Result>(`/results/${id}`, data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Delete a result
   */
  async deleteResult(id: number): Promise<void> {
    try {
      await apiClient.delete(`/results/${id}`);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get student scores for an exam
   */
  async getStudentScores(
    studentId: number,
    examId: number
  ): Promise<StudentScore[]> {
    try {
      return await apiClient.get<StudentScore[]>(
        `/students/${studentId}/exam/${examId}/scores`
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
          return new Error('Result not found');
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

export const resultsService = new ResultsService();
