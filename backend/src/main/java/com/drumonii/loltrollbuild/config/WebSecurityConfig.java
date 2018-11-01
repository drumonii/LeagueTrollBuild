package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Embedded;
import com.drumonii.loltrollbuild.config.Profiles.External;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import com.drumonii.loltrollbuild.security.login.JsonAuthenticationFailureHandler;
import com.drumonii.loltrollbuild.security.login.JsonAuthenticationSuccessHandler;
import com.drumonii.loltrollbuild.security.logout.JsonLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * Configuration for web security/authentication and overriding components in {@link WebSecurityConfigurerAdapter}.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${api.base-path}/**")
	private String apiPath;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(UserRole.ADMIN)
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.loginProcessingUrl(apiPath + "/admin/login")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
				.permitAll()
			.and()
			.logout()
				.logoutUrl(apiPath + "/admin/logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.permitAll()
			.and()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		// @formatter:on
	}

	// login

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new JsonAuthenticationSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new JsonAuthenticationFailureHandler();
	}

	// logout

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new JsonLogoutSuccessHandler();
	}

	/**
	 * In memory authentication configuration for {@link Dev} and {@link Testing} profiles.
	 */
	@Configuration
	@Dev @Testing
	public static class WebDevTestingSecurityConfig {

		public static final String IN_MEM_USERNAME = "admin";
		public static final String IN_MEM_PASSWORD = "password";

		@Bean
		public UserDetailsService userDetailsService() {
			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			manager.createUser(User.withUsername(IN_MEM_USERNAME)
					.password("{noop}" + IN_MEM_PASSWORD)
					.roles(UserRole.ADMIN)
					.build());
			return manager;
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}

	}

	/**
	 * JDBC authentication configuration for {@link Embedded} and {@link External} profiles.
	 */
	@Configuration
	@Embedded @External
	public static class WebEmbeddedExternalSecurityConfig {

		@Bean
		public UserDetailsService userDetailsService(DataSource dataSource) {
			JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
			manager.setDataSource(dataSource);
			return manager;
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

	}

	public static class UserRole {

		private UserRole() {}

		public static final String ADMIN = "ADMIN";

	}

}
