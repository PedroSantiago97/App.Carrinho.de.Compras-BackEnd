package com.app2.productsCatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador responsável pelos endpoints de autenticação e registro de usuários.
 * Gerencia operações de login, registro de novos usuários e autenticação de administradores.
 * 
 * @author SeuNome
 * @since 1.0
 * @version 1.0
 * 
 * @see AuthenticationManager
 * @see UserRepository
 * @see TokenService
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para operações de autenticação e registro de usuários")
public class AuthenticationController {
	
	/**
	 * Gerenciador de autenticação do Spring Security.
	 * Responsável por validar credenciais de usuário.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * Repositório de dados para operações com entidades User.
	 * Fornece métodos para persistência e consulta de usuários.
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Serviço responsável pela geração e validação de tokens JWT.
	 * Utilizado para criar tokens de autenticação após login bem-sucedido.
	 */
	@Autowired
	private TokenService tokenService;
	
	
	/**
	 * Registra um novo usuário no sistema.
	 * Valida se o usuário já existe e criptografa a senha antes de salvar.
	 * 
	 * @param data DTO contendo os dados de registro do usuário
	 * @return ResponseEntity com status 200 em caso de sucesso ou 400 em caso de erro
	 */
	@PostMapping("/register")
	@Operation(
		summary = "Registrar novo usuário",
		description = "Cria uma nova conta de usuário no sistema. Não permite registro com role ADMIN."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Usuário registrado com sucesso"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Erro no registro - usuário já existe ou tentativa de registro como ADMIN",
			content = @Content(schema = @Schema(implementation = Void.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "Dados de entrada inválidos"
		)
	})
	public ResponseEntity<Void> register(
			@Parameter(
				description = "Dados necessários para registro do usuário",
				required = true,
				content = @Content(schema = @Schema(implementation = RegisterDTO.class))
			)
			@RequestBody @Valid RegisterDTO data) {
		
		// Verifica se usuário já existe
		if(this.userRepository.findByLogin(data.login()) != null) {
			return ResponseEntity.badRequest().build();
		}
		
		// Impede registro direto como ADMIN
		if(data.role() == UserRole.ADMIN) {
			return ResponseEntity.badRequest().build();
		}
		
		// Criptografa a senha
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		
		// Cria e salva o novo usuário
		User newUser = new User(data.login(), encryptedPassword, data.role());
		this.userRepository.save(newUser);
		
		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * Realiza login de um usuário comum.
	 * Autentica as credenciais e retorna um token JWT em caso de sucesso.
	 * 
	 * @param data DTO contendo login e senha do usuário
	 * @return ResponseEntity contendo o token JWT ou erro de autenticação
	 */
	@PostMapping("/login")
	@Operation(
		summary = "Login de usuário",
		description = "Autentica um usuário com credenciais válidas e retorna um token JWT para acesso aos recursos protegidos.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Login realizado com sucesso",
			content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
		),
		@ApiResponse(
			responseCode = "401",
			description = "Credenciais inválidas ou usuário não encontrado"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Dados de entrada inválidos"
		)
	})
	public ResponseEntity<LoginResponseDTO> login(
			@Parameter(
				description = "Credenciais de autenticação do usuário",
				required = true,
				content = @Content(schema = @Schema(implementation = AuthenticationDTO.class))
			)
			@RequestBody @Valid AuthenticationDTO data) {
		
		// Cria token de autenticação
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		
		// Autentica o usuário
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		// Gera token JWT
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	/**
	 * Realiza login de um administrador.
	 * Endpoint restrito que verifica credenciais especiais antes da autenticação.
	 * 
	 * @param data DTO contendo login e senha do administrador
	 * @return ResponseEntity contendo o token JWT ou erro de autenticação
	 */
	@PostMapping("/admin")
	@Operation(
		summary = "Login de administrador",
		description = "Autentica um usuário com privilégios de administrador. Requer credenciais especiais."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Admin autenticado com sucesso",
			content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "Credenciais de administrador inválidas"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Falha na autenticação"
		)
	})
	public ResponseEntity<LoginResponseDTO> loginAdmin(
			@Parameter(
				description = "Credenciais de administrador",
				required = true,
				content = @Content(schema = @Schema(implementation = AuthenticationDTO.class))
			)
			@RequestBody @Valid AuthenticationDTO data) {
		
		// Verificação especial para admin (IMPORTANTE: corrija essa verificação!)
		// Problema: comparação de strings com == está errada!
		if(!"gerenciador".equals(data.login())) {
			return ResponseEntity.badRequest().build();
		}
		
		// Cria token de autenticação
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		
		// Autentica o usuário
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		// Gera token JWT
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
}