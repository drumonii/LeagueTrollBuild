package com.drumonii.loltrollbuild.security.login;

import com.drumonii.loltrollbuild.security.AdminUserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

/**
 * Login response from the result of the form login authentication.
 */
@JsonInclude(Include.NON_NULL)
public class LoginResponse {

    @JsonProperty
    private LoginStatus status;

    @JsonProperty
    private String message;

    // For valid login

    @JsonProperty
    private AdminUserDetails userDetails;

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
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

    public enum LoginStatus {

        SUCCESS, FAILED

    }

    private abstract static class LoginResponseBuilder<B extends LoginResponseBuilder<B>> {

        protected LoginStatus status;
        protected String message;
        protected AdminUserDetails adminUserDetails;

        protected B withStatus(LoginStatus status) {
            this.status = status;
            return self();
        }

        protected B withMessage(String message) {
            this.message = message;
            return self();
        }

        protected LoginResponse build() {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setStatus(status);
            loginResponse.setMessage(message);
            loginResponse.setUserDetails(adminUserDetails);
            return loginResponse;
        }

        protected abstract B self();

    }

    public static final class SuccessfulLoginResponseBuilder extends LoginResponseBuilder<SuccessfulLoginResponseBuilder> {

        public SuccessfulLoginResponseBuilder() {
            withStatus(LoginStatus.SUCCESS);
            withMessage("Authentication success");
        }

        public SuccessfulLoginResponseBuilder fromAuthentication(Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            adminUserDetails = new AdminUserDetails(user);
            return this;
        }

        @Override
        protected SuccessfulLoginResponseBuilder self() {
            return this;
        }

    }

    public static final class FailedLoginResponseBuilder extends LoginResponseBuilder<FailedLoginResponseBuilder> {

        public FailedLoginResponseBuilder() {
            withStatus(LoginStatus.FAILED);
        }

        public FailedLoginResponseBuilder fromAuthenticationException(AuthenticationException exception) {
            withMessage(exception.getMessage());
            return this;
        }

        @Override
        protected FailedLoginResponseBuilder self() {
            return this;
        }

    }

}
