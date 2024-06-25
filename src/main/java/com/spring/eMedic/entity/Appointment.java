package com.spring.eMedic.entity;

import org.springframework.data.annotation.Transient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "app")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "date")
	private String date;

	@Column(name = "time")
	private String time;

	@Column(name = "description")
	private String description;

	@Column(name = "regtime")
	@Transient
	private String regtime;

}