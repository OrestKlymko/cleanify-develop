package com.cleaning.cleanify.auth.controller;


import com.cleaning.cleanify.auth.UserService;
import com.cleaning.cleanify.auth.dto.UserUpdateRequest;
import com.cleaning.cleanify.auth.dto.UserUpdateResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@GetMapping
	public UserUpdateResponse getUserInfo() {
		return userService.getUserInfo();
	}

	@PutMapping
	public UserUpdateResponse updateUser(@RequestBody UserUpdateRequest request) {
		return userService.updateUser(request);
	}
}
