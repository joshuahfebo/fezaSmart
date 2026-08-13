import React from 'react';
import { StyleSheet, View, ScrollView } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { useAuth } from '@/contexts/auth-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses, getStudentStats, weeklyAttendance } from '@/data/studentData';

export default function ProfileScreen({ onLogout }: { onLogout?: () => void }) {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const { studentProfile, studentName } = useStudent();
  const { userProfile } = useAuth();
  const stats = getStudentStats();
  const presentDays = weeklyAttendance.filter(a => a.status === 'present').length;
  const totalDays = weeklyAttendance.length;
  const absentDays = weeklyAttendance.filter(a => a.status === 'absent').length;
  const lateDays = weeklyAttendance.filter(a => a.status === 'late').length;

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        <SafeAreaView style={{ flex: 1 }}>
          <View style={[styles.header, { backgroundColor: isDark ? 'rgba(0,0,0,0.4)' : 'rgba(255,140,0,0.1)' }]}>
            <BlurView intensity={isDark ? 40 : 60} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
            <YStack gap="$4" padding="$6" alignItems="center">
              <View style={[styles.avatar, { backgroundColor: colors.orangeBgMedium }]}>
                <Ionicons name="person" size={48} color={colors.primary} />
              </View>
              <YStack gap="$1" alignItems="center">
                <Text fontSize={28} fontWeight="800" color={colors.text}>{studentName}</Text>
                <Text fontSize={16} color={colors.textSecondary}>
                  {studentProfile?.controlNumber || userProfile?.username || ''}
                </Text>
              </YStack>
              <View style={[styles.statsRow, { backgroundColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(255,255,255,0.3)' }]}>
                <BlurView intensity={isDark ? 20 : 10} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                <XStack gap="$4" alignItems="center" paddingHorizontal="$4" paddingVertical="$2">
                  <YStack gap="$1" alignItems="center">
                    <Ionicons name="trophy-outline" size={18} color={colors.textSecondary} />
                    <Text fontSize={16} fontWeight="800" color={colors.text}>{stats.gpa.toFixed(2)}</Text>
                    <Text fontSize={11} color={colors.textTertiary}>GPA</Text>
                  </YStack>
                  <View style={styles.statDivider} />
                  <YStack gap="$1" alignItems="center">
                    <Ionicons name="book-outline" size={18} color={colors.textSecondary} />
                    <Text fontSize={16} fontWeight="800" color={colors.text}>{stats.totalCourses}</Text>
                    <Text fontSize={11} color={colors.textTertiary}>Courses</Text>
                  </YStack>
                  <View style={styles.statDivider} />
                  <YStack gap="$1" alignItems="center">
                    <Ionicons name="star-outline" size={18} color={colors.textSecondary} />
                    <Text fontSize={16} fontWeight="800" color={colors.text}>{stats.totalCredits}</Text>
                    <Text fontSize={11} color={colors.textTertiary}>Credits</Text>
                  </YStack>
                </XStack>
              </View>
            </YStack>
          </View>

          <YStack gap="$4" padding="$4" marginTop="$2">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Student Information</Text>
            <View style={[styles.infoCard, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
              <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
              <YStack gap="$3" padding="$4">
                {[
                  { icon: 'person', label: 'Full Name', value: studentName },
                  { icon: 'code', label: 'Control Number', value: studentProfile?.controlNumber || 'N/A' },
                  { icon: 'finger-print', label: 'Username', value: userProfile?.username || 'N/A' },
                  { icon: 'mail', label: 'Email', value: userProfile?.email || 'Not set' },
                  ...(studentProfile?.gender ? [{ icon: 'male-female', label: 'Gender', value: studentProfile.gender }] : []),
                ].map((item, i) => (
                  <XStack key={i} gap="$3" alignItems="center" paddingVertical="$2">
                    <View style={[styles.infoIcon, { backgroundColor: colors.orangeBgMedium }]}>
                      <Ionicons name={item.icon + '-outline' as any} size={18} color={colors.primary} />
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text fontSize={12} color={colors.textTertiary}>{item.label}</Text>
                      <Text fontSize={15} fontWeight="600" color={colors.text}>{item.value}</Text>
                    </YStack>
                  </XStack>
                ))}
              </YStack>
            </View>
          </YStack>

          <YStack gap="$4" padding="$4">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Attendance</Text>
            <View style={[styles.attCard, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
              <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
              <YStack gap="$4" padding="$4">
                <XStack gap="$4" justifyContent="space-between">
                  {[
                    { label: 'Present', value: presentDays, color: '#4CAF50', icon: 'checkmark-circle' },
                    { label: 'Absent', value: absentDays, color: '#F44336', icon: 'close-circle' },
                    { label: 'Late', value: lateDays, color: '#FF9800', icon: 'alert-circle' },
                  ].map((stat, i) => (
                    <View key={i} style={[styles.attStat, { backgroundColor: isDark ? 'rgba(255,255,255,0.08)' : 'rgba(255,255,255,0.4)' }]}>
                      <View style={[styles.attIcon, { backgroundColor: stat.color + '26' }]}>
                        <Ionicons name={stat.icon + '-outline' as any} size={20} color={stat.color} />
                      </View>
                      <Text fontSize={18} fontWeight="800" color={stat.color}>{stat.value}</Text>
                      <Text fontSize={11} color={colors.textTertiary}>{stat.label}</Text>
                    </View>
                  ))}
                </XStack>
                <YStack gap="$2">
                  <XStack justifyContent="space-between" alignItems="center">
                    <Text fontSize={14} color={colors.textSecondary}>Overall Rate</Text>
                    <Text fontSize={16} fontWeight="700" color={colors.primary}>{Math.round((presentDays / totalDays) * 100)}%</Text>
                  </XStack>
                  <View style={styles.progressBarContainer}>
                    <View style={[styles.progressBar, { width: `${Math.round((presentDays / totalDays) * 100)}%`, backgroundColor: colors.primary }]} />
                  </View>
                </YStack>
              </YStack>
            </View>
          </YStack>

          <YStack gap="$3" padding="$4">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Course Performance</Text>
            {courses.map((course) => {
              const perfColor = course.progress >= 80 ? '#4CAF50' : course.progress >= 60 ? '#FF9800' : '#F44336';
              return (
                <View key={course.id} style={[styles.coursePerf, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: course.color + '33' }]}>
                  <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                  <XStack gap="$3" alignItems="center" padding="$3">
                    <View style={[styles.coursePerfIcon, { backgroundColor: course.color + '26' }]}>
                      <Ionicons name={course.icon as any} size={18} color={course.color} />
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text fontSize={14} fontWeight="700" color={colors.text}>{course.code}</Text>
                      <Text fontSize={12} color={colors.textTertiary}>{course.name}</Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text fontSize={14} fontWeight="700" color={perfColor}>{course.progress}%</Text>
                      <Text fontSize={12} fontWeight="600" color={colors.textSecondary}>{course.grade}</Text>
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

const styles = StyleSheet.create({
  header: { borderBottomLeftRadius: 20, borderBottomRightRadius: 20, overflow: 'hidden', marginBottom: 16 },
  avatar: { width: 96, height: 96, borderRadius: 48, alignItems: 'center', justifyContent: 'center', borderWidth: 4, borderColor: 'rgba(255,140,0,0.3)' },
  statsRow: { flexDirection: 'row', borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.1)' },
  statDivider: { width: 1, height: 40, backgroundColor: 'rgba(0,0,0,0.1)' },
  infoCard: { borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.05)' },
  infoIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
  attCard: { borderRadius: 16, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(0,0,0,0.05)' },
  attStat: { flex: 1, padding: 12, borderRadius: 12, overflow: 'hidden', alignItems: 'center' },
  attIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center', marginBottom: 4 },
  progressBarContainer: { height: 8, backgroundColor: 'rgba(0,0,0,0.1)', borderRadius: 4, overflow: 'hidden' },
  progressBar: { height: '100%', borderRadius: 4 },
  coursePerf: { borderRadius: 12, overflow: 'hidden', borderWidth: 1 },
  coursePerfIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
});
