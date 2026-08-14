import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  ScrollView,
  TouchableOpacity,
  RefreshControl,
} from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses } from '@/data/studentData';
import { useRole } from '@/utils/role-utils';

const schedule: Record<
  string,
  { courseId: string; time: string; room: string }[]
> = {
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

const dayNames = [
  'Sunday',
  'Monday',
  'Tuesday',
  'Wednesday',
  'Thursday',
  'Friday',
  'Saturday',
];
const today = dayNames[new Date().getDay()];

const classDirectory = [
  { id: 1, name: 'Form 1A', students: 30, teacher: 'Mr. Johnson', color: '#4CAF50' },
  { id: 2, name: 'Form 2A', students: 28, teacher: 'Ms. Amina', color: '#2196F3' },
  { id: 3, name: 'Form 3A', students: 32, teacher: 'Dr. Kimani', color: '#FF9800' },
  { id: 4, name: 'Form 4A', students: 26, teacher: 'Ms. Wanjiku', color: '#9C27B0' },
];

export default function ClassesScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const role = useRole();
  const { studentName } = useStudent();
  const [selectedDay, setSelectedDay] = useState(today);
  const [refreshing, setRefreshing] = useState(false);
  const displaySchedule = schedule[selectedDay] || [];

  const onRefresh = async () => {
    setRefreshing(true);
    setTimeout(() => setRefreshing(false), 500);
  };

  const displayName = role.isStudent
    ? studentName
    : role.label;

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView
        contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }}
        showsVerticalScrollIndicator={false}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            tintColor={colors.primary}
          />
        }
      >
        <SafeAreaView style={{ flex: 1 }}>
          {role.isStudent && (
            <StudentClasses
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              selectedDay={selectedDay}
              setSelectedDay={setSelectedDay}
              displaySchedule={displaySchedule}
            />
          )}

          {role.isParent && (
            <ParentClasses
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              selectedDay={selectedDay}
              setSelectedDay={setSelectedDay}
              displaySchedule={displaySchedule}
            />
          )}

          {(role.isSuperAdmin || role.isSchoolAdmin) && (
            <AdminClasses
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              selectedDay={selectedDay}
              setSelectedDay={setSelectedDay}
              displaySchedule={displaySchedule}
            />
          )}

          {!role.isStudent && !role.isParent && !role.canManage && (
            <GenericClasses
              colors={colors}
              isDark={isDark}
              role={role}
            />
          )}
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ STUDENT CLASSES ━━━━━━━━━━━━━━━━━━━━━ */
function StudentClasses({
  colors,
  isDark,
  role,
  displayName,
  selectedDay,
  setSelectedDay,
  displaySchedule,
}: any) {
  return (
    <>
      <YStack gap="$4" padding="$4">
        <YStack gap="$1">
          <Text
            fontSize={28}
            fontWeight="800"
            color={colors.text}
            letterSpacing={-0.5}
          >
            My Classes
          </Text>
          <XStack gap="$2" alignItems="center">
            <Text fontSize={16} color={colors.textSecondary}>
              {displayName}'s weekly schedule
            </Text>
            <View
              style={[
                styles.roleBadge,
                { backgroundColor: role.color + '22' },
              ]}
            >
              <Text
                fontSize={10}
                fontWeight="700"
                color={role.color}
              >
                {role.label}
              </Text>
            </View>
          </XStack>
        </YStack>

        <DaySelector
          selectedDay={selectedDay}
          setSelectedDay={setSelectedDay}
          colors={colors}
          isDark={isDark}
        />
      </YStack>

      <ScheduleList
        displaySchedule={displaySchedule}
        colors={colors}
        isDark={isDark}
      />

      <YStack gap="$3" padding="$4">
        <Text fontSize={15} fontWeight="700" color={colors.text}>
          All Courses
        </Text>
        <CourseGrid courses={courses} colors={colors} isDark={isDark} />
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ PARENT CLASSES ━━━━━━━━━━━━━━━━━━━━━ */
function ParentClasses({
  colors,
  isDark,
  role,
  displayName,
  selectedDay,
  setSelectedDay,
  displaySchedule,
}: any) {
  const mockChildren = [
    { id: 1, name: 'Amara Okafor', class: 'Form 3A' },
    { id: 2, name: 'Chidi Okafor', class: 'Form 1A' },
  ];
  const [selectedChild, setSelectedChild] = useState(mockChildren[0]);

  return (
    <>
      <YStack gap="$4" padding="$4">
        <YStack gap="$1">
          <Text
            fontSize={28}
            fontWeight="800"
            color={colors.text}
            letterSpacing={-0.5}
          >
            Class Schedule
          </Text>
          <Text fontSize={16} color={colors.textSecondary}>
            View your children's class timetables
          </Text>
        </YStack>

        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={{ paddingRight: 24 }}
        >
          {mockChildren.map((child) => {
            const isActive = selectedChild.id === child.id;
            return (
              <TouchableOpacity
                key={child.id}
                onPress={() => setSelectedChild(child)}
                style={{ marginRight: 10 }}
              >
                <View
                  style={[
                    styles.childSelector,
                    {
                      backgroundColor: isActive
                        ? colors.primary
                        : isDark
                          ? 'rgba(255,255,255,0.08)'
                          : 'rgba(0,0,0,0.04)',
                      borderColor: isActive
                        ? colors.primary
                        : 'rgba(0,0,0,0.1)',
                    },
                  ]}
                >
                  <View
                    style={[
                      styles.childAvatar,
                      {
                        backgroundColor: isActive
                          ? 'rgba(255,255,255,0.25)'
                          : colors.orangeBgMedium,
                      },
                    ]}
                  >
                    <Ionicons
                      name="person"
                      size={16}
                      color={isActive ? '#FFF' : colors.primary}
                    />
                  </View>
                  <YStack gap="$0" marginLeft="$2">
                    <Text
                      fontSize={14}
                      fontWeight="700"
                      color={isActive ? '#FFF' : colors.text}
                    >
                      {child.name}
                    </Text>
                    <Text
                      fontSize={11}
                      color={
                        isActive
                          ? 'rgba(255,255,255,0.7)'
                          : colors.textTertiary
                      }
                    >
                      {child.class}
                    </Text>
                  </YStack>
                </View>
              </TouchableOpacity>
            );
          })}
        </ScrollView>

        <DaySelector
          selectedDay={selectedDay}
          setSelectedDay={setSelectedDay}
          colors={colors}
          isDark={isDark}
        />
      </YStack>

      <ScheduleList
        displaySchedule={displaySchedule}
        colors={colors}
        isDark={isDark}
      />

      <YStack gap="$3" padding="$4">
        <Text fontSize={15} fontWeight="700" color={colors.text}>
          {selectedChild.class} Courses
        </Text>
        <CourseGrid courses={courses} colors={colors} isDark={isDark} />
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ ADMIN CLASSES ━━━━━━━━━━━━━━━━━━━━━ */
function AdminClasses({
  colors,
  isDark,
  role,
  displayName,
  selectedDay,
  setSelectedDay,
  displaySchedule,
}: any) {
  const [viewMode, setViewMode] = useState<'directory' | 'schedule'>(
    'directory',
  );

  return (
    <>
      <YStack gap="$4" padding="$4">
        <YStack gap="$1">
          <Text
            fontSize={28}
            fontWeight="800"
            color={colors.text}
            letterSpacing={-0.5}
          >
            Classes Management
          </Text>
          <XStack gap="$2" alignItems="center">
            <Text fontSize={16} color={colors.textSecondary}>
              {role.isSuperAdmin
                ? 'System-wide class directory'
                : 'School class management'}
            </Text>
            <View
              style={[
                styles.roleBadge,
                { backgroundColor: role.color + '22' },
              ]}
            >
              <Text
                fontSize={10}
                fontWeight="700"
                color={role.color}
              >
                {role.label}
              </Text>
            </View>
          </XStack>
        </YStack>

        <XStack gap="$2">
          {(['directory', 'schedule'] as const).map((mode) => (
            <TouchableOpacity
              key={mode}
              onPress={() => setViewMode(mode)}
              style={{ flex: 1 }}
            >
              <View
                style={[
                  styles.tabBtn,
                  {
                    backgroundColor:
                      viewMode === mode
                        ? colors.primary
                        : isDark
                          ? 'rgba(255,255,255,0.08)'
                          : 'rgba(0,0,0,0.04)',
                  },
                ]}
              >
                <Ionicons
                  name={
                    mode === 'directory'
                      ? ('list-outline' as any)
                      : ('calendar-outline' as any)
                  }
                  size={16}
                  color={viewMode === mode ? '#FFF' : colors.textSecondary}
                />
                <Text
                  fontSize={12}
                  fontWeight={viewMode === mode ? '600' : '500'}
                  color={viewMode === mode ? '#FFF' : colors.textSecondary}
                  marginLeft={4}
                >
                  {mode === 'directory' ? 'Directory' : 'Schedule'}
                </Text>
              </View>
            </TouchableOpacity>
          ))}
        </XStack>
      </YStack>

      {viewMode === 'directory' ? (
        <YStack gap="$3" padding="$4">
          <Text
            fontSize={13}
            fontWeight="600"
            color={colors.textTertiary}
            textTransform="uppercase"
            letterSpacing={1}
          >
            {classDirectory.length} Classes
          </Text>
          {classDirectory.map((cls) => (
            <TouchableOpacity key={cls.id}>
              <View
                style={[
                  styles.classDirectoryItem,
                  {
                    backgroundColor: isDark
                      ? 'rgba(255,255,255,0.06)'
                      : 'rgba(255,255,255,0.6)',
                    borderColor: cls.color + '33',
                  },
                ]}
              >
                <BlurView
                  intensity={isDark ? 12 : 6}
                  tint={isDark ? 'dark' : 'light'}
                  style={StyleSheet.absoluteFill}
                />
                <XStack gap="$3" alignItems="center" padding="$3">
                  <View
                    style={[
                      styles.classDirectoryIcon,
                      { backgroundColor: cls.color + '26' },
                    ]}
                  >
                    <Ionicons
                      name="school"
                      size={20}
                      color={cls.color}
                    />
                  </View>
                  <YStack flex={1} gap="$1">
                    <Text
                      fontSize={16}
                      fontWeight="700"
                      color={colors.text}
                    >
                      {cls.name}
                    </Text>
                    <XStack gap="$3">
                      <Text fontSize={12} color={colors.textTertiary}>
                        {cls.students} students
                      </Text>
                      <Text fontSize={12} color={colors.textTertiary}>
                        {cls.teacher}
                      </Text>
                    </XStack>
                  </YStack>
                  <Ionicons
                    name="chevron-forward"
                    size={18}
                    color={colors.textTertiary}
                  />
                </XStack>
              </View>
            </TouchableOpacity>
          ))}
        </YStack>
      ) : (
        <>
          <YStack padding="$4">
            <DaySelector
              selectedDay={selectedDay}
              setSelectedDay={setSelectedDay}
              colors={colors}
              isDark={isDark}
            />
          </YStack>
          <ScheduleList
            displaySchedule={displaySchedule}
            colors={colors}
            isDark={isDark}
          />
        </>
      )}

      <YStack gap="$3" padding="$4">
        <Text fontSize={15} fontWeight="700" color={colors.text}>
          Subject Allocation
        </Text>
        <CourseGrid courses={courses} colors={colors} isDark={isDark} />
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ GENERIC CLASSES ━━━━━━━━━━━━━━━━━━━━━ */
function GenericClasses({
  colors,
  isDark,
  role,
}: any) {
  return (
    <>
      <YStack gap="$4" padding="$4">
        <YStack gap="$1">
          <Text
            fontSize={28}
            fontWeight="800"
            color={colors.text}
            letterSpacing={-0.5}
          >
            Classes
          </Text>
          <XStack gap="$2" alignItems="center">
            <Text fontSize={16} color={colors.textSecondary}>
              {role.label} view
            </Text>
            <View
              style={[
                styles.roleBadge,
                { backgroundColor: role.color + '22' },
              ]}
            >
              <Text
                fontSize={10}
                fontWeight="700"
                color={role.color}
              >
                {role.label}
              </Text>
            </View>
          </XStack>
        </YStack>
      </YStack>

      <YStack gap="$4" padding="$4" alignItems="center" marginTop="$6">
        <View
          style={[
            styles.emptyState,
            {
              backgroundColor: isDark
                ? 'rgba(255,255,255,0.06)'
                : 'rgba(255,255,255,0.4)',
            },
          ]}
        >
          <Ionicons name="school" size={64} color={role.color + '66'} />
          <Text
            fontSize={20}
            fontWeight="700"
            color={colors.text}
            marginTop="$3"
          >
            Classes Portal
          </Text>
          <Text
            fontSize={14}
            color={colors.textSecondary}
            marginTop="$2"
            textAlign="center"
          >
            Class data and timetables will appear here.
          </Text>
        </View>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ SHARED COMPONENTS ━━━━━━━━━━━━━━━━━━━━━ */
function DaySelector({
  selectedDay,
  setSelectedDay,
  colors,
  isDark,
}: any) {
  return (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      contentContainerStyle={{ paddingRight: 24 }}
    >
      {(
        ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'] as const
      ).map((day) => (
        <TouchableOpacity
          key={day}
          onPress={() => setSelectedDay(day)}
          style={{ marginRight: 8 }}
        >
          <View
            style={[
              styles.dayBtn,
              {
                backgroundColor:
                  selectedDay === day
                    ? colors.primary
                    : isDark
                      ? 'rgba(255,255,255,0.08)'
                      : 'rgba(0,0,0,0.04)',
                borderColor:
                  selectedDay === day
                    ? colors.primary
                    : 'rgba(0,0,0,0.1)',
              },
            ]}
          >
            <Text
              fontSize={13}
              fontWeight={selectedDay === day ? '600' : '500'}
              color={selectedDay === day ? '#FFF' : colors.text}
            >
              {day.slice(0, 3)}
            </Text>
            {day === today && (
              <View
                style={[
                  styles.todayDot,
                  {
                    backgroundColor:
                      selectedDay === day ? '#FFF' : colors.primary,
                  },
                ]}
              />
            )}
          </View>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );
}

function ScheduleList({
  displaySchedule,
  colors,
  isDark,
}: any) {
  return (
    <YStack gap="$2" padding="$4">
      {displaySchedule.length > 0 ? (
        displaySchedule.map((cls: any, index: number) => {
          const course = courses.find((c) => c.id === cls.courseId);
          if (!course) return null;
          return (
            <View
              key={index}
              style={[
                styles.classItem,
                {
                  backgroundColor: isDark
                    ? 'rgba(255,255,255,0.06)'
                    : 'rgba(255,255,255,0.6)',
                  borderColor: course.color + '33',
                },
              ]}
            >
              <BlurView
                intensity={isDark ? 12 : 6}
                tint={isDark ? 'dark' : 'light'}
                style={StyleSheet.absoluteFill}
              />
              <XStack gap="$3" alignItems="center" padding="$3">
                <View
                  style={[
                    styles.classNum,
                    { backgroundColor: course.color + '26' },
                  ]}
                >
                  <Text
                    fontSize={14}
                    fontWeight="800"
                    color={course.color}
                  >
                    {index + 1}
                  </Text>
                </View>
                <View
                  style={[
                    styles.classIcon,
                    { backgroundColor: course.color + '33' },
                  ]}
                >
                  <Ionicons
                    name={course.icon as any}
                    size={18}
                    color={course.color}
                  />
                </View>
                <YStack flex={1} gap="$1">
                  <Text
                    fontSize={14}
                    fontWeight="700"
                    color={colors.text}
                  >
                    {course.code} - {course.name}
                  </Text>
                  <XStack gap="$4">
                    <XStack gap={4} alignItems="center">
                      <Ionicons
                        name="time-outline"
                        size={14}
                        color={colors.textTertiary}
                      />
                      <Text fontSize={12} color={colors.textTertiary}>
                        {cls.time}
                      </Text>
                    </XStack>
                    <XStack gap={4} alignItems="center">
                      <Ionicons
                        name="location-outline"
                        size={14}
                        color={colors.textTertiary}
                      />
                      <Text fontSize={12} color={colors.textTertiary}>
                        {cls.room}
                      </Text>
                    </XStack>
                  </XStack>
                </YStack>
                <View
                  style={[
                    styles.teacherBadge,
                    { backgroundColor: course.color + '26' },
                  ]}
                >
                  <Text
                    fontSize={11}
                    fontWeight="600"
                    color={course.color}
                  >
                    {course.teacher.split(' ')[1]}
                  </Text>
                </View>
              </XStack>
            </View>
          );
        })
      ) : (
        <View
          style={[
            styles.emptyState,
            {
              backgroundColor: isDark
                ? 'rgba(255,255,255,0.06)'
                : 'rgba(255,255,255,0.4)',
            },
          ]}
        >
          <Ionicons
            name="calendar-outline"
            size={48}
            color={colors.textTertiary}
          />
          <Text
            fontSize={16}
            color={colors.textSecondary}
            marginTop="$2"
          >
            No classes on this day
          </Text>
        </View>
      )}
    </YStack>
  );
}

function CourseGrid({
  courses: courseList,
  colors,
  isDark,
}: any) {
  return (
    <XStack gap="$3" flexWrap="wrap">
      {courseList.map((course: any) => (
        <View
          key={course.id}
          style={[
            styles.courseMini,
            {
              backgroundColor: isDark
                ? 'rgba(255,255,255,0.06)'
                : 'rgba(255,255,255,0.6)',
              borderColor: course.color + '33',
            },
          ]}
        >
          <BlurView
            intensity={isDark ? 12 : 6}
            tint={isDark ? 'dark' : 'light'}
            style={StyleSheet.absoluteFill}
          />
          <YStack gap="$2" alignItems="center" padding="$3">
            <View
              style={[
                styles.courseMiniIcon,
                { backgroundColor: course.color + '33' },
              ]}
            >
              <Ionicons
                name={course.icon as any}
                size={18}
                color={course.color}
              />
            </View>
            <Text
              fontSize={12}
              fontWeight="600"
              color={colors.text}
              numberOfLines={1}
            >
              {course.code}
            </Text>
            <Text fontSize={11} color={colors.textTertiary}>
              {course.progress}%
            </Text>
          </YStack>
        </View>
      ))}
    </XStack>
  );
}

const styles = StyleSheet.create({
  roleBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 10,
  },
  dayBtn: {
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderRadius: 20,
    borderWidth: 1,
    alignItems: 'center',
  },
  todayDot: {
    width: 5,
    height: 5,
    borderRadius: 2.5,
    marginTop: 4,
  },
  classItem: {
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
  },
  classNum: {
    width: 28,
    height: 28,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  classIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  teacherBadge: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 10,
  },
  courseMini: {
    width: '31%',
    minWidth: 100,
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
  },
  courseMiniIcon: {
    width: 32,
    height: 32,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  classDirectoryItem: {
    borderRadius: 14,
    overflow: 'hidden',
    borderWidth: 1,
  },
  classDirectoryIcon: {
    width: 44,
    height: 44,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  tabBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderRadius: 12,
  },
  childSelector: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderRadius: 16,
    borderWidth: 1,
  },
  childAvatar: {
    width: 32,
    height: 32,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emptyState: {
    padding: 40,
    borderRadius: 20,
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
