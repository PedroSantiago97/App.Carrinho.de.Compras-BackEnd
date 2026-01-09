package com.app2.productsCatalog.domain.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um usuário do sistema.
 * Esta classe armazena as informações de login, senha e permissões do usuário.
 * O Spring Security usa esta classe para fazer autenticação e autorização.
 */
@Table(name = "users")
@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa um usuário do sistema")
public class User implements UserDetails {
    
    /**
     * ID único do usuário. É gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    /**
     * Login do usuário (pode ser email ou nome de usuário).
     */
    @Schema(description = "Login do usuário (email ou username)", example = "usuario@email.com")
    private String login;
    
    /**
     * Senha do usuário (armazenada criptografada).
     */
    @Schema(description = "Senha criptografada do usuário")
    private String password;
    
    /**
     * Tipo de usuário: USER ou ADMIN.
     * Define o que o usuário pode fazer no sistema.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de usuário: USER ou ADMIN", example = "USER")
    private UserRole role;
    
    /**
     * Cria um novo usuário.
     * 
     * @param login Login do usuário
     * @param password Senha já criptografada
     * @param role Tipo de usuário (USER ou ADMIN)
     */
    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
    
    /**
     * Retorna o ID do usuário.
     */
    public UUID getId() {
        return this.id;
    }
    
    /**
     * Retorna as permissões do usuário.
     * ADMIN tem permissões de ADMIN e USER.
     * USER tem apenas permissões de USER.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"), 
                new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Retorna o login do usuário (Spring Security chama de "username").
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * A conta nunca expira.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * A conta nunca é bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * As credenciais nunca expiram.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * O usuário está sempre ativo.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
