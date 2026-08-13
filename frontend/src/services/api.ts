import axios, { AxiosInstance, AxiosError } from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { Platform } from 'react-native';

const getBaseUrl = () => {
  if (__DEV__) {
    // Android emulator uses 10.0.2.2 to reach host machine's localhost
    if (Platform.OS === 'android') {
      return 'http://10.0.2.2:8080/api';
    }
    // iOS simulator can use localhost directly
    return 'http://localhost:8080/api';
  }
  // Production URL
  return 'https://api.fezaschools.ac.ke/api';
};

const API_BASE_URL = getBaseUrl();

class ApiClient {
  private axiosInstance: AxiosInstance;
  private refreshTokenPromise: Promise<string> | null = null;

  constructor() {
    this.axiosInstance = axios.create({
      baseURL: API_BASE_URL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // Request interceptor - add JWT token to headers
    this.axiosInstance.interceptors.request.use(
      async (config) => {
        const token = await AsyncStorage.getItem('jwt_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor - handle 401 errors and token refresh
    this.axiosInstance.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as any;

        // If 401 and not already retried
        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;

          try {
            // Use a promise queue to avoid multiple simultaneous refresh attempts
            if (!this.refreshTokenPromise) {
              this.refreshTokenPromise = this.refreshAccessToken();
            }

            const newToken = await this.refreshTokenPromise;
            this.refreshTokenPromise = null;

            if (newToken) {
              originalRequest.headers.Authorization = `Bearer ${newToken}`;
              return this.axiosInstance(originalRequest);
            }
          } catch (refreshError) {
            // Refresh failed - logout user
            await this.clearAuth();
            // Redirect to login will be handled by auth context
            return Promise.reject(refreshError);
          }
        }

        return Promise.reject(error);
      }
    );
  }

  private async refreshAccessToken(): Promise<string> {
    try {
      const refreshToken = await AsyncStorage.getItem('refresh_token');
      if (!refreshToken) {
        throw new Error('No refresh token available');
      }

      const response = await axios.post(
        `${API_BASE_URL}/auth/refresh`,
        { refreshToken },
        {
          headers: { 'Content-Type': 'application/json' },
        }
      );

      const { accessToken } = response.data;
      await AsyncStorage.setItem('jwt_token', accessToken);
      return accessToken;
    } catch (error) {
      await this.clearAuth();
      throw error;
    }
  }

  private async clearAuth() {
    await AsyncStorage.removeItem('jwt_token');
    await AsyncStorage.removeItem('refresh_token');
    await AsyncStorage.removeItem('user_data');
  }

  public getAxiosInstance(): AxiosInstance {
    return this.axiosInstance;
  }

  // Generic GET request
  public async get<T>(url: string, params?: any): Promise<T> {
    const response = await this.axiosInstance.get<T>(url, { params });
    return response.data;
  }

  // Generic POST request
  public async post<T>(url: string, data: any): Promise<T> {
    const response = await this.axiosInstance.post<T>(url, data);
    return response.data;
  }

  // Generic PUT request
  public async put<T>(url: string, data: any): Promise<T> {
    const response = await this.axiosInstance.put<T>(url, data);
    return response.data;
  }

  // Generic DELETE request
  public async delete<T>(url: string): Promise<T> {
    const response = await this.axiosInstance.delete<T>(url);
    return response.data;
  }
}

export const apiClient = new ApiClient();
