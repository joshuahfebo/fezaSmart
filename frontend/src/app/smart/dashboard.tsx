import React, { useEffect, useState } from 'react';
import { StyleSheet, View, ScrollView, Dimensions, ActivityIndicator } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { useResults } from '@/contexts/results-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses, recentGrades, getStudentStats, weeklyAttendance } from '@/data/studentData';

const { width } = Dimensions.get('window');

export default function DashboardScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const { studentProfile, studentName, loading: studentLoading } = useStudent();
  const { currentStudentResults, fetchStudentResults, loading: resultsLoading } = useResults();

  const [stats, setStats] = useState(getStudentStats());
  const attendanceStreak = weeklyAttendance.filter(a => a.status === 'present').length;

  useEffect(() => {
    if (studentProfile?.id) {
      fetchStudentResults(studentProfile.id);
    }
  }, [studentProfile?.id]);

  useEffect(() => {
    if (currentStudentResults.length > 0) {
      const avgScore = currentStudentResults.reduce((sum, r) => sum + (r.averagePercentage || 0), 0) / currentStudentResults.length;
      setStats(prev => ({ ...prev, gpa: avgScore / 25 }));
    }
  }, [currentStudentResults]);

  if (studentLoading) {
    return (
      <View style={[styles.centered, { backgroundColor: colors.background }]}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text fontSize={14} color={colors.textSecondary} marginTop="$2">Loading dashboard...</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        <SafeAreaView style={{ flex: 1 }}>
          <YStack gap="$4" padding="$4">
            <XStack justifyContent="space-between" alignItems="center">
              <YStack gap="$1">
                <Text fontSize={28} fontWeight="800" color={colors.text} letterSpacing={-0.5}>Welcome back</Text>
                <Text fontSize={16} color={colors.textSecondary}>{studentName}</Text>
              </YStack>
              <View style={[styles.avatar, { backgroundColor: colors.orangeBgMedium }]}>
                <Ionicons name="person" size={24} color={colors.primary} />
              </View>
            </XStack>

            <XStack gap="$3" justifyContent="space-between">
              <StatCard icon="trophy" label="GPA" value={stats.gpa.toFixed(2)} subtext="/4.0" color="#FF8C00" colors={colors} isDark={isDark} />
              <StatCard icon="checkmark-circle" label="Attendance" value={stats.attendanceRate + '%'} subtext={attendanceStreak + ' day streak'} color="#4CAF50" colors={colors} isDark={isDark} />
              <StatCard icon="book" label="Courses" value={stats.totalCourses} subtext={stats.completedCourses + ' completed'} color="#2196F3" colors={colors} isDark={isDark} />
            </XStack>
          </YStack>

          <YStack gap="$4" padding="$4">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Your Progress</Text>
            <View style={[styles.card, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
              <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
              <YStack gap="$3" padding="$4">
                <XStack justifyContent="space-between" alignItems="center">
                  <Text fontSize={16} fontWeight="700" color={colors.text}>Overall Performance</Text>
                  <View style={[styles.badge, { backgroundColor: colors.orangeBg }]}>
                    <Text fontSize={12} fontWeight="600" color={colors.primary}>
                      {stats.gpa >= 3.5 ? 'Excellent' : stats.gpa >= 3.0 ? 'Good' : 'Average'}
                    </Text>
                  </View>
                </XStack>
                <YStack gap="$2">
                  <XStack justifyContent="space-between">
                    <Text fontSize={14} color={colors.textSecondary}>Semester Progress</Text>
                    <Text fontSize={14} fontWeight="600" color={colors.primary}>
                      {Math.round(courses.reduce((sum, c) => sum + c.progress, 0) / courses.length)}%
                    </Text>
                  </XStack>
                  <View style={styles.progressBarContainer}>
                    <View style={[styles.progressBar, {
                      width: `${Math.round(courses.reduce((sum, c) => sum + c.progress, 0) / courses.length)}%`,
                      backgroundColor: colors.primary
                    }]} />
                  </View>
                </YStack>
                <XStack gap="$3" flexWrap="wrap">
                  {courses.slice(0, 4).map((course) => (
                    <View key={course.id} style={styles.courseMiniCard}>
                      <View style={[styles.courseIcon, { backgroundColor: course.color + '33' }]}>
                        <Ionicons name={course.icon as any} size={16} color={course.color} />
                      </View>
                      <Text fontSize={12} fontWeight="600" color={colors.text} numberOfLines={1}>{course.code}</Text>
                      <Text fontSize={11} color={colors.textTertiary}>{course.progress}%</Text>
                    </View>
                  ))}
                </XStack>
              </YStack>
            </View>
          </YStack>

          {studentProfile && (
            <YStack gap="$4" padding="$4">
              <Text fontSize={18} fontWeight="700" color={colors.text}>Student Info</Text>
              <View style={[styles.card, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
                <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                <YStack gap="$2" padding="$4">
                  <XStack justifyContent="space-between">
                    <Text fontSize={14} color={colors.textSecondary}>Name</Text>
                    <Text fontSize={14} fontWeight="600" color={colors.text}>{studentProfile.firstName} {studentProfile.lastName}</Text>
                  </XStack>
                  <XStack justifyContent="space-between">
                    <Text fontSize={14} color={colors.textSecondary}>Control Number</Text>
                    <Text fontSize={14} fontWeight="600" color={colors.text}>{studentProfile.controlNumber}</Text>
                  </XStack>
                  {studentProfile.gender && (
                    <XStack justifyContent="space-between">
                      <Text fontSize={14} color={colors.textSecondary}>Gender</Text>
                      <Text fontSize={14} fontWeight="600" color={colors.text}>{studentProfile.gender}</Text>
                    </XStack>
                  )}
                </YStack>
              </View>
            </YStack>
          )}

          <YStack gap="$4" padding="$4">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Recent Grades</Text>
            {recentGrades.slice(0, 3).map((grade) => {
              const gradeColor = grade.percentage >= 80 ? '#4CAF50' : grade.percentage >= 60 ? '#FF9800' : '#F44336';
              return (
                <View key={grade.id} style={[styles.card, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
                  <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                  <XStack gap="$3" alignItems="center" padding="$3">
                    <YStack flex={1} gap="$1">
                      <Text fontSize={15} fontWeight="700" color={colors.text}>{grade.courseName}</Text>
                      <Text fontSize={12} color={colors.textTertiary}>{grade.type} - {grade.date}</Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text fontSize={16} fontWeight="800" color={gradeColor}>{grade.score}/{grade.maxScore}</Text>
                      <View style={[styles.badge, { backgroundColor: gradeColor + '26' }]}>
                        <Text fontSize={11} fontWeight="600" color={gradeColor}>{grade.grade}</Text>
                      </View>
                    </YStack>
                  </XStack>
                </View>
              );
            })}
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

function StatCard({ icon, label, value, subtext, color, colors, isDark }: {
  icon: string;
  label: string;
  value: string | number;
  subtext: string;
  color: string;
  colors: any;
  isDark: boolean;
}) {
  return (
    <View style={[styles.statCard, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
      <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
      <YStack gap="$1" padding="$3" alignItems="center">
        <View style={[styles.statIcon, { backgroundColor: color + '26' }]}>
          <Ionicons name={icon as any} size={18} color={color} />
        </View>
        <Text fontSize={20} fontWeight="800" color={color}>{value}</Text>
        <Text fontSize={10} color={colors.textTertiary}>{label}</Text>
        <Text fontSize={9} color={colors.textTertiary}>{subtext}</Text>
      </YStack>
    </View>
  );
}

const styles = StyleSheet.create({
  centered: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  avatar: { width: 48, height: 48, borderRadius: 24, alignItems: 'center', justifyContent: 'center' },
  statCard: { flex: 1, borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.05)' },
  statIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
  card: { borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.05)' },
  badge: { paddingHorizontal: 8, paddingVertical: 4, borderRadius: 8 },
  courseMiniCard: { width: '23%', minWidth: 70, alignItems: 'center', gap: 4 },
  courseIcon: { width: 32, height: 32, borderRadius: 8, alignItems: 'center', justifyContent: 'center' },
  progressBarContainer: { height: 8, backgroundColor: 'rgba(0,0,0,0.1)', borderRadius: 4, overflow: 'hidden' },
  progressBar: { height: '100%', borderRadius: 4 },
});
