import { AdminUserDetails } from '@admin-security/admin-user-details';

export interface AdminLogoutResponse {
  status: AdminLogoutStatus;
  message: string;
  userDetails?: AdminUserDetails;
}

export enum AdminLogoutStatus {
  SUCCESS = 'SUCCESS', FAILED = 'FAILED'
}
