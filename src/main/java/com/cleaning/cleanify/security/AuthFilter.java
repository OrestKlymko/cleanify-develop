package com.cleaning.cleanify.security;

import com.cleaning.cleanify.auth.model.Role;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.repository.UserRepository;
import com.cleaning.cleanify.auth.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Component
public class AuthFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;
	private final UserService userService;

	@Value("${google.client.id}")
	private String CLIENT_ID;

	public AuthFilter(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String idTokenFromRequest = request.getHeader("Authorization");

		if (idTokenFromRequest != null && !idTokenFromRequest.isEmpty()) {
			GoogleIdTokenVerifier verifier = null;
			try {
				verifier = new GoogleIdTokenVerifier.Builder(
						GoogleNetHttpTransport.newTrustedTransport(),
						JacksonFactory.getDefaultInstance())
						.setAudience(Collections.singletonList(CLIENT_ID))
						.build();
				GoogleIdToken idToken = verifier.verify(idTokenFromRequest);
				if (idToken != null) {
					GoogleIdToken.Payload tokenPayload = idToken.getPayload();
					String email = tokenPayload.getEmail();
					User user = userService.saveUser(email, tokenPayload);
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(), null, null);
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}
		}
		filterChain.doFilter(request, response);
	}

}
