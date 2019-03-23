import { HttpHeaders } from '@angular/common/http';
import { ADMIN_INTERCEPT_HEADER, ADMIN_INTERCEPT_HEADER_VAL } from '@admin-interceptor/admin-http-interceptor-headers';

/**
 * Base service for admin services.
 */
export class AdminService {

  /**
   * Sets the base {@link HttpHeaders} for admin services.
   *
   * @return the {@link HttpHeaders}
   */
  getBaseHttpHeaders(): HttpHeaders {
    return new HttpHeaders()
      .set(ADMIN_INTERCEPT_HEADER, ADMIN_INTERCEPT_HEADER_VAL);
  }

}
