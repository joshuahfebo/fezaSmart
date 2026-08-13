import { View, type ViewProps } from 'react-native';

import { ThemeColorKey } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

export type ThemedViewProps = ViewProps & {
  lightColor?: string;
  darkColor?: string;
  type?: ThemeColorKey;
};

export function ThemedView({ style, lightColor, darkColor, type, ...otherProps }: ThemedViewProps) {
  const colors = useTheme();

  return <View style={[{ backgroundColor: type ? colors[type] : colors.background }, style]} {...otherProps} />;
}
