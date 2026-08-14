import { useTheme, useThemeMode } from "@/hooks/use-theme";
import { useAuth } from "@/contexts/auth-context";
import { Ionicons } from "@react-native-vector-icons/ionicons";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import {
  Alert,
  Dimensions,
  Image,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  View,
  ActivityIndicator,
} from "react-native";
import Animated, {
  Easing,
  Extrapolate,
  interpolate,
  useAnimatedStyle,
  useSharedValue,
  withDelay,
  withSpring,
  withTiming,
} from "react-native-reanimated";
import {
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { Text, XStack, YStack } from "tamagui";
import ClassesScreen from "./classes";
import DashboardScreen from "./dashboard";
import ProfileScreen from "./profile";
import ResultsScreen from "./results";

const { width: screenWidth, height } = Dimensions.get("window");

const TABS = [
  {
    key: "dashboard",
    icon: "home",
    iconOutline: "home-outline",
    label: "Dashboard",
  },
  {
    key: "results",
    icon: "bar-chart",
    iconOutline: "bar-chart-outline",
    label: "Results",
  },
  {
    key: "classes",
    icon: "school",
    iconOutline: "school-outline",
    label: "Classes",
  },
  {
    key: "profile",
    icon: "person",
    iconOutline: "person-outline",
    label: "Profile",
  },
];

const TAB_INDEX_MAP: Record<string, number> = {
  dashboard: 0,
  results: 1,
  classes: 2,
  profile: 3,
};

/* ── SmartRevealSection — mount-based staggered entrance ── */
export function SmartRevealSection({
  index,
  delay = 0,
  children,
  style,
}: {
  index: number;
  delay?: number;
  children: React.ReactNode;
  style?: any;
}) {
  const progress = useSharedValue(0);

  useEffect(() => {
    progress.value = withDelay(
      delay + index * 80,
      withTiming(1, { duration: 450, easing: Easing.out(Easing.cubic) }),
    );
  }, []);

  const animatedStyle = useAnimatedStyle(() => ({
    opacity: interpolate(progress.value, [0, 1], [0, 1], Extrapolate.CLAMP),
    transform: [
      {
        translateY: interpolate(
          progress.value,
          [0, 1],
          [28, 0],
          Extrapolate.CLAMP,
        ),
      },
    ],
  }));

  return (
    <Animated.View style={[{ overflow: "visible" }, animatedStyle, style]}>
      {children}
    </Animated.View>
  );
}

export default function SmartScreen({
  onLoginStateChange,
}: {
  onLoginStateChange?: (loggedIn: boolean) => void;
}) {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === "dark";
  const insets = useSafeAreaInsets();
  const { isAuthenticated, login, logout, loading, error, clearError } =
    useAuth();
  const [activeTab, setActiveTab] = useState("dashboard");

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [secureText, setSecureText] = useState(true);
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async () => {
    if (!username || !password) {
      Alert.alert("Error", "Please enter both email and password");
      return;
    }
    setIsLoading(true);
    clearError?.();
    try {
      await login(username, password);
      onLoginStateChange?.(true);
    } catch (err: any) {
      Alert.alert("Login Failed", err?.message || "Unable to sign in");
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    setActiveTab("dashboard");
    setUsername("");
    setPassword("");
    setSecureText(true);
    onLoginStateChange?.(false);
  };

  if (!isAuthenticated) {
    return (
      <LoginForm
        colors={colors}
        mode={mode}
        username={username}
        setUsername={setUsername}
        password={password}
        setPassword={setPassword}
        secureText={secureText}
        setSecureText={setSecureText}
        isLoading={isLoading || loading}
        onLogin={handleLogin}
        error={error}
      />
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      {activeTab === "dashboard" && <DashboardScreen />}
      {activeTab === "results" && <ResultsScreen />}
      {activeTab === "classes" && <ClassesScreen />}
      {activeTab === "profile" && <ProfileScreen onLogout={handleLogout} />}

      {/* Floating Logout Button */}
      <View
        style={[
          styles.logoutContainer,
          { top: insets.top + 16, right: 16, zIndex: 9999 },
        ]}
      >
        <Pressable
          onPress={handleLogout}
          style={({ pressed }) => [
            styles.logoutButton,
            {
              backgroundColor: isDark
                ? "rgba(255,255,255,0.12)"
                : "rgba(0,0,0,0.06)",
              opacity: pressed ? 0.8 : 1,
              transform: pressed ? [{ scale: 0.95 }] : [{ scale: 1 }],
            },
          ]}
        >
          <Ionicons
            name="log-out-outline"
            size={20}
            color={isDark ? "#FFF" : "#1A1A1A"}
          />
          <Text
            color={isDark ? "#FFF" : "#1A1A1A"}
            fontSize={12}
            fontWeight="600"
            marginLeft={4}
          >
            Logout
          </Text>
        </Pressable>
      </View>

      {/* Floating Tab Bar */}
      <SmartTabBar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        colors={colors}
        isDark={isDark}
        insets={insets}
      />
    </View>
  );
}

/* ── Login Form ── */
function LoginForm({
  colors,
  mode,
  username,
  setUsername,
  password,
  setPassword,
  secureText,
  setSecureText,
  isLoading,
  onLogin,
  error,
}: {
  colors: any;
  mode: string;
  username: string;
  setUsername: (v: string) => void;
  password: string;
  setPassword: (v: string) => void;
  secureText: boolean;
  setSecureText: (v: boolean) => void;
  isLoading: boolean;
  onLogin: () => void;
  error?: string | null;
}) {
  const logoSize = useMemo(() => {
    const maxWidth = screenWidth * 0.5;
    const aspect = 180 / 100;
    return { width: maxWidth, height: maxWidth / aspect };
  }, [screenWidth]);
  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <View style={StyleSheet.absoluteFill}>
        <View
          style={[
            StyleSheet.absoluteFill,
            { backgroundColor: colors.background },
          ]}
        />
        <BlurView
          intensity={30}
          tint={mode === "light" ? "light" : "dark"}
          style={StyleSheet.absoluteFill}
        />
        <View
          style={[
            StyleSheet.absoluteFill,
            { backgroundColor: colors.orangeBg },
          ]}
        />
      </View>

      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === "ios" ? "padding" : "height"}
      >
        <ScrollView
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
          keyboardShouldPersistTaps="handled"
        >
          <SafeAreaView style={{ flex: 1 }}>
            <YStack gap="$6" padding="$4" justifyContent="center" flex={1}>
              <YStack alignItems="center" gap="$2">
                <View
                  style={{
                    width: logoSize.width,
                    height: logoSize.height,
                    borderRadius: 20,
                    // backgroundColor: colors.orangeBgMedium,
                    alignItems: "center",
                    justifyContent: "center",
                    marginBottom: 0,
                  }}
                >
                  <XStack left={13} style={{}}>
                    <Image
                      source={
                        mode === "dark"
                          ? require("../../../assets/images/logoIcons/logoLight.webp")
                          : require("../../../assets/images/logoIcons/logoDark.png")
                      }
                      style={{
                        width: logoSize.width,
                        height: logoSize.height,
                        marginRight: 20,
                        marginBottom: 0,
                      }}
                      resizeMode="contain"
                    />
                    <Text
                      fontSize={36}
                      fontWeight="900"
                      letterSpacing={-1}
                      color={colors.text}
                      lineHeight={42}
                      left={-25}
                    >
                      ™
                    </Text>
                  </XStack>
                  {/*  */}
                </View>
                <XStack>
                  <XStack
                    width={screenWidth * 0.25}
                    height={6}
                    borderTopLeftRadius={3}
                    borderTopRightRadius={0}
                    borderBottomLeftRadius={0}
                    borderBottomRightRadius={0}
                    backgroundColor={colors.text}
                  />
                  <XStack
                    width={screenWidth * 0.25}
                    height={6}
                    borderTopLeftRadius={0}
                    borderTopRightRadius={0}
                    borderBottomLeftRadius={0}
                    borderBottomRightRadius={3}
                    backgroundColor={colors.primary}
                  />
                </XStack>

                <Text
                  fontSize={36}
                  fontWeight="900"
                  letterSpacing={-1}
                  color={colors.text}
                  lineHeight={42}
                >
                  <Ionicons name="school" size={32} color={colors.primary} />{" "}
                  Feza Smart
                </Text>
                <Text
                  color={colors.textTertiary}
                  fontSize={15}
                  fontWeight="600"
                  letterSpacing={0.15}
                  textAlign="center"
                >
                  Students, Teachers, Staff connected.
                </Text>
                {/* <XStack
                  width={screenWidth * 0.7}
                  height={6}
                  borderTopLeftRadius={0}
                  borderTopRightRadius={0}
                  borderBottomLeftRadius={3}
                  borderBottomRightRadius={3}
                  backgroundColor={colors.primary}
                /> */}
              </YStack>

              <YStack gap="$4" marginTop="$2">
                <GlassInput
                  colors={colors}
                  mode={mode}
                  icon="person-outline"
                  placeholder="Email or Username"
                  value={username}
                  onChangeText={setUsername}
                  autoCapitalize="none"
                  keyboardType="email-address"
                />
                <GlassInput
                  colors={colors}
                  mode={mode}
                  icon="lock-closed-outline"
                  placeholder="Password"
                  value={password}
                  onChangeText={setPassword}
                  secureTextEntry={secureText}
                  rightIcon={
                    <TouchableOpacity
                      onPress={() => setSecureText(!secureText)}
                      style={{ padding: 4 }}
                    >
                      <Ionicons
                        name={secureText ? "eye-off-outline" : "eye-outline"}
                        size={20}
                        color={colors.textTertiary}
                      />
                    </TouchableOpacity>
                  }
                />
              </YStack>

              <TouchableOpacity
                onPress={onLogin}
                activeOpacity={0.8}
                disabled={isLoading}
              >
                <LinearGradient
                  colors={[colors.primary, colors.primaryDim]}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 0 }}
                  style={{
                    height: 56,
                    borderRadius: 28,
                    alignItems: "center",
                    justifyContent: "center",
                    shadowColor: colors.primary,
                    shadowOffset: { width: 0, height: 8 },
                    shadowOpacity: 0.3,
                    shadowRadius: 16,
                    elevation: 8,
                    opacity: isLoading ? 0.8 : 1,
                  }}
                >
                  {isLoading ? (
                    <Ionicons name="sync" size={20} color="#FFF" />
                  ) : (
                    <Text
                      color="#FFF"
                      fontWeight="700"
                      fontSize={17}
                      letterSpacing={0.5}
                    >
                      Sign In
                    </Text>
                  )}
                </LinearGradient>
              </TouchableOpacity>

              {error ? (
                <Text
                  color="#F44336"
                  fontSize={13}
                  textAlign="center"
                  marginTop={-4}
                >
                  {error}
                </Text>
              ) : (
                <Text
                  color={colors.textTertiary}
                  fontSize={13}
                  textAlign="center"
                  marginTop={-4}
                >
                  Sign in with your school email and password
                </Text>
              )}
            </YStack>
          </SafeAreaView>
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  );
}

/* ── Smart Tab Bar (floating, matches main glass bar style) ── */
function SmartTabBar({
  activeTab,
  setActiveTab,
  colors,
  isDark,
  insets,
}: {
  activeTab: string;
  setActiveTab: (tab: string) => void;
  colors: any;
  isDark: boolean;
  insets: any;
}) {
  const barWidth = useSharedValue(0);
  const indicatorLeft = useSharedValue(0);

  const totalTabs = TABS.length;
  const TAB_BAR_HEIGHT = 64;
  const INDICATOR_W = 48;
  const INDICATOR_H = 36;

  useEffect(() => {
    const activeIndex = TABS.findIndex((t) => t.key === activeTab);
    if (activeIndex >= 0 && barWidth.value > 0) {
      const slotWidth = barWidth.value / totalTabs;
      const left = activeIndex * slotWidth + (slotWidth - INDICATOR_W) / 2;
      indicatorLeft.value = withSpring(left, {
        damping: 20,
        stiffness: 180,
        mass: 0.8,
      });
    }
  }, [activeTab]);

  const indicatorStyle = useAnimatedStyle(() => ({
    left: indicatorLeft.value,
  }));

  const onLayout = useCallback((e: any) => {
    const w = e.nativeEvent.layout.width;
    if (w > 0) {
      barWidth.value = w;
      const activeIndex = TABS.findIndex((t) => t.key === activeTab);
      if (activeIndex >= 0) {
        const slotWidth = w / totalTabs;
        const left = activeIndex * slotWidth + (slotWidth - INDICATOR_W) / 2;
        indicatorLeft.value = withSpring(left, {
          damping: 20,
          stiffness: 180,
          mass: 0.8,
        });
      }
    }
  }, [activeTab]);

  return (
    <SafeAreaView edges={["bottom"]} style={styles.smartTabWrapper}>
      <View style={styles.smartBarContainer} onLayout={onLayout}>
        <LinearGradient
          colors={[
            colors.glassGradientStart,
            colors.glassGradientMid,
            colors.glassGradientEnd,
          ]}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={styles.smartGlassBorder}
        >
          <BlurView
            intensity={55}
            tint="dark"
            style={StyleSheet.absoluteFill}
          />
          <View style={styles.smartSlotRow}>
            {TABS.map((tab) => {
              const isFocused = activeTab === tab.key;
              return (
                <TouchableOpacity
                  key={tab.key}
                  onPress={() => setActiveTab(tab.key)}
                  activeOpacity={0.6}
                  style={styles.smartSlotTouchable}
                >
                  <Ionicons
                    name={
                      isFocused ? (tab.icon as any) : (tab.iconOutline as any)
                    }
                    size={22}
                    color={isFocused ? colors.primary : colors.textSecondary}
                    style={{ opacity: isFocused ? 1 : 0.65 }}
                  />
                </TouchableOpacity>
              );
            })}
          </View>
        </LinearGradient>

        <Animated.View
          style={[
            styles.smartIndicator,
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
              colors.indicatorGradientStart,
              colors.indicatorGradientMid,
              colors.indicatorGradientEnd,
            ]}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={StyleSheet.absoluteFill}
          />
        </Animated.View>
      </View>
    </SafeAreaView>
  );
}

/* ── Glass Input ── */
function GlassInput({
  colors,
  mode,
  icon,
  placeholder,
  value,
  onChangeText,
  secureTextEntry,
  keyboardType,
  autoCapitalize,
  rightIcon,
}: {
  colors: any;
  mode: string;
  icon: string;
  placeholder: string;
  value: string;
  onChangeText: (text: string) => void;
  secureTextEntry?: boolean;
  keyboardType?: any;
  autoCapitalize?: any;
  rightIcon?: React.ReactNode;
}) {
  return (
    <View style={styles.inputWrapper}>
      <LinearGradient
        colors={[colors.glassGradientStart, colors.glassGradientEnd]}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.inputGradient}
      >
        <BlurView
          intensity={20}
          tint={mode === "light" ? "light" : "dark"}
          style={styles.inputBlur}
        >
          <Ionicons
            name={icon as any}
            size={20}
            color={colors.primary}
            style={{ marginRight: 12 }}
          />
          <TextInput
            style={[styles.textInput, { color: colors.text }]}
            placeholder={placeholder}
            placeholderTextColor={colors.textTertiary}
            value={value}
            onChangeText={onChangeText}
            secureTextEntry={secureTextEntry}
            keyboardType={keyboardType}
            autoCapitalize={autoCapitalize}
          />
          {rightIcon}
        </BlurView>
      </LinearGradient>
    </View>
  );
}

const styles = StyleSheet.create({
  scrollContent: {
    flexGrow: 1,
    justifyContent: "center",
    paddingHorizontal: 24,
  },
  inputWrapper: {
    borderRadius: 28,
    overflow: "hidden",
    borderWidth: 1,
    borderColor: "rgba(128,128,128,0.2)",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 5,
  },
  inputGradient: {
    borderRadius: 28,
  },
  inputBlur: {
    flexDirection: "row",
    alignItems: "center",
    height: 56,
    paddingHorizontal: 20,
    borderRadius: 28,
    overflow: "hidden",
  },
  textInput: {
    flex: 1,
    fontSize: 16,
    fontWeight: "500",
  },
  logoutContainer: {
    position: "absolute",
  },
  logoutButton: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: "rgba(255,140,0,0.2)",
  },
  smartTabWrapper: {
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    alignItems: "center",
    zIndex: 9998,
  },
  smartBarContainer: {
    width: "78%",
    maxWidth: 420,
    height: 64,
    marginBottom: Platform.OS === "ios" ? 12 : 20,
    position: "relative",
  },
  smartGlassBorder: {
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
  smartSlotRow: {
    flexDirection: "row",
    height: "100%",
    alignItems: "center",
    paddingHorizontal: 1.5,
  },
  smartSlotTouchable: {
    flex: 1,
    height: "100%",
    alignItems: "center",
    justifyContent: "center",
  },
  smartIndicator: {
    position: "absolute",
    top: (64 - 36) / 2,
    borderRadius: 18,
    overflow: "hidden",
    shadowColor: "#FF8C00",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 14,
    elevation: 12,
  },
});
