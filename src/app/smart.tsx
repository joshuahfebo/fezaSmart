// src/app/smart.tsx
import React, { useState } from 'react'
import {
  Dimensions,
  StyleSheet,
  View,
  TextInput,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  TouchableOpacity,
} from 'react-native'
import { BlurView } from 'expo-blur'
import { SafeAreaView } from 'react-native-safe-area-context'
import { YStack, XStack, Text, Button } from 'tamagui'
import { LinearGradient } from 'expo-linear-gradient'
import { Ionicons } from '@expo/vector-icons'
import { useRouter } from 'expo-router'

const { width } = Dimensions.get('window')

export default function SmartScreen() {
  const [email, setEmail] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [secureText, setSecureText] = useState(true)
  const router = useRouter()

  const handleLogin = () => {
    // mock login – navigate to home
    router.replace('/')
  }

  return (
    <View style={{ flex: 1, backgroundColor: '#0A0A0A' }}>
      {/* Subtle background layer */}
      <View style={StyleSheet.absoluteFill}>
        <View style={[StyleSheet.absoluteFill, { backgroundColor: '#0A0A0A' }]} />
        {/* Soft radial gradient effect (simulated with multiple blurs) */}
        <BlurView intensity={30} tint="dark" style={StyleSheet.absoluteFill} />
        <View
          style={[
            StyleSheet.absoluteFill,
            {
              backgroundColor: 'rgba(255,140,0,0.03)',
            },
          ]}
        />
      </View>

      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      >
        <ScrollView
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
          keyboardShouldPersistTaps="handled"
        >
          <SafeAreaView style={{ flex: 1 }}>
            <YStack gap="$6" padding="$4" justifyContent="center" flex={1}>
              {/* Logo / Branding area */}
              <YStack alignItems="center" gap="$2">
                {/* Minimal icon */}
                <View
                  style={{
                    width: 60,
                    height: 60,
                    borderRadius: 20,
                    backgroundColor: 'rgba(255,140,0,0.15)',
                    alignItems: 'center',
                    justifyContent: 'center',
                    marginBottom: 8,
                  }}
                >
                  <Ionicons name="book" size={32} color="#FF8C00" />
                </View>

                <Text
                  fontSize={36}
                  fontWeight="900"
                  letterSpacing={-1}
                  color="#FFFFFF"
                  lineHeight={42}
                >
                  Feza Smart
                </Text>
                <Text
                  color="#888"
                  fontSize={15}
                  fontWeight="500"
                  letterSpacing={0.2}
                  textAlign="center"
                >
                   Sign in to your account
                </Text>
              </YStack>

              {/* Input fields – liquid glass */}
              <YStack gap="$4" marginTop="$2">
                {/* Email */}
                <GlassInput
                  icon="mail-outline"
                  placeholder="Email"
                  value={email}
                  onChangeText={setEmail}
                  keyboardType="email-address"
                  autoCapitalize="none"
                />

                {/* Username */}
                <GlassInput
                  icon="person-outline"
                  placeholder="Username"
                  value={username}
                  onChangeText={setUsername}
                  autoCapitalize="none"
                />

                {/* Password */}
                <GlassInput
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
                        name={secureText ? 'eye-off-outline' : 'eye-outline'}
                        size={20}
                        color="#777"
                      />
                    </TouchableOpacity>
                  }
                />
              </YStack>

              {/* Sign in button */}
              <TouchableOpacity onPress={handleLogin} activeOpacity={0.8}>
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
                    shadowOffset: { width: 0, height: 8 },
                    shadowOpacity: 0.3,
                    shadowRadius: 16,
                    elevation: 8,
                  }}
                >
                  <Text color="#FFF" fontWeight="700" fontSize={17} letterSpacing={0.5}>
                    Sign In
                  </Text>
                </LinearGradient>
              </TouchableOpacity>

              {/* Footer link */}
              <XStack justifyContent="center" gap="$2" marginTop="$2">
                <Text color="#888" fontSize={14}>
                  Don't have an account?
                </Text>
                <TouchableOpacity>
                  <Text color="#FF8C00" fontSize={14} fontWeight="600">
                    Create one
                  </Text>
                </TouchableOpacity>
              </XStack>
            </YStack>
          </SafeAreaView>
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  )
}

/* ── Glass input component ── */
function GlassInput({
  icon,
  placeholder,
  value,
  onChangeText,
  secureTextEntry,
  keyboardType,
  autoCapitalize,
  rightIcon,
}: {
  icon: keyof typeof Ionicons.glyphMap
  placeholder: string
  value: string
  onChangeText: (text: string) => void
  secureTextEntry?: boolean
  keyboardType?: any
  autoCapitalize?: any
  rightIcon?: React.ReactNode
}) {
  return (
    <View style={styles.inputWrapper}>
      <LinearGradient
        colors={['rgba(255,255,255,0.08)', 'rgba(255,255,255,0.03)']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.inputGradient}
      >
        <BlurView intensity={20} tint="dark" style={styles.inputBlur}>
          <Ionicons
            name={icon}
            size={20}
            color="#FF8C00"
            style={{ marginRight: 12 }}
          />
          <TextInput
            style={styles.textInput}
            placeholder={placeholder}
            placeholderTextColor="#777"
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
  )
}

const styles = StyleSheet.create({
  scrollContent: {
    flexGrow: 1,
    justifyContent: 'center',
    paddingHorizontal: 24,
  },
  inputWrapper: {
    borderRadius: 28,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 12,
    elevation: 5,
  },
  inputGradient: {
    borderRadius: 28,
  },
  inputBlur: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 56,
    paddingHorizontal: 20,
    borderRadius: 28,
    overflow: 'hidden',
  },
  textInput: {
    flex: 1,
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '500',
  },
})