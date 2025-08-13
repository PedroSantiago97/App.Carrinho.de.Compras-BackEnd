package com.app2.productsCatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app2.productsCatalog.domain.user.AuthenticationDTO;
import com.app2.productsCatalog.domain.user.LoginResponseDTO;
import com.app2.productsCatalog.domain.user.RegisterDTO;
import com.app2.productsCatalog.domain.user.User;
import com.app2.productsCatalog.domain.user.UserRole;
import com.app2.productsCatalog.repository.UserRepository;
import com.app2.productsCatalog.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
		if(this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
		if(data.role() == UserRole.ADMIN) return ResponseEntity.badRequest().build();
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		
		User newUser = new User(data.login(), encryptedPassword, data.role());
		this.userRepository.save(newUser);
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
	
		var auth = this.authenticationManager.authenticate(usernamePassword);
	
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/admin")
	public ResponseEntity<LoginResponseDTO> loginAdmin(@RequestBody @Valid AuthenticationDTO data) {
		if(data.login() != "gerenciador") return ResponseEntity.badRequest().build();
		
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		
		
	
		var auth = this.authenticationManager.authenticate(usernamePassword);
	
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	
	
	
}
