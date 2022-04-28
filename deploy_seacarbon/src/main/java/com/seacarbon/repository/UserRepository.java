package com.seacarbon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seacarbon.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByUsuario(String usuario);
}
