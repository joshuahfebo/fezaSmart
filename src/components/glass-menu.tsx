// src/components/glass-menu.tsx
import React from 'react'
import {
  View,
  StyleSheet,
  TouchableOpacity,
  Modal,
  Pressable,
} from 'react-native'
import { BlurView } from 'expo-blur'
import { LinearGradient } from 'expo-linear-gradient'
import Animated, {
  FadeIn,
  SlideInDown,
  FadeOut,
  SlideOutDown,
} from 'react-native-reanimated'
import { Text, YStack, XStack } from 'tamagui'
import { Ionicons } from '@expo/vector-icons'

interface MenuItemData {
  label: string
  subtitle: string
  icon: keyof typeof Ionicons.glyphMap
  action: string
}

interface GlassMenuProps {
  visible: boolean
  onClose: () => void
  onAction: (action: string) => void
  menuItems?: MenuItemData[]
}

const defaultMenuItems: MenuItemData[] = [
  {
    label: 'About Feza',
    subtitle: 'Learn more about us',
    icon: 'information-circle-outline',
    action: 'about',
  },
  {
    label: 'Settings',
    subtitle: 'App preferences',
    icon: 'settings-outline',
    action: 'settings',
  },
  {
    label: 'Contact Us',
    subtitle: 'Get in touch',
    icon: 'chatbubble-outline',
    action: 'contact',
  },
]

export function GlassMenu({
  visible,
  onClose,
  onAction,
  menuItems = defaultMenuItems,
}: GlassMenuProps) {
  return (
    <Modal
      visible={visible}
      transparent
      animationType="none"
      onRequestClose={onClose}
    >
      {/* Tappable backdrop */}
      <Pressable style={styles.backdrop} onPress={onClose}>
        <Animated.View
          entering={FadeIn.duration(200)}
          exiting={FadeOut.duration(200)}
          style={StyleSheet.absoluteFill}
        />
      </Pressable>

      {/* Menu panel */}
      <Animated.View
        entering={SlideInDown.springify().damping(18).stiffness(180)}
        exiting={SlideOutDown.duration(200)}
        style={styles.panel}
      >
        <LinearGradient
          colors={['rgba(255,255,255,0.16)', 'rgba(255,255,255,0.06)']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={styles.panelBorder}
        >
          <BlurView intensity={85} tint="dark" style={styles.panelBlur}>
            <YStack padding="$4" gap="$4">
              {/* Header */}
              <XStack alignItems="center" gap="$3">
                <View style={styles.iconBox}>
                  <Ionicons name="apps-outline" size={22} color="#FF8C00" />
                </View>
                <Text color="#FFF" fontSize={20} fontWeight="700">
                  Menu
                </Text>
              </XStack>

              {/* Items */}
              {menuItems.map((item, index) => (
                <TouchableOpacity
                  key={index}
                  onPress={() => onAction(item.action)}
                  activeOpacity={0.7}
                >
                  <XStack
                    gap="$3"
                    alignItems="center"
                    padding="$3"
                    borderRadius={14}
                    backgroundColor="rgba(255,255,255,0.08)"
                  >
                    <View style={styles.iconBox}>
                      <Ionicons name={item.icon} size={20} color="#FF8C00" />
                    </View>
                    <YStack flex={1}>
                      <Text color="#FFF" fontSize={16} fontWeight="600">
                        {item.label}
                      </Text>
                      <Text color="#CCC" fontSize={13} opacity={0.85}>
                        {item.subtitle}
                      </Text>
                    </YStack>
                    <Ionicons name="chevron-forward" size={16} color="#777" />
                  </XStack>
                </TouchableOpacity>
              ))}

              {/* Close button – soft red */}
              <TouchableOpacity
                onPress={onClose}
                activeOpacity={0.7}
                style={styles.closeBtn}
              >
                <XStack gap="$2" alignItems="center" justifyContent="center">
                  <Ionicons name="close-circle" size={18} color="#FF7A7A" />
                  <Text color="#FF7A7A" fontSize={15} fontWeight="600">
                    Close Menu
                  </Text>
                </XStack>
              </TouchableOpacity>
            </YStack>
          </BlurView>
        </LinearGradient>
      </Animated.View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.55)',
  },
  panel: {
    position: 'absolute',
    bottom: 100,
    left: 28,
    right: 28,
    borderRadius: 30,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 18 },
    shadowOpacity: 0.6,
    shadowRadius: 36,
    elevation: 24,
  },
  panelBorder: {
    borderRadius: 30,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.12)',
  },
  panelBlur: {
    borderRadius: 30,
    overflow: 'hidden',
  },
  iconBox: {
    width: 40,
    height: 40,
    borderRadius: 13,
    backgroundColor: 'rgba(255,140,0,0.15)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  closeBtn: {
    marginTop: 8,
    paddingVertical: 14,
    borderRadius: 16,
    backgroundColor: 'rgba(255,100,100,0.12)',
  },
})