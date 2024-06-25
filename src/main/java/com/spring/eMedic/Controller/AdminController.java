package com.spring.eMedic.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/admin")
public class AdminController {

	private AdminServiceImplementation adminServiceImplementation;
	private AppointmentServiceImplementation appointmentServiceImplementation;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public AdminController(AdminServiceImplementation obj, AppointmentServiceImplementation app,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.adminServiceImplementation = obj;
		this.appointmentServiceImplementation = app;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	private void updateLastSeen() {
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
	}

	@RequestMapping("/user-details")
	public String index(Model model) {
		updateLastSeen();
		List<Admin> list = adminServiceImplementation.findByRole("ROLE_USER");
		model.addAttribute("user", list);
		return "admin/user";
	}

	@RequestMapping("/doctor-details")
	public String doctorDetails(Model model) {
		updateLastSeen();
		List<Admin> list = adminServiceImplementation.findByRole("ROLE_DOCTOR");
		model.addAttribute("user", list);
		return "admin/doctor";
	}

	@RequestMapping("/admin-details")
	public String adminDetails(Model model) {
		updateLastSeen();
		List<Admin> list = adminServiceImplementation.findByRole("ROLE_ADMIN");
		model.addAttribute("user", list);
		return "admin/admin";
	}

	@GetMapping("/add-doctor")
	public String showFormForAdd(Model theModel) {
		updateLastSeen();
		Admin admin = new Admin();
		theModel.addAttribute("doctor", admin);
		return "admin/addDoctor";
	}

	@PostMapping("/save-doctor")
	public String saveDoctor(@ModelAttribute("doctor") Admin admin) {
		admin.setRole("ROLE_DOCTOR");
		admin.setPassword(bCryptPasswordEncoder.encode("default@123"));
		admin.setEnabled(true);
		admin.setConfirmationToken("ByAdmin-Panel");
		adminServiceImplementation.save(admin);
		return "redirect:/admin/user-details";
	}

	@GetMapping("/add-admin")
	public String showForm(Model theModel) {
		updateLastSeen();
		Admin admin = new Admin();
		theModel.addAttribute("doctor", admin);
		return "admin/addAdmin";
	}

	@PostMapping("/save-admin")
	public String saveAdmin(@ModelAttribute("doctor") Admin admin) {
		admin.setRole("ROLE_ADMIN");
		admin.setPassword(bCryptPasswordEncoder.encode("default@123"));
		admin.setEnabled(true);
		admin.setConfirmationToken("ByAdmin-Panel");
		adminServiceImplementation.save(admin);
		return "redirect:/admin/user-details";
	}

	@GetMapping("/edit-my-profile")
	public String editForm(Model theModel) {
		updateLastSeen();
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails details) {
			username = details.getUsername();
		} else {
			username = principal.toString();
		}
		Admin admin = adminServiceImplementation.findByEmail(username);
		theModel.addAttribute("profile", admin);
		return "admin/updateMyProfile";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("profile") Admin admin) {
		adminServiceImplementation.save(admin);
		return "redirect:/admin/user-details";
	}

	@RequestMapping("/appointments")
	public String appointments(Model model) {
		updateLastSeen();
		List<Appointment> list = appointmentServiceImplementation.findAll();
		model.addAttribute("app", list);
		return "admin/appointment";
	}
}
