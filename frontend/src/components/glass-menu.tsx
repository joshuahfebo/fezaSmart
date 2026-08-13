import { useTheme, useThemeMode } from "@/hooks/use-theme";
import { Ionicons } from "@react-native-vector-icons/ionicons";
import { BlurView } from "expo-blur";
import * as Haptics from "expo-haptics";
import { LinearGradient } from "expo-linear-gradient";
import { useCallback, useEffect } from "react";
import { Dimensions, Modal, Pressable, StyleSheet, View } from "react-native";
import { Gesture, GestureDetector } from "react-native-gesture-handler";
import Animated, {
  Easing,
  runOnJS,
  useAnimatedStyle,
  useSharedValue,
  withSpring,
  withTiming,
  type SharedValue,
} from "react-native-reanimated";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { Text, XStack, YStack } from "tamagui";

const { height: SCREEN_HEIGHT, width: SCREEN_WIDTH } = Dimensions.get("window");
const SHEET_TOP_RADIUS = 40;
const SNAP_POINTS = [SCREEN_HEIGHT * 0.4, SCREEN_HEIGHT * 0.85];
const ANIMATION_DURATION = 400;
const SPRING_CONFIG = {
  damping: 25,
  stiffness: 300,
  mass: 0.8,
  overshootClamping: true,
};
const FAST_SPRING_CONFIG = {
  damping: 20,
  stiffness: 400,
  mass: 0.6,
};

function clamp(value: number, min: number, max: number) {
  "worklet";
  return Math.min(Math.max(value, min), max);
}

interface MenuItemData {
  label: string;
  subtitle: string;
  icon: string;
  action: string;
}

interface GlassMenuProps {
  visible: boolean;
  onClose: () => void;
  onAction: (action: string) => void;
  menuItems?: MenuItemData[];
}

const defaultMenuItems: MenuItemData[] = [
  {
    label: "About Feza",
    subtitle: "Explore our story and mission",
    icon: "information-circle-outline",
    action: "about",
  },
  {
    label: "Settings",
    subtitle: "Notifications, privacy & more",
    icon: "settings-outline",
    action: "settings",
  },
  {
    label: "Contact Us",
    subtitle: "Get in touch with the team",
    icon: "chatbubble-outline",
    action: "contact",
  },
];

function SheetHandle({
  translateY,
  isDark,
}: {
  translateY: SharedValue<number>;
  isDark: boolean;
}) {
  const handleScale = useSharedValue(1);
  const handleOpacity = useSharedValue(1);

  const handleStyle = useAnimatedStyle(() => {
    const scale = handleScale.value;
    const opacity = handleOpacity.value;
    return {
      transform: [{ scale }],
      opacity,
    };
  });

  useEffect(() => {
    handleScale.value = withSpring(1, FAST_SPRING_CONFIG);
    handleOpacity.value = withTiming(1, {
      duration: ANIMATION_DURATION,
      easing: Easing.out(Easing.exp),
    });
  }, []);

  return (
    <View style={styles.handleRow}>
      <View
        style={[
          styles.handleTrack,
          {
            backgroundColor: isDark
              ? "rgba(255,255,255,0.15)"
              : "rgba(0,0,0,0.1)",
          },
        ]}
      >
        <Animated.View style={[styles.handleGradientContainer, handleStyle]}>
          <LinearGradient
            colors={["#FF8C00", "#FFB347", "#FF8C00"]}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={styles.handleGradient}
          />
        </Animated.View>
      </View>
    </View>
  );
}

function SheetContent({
  colors,
  isDark,
  insets,
  translateY,
  menuItems,
  onAction,
  onClose,
}: {
  colors: any;
  isDark: boolean;
  insets: any;
  translateY: SharedValue<number>;
  menuItems: MenuItemData[];
  onAction: (a: string) => void;
  onClose: () => void;
}) {
  const headerScale = useSharedValue(0.9);
  const headerOpacity = useSharedValue(0);
  const itemsOpacity = useSharedValue(0);
  const handleScale = useSharedValue(1);
  const handleOpacity = useSharedValue(1);

  useEffect(() => {
    headerScale.value = withSpring(1, FAST_SPRING_CONFIG);
    headerOpacity.value = withTiming(1, { duration: ANIMATION_DURATION + 100 });
    handleScale.value = withSpring(1, FAST_SPRING_CONFIG);
    handleOpacity.value = withTiming(1, {
      duration: ANIMATION_DURATION,
      easing: Easing.out(Easing.exp),
    });
    setTimeout(() => {
      itemsOpacity.value = withTiming(1, {
        duration: ANIMATION_DURATION + 200,
      });
    }, 100);
  }, []);

  const headerAnimatedStyle = useAnimatedStyle(() => ({
    transform: [{ scale: headerScale.value }],
    opacity: headerOpacity.value,
  }));

  const itemsAnimatedStyle = useAnimatedStyle(() => ({
    opacity: itemsOpacity.value,
  }));

  const handleStyle = useAnimatedStyle(() => {
    const scale = handleScale.value;
    const opacity = handleOpacity.value;
    return {
      transform: [{ scale }],
      opacity,
    };
  });

  const handleMenuAction = useCallback(
    (action: string) => {
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
      onAction(action);
      onClose();
    },
    [onAction, onClose],
  );

  const handleClose = useCallback(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    onClose();
  }, [onClose]);

  return (
    <LinearGradient
      colors={[
        isDark ? "rgba(40,40,46,0.92)" : "rgba(255,255,255,0.92)",
        isDark ? "rgba(28,28,34,0.92)" : "rgba(245,243,240,0.92)",
      ]}
      start={{ x: 0, y: 0 }}
      end={{ x: 0, y: 1 }}
      style={styles.sheetGradient}
    >
      <BlurView
        intensity={isDark ? 40 : 60}
        tint={isDark ? "dark" : "light"}
        style={styles.sheetBlur}
      >
        <Animated.View style={[styles.headerContainer, headerAnimatedStyle]}>
          <View style={[styles.handleRow, { paddingTop: 8 }]}>
            <View
              style={[
                styles.handleTrack,
                {
                  backgroundColor: isDark
                    ? "rgba(255,255,255,0.15)"
                    : "rgba(0,0,0,0.1)",
                },
              ]}
            >
              <Animated.View
                style={[styles.handleGradientContainer, handleStyle]}
              >
                <LinearGradient
                  colors={["#FF8C00", "#FFB347", "#FF8C00"]}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 0 }}
                  style={styles.handleGradient}
                />
              </Animated.View>
            </View>
          </View>

          <XStack
            alignItems="center"
            gap="$4"
            paddingHorizontal="$6"
            paddingTop="$7"
          >
            <View
              style={[
                styles.sheetIcon,
                {
                  backgroundColor: isDark
                    ? "rgba(255,140,0,0.2)"
                    : "rgba(255,140,0,0.15)",
                  shadowColor: colors.primary,
                  shadowOffset: { width: 0, height: 0 },
                  shadowOpacity: 0.3,
                  shadowRadius: 8,
                  elevation: 4,
                },
              ]}
            >
              <Ionicons
                name="layers-outline"
                size={24}
                color={colors.primary}
              />
            </View>
            <YStack flex={1} gap={1}>
              <Text
                color={isDark ? "#FFF" : "#1A1A1A"}
                fontSize={24}
                fontWeight="700"
                letterSpacing={-0.5}
              >
                Quick Menu
              </Text>
              <Text
                color={isDark ? "rgba(255,255,255,0.5)" : "rgba(0,0,0,0.4)"}
                fontSize={14}
                fontWeight="400"
              >
                Navigate anywhere in Feza
              </Text>
            </YStack>
            <Pressable
              onPress={handleClose}
              style={({ pressed }) => [
                styles.closeIconBtn,
                // { backgroundColor: colors.close },
                pressed && { opacity: 0.6, transform: [{ scale: 0.95 }] },
              ]}
            >
              <View
                style={[
                  styles.closeIconBg,
                  {
                    backgroundColor: isDark
                      ? "rgba(255,255,255,0.15)"
                      : "rgba(0,0,0,0.08)",
                    shadowColor: isDark
                      ? "rgba(255,255,255,0.2)"
                      : "rgba(0,0,0,0.1)",
                    shadowOffset: { width: 0, height: 2 },
                    shadowOpacity: 0.5,
                    shadowRadius: 4,
                  },
                ]}
              >
                <Ionicons
                  name="close"
                  size={20}
                  color={isDark ? "rgba(255,255,255,0.6)" : "rgba(0,0,0,0.5)"}
                />
              </View>
            </Pressable>
          </XStack>

          <View
            style={[
              styles.divider,
              {
                backgroundColor: isDark
                  ? "rgba(255,255,255,0.1)"
                  : "rgba(0,0,0,0.06)",
                marginHorizontal: 24,
                marginTop: 16,
              },
            ]}
          />
        </Animated.View>

        <Animated.View style={[styles.itemsContainer, itemsAnimatedStyle]}>
          <YStack gap="$3" paddingHorizontal="$5" paddingTop="$2">
            {menuItems.map((item, index) => (
              <Pressable
                key={index}
                onPress={() => handleMenuAction(item.action)}
                style={({ pressed }) => [
                  styles.menuItemOuter,
                  {
                    transform: pressed ? [{ scale: 0.98 }] : [{ scale: 1 }],
                    opacity: pressed ? 0.85 : 1,
                  },
                ]}
              >
                <LinearGradient
                  colors={
                    isDark
                      ? ["rgba(255,255,255,0.12)", "rgba(255,255,255,0.04)"]
                      : ["rgba(255,255,255,0.6)", "rgba(255,255,255,0.25)"]
                  }
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 1 }}
                  style={styles.menuItemGradient}
                >
                  <BlurView
                    intensity={isDark ? 16 : 8}
                    tint={isDark ? "dark" : "light"}
                    style={styles.menuItemBlur}
                  >
                    <XStack
                      gap="$4"
                      alignItems="center"
                      paddingHorizontal="$4"
                      paddingVertical="$4"
                    >
                      <View
                        style={[
                          styles.itemIcon,
                          {
                            backgroundColor: isDark
                              ? "rgba(255,140,0,0.18)"
                              : "rgba(255,140,0,0.12)",
                            shadowColor: colors.primary,
                            shadowOffset: { width: 0, height: 0 },
                            shadowOpacity: 0.25,
                            shadowRadius: 6,
                          },
                        ]}
                      >
                        <Ionicons
                          name={item.icon as any}
                          size={22}
                          color={colors.primary}
                        />
                      </View>
                      <YStack flex={1} gap={1}>
                        <Text
                          color={isDark ? "#FFF" : "#1A1A1A"}
                          fontSize={17}
                          fontWeight="600"
                          letterSpacing={0.2}
                        >
                          {item.label}
                        </Text>
                        <Text
                          color={
                            isDark
                              ? "rgba(255,255,255,0.5)"
                              : "rgba(0,0,0,0.45)"
                          }
                          fontSize={14}
                          fontWeight="400"
                        >
                          {item.subtitle}
                        </Text>
                      </YStack>
                      <View
                        style={[
                          styles.chevronCircle,
                          {
                            backgroundColor: isDark
                              ? "rgba(255,255,255,0.12)"
                              : "rgba(0,0,0,0.06)",
                            shadowColor: isDark
                              ? "rgba(255,255,255,0.2)"
                              : "rgba(0,0,0,0.1)",
                            shadowOffset: { width: 0, height: 1 },
                            shadowOpacity: 0.3,
                            shadowRadius: 3,
                          },
                        ]}
                      >
                        <Ionicons
                          name="chevron-forward"
                          size={16}
                          color={
                            isDark ? "rgba(255,255,255,0.5)" : "rgba(0,0,0,0.4)"
                          }
                        />
                      </View>
                    </XStack>
                  </BlurView>
                </LinearGradient>
              </Pressable>
            ))}
          </YStack>

          <Pressable
            onPress={handleClose}
            style={({ pressed }) => [
              styles.closeBtn,
              {
                backgroundColor: colors.primaryDim,
                marginHorizontal: 20,
                marginBottom: insets.bottom + 16,
              },
              pressed && {
                opacity: 0.85,
                transform: [{ scale: 0.98 }],
              },
            ]}
          >
            <Text
              color={colors.text}
              fontSize={18}
              fontWeight="800"
              textAlign="center"
            >
              Dismiss
            </Text>
          </Pressable>
        </Animated.View>
      </BlurView>
    </LinearGradient>
  );
}

export function GlassMenu({
  visible,
  onClose,
  onAction,
  menuItems = defaultMenuItems,
}: GlassMenuProps) {
  const colors = useTheme();
  const mode = useThemeMode();
  const insets = useSafeAreaInsets();
  const isDark = mode === "dark";
  const translateY = useSharedValue(SCREEN_HEIGHT);
  const context = useSharedValue(0);
  const backdropOpacity = useSharedValue(0);
  const sheetScale = useSharedValue(0.95);

  useEffect(() => {
    if (visible) {
      backdropOpacity.value = withTiming(1, { duration: ANIMATION_DURATION });
      sheetScale.value = withSpring(1, SPRING_CONFIG);
      translateY.value = withSpring(0, SPRING_CONFIG);
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    } else {
      backdropOpacity.value = withTiming(0, {
        duration: ANIMATION_DURATION * 0.8,
      });
    }
  }, [visible]);

  const closeSheet = () => {
    "worklet";
    runOnJS(onClose)();
  };

  const panGesture = Gesture.Pan()
    .onStart(() => {
      context.value = translateY.value;
    })
    .onUpdate((e) => {
      const dragging = context.value + e.translationY;
      translateY.value = dragging < 0 ? dragging * 0.3 : dragging;
    })
    .onEnd((e) => {
      const shouldClose =
        e.velocityY > 200 || translateY.value > SCREEN_HEIGHT * 0.2;

      if (shouldClose) {
        backdropOpacity.value = withTiming(0, {
          duration: ANIMATION_DURATION * 0.7,
        });
        translateY.value = withSpring(
          SCREEN_HEIGHT,
          {
            damping: 22,
            stiffness: 280,
            mass: 0.7,
            overshootClamping: true,
          },
          (finished) => {
            if (finished) {
              sheetScale.value = 0.95;
              runOnJS(onClose)();
            }
          },
        );
      } else {
        backdropOpacity.value = withTiming(1, {
          duration: ANIMATION_DURATION * 0.5,
        });
        translateY.value = withSpring(0, SPRING_CONFIG);
      }
    });

  const backdropStyle = useAnimatedStyle(() => ({
    opacity: backdropOpacity.value,
  }));

  const sheetStyle = useAnimatedStyle(() => ({
    transform: [{ translateY: translateY.value }, { scale: sheetScale.value }],
  }));

  const handleBackdropPress = useCallback(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    onClose();
  }, [onClose]);

  return (
    <Modal
      visible={visible}
      transparent
      animationType="none"
      onRequestClose={onClose}
      statusBarTranslucent
    >
      <Pressable style={StyleSheet.absoluteFill} onPress={handleBackdropPress}>
        <Animated.View
          style={[StyleSheet.absoluteFill, styles.backdrop, backdropStyle]}
        />
      </Pressable>

      <GestureDetector gesture={panGesture}>
        <Animated.View
          style={[
            styles.sheet,
            sheetStyle,
            { shadowColor: colors.shadowStrong },
          ]}
        >
          <SheetContent
            colors={colors}
            isDark={isDark}
            insets={insets}
            translateY={translateY}
            menuItems={menuItems}
            onAction={onAction}
            onClose={onClose}
          />
        </Animated.View>
      </GestureDetector>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    backgroundColor: "rgba(0,0,0,0.65)",
  },
  sheet: {
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    borderTopLeftRadius: SHEET_TOP_RADIUS,
    borderTopRightRadius: SHEET_TOP_RADIUS,
    overflow: "hidden",
    shadowOffset: { width: 0, height: -12 },
    shadowOpacity: 0.4,
    shadowRadius: 40,
    elevation: 20,
  },
  sheetGradient: {
    flex: 1,
    borderTopLeftRadius: SHEET_TOP_RADIUS,
    borderTopRightRadius: SHEET_TOP_RADIUS,
  },
  sheetBlur: {
    flex: 1,
    borderTopLeftRadius: SHEET_TOP_RADIUS,
    borderTopRightRadius: SHEET_TOP_RADIUS,
  },
  headerContainer: {
    paddingTop: 8,
  },
  itemsContainer: {
    flex: 1,
  },
  handleRow: {
    position: "absolute",
    top: 12,
    left: 0,
    right: 0,
    alignItems: "center",
    zIndex: 10,
  },
  handleTrack: {
    width: 48,
    height: 5,
    borderRadius: 2.5,
    overflow: "hidden",
  },
  handleGradientContainer: {
    width: "100%",
    height: "100%",
    borderRadius: 2.5,
  },
  handleGradient: {
    width: "100%",
    height: "100%",
    borderRadius: 2.5,
  },
  sheetIcon: {
    width: 48,
    height: 48,
    borderRadius: 16,
    alignItems: "center",
    justifyContent: "center",
  },
  closeIconBtn: {
    width: 40,
    height: 40,
    borderRadius: 20,
    alignItems: "center",
    justifyContent: "center",
  },
  closeIconBg: {
    width: 36,
    height: 36,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
  },
  divider: {
    height: StyleSheet.hairlineWidth,
  },
  itemIcon: {
    width: 44,
    height: 44,
    borderRadius: 14,
    alignItems: "center",
    justifyContent: "center",
  },
  menuItemOuter: {
    borderRadius: 18,
    marginBottom: 4,
  },
  menuItemGradient: {
    borderRadius: 18,
    overflow: "hidden",
  },
  menuItemBlur: {
    borderRadius: 18,
    overflow: "hidden",
  },
  chevronCircle: {
    width: 32,
    height: 32,
    borderRadius: 16,
    alignItems: "center",
    justifyContent: "center",
  },
  closeBtn: {
    marginTop: 10,
    paddingVertical: 16,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
  },
});
