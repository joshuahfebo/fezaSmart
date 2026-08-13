import { useThemeContext } from '@/contexts/theme-context'

export function useTheme() {
  return useThemeContext().colors
}

export function useThemeMode() {
  return useThemeContext().mode
}

export function useToggleTheme() {
  return useThemeContext().toggleTheme
}
