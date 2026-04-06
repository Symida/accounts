package com.symida.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;
}


