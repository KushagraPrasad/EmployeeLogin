package com.employee.login.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.login.entity.Attendance;
import com.employee.login.repository.AttendanceRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	public Date stripTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public boolean isAttendanceMarked(Long userId, Date date) {
		Date dateWithoutTime = stripTime(date);
		boolean exists = attendanceRepository.existsByIdAndDate(userId, dateWithoutTime);
		System.out.println("Checking if attendance exists for userId: " + userId + " on date: " + dateWithoutTime
				+ " - " + exists);
		return exists;
	}

	public void markAttendance(Long userId, boolean present) {
		Attendance attendance = new Attendance();
		attendance.setId(userId);
		Date date = new Date();
		Date dateWithoutTime = stripTime(date);
		attendance.setDate(dateWithoutTime);
		attendance.setPresent(present);
		attendanceRepository.save(attendance);
	}
}
