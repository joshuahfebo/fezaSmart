import React from 'react'
import { Dimensions, StyleSheet, View, ScrollView } from 'react-native'
import { BlurView } from 'expo-blur'
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context'
import { YStack, XStack, Text, Avatar, H3, H4 } from 'tamagui'
import { LinearGradient } from 'expo-linear-gradient'
import { Ionicons } from '@expo/vector-icons'

const { width } = Dimensions.get('window')

export default function ProfileScreen() {
  const insets = useSafeAreaInsets()
  const bottomPadding = insets.bottom + 80

  return (
    <View style={{ flex: 1, backgroundColor: '#0A0A0A' }}>
      {/* Full‑screen subtle background layer (optional, matches home aesthetic) */}
      <View style={StyleSheet.absoluteFill}>
        <Image
          source={require('../../assets/images/fezaschools/fezaBoys.webp')}
          blurRadius={80}
          style={[StyleSheet.absoluteFillObject, { width: '100%', height: '100%', opacity: 0.15 }]}
          resizeMode="cover"
        />
        <BlurView intensity={60} tint="dark" style={StyleSheet.absoluteFill} />
        <View style={[StyleSheet.absoluteFill, { backgroundColor: 'rgba(10,10,10,0.25)' }]} />
      </View>

      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: bottomPadding }}
        showsVerticalScrollIndicator={false}
      >
        <SafeAreaView style={{ flex: 1 }}>
          <YStack padding="$4" gap="$5">
            {/* Header – same as HomeScreen */}
            <XStack alignItems="flex-start" gap="$3">
              <YStack
                width={4}
                height={36}
                borderRadius={2}
                backgroundColor="#FF8C00"
                marginTop={4}
              />
              <YStack>
                <Text
                  fontSize={38}
                  fontWeight="900"
                  letterSpacing={-1}
                  color="#FFFFFF"
                  lineHeight={42}
                >
                  Profile
                </Text>
                <Text
                  color="#888"
                  fontSize={13}
                  fontStyle="italic"
                  fontWeight="500"
                  marginTop={4}
                  letterSpacing={0.3}
                >
                  Your campus identity
                </Text>
              </YStack>
            </XStack>

            {/* Avatar card – liquid glass */}
            <YStack
              borderRadius={30}
              borderWidth={1}
              borderColor="rgba(255,255,255,0.08)"
              backgroundColor="rgba(24,24,26,0.7)"
              overflow="hidden"
              shadowColor="#000"
              shadowOffset={{ width: 0, height: 10 }}
              shadowOpacity={0.3}
              shadowRadius={20}
              elevation={12}
            >
              <LinearGradient
                colors={['rgba(255,255,255,0.05)', 'transparent']}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={{
                  padding: 24,
                  alignItems: 'center',
                }}
              >
                <Avatar circular size="$8" borderWidth={2} borderColor="#FF8C00" marginBottom="$3">
                  <Avatar.Image
                    source={{ uri: 'https://i.pravatar.cc/300' }}
                  />
                </Avatar>
                <H3 color="#fff" fontWeight="700" marginBottom="$1">
                  Joshuah Kyando
                </H3>
                <Text color="#AAA" fontSize={14} opacity={0.8}>
                  Student • Feza Boys' Secondary
                </Text>
              </LinearGradient>
            </YStack>

            {/* Stats row */}
            <XStack gap="$4" justifyContent="space-around">
              <StatCard label="Attendance" value="98%" />
              <StatCard label="Grades" value="A" />
              <StatCard label="Activities" value="5" />
            </XStack>

            {/* Menu items – glass list */}
            <YStack
              borderRadius={24}
              overflow="hidden"
              borderWidth={1}
              borderColor="rgba(255,255,255,0.08)"
              backgroundColor="rgba(24,24,26,0.6)"
              shadowColor="#000"
              shadowOffset={{ width: 0, height: 8 }}
              shadowOpacity={0.2}
              shadowRadius={16}
              elevation={8}
            >
              <MenuItem icon="person-circle-outline" label="Personal Details" />
              <MenuItem icon="calendar-outline" label="Timetable" />
              <MenuItem icon="ribbon-outline" label="Achievements" />
              <MenuItem icon="settings-outline" label="Settings" />
              <MenuItem icon="log-out-outline" label="Sign Out" danger />
            </YStack>
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  )
}

/* ── Reusable components ── */

function StatCard({ label, value }: { label: string; value: string }) {
  return (
    <YStack
      flex={1}
      borderRadius={20}
      backgroundColor="rgba(24,24,26,0.5)"
      borderWidth={1}
      borderColor="rgba(255,255,255,0.06)"
      padding="$3"
      alignItems="center"
      justifyContent="center"
      shadowColor="#000"
      shadowOffset={{ width: 0, height: 4 }}
      shadowOpacity={0.2}
      shadowRadius={8}
      elevation={4}
    >
      <Text color="#FF8C00" fontSize={24} fontWeight="800">
        {value}
      </Text>
      <Text color="#AAA" fontSize={13} marginTop="$1" opacity={0.8}>
        {label}
      </Text>
    </YStack>
  )
}

function MenuItem({
  icon,
  label,
  danger = false,
}: {
  icon: keyof typeof Ionicons.glyphMap
  label: string
  danger?: boolean
}) {
  return (
    <XStack
      padding="$4"
      alignItems="center"
      borderBottomWidth={0.5}
      borderBottomColor="rgba(255,255,255,0.05)"
    >
      <Ionicons
        name={icon}
        size={22}
        color={danger ? '#FF4757' : '#FF8C00'}
        style={{ marginRight: 14 }}
      />
      <Text color={danger ? '#FF4757' : '#FFFFFF'} fontSize={16} fontWeight="500" flex={1}>
        {label}
      </Text>
      <Ionicons name="chevron-forward" size={18} color="#666" />
    </XStack>
  )
}

// Fix missing Image import at top (add this to existing imports)
import { Image } from 'react-native'