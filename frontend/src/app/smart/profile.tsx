import React from 'react';
import { StyleSheet, View, ScrollView, TouchableOpacity, Alert } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useStudent } from '@/contexts/student-context';
import { useAuth } from '@/contexts/auth-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses, getStudentStats, weeklyAttendance } from '@/data/studentData';
import { useRole, ROLE_LABELS, ROLE_COLORS } from '@/utils/role-utils';
import { useToggleTheme } from '@/hooks/use-theme';

export default function ProfileScreen({ onLogout }: { onLogout?: () => void }) {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const toggleTheme = useToggleTheme();
  const role = useRole();
  const { studentProfile, studentName } = useStudent();
  const { userProfile, user } = useAuth();
  const stats = getStudentStats();
  const presentDays = weeklyAttendance.filter((a) => a.status === 'present').length;
  const totalDays = weeklyAttendance.length;
  const absentDays = weeklyAttendance.filter((a) => a.status === 'absent').length;
  const lateDays = weeklyAttendance.filter((a) => a.status === 'late').length;

  const displayName = role.isStudent
    ? studentName
    : `${(userProfile as any)?.firstName || ''} ${(userProfile as any)?.lastName || ''}`.trim() || userProfile?.username || 'User';

  const userInitials = displayName
    .split(' ')
    .map((n: string) => n[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        <SafeAreaView style={{ flex: 1 }}>
          {/* ── Header with Avatar ── */}
          <View style={[styles.header, { backgroundColor: isDark ? 'rgba(0,0,0,0.4)' : 'rgba(255,140,0,0.1)' }]}>
            <BlurView intensity={isDark ? 40 : 60} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
            <YStack gap="$4" padding="$6" alignItems="center">
              <View style={[styles.avatar, { backgroundColor: role.color + '22', borderColor: role.color + '44' }]}>
                <Text fontSize={36} fontWeight="900" color={role.color}>
                  {userInitials}
                </Text>
              </View>
              <YStack gap="$1" alignItems="center">
                <Text fontSize={28} fontWeight="800" color={colors.text}>{displayName}</Text>
                <XStack gap="$2" alignItems="center">
                  <View style={[styles.roleBadge, { backgroundColor: role.color + '22' }]}>
                    <Ionicons name={role.icon as any} size={12} color={role.color} />
                    <Text fontSize={12} fontWeight="700" color={role.color} marginLeft={4}>{role.label}</Text>
                  </View>
                </XStack>
              </YStack>

              {/* Stats Row */}
              <View style={[styles.statsRow, { backgroundColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(255,255,255,0.3)' }]}>
                <BlurView intensity={isDark ? 20 : 10} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                <XStack gap="$4" alignItems="center" paddingHorizontal="$4" paddingVertical="$2">
                  {role.isStudent ? (
                    <>
                      <StatPill icon="trophy-outline" value={stats.gpa.toFixed(2)} label="GPA" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="book-outline" value={String(stats.totalCourses)} label="Courses" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="star-outline" value={String(stats.totalCredits)} label="Credits" colors={colors} />
                    </>
                  ) : role.isParent ? (
                    <>
                      <StatPill icon="people-outline" value="2" label="Children" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="school-outline" value="2" label="Classes" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="checkmark-circle-outline" value="91%" label="Avg Att." colors={colors} />
                    </>
                  ) : (
                    <>
                      <StatPill icon="shield-checkmark-outline" value={String(role.allRoles.length)} label="Roles" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="key-outline" value="Active" label="Session" colors={colors} />
                      <View style={styles.statDivider} />
                      <StatPill icon="finger-print-outline" value={userProfile?.id ? `#${userProfile.id}` : 'N/A'} label="User ID" colors={colors} />
                    </>
                  )}
                </XStack>
              </View>
            </YStack>
          </View>

          {/* ── Account Information ── */}
          <YStack gap="$4" padding="$4" marginTop="$2">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Account Information</Text>
            <InfoCard colors={colors} isDark={isDark}>
              <YStack gap="$3" padding="$4">
                {[
                  { icon: 'person', label: 'Full Name', value: displayName },
                  { icon: 'finger-print', label: 'Username', value: userProfile?.username || 'N/A' },
                  { icon: 'mail', label: 'Email', value: userProfile?.email || 'Not set' },
                  { icon: 'call', label: 'Phone', value: (userProfile as any)?.phone || 'Not set' },
                  { icon: 'shield', label: 'Role', value: role.label },
                  ...(userProfile?.createdAt
                    ? [{ icon: 'calendar', label: 'Member Since', value: new Date(userProfile.createdAt).toLocaleDateString() }]
                    : []),
                ].map((item, i) => (
                  <InfoRow key={i} icon={item.icon} label={item.label} value={item.value} colors={colors} />
                ))}
              </YStack>
            </InfoCard>
          </YStack>

          {/* ── Student-Specific: Attendance ── */}
          {role.isStudent && (
            <YStack gap="$4" padding="$4">
              <Text fontSize={18} fontWeight="700" color={colors.text}>Attendance</Text>
              <InfoCard colors={colors} isDark={isDark}>
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
              </InfoCard>
            </YStack>
          )}

          {/* ── Student-Specific: Course Performance ── */}
          {role.isStudent && (
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
          )}

          {/* ── Parent-Specific: Linked Children ── */}
          {role.isParent && (
            <YStack gap="$4" padding="$4">
              <Text fontSize={18} fontWeight="700" color={colors.text}>Linked Children</Text>
              {[
                { name: 'Amara Okafor', class: 'Form 3A', avg: 82, avatar: 'AO' },
                { name: 'Chidi Okafor', class: 'Form 1A', avg: 76, avatar: 'CO' },
              ].map((child, i) => (
                <View key={i} style={[styles.childCard, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)', borderColor: colors.primary + '26' }]}>
                  <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
                  <XStack gap="$3" alignItems="center" padding="$3">
                    <View style={[styles.childAvatar, { backgroundColor: colors.orangeBgMedium }]}>
                      <Text fontSize={14} fontWeight="800" color={colors.primary}>{child.avatar}</Text>
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text fontSize={15} fontWeight="700" color={colors.text}>{child.name}</Text>
                      <Text fontSize={12} color={colors.textTertiary}>{child.class}</Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text fontSize={14} fontWeight="700" color={child.avg >= 80 ? '#4CAF50' : '#FF9800'}>{child.avg}%</Text>
                      <Text fontSize={11} color={colors.textTertiary}>Average</Text>
                    </YStack>
                  </XStack>
                </View>
              ))}
            </YStack>
          )}

          {/* ── Admin-Specific: Permissions ── */}
          {role.canManage && (
            <YStack gap="$4" padding="$4">
              <Text fontSize={18} fontWeight="700" color={colors.text}>Permissions</Text>
              <InfoCard colors={colors} isDark={isDark}>
                <YStack gap="$2" padding="$4">
                  {[
                    { label: 'Manage Users', granted: role.canManageStudents },
                    { label: 'Manage Staff', granted: role.canManageStaff },
                    { label: 'View All Data', granted: role.canViewAll },
                    { label: 'Compute Results', granted: role.canComputeResults },
                    { label: 'View Analytics', granted: role.canViewAnalytics },
                    { label: 'System Administration', granted: role.isSuperAdmin },
                  ].map((perm, i) => (
                    <XStack key={i} justifyContent="space-between" alignItems="center" paddingVertical="$1">
                      <Text fontSize={14} color={colors.text}>{perm.label}</Text>
                      <Ionicons
                        name={perm.granted ? 'checkmark-circle' : 'close-circle'}
                        size={20}
                        color={perm.granted ? '#4CAF50' : colors.textTertiary}
                      />
                    </XStack>
                  ))}
                </YStack>
              </InfoCard>
            </YStack>
          )}

          {/* ── Admin-Specific: All Roles Assigned ── */}
          {role.isSuperAdmin && (
            <YStack gap="$4" padding="$4">
              <Text fontSize={18} fontWeight="700" color={colors.text}>System Roles</Text>
              <InfoCard colors={colors} isDark={isDark}>
                <YStack gap="$2" padding="$4">
                  {role.allRoles.map((r, i) => (
                    <XStack key={i} gap="$3" alignItems="center" paddingVertical="$1">
                      <View style={[styles.roleDot, { backgroundColor: ROLE_COLORS[r] || colors.primary }]} />
                      <Text fontSize={14} fontWeight="600" color={colors.text}>{ROLE_LABELS[r] || r}</Text>
                    </XStack>
                  ))}
                </YStack>
              </InfoCard>
            </YStack>
          )}

          {/* ── Settings ── */}
          <YStack gap="$4" padding="$4">
            <Text fontSize={18} fontWeight="700" color={colors.text}>Settings</Text>
            <InfoCard colors={colors} isDark={isDark}>
              <YStack gap="$0" padding="$2">
                <SettingsRow
                  icon="moon"
                  label="Dark Mode"
                  value={isDark ? 'On' : 'Off'}
                  onPress={toggleTheme}
                  colors={colors}
                />
                <SettingsRow
                  icon="lock-closed"
                  label="Change Password"
                  value=""
                  onPress={() => Alert.alert('Coming Soon', 'Password change will be available soon.')}
                  colors={colors}
                />
                <SettingsRow
                  icon="notifications"
                  label="Notifications"
                  value="Enabled"
                  onPress={() => {}}
                  colors={colors}
                />
                <SettingsRow
                  icon="help-circle"
                  label="Help & Support"
                  value=""
                  onPress={() => {}}
                  colors={colors}
                />
                <SettingsRow
                  icon="information-circle"
                  label="About Feza Smart"
                  value="v1.0.0"
                  onPress={() => Alert.alert('Feza Smart', 'Feza Smart School Results Tracking System\nVersion 1.0.0')}
                  colors={colors}
                  isLast
                />
              </YStack>
            </InfoCard>
          </YStack>

          {/* ── Logout Button ── */}
          <YStack gap="$3" padding="$4" paddingBottom="$8">
            <TouchableOpacity onPress={onLogout} style={[styles.logoutBtn, { borderColor: '#FF4757' + '44' }]}>
              <Ionicons name="log-out-outline" size={20} color="#FF4757" />
              <Text fontSize={16} fontWeight="600" color="#FF4757" marginLeft="$2">
                Sign Out
              </Text>
            </TouchableOpacity>
          </YStack>
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

/* ── Shared Components ── */
function StatPill({ icon, value, label, colors }: { icon: string; value: string; label: string; colors: any }) {
  return (
    <YStack gap="$1" alignItems="center">
      <Ionicons name={icon as any} size={18} color={colors.textSecondary} />
      <Text fontSize={16} fontWeight="800" color={colors.text}>{value}</Text>
      <Text fontSize={11} color={colors.textTertiary}>{label}</Text>
    </YStack>
  );
}

function InfoCard({ colors, isDark, children }: { colors: any; isDark: boolean; children: React.ReactNode }) {
  return (
    <View style={[styles.infoCard, { backgroundColor: isDark ? 'rgba(255,255,255,0.06)' : 'rgba(255,255,255,0.6)' }]}>
      <BlurView intensity={isDark ? 12 : 6} tint={isDark ? 'dark' : 'light'} style={StyleSheet.absoluteFill} />
      {children}
    </View>
  );
}

function InfoRow({ icon, label, value, colors }: { icon: string; label: string; value: string; colors: any }) {
  return (
    <XStack gap="$3" alignItems="center" paddingVertical="$2">
      <View style={[styles.infoIcon, { backgroundColor: colors.orangeBgMedium }]}>
        <Ionicons name={icon + '-outline' as any} size={18} color={colors.primary} />
      </View>
      <YStack flex={1} gap="$1">
        <Text fontSize={12} color={colors.textTertiary}>{label}</Text>
        <Text fontSize={15} fontWeight="600" color={colors.text}>{value}</Text>
      </YStack>
    </XStack>
  );
}

function SettingsRow({ icon, label, value, onPress, colors, isLast }: {
  icon: string; label: string; value: string; onPress: () => void; colors: any; isLast?: boolean;
}) {
  return (
    <TouchableOpacity onPress={onPress}>
      <XStack
        gap="$3"
        alignItems="center"
        paddingVertical="$3"
        paddingHorizontal="$2"
        borderBottomWidth={isLast ? 0 : 1}
        borderBottomColor={colors.border}
      >
        <View style={[styles.infoIcon, { backgroundColor: colors.orangeBgMedium }]}>
          <Ionicons name={icon + '-outline' as any} size={18} color={colors.primary} />
        </View>
        <Text fontSize={15} fontWeight="500" color={colors.text} flex={1}>{label}</Text>
        {value ? (
          <Text fontSize={13} color={colors.textTertiary}>{value}</Text>
        ) : null}
        <Ionicons name="chevron-forward" size={16} color={colors.textTertiary} />
      </XStack>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  header: {
    borderBottomLeftRadius: 20,
    borderBottomRightRadius: 20,
    overflow: 'hidden',
    marginBottom: 16,
  },
  avatar: {
    width: 96,
    height: 96,
    borderRadius: 48,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 4,
  },
  roleBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
  },
  statsRow: {
    flexDirection: 'row',
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.1)',
  },
  statDivider: {
    width: 1,
    height: 40,
    backgroundColor: 'rgba(0,0,0,0.1)',
  },
  infoCard: {
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.05)',
  },
  infoIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  attStat: {
    flex: 1,
    padding: 12,
    borderRadius: 12,
    overflow: 'hidden',
    alignItems: 'center',
  },
  attIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 4,
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
  coursePerf: {
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
  },
  coursePerfIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  childCard: {
    borderRadius: 14,
    overflow: 'hidden',
    borderWidth: 1,
  },
  childAvatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    alignItems: 'center',
    justifyContent: 'center',
  },
  roleDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
  },
  logoutBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 52,
    borderRadius: 16,
    borderWidth: 1,
    backgroundColor: 'rgba(255,71,87,0.06)',
  },
});
