import { inject, TestBed } from '@angular/core/testing';

import { AdminUserDetails } from '@security/admin-user-details';
import { AdminAuthService } from './admin-auth.service';

describe('AdminAuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdminAuthService]
    });
  });

  afterEach(() => {
    localStorage.clear();
  });

  describe('getAdminUserDetails', () => {

    it('with admin user details in localStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      const mockAdminUserDetails: AdminUserDetails = {
        username: 'admin',
        authorities: [
          {
            authority: 'ROLE_ADMIN'
          }
        ]
      };

      localStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(mockAdminUserDetails));

      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toEqual(mockAdminUserDetails);
      });
    }));

    it('without admin user details in localStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toBeNull();
      });
    }));

  });

});
