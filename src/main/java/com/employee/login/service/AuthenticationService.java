package com.employee.login.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee.login.dto.LoginUserDto;
import com.employee.login.dto.RegisterUserDto;
import com.employee.login.entity.Role;
import com.employee.login.entity.RoleEnum;
import com.employee.login.entity.User;
import com.employee.login.repository.RoleRepository;
import com.employee.login.repository.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final RoleRepository roleRepository;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public User signup(RegisterUserDto input) {
		RoleEnum roleEnum = RoleEnum.valueOf(input.getRole().toUpperCase());
		Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
		return optionalRole
				.map((Role role) -> new User().setFirstName(input.getFirstName()).setLastName(input.getLastName())
						.setEmail(input.getEmail()).setPassword(passwordEncoder.encode(input.getPassword()))
						.setRole(role).setMobile(input.getMobile()))
				.map((User user) -> userRepository.save(user)).orElse(null);
	}

	public Optional<User> authenticate(LoginUserDto input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		Optional<User> user = userRepository.findByEmail(input.getEmail());
		if (user == null) {
			throw new NoSuchElementException("User not found");
		}
		return user;
	}
}