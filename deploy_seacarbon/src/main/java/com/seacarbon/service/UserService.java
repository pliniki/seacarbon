package com.seacarbon.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seacarbon.model.User;
import com.seacarbon.model.UserLogin;
import com.seacarbon.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public User CadastrarUsuario(User usuario) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);

		return repository.save(usuario);
	}

	public Optional<UserLogin> Logar(Optional<UserLogin> user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<User> usuario = repository.findByUsuario(user.get().getUsuario());

		if (usuario.isPresent()) {
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {

				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodeAuth);

				user.get().setToken(authHeader);
				user.get().setId(usuario.get().getId());
				user.get().setNome(usuario.get().getNome());
				user.get().setFoto(usuario.get().getFoto());
				user.get().setTipo(usuario.get().getTipo());
				user.get().setSenha(usuario.get().getSenha());

				return user;
			}
		}

		return null;
	}
	
	public Optional<User> atualizarUsuario(User usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {
			Optional<User> buscaUsuario = repository.findByUsuario(usuario.getUsuario());

			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			return Optional.ofNullable(repository.save(usuario));
		}
		return Optional.empty();
	}
	
	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	}

}
