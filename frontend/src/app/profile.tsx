import { useTheme, useThemeMode, useToggleTheme } from "@/hooks/use-theme";
import { Ionicons } from "@react-native-vector-icons/ionicons";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";
import React, { useCallback, useEffect, useRef, useState } from "react";
import {
  Animated,
  Dimensions,
  Easing,
  FlatList,
  KeyboardAvoidingView,
  Platform,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import {
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { Text, XStack, YStack } from "tamagui";

const { width: screenWidth } = Dimensions.get("window");

type Message = {
  id: string;
  role: "user" | "assistant";
  text: string;
  timestamp: number;
};

const WELCOME_MESSAGE: Message = {
  id: "welcome",
  role: "assistant",
  text: "Hello! I'm Fezaintelligence — your AI assistant for Feza Schools. I can help you with results, schedules, school info, and more. What would you like to know?",
  timestamp: Date.now(),
};

const MOCK_RESPONSES = [
  "Based on the latest school data, your academic performance is on track. Would you like me to break down your results by subject?",
  "The school timetable for next week has been updated. Form 3A has Mathematics on Monday at 8:00 AM in Room 101.",
  "Your current GPA is 3.65 out of 4.0 — that's in the 'Excellent' range. Keep up the great work!",
  "The next parent-teacher conference is scheduled for August 28th, 2026. Would you like me to set a reminder?",
  "I found 3 recent exam results for your account. The most recent was End of Term 1 with an average of 82%.",
  "School fees for Term 2 are due by September 1st. The outstanding balance is UGX 450,000.",
];

/* ── Fezaintelligence Animated Title ── */
function FezTitle({ colors }: { colors: any }) {
  const fadeAnim = useRef(new Animated.Value(1)).current;
  const [showFull, setShowFull] = useState(true);

  useEffect(() => {
    const cycle = () => {
      setShowFull(true);
      fadeAnim.setValue(1);

      const timer = setTimeout(() => {
        Animated.timing(fadeAnim, {
          toValue: 0,
          duration: 1800,
          easing: Easing.bezier(0.4, 0, 0.2, 1),
          useNativeDriver: true,
        }).start(() => {
          setShowFull(false);
          setTimeout(cycle, 8000);
        });
      }, 10000);

      return () => clearTimeout(timer);
    };

    const cleanup = cycle();
    return cleanup;
  }, []);

  return (
    <XStack alignItems="baseline" overflow="hidden">
      <Animated.View
        style={{
          opacity: fadeAnim,
          flexDirection: "row",
        }}
      >
        <Text
          fontSize={22}
          fontWeight="800"
          color={colors.text}
          letterSpacing={-0.5}
        >
          Fez
        </Text>
      </Animated.View>
      <Animated.View
        style={{
          opacity: showFull ? 1 : 1,
          flexDirection: "row",
        }}
      >
        <Text
          fontSize={22}
          fontWeight="800"
          color={colors.primary}
          letterSpacing={-0.5}
        >
          aintelligence
        </Text>
      </Animated.View>
    </XStack>
  );
}

/* ── Typing Indicator ── */
function TypingIndicator({ colors }: { colors: any }) {
  const dots = [0, 1, 2];
  return (
    <XStack gap={4} alignItems="center" paddingHorizontal="$4" paddingVertical="$3">
      <View
        style={[
          styles.assistantBubble,
          {
            backgroundColor: colors.cardBackground,
            borderColor: colors.cardBorder,
          },
        ]}
      >
        <XStack gap={5} alignItems="center">
          <Ionicons name="sparkles" size={14} color={colors.primary} />
          {dots.map((i) => (
            <AnimatedDot key={i} index={i} color={colors.primary} />
          ))}
        </XStack>
      </View>
    </XStack>
  );
}

function AnimatedDot({ index, color }: { index: number; color: string }) {
  const pulse = useRef(new Animated.Value(0.3)).current;

  useEffect(() => {
    const loop = Animated.loop(
      Animated.sequence([
        Animated.delay(index * 180),
        Animated.timing(pulse, {
          toValue: 1,
          duration: 400,
          useNativeDriver: true,
        }),
        Animated.timing(pulse, {
          toValue: 0.3,
          duration: 400,
          useNativeDriver: true,
        }),
      ]),
    );
    loop.start();
    return () => loop.stop();
  }, []);

  return (
    <Animated.View
      style={{
        width: 6,
        height: 6,
        borderRadius: 3,
        backgroundColor: color,
        opacity: pulse,
        transform: [{ scale: pulse }],
      }}
    />
  );
}

/* ── Chat Message Bubble ── */
function MessageBubble({
  message,
  colors,
  isDark,
  isLast,
}: {
  message: Message;
  colors: any;
  isDark: boolean;
  isLast: boolean;
}) {
  const isUser = message.role === "user";
  const scaleAnim = useRef(new Animated.Value(0.95)).current;
  const opacityAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.parallel([
      Animated.spring(scaleAnim, {
        toValue: 1,
        tension: 80,
        friction: 12,
        useNativeDriver: true,
      }),
      Animated.timing(opacityAnim, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }),
    ]).start();
  }, []);

  const timeStr = new Date(message.timestamp).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });

  return (
    <Animated.View
      style={{
        opacity: opacityAnim,
        transform: [{ scale: scaleAnim }],
        paddingHorizontal: 16,
        paddingVertical: 4,
      }}
    >
      <XStack
        gap="$2"
        alignItems={isUser ? "flex-end" : "flex-start"}
        flexDirection={isUser ? "row-reverse" : "row"}
      >
        {!isUser && (
          <View
            style={[
              styles.aiAvatar,
              { backgroundColor: colors.primary + "18" },
            ]}
          >
            <Ionicons name="sparkles" size={14} color={colors.primary} />
          </View>
        )}

        <View style={{ maxWidth: screenWidth * 0.78 }}>
          <View
            style={[
              isUser ? styles.userBubble : styles.assistantBubble,
              {
                backgroundColor: isUser
                  ? colors.primary
                  : isDark
                    ? "rgba(255,255,255,0.06)"
                    : "rgba(0,0,0,0.04)",
                borderColor: isUser
                  ? colors.primary
                  : colors.cardBorder,
              },
            ]}
          >
            <Text
              fontSize={15}
              lineHeight={22}
              color={isUser ? "#FFFFFF" : colors.text}
              fontWeight={isUser ? "500" : "400"}
            >
              {message.text}
            </Text>
          </View>

          <Text
            fontSize={11}
            color={colors.textTertiary}
            marginTop={4}
            paddingHorizontal={4}
            textAlign={isUser ? "right" : "left"}
          >
            {timeStr}
          </Text>
        </View>
      </XStack>
    </Animated.View>
  );
}

/* ── Suggestion Chips ── */
function SuggestionChips({
  onSelect,
  colors,
  isDark,
}: {
  onSelect: (text: string) => void;
  colors: any;
  isDark: boolean;
}) {
  const suggestions = [
    { icon: "bar-chart", text: "My results" },
    { icon: "calendar", text: "Class schedule" },
    { icon: "wallet", text: "Fee balance" },
    { icon: "school", text: "School info" },
  ];

  return (
    <XStack gap="$2" paddingHorizontal="$4" paddingVertical="$2" flexWrap="wrap">
      {suggestions.map((s, i) => (
        <TouchableOpacity key={i} onPress={() => onSelect(s.text)}>
          <View
            style={[
              styles.suggestionChip,
              {
                backgroundColor: isDark
                  ? "rgba(255,255,255,0.06)"
                  : "rgba(0,0,0,0.04)",
                borderColor: colors.primary + "33",
              },
            ]}
          >
            <Ionicons
              name={s.icon as any}
              size={14}
              color={colors.primary}
            />
            <Text
              fontSize={13}
              fontWeight="500"
              color={colors.text}
              marginLeft={6}
            >
              {s.text}
            </Text>
          </View>
        </TouchableOpacity>
      ))}
    </XStack>
  );
}

/* ── Main Chatbot Screen ── */
export default function ProfileScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === "dark";
  const insets = useSafeAreaInsets();
  const flatListRef = useRef<FlatList>(null);

  const [messages, setMessages] = useState<Message[]>([WELCOME_MESSAGE]);
  const [inputText, setInputText] = useState("");
  const [isTyping, setIsTyping] = useState(false);

  const sendMessage = useCallback(
    (text: string) => {
      if (!text.trim()) return;

      const userMsg: Message = {
        id: Date.now().toString(),
        role: "user",
        text: text.trim(),
        timestamp: Date.now(),
      };

      setMessages((prev) => [...prev, userMsg]);
      setInputText("");
      setIsTyping(true);

      setTimeout(() => {
        const response =
          MOCK_RESPONSES[Math.floor(Math.random() * MOCK_RESPONSES.length)];

        const aiMsg: Message = {
          id: (Date.now() + 1).toString(),
          role: "assistant",
          text: response,
          timestamp: Date.now(),
        };

        setIsTyping(false);
        setMessages((prev) => [...prev, aiMsg]);
      }, 1200 + Math.random() * 1000);
    },
    [],
  );

  const handleSuggestion = useCallback(
    (text: string) => {
      sendMessage(text);
    },
    [sendMessage],
  );

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      {/* ── Header ── */}
      <View
        style={[
          styles.header,
          {
            backgroundColor: colors.background,
            borderBottomColor: colors.border,
          },
        ]}
      >
        <BlurView
          intensity={isDark ? 60 : 40}
          tint={isDark ? "dark" : "light"}
          style={StyleSheet.absoluteFill}
        />
        <SafeAreaView edges={["top"]}>
          <XStack
            paddingHorizontal="$4"
            paddingVertical="$3"
            alignItems="center"
            justifyContent="space-between"
          >
            <XStack alignItems="center" gap="$3">
              <View
                style={[
                  styles.headerAvatar,
                  { backgroundColor: colors.primary + "18" },
                ]}
              >
                <Ionicons
                  name="sparkles"
                  size={20}
                  color={colors.primary}
                />
              </View>
              <YStack gap={0}>
                <FezTitle colors={colors} />
                <Text
                  fontSize={11}
                  color={colors.textTertiary}
                  fontWeight="500"
                  letterSpacing={0.3}
                >
                  {isTyping ? "Thinking..." : "Online"}
                </Text>
              </YStack>
            </XStack>

            <XStack gap="$3" alignItems="center">
              <TouchableOpacity
                style={[
                  styles.headerBtn,
                  {
                    backgroundColor: isDark
                      ? "rgba(255,255,255,0.06)"
                      : "rgba(0,0,0,0.04)",
                  },
                ]}
              >
                <Ionicons
                  name="ellipsis-vertical"
                  size={18}
                  color={colors.textSecondary}
                />
              </TouchableOpacity>
            </XStack>
          </XStack>
        </SafeAreaView>
      </View>

      {/* ── Messages ── */}
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === "ios" ? "padding" : "height"}
        keyboardVerticalOffset={100}
      >
        <FlatList
          ref={flatListRef}
          data={messages}
          keyExtractor={(item) => item.id}
          renderItem={({ item, index }) => (
            <MessageBubble
              message={item}
              colors={colors}
              isDark={isDark}
              isLast={index === messages.length - 1}
            />
          )}
          contentContainerStyle={{
            paddingTop: 12,
            paddingBottom: 8,
          }}
          onContentSizeChange={() =>
            flatListRef.current?.scrollToEnd({ animated: true })
          }
          onLayout={() =>
            flatListRef.current?.scrollToEnd({ animated: false })
          }
          ListFooterComponent={
            isTyping ? <TypingIndicator colors={colors} /> : null
          }
          ListHeaderComponent={
            messages.length <= 1 ? (
              <SuggestionChips
                onSelect={handleSuggestion}
                colors={colors}
                isDark={isDark}
              />
            ) : null
          }
        />

        {/* ── Input Bar ── */}
        <View
          style={[
            styles.inputContainer,
            {
              backgroundColor: colors.background,
              borderTopColor: colors.border,
            },
          ]}
        >
          <SafeAreaView edges={["bottom"]}>
            <XStack
              paddingHorizontal="$3"
              paddingVertical="$2"
              alignItems="flex-end"
              gap="$2"
            >
              <View
                style={[
                  styles.inputWrapper,
                  {
                    backgroundColor: isDark
                      ? "rgba(255,255,255,0.06)"
                      : "rgba(0,0,0,0.04)",
                    borderColor: colors.cardBorder,
                  },
                ]}
              >
                <TextInput
                  style={[styles.textInput, { color: colors.text }]}
                  placeholder="Ask Fezaintelligence..."
                  placeholderTextColor={colors.textTertiary}
                  value={inputText}
                  onChangeText={setInputText}
                  multiline
                  maxLength={2000}
                />
              </View>

              <TouchableOpacity
                onPress={() => sendMessage(inputText)}
                disabled={!inputText.trim()}
                style={[
                  styles.sendButton,
                  {
                    backgroundColor:
                      inputText.trim()
                        ? colors.primary
                        : isDark
                          ? "rgba(255,255,255,0.08)"
                          : "rgba(0,0,0,0.06)",
                  },
                ]}
                activeOpacity={0.7}
              >
                <Ionicons
                  name="arrow-up"
                  size={20}
                  color={
                    inputText.trim() ? "#FFFFFF" : colors.textTertiary
                  }
                />
              </TouchableOpacity>
            </XStack>
          </SafeAreaView>
        </View>
      </KeyboardAvoidingView>
    </View>
  );
}

const styles = StyleSheet.create({
  header: {
    position: "relative",
    overflow: "hidden",
    borderBottomWidth: 0.5,
    zIndex: 10,
  },
  headerAvatar: {
    width: 38,
    height: 38,
    borderRadius: 19,
    alignItems: "center",
    justifyContent: "center",
  },
  headerBtn: {
    width: 36,
    height: 36,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
  },
  aiAvatar: {
    width: 28,
    height: 28,
    borderRadius: 14,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 2,
  },
  userBubble: {
    borderRadius: 20,
    borderBottomRightRadius: 6,
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderWidth: 1,
  },
  assistantBubble: {
    borderRadius: 20,
    borderBottomLeftRadius: 6,
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderWidth: 1,
  },
  suggestionChip: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: 20,
    borderWidth: 1,
    marginBottom: 4,
  },
  inputContainer: {
    position: "relative",
    borderTopWidth: 0.5,
  },
  inputWrapper: {
    flex: 1,
    borderRadius: 24,
    borderWidth: 1,
    paddingHorizontal: 16,
    paddingVertical: Platform.OS === "ios" ? 10 : 6,
    minHeight: 44,
    maxHeight: 120,
  },
  textInput: {
    fontSize: 16,
    lineHeight: 22,
    fontWeight: "400",
    padding: 0,
    margin: 0,
  },
  sendButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    alignItems: "center",
    justifyContent: "center",
  },
});
