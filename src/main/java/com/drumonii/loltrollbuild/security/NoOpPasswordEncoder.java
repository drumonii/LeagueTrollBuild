package com.drumonii.loltrollbuild.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@link PasswordEncoder} used for testing purposes.
 */
public class NoOpPasswordEncoder implements PasswordEncoder {

	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}

}
