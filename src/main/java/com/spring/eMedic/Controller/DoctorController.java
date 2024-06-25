package com.spring.eMedic.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.eMedic.entity.Admin;
import com.spring.eMedic.entity.Appointment;
import com.spring.eMedic.service.AdminServiceImplementation;
import com.spring.eMedic.service.AppointmentServiceImplementation;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

	private AdminServiceImplementation adminServiceImplementation;
	private AppointmentServiceImplementation appointmentServiceImplementation;

	public DoctorController(AdminServiceImplementation obj, AppointmentServiceImplementation app) {
		adminServiceImplementation = obj;
		appointmentServiceImplementation = app;
	}

	private Admin updateLastSeen() {
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails details) {
			username = details.getUsername();
		} else {
			username = principal.toString();
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();
		String log = formatter.format(now);

		admin.setLastseen(log);
		adminServiceImplementation.save(admin);

		return admin;
	}

	@RequestMapping("/index")
	public String index(Model model) {
		Admin admin = updateLastSeen();
		List<Appointment> list = appointmentServiceImplementation.findAll();

		model.addAttribute("name", admin.getFirstName());
		model.addAttribute("email", admin.getEmail());
		model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
		model.addAttribute("app", list);

		return "doctor/index";
	}
}
