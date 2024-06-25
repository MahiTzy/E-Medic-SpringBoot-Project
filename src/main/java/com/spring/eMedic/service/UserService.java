package com.spring.eMedic.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.eMedic.entity.User;
import com.spring.eMedic.repository.UserRepository;

import lombok.NonNull;

@Service("userService")
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	@NonNull
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users != null ? users : Collections.emptyList();
    }

}