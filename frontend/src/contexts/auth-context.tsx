import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authService, LoginResponse, UserProfile } from '../services';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface AuthContextType {
  isAuthenticated: boolean;
  user: LoginResponse | null;
  userProfile: UserProfile | null;
  token: string | null;
  loading: boolean;
  error: string | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  register: (
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    phoneNumber?: string
  ) => Promise<void>;
  refreshUser: () => Promise<void>;
  clearError: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState<LoginResponse | null>(null);
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Check for existing token on app startup
  useEffect(() => {
    bootstrapAsync();
  }, []);

  const bootstrapAsync = async () => {
    try {
      setLoading(true);
      
      // Try to get stored token
      const storedToken = await authService.getStoredToken();
      const storedUser = await authService.getStoredUserData();

      if (storedToken && storedUser) {
        // Token exists, validate it by fetching user profile
        try {
          const profile = await authService.getCurrentUser();
          setToken(storedToken);
          setUser(storedUser);
          setUserProfile(profile);
          setIsAuthenticated(true);
        } catch (validationError) {
          // Token is invalid or expired
          console.log('Token validation failed, clearing auth');
          await authService.logout();
          setToken(null);
          setUser(null);
          setUserProfile(null);
          setIsAuthenticated(false);
        }
      } else {
        setIsAuthenticated(false);
      }
    } catch (err) {
      console.error('Error during bootstrap:', err);
      setIsAuthenticated(false);
    } finally {
      setLoading(false);
    }
  };

  const login = async (username: string, password: string) => {
    try {
      setError(null);
      setLoading(true);

      const response = await authService.login({ username, password });
      
      // Fetch user profile
      const profile = await authService.getCurrentUser();

      setToken(response.accessToken);
      setUser(response);
      setUserProfile(profile);
      setIsAuthenticated(true);
    } catch (err: any) {
      const errorMessage = err.message || 'Login failed';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const register = async (
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    phoneNumber?: string
  ) => {
    try {
      setError(null);
      setLoading(true);

      const response = await authService.register({
        firstName,
        lastName,
        email,
        password,
        phoneNumber,
      });

      // Fetch user profile
      const profile = await authService.getCurrentUser();

      setToken(response.accessToken);
      setUser(response);
      setUserProfile(profile);
      setIsAuthenticated(true);
    } catch (err: any) {
      const errorMessage = err.message || 'Registration failed';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      setLoading(true);
      await authService.logout();
      setToken(null);
      setUser(null);
      setUserProfile(null);
      setIsAuthenticated(false);
      setError(null);
    } catch (err: any) {
      console.error('Logout error:', err);
    } finally {
      setLoading(false);
    }
  };

  const refreshUser = async () => {
    try {
      const profile = await authService.getCurrentUser();
      setUserProfile(profile);
    } catch (err: any) {
      console.error('Failed to refresh user profile:', err);
      setError(err.message);
    }
  };

  const clearError = () => {
    setError(null);
  };

  const value: AuthContextType = {
    isAuthenticated,
    user,
    userProfile,
    token,
    loading,
    error,
    login,
    logout,
    register,
    refreshUser,
    clearError,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
