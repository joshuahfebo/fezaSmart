import LottieView from 'lottie-react-native';
import { useEffect, useState } from 'react';
import { StyleSheet, View } from 'react-native';

interface LottieLoadingScreenProps {
  mode: 'light' | 'dark';
}

export function LottieLoadingScreen({ mode }: LottieLoadingScreenProps) {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
    }, 4200);

    return () => clearTimeout(timer);
  }, []);

  if (!visible) return null;

  return (
    <View style={styles.container}>
      <LottieView
        source={
          mode === 'light'
            ? require('@/assets/images/lottieImages/forLightMode.json')
            : require('@/assets/images/lottieImages/forDarkMode.json')
        }
        autoPlay
        loop={false}
        style={styles.animation}
        resizeMode="cover"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFill,
    zIndex: 1000,
    justifyContent: 'center',
    alignItems: 'center',
  },
  animation: {
    width: '100%',
    height: '100%',
  },
});
