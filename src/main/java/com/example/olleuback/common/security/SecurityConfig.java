package com.example.olleuback.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final OlleUAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtProvider jwtProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.requestMatchers("/user/login").permitAll()
			.requestMatchers("/user/signup").permitAll()
			.requestMatchers("/docs/**").permitAll()
				.anyRequest().authenticated()
			.and()
			.formLogin().disable().csrf().disable().cors()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
