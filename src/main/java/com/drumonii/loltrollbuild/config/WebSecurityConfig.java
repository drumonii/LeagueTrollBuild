package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Embedded;
import com.drumonii.loltrollbuild.config.Profiles.External;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.boot.autoconfigure.security.SecurityProperties.ACCESS_OVERRIDE_ORDER;
import static org.springframework.http.HttpMethod.*;

/**
 * Configuration for web security/authentication and overriding components in {@link WebSecurityConfigurerAdapter}.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String ADMIN_ROLE = "ADMIN";

	@Autowired
	private RepositoryRestProperties restProperties;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String apiPath = restProperties.getBasePath() + "/**";
		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers("/riot/**")     .hasRole(ADMIN_ROLE)
				.antMatchers("/admin/**")    .hasRole(ADMIN_ROLE)
				.antMatchers(POST,   apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(PUT,    apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(PATCH,  apiPath).hasRole(ADMIN_ROLE)
				.antMatchers(DELETE, apiPath).hasRole(ADMIN_ROLE)
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.defaultSuccessUrl("/admin")
				.permitAll()
			.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
				.permitAll();
		// @formatter:on
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	/**
	 * In memory authentication configuration for {@link Dev} and {@link Testing} profiles.
	 */
	@Order(ACCESS_OVERRIDE_ORDER)
	@Configuration
	@Dev @Testing
	public static class WebDevTestingSecurityConfig extends WebSecurityConfigurerAdapter {

		public static final String IN_MEM_USERNAME = "admin";
		public static final String IN_MEM_PASSWORD = "password";

		@Autowired
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// @formatter:off
			auth
				.inMemoryAuthentication()
					.withUser(IN_MEM_USERNAME).password(IN_MEM_PASSWORD).authorities("ROLE_" + ADMIN_ROLE);
			// @formatter:on
		}

	}

	/**
	 * JDBC authentication configuration for {@link Embedded} and {@link External} profiles.
	 */
	@Order(ACCESS_OVERRIDE_ORDER)
	@Configuration
	@Embedded @External
	public static class WebEmbeddedExternalSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private DataSource dataSource;

		@Autowired
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// @formatter:off
			auth
				.jdbcAuthentication()
					.dataSource(dataSource)
					.passwordEncoder(new BCryptPasswordEncoder());
			// @formatter:on
		}

	}

}
