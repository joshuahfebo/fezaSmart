import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { useAuth } from './auth-context';
import { studentService, type StudentProfile } from '../services';

interface StudentContextType {
  studentProfile: StudentProfile | null;
  studentName: string;
  studentInitials: string;
  loading: boolean;
  error: string | null;
  refreshProfile: () => Promise<void>;
}

const StudentContext = createContext<StudentContextType | undefined>(undefined);

interface StudentProviderProps {
  children: ReactNode;
}

export const StudentProvider: React.FC<StudentProviderProps> = ({ children }) => {
  const { isAuthenticated, userProfile } = useAuth();
  const [studentProfile, setStudentProfile] = useState<StudentProfile | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchProfile = useCallback(async () => {
    if (!isAuthenticated) {
      setStudentProfile(null);
      return;
    }
    try {
      setLoading(true);
      setError(null);
      const profile = await studentService.getCurrentStudentProfile();
      setStudentProfile(profile);
    } catch (err: any) {
      // Silently handle 404 — user is not a student (e.g. teacher, admin)
      if (err?.message?.includes('Not found') || err?.message?.includes('404') || err?.response?.status === 404) {
        setStudentProfile(null);
      } else {
        console.warn('Unexpected error loading student profile:', err.message);
        setError(err.message || 'Failed to load student profile');
        setStudentProfile(null);
      }
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  const refreshProfile = async () => {
    await fetchProfile();
  };

  const studentName = studentProfile
    ? `${studentProfile.firstName} ${studentProfile.lastName}`
    : userProfile?.username || 'Student';

  const studentInitials = studentProfile
    ? `${studentProfile.firstName?.[0] || ''}${studentProfile.lastName?.[0] || ''}`.toUpperCase()
    : studentName.slice(0, 2).toUpperCase();

  const value: StudentContextType = {
    studentProfile,
    studentName,
    studentInitials,
    loading,
    error,
    refreshProfile,
  };

  return (
    <StudentContext.Provider value={value}>
      {children}
    </StudentContext.Provider>
  );
};

export const useStudent = () => {
  const context = useContext(StudentContext);
  if (!context) {
    throw new Error('useStudent must be used within a StudentProvider');
  }
  return context;
};
