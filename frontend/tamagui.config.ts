import { createFont, createTamagui, createTokens, createTheme } from '@tamagui/core';

const interFont = createFont({
  face: {
    300: { normal: 'Inter' },
    400: { normal: 'Inter' },
    500: { normal: 'Inter' },
    600: { normal: 'Inter' },
    700: { normal: 'Inter' },
    800: { normal: 'Inter' },
    900: { normal: 'Inter' },
  },
  size: {
    1: 11,
    2: 12,
    3: 14,
    4: 15,
    5: 17,
    6: 20,
    7: 24,
    8: 30,
    9: 38,
    10: 48,
    11: 60,
  },
  lineHeight: {
    1: 14,
    2: 16,
    3: 20,
    4: 22,
    5: 24,
    6: 28,
    7: 32,
    8: 38,
    9: 46,
    10: 56,
    11: 68,
  },
  weight: {
    1: '400',
    2: '400',
    3: '400',
    4: '400',
    5: '500',
    6: '600',
    7: '700',
    8: '800',
    9: '800',
    10: '900',
    11: '900',
  },
  letterSpacing: {
    1: 0.5,
    2: 0.3,
    3: 0.15,
    4: 0,
    5: -0.2,
    6: -0.3,
    7: -0.5,
    8: -0.6,
    9: -0.8,
    10: -1.0,
    11: -1.2,
  },
});

const tokens = createTokens({
  color: {
    white: '#ffffff',
    black: '#000000',
    dark0: '#0a0a0a',
    dark1: '#1a1a1a',
    dark2: '#2d2d2d',
    dark3: '#3a3a3a',
    accent: '#FF8C00',
    accentDim: '#e67e00',
    success: '#10b981',
    warning: '#f59e0b',
    error: '#ef4444',
  },
  space: {
    0: 0,
    1: 4,
    2: 8,
    3: 12,
    4: 16,
    5: 20,
    6: 24,
    7: 28,
    8: 32,
    9: 36,
    10: 40,
  },
  size: {
    0: 0,
    1: 4,
    2: 8,
    3: 12,
    4: 16,
    5: 20,
    6: 24,
    7: 28,
    8: 32,
    9: 36,
    10: 40,
    full: '100%',
  },
  radius: {
    0: 0,
    1: 4,
    2: 8,
    3: 12,
    4: 16,
    5: 20,
    6: 24,
    true: 8,
  },
  zIndex: {
    0: 0,
    1: 100,
    2: 200,
    3: 300,
  },
});

const darkTheme = createTheme({
  background: '#0A0A0A',
  backgroundHover: '#1a1a1a',
  backgroundPress: '#2d2d2d',
  backgroundFocus: '#2d2d2d',
  borderColor: '#3a3a3a',
  color: '#FFFFFF',
  colorHover: '#FFFFFF',
  colorPress: '#FF8C00',
  colorFocus: '#FF8C00',
  shadowColor: '#000000',
  placeholderColor: '#808080',
});

const lightTheme = createTheme({
  background: '#F8F6F3',
  backgroundHover: '#F0EDE8',
  backgroundPress: '#E8E5E0',
  backgroundFocus: '#E8E5E0',
  borderColor: 'rgba(0,0,0,0.1)',
  color: '#1A1A1A',
  colorHover: '#1A1A1A',
  colorPress: '#FF8C00',
  colorFocus: '#FF8C00',
  shadowColor: 'rgba(0,0,0,0.08)',
  placeholderColor: '#A0A0A0',
});

const config = createTamagui({
  tokens,
  themes: {
    dark: darkTheme,
    light: lightTheme,
  },
  shouldAddPx: true,
  fonts: {
    heading: interFont,
    body: interFont,
  },
});

export type AppConfig = typeof config;

declare module '@tamagui/core' {
  interface TamaguiCustomConfig extends AppConfig {}
}

export default config;
