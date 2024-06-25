package com.spring.eMedic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.eMedic.entity.Admin;

@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByEmail(String user);
	
	List<Admin> findByRole(String user);
}