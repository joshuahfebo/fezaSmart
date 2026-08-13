import { TamaguiProvider } from '@tamagui/core';
import config from '../../tamagui.config';
import { ReactNode } from 'react';
import { useThemeContext } from '@/contexts/theme-context';

export function TamaguiRoot({ children }: { children: ReactNode }) {
  const { mode } = useThemeContext();

  return (
    <TamaguiProvider config={config} defaultTheme={mode}>
      {children}
    </TamaguiProvider>
  );
}
