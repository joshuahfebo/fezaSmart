// src/app/_layout.tsx
import { AnimatedSplashOverlay } from "@/components/animated-icon";
import { GlassMenu } from "@/components/glass-menu";
import { TamaguiRoot } from "@/components/tamagui-root";
import { Colors } from "@/constants/theme";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import { Tabs, router } from "expo-router";
import React, { useCallback, useState } from "react";
import {
    Image,
    LayoutChangeEvent,
    Platform,
    StyleSheet,
    TouchableOpacity,
    View,
    useColorScheme,
} from "react-native";
import {
    GestureHandlerRootView,
    PanGestureHandler,
    State,
} from "react-native-gesture-handler";
import Animated, {
    useAnimatedStyle,
    useSharedValue,
    withSpring,
} from "react-native-reanimated";
import { SafeAreaView } from "react-native-safe-area-context";

/* ── FloatingTabBar (liquid glass meniscus) ── */
function FloatingTabBar({ state, navigation }: any) {
  const scheme = useColorScheme();
  const colors = Colors[scheme === "unspecified" ? "light" : scheme];
  const [barWidth, setBarWidth] = useState(0);
  const [menuOpen, setMenuOpen] = useState(false);

  const numberOfTabs = state.routes.length; // 4
  const totalSlots = numberOfTabs + 1; // menu + tabs
  const INDICATOR_W = 48;
  const INDICATOR_H = 36;
  const BAR_H = 60;
  const BORDER_PAD = 1.5;

  const slotWidth = barWidth > 0 ? barWidth / totalSlots : 0;
  const activeIndex = state.index;

  const indicatorLeft = useSharedValue(0);

  const updateIndicator = () => {
    if (slotWidth <= 0) return;
    const left =
      slotWidth + activeIndex * slotWidth + (slotWidth - INDICATOR_W) / 2;
    indicatorLeft.value = withSpring(left, {
      damping: 20,
      stiffness: 180,
      mass: 0.8,
    });
  };

  React.useEffect(() => {
    updateIndicator();
  }, [activeIndex, slotWidth]);

  const indicatorStyle = useAnimatedStyle(() => ({
    left: indicatorLeft.value,
  }));

  const onBarLayout = (e: LayoutChangeEvent) => {
    const w = e.nativeEvent.layout.width;
    if (w !== barWidth) {
      setBarWidth(w);
      setTimeout(() => updateIndicator(), 0);
    }
  };

  const iconSource = (name: string) => {
    switch (name) {
      case "index":
        return require("@/assets/images/tabIcons/home.png");
      case "explore":
        return require("@/assets/images/tabIcons/tool.png");
      case "smart":
        return require("@/assets/images/tabIcons/fezasmart.png");
      case "profile":
        return require("@/assets/images/tabIcons/user.png");
      default:
        return require("@/assets/images/tabIcons/home.png");
    }
  };

  const handleMenuAction = (action: string) => {
    setMenuOpen(false);
    setTimeout(() => {
      switch (action) {
        case "about":
          router.push("/explore");
          break;
        case "settings":
          router.push("/profile");
          break;
        case "contact":
          router.push("/smart");
          break;
        default:
          break;
      }
    }, 300);
  };

  return (
    <SafeAreaView edges={["bottom"]} style={styles.wrapper}>
      {/* Bar & indicator container */}
      <View style={styles.barContainer}>
        {/* Glass background */}
        <LinearGradient
          colors={[
            "rgba(255,255,255,0.18)",
            "rgba(255,255,255,0.04)",
            "rgba(255,255,255,0.1)",
          ]}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={styles.glassBorder}
        >
          <BlurView
            intensity={55}
            tint="dark"
            style={StyleSheet.absoluteFill}
          />

          {/* Row of slots (menu + tabs) */}
          <View style={styles.slotRow} onLayout={onBarLayout}>
            {/* Menu trigger */}
            <View style={styles.slot}>
              <TouchableOpacity
                onPress={() => setMenuOpen(true)}
                activeOpacity={0.6}
                style={styles.slotTouchable}
              >
                <Image
                  source={require("@/assets/images/tabIcons/more.png")}
                  style={{
                    width: 24,
                    height: 24,
                    tintColor: menuOpen ? colors.primary : colors.textSecondary,
                    opacity: menuOpen ? 1 : 0.65,
                  }}
                  resizeMode="contain"
                />
              </TouchableOpacity>
            </View>

            {/* Tabs */}
            {state.routes.map((route: any, index: number) => {
              const isFocused = state.index === index;
              const onPress = () => {
                const event = navigation.emit({
                  type: "tabPress",
                  target: route.key,
                  canPreventDefault: true,
                });
                if (!isFocused && !event.defaultPrevented) {
                  navigation.navigate(route.name);
                }
              };

              return (
                <View key={route.key} style={styles.slot}>
                  <TouchableOpacity
                    onPress={onPress}
                    style={styles.slotTouchable}
                    activeOpacity={0.6}
                  >
                    <Image
                      source={iconSource(route.name)}
                      style={{
                        width: 24,
                        height: 24,
                        tintColor: isFocused
                          ? colors.primary
                          : colors.textSecondary,
                        opacity: isFocused ? 1 : 0.65,
                      }}
                      resizeMode="contain"
                    />
                  </TouchableOpacity>
                </View>
              );
            })}
          </View>
        </LinearGradient>

        {/* Floating liquid pill */}
        <Animated.View
          style={[
            styles.indicator,
            { width: INDICATOR_W, height: INDICATOR_H },
            indicatorStyle,
          ]}
          pointerEvents="none"
        >
          <BlurView
            intensity={30}
            tint="dark"
            style={StyleSheet.absoluteFill}
          />
          <LinearGradient
            colors={[
              "rgba(255,140,0,0.2)",
              "rgba(255,140,0,0.08)",
              "rgba(255,140,0,0.2)",
            ]}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={StyleSheet.absoluteFill}
          />
        </Animated.View>
      </View>

      {/* Modular glass menu */}
      <GlassMenu
        visible={menuOpen}
        onClose={() => setMenuOpen(false)}
        onAction={handleMenuAction}
      />
    </SafeAreaView>
  );
}

/* ── Root Layout ── */
const SWIPE_DISTANCE_THRESHOLD = 100;

export default function TabLayout() {
  const colorScheme = useColorScheme();
  const tabStateRef = React.useRef<any>(null);
  const tabNavigationRef = React.useRef<any>(null);

  const handleSwipeEnd = useCallback((translationX: number) => {
    const state = tabStateRef.current;
    const navigation = tabNavigationRef.current;

    if (!state || !navigation) {
      return;
    }

    if (
      translationX < -SWIPE_DISTANCE_THRESHOLD &&
      state.index < state.routes.length - 1
    ) {
      navigation.navigate(state.routes[state.index + 1].name);
    } else if (translationX > SWIPE_DISTANCE_THRESHOLD && state.index > 0) {
      navigation.navigate(state.routes[state.index - 1].name);
    }
  }, []);

  const gestureTranslationX = React.useRef(0);
  const gestureTranslationY = React.useRef(0);

  const onGestureEvent = ({ nativeEvent }: any) => {
    gestureTranslationX.current = nativeEvent.translationX;
    gestureTranslationY.current = nativeEvent.translationY;
  };

  const onHandlerStateChange = ({ nativeEvent }: any) => {
    if (
      nativeEvent.oldState === State.ACTIVE &&
      (nativeEvent.state === State.END || nativeEvent.state === State.CANCELLED)
    ) {
      const isHorizontal =
        Math.abs(gestureTranslationX.current) >
        Math.abs(gestureTranslationY.current);
      const isLongEnough =
        Math.abs(gestureTranslationX.current) > SWIPE_DISTANCE_THRESHOLD;

      if (isHorizontal && isLongEnough) {
        handleSwipeEnd(gestureTranslationX.current);
      }
    }
  };

  return (
    <TamaguiRoot>
      <AnimatedSplashOverlay />
      <GestureHandlerRootView style={styles.gestureRoot}>
        <PanGestureHandler
          onGestureEvent={onGestureEvent}
          onHandlerStateChange={onHandlerStateChange}
          activeOffsetX={[-20, 20]}
          failOffsetY={[-15, 15]}
          minPointers={1}
          maxPointers={1}
        >
          <Animated.View style={styles.gestureView}>
            <Tabs
              tabBar={(props) => {
                tabStateRef.current = props.state;
                tabNavigationRef.current = props.navigation;
                return <FloatingTabBar {...props} />;
              }}
              screenOptions={{ headerShown: false }}
            >
              <Tabs.Screen name="explore" />
              <Tabs.Screen name="index" />
              <Tabs.Screen name="smart" />
              <Tabs.Screen name="profile" />
            </Tabs>
          </Animated.View>
        </PanGestureHandler>
      </GestureHandlerRootView>
    </TamaguiRoot>
  );
}

/* ── Styles ── */
const styles = StyleSheet.create({
  wrapper: {
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    alignItems: "center",
  },
  barContainer: {
    width: "78%",
    maxWidth: 420,
    alignSelf: "center",
    height: 63,
    marginBottom: Platform.OS === "ios" ? 12 : 20,
    position: "relative",
  },
  glassBorder: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    borderRadius: 50,
    borderWidth: 1.5,
    borderColor: "transparent",
    overflow: "hidden",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.2,
    shadowRadius: 20,
    elevation: 12,
  },
  slotRow: {
    flexDirection: "row",
    height: "100%",
    alignItems: "center",
    paddingHorizontal: 1.5,
  },
  slot: {
    flex: 1,
    height: "100%",
    alignItems: "center",
    justifyContent: "center",
  },
  slotTouchable: {
    width: "100%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center",
  },
  indicator: {
    position: "absolute",
    top: (63 - 36) / 2,
    borderRadius: 18,
    overflow: "hidden",
    shadowColor: "#FF8C00",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 14,
    elevation: 12,
  },
  gestureRoot: {
    flex: 1,
  },
  gestureView: {
    flex: 1,
  },
});
