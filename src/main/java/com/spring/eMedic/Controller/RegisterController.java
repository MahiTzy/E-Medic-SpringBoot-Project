package com.spring.eMedic.Controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.spring.eMedic.entity.User;
import com.spring.eMedic.service.EmailService;
import com.spring.eMedic.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class RegisterController {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserService userService;
	private EmailService emailService;

	public RegisterController(UserService userService, EmailService emailService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.emailService = emailService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping("/register")
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;
	}

	@PostMapping("/register")
	public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user,
			BindingResult bindingResult, HttpServletRequest request) {
		User userExists = userService.findByEmail(user.getEmail());

		if (userExists != null) {
			modelAndView.addObject("alreadyRegisteredMessage",
					"Oops! There is already a user registered with the email provided.");
			modelAndView.setViewName("register");
			bindingResult.reject("email");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("register");
		} else {
			user.setEnabled(false);
			user.setRole("ROLE_USER");
			user.setConfirmationToken(UUID.randomUUID().toString());
			userService.saveUser(user);

			String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

			sendConfirmationEmail(user, appUrl);

			modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
			modelAndView.setViewName("register");
		}

		return modelAndView;
	}

	private void sendConfirmationEmail(User user, String appUrl) {
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
				+ appUrl + "/confirm?token=" + user.getConfirmationToken());
		registrationEmail.setFrom("spring.email.auth@gmail.com");
		emailService.sendEmail(registrationEmail);
	}

	@GetMapping("/confirm")
	public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam String token) {
		User user = userService.findByConfirmationToken(token);

		if (user == null) {
			modelAndView.addObject("invalidToken", "Oops! This is an invalid confirmation link.");
		} else {
			modelAndView.addObject("confirmationToken", user.getConfirmationToken());
		}

		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	@PostMapping("/confirm")
	public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map<String, String> requestParams, RedirectAttributes redir) {
		modelAndView.setViewName("confirm");

		Zxcvbn passwordCheck = new Zxcvbn();
		Strength strength = passwordCheck.measure(requestParams.get("password"));

		if (strength.getScore() < 3) {
			bindingResult.reject("password");
			redir.addFlashAttribute("errorMessage", "Your password is too weak. Choose a stronger one.");
			modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
			return modelAndView;
		}

		User user = userService.findByConfirmationToken(requestParams.get("token"));
		user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
		user.setEnabled(true);
		userService.saveUser(user);

		modelAndView.addObject("successMessage", "Your password has been set!");
		return modelAndView;
	}
}
