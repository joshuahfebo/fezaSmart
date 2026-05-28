import { TamaguiProvider } from '@tamagui/core';
import config from '../../tamagui.config';
import { ReactNode } from 'react';

export function TamaguiRoot({ children }: { children: ReactNode }) {
  return (
    <TamaguiProvider config={config} defaultTheme="dark">
      {children}
    </TamaguiProvider>
  );
}
