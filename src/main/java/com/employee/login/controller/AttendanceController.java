package com.employee.login.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.login.repository.UserRepository;
import com.employee.login.service.AttendanceService;
import com.employee.login.service.JwtService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/mark")
	public ResponseEntity<String> markAttendance(@RequestHeader("Authorization") String token) {
		String email = jwtService.extractUsername(token.substring(7));
		Long userId = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"))
				.getId();
		Date currentDate = new Date();
		Date dateWithoutTime = attendanceService.stripTime(currentDate);

		if (!attendanceService.isAttendanceMarked(userId, dateWithoutTime)) {
			attendanceService.markAttendance(userId, true);
			return ResponseEntity.ok("Attendance marked successfully");
		} else {
			return ResponseEntity.badRequest().body("Attendance already marked for today");
		}
	}
}
