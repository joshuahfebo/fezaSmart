import React, { createContext, useContext, useState, ReactNode } from 'react';
import { resultsService, Result, PagedResponse, ExamResult } from '../services';

interface ResultsContextType {
  results: Result[];
  examStats: ExamResult | null;
  currentStudentResults: Result[];
  currentClassResults: Result[];
  loading: boolean;
  error: string | null;
  
  // Methods
  fetchStudentResults: (studentId: number, filters?: any) => Promise<void>;
  fetchClassResults: (classId: number, filters?: any) => Promise<void>;
  fetchAllResults: (filters?: any) => Promise<void>;
  fetchExamResults: (examId: number, filters?: any) => Promise<void>;
  fetchExamStats: (examId: number) => Promise<void>;
  computeExamResults: (examId: number) => Promise<void>;
  clearError: () => void;
}

const ResultsContext = createContext<ResultsContextType | undefined>(undefined);

interface ResultsProviderProps {
  children: ReactNode;
}

export const ResultsProvider: React.FC<ResultsProviderProps> = ({ children }) => {
  const [results, setResults] = useState<Result[]>([]);
  const [currentStudentResults, setCurrentStudentResults] = useState<Result[]>([]);
  const [currentClassResults, setCurrentClassResults] = useState<Result[]>([]);
  const [examStats, setExamStats] = useState<ExamResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchStudentResults = async (
    studentId: number,
    filters?: any
  ) => {
    try {
      setLoading(true);
      setError(null);
      const response = await resultsService.getStudentResults(
        studentId,
        filters
      );
      setCurrentStudentResults(response.content);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to fetch student results';
      setError(errorMessage);
      console.error('Fetch student results error:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchClassResults = async (
    classId: number,
    filters?: any
  ) => {
    try {
      setLoading(true);
      setError(null);
      const response = await resultsService.getClassResults(
        classId,
        filters
      );
      setCurrentClassResults(response.content);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to fetch class results';
      setError(errorMessage);
      console.error('Fetch class results error:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchAllResults = async (filters?: any) => {
    try {
      setLoading(true);
      setError(null);
      const response = await resultsService.getAllResults(filters);
      setResults(response.content);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to fetch results';
      setError(errorMessage);
      console.error('Fetch all results error:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchExamResults = async (
    examId: number,
    filters?: any
  ) => {
    try {
      setLoading(true);
      setError(null);
      const response = await resultsService.getExamResults(
        examId,
        filters
      );
      setResults(response.content);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to fetch exam results';
      setError(errorMessage);
      console.error('Fetch exam results error:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchExamStats = async (examId: number) => {
    try {
      setLoading(true);
      setError(null);
      const stats = await resultsService.getExamStats(examId);
      setExamStats(stats);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to fetch exam statistics';
      setError(errorMessage);
      console.error('Fetch exam stats error:', err);
    } finally {
      setLoading(false);
    }
  };

  const computeExamResults = async (examId: number) => {
    try {
      setLoading(true);
      setError(null);
      const computedResults = await resultsService.computeExamResults(examId);
      setResults(computedResults);
    } catch (err: any) {
      const errorMessage = err.message || 'Failed to compute results';
      setError(errorMessage);
      console.error('Compute results error:', err);
    } finally {
      setLoading(false);
    }
  };

  const clearError = () => {
    setError(null);
  };

  const value: ResultsContextType = {
    results,
    examStats,
    currentStudentResults,
    currentClassResults,
    loading,
    error,
    fetchStudentResults,
    fetchClassResults,
    fetchAllResults,
    fetchExamResults,
    fetchExamStats,
    computeExamResults,
    clearError,
  };

  return (
    <ResultsContext.Provider value={value}>
      {children}
    </ResultsContext.Provider>
  );
};

export const useResults = () => {
  const context = useContext(ResultsContext);
  if (!context) {
    throw new Error('useResults must be used within a ResultsProvider');
  }
  return context;
};
