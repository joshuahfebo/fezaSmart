// src/app/explore.tsx
import React from 'react'
import { Dimensions, StyleSheet, View, ScrollView, TouchableOpacity } from 'react-native'
import { BlurView } from 'expo-blur'
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context'
import { YStack, XStack, Text } from 'tamagui'
import { LinearGradient } from 'expo-linear-gradient'
import { Ionicons } from '@expo/vector-icons'
import Animated, {
  useSharedValue,
  useAnimatedScrollHandler,
  useAnimatedStyle,
  interpolate,
  Extrapolate,
} from 'react-native-reanimated'

const { width } = Dimensions.get('window')
const CARD_WIDTH = width - 32

// Mock news data
const newsItems = [
  {
    id: '1',
    title: 'Feza Boys Wins National Science Fair',
    category: 'Achievement',
    date: '23 Apr 2026',
    summary: 'Students from Feza Boys\' Secondary brought home the gold medal at the Tanzania National Science and Engineering Fair, impressing judges with their innovative water purification system.',
    image: require('../../assets/images/fezaschools/fezaBoys.webp'),
  },
  {
    id: '2',
    title: 'New IB Diploma Programme Launched',
    category: 'Academics',
    date: '20 Apr 2026',
    summary: 'Feza International School is now an authorised IB World School, offering the prestigious International Baccalaureate Diploma Programme starting next academic year.',
    image: require('../../assets/images/fezaschools/fis.webp'),
  },
  {
    id: '3',
    title: 'Campus Expansion in Dodoma',
    category: 'Development',
    date: '15 Apr 2026',
    summary: 'Construction of a new state-of-the-art library and sports complex has begun at Feza Dodoma, set to be completed by December 2026.',
    image: require('../../assets/images/fezaschools/fezaDodoma.webp'),
  },
  {
    id: '4',
    title: 'Community Outreach Programme',
    category: 'Community',
    date: '10 Apr 2026',
    summary: 'Feza Primary students visited local villages to distribute educational materials and conduct reading sessions for underprivileged children.',
    image: require('../../assets/images/fezaschools/fezaPrimary.webp'),
  },
  {
    id: '5',
    title: 'Top Results in National Exams',
    category: 'Academics',
    date: '5 Apr 2026',
    summary: 'Feza Girls\' Secondary records outstanding performance in the NECTA Form Four examinations, with 98% of students achieving Division One.',
    image: require('../../assets/images/fezaschools/fezaGirls.webp'),
  },
]

export default function ExploreScreen() {
  const insets = useSafeAreaInsets()
  const scrollY = useSharedValue(0)

  const onScroll = useAnimatedScrollHandler({
    onScroll: (event) => {
      scrollY.value = event.contentOffset.y
    },
  })

  const bottomPadding = insets.bottom + 100

  return (
    <View style={{ flex: 1, backgroundColor: '#0A0A0A' }}>
      {/* Subtle background */}
      <View style={StyleSheet.absoluteFill}>
        <BlurView intensity={40} tint="dark" style={StyleSheet.absoluteFill} />
        <View style={[StyleSheet.absoluteFill, { backgroundColor: 'rgba(10,10,10,0.4)' }]} />
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
            {/* Header */}
            <XStack alignItems="flex-start" gap="$3" marginTop="$4">
              <YStack
                width={4}
                height={42}
                borderRadius={2}
                backgroundColor="#FF8C00"
                marginTop={4}
              />
              <YStack>
                <Text
                  fontSize={40}
                  fontWeight="900"
                  letterSpacing={-1.2}
                  color="#FFFFFF"
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
                  color="#FF8C00"
                  lineHeight={42}
                  marginTop={-8}
                  textShadowColor="rgba(255,140,0,0.3)"
                  textShadowOffset={{ width: 0, height: 2 }}
                  textShadowRadius={8}
                >
                  Updates
                </Text>
                <Text
                  color="#999"
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

            {/* Latest Updates Tagline */}
            <Text
              color="#FF8C00"
              fontWeight="800"
              fontSize={24}
              letterSpacing={-0.5}
            >
              What’s new across our campuses
            </Text>

            {/* News Cards with Reveal Animation */}
            {newsItems.map((item, index) => (
              <RevealSection key={item.id} index={index} scrollY={scrollY}>
                <NewsCard item={item} />
              </RevealSection>
            ))}
          </YStack>
        </SafeAreaView>
      </Animated.ScrollView>
    </View>
  )
}

/* ── News Card ── */
function NewsCard({ item }: { item: typeof newsItems[0] }) {
  return (
    <TouchableOpacity activeOpacity={0.8}>
      <YStack
        borderRadius={24}
        overflow="hidden"
        backgroundColor="rgba(24,24,26,0.6)"
        borderWidth={1}
        borderColor="rgba(255,255,255,0.08)"
        shadowColor="#000"
        shadowOffset={{ width: 0, height: 8 }}
        shadowOpacity={0.3}
        shadowRadius={16}
        elevation={8}
      >
        {/* Image with gradient overlay */}
        <View>
          <Animated.Image
            source={item.image}
            resizeMode="cover"
            style={{ width: '100%', height: 160 }}
          />
          <LinearGradient
            colors={['transparent', 'rgba(0,0,0,0.7)']}
            style={{ position: 'absolute', bottom: 0, left: 0, right: 0, height: 80 }}
          />
          {/* Category badge */}
          <View
            style={{
              position: 'absolute',
              top: 12,
              left: 12,
              backgroundColor: 'rgba(255,140,0,0.85)',
              borderRadius: 8,
              paddingHorizontal: 10,
              paddingVertical: 4,
            }}
          >
            <Text color="#FFF" fontSize={11} fontWeight="700" letterSpacing={0.5}>
              {item.category.toUpperCase()}
            </Text>
          </View>
        </View>

        {/* Text content */}
        <YStack padding="$3" gap="$2">
          <Text color="#FFFFFF" fontSize={18} fontWeight="700" letterSpacing={-0.2} lineHeight={24}>
            {item.title}
          </Text>
          <XStack alignItems="center" gap="$2">
            <Ionicons name="calendar-outline" size={13} color="#FF8C00" />
            <Text color="#AAA" fontSize={12} fontWeight="500">
              {item.date}
            </Text>
          </XStack>
          <Text color="#CCC" fontSize={14} lineHeight={20} opacity={0.9}>
            {item.summary}
          </Text>
        </YStack>
      </YStack>
    </TouchableOpacity>
  )
}

/* ── Reveal Animation Wrapper (reused from your home screen) ── */
function RevealSection({
  index,
  scrollY,
  children,
}: {
  index: number
  scrollY: any
  children: React.ReactNode
}) {
  const start = index * 240 + 150
  const end = start + 200

  const animatedStyle = useAnimatedStyle(() => {
    const opacity = interpolate(scrollY.value, [start, end], [0.4, 1], Extrapolate.CLAMP)
    const translateY = interpolate(scrollY.value, [start, end], [40, 0], Extrapolate.CLAMP)
    return { opacity, transform: [{ translateY }] }
  })

  return (
    <Animated.View style={animatedStyle}>
      {children}
    </Animated.View>
  )
}