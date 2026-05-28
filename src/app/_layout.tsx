// src/app/_layout.tsx
import { AnimatedSplashOverlay } from "@/components/animated-icon";
import { GlassMenu } from "@/components/glass-menu";
import { TamaguiRoot } from "@/components/tamagui-root";
import { Colors } from "@/constants/theme";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import React, { useState } from "react";
import {
    Image,
    LayoutChangeEvent,
    Platform,
    StyleSheet,
    TouchableOpacity,
    View,
    useColorScheme,
    useWindowDimensions,
} from "react-native";
import {
    GestureHandlerRootView,
} from "react-native-gesture-handler";
import Animated, {
    useAnimatedStyle,
    useSharedValue,
    withSpring,
} from "react-native-reanimated";
import { SafeAreaView } from "react-native-safe-area-context";
import { TabView, SceneMap } from "react-native-tab-view";
import ExploreScreen from "./explore";
import HomeScreen from "./index";
import SmartScreen from "./smart";
import ProfileScreen from "./profile";

/* ── FloatingTabBar (liquid glass meniscus) ── */
function FloatingTabBar({ state, setIndex }: any) {
  const scheme = useColorScheme();
  const colors = Colors[scheme === "unspecified" ? "light" : scheme];
  const [barWidth, setBarWidth] = useState(0);
  const [menuOpen, setMenuOpen] = useState(false);

  const numberOfTabs = state.routes.length; // 4
  const totalSlots = numberOfTabs + 1; // menu + tabs
  const INDICATOR_W = 48;
  const INDICATOR_H = 36;

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
          setIndex(0);
          break;
        case "settings":
          setIndex(3);
          break;
        case "contact":
          setIndex(2);
          break;
        default:
          break;
      }
    }, 300);
  };

  return (
    <SafeAreaView edges={["bottom"]} style={styles.wrapper}>
      <View style={styles.barContainer}>
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

          <View style={styles.slotRow} onLayout={onBarLayout}>
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

            {state.routes.map((route: any, index: number) => {
              const isFocused = state.index === index;
              const onPress = () => setIndex(index);

              return (
                <View key={route.key} style={styles.slot}>
                  <TouchableOpacity
                    onPress={onPress}
                    style={styles.slotTouchable}
                    activeOpacity={0.6}
                  >
                    <Image
                      source={iconSource(route.key)}
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

      <GlassMenu
        visible={menuOpen}
        onClose={() => setMenuOpen(false)}
        onAction={handleMenuAction}
      />
    </SafeAreaView>
  );
}

const renderScene = SceneMap({
  explore: ExploreScreen,
  index: HomeScreen,
  smart: SmartScreen,
  profile: ProfileScreen,
});

export default function TabLayout() {
  const layout = useWindowDimensions();
  const [index, setIndex] = useState(1);
  const [routes] = useState([
    { key: "explore", title: "Explore" },
    { key: "index", title: "Home" },
    { key: "smart", title: "Smart" },
    { key: "profile", title: "Profile" },
  ]);

  return (
    <TamaguiRoot>
      <AnimatedSplashOverlay />
      <GestureHandlerRootView style={styles.gestureRoot}>
        <TabView
          navigationState={{ index, routes }}
          renderScene={renderScene}
          renderTabBar={() => null}
          onIndexChange={setIndex}
          initialLayout={{ width: layout.width }}
          swipeEnabled
          lazy
          tabBarPosition="bottom"
        />
        <FloatingTabBar state={{ index, routes }} setIndex={setIndex} />
      </GestureHandlerRootView>
    </TamaguiRoot>
  );
}

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
    backgroundColor: "#0A0A0A",
  },
});
