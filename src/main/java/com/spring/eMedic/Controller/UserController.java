package com.spring.eMedic.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.eMedic.entity.Admin;
import com.spring.eMedic.entity.Appointment;
import com.spring.eMedic.service.AdminServiceImplementation;
import com.spring.eMedic.service.AppointmentServiceImplementation;

@Controller
@RequestMapping("/user")
public class UserController {

	private final AppointmentServiceImplementation appointmentServiceImplementation;
	private final AdminServiceImplementation adminServiceImplementation;

	public UserController(AppointmentServiceImplementation appointmentServiceImplementation,
			AdminServiceImplementation adminServiceImplementation) {
		this.appointmentServiceImplementation = appointmentServiceImplementation;
		this.adminServiceImplementation = adminServiceImplementation;
	}

	@GetMapping("/index")
	public String index(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/index";
	}

	@PostMapping("/save-app")
	public String saveAppointment(@ModelAttribute("app") Appointment appointment) {
		appointment.setRegtime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		appointmentServiceImplementation.save(appointment);
		return "redirect:/user/index";
	}

	@GetMapping("/about")
	public String about(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/about";
	}

	@GetMapping("/blog-single")
	public String blogSingle(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/blog-single";
	}

	@GetMapping("/blog")
	public String blog(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/blog";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/contact";
	}

	@GetMapping("/department-single")
	public String departmentSingle(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/department-single";
	}

	@GetMapping("/departments")
	public String departments(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/departments";
	}

	@GetMapping("/doctor")
	public String doctor(Model model) {
		setUserAndAppointmentAttributes(model);
		return "user/doctor";
	}

	private void setUserAndAppointmentAttributes(Model model) {
		String username = getUsernameFromSecurityContext();
		Admin admin = adminServiceImplementation.findByEmail(username);

		updateLastSeen(admin);

		Appointment appointment = new Appointment();
		appointment.setName(admin.getFirstName() + " " + admin.getLastName());
		appointment.setEmail(admin.getEmail());

		model.addAttribute("app", appointment);
	}

	private String getUsernameFromSecurityContext() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails details) {
			return details.getUsername();
		} else {
			return principal.toString();
		}
	}

	private void updateLastSeen(Admin admin) {
		String lastSeen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		admin.setLastseen(lastSeen);
		adminServiceImplementation.save(admin);
	}
}
