package com.seacarbon.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seacarbon.model.User;
import com.seacarbon.model.UserLogin;
import com.seacarbon.repository.UserRepository;
import com.seacarbon.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<UserLogin> Autentication(@RequestBody Optional<UserLogin> user) {
		return userService.Logar(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/signup")
	public ResponseEntity<User> Post(@RequestBody User usuario) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.CadastrarUsuario(usuario));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable Long id) {
		return userRepository.findById(id)
			.map(resposta -> ResponseEntity.ok(resposta))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<User> putUsuario(@Valid @RequestBody User usuario) {
		return userService.atualizarUsuario(usuario)
			.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
}