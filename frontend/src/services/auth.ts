import { apiClient } from './api';
import AsyncStorage from '@react-native-async-storage/async-storage';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  roles: string[];
  userId: number;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber?: string;
  roleId?: number;
}

export interface UserProfile {
  id: number;
  username: string;
  email?: string;
  phone?: string;
  isActive?: boolean;
  emailVerified?: boolean;
  userRoleRoles?: number[];
  createdAt?: string;
}

class AuthService {
  /**
   * Login with email and password
   */
  async login(request: LoginRequest): Promise<LoginResponse> {
    try {
      const response = await apiClient.post<LoginResponse>(
        '/auth/login',
        request
      );

      // Store token and refresh token in AsyncStorage
      await AsyncStorage.setItem('jwt_token', response.accessToken);
      await AsyncStorage.setItem('refresh_token', response.refreshToken);
      await AsyncStorage.setItem('user_data', JSON.stringify(response));

      return response;
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Register a new user
   */
  async register(request: RegisterRequest): Promise<LoginResponse> {
    try {
      const response = await apiClient.post<LoginResponse>(
        '/auth/register',
        request
      );

      // Store token in AsyncStorage
      await AsyncStorage.setItem('jwt_token', response.accessToken);
      await AsyncStorage.setItem('refresh_token', response.refreshToken);
      await AsyncStorage.setItem('user_data', JSON.stringify(response));

      return response;
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Get current user profile
   */
  async getCurrentUser(): Promise<UserProfile> {
    try {
      const response = await apiClient.get<UserProfile>('/users/me');
      return response;
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Logout - clear stored tokens
   */
  async logout(): Promise<void> {
    await AsyncStorage.removeItem('jwt_token');
    await AsyncStorage.removeItem('refresh_token');
    await AsyncStorage.removeItem('user_data');
  }

  /**
   * Get stored token from AsyncStorage
   */
  async getStoredToken(): Promise<string | null> {
    return await AsyncStorage.getItem('jwt_token');
  }

  /**
   * Get stored user data from AsyncStorage
   */
  async getStoredUserData(): Promise<LoginResponse | null> {
    const data = await AsyncStorage.getItem('user_data');
    return data ? JSON.parse(data) : null;
  }

  /**
   * Check if user is authenticated
   */
  async isAuthenticated(): Promise<boolean> {
    const token = await this.getStoredToken();
    return !!token;
  }

  /**
   * Change password
   */
  async changePassword(
    oldPassword: string,
    newPassword: string
  ): Promise<void> {
    try {
      await apiClient.put('/users/me/password', {
        oldPassword,
        newPassword,
      });
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Verify email
   */
  async verifyEmail(token: string): Promise<void> {
    try {
      await apiClient.post('/auth/verify-email', { token });
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Request password reset
   */
  async requestPasswordReset(email: string): Promise<void> {
    try {
      await apiClient.post('/auth/forgot-password', { email });
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * Reset password with token
   */
  async resetPassword(token: string, newPassword: string): Promise<void> {
    try {
      await apiClient.post('/auth/reset-password', { token, newPassword });
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
          return new Error('Invalid username or password');
        case 403:
          return new Error('Access denied');
        case 404:
          return new Error('User not found');
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

export const authService = new AuthService();
