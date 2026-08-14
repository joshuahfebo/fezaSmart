import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
  Alert,
  RefreshControl,
} from 'react-native';
import { BlurView } from 'expo-blur';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@react-native-vector-icons/ionicons';
import { useTheme, useThemeMode } from '@/hooks/use-theme';
import { useResults } from '@/contexts/results-context';
import { useStudent } from '@/contexts/student-context';
import { useAuth } from '@/contexts/auth-context';
import { Text, XStack, YStack } from 'tamagui';
import { SafeAreaView } from 'react-native-safe-area-context';
import { courses } from '@/data/studentData';
import { useRole } from '@/utils/role-utils';

type TabType = 'all' | 'exams' | 'quizzes' | 'assignments';

export default function ResultsScreen() {
  const colors = useTheme();
  const mode = useThemeMode();
  const isDark = mode === 'dark';
  const role = useRole();
  const { studentProfile } = useStudent();
  const {
    currentStudentResults,
    results,
    fetchStudentResults,
    fetchAllResults,
    fetchExamResults,
    computeExamResults,
    loading,
    error,
  } = useResults();
  const [activeTab, setActiveTab] = useState<TabType>('all');
  const [refreshing, setRefreshing] = useState(false);
  const [computing, setComputing] = useState(false);

  const displayResults = role.isStudent ? currentStudentResults : results;

  useEffect(() => {
    if (role.isStudent && studentProfile?.id) {
      fetchStudentResults(studentProfile.id);
    } else if (role.canViewAll) {
      fetchAllResults({ page: 0, size: 50 });
    }
  }, [studentProfile?.id, role.primaryRole]);

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

  const handleComputeResults = async () => {
    Alert.alert(
      'Compute Results',
      'This will compute results for all exams. Continue?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Compute',
          onPress: async () => {
            setComputing(true);
            try {
              await computeExamResults(1);
              Alert.alert('Success', 'Results computed successfully');
            } catch (err: any) {
              Alert.alert('Error', err.message || 'Failed to compute results');
            } finally {
              setComputing(false);
            }
          },
        },
      ],
    );
  };

  const overallAverage =
    displayResults.length > 0
      ? (
          displayResults.reduce(
            (sum, r) => sum + (r.averagePercentage || 0),
            0,
          ) / displayResults.length
        ).toFixed(1)
      : '0';

  const highestScore =
    displayResults.length > 0
      ? Math.max(...displayResults.map((r) => r.averagePercentage || 0))
      : 0;

  if (loading && !refreshing) {
    return (
      <View style={[styles.centered, { backgroundColor: colors.background }]}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text fontSize={14} color={colors.textSecondary} marginTop="$2">
          Loading results...
        </Text>
      </View>
    );
  }

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
            <StudentResults
              colors={colors}
              isDark={isDark}
              role={role}
              displayResults={displayResults}
              overallAverage={overallAverage}
              highestScore={highestScore}
              activeTab={activeTab}
              setActiveTab={setActiveTab}
            />
          )}

          {role.isParent && (
            <ParentResults
              colors={colors}
              isDark={isDark}
              role={role}
              displayResults={displayResults}
              overallAverage={overallAverage}
              highestScore={highestScore}
            />
          )}

          {(role.isSuperAdmin || role.isSchoolAdmin) && (
            <AdminResults
              colors={colors}
              isDark={isDark}
              role={role}
              displayResults={displayResults}
              overallAverage={overallAverage}
              highestScore={highestScore}
              computing={computing}
              onCompute={handleComputeResults}
              activeTab={activeTab}
              setActiveTab={setActiveTab}
            />
          )}

          {!role.isStudent && !role.isParent && !role.canManage && (
            <GenericResults
              colors={colors}
              isDark={isDark}
              role={role}
              displayResults={displayResults}
              overallAverage={overallAverage}
            />
          )}
        </SafeAreaView>
      </ScrollView>
    </View>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ STUDENT RESULTS ━━━━━━━━━━━━━━━━━━━━━ */
function StudentResults({
  colors,
  isDark,
  role,
  displayResults,
  overallAverage,
  highestScore,
  activeTab,
  setActiveTab,
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
            Academic Results
          </Text>
          <XStack gap="$2" alignItems="center">
            <Text fontSize={16} color={colors.textSecondary}>
              Track your performance
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

        <StatsRow
          colors={colors}
          isDark={isDark}
          stats={[
            { label: 'Average', value: overallAverage + '%', color: colors.primary },
            { label: 'Total', value: String(displayResults.length || courses.length), color: colors.text },
            { label: 'Highest', value: (highestScore > 0 ? highestScore.toFixed(0) : Math.max(...courses.map(c => c.progress))) + '%', color: '#4CAF50' },
          ]}
        />

        <XStack gap="$2">
          {(['all', 'exams', 'quizzes', 'assignments'] as TabType[]).map(
            (tab) => (
              <TouchableOpacity
                key={tab}
                onPress={() => setActiveTab(tab)}
                style={{ flex: 1 }}
              >
                <View
                  style={[
                    styles.tabBtn,
                    {
                      backgroundColor:
                        activeTab === tab
                          ? colors.primary
                          : isDark
                            ? 'rgba(255,255,255,0.08)'
                            : 'rgba(0,0,0,0.04)',
                    },
                  ]}
                >
                  <Text
                    fontSize={12}
                    fontWeight={activeTab === tab ? '600' : '500'}
                    color={activeTab === tab ? '#FFF' : colors.textSecondary}
                  >
                    {tab.charAt(0).toUpperCase() + tab.slice(1)}
                  </Text>
                </View>
              </TouchableOpacity>
            ),
          )}
        </XStack>
      </YStack>

      <YStack gap="$2" padding="$4">
        {displayResults.length > 0
          ? displayResults.map((result: any) => {
              const gradeColor =
                (result.averagePercentage || 0) >= 80
                  ? '#4CAF50'
                  : (result.averagePercentage || 0) >= 60
                    ? '#FF9800'
                    : '#F44336';
              return (
                <View
                  key={result.id}
                  style={[
                    styles.gradeItem,
                    {
                      backgroundColor: isDark
                        ? 'rgba(255,255,255,0.06)'
                        : 'rgba(255,255,255,0.6)',
                      borderColor: colors.primary + '26',
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
                        styles.gradeIcon,
                        { backgroundColor: colors.primary + '26' },
                      ]}
                    >
                      <Ionicons
                        name="trophy"
                        size={18}
                        color={colors.primary}
                      />
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text
                        fontSize={15}
                        fontWeight="700"
                        color={colors.text}
                      >
                        Exam #{result.examId}
                      </Text>
                      <Text fontSize={12} color={colors.textTertiary}>
                        Division: {result.division || 'N/A'} | Rank: #
                        {result.ranking || 'N/A'}
                      </Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text
                        fontSize={16}
                        fontWeight="800"
                        color={gradeColor}
                      >
                        {(result.averagePercentage || 0).toFixed(1)}%
                      </Text>
                      <View
                        style={[
                          styles.gradeBadge,
                          { backgroundColor: gradeColor + '26' },
                        ]}
                      >
                        <Text
                          fontSize={11}
                          fontWeight="600"
                          color={gradeColor}
                        >
                          {result.totalPoints || 0} pts
                        </Text>
                      </View>
                    </YStack>
                  </XStack>
                </View>
              );
            })
          : courses.map((course) => {
              const gradeColor =
                course.progress >= 80
                  ? '#4CAF50'
                  : course.progress >= 60
                    ? '#FF9800'
                    : '#F44336';
              return (
                <View
                  key={course.id}
                  style={[
                    styles.gradeItem,
                    {
                      backgroundColor: isDark
                        ? 'rgba(255,255,255,0.06)'
                        : 'rgba(255,255,255,0.6)',
                      borderColor: course.color + '26',
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
                        styles.gradeIcon,
                        { backgroundColor: course.color + '26' },
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
                        fontSize={15}
                        fontWeight="700"
                        color={colors.text}
                      >
                        {course.name}
                      </Text>
                      <Text fontSize={12} color={colors.textTertiary}>
                        {course.code} - {course.teacher}
                      </Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text
                        fontSize={16}
                        fontWeight="800"
                        color={gradeColor}
                      >
                        {course.progress}%
                      </Text>
                      <View
                        style={[
                          styles.gradeBadge,
                          { backgroundColor: gradeColor + '26' },
                        ]}
                      >
                        <Text
                          fontSize={11}
                          fontWeight="600"
                          color={gradeColor}
                        >
                          {course.grade}
                        </Text>
                      </View>
                    </YStack>
                  </XStack>
                </View>
              );
            })}
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ PARENT RESULTS ━━━━━━━━━━━━━━━━━━━━━ */
function ParentResults({
  colors,
  isDark,
  role,
  displayResults,
  overallAverage,
  highestScore,
}: any) {
  const mockChildren = [
    { id: 1, name: 'Amara Okafor', class: 'Form 3A', avg: 82, division: 'Division II' },
    { id: 2, name: 'Chidi Okafor', class: 'Form 1A', avg: 76, division: 'Division III' },
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
            Report Cards
          </Text>
          <Text fontSize={16} color={colors.textSecondary}>
            View your children's academic results
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

      <YStack gap="$3" padding="$4">
        <StatsRow
          colors={colors}
          isDark={isDark}
          stats={[
            { label: 'Average', value: selectedChild.avg + '%', color: colors.primary },
            { label: 'Division', value: selectedChild.division.split(' ')[1], color: '#4CAF50' },
            { label: 'Class', value: selectedChild.class.replace('Form ', 'F'), color: '#2196F3' },
          ]}
        />
      </YStack>

      <YStack gap="$4" padding="$4">
        <Text fontSize={18} fontWeight="700" color={colors.text}>
          Subject Breakdown
        </Text>
        {courses.map((course) => {
          const score = Math.round(
            (selectedChild.avg / 100) * course.progress +
              (Math.random() * 10 - 5),
          );
          const gradeColor =
            score >= 80 ? '#4CAF50' : score >= 60 ? '#FF9800' : '#F44336';
          return (
            <View
              key={course.id}
              style={[
                styles.gradeItem,
                {
                  backgroundColor: isDark
                    ? 'rgba(255,255,255,0.06)'
                    : 'rgba(255,255,255,0.6)',
                  borderColor: course.color + '26',
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
                    styles.gradeIcon,
                    { backgroundColor: course.color + '26' },
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
                    fontSize={15}
                    fontWeight="700"
                    color={colors.text}
                  >
                    {course.name}
                  </Text>
                  <Text fontSize={12} color={colors.textTertiary}>
                    {course.code}
                  </Text>
                </YStack>
                <YStack alignItems="flex-end" gap="$1">
                  <Text
                    fontSize={16}
                    fontWeight="800"
                    color={gradeColor}
                  >
                    {score}%
                  </Text>
                  <View
                    style={[
                      styles.gradeBadge,
                      { backgroundColor: gradeColor + '26' },
                    ]}
                  >
                    <Text
                      fontSize={11}
                      fontWeight="600"
                      color={gradeColor}
                    >
                      {score >= 80 ? 'A' : score >= 70 ? 'B' : score >= 60 ? 'C' : 'D'}
                    </Text>
                  </View>
                </YStack>
              </XStack>
            </View>
          );
        })}
      </YStack>

      <YStack gap="$3" padding="$4">
        <TouchableOpacity style={styles.downloadBtn}>
          <Ionicons name="download-outline" size={18} color="#FFF" />
          <Text
            fontSize={14}
            fontWeight="600"
            color="#FFF"
            marginLeft="$2"
          >
            Download Report Card
          </Text>
        </TouchableOpacity>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ ADMIN RESULTS ━━━━━━━━━━━━━━━━━━━━━ */
function AdminResults({
  colors,
  isDark,
  role,
  displayResults,
  overallAverage,
  highestScore,
  computing,
  onCompute,
  activeTab,
  setActiveTab,
}: any) {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedExam, setSelectedExam] = useState('all');

  const exams = [
    { id: 'all', label: 'All Exams' },
    { id: '1', label: 'Midterm 1' },
    { id: '2', label: 'End of Term 1' },
    { id: '3', label: 'Midterm 2' },
  ];

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
            Results Management
          </Text>
          <XStack gap="$2" alignItems="center">
            <Text fontSize={16} color={colors.textSecondary}>
              {role.isSuperAdmin
                ? 'System-wide results control'
                : 'School results management'}
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

        <StatsRow
          colors={colors}
          isDark={isDark}
          stats={[
            { label: 'Average', value: overallAverage + '%', color: colors.primary },
            { label: 'Records', value: String(displayResults.length), color: colors.text },
            { label: 'Highest', value: (highestScore > 0 ? highestScore.toFixed(0) : '0') + '%', color: '#4CAF50' },
          ]}
        />

        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={{ paddingRight: 24 }}
        >
          {exams.map((exam) => (
            <TouchableOpacity
              key={exam.id}
              onPress={() => setSelectedExam(exam.id)}
              style={{ marginRight: 8 }}
            >
              <View
                style={[
                  styles.filterChip,
                  {
                    backgroundColor:
                      selectedExam === exam.id
                        ? colors.primary
                        : isDark
                          ? 'rgba(255,255,255,0.08)'
                          : 'rgba(0,0,0,0.04)',
                  },
                ]}
              >
                <Text
                  fontSize={12}
                  fontWeight={selectedExam === exam.id ? '600' : '500'}
                  color={
                    selectedExam === exam.id ? '#FFF' : colors.textSecondary
                  }
                >
                  {exam.label}
                </Text>
              </View>
            </TouchableOpacity>
          ))}
        </ScrollView>

        {role.canComputeResults && (
          <TouchableOpacity
            onPress={onCompute}
            disabled={computing}
            style={styles.computeBtn}
          >
            <LinearGradient
              colors={[colors.primary, colors.primaryDim]}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={styles.computeGradient}
            >
              {computing ? (
                <ActivityIndicator size="small" color="#FFF" />
              ) : (
                <Ionicons name="calculator" size={18} color="#FFF" />
              )}
              <Text
                fontSize={14}
                fontWeight="700"
                color="#FFF"
                marginLeft="$2"
              >
                {computing ? 'Computing...' : 'Compute Exam Results'}
              </Text>
            </LinearGradient>
          </TouchableOpacity>
        )}
      </YStack>

      <XStack gap="$2" paddingHorizontal="$4">
        {(['all', 'exams', 'quizzes', 'assignments'] as TabType[]).map(
          (tab) => (
            <TouchableOpacity
              key={tab}
              onPress={() => setActiveTab(tab)}
              style={{ flex: 1 }}
            >
              <View
                style={[
                  styles.tabBtn,
                  {
                    backgroundColor:
                      activeTab === tab
                        ? colors.primary
                        : isDark
                          ? 'rgba(255,255,255,0.08)'
                          : 'rgba(0,0,0,0.04)',
                  },
                ]}
              >
                <Text
                  fontSize={12}
                  fontWeight={activeTab === tab ? '600' : '500'}
                  color={activeTab === tab ? '#FFF' : colors.textSecondary}
                >
                  {tab.charAt(0).toUpperCase() + tab.slice(1)}
                </Text>
              </View>
            </TouchableOpacity>
          ),
        )}
      </XStack>

      <YStack gap="$2" padding="$4">
        <Text
          fontSize={13}
          fontWeight="600"
          color={colors.textTertiary}
          marginBottom="$1"
        >
          {displayResults.length} records found
        </Text>
        {displayResults.length > 0
          ? displayResults.map((result: any) => {
              const gradeColor =
                (result.averagePercentage || 0) >= 80
                  ? '#4CAF50'
                  : (result.averagePercentage || 0) >= 60
                    ? '#FF9800'
                    : '#F44336';
              return (
                <View
                  key={result.id}
                  style={[
                    styles.gradeItem,
                    {
                      backgroundColor: isDark
                        ? 'rgba(255,255,255,0.06)'
                        : 'rgba(255,255,255,0.6)',
                      borderColor: colors.primary + '26',
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
                        styles.gradeIcon,
                        { backgroundColor: colors.primary + '26' },
                      ]}
                    >
                      <Ionicons
                        name="trophy"
                        size={18}
                        color={colors.primary}
                      />
                    </View>
                    <YStack flex={1} gap="$1">
                      <Text
                        fontSize={15}
                        fontWeight="700"
                        color={colors.text}
                      >
                        Exam #{result.examId} - Student #{result.studentId}
                      </Text>
                      <Text fontSize={12} color={colors.textTertiary}>
                        Div: {result.division || 'N/A'} | Rank: #
                        {result.ranking || 'N/A'}
                      </Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text
                        fontSize={16}
                        fontWeight="800"
                        color={gradeColor}
                      >
                        {(result.averagePercentage || 0).toFixed(1)}%
                      </Text>
                      <View
                        style={[
                          styles.gradeBadge,
                          { backgroundColor: gradeColor + '26' },
                        ]}
                      >
                        <Text
                          fontSize={11}
                          fontWeight="600"
                          color={gradeColor}
                        >
                          {result.totalPoints || 0} pts
                        </Text>
                      </View>
                    </YStack>
                  </XStack>
                </View>
              );
            })
          : courses.map((course) => {
              const gradeColor =
                course.progress >= 80
                  ? '#4CAF50'
                  : course.progress >= 60
                    ? '#FF9800'
                    : '#F44336';
              return (
                <View
                  key={course.id}
                  style={[
                    styles.gradeItem,
                    {
                      backgroundColor: isDark
                        ? 'rgba(255,255,255,0.06)'
                        : 'rgba(255,255,255,0.6)',
                      borderColor: course.color + '26',
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
                        styles.gradeIcon,
                        { backgroundColor: course.color + '26' },
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
                        fontSize={15}
                        fontWeight="700"
                        color={colors.text}
                      >
                        {course.name}
                      </Text>
                      <Text fontSize={12} color={colors.textTertiary}>
                        {course.code} - {course.teacher}
                      </Text>
                    </YStack>
                    <YStack alignItems="flex-end" gap="$1">
                      <Text
                        fontSize={16}
                        fontWeight="800"
                        color={gradeColor}
                      >
                        {course.progress}%
                      </Text>
                      <View
                        style={[
                          styles.gradeBadge,
                          { backgroundColor: gradeColor + '26' },
                        ]}
                      >
                        <Text
                          fontSize={11}
                          fontWeight="600"
                          color={gradeColor}
                        >
                          {course.grade}
                        </Text>
                      </View>
                    </YStack>
                  </XStack>
                </View>
              );
            })}
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ GENERIC RESULTS ━━━━━━━━━━━━━━━━━━━━━ */
function GenericResults({
  colors,
  isDark,
  role,
  displayResults,
  overallAverage,
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
            Results
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
          <Ionicons name="bar-chart" size={64} color={role.color + '66'} />
          <Text
            fontSize={20}
            fontWeight="700"
            color={colors.text}
            marginTop="$3"
          >
            Results Portal
          </Text>
          <Text
            fontSize={14}
            color={colors.textSecondary}
            marginTop="$2"
            textAlign="center"
          >
            Results data will appear here once connected.
          </Text>
        </View>
      </YStack>
    </>
  );
}

/* ━━━━━━━━━━━━━━━━━━━━━ SHARED COMPONENTS ━━━━━━━━━━━━━━━━━━━━━ */
function StatsRow({
  colors,
  isDark,
  stats,
}: {
  colors: any;
  isDark: boolean;
  stats: { label: string; value: string; color: string }[];
}) {
  return (
    <View
      style={[
        styles.statsRow,
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
      <XStack gap="$4" alignItems="center" padding="$4">
        {stats.map((stat, i) => (
          <React.Fragment key={i}>
            {i > 0 && <View style={styles.divider} />}
            <YStack alignItems="center" flex={1}>
              <Text fontSize={12} color={colors.textTertiary}>
                {stat.label}
              </Text>
              <Text fontSize={24} fontWeight="800" color={stat.color}>
                {stat.value}
              </Text>
            </YStack>
          </React.Fragment>
        ))}
      </XStack>
    </View>
  );
}

const styles = StyleSheet.create({
  centered: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  roleBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 10,
  },
  statsRow: {
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.05)',
  },
  divider: {
    width: 1,
    height: 40,
    backgroundColor: 'rgba(0,0,0,0.1)',
  },
  tabBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderRadius: 12,
  },
  filterChip: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.1)',
  },
  gradeItem: {
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
  },
  gradeIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  gradeBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
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
  computeBtn: {
    borderRadius: 16,
    overflow: 'hidden',
  },
  computeGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 52,
    borderRadius: 16,
  },
  downloadBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FF8C00',
    height: 48,
    borderRadius: 14,
  },
  emptyState: {
    padding: 40,
    borderRadius: 20,
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
    width: '100%',
  },
});
