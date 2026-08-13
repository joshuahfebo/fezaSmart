import React, { useState } from 'react';
import { StyleSheet, View, ScrollView, TouchableOpacity } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses } from '@/data/studentData';

const schedule: Record<string, { courseId: string; time: string; room: string }[]> = {
  Monday: [
    { courseId: 'math-101', time: '08:00 - 09:30', room: 'Room 101' },
    { courseId: 'eng-101', time: '10:00 - 11:30', room: 'Room 102' },
    { courseId: 'phy-101', time: '13:00 - 14:30', room: 'Lab A' },
  ],
  Tuesday: [
    { courseId: 'chem-101', time: '09:00 - 10:30', room: 'Lab B' },
    { courseId: 'bio-101', time: '11:00 - 12:30', room: 'Room 103' },
    { courseId: 'his-101', time: '14:00 - 15:30', room: 'Room 104' },
  ],
  Wednesday: [
    { courseId: 'math-101', time: '08:00 - 09:30', room: 'Room 101' },
    { courseId: 'eng-101', time: '10:00 - 11:30', room: 'Room 102' },
    { courseId: 'phy-101', time: '13:00 - 14:30', room: 'Lab A' },
  ],
  Thursday: [
    { courseId: 'chem-101', time: '09:00 - 10:30', room: 'Lab B' },
    { courseId: 'bio-101', time: '11:00 - 12:30', room: 'Room 103' },
    { courseId: 'his-101', time: '14:00 - 15:30', room: 'Room 104' },
  ],
  Friday: [
    { courseId: 'math-101', time: '08:00 - 09:30', room: 'Room 101' },
    { courseId: 'eng-101', time: '10:00 - 11:30', room: 'Room 102' },
  ],
};

const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
const today = dayNames[new Date().getDay()];

export default function ClassesScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const { studentName } = useStudent();
  const [selectedDay, setSelectedDay] = useState(today);
  const displaySchedule = schedule[selectedDay] || [];

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        <SafeAreaView style={{ flex: 1 }}>
          <YStack gap="$4" padding="$4">
            <YStack gap="$1">
              <Text fontSize={28} fontWeight="800" color={colors.text} letterSpacing={-0.5}>My Classes</Text>
              <Text fontSize={16} color={colors.textSecondary}>{studentName}'s weekly schedule</Text>
            </YStack>

            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingRight: 24 }}>
              {(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'] as const).map((day) => (
                <TouchableOpacity key={day} onPress={() => setSelectedDay(day)} style={{ marginRight: 8 }}>
                  <View style={[styles.dayBtn, {
                    backgroundColor: selectedDay === day ? colors.primary : isDark ? 'rgba(255,255,255,0.08)' : 'rgba(0,0,0,0.04)',
                    borderColor: selectedDay === day ? colors.primary : 'rgba(0,0,0,0.1)',
                  }]}>
                    <Text fontSize={13} fontWeight={selectedDay === day ? '600' : '500'} color={selectedDay === day ? '#FFF' : colors.text}>
                      {day.slice(0, 3)}
                    </Text>
                    {day === today && <View style={[styles.todayDot, { backgroundColor: selectedDay === day ? '#FFF' : colors.primary }]} />}
                  </View>
                </TouchableOpacity>
              ))}
            </ScrollView>
          </YStack>

          <YStack gap="$2" padding="$4">
            {displaySchedule.length > 0 ? displaySchedule.map((cls, index) => {
              const course = courses.find(c => c.id === cls.courseId);
              if (!course) return null;
              return (
                <View key={index} style={[styles.classItem, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: course.color + '33' }]}>
                  <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                  <XStack gap="$3" alignItems="center" padding="$3">
                    <View style={[styles.classNum, { backgroundColor: course.color + '26' }]}>
                      <Text fontSize={14} fontWeight="800" color={course.color}>{index + 1}</Text>
                    </View>
                    <View style={[styles.classIcon, { backgroundColor: course.color + '33' }]}>
                      <Ionicons name={course.icon as any} size={18} color={course.color} />
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text fontSize={14} fontWeight="700" color={colors.text}>{course.code} - {course.name}</Text>
                      <XStack gap="$4">
                        <XStack gap={4} alignItems="center">
                          <Ionicons name="time-outline" size={14} color={colors.textTertiary} />
                          <Text fontSize={12} color={colors.textTertiary}>{cls.time}</Text>
                        </XStack>
                        <XStack gap={4} alignItems="center">
                          <Ionicons name="location-outline" size={14} color={colors.textTertiary} />
                          <Text fontSize={12} color={colors.textTertiary}>{cls.room}</Text>
                        </XStack>
                      </XStack>
                    </YStack>
                    <View style={[styles.teacherBadge, { backgroundColor: course.color + '26' }]}>
                      <Text fontSize={11} fontWeight="600" color={course.color}>{course.teacher.split(' ')[1]}</Text>
                    </View>
                  </XStack>
                </View>
              );
            }) : (
              <View style={[styles.emptyState, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.4)' }]}>
                <Ionicons name="calendar-outline" size={48} color={colors.textTertiary} />
                <Text fontSize={16} color={colors.textSecondary} marginTop="$2">No classes on this day</Text>
              </View>
            )}
          </YStack>

          <YStack gap="$3" padding="$4">
            <Text fontSize={15} fontWeight="700" color={colors.text}>All Courses</Text>
            <XStack gap="$3" flexWrap="wrap">
              {courses.map((course) => (
                <View key={course.id} style={[styles.courseMini, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: course.color + '33' }]}>
                  <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                  <YStack gap="$2" alignItems="center" padding="$3">
                    <View style={[styles.courseMiniIcon, { backgroundColor: course.color + '33' }]}>
                      <Ionicons name={course.icon as any} size={18} color={course.color} />
                    </View>
                    <Text fontSize={12} fontWeight="600" color={colors.text} numberOfLines={1}>{course.code}</Text>
                    <Text fontSize={11} color={colors.textTertiary}>{course.progress}%</Text>
                  </YStack>
                </View>
              ))}
            </XStack>
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  dayBtn: { paddingHorizontal: 16, paddingVertical: 10, borderRadius: 20, borderWidth: 1, alignItems: 'center' },
  todayDot: { width: 5, height: 5, borderRadius: 2.5, marginTop: 4 },
  classItem: { borderRadius: 12, overflow: 'hidden', borderWidth: 1 },
  classNum: { width: 28, height: 28, borderRadius: 8, alignItems: 'center', justifyContent: 'center' },
  classIcon: { width: 36, height: 36, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
  teacherBadge: { paddingHorizontal: 10, paddingVertical: 6, borderRadius: 10 },
  courseMini: { width: '31%', minWidth: 100, borderRadius: 12, overflow: 'hidden', borderWidth: 1 },
  courseMiniIcon: { width: 32, height: 32, borderRadius: 10, alignItems: 'center', justifyContent: 'center' },
  emptyState: { padding: 40, borderRadius: 20, overflow: 'hidden', alignItems: 'center', justifyContent: 'center' },
});
