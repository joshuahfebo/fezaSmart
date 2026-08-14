import React, { useEffect, useState } from 'react';
import {
  StyleSheet,
  View,
  ScrollView,
  Dimensions,
  ActivityIndicator,
  TouchableOpacity,
  RefreshControl,
} from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { LinearGradient } from 'expo-linear-gradient';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { useResults } from '@/contexts/results-context';
import { useAuth } from '@/contexts/auth-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses, recentGrades, getStudentStats, weeklyAttendance } from '@/data/studentData';
import {
  useRole,
  ROLE_LABELS,
  ROLE_COLORS,
  ROLE_ICONS,
  type UserRole,
} from '@/utils/role-utils';

const { width } = Dimensions.get('window');

export default function DashboardScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const role = useRole();
  const { studentProfile, studentName, loading: studentLoading } = useStudent();
  const {
    currentStudentResults,
    fetchStudentResults,
    results,
    fetchAllResults,
    loading: resultsLoading,
  } = useResults();
  const { user, userProfile } = useAuth();
  const [refreshing, setRefreshing] = useState(false);
  const [stats, setStats] = useState(getStudentStats());

  useEffect(() => {
    if (role.isStudent && studentProfile?.id) {
      fetchStudentResults(studentProfile.id);
    } else if (role.canViewAll) {
      fetchAllResults({ page: 0, size: 50 });
    }
  }, [studentProfile?.id, role.primaryRole]);

  useEffect(() => {
    if (currentStudentResults.length > 0) {
      const avgScore =
        currentStudentResults.reduce(
          (sum, r) => sum + (r.averagePercentage || 0),
          0,
        ) / currentStudentResults.length;
      setStats((prev) => ({ ...prev, gpa: avgScore / 25 }));
    }
  }, [currentStudentResults]);

  const onRefresh = async () => {
    setRefreshing(true);
    try {
      if (role.isStudent && studentProfile?.id) {
        await fetchStudentResults(studentProfile.id);
      } else if (role.canViewAll) {
        await fetchAllResults({ page: 0, size: 50 });
      }
    } catch {}
    setRefreshing(false);
  };

  if (studentLoading && role.isStudent) {
    return (
      <View style={[styles.centered, { backgroundColor: colors.background }]}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text fontSize={14} color={colors.textSecondary} marginTop="$2">
          Loading dashboard...
        </Text>
      </View>
    );
  }

  const displayName =
    role.isStudent
      ? studentName
      : role.isParent
        ? `${(userProfile as any)?.firstName || ''} ${(userProfile as any)?.lastName || ''}`.trim() || 'Parent'
        : `${(userProfile as any)?.firstName || ''} ${(userProfile as any)?.lastName || ''}`.trim() || userProfile?.username || 'Admin';

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
            <StudentDashboard
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              stats={stats}
              currentStudentResults={currentStudentResults}
              studentProfile={studentProfile}
              resultsLoading={resultsLoading}
            />
          )}
          {role.isParent && (
            <ParentDashboard
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              stats={stats}
            />
          )}
          {role.isSuperAdmin && (
            <SuperAdminDashboard
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              results={results}
              resultsLoading={resultsLoading}
            />
          )}
          {role.isSchoolAdmin && (
            <SchoolAdminDashboard
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
              results={results}
              resultsLoading={resultsLoading}
            />
          )}
          {!role.isStudent && !role.isParent && !role.isSuperAdmin && !role.isSchoolAdmin && (
            <GenericDashboard
              colors={colors}
              isDark={isDark}
              role={role}
              displayName={displayName}
            />
          )}
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ STUDENT DASHBOARD ━━━━━━━━━━━━━━━━━━━━━ */
function StudentDashboard({
  colors,
  isDark,
  role,
  displayName,
  stats,
  currentStudentResults,
  studentProfile,
  resultsLoading,
}: any) {
  const attendanceStreak = weeklyAttendance.filter(
    (a) => a.status === 'present',
  ).length;

  return (
    <>
      <YStack gap="$4" padding="$4">
        <XStack justifyContent="space-between" alignItems="center">
          <YStack gap="$1">
            <Text
              fontSize={28}
              fontWeight="800"
              color={colors.text}
              letterSpacing={-0.5}
            >
              Welcome back
            </Text>
            <XStack gap="$2" alignItems="center">
              <Text fontSize={16} color={colors.textSecondary}>
                {displayName}
              </Text>
              <View
                style={[
                  styles.roleBadge,
                  { backgroundColor: role.color + '22' },
                ]}
              >
                <Ionicons
                  name={role.icon as any}
                  size={11}
                  color={role.color}
                />
                <Text
                  fontSize={10}
                  fontWeight="700"
                  color={role.color}
                  marginLeft={3}
                >
                  {role.label}
                </Text>
              </View>
            </XStack>
          </YStack>
          <View
            style={[styles.avatar, { backgroundColor: colors.orangeBgMedium }]}
          >
            <Ionicons name="person" size={24} color={colors.primary} />
          </View>
        </XStack>

        <XStack gap="$3" justifyContent="space-between">
          <StatCard
            icon="trophy"
            label="GPA"
            value={stats.gpa.toFixed(2)}
            subtext="/4.0"
            color="#FF8C00"
            colors={colors}
            isDark={isDark}
          />
          <StatCard
            icon="checkmark-circle"
            label="Attendance"
            value={stats.attendanceRate + '%'}
            subtext={attendanceStreak + ' day streak'}
            color="#4CAF50"
            colors={colors}
            isDark={isDark}
          />
          <StatCard
            icon="book"
            label="Courses"
            value={stats.totalCourses}
            subtext={stats.completedCourses + ' completed'}
            color="#2196F3"
            colors={colors}
            isDark={isDark}
          />
        </XStack>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Your Progress
        </Text>
        <GlassCard colors={colors} isDark={isDark}>
          <YStack gap="$3" padding="$4">
            <XStack justifyContent="space-between" alignItems="center">
              <Text fontSize={16} fontWeight="700" color={colors.text}>
                Overall Performance
              </Text>
              <View
                style={[
                  styles.badge,
                  { backgroundColor: colors.orangeBg },
                ]}
              >
                <Text
                  fontSize={12}
                  fontWeight="600"
                  color={colors.primary}
                >
                  {stats.gpa >= 3.5
                    ? 'Excellent'
                    : stats.gpa >= 3.0
                      ? 'Good'
                      : 'Average'}
                </Text>
              </View>
            </XStack>
            <YStack gap="$2">
              <XStack justifyContent="space-between">
                <Text fontSize={14} color={colors.textSecondary}>
                  Semester Progress
                </Text>
                <Text
                  fontSize={14}
                  fontWeight="600"
                  color={colors.primary}
                >
                  {Math.round(
                    courses.reduce((sum, c) => sum + c.progress, 0) /
                      courses.length,
                  )}
                  %
                </Text>
              </XStack>
              <ProgressBar
                value={
                  courses.reduce((sum, c) => sum + c.progress, 0) /
                  courses.length
                }
                color={colors.primary}
              />
            </YStack>
            <XStack gap="$3" flexWrap="wrap">
              {courses.slice(0, 4).map((course) => (
                <View key={course.id} style={styles.courseMiniCard}>
                  <View
                    style={[
                      styles.courseIcon,
                      { backgroundColor: course.color + '33' },
                    ]}
                  >
                    <Ionicons
                      name={course.icon as any}
                      size={16}
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
                </View>
              ))}
            </XStack>
          </YStack>
        </GlassCard>
      </YStack>

      {studentProfile && (
        <YStack gap="$4" padding="$4">
          <Text fontSize={18} fontWeight="700" color={colors.text}>
            Student Info
          </Text>
          <GlassCard colors={colors} isDark={isDark}>
            <YStack gap="$2" padding="$4">
              <InfoRow
                label="Name"
                value={`${studentProfile.firstName} ${studentProfile.lastName}`}
                colors={colors}
              />
              <InfoRow
                label="Control Number"
                value={studentProfile.controlNumber}
                colors={colors}
              />
              {studentProfile.gender && (
                <InfoRow
                  label="Gender"
                  value={studentProfile.gender}
                  colors={colors}
                />
              )}
            </YStack>
          </GlassCard>
        </YStack>
      )}

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Recent Grades
        </Text>
        {recentGrades.slice(0, 3).map((grade) => {
          const gradeColor =
            grade.percentage >= 80
              ? '#4CAF50'
              : grade.percentage >= 60
                ? '#FF9800'
                : '#F44336';
          return (
            <GlassCard key={grade.id} colors={colors} isDark={isDark}>
              <XStack gap="$3" alignItems="center" padding="$3">
                <YStack flex={1} gap="$1">
                  <Text
                    fontSize={15}
                    fontWeight="700"
                    color={colors.text}
                  >
                    {grade.courseName}
                  </Text>
                  <Text fontSize={12} color={colors.textTertiary}>
                    {grade.type} - {grade.date}
                  </Text>
                </YStack>
                <YStack alignItems="flex-end" gap="$1">
                  <Text
                    fontSize={16}
                    fontWeight="800"
                    color={gradeColor}
                  >
                    {grade.score}/{grade.maxScore}
                  </Text>
                  <View
                    style={[
                      styles.badge,
                      { backgroundColor: gradeColor + '26' },
                    ]}
                  >
                    <Text
                      fontSize={11}
                      fontWeight="600"
                      color={gradeColor}
                    >
                      {grade.grade}
                    </Text>
                  </View>
                </YStack>
              </XStack>
            </GlassCard>
          );
        })}
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ PARENT DASHBOARD ━━━━━━━━━━━━━━━━━━━━━ */
function ParentDashboard({
  colors,
  isDark,
  role,
  displayName,
  stats,
}: any) {
  const mockChildren = [
    { id: 1, name: 'Amara Okafor', class: 'Form 3A', avg: 82, attendance: 94 },
    { id: 2, name: 'Chidi Okafor', class: 'Form 1A', avg: 76, attendance: 88 },
  ];
  const [selectedChild, setSelectedChild] = useState(mockChildren[0]);

  return (
    <>
      <YStack gap="$4" padding="$4">
        <XStack justifyContent="space-between" alignItems="center">
          <YStack gap="$1">
            <Text
              fontSize={28}
              fontWeight="800"
              color={colors.text}
              letterSpacing={-0.5}
            >
              Parent Dashboard
            </Text>
            <XStack gap="$2" alignItems="center">
              <Text fontSize={16} color={colors.textSecondary}>
                {displayName}
              </Text>
              <View
                style={[
                  styles.roleBadge,
                  { backgroundColor: role.color + '22' },
                ]}
              >
                <Ionicons
                  name={role.icon as any}
                  size={11}
                  color={role.color}
                />
                <Text
                  fontSize={10}
                  fontWeight="700"
                  color={role.color}
                  marginLeft={3}
                >
                  {role.label}
                </Text>
              </View>
            </XStack>
          </YStack>
          <View
            style={[styles.avatar, { backgroundColor: colors.orangeBgMedium }]}
          >
            <Ionicons name="heart" size={24} color={colors.primary} />
          </View>
        </XStack>

        <YStack gap="$2">
          <Text
            fontSize={13}
            fontWeight="600"
            color={colors.textTertiary}
            textTransform="uppercase"
            letterSpacing={1}
          >
            Your Children
          </Text>
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
                          isActive ? 'rgba(255,255,255,0.7)' : colors.textTertiary
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
        </YStack>
      </YStack>

      <YStack gap="$3" padding="$4">
        <XStack gap="$3" justifyContent="space-between">
          <StatCard
            icon="trophy"
            label="Avg Score"
            value={selectedChild.avg + '%'}
            subtext={selectedChild.avg >= 80 ? 'Excellent' : 'Good'}
            color="#FF8C00"
            colors={colors}
            isDark={isDark}
          />
          <StatCard
            icon="checkmark-circle"
            label="Attendance"
            value={selectedChild.attendance + '%'}
            subtext="This term"
            color="#4CAF50"
            colors={colors}
            isDark={isDark}
          />
          <StatCard
            icon="school"
            label="Class"
            value={selectedChild.class}
            subtext="Current"
            color="#2196F3"
            colors={colors}
            isDark={isDark}
          />
        </XStack>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Performance Overview
        </Text>
        <GlassCard colors={colors} isDark={isDark}>
          <YStack gap="$3" padding="$4">
            <XStack justifyContent="space-between" alignItems="center">
              <Text fontSize={16} fontWeight="700" color={colors.text}>
                {selectedChild.name}
              </Text>
              <View
                style={[
                  styles.badge,
                  {
                    backgroundColor:
                      selectedChild.avg >= 80 ? '#4CAF5026' : '#FF980026',
                  },
                ]}
              >
                <Text
                  fontSize={12}
                  fontWeight="600"
                  color={
                    selectedChild.avg >= 80 ? '#4CAF50' : '#FF9800'
                  }
                >
                  {selectedChild.avg >= 80 ? 'On Track' : 'Needs Support'}
                </Text>
              </View>
            </XStack>
            <YStack gap="$2">
              <XStack justifyContent="space-between">
                <Text fontSize={14} color={colors.textSecondary}>
                  Average Score
                </Text>
                <Text
                  fontSize={14}
                  fontWeight="600"
                  color={colors.primary}
                >
                  {selectedChild.avg}%
                </Text>
              </XStack>
              <ProgressBar value={selectedChild.avg} color={colors.primary} />
            </YStack>
            <YStack gap="$2">
              <XStack justifyContent="space-between">
                <Text fontSize={14} color={colors.textSecondary}>
                  Attendance
                </Text>
                <Text
                  fontSize={14}
                  fontWeight="600"
                  color="#4CAF50"
                >
                  {selectedChild.attendance}%
                </Text>
              </XStack>
              <ProgressBar
                value={selectedChild.attendance}
                color="#4CAF50"
              />
            </YStack>
          </YStack>
        </GlassCard>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Quick Actions
        </Text>
        <XStack gap="$3" flexWrap="wrap">
          {[
            { icon: 'document-text', label: 'Report Card', color: '#FF8C00' },
            { icon: 'chatbubble', label: 'Message Teacher', color: '#2196F3' },
            { icon: 'calendar', label: 'School Events', color: '#4CAF50' },
            { icon: 'wallet', label: 'Fee Balance', color: '#E91E63' },
          ].map((action, i) => (
            <TouchableOpacity
              key={i}
              style={[
                styles.actionCard,
                {
                  backgroundColor: isDark
                    ? 'rgba(255,255,255,0.06)'
                    : 'rgba(255,255,255,0.6)',
                  borderColor: action.color + '33',
                },
              ]}
            >
              <View
                style={[
                  styles.actionIcon,
                  { backgroundColor: action.color + '22' },
                ]}
              >
                <Ionicons
                  name={action.icon as any}
                  size={20}
                  color={action.color}
                />
              </View>
              <Text
                fontSize={12}
                fontWeight="600"
                color={colors.text}
                marginTop="$2"
              >
                {action.label}
              </Text>
            </TouchableOpacity>
          ))}
        </XStack>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ SUPER ADMIN DASHBOARD ━━━━━━━━━━━━━━━━━━━━━ */
function SuperAdminDashboard({
  colors,
  isDark,
  role,
  displayName,
  results,
  resultsLoading,
}: any) {
  const systemMetrics = [
    {
      icon: 'school',
      label: 'Schools',
      value: '12',
      color: '#FF8C00',
      delta: '+2 this month',
    },
    {
      icon: 'people',
      label: 'Users',
      value: '1,847',
      color: '#4CAF50',
      delta: '+156 active',
    },
    {
      icon: 'book',
      label: 'Students',
      value: '1,523',
      color: '#2196F3',
      delta: '+89 enrolled',
    },
    {
      icon: 'briefcase',
      label: 'Staff',
      value: '324',
      color: '#9C27B0',
      delta: '12 on leave',
    },
  ];

  const systemAlerts = [
    {
      icon: 'checkmark-circle',
      text: 'Database backup completed',
      time: '2h ago',
      color: '#4CAF50',
    },
    {
      icon: 'warning',
      text: 'API response time elevated',
      time: '4h ago',
      color: '#FF9800',
    },
    {
      icon: 'information-circle',
      text: 'System update available v2.4.1',
      time: '1d ago',
      color: '#2196F3',
    },
  ];

  return (
    <>
      <YStack gap="$4" padding="$4">
        <XStack justifyContent="space-between" alignItems="center">
          <YStack gap="$1">
            <Text
              fontSize={28}
              fontWeight="800"
              color={colors.text}
              letterSpacing={-0.5}
            >
              System Overview
            </Text>
            <XStack gap="$2" alignItems="center">
              <Text fontSize={16} color={colors.textSecondary}>
                {displayName}
              </Text>
              <View
                style={[
                  styles.roleBadge,
                  { backgroundColor: role.color + '22' },
                ]}
              >
                <Ionicons
                  name={role.icon as any}
                  size={11}
                  color={role.color}
                />
                <Text
                  fontSize={10}
                  fontWeight="700"
                  color={role.color}
                  marginLeft={3}
                >
                  {role.label}
                </Text>
              </View>
            </XStack>
          </YStack>
          <View
            style={[styles.avatar, { backgroundColor: role.color + '22' }]}
          >
            <Ionicons name={role.icon as any} size={24} color={role.color} />
          </View>
        </XStack>

        <XStack gap="$3" justifyContent="space-between">
          {systemMetrics.slice(0, 3).map((m, i) => (
            <StatCard
              key={i}
              icon={m.icon}
              label={m.label}
              value={m.value}
              subtext={m.delta}
              color={m.color}
              colors={colors}
              isDark={isDark}
            />
          ))}
        </XStack>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          System Health
        </Text>
        <GlassCard colors={colors} isDark={isDark}>
          <YStack gap="$3" padding="$4">
            {[
              { label: 'API Server', status: 'Operational', color: '#4CAF50', icon: 'checkmark-circle' },
              { label: 'Database', status: 'Healthy', color: '#4CAF50', icon: 'server' },
              { label: 'Authentication', status: 'Active', color: '#4CAF50', icon: 'key' },
              { label: 'Storage', status: '78% Used', color: '#FF9800', icon: 'cloud' },
            ].map((item, i) => (
              <XStack
                key={i}
                justifyContent="space-between"
                alignItems="center"
                paddingVertical="$1"
              >
                <XStack gap="$2" alignItems="center">
                  <Ionicons
                    name={item.icon as any}
                    size={18}
                    color={item.color}
                  />
                  <Text fontSize={14} color={colors.text}>
                    {item.label}
                  </Text>
                </XStack>
                <View
                  style={[
                    styles.statusBadge,
                    { backgroundColor: item.color + '22' },
                  ]}
                >
                  <Text
                    fontSize={11}
                    fontWeight="600"
                    color={item.color}
                  >
                    {item.status}
                  </Text>
                </View>
              </XStack>
            ))}
          </YStack>
        </GlassCard>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          System Alerts
        </Text>
        {systemAlerts.map((alert, i) => (
          <GlassCard key={i} colors={colors} isDark={isDark}>
            <XStack gap="$3" alignItems="center" padding="$3">
              <View
                style={[
                  styles.alertIcon,
                  { backgroundColor: alert.color + '22' },
                ]}
              >
                <Ionicons
                  name={alert.icon as any}
                  size={18}
                  color={alert.color}
                />
              </View>
              <YStack flex={1} gap="$1">
                <Text fontSize={14} fontWeight="600" color={colors.text}>
                  {alert.text}
                </Text>
                <Text fontSize={11} color={colors.textTertiary}>
                  {alert.time}
                </Text>
              </YStack>
            </XStack>
          </GlassCard>
        ))}
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Quick Actions
        </Text>
        <XStack gap="$3" flexWrap="wrap">
          {[
            { icon: 'people', label: 'Manage Users', color: '#FF8C00' },
            { icon: 'add-circle', label: 'Register School', color: '#4CAF50' },
            { icon: 'analytics', label: 'Analytics', color: '#2196F3' },
            { icon: 'settings', label: 'System Config', color: '#9C27B0' },
            { icon: 'database', label: 'Data Seeder', color: '#FF5722' },
            { icon: 'download', label: 'Export Data', color: '#607D8B' },
          ].map((action, i) => (
            <TouchableOpacity
              key={i}
              style={[
                styles.actionCard,
                {
                  backgroundColor: isDark
                    ? 'rgba(255,255,255,0.06)'
                    : 'rgba(255,255,255,0.6)',
                  borderColor: action.color + '33',
                },
              ]}
            >
              <View
                style={[
                  styles.actionIcon,
                  { backgroundColor: action.color + '22' },
                ]}
              >
                <Ionicons
                  name={action.icon as any}
                  size={20}
                  color={action.color}
                />
              </View>
              <Text
                fontSize={12}
                fontWeight="600"
                color={colors.text}
                marginTop="$2"
                numberOfLines={1}
              >
                {action.label}
              </Text>
            </TouchableOpacity>
          ))}
        </XStack>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ SCHOOL ADMIN DASHBOARD ━━━━━━━━━━━━━━━━━━━━━ */
function SchoolAdminDashboard({
  colors,
  isDark,
  role,
  displayName,
  results,
  resultsLoading,
}: any) {
  const schoolStats = [
    { icon: 'book', label: 'Students', value: '324', color: '#2196F3' },
    { icon: 'briefcase', label: 'Staff', value: '42', color: '#9C27B0' },
    { icon: 'checkmark-circle', label: 'Attendance', value: '92%', color: '#4CAF50' },
    { icon: 'trophy', label: 'Pass Rate', value: '78%', color: '#FF8C00' },
  ];

  return (
    <>
      <YStack gap="$4" padding="$4">
        <XStack justifyContent="space-between" alignItems="center">
          <YStack gap="$1">
            <Text
              fontSize={28}
              fontWeight="800"
              color={colors.text}
              letterSpacing={-0.5}
            >
              School Dashboard
            </Text>
            <XStack gap="$2" alignItems="center">
              <Text fontSize={16} color={colors.textSecondary}>
                {displayName}
              </Text>
              <View
                style={[
                  styles.roleBadge,
                  { backgroundColor: role.color + '22' },
                ]}
              >
                <Ionicons
                  name={role.icon as any}
                  size={11}
                  color={role.color}
                />
                <Text
                  fontSize={10}
                  fontWeight="700"
                  color={role.color}
                  marginLeft={3}
                >
                  {role.label}
                </Text>
              </View>
            </XStack>
          </YStack>
          <View
            style={[styles.avatar, { backgroundColor: role.color + '22' }]}
          >
            <Ionicons name={role.icon as any} size={24} color={role.color} />
          </View>
        </XStack>

        <XStack gap="$3" justifyContent="space-between">
          {schoolStats.map((s, i) => (
            <StatCard
              key={i}
              icon={s.icon}
              label={s.label}
              value={s.value}
              subtext=""
              color={s.color}
              colors={colors}
              isDark={isDark}
            />
          ))}
        </XStack>
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Class Performance
        </Text>
        {['Form 4A', 'Form 3A', 'Form 2A', 'Form 1A'].map((cls, i) => {
          const avg = [82, 76, 69, 72][i];
          const color =
            avg >= 80 ? '#4CAF50' : avg >= 70 ? '#FF9800' : '#F44336';
          return (
            <GlassCard key={cls} colors={colors} isDark={isDark}>
              <XStack gap="$3" alignItems="center" padding="$3">
                <View
                  style={[
                    styles.classNum,
                    { backgroundColor: color + '26' },
                  ]}
                >
                  <Text fontSize={14} fontWeight="800" color={color}>
                    {i + 1}
                  </Text>
                </View>
                <YStack flex={1} gap="$1">
                  <Text
                    fontSize={14}
                    fontWeight="700"
                    color={colors.text}
                  >
                    {cls}
                  </Text>
                  <XStack gap="$3">
                    <Text fontSize={12} color={colors.textTertiary}>
                      30 students
                    </Text>
                    <Text fontSize={12} color={colors.textTertiary}>
                      Avg: {avg}%
                    </Text>
                  </XStack>
                </YStack>
                <View
                  style={[styles.badge, { backgroundColor: color + '26' }]}
                >
                  <Text fontSize={11} fontWeight="600" color={color}>
                    {avg >= 80
                      ? 'Excellent'
                      : avg >= 70
                        ? 'Good'
                        : 'Average'}
                  </Text>
                </View>
              </XStack>
            </GlassCard>
          );
        })}
      </YStack>

      {role.canComputeResults && (
        <YStack gap="$4" padding="$4">
          <Text fontSize={18} fontWeight="700" color={colors.text}>
            Admin Tools
          </Text>
          <XStack gap="$3" flexWrap="wrap">
            {[
              { icon: 'calculator', label: 'Compute Results', color: '#FF8C00' },
              { icon: 'person-add', label: 'Add Student', color: '#4CAF50' },
              { icon: 'document-text', label: 'Report Cards', color: '#2196F3' },
              { icon: 'analytics', label: 'Analytics', color: '#9C27B0' },
            ].map((action, i) => (
              <TouchableOpacity
                key={i}
                style={[
                  styles.actionCard,
                  {
                    backgroundColor: isDark
                      ? 'rgba(255,255,255,0.06)'
                      : 'rgba(255,255,255,0.6)',
                    borderColor: action.color + '33',
                  },
                ]}
              >
                <View
                  style={[
                    styles.actionIcon,
                    { backgroundColor: action.color + '22' },
                  ]}
                >
                  <Ionicons
                    name={action.icon as any}
                    size={20}
                    color={action.color}
                  />
                </View>
                <Text
                  fontSize={12}
                  fontWeight="600"
                  color={colors.text}
                  marginTop="$2"
                  numberOfLines={1}
                >
                  {action.label}
                </Text>
              </TouchableOpacity>
            ))}
          </XStack>
        </YStack>
      )}
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ GENERIC DASHBOARD ━━━━━━━━━━━━━━━━━━━━━ */
function GenericDashboard({
  colors,
  isDark,
  role,
  displayName,
}: any) {
  return (
    <>
      <YStack gap="$4" padding="$4">
        <XStack justifyContent="space-between" alignItems="center">
          <YStack gap="$1">
            <Text
              fontSize={28}
              fontWeight="800"
              color={colors.text}
              letterSpacing={-0.5}
            >
              Welcome
            </Text>
            <XStack gap="$2" alignItems="center">
              <Text fontSize={16} color={colors.textSecondary}>
                {displayName}
              </Text>
              <View
                style={[
                  styles.roleBadge,
                  { backgroundColor: role.color + '22' },
                ]}
              >
                <Ionicons
                  name={role.icon as any}
                  size={11}
                  color={role.color}
                />
                <Text
                  fontSize={10}
                  fontWeight="700"
                  color={role.color}
                  marginLeft={3}
                >
                  {role.label}
                </Text>
              </View>
            </XStack>
          </YStack>
          <View
            style={[styles.avatar, { backgroundColor: role.color + '22' }]}
          >
            <Ionicons name={role.icon as any} size={24} color={role.color} />
          </View>
        </XStack>
      </YStack>

      <YStack gap="$4" padding="$4" alignItems="center" marginTop="$6">
        <View
          style={[
            styles.emptyState,
            {
              backgroundColor: isDark
                ? 'rgba(255,255,255,0.06)'
                : 'rgba(255,255,255,0.4)',
              width: width * 0.8,
            },
          ]}
        >
          <Ionicons name={role.icon as any} size={64} color={role.color + '66'} />
          <Text
            fontSize={20}
            fontWeight="700"
            color={colors.text}
            marginTop="$3"
          >
            {role.label} Portal
          </Text>
          <Text
            fontSize={14}
            color={colors.textSecondary}
            marginTop="$2"
            textAlign="center"
          >
            Your role-specific tools and data will appear here once connected to the backend.
          </Text>
        </View>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ SHARED COMPONENTS ━━━━━━━━━━━━━━━━━━━━━ */
function StatCard({
  icon,
  label,
  value,
  subtext,
  color,
  colors,
  isDark,
}: {
  icon: string;
  label: string;
  value: string | number;
  subtext: string;
  color: string;
  colors: any;
  isDark: boolean;
}) {
  return (
    <View
      style={[
        styles.statCard,
        {
          backgroundColor: isDark
            ? 'rgba(255,255,255,0.06)'
            : 'rgba(255,255,255,0.6)',
        },
      ]}
    >
      <BlurView
        intensity={isDark ? 12 : 6}
        tint={isDark ? 'dark' : 'light'}
        style={StyleSheet.absoluteFill}
      />
      <YStack gap="$1" padding="$3" alignItems="center">
        <View style={[styles.statIcon, { backgroundColor: color + '26' }]}>
          <Ionicons name={icon as any} size={18} color={color} />
        </View>
        <Text fontSize={18} fontWeight="800" color={color}>
          {value}
        </Text>
        <Text fontSize={10} color={colors.textTertiary}>
          {label}
        </Text>
        {subtext ? (
          <Text fontSize={9} color={colors.textTertiary}>
            {subtext}
          </Text>
        ) : null}
      </YStack>
    </View>
  );
}

function GlassCard({
  colors,
  isDark,
  children,
}: {
  colors: any;
  isDark: boolean;
  children: React.ReactNode;
}) {
  return (
    <View
      style={[
        styles.card,
        {
          backgroundColor: isDark
            ? 'rgba(255,255,255,0.06)'
            : 'rgba(255,255,255,0.6)',
        },
      ]}
    >
      <BlurView
        intensity={isDark ? 12 : 6}
        tint={isDark ? 'dark' : 'light'}
        style={StyleSheet.absoluteFill}
      />
      {children}
    </View>
  );
}

function InfoRow({
  label,
  value,
  colors,
}: {
  label: string;
  value: string;
  colors: any;
}) {
  return (
    <XStack justifyContent="space-between">
      <Text fontSize={14} color={colors.textSecondary}>
        {label}
      </Text>
      <Text fontSize={14} fontWeight="600" color={colors.text}>
        {value}
      </Text>
    </XStack>
  );
}

function ProgressBar({ value, color }: { value: number; color: string }) {
  return (
    <View style={styles.progressBarContainer}>
      <View
        style={[
          styles.progressBar,
          {
            width: `${Math.min(value, 100)}%`,
            backgroundColor: color,
          },
        ]}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  centered: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  avatar: {
    width: 48,
    height: 48,
    borderRadius: 24,
    alignItems: 'center',
    justifyContent: 'center',
  },
  roleBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 10,
  },
  statCard: {
    flex: 1,
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.05)',
  },
  statIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  card: {
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.05)',
  },
  badge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  statusBadge: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 8,
  },
  courseMiniCard: {
    width: '23%',
    minWidth: 70,
    alignItems: 'center',
    gap: 4,
  },
  courseIcon: {
    width: 32,
    height: 32,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  classNum: {
    width: 32,
    height: 32,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  progressBarContainer: {
    height: 8,
    backgroundColor: 'rgba(0,0,0,0.1)',
    borderRadius: 4,
    overflow: 'hidden',
  },
  progressBar: {
    height: '100%',
    borderRadius: 4,
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
  actionCard: {
    width: '31%',
    minWidth: 100,
    borderRadius: 14,
    overflow: 'hidden',
    borderWidth: 1,
    alignItems: 'center',
    paddingVertical: 14,
  },
  actionIcon: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  alertIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
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
