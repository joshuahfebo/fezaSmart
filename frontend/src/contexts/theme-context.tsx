import React, { createContext, useContext, useState, useCallback, useMemo } from 'react'
import { Colors } from '@/constants/theme'

type ThemeMode = 'dark' | 'light'

type ThemeColorMap = { [K in keyof typeof Colors.light]: string }

interface ThemeContextValue {
  mode: ThemeMode
  toggleTheme: () => void
  colors: ThemeColorMap
}

const ThemeContext = createContext<ThemeContextValue | null>(null)

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [mode, setMode] = useState<ThemeMode>('dark')

  const toggleTheme = useCallback(() => {
    setMode((prev) => (prev === 'dark' ? 'light' : 'dark'))
  }, [])

  const value = useMemo<ThemeContextValue>(
    () => ({ mode, toggleTheme, colors: Colors[mode] as ThemeColorMap }),
    [mode, toggleTheme],
  )

  return (
    <ThemeContext.Provider value={value}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useThemeContext() {
  const ctx = useContext(ThemeContext)
  if (!ctx) throw new Error('useThemeContext must be used within ThemeProvider')
  return ctx
}
