import { AdminUserDetails } from '@admin-security/admin-user-details';

export interface AdminLoginResponse {
  status: AdminLoginStatus;
  message: string;
  userDetails?: AdminUserDetails;
}

export enum AdminLoginStatus {

  SUCCESS = 'SUCCESS', FAILED = 'FAILED'

}
