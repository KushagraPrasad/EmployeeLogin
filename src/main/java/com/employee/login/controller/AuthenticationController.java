package com.employee.login.controller;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.login.dto.LoginResponse;
import com.employee.login.dto.LoginUserDto;
import com.employee.login.entity.User;
import com.employee.login.repository.UserRepository;
import com.employee.login.service.AuthenticationService;
import com.employee.login.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

	private final JwtService jwtService;

	private final AuthenticationService authenticationService;

	private final UserRepository userRepository;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
			UserRepository userRepository) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
		this.userRepository = userRepository;
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		try {
			token = token.substring(7);
			String email = jwtService.extractUsername(token);
			UserDetails userDetails = userRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found"));

			if (jwtService.isTokenValid(token, userDetails)) {
				System.out.println("Token is valid for user: " + email);
				return ResponseEntity.ok().build();
			} else {
				System.out.println("Token is invalid for user: " + email);
				return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid token");
			}
		} catch (Exception e) {
			System.out.println("Error validating token: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid token");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
		try {
			Optional<User> authenticatedUser = authenticationService.authenticate(loginUserDto);
			User user = authenticatedUser.orElseThrow(() -> new BadCredentialsException("Invalid login credentials"));
			String jwtToken = jwtService.generateToken(user);
			long expiresIn = jwtService.getExpirationTime();
			LoginResponse loginResponse = new LoginResponse(jwtToken, expiresIn, user.getEmail(), user.getFirstName(),
					user.getLastName(), user.getMobile(), user.getRole().getName().toString());
			return ResponseEntity.ok(loginResponse);
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(401).body("Invalid email or password."); // Return 401 status code
		} catch (UsernameNotFoundException ex) {
			return ResponseEntity.status(401).body("Invalid email or password."); // Return 401 status code
		}
	}
}