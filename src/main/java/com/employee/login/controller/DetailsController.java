package com.employee.login.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.login.entity.User;
import com.employee.login.service.JwtService;
import com.employee.login.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class DetailsController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/details")
	public ResponseEntity<User> getUserDetails(@RequestHeader("Authorization") String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			String jwtToken = token.substring(7);
			String email = jwtService.extractUsername(jwtToken);

			Optional<User> userOptional = userService.findByEmail(email);
			return userOptional.map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
