package com.spring.eMedic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.eMedic.entity.Appointment;
import com.spring.eMedic.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation {

	private AppointmentRepository appointmentRepository;

	public AppointmentServiceImplementation(AppointmentRepository obj) {
		appointmentRepository = obj;
	}

	public void save(Appointment app) {

		appointmentRepository.save(app);
	}

	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

}
