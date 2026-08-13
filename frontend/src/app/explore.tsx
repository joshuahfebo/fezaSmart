import { useTheme, useThemeMode } from "@/hooks/use-theme";
import { Ionicons } from "@react-native-vector-icons/ionicons";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import React, { useMemo } from "react";
import {
  Dimensions,
  Image,
  StyleSheet,
  TouchableOpacity,
  View,
} from "react-native";
import Animated, {
  Extrapolate,
  interpolate,
  useAnimatedScrollHandler,
  useAnimatedStyle,
  useSharedValue,
} from "react-native-reanimated";
import {
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { Text, XStack, YStack } from "tamagui";
const { width: screenWidth, height } = Dimensions.get("window");
const { width } = Dimensions.get("window");
const CARD_WIDTH = width - 32;

const newsItems = [
  {
    id: "1",
    title: "Feza Boys Wins National Science Fair",
    category: "Achievement",
    date: "23 Apr 2026",
    summary:
      "Students from Feza Boys' Secondary brought home the gold medal at the Tanzania National Science and Engineering Fair, impressing judges with their innovative water purification system.",
    image: require("../../assets/images/fezaschools/fezaBoys.webp"),
  },
  {
    id: "2",
    title: "New IB Diploma Programme Launched",
    category: "Academics",
    date: "20 Apr 2026",
    summary:
      "Feza International School is now an authorised IB World School, offering the prestigious International Baccalaureate Diploma Programme starting next academic year.",
    image: require("../../assets/images/fezaschools/fis.webp"),
  },
  {
    id: "3",
    title: "Campus Expansion in Dodoma",
    category: "Development",
    date: "15 Apr 2026",
    summary:
      "Construction of a new state-of-the-art library and sports complex has begun at Feza Dodoma, set to be completed by December 2026.",
    image: require("../../assets/images/fezaschools/fezaDodoma.webp"),
  },
  {
    id: "4",
    title: "Community Outreach Programme",
    category: "Community",
    date: "10 Apr 2026",
    summary:
      "Feza Primary students visited local villages to distribute educational materials and conduct reading sessions for underprivileged children.",
    image: require("../../assets/images/fezaschools/fezaPrimary.webp"),
  },
  {
    id: "5",
    title: "Top Results in National Exams",
    category: "Academics",
    date: "5 Apr 2026",
    summary:
      "Feza Girls' Secondary records outstanding performance in the NECTA Form Four examinations, with 98% of students achieving Division One.",
    image: require("../../assets/images/fezaschools/fezaGirls.webp"),
  },
];

export default function ExploreScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const insets = useSafeAreaInsets();
  const scrollY = useSharedValue(0);

  const onScroll = useAnimatedScrollHandler({
    onScroll: (event) => {
      scrollY.value = event.contentOffset.y;
    },
  });

  const bottomPadding = insets.bottom + 100;

  const logoSize = useMemo(() => {
    const maxWidth = screenWidth * 0.4;
    const aspect = 180 / 100;
    return { width: maxWidth, height: maxWidth / aspect };
  }, [screenWidth]);
  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <View style={StyleSheet.absoluteFill}>
        <BlurView
          intensity={40}
          tint={mode === "light" ? "light" : "dark"}
          style={StyleSheet.absoluteFill}
        />
        <View
          style={[StyleSheet.absoluteFill, { backgroundColor: colors.overlay }]}
        />
      </View>

      <Animated.ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: bottomPadding }}
        showsVerticalScrollIndicator={false}
        onScroll={onScroll}
        scrollEventThrottle={16}
      >
        <SafeAreaView>
          <YStack padding="$4" gap="$5">
            <XStack alignItems="flex-start" gap="$3" marginTop="$4">
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
              <YStack gap={0} marginTop={4}>
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
                  fontSize={40}
                  fontWeight="900"
                  letterSpacing={-1.2}
                  color={colors.text}
                  lineHeight={44}
                  textShadowColor="rgba(0,0,0,0.6)"
                  textShadowOffset={{ width: 0, height: 2 }}
                  textShadowRadius={8}
                >
                  News
                </Text>
                <Text
                  fontSize={38}
                  fontWeight="900"
                  letterSpacing={-1}
                  color={colors.primary}
                  lineHeight={42}
                  marginTop={-8}
                  textShadowColor="rgba(255,140,0,0.3)"
                  textShadowOffset={{ width: 0, height: 2 }}
                  textShadowRadius={8}
                >
                  Updates
                </Text>
                <Text
                  color={colors.textTertiary}
                  fontSize={14}
                  fontStyle="italic"
                  fontWeight="500"
                  marginTop={4}
                  letterSpacing={0.3}
                >
                  Stay informed
                </Text>
              </YStack>
            </XStack>

            <Text
              color={colors.primary}
              fontWeight="800"
              fontSize={24}
              letterSpacing={-0.5}
            >
              Latest feed
            </Text>

            {newsItems.map((item, index) => (
              <RevealSection key={item.id} index={index} scrollY={scrollY}>
                <NewsCard item={item} colors={colors} />
              </RevealSection>
            ))}
          </YStack>
        </SafeAreaView>
      </Animated.ScrollView>
    </View>
  );
}

function NewsCard({
  item,
  colors,
}: {
  item: (typeof newsItems)[0];
  colors: any;
}) {
  return (
    <TouchableOpacity activeOpacity={0.8}>
      <YStack
        borderRadius={24}
        overflow="hidden"
        backgroundColor={colors.cardBackground}
        borderWidth={1}
        borderColor={colors.cardBorder}
        shadowColor={colors.shadow}
        shadowOffset={{ width: 0, height: 8 }}
        shadowOpacity={1}
        shadowRadius={16}
        elevation={8}
      >
        <View>
          <Animated.Image
            source={item.image}
            resizeMode="cover"
            style={{ width: "100%", height: 160 }}
          />
          <LinearGradient
            colors={["transparent", "rgba(0,0,0,0.7)"]}
            style={{
              position: "absolute",
              bottom: 0,
              left: 0,
              right: 0,
              height: 80,
            }}
          />
          <View
            style={{
              position: "absolute",
              top: 12,
              left: 12,
              backgroundColor: colors.orangeBgStrong,
              borderRadius: 8,
              paddingHorizontal: 10,
              paddingVertical: 4,
            }}
          >
            <Text
              color="#FFF"
              fontSize={11}
              fontWeight="700"
              letterSpacing={0.5}
            >
              {item.category.toUpperCase()}
            </Text>
          </View>
        </View>

        <YStack padding="$3" gap="$2">
          <Text
            color={colors.text}
            fontSize={18}
            fontWeight="700"
            letterSpacing={-0.2}
            lineHeight={24}
          >
            {item.title}
          </Text>
          <XStack alignItems="center" gap="$2">
            <Ionicons
              name="calendar-outline"
              size={13}
              color={colors.primary}
            />
            <Text color={colors.textSecondary} fontSize={12} fontWeight="500">
              {item.date}
            </Text>
          </XStack>
          <Text
            color={colors.textSecondary}
            fontSize={14}
            lineHeight={20}
            opacity={0.9}
          >
            {item.summary}
          </Text>
        </YStack>
      </YStack>
    </TouchableOpacity>
  );
}

function RevealSection({
  index,
  scrollY,
  children,
}: {
  index: number;
  scrollY: any;
  children: React.ReactNode;
}) {
  const start = index * 240 + 150;
  const end = start + 200;

  const animatedStyle = useAnimatedStyle(() => {
    const opacity = interpolate(
      scrollY.value,
      [start, end],
      [0.8, 1],
      Extrapolate.CLAMP,
    );
    const translateY = interpolate(
      scrollY.value,
      [start, end],
      [40, 0],
      Extrapolate.CLAMP,
    );
    return { opacity, transform: [{ translateY }] };
  });

  return <Animated.View style={animatedStyle}>{children}</Animated.View>;
}
