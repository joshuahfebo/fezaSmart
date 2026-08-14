import { useAuth } from '@/contexts/auth-context';

export type UserRole =
  | 'SUPER_ADMIN'
  | 'HEAD_MASTER'
  | 'GENERAL_SECOND_MASTER'
  | 'HUMAN_RESOURCE'
  | 'SECOND_MASTER'
  | 'PARENT'
  | 'STUDENT'
  | 'TEACHER'
  | 'ACCOUNTANT'
  | 'BURSAR'
  | 'LIBRARIAN';

export const ROLE_LABELS: Record<UserRole, string> = {
  SUPER_ADMIN: 'Super Admin',
  HEAD_MASTER: 'Headmaster',
  GENERAL_SECOND_MASTER: 'Deputy Head',
  HUMAN_RESOURCE: 'HR Manager',
  SECOND_MASTER: 'Academic Admin',
  PARENT: 'Parent',
  STUDENT: 'Student',
  TEACHER: 'Teacher',
  ACCOUNTANT: 'Accountant',
  BURSAR: 'Bursar',
  LIBRARIAN: 'Librarian',
};

export const ROLE_COLORS: Record<UserRole, string> = {
  SUPER_ADMIN: '#9C27B0',
  HEAD_MASTER: '#FF8C00',
  GENERAL_SECOND_MASTER: '#2196F3',
  HUMAN_RESOURCE: '#4CAF50',
  SECOND_MASTER: '#00BCD4',
  PARENT: '#E91E63',
  STUDENT: '#FF9800',
  TEACHER: '#3F51B5',
  ACCOUNTANT: '#795548',
  BURSAR: '#607D8B',
  LIBRARIAN: '#8BC34A',
};

export const ROLE_ICONS: Record<UserRole, string> = {
  SUPER_ADMIN: 'shield-checkmark',
  HEAD_MASTER: 'school',
  GENERAL_SECOND_MASTER: 'people',
  HUMAN_RESOURCE: 'person-add',
  SECOND_MASTER: 'analytics',
  PARENT: 'heart',
  STUDENT: 'book',
  TEACHER: 'chalkboard',
  ACCOUNTANT: 'wallet',
  BURSAR: 'cash',
  LIBRARIAN: 'library',
};

export interface RoleInfo {
  primaryRole: UserRole;
  allRoles: UserRole[];
  isSuperAdmin: boolean;
  isSchoolAdmin: boolean;
  isHeadmaster: boolean;
  isHR: boolean;
  isSecondMaster: boolean;
  isParent: boolean;
  isStudent: boolean;
  isTeacher: boolean;
  canManage: boolean;
  canViewAll: boolean;
  canComputeResults: boolean;
  canManageStudents: boolean;
  canManageStaff: boolean;
  canViewAnalytics: boolean;
  label: string;
  color: string;
  icon: string;
}

function parseRoles(rawRoles: any[]): UserRole[] {
  if (!rawRoles || !Array.isArray(rawRoles)) return [];
  return rawRoles
    .map((r: any) => {
      if (typeof r === 'string') return r.toUpperCase().replace(/\s+/g, '_') as UserRole;
      if (r?.name) return String(r.name).toUpperCase().replace(/\s+/g, '_') as UserRole;
      if (r?.roleName) return String(r.roleName).toUpperCase().replace(/\s+/g, '_') as UserRole;
      return null;
    })
    .filter(Boolean) as UserRole[];
}

function determinePrimary(roles: UserRole[]): UserRole {
  const priority: UserRole[] = [
    'SUPER_ADMIN',
    'HEAD_MASTER',
    'GENERAL_SECOND_MASTER',
    'HUMAN_RESOURCE',
    'SECOND_MASTER',
    'TEACHER',
    'ACCOUNTANT',
    'BURSAR',
    'LIBRARIAN',
    'PARENT',
    'STUDENT',
  ];
  for (const p of priority) {
    if (roles.includes(p)) return p;
  }
  return 'STUDENT';
}

export function useRole(): RoleInfo {
  const { user, userProfile } = useAuth();
  const rawRoles = (user as any)?.roles || userProfile?.userRoleRoles || [];
  const allRoles = parseRoles(rawRoles);
  const primaryRole = determinePrimary(allRoles);

  const isSuperAdmin = allRoles.includes('SUPER_ADMIN');
  const isHeadmaster = allRoles.includes('HEAD_MASTER');
  const isHR = allRoles.includes('HUMAN_RESOURCE');
  const isSecondMaster =
    allRoles.includes('SECOND_MASTER') ||
    allRoles.includes('GENERAL_SECOND_MASTER');
  const isSchoolAdmin = isHeadmaster || isHR || isSecondMaster || isSuperAdmin;
  const isParent = allRoles.includes('PARENT');
  const isStudent = allRoles.includes('STUDENT');
  const isTeacher = allRoles.includes('TEACHER');

  return {
    primaryRole,
    allRoles,
    isSuperAdmin,
    isSchoolAdmin,
    isHeadmaster,
    isHR,
    isSecondMaster,
    isParent,
    isStudent,
    isTeacher,
    canManage: isSuperAdmin || isSchoolAdmin,
    canViewAll: isSuperAdmin || isSchoolAdmin || isHeadmaster,
    canComputeResults: isSuperAdmin || isHeadmaster || isSecondMaster,
    canManageStudents: isSuperAdmin || isSchoolAdmin,
    canManageStaff: isSuperAdmin || isHR || isHeadmaster,
    canViewAnalytics: isSuperAdmin || isSchoolAdmin || isHeadmaster,
    label: ROLE_LABELS[primaryRole] || 'User',
    color: ROLE_COLORS[primaryRole] || '#FF8C00',
    icon: ROLE_ICONS[primaryRole] || 'person',
  };
}
