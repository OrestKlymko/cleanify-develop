package com.cleaning.cleanify.auth.service;

import com.cleaning.cleanify.auth.dto.UserUpdateRequest;
import com.cleaning.cleanify.auth.dto.UserUpdateResponse;
import com.cleaning.cleanify.auth.model.Role;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.repository.UserRepository;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow();
	}

	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUsers = authentication.getName();
		return userRepository.findById(Long.valueOf(idUsers)).orElseThrow();
	}

	public UserUpdateResponse getUserInfo() {
		User authenticatedUser = getAuthenticatedUser();
		return new UserUpdateResponse(
				authenticatedUser.getEmail(),
				authenticatedUser.getFirstName(),
				authenticatedUser.getBirthDate(),
				authenticatedUser.getPhoneNumber(),
				authenticatedUser.getAddress()
		);
	}

	public User saveUser(String email, GoogleIdToken.Payload tokenPayload) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			String givenName = (String) tokenPayload.get("given_name");
			User user = new User();
			user.setEmail(email);
			user.setFirstName(givenName);
			if (email.equals("cleanifybee@gmail.com")) {
				user.setRole(Role.ADMIN);
			} else {
				user.setRole(Role.USER);
			}
			return userRepository.save(user);
		});
	}

	public UserUpdateResponse updateUser(UserUpdateRequest userRequest) {
		User user = getAuthenticatedUser();
		user.setFirstName(userRequest.givenName());
		user.setBirthDate(userRequest.birthDate());
		user.setPhoneNumber(userRequest.phone());
		user.setAddress(userRequest.address());
		User savedUser = userRepository.save(user);
		return new UserUpdateResponse(
				savedUser.getEmail(),
				savedUser.getFirstName(),
				savedUser.getBirthDate(),
				savedUser.getPhoneNumber(),
				savedUser.getAddress()
		);
	}
}