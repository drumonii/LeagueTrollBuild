package com.drumonii.loltrollbuild.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class NoOpPasswordEncoderTest {

	private NoOpPasswordEncoder noOpPasswordEncoder = new NoOpPasswordEncoder();

	@Test
	public void noOpEncodesPassword() {
		assertThat(noOpPasswordEncoder.encode("world4321")).isEqualTo("world4321");
	}

	@Test
	public void rawPasswordMatchesEncodedPassword() {
		assertThat(noOpPasswordEncoder.matches("hello1234", "hello1234")).isTrue();
	}

}