package com.spring.eMedic.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.eMedic.entity.User;

import lombok.NonNull;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

	User findByEmail(String email);

	User findByConfirmationToken(String confirmationToken);

	@SuppressWarnings("null")
	@NonNull
	List<User> findAll();
}