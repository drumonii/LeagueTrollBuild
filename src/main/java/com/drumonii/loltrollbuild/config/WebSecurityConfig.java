package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Embedded;
import com.drumonii.loltrollbuild.config.Profiles.External;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers("/riot/**")       .hasRole("ADMIN")
				.antMatchers("/admin/**")      .hasRole("ADMIN")
				.antMatchers(POST,   "/api/**").hasRole("ADMIN")
				.antMatchers(PUT,    "/api/**").hasRole("ADMIN")
				.antMatchers(PATCH,  "/api/**").hasRole("ADMIN")
				.antMatchers(DELETE, "/api/**").hasRole("ADMIN")
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.defaultSuccessUrl("/admin")
				.permitAll()
			.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
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

		@Autowired
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// @formatter:off
			auth
				.inMemoryAuthentication()
					.withUser("admin").password("password").authorities("ROLE_ADMIN");
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
