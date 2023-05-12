import {pl_types} from '../generated/protobuf-js';
import IUser = pl_types.IUser;
import User = pl_types.User;

export enum Role {
  ADMIN,
  TEACHER,
  STUDENT,
}

export type LoggedInUser = {
  userXId: number;
  districtId: number;
  firstName: string;
  lastName: string;
  emailAddress: string;
  roles: Set<Role>;
};

export function login(user: IUser) {
  localStorage.setItem('user', JSON.stringify(user));
}

export function logout() {
  localStorage.removeItem('user');
}

export function getCurrentUser(
  onNotLoggedIn?: () => void
): LoggedInUser | undefined {
  const userJson = localStorage.getItem('user');
  if (userJson != null) {
    const user = User.fromObject(JSON.parse(userJson));

    const roles = new Set<Role>();
    user.isAdmin && roles.add(Role.ADMIN);
    user.isTeacher && roles.add(Role.TEACHER);
    user.isStudent && roles.add(Role.STUDENT);

    return {
      userXId: user.id!,
      districtId: user.districtId!,
      firstName: user.firstName!,
      lastName: user.lastName!,
      emailAddress: user.emailAddress!,
      roles: roles,
    };
  }
  if (onNotLoggedIn != null) {
    onNotLoggedIn();
  } else {
    window.open('/users/login', '_self');
  }
  return undefined;
}
