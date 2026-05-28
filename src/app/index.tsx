// src/app/index.tsx
import { useEffect, useRef, useMemo, useState } from 'react'
import {
  Dimensions,
  StyleSheet,
  View,
  TouchableOpacity,
  Image,
} from 'react-native'
import Animated, {
  useSharedValue,
  useAnimatedScrollHandler,
  useAnimatedStyle,
  interpolate,
  Extrapolate,
  withTiming,
  cancelAnimation,
  useAnimatedReaction,
  runOnJS,
  Easing,
} from 'react-native-reanimated'
import { BlurView } from 'expo-blur'
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context'
import { YStack, XStack, Text } from 'tamagui'
import { LinearGradient } from 'expo-linear-gradient'
import { Ionicons } from '@expo/vector-icons'
import { router } from 'expo-router'

const { width: screenWidth, height } = Dimensions.get('window')

// Carousel constants
const ITEM_WIDTH = screenWidth * 0.62
const SPACING = 10
const SIDE_SPACING = (screenWidth - ITEM_WIDTH) / 2
const ASPECT_RATIO = 4 / 3
const IMAGE_HEIGHT = ITEM_WIDTH / ASPECT_RATIO
const CAROUSEL_HEIGHT = IMAGE_HEIGHT + 60

const schools = [
  { name: "Feza Boys' Secondary & High School", short: "Boys'", image: require('../../assets/images/fezaschools/fezaBoys.webp') },
  { name: "Feza Girls' Secondary & High School", short: "Girls'", image: require('../../assets/images/fezaschools/fezaGirls.webp') },
  { name: 'Feza Zanzibar', short: 'Zanzibar', image: require('../../assets/images/fezaschools/fezaZanzibar.webp') },
  { name: 'Feza Primary School', short: 'Primary', image: require('../../assets/images/fezaschools/fezaPrimary.webp') },
  { name: 'Feza Dodoma', short: 'Dodoma', image: require('../../assets/images/fezaschools/fezaDodoma.webp') },
  { name: 'Feza Nursery School', short: 'Nursery', image: require('../../assets/images/fezaschools/fezaNurserynDayCare.webp') },
  { name: 'Feza International School', short: 'International', image: require('../../assets/images/fezaschools/fis.webp') },
]

const stats = [
  { label: 'Campuses', value: '7', icon: 'business-outline' },
  { label: 'Students', value: '5,000+', icon: 'people-outline' },
  { label: 'Acceptance', value: '100%', icon: 'trophy-outline' },
]

const features = [
  { title: 'Global Curriculum', desc: 'Cambridge & IB certified programmes', icon: 'globe-outline' },
  { title: 'Leadership', desc: "Developing tomorrow's innovative leaders", icon: 'rocket-outline' },
  { title: 'Community', desc: 'Inclusive, multicultural campus life', icon: 'heart-outline' },
]

export default function HomeScreen() {
  const carouselRef = useRef<any>(null)
  const scrollX = useSharedValue(0)
  const scrollY = useSharedValue(0)
  const carouselIndex = useSharedValue(0)
  const insets = useSafeAreaInsets()

  // Single layer approach – far cleaner
  const [displayedShort, setDisplayedShort] = useState(schools[0].short)

  const textOpacity = useSharedValue(1)
  const textTranslateY = useSharedValue(0)

  // When carouselIndex changes, perform a smooth fade-out → update → fade-in
  useAnimatedReaction(
    () => carouselIndex.value,
    (currentIdx) => {
      const newName = schools[currentIdx].short

      cancelAnimation(textOpacity)
      cancelAnimation(textTranslateY)

      // Fade out
      textOpacity.value = withTiming(0, { duration: 160, easing: Easing.out(Easing.cubic) }, (finished) => {
        if (finished) {
          // Update text while invisible
          runOnJS(setDisplayedShort)(newName)
          // Fade back in with a gentle lift
          textTranslateY.value = 8
          textOpacity.value = withTiming(1, { duration: 220, easing: Easing.out(Easing.cubic) })
          textTranslateY.value = withTiming(0, { duration: 220, easing: Easing.out(Easing.cubic) })
        }
      })
    }
  )

  const textAnimStyle = useAnimatedStyle(() => ({
    opacity: textOpacity.value,
    transform: [{ translateY: textTranslateY.value }],
  }))

  useEffect(() => {
    const interval = setInterval(() => {
      const current = carouselIndex.value
      const next = (current + 1) % schools.length
      carouselRef.current?.scrollTo({
        x: next * (ITEM_WIDTH + SPACING * 2),
        animated: true,
      })
      carouselIndex.value = next
    }, 4000)
    return () => clearInterval(interval)
  }, [])

  const onHorizontalScroll = useAnimatedScrollHandler({
    onScroll: (e) => {
      scrollX.value = e.contentOffset.x
      const i = Math.round(e.contentOffset.x / (ITEM_WIDTH + SPACING * 2))
      carouselIndex.value = i
    },
  })

  const onVerticalScroll = useAnimatedScrollHandler({
    onScroll: (e) => {
      scrollY.value = e.contentOffset.y
    },
  })

  const bottomPadding = insets.bottom + 120

  const logoSize = useMemo(() => {
    const maxWidth = screenWidth * 0.4
    const aspect = 180 / 100
    return { width: maxWidth, height: maxWidth / aspect }
  }, [screenWidth])

  return (
    <View style={{ flex: 1, backgroundColor: '#0A0A0A' }}>
      <View style={StyleSheet.absoluteFill}>
        {schools.map((item, i) => {
          const bgStyle = useAnimatedStyle(() => {
            const opacity = interpolate(
              scrollX.value,
              [(i - 1) * (ITEM_WIDTH + SPACING * 2), i * (ITEM_WIDTH + SPACING * 2), (i + 1) * (ITEM_WIDTH + SPACING * 2)],
              [0, 0.35, 0],
              Extrapolate.CLAMP,
            )
            return { opacity }
          })
          return (
            <Animated.Image
              key={i}
              source={item.image}
              blurRadius={80}
              style={[StyleSheet.absoluteFillObject, { width: screenWidth, height }, bgStyle]}
              resizeMode="cover"
            />
          )
        })}
        <BlurView intensity={50} tint="dark" style={StyleSheet.absoluteFill} />
        <View style={[StyleSheet.absoluteFill, { backgroundColor: 'rgba(10,10,10,0.25)' }]} />
      </View>

      <Animated.ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: bottomPadding }}
        showsVerticalScrollIndicator={false}
        onScroll={onVerticalScroll}
        scrollEventThrottle={16}
      >
        <SafeAreaView>
          <YStack padding="$4" gap="$6">
            {/* ── Header ── */}
            <XStack alignItems="flex-start" gap="$3" marginTop="$4">
              <View style={styles.logoBox}>
                <Image
                  source={require('../../assets/images/logoIcons/logoLight.webp')}
                  style={{ width: logoSize.width, height: logoSize.height }}
                  resizeMode="contain"
                />
              </View>

              {/* Accent bars – no gap */}
              <YStack gap={0} marginTop={4}>
                <YStack
                  width={6}
                  height={30}
                  borderTopLeftRadius={3}
                  borderTopRightRadius={3}
                  borderBottomLeftRadius={0}
                  borderBottomRightRadius={0}
                  backgroundColor="#FF8C00"
                />
                <YStack
                  width={6}
                  height={30}
                  borderTopLeftRadius={0}
                  borderTopRightRadius={0}
                  borderBottomLeftRadius={3}
                  borderBottomRightRadius={3}
                  backgroundColor="#FFFFFF"
                  opacity={0.7}
                />
              </YStack>

              <YStack flex={1}>
                <Text
                  fontSize={30}
                  fontWeight="900"
                  letterSpacing={-1.2}
                  color="#FFFFFF"
                  lineHeight={44}
                  textShadowColor="rgba(0,0,0,0.6)"
                  textShadowOffset={{ width: 0, height: 2 }}
                  textShadowRadius={8}
                >
                  Feza
                </Text>

                {/* Single animated name */}
                <View style={{ height: 40, marginTop: -6, overflow: 'hidden' }}>
                  <Animated.View style={[{ position: 'absolute', left: 0, right: 0 }, textAnimStyle]}>
                    <Text
                      fontSize={30}
                      fontWeight="800"
                      letterSpacing={-0.5}
                      color="#FF8C00"
                      lineHeight={34}
                      textShadowColor="rgba(255,140,0,0.3)"
                      textShadowOffset={{ width: 0, height: 2 }}
                      textShadowRadius={8}
                    >
                      {displayedShort}
                    </Text>
                  </Animated.View>
                </View>

                <Text color="#999" fontSize={14} fontStyle="italic" fontWeight="500" marginTop={4} letterSpacing={0.3}>
                  Be better educated
                </Text>
              </YStack>
            </XStack>

            {/* Hero tagline */}
            <YStack gap="$2">
              <Text
                color="#FF8C00"
                fontWeight="800"
                fontSize={28}
                letterSpacing={-0.7}
                lineHeight={34}
                textShadowColor="rgba(255,140,0,0.2)"
                textShadowOffset={{ width: 0, height: 1 }}
                textShadowRadius={6}
              >
                Excellence{'\n'}in Education
              </Text>
              <Text color="#EEE" fontSize={15} lineHeight={22} opacity={0.9} maxWidth="85%">
                Shaping future leaders through innovation, discipline, and global academic excellence.
              </Text>
            </YStack>

            {/* Carousel */}
            <YStack height={CAROUSEL_HEIGHT} justifyContent="center">
              <Animated.ScrollView
                ref={carouselRef}
                horizontal
                showsHorizontalScrollIndicator={false}
                snapToInterval={ITEM_WIDTH + SPACING * 2}
                decelerationRate="fast"
                onScroll={onHorizontalScroll}
                scrollEventThrottle={16}
                contentContainerStyle={{ paddingHorizontal: SIDE_SPACING }}
              >
                {schools.map((item, i) => (
                  <ParallaxItem key={i} item={item} index={i} scrollX={scrollX} />
                ))}
              </Animated.ScrollView>
              <XStack justifyContent="center" gap="$2" marginTop="$3">
                {schools.map((_, i) => (
                  <AnimatedDot key={i} index={i} scrollX={scrollX} />
                ))}
              </XStack>
            </YStack>

            {/* Stats, Features, Testimonial, CTA unchanged */}
            <RevealSection index={0} scrollY={scrollY}>
              <XStack gap="$3" justifyContent="space-around" flexWrap="wrap">
                {stats.map((s, idx) => (
                  <YStack
                    key={idx}
                    flex={1}
                    minWidth={100}
                    backgroundColor="rgba(24,24,26,0.6)"
                    borderRadius={20}
                    borderWidth={1}
                    borderColor="rgba(255,255,255,0.08)"
                    padding="$3"
                    alignItems="center"
                    justifyContent="center"
                    shadowColor="#000"
                    shadowOffset={{ width: 0, height: 5 }}
                    shadowOpacity={0.2}
                    shadowRadius={10}
                  >
                    <Ionicons name={s.icon as any} size={22} color="#FF8C00" style={{ marginBottom: 8 }} />
                    <Text color="#FF8C00" fontSize={24} fontWeight="800">{s.value}</Text>
                    <Text color="#AAA" fontSize={13} marginTop="$1">{s.label}</Text>
                  </YStack>
                ))}
              </XStack>
            </RevealSection>

            <RevealSection index={1} scrollY={scrollY}>
              <YStack
                backgroundColor="rgba(24,24,26,0.5)"
                borderRadius={24}
                borderWidth={1}
                borderColor="rgba(255,255,255,0.06)"
                padding="$4"
                gap="$4"
                shadowColor="#000"
                shadowOffset={{ width: 0, height: 8 }}
                shadowOpacity={0.2}
                shadowRadius={16}
              >
                <Text color="#FFF" fontSize={20} fontWeight="700" letterSpacing={-0.3} marginBottom="$2">
                  Why Feza?
                </Text>
                {features.map((f, idx) => (
                  <XStack key={idx} gap="$3" alignItems="center">
                    <View style={{
                      width: 42, height: 42, borderRadius: 12,
                      backgroundColor: 'rgba(255,140,0,0.15)',
                      alignItems: 'center', justifyContent: 'center',
                    }}>
                      <Ionicons name={f.icon as any} size={20} color="#FF8C00" />
                    </View>
                    <YStack flex={1}>
                      <Text color="#FFF" fontSize={15} fontWeight="600">{f.title}</Text>
                      <Text color="#AAA" fontSize={13} opacity={0.8}>{f.desc}</Text>
                    </YStack>
                  </XStack>
                ))}
              </YStack>
            </RevealSection>

            <RevealSection index={2} scrollY={scrollY}>
              <YStack
                backgroundColor="rgba(255,140,0,0.08)"
                borderRadius={24}
                borderWidth={1}
                borderColor="rgba(255,140,0,0.2)"
                padding="$4"
                gap="$3"
              >
                <Ionicons name="accessibility" size={28} color="#FF8C00" />
                <Text fontStyle="italic" color="#EEE" fontSize={16} lineHeight={24} opacity={0.95}>
                  "Feza Schools didn't just teach my son – they shaped him into a confident, globally minded young leader."
                </Text>
                <XStack gap="$2" alignItems="center">
                  <View style={{ width: 4, height: 18, backgroundColor: '#FF8C00', borderRadius: 2 }} />
                  <Text color="#AAA" fontSize={13} fontWeight="500">
                    Parent of a Feza Boys' graduate
                  </Text>
                </XStack>
              </YStack>
            </RevealSection>

            <RevealSection index={3} scrollY={scrollY}>
              <TouchableOpacity activeOpacity={0.8} onPress={() => router.push('/explore')}>
                <LinearGradient
                  colors={['#FF8C00', '#FF6D00']}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 0 }}
                  style={{
                    height: 56,
                    borderRadius: 28,
                    alignItems: 'center',
                    justifyContent: 'center',
                    shadowColor: '#FF8C00',
                    shadowOffset: { width: 0, height: 6 },
                    shadowOpacity: 0.4,
                    shadowRadius: 12,
                  }}
                >
                  <Text color="#FFF" fontWeight="700" fontSize={17} letterSpacing={0.5}>
                    Explore All Campuses
                  </Text>
                </LinearGradient>
              </TouchableOpacity>
            </RevealSection>

            <YStack height={180} justifyContent="center" alignItems="center" opacity={0.5}>
              <Ionicons name="arrow-down-circle-outline" size={24} color="#666" />
              <Text color="#666" fontSize={13} marginTop="$2">
                Scroll for more
              </Text>
            </YStack>
          </YStack>
        </SafeAreaView>
      </Animated.ScrollView>
    </View>
  )
}

// ── Reveal Animation Wrapper (unchanged) ──
function RevealSection({ index, scrollY, children }: { index: number; scrollY: any; children: React.ReactNode }) {
  const start = index * 280 + 200
  const end = start + 200

  const animatedStyle = useAnimatedStyle(() => {
    const opacity = interpolate(scrollY.value, [start, end], [0.7, 1], Extrapolate.CLAMP)
    const translateY = interpolate(scrollY.value, [start, end], [40, 0], Extrapolate.CLAMP)
    return { opacity, transform: [{ translateY }] }
  })

  return (
    <Animated.View style={[{ marginTop: 16 }, animatedStyle]}>
      {children}
    </Animated.View>
  )
}

// ── Parallax Card (unchanged) ──
function ParallaxItem({ item, index, scrollX }: any) {
  const inputRange = [
    (index - 1) * (ITEM_WIDTH + SPACING * 2),
    index * (ITEM_WIDTH + SPACING * 2),
    (index + 1) * (ITEM_WIDTH + SPACING * 2),
  ]

  const containerStyle = useAnimatedStyle(() => {
    const scale = interpolate(scrollX.value, inputRange, [0.92, 1, 0.92], Extrapolate.CLAMP)
    const opacity = interpolate(scrollX.value, inputRange, [0.65, 1, 0.65], Extrapolate.CLAMP)
    return { transform: [{ scale }], opacity }
  })

  return (
    <Animated.View style={[{ width: ITEM_WIDTH, marginHorizontal: SPACING }, containerStyle]}>
      <YStack
        borderRadius={30}
        overflow="hidden"
        backgroundColor="#18181A"
        borderWidth={1}
        borderColor="rgba(255,255,255,0.08)"
        shadowColor="#000"
        shadowOffset={{ width: 0, height: 10 }}
        shadowOpacity={0.4}
        shadowRadius={20}
        elevation={12}
      >
        <Animated.Image source={item.image} resizeMode="cover" style={{ width: '100%', height: IMAGE_HEIGHT }} />
        <LinearGradient
          colors={['transparent', 'rgba(0,0,0,0.6)', 'rgba(0,0,0,0.9)']}
          style={{ position: 'absolute', bottom: 0, left: 0, right: 0, height: 100 }}
        />
        <YStack position="absolute" bottom={0} width="100%" padding="$4">
          <Text
            color="#FFFFFF"
            fontWeight="700"
            fontSize={16}
            letterSpacing={0.2}
            textShadowColor="rgba(0,0,0,0.5)"
            textShadowOffset={{ width: 0, height: 1 }}
            textShadowRadius={4}
          >
            {item.name}
          </Text>
        </YStack>
      </YStack>
    </Animated.View>
  )
}

// ── Animated Dot (unchanged) ──
function AnimatedDot({ index, scrollX }: any) {
  const inputRange = [
    (index - 1) * (ITEM_WIDTH + SPACING * 2),
    index * (ITEM_WIDTH + SPACING * 2),
    (index + 1) * (ITEM_WIDTH + SPACING * 2),
  ]

  const dotStyle = useAnimatedStyle(() => {
    const width = interpolate(scrollX.value, inputRange, [8, 28, 8], Extrapolate.CLAMP)
    const opacity = interpolate(scrollX.value, inputRange, [0.3, 1, 0.3], Extrapolate.CLAMP)
    const scale = interpolate(scrollX.value, inputRange, [1, 1.15, 1], Extrapolate.CLAMP)
    return { width, opacity, transform: [{ scale }] }
  })

  return (
    <Animated.View
      style={[{
        height: 6,
        borderRadius: 6,
        backgroundColor: '#FF8C00',
        shadowColor: '#FF8C00',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.5,
        shadowRadius: 6,
      }, dotStyle]}
    />
  )
}

const styles = StyleSheet.create({
  logoBox: {},
})