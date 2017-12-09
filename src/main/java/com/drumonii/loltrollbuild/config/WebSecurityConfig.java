package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Embedded;
import com.drumonii.loltrollbuild.config.Profiles.External;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import com.drumonii.loltrollbuild.security.CsrfTokenExpiredAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

/**
 * Configuration for web security/authentication and overriding components in {@link WebSecurityConfigurerAdapter}.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String ADMIN_ROLE = "ADMIN";

	@Value("${spring.data.rest.base-path}/**")
	private String apiPath;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers("/riot/**")     .hasRole(ADMIN_ROLE)
				.antMatchers("/admin/**")    .hasRole(ADMIN_ROLE)
				.antMatchers(POST,   apiPath + "/builds").permitAll()
				.antMatchers(POST,   apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(PUT,    apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(PATCH,  apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(DELETE, apiPath).hasRole(ADMIN_ROLE)
			.and()
			.headers().cacheControl().disable()
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.defaultSuccessUrl("/admin")
				.permitAll()
			.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
				.permitAll()
			.and()
			.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository())
			.and()
				.exceptionHandling()
					.accessDeniedHandler(new CsrfTokenExpiredAccessDeniedHandler());
		// @formatter:on
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
			manager.createUser(User.withUsername(IN_MEM_USERNAME).password(IN_MEM_PASSWORD).roles(ADMIN_ROLE).build());
			return manager;
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

}
