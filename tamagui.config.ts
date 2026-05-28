import { createFont, createTamagui, createTokens, createTheme } from '@tamagui/core';

const headingFont = createFont({
  face: {
    700: { normal: 'System' },
    800: { normal: 'System' },
    900: { normal: 'System' },
  },
  size: {
    true: 16,
  },
  transform: {
    7: { letterSpacing: 0.5 },
    8: { letterSpacing: 0.25 },
    9: { letterSpacing: 0 },
  },
  weight: {
    7: '700',
    8: '800',
    9: '900',
  },
});

const bodyFont = createFont({
  face: {
    400: { normal: 'System' },
    700: { normal: 'System' },
  },
  size: {
    true: 14,
  },
  transform: {
    6: { letterSpacing: 0.25 },
  },
  weight: {
    6: '400',
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
    accent: '#00d4ff',
    accentDim: '#00a8cc',
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
  background: tokens.color.dark0,
  backgroundHover: tokens.color.dark1,
  backgroundPress: tokens.color.dark2,
  backgroundFocus: tokens.color.dark2,
  borderColor: tokens.color.dark3,
  color: tokens.color.white,
  colorHover: tokens.color.white,
  colorPress: tokens.color.accent,
  colorFocus: tokens.color.accent,
  shadowColor: '#000000',
  placeholderColor: '#808080',
});

const lightTheme = createTheme({
  background: tokens.color.white,
  backgroundHover: '#f5f5f5',
  backgroundPress: '#efefef',
  backgroundFocus: '#efefef',
  borderColor: '#e0e0e0',
  color: tokens.color.black,
  colorHover: tokens.color.black,
  colorPress: tokens.color.accentDim,
  colorFocus: tokens.color.accentDim,
  shadowColor: '#000000',
  placeholderColor: '#808080',
});

const config = createTamagui({
  tokens,
  themes: {
    dark: darkTheme,
    light: lightTheme,
  },
  shouldAddPx: true,
  fonts: {
    heading: headingFont,
    body: bodyFont,
  },
});

export type AppConfig = typeof config;

declare module '@tamagui/core' {
  interface TamaguiCustomConfig extends AppConfig {}
}

export default config;
