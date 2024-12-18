package com.cleaning.cleanify.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthFilter authFilter;

	public SecurityConfig(AuthFilter authFilter) {
		this.authFilter = authFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/ws/**","/ws","ws").permitAll()
								.anyRequest().authenticated()
				)
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}



}
