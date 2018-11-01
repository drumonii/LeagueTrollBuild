package com.drumonii.loltrollbuild.security.logout;

import com.drumonii.loltrollbuild.security.AdminUserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

/**
 * Logout response from the result of the form logout.
 */
@JsonInclude(Include.NON_NULL)
public class LogoutResponse {

    @JsonProperty
    private LogoutStatus status;

    @JsonProperty
    private String message;

    @JsonProperty
    private AdminUserDetails userDetails;

    public LogoutStatus getStatus() {
        return status;
    }

    public void setStatus(LogoutStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AdminUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(AdminUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public enum LogoutStatus {

        SUCCESS, FAILED

    }

    private abstract static class LogoutResponseBuilder<B extends LogoutResponseBuilder<B>> {

        protected LogoutStatus status;
        protected String message;
        protected AdminUserDetails adminUserDetails;

        protected B withStatus(LogoutStatus status) {
            this.status = status;
            return self();
        }

        protected B withMessage(String message) {
            this.message = message;
            return self();
        }

        protected LogoutResponse build() {
            LogoutResponse logoutResponse = new LogoutResponse();
            logoutResponse.setStatus(status);
            logoutResponse.setMessage(message);
            logoutResponse.setUserDetails(adminUserDetails);
            return logoutResponse;
        }

        protected abstract B self();

    }

    public static final class SuccessfulLogoutResponseBuilder extends LogoutResponseBuilder<SuccessfulLogoutResponseBuilder> {

        public SuccessfulLogoutResponseBuilder() {
            withStatus(LogoutStatus.SUCCESS);
            withMessage("Logout success");
        }

        public SuccessfulLogoutResponseBuilder fromAuthentication(Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            adminUserDetails = new AdminUserDetails(user);
            return this;
        }

        @Override
        protected SuccessfulLogoutResponseBuilder self() {
            return this;
        }

    }

    public static final class FailedLogoutResponseBuilder extends LogoutResponseBuilder<FailedLogoutResponseBuilder> {

        public FailedLogoutResponseBuilder() {
            withStatus(LogoutStatus.FAILED);
            withMessage("Logout failed");
        }

        @Override
        protected FailedLogoutResponseBuilder self() {
            return this;
        }

    }

}
