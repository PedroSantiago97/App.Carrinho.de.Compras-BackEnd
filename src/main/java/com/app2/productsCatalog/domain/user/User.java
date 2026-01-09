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
 * Entidade que representa um usuário do sistema.
 * Implementa a interface UserDetails do Spring Security para integração
 * completa com o sistema de autenticação e autorização.
 * 
 * Esta classe mapeia para a tabela "users" no banco de dados e contém
 * as informações necessárias para autenticação, autorização e gestão
 * do ciclo de vida da conta do usuário.
 * 
 * @author SeuNome
 * @since 1.0
 * @version 1.0
 * 
 * @see UserDetails
 * @see UserRole
 * @see GrantedAuthority
 */
@Table(name = "users")
@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "User",
    description = """
        Entidade que representa um usuário do sistema com funcionalidades completas
        de autenticação e autorização através do Spring Security.
        
        ### Características:
        - **Autenticação**: Implementa UserDetails para integração com Spring Security
        - **Autorização**: Suporte a roles (USER e ADMIN) com hierarquia de permissões
        - **Persistência**: Mapeamento JPA para tabela "users" no banco de dados
        - **Segurança**: Métodos para controle de estado da conta (expirada, bloqueada, etc.)
        
        ### Hierarquia de Roles:
        - **ADMIN**: Possui todas as permissões de USER + permissões administrativas
        - **USER**: Permissões básicas de usuário autenticado
        
        ### Exemplo de uso:
        ```java
        // Criação de um usuário comum
        User user = new User("usuario@email.com", "senhaCriptografada", UserRole.USER);
        
        // Criação de um administrador
        User admin = new User("admin@email.com", "senhaCriptografada", UserRole.ADMIN);
        ```
        """,
    example = """
        {
          "id": "123e4567-e89b-12d3-a456-426614174000",
          "login": "usuario@email.com",
          "password": "$2a$10$N9qo8uLOickgx2ZMRZoMye.MdVD3lK6CwCWK6KQ8G0e7.5J2T8FqW",
          "role": "USER"
        }
        """
)
public class User implements UserDetails {
    
    /**
     * Identificador único universal do usuário.
     * Gerado automaticamente pelo banco de dados usando o padrão UUID.
     * 
     * Este campo é a chave primária da entidade e garante unicidade
     * mesmo em sistemas distribuídos ou com alta concorrência.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(
        description = """
            Identificador único universal (UUID) do usuário.
            
            ### Características:
            - Gerado automaticamente pelo banco de dados
            - Garante unicidade global
            - Imutável após criação
            - Utilizado como chave primária
            
            ### Formato:
            Versão 4 do UUID (aleatório)
            """,
        example = "123e4567-e89b-12d3-a456-426614174000",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "uuid",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;
    
    /**
     * Identificador de login do usuário.
     * Pode ser um endereço de email, nome de usuário ou qualquer
     * outro identificador único utilizado para autenticação.
     */
    @Schema(
        description = """
            Identificador utilizado para login no sistema.
            
            ### Características:
            - Deve ser único no sistema
            - Utilizado como username no processo de autenticação
            - Pode ser um email, CPF, nome de usuário, etc.
            
            ### Restrições:
            - Máximo de 255 caracteres (limitação do banco de dados)
            - Não pode ser nulo ou vazio
            - Case-sensitive dependendo da configuração
            """,
        example = "usuario@email.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 255
    )
    private String login;
    
    /**
     * Senha criptografada do usuário.
     * Deve ser armazenada utilizando um algoritmo de hash seguro
     * (como BCrypt) antes da persistência no banco de dados.
     */
    @Schema(
        description = """
            Senha do usuário após processo de criptografia/hash.
            
            ### Características:
            - Sempre armazenada criptografada (nunca em texto plano)
            - Utiliza BCryptPasswordEncoder para hash
            - Inclui salt automático para maior segurança
            
            ### Processo:
            1. Usuário fornece senha em texto plano
            2. Sistema aplica BCrypt com custo 10-12
            3. Hash resultante é armazenado neste campo
            
            ### Exemplo de hash BCrypt:
            ```
            $2a$10$N9qo8uLOickgx2ZMRZoMye.MdVD3lK6CwCWK6KQ8G0e7.5J2T8FqW
            ```
            """,
        example = "$2a$10$N9qo8uLOickgx2ZMRZoMye.MdVD3lK6CwCWK6KQ8G0e7.5J2T8FqW",
        requiredMode = Schema.RequiredMode.REQUIRED,
        writeOnly = true
    )
    private String password;
    
    /**
     * Papel (role) do usuário no sistema.
     * Define o nível de acesso e as permissões disponíveis.
     * Armazenado como string no banco de dados para legibilidade.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
        description = """
            Papel (role) do usuário que define suas permissões no sistema.
            
            ### Roles disponíveis:
            - **USER**: Usuário comum com permissões básicas
            - **ADMIN**: Administrador com permissões completas
            
            ### Hierarquia:
            ```
            ADMIN
            └── USER (inclui todas as permissões de USER)
            ```
            
            ### Comportamento:
            - ADMIN herda automaticamente todas as permissões de USER
            - Validações de segurança são baseadas nestas roles
            - Pode ser extendido para suportar mais roles no futuro
            """,
        example = "USER",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"USER", "ADMIN"}
    )
    private UserRole role;
    
    /**
     * Construtor para criação de instâncias de User sem ID.
     * Utilizado principalmente durante o registro de novos usuários,
     * onde o ID será gerado automaticamente pelo banco de dados.
     * 
     * @param login Identificador de login do usuário
     * @param password Senha já criptografada utilizando BCrypt
     * @param role Papel do usuário no sistema (USER ou ADMIN)
     * 
     * @throws IllegalArgumentException se login, password ou role forem nulos
     * @throws IllegalStateException se a senha não estiver criptografada
     */
    public User(String login, String password, UserRole role) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login não pode ser nulo ou vazio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password não pode ser nulo ou vazio");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role não pode ser nula");
        }
        
        this.login = login.trim();
        this.password = password;
        this.role = role;
    }
    
    /**
     * Retorna o identificador único do usuário.
     * 
     * @return UUID do usuário, ou null se ainda não persistido
     */
    public UUID getId() {
        return this.id;
    }
    
    /**
     * Retorna as autoridades (permissões) concedidas ao usuário.
     * O Spring Security utiliza esta informação para determinar
     * quais recursos e operações o usuário pode acessar.
     * 
     * ### Estrutura de Authorities:
     * - USER: ROLE_USER
     * - ADMIN: ROLE_ADMIN, ROLE_USER
     * 
     * @return Coleção de autoridades baseadas na role do usuário
     * 
     * @see SimpleGrantedAuthority
     * @see GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"), 
                new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Retorna o nome de usuário utilizado para autenticação.
     * Este método é requerido pela interface UserDetails e
     * retorna o valor do campo login.
     * 
     * @return Login do usuário
     */
    @Override
    @Schema(hidden = true)
    public String getUsername() {
        return login;
    }

    /**
     * Indica se a conta do usuário não expirou.
     * Implementação padrão retorna true, indicando que contas
     * neste sistema não possuem data de expiração.
     * 
     * @return true se a conta não expirou, false caso contrário
     */
    @Override
    @Schema(hidden = true)
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário não está bloqueada.
     * Implementação padrão retorna true, indicando que contas
     * neste sistema não são bloqueadas automaticamente.
     * 
     * @return true se a conta não está bloqueada, false caso contrário
     */
    @Override
    @Schema(hidden = true)
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais do usuário não expiraram.
     * Implementação padrão retorna true, indicando que senhas
     * neste sistema não possuem data de expiração.
     * 
     * @return true se as credenciais não expiraram, false caso contrário
     */
    @Override
    @Schema(hidden = true)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário está habilitado (ativo).
     * Implementação padrão retorna true, indicando que todos
     * os usuários estão ativos por padrão.
     * 
     * @return true se o usuário está habilitado, false caso contrário
     */
    @Override
    @Schema(hidden = true)
    public boolean isEnabled() {
        return true;
    }
    
    /**
     * Verifica se o usuário possui role de administrador.
     * Método de conveniência para verificações de permissão.
     * 
     * @return true se o usuário é ADMIN, false caso contrário
     */
    @Schema(hidden = true)
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    
    /**
     * Verifica se o usuário possui role de usuário comum.
     * Note que administradores também possuem permissões de USER.
     * 
     * @return true se o usuário tem permissão USER (sempre true)
     */
    @Schema(hidden = true)
    public boolean isUser() {
        return true; // Todos os usuários têm pelo menos role USER
    }
    
    /**
     * Retorna uma representação em string segura do usuário
     * (não inclui a senha por questões de segurança).
     * 
     * @return String representando o usuário sem informações sensíveis
     */
    @Override
    @Schema(hidden = true)
    public String toString() {
        return String.format(
            "User{id=%s, login='%s', role=%s}",
            id != null ? id.toString() : "null",
            login,
            role
        );
    }
}