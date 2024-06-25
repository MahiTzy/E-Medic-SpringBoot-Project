package com.spring.eMedic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.eMedic.entity.Admin;
import com.spring.eMedic.repository.AdminRepository;

@Service
public class AdminServiceImplementation implements AdminService {

	private AdminRepository adminRepository;

	public AdminServiceImplementation(AdminRepository obj) {
		adminRepository = obj;
	}

	@Override
	public List<Admin> findAll() {
		return adminRepository.findAll();
	}

	@Override
	public void save(Admin admin) {

		adminRepository.save(admin);
	}

	@Override
	public Admin findByEmail(String user) {

		return adminRepository.findByEmail(user);

	}

	@Override
	public List<Admin> findByRole(String user) {

		return adminRepository.findByRole(user);
	}

}
