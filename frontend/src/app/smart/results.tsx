import React, { useState, useEffect } from 'react';
import { StyleSheet, View, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useResults } from '@/contexts/results-context';
import { useStudent } from '@/contexts/student-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses } from '@/data/studentData';

type TabType = 'all' | 'exams' | 'quizzes' | 'assignments';

export default function ResultsScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const { studentProfile } = useStudent();
  const { currentStudentResults, fetchStudentResults, loading, error } = useResults();
  const [activeTab, setActiveTab] = useState<TabType>('all');
  const [selectedCourse, setSelectedCourse] = useState<string>('all');

  useEffect(() => {
    if (studentProfile?.id) {
      fetchStudentResults(studentProfile.id);
    }
  }, [studentProfile?.id]);

  const overallAverage = currentStudentResults.length > 0
    ? (currentStudentResults.reduce((sum, r) => sum + (r.averagePercentage || 0), 0) / currentStudentResults.length).toFixed(1)
    : '0';

  const highestScore = currentStudentResults.length > 0
    ? Math.max(...currentStudentResults.map(r => r.averagePercentage || 0))
    : 0;

  if (loading) {
    return (
      <View style={[styles.centered, { backgroundColor: colors.background }]}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text fontSize={14} color={colors.textSecondary} marginTop="$2">Loading results...</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        <SafeAreaView style={{ flex: 1 }}>
          <YStack gap="$4" padding="$4">
            <YStack gap="$1">
              <Text fontSize={28} fontWeight="800" color={colors.text} letterSpacing={-0.5}>Academic Results</Text>
              <Text fontSize={16} color={colors.textSecondary}>Track your performance</Text>
            </YStack>

            <View style={[styles.statsRow, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
              <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
              <XStack gap="$4" alignItems="center" padding="$4">
                <YStack alignItems="center" flex={1}>
                  <Text fontSize={12} color={colors.textTertiary}>Average</Text>
                  <Text fontSize={24} fontWeight="800" color={colors.primary}>{overallAverage}%</Text>
                </YStack>
                <View style={styles.divider} />
                <YStack alignItems="center" flex={1}>
                  <Text fontSize={12} color={colors.textTertiary}>Total</Text>
                  <Text fontSize={24} fontWeight="800" color={colors.text}>{currentStudentResults.length || courses.length}</Text>
                </YStack>
                <View style={styles.divider} />
                <YStack alignItems="center" flex={1}>
                  <Text fontSize={12} color={colors.textTertiary}>Highest</Text>
                  <Text fontSize={24} fontWeight="800" color="#4CAF50">{highestScore > 0 ? highestScore.toFixed(0) : Math.max(...courses.map(c => c.progress))}%</Text>
                </YStack>
              </XStack>
            </View>

            <XStack gap="$2">
              {(['all', 'exams', 'quizzes', 'assignments'] as TabType[]).map((tab) => (
                <TouchableOpacity key={tab} onPress={() => setActiveTab(tab)} style={{ flex: 1 }}>
                  <View style={[styles.tabBtn, { backgroundColor: activeTab === tab ? colors.primary : isDark ? 'rgba(255,255,255,0.08)' : 'rgba(0,0,0,0.04)' }]}>
                    <Text fontSize={12} fontWeight={activeTab === tab ? '600' : '500'} color={activeTab === tab ? '#FFF' : colors.textSecondary}>
                      {tab.charAt(0).toUpperCase() + tab.slice(1)}
                    </Text>
                  </View>
                </TouchableOpacity>
              ))}
            </XStack>
          </YStack>

          <YStack gap="$2" padding="$4">
            {currentStudentResults.length > 0 ? (
              currentStudentResults.map((result) => {
                const gradeColor = (result.averagePercentage || 0) >= 80 ? '#4CAF50' : (result.averagePercentage || 0) >= 60 ? '#FF9800' : '#F44336';
                return (
                  <View key={result.id} style={[styles.gradeItem, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: colors.primary + '26' }]}>
                    <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                    <XStack gap="$3" alignItems="center" padding="$3">
                      <View style={[styles.gradeIcon, { backgroundColor: colors.primary + '26' }]}>
                        <Ionicons name="trophy" size={18} color={colors.primary} />
                      </View>
                      <YStack flex={1} gap="$1">
                        <Text fontSize={15} fontWeight="700" color={colors.text}>Exam #{result.examId}</Text>
                        <Text fontSize={12} color={colors.textTertiary}>
                          Division: {result.division || 'N/A'} | Rank: #{result.ranking || 'N/A'}
                        </Text>
                      </YStack>
                      <YStack alignItems="flex-end" gap="$1">
                        <Text fontSize={16} fontWeight="800" color={gradeColor}>{(result.averagePercentage || 0).toFixed(1)}%</Text>
                        <View style={[styles.gradeBadge, { backgroundColor: gradeColor + '26' }]}>
                          <Text fontSize={11} fontWeight="600" color={gradeColor}>{result.totalPoints || 0} pts</Text>
                        </View>
                      </YStack>
                    </XStack>
                  </View>
                );
              })
            ) : (
              courses.map((course) => {
                const gradeColor = course.progress >= 80 ? '#4CAF50' : course.progress >= 60 ? '#FF9800' : '#F44336';
                return (
                  <View key={course.id} style={[styles.gradeItem, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: course.color + '26' }]}>
                    <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                    <XStack gap="$3" alignItems="center" padding="$3">
                      <View style={[styles.gradeIcon, { backgroundColor: course.color + '26' }]}>
                        <Ionicons name={course.icon as any} size={18} color={course.color} />
                      </View>
                      <YStack flex={1} gap="$1">
                        <Text fontSize={15} fontWeight="700" color={colors.text}>{course.name}</Text>
                        <Text fontSize={12} color={colors.textTertiary}>{course.code} - {course.teacher}</Text>
                      </YStack>
                      <YStack alignItems="flex-end" gap="$1">
                        <Text fontSize={16} fontWeight="800" color={gradeColor}>{course.progress}%</Text>
                        <View style={[styles.gradeBadge, { backgroundColor: gradeColor + '26' }]}>
                          <Text fontSize={11} fontWeight="600" color={gradeColor}>{course.grade}</Text>
                        </View>
                      </YStack>
                    </XStack>
                  </View>
                );
              })
            )}
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  centered: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  statsRow: { borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.05)' },
  divider: { width: 1, height: 40, backgroundColor: 'rgba(0,0,0,0.1)' },
  tabBtn: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', paddingVertical: 10, paddingHorizontal: 12, borderRadius: 12 },
  chip: { paddingHorizontal: 14, paddingVertical: 8, borderRadius: 20, borderWidth: 1 },
  gradeItem: { borderRadius: 12, overflow: 'hidden', borderWidth: 1 },
  gradeIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
  gradeBadge: { paddingHorizontal: 8, paddingVertical: 4, borderRadius: 8 },
  emptyState: { padding: 40, borderRadius: 20, overflow: 'hidden', alignItems: 'center', justifyContent: 'center' },
});
