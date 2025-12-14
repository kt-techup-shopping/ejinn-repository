package com.kt.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kt.security.DefaultCurrentUser;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
	@Bean
	public AuditorAware<?> auditorProvider() {
		return () -> {
			var principal = SecurityContextHolder
				.getContext()
				.getAuthentication();

			System.out.println("principal = " + principal);
			return principal instanceof DefaultCurrentUser currentUser ?
				Optional.of(currentUser.getId()) :
				Optional.empty();
		};
	}
}