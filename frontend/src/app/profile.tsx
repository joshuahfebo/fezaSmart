import { useTheme, useThemeMode, useToggleTheme } from "@/hooks/use-theme";
import { Ionicons } from "@react-native-vector-icons/ionicons";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import {
  Dimensions,
  Image,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  View,
} from "react-native";
import {
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { Avatar, Text, XStack, YStack } from "tamagui";

const { width: screenWidth, height } = Dimensions.get("window");

export default function ProfileScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const toggleTheme = useToggleTheme();
  const insets = useSafeAreaInsets();
  const bottomPadding = insets.bottom + 80;
  const logoSize = React.useMemo(() => {
    const maxWidth = screenWidth * 0.4;
    const aspect = 180 / 100;
    return { width: maxWidth, height: maxWidth / aspect };
  }, [screenWidth]);
  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <View style={StyleSheet.absoluteFill}>
        <Image
          source={require("../../assets/images/fezaschools/fezaBoys.webp")}
          blurRadius={80}
          style={[
            StyleSheet.absoluteFill,
            { width: "100%", height: "100%", opacity: 0.15 },
          ]}
          resizeMode="cover"
        />
        <BlurView
          intensity={60}
          tint={mode === "light" ? "light" : "dark"}
          style={StyleSheet.absoluteFill}
        />
        <View
          style={[StyleSheet.absoluteFill, { backgroundColor: colors.overlay }]}
        />
      </View>

      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: bottomPadding }}
        showsVerticalScrollIndicator={false}
      >
        <SafeAreaView style={{ flex: 1 }}>
          <YStack padding="$4" gap="$5">
            <XStack alignItems="flex-start" gap="$3">
              <View>
                <Image
                  source={
                    mode === "dark"
                      ? require("../../assets/images/logoIcons/logoLight.webp")
                      : require("../../assets/images/logoIcons/logoDark.png")
                  }
                  style={{ width: logoSize.width, height: logoSize.height }}
                  resizeMode="contain"
                />
              </View>

              <YStack>
                <YStack
                  width={6}
                  height={36}
                  borderTopLeftRadius={3}
                  borderTopRightRadius={3}
                  borderBottomLeftRadius={0}
                  borderBottomRightRadius={0}
                  backgroundColor={colors.primary}
                />
                <YStack
                  width={6}
                  height={36}
                  borderTopLeftRadius={0}
                  borderTopRightRadius={0}
                  borderBottomLeftRadius={3}
                  borderBottomRightRadius={3}
                  backgroundColor={colors.text}
                />
              </YStack>
              <YStack>
                <Text
                  fontSize={38}
                  fontWeight="900"
                  letterSpacing={-1}
                  color={colors.text}
                  lineHeight={42}
                >
                  Profile
                </Text>
                <Text
                  color={colors.textTertiary}
                  fontSize={13}
                  fontStyle="italic"
                  fontWeight="500"
                  marginTop={4}
                  letterSpacing={0.3}
                >
                  Your Feza identity
                </Text>
              </YStack>
            </XStack>

            <YStack
              borderRadius={30}
              borderWidth={1}
              borderColor={colors.cardBorder}
              backgroundColor={colors.cardBackground}
              overflow="hidden"
              shadowColor={colors.shadow}
              shadowOffset={{ width: 0, height: 10 }}
              shadowOpacity={1}
              shadowRadius={20}
              elevation={12}
            >
              <LinearGradient
                colors={
                  mode === "dark"
                    ? ["transparent", "#FF6600", colors.primary]
                    : [colors.primary, "#FF6600", colors.text]
                }
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={{
                  padding: 24,
                  alignItems: "center",
                }}
              >
                <Avatar
                  circular
                  size={90}
                  borderWidth={0}
                  borderColor={"transparent"}
                  marginBottom="$3"
                >
                  <Avatar.Image
                    source={
                      mode === "dark"
                        ? require("../../assets/images/logoIcons/profile.jpg")
                        : require("../../assets/images/logoIcons/profile.jpg")
                    }
                    backgroundColor={colors.background}
                  />
                </Avatar>
                <Text
                  style={{
                    fontSize: 24,
                    marginBottom: 1,
                  }}
                  color={colors.text}
                  fontWeight="700"
                >
                  Joshuah Kyando
                </Text>
                <Text
                  color={colors.text}
                  fontSize={14}
                  fontWeight={800}
                  opacity={0.75}
                >
                  Private User
                </Text>
              </LinearGradient>
            </YStack>

            <XStack gap="$4" justifyContent="space-around">
              <StatCard label="Attendance" value="98%" colors={colors} />
              <StatCard label="Grades" value="A" colors={colors} />
              <StatCard label="Activities" value="5" colors={colors} />
            </XStack>

            <YStack
              borderRadius={24}
              overflow="hidden"
              borderWidth={1}
              borderColor={colors.cardBorder}
              backgroundColor={colors.cardBackground}
              shadowColor={colors.shadow}
              shadowOffset={{ width: 0, height: 8 }}
              shadowOpacity={1}
              shadowRadius={16}
              elevation={8}
            >
              <MenuItem
                icon="person-circle-outline"
                label="Personal Details"
                colors={colors}
              />
              <MenuItem
                icon="calendar-outline"
                label="Timetable"
                colors={colors}
              />
              <MenuItem
                icon="ribbon-outline"
                label="Achievements"
                colors={colors}
              />
              <ThemeToggleRow
                mode={mode}
                onToggle={toggleTheme}
                colors={colors}
              />
              <MenuItem
                icon="settings-outline"
                label="Settings"
                colors={colors}
              />
              <MenuItem
                icon="log-out-outline"
                label="Sign Out"
                colors={colors}
                danger
              />
            </YStack>
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

function StatCard({
  label,
  value,
  colors,
}: {
  label: string;
  value: string;
  colors: any;
}) {
  return (
    <YStack
      flex={1}
      borderRadius={20}
      backgroundColor={colors.cardBackground}
      borderWidth={1}
      borderColor={colors.cardBorder}
      padding="$3"
      alignItems="center"
      justifyContent="center"
      shadowColor={colors.shadow}
      shadowOffset={{ width: 0, height: 4 }}
      shadowOpacity={1}
      shadowRadius={8}
      elevation={4}
    >
      <Text color={colors.primary} fontSize={24} fontWeight="800">
        {value}
      </Text>
      <Text
        color={colors.textSecondary}
        fontSize={13}
        marginTop="$1"
        opacity={0.8}
      >
        {label}
      </Text>
    </YStack>
  );
}

function ThemeToggleRow({
  mode,
  onToggle,
  colors,
}: {
  mode: string;
  onToggle: () => void;
  colors: any;
}) {
  return (
    <TouchableOpacity onPress={onToggle} activeOpacity={0.7}>
      <XStack
        padding="$4"
        alignItems="center"
        borderBottomWidth={0.5}
        borderBottomColor={colors.border}
      >
        <Ionicons
          name={mode === "dark" ? "moon-outline" : "sunny-outline"}
          size={22}
          color={colors.primary}
          style={{ marginRight: 14 }}
        />
        <Text color={colors.text} fontSize={16} fontWeight="500" flex={1}>
          {mode === "dark" ? "Dark Mode" : "Light Mode"}
        </Text>
        <Ionicons
          name={mode === "dark" ? "toggle" : "toggle-outline"}
          size={24}
          color={mode === "dark" ? colors.primary : colors.textTertiary}
        />
      </XStack>
    </TouchableOpacity>
  );
}

function MenuItem({
  icon,
  label,
  colors,
  danger = false,
}: {
  icon: string;
  label: string;
  colors: any;
  danger?: boolean;
}) {
  return (
    <XStack
      padding="$4"
      alignItems="center"
      borderBottomWidth={0.5}
      borderBottomColor={colors.border}
    >
      <Ionicons
        name={icon as any}
        size={22}
        color={danger ? colors.danger : colors.primary}
        style={{ marginRight: 14 }}
      />
      <Text
        color={danger ? colors.danger : colors.text}
        fontSize={16}
        fontWeight="500"
        flex={1}
      >
        {label}
      </Text>
      <Ionicons name="chevron-forward" size={18} color={colors.textTertiary} />
    </XStack>
  );
}
