package com.cleaning.cleanify.security;

import com.cleaning.cleanify.auth.UserService;
import com.cleaning.cleanify.auth.dto.UserUpdateResponse;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.repository.UserRepository;
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

	@Value("${google.client.id}")
	private String CLIENT_ID;

	public AuthFilter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String idTokenFromRequest = request.getHeader("Authorization");
		if (idTokenFromRequest != null) {
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

					User savedUser = userRepository.findByEmail(email).orElseGet(() -> {
						String givenName = (String) tokenPayload.get("given_name");
						User user = new User();
						user.setEmail(email);
						user.setFirstName(givenName);
						return userRepository.save(user);
					});
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(savedUser.getId(), null, null);
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}

			filterChain.doFilter(request, response);
		}
	}
}
