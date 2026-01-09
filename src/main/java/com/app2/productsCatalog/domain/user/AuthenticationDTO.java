package com.app2.productsCatalog.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


/**
 * Data Transfer Object (DTO) para autenticação de usuários.
 * Contém as credenciais necessárias para realizar o login no sistema.
 * 
 * Este DTO é utilizado no endpoint de login (/auth/login) para receber
 * as credenciais do usuário que deseja se autenticar no sistema.
 * 
 * @author PedroSantiago97
 * @since 1.0
 * @version 1.0
 * 
 * @see User
 * @see AuthenticationController#login(AuthenticationDTO)
 * @see LoginResponseDTO
 */
@Schema(
    name = "AuthenticationDTO",
    description = """
        Objeto de transferência de dados para autenticação de usuários.
        
        Contém as credenciais necessárias para realizar login no sistema.
        Após validação bem-sucedida, o sistema retorna um token JWT.
        
        ### Fluxo de autenticação:
        1. Cliente envia este DTO com login e senha
        2. Sistema verifica credenciais no banco de dados
        3. Se válido: retorna LoginResponseDTO com token JWT
        4. Se inválido: retorna erro 401 Unauthorized
        
        ### Exemplo de uso:
        ```json
        {
          "login": "usuario@email.com",
          "password": "MinhaSenha123"
        }
        ```
        """,
    example = """
        {
          "login": "joao.silva@email.com",
          "password": "SenhaSegura456"
        }
        """
)
public record AuthenticationDTO(
    
    @Schema(
        description = """
            Login do usuário para autenticação.
            
            Pode ser o email ou nome de usuário cadastrado no sistema.
            Deve corresponder exatamente ao valor cadastrado (case-sensitive).
            
            ### Exemplos válidos:
            - usuario@email.com
            - joao.silva
            - cliente123
            """,
        example = "usuario@email.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "O login é obrigatório")
    String login,
    
    @Schema(
        description = """
            Senha do usuário para autenticação.
            
            Deve corresponder à senha cadastrada no momento do registro.
            A senha é verificada contra o hash armazenado no banco de dados.
            
            ### Importante:
            - Enviada em texto plano (HTTPS é recomendado)
            - Nunca armazenada em texto plano no servidor
            - Comparada com hash BCrypt armazenado
            """,
        example = "MinhaSenha123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        writeOnly = true
    )
    @NotBlank(message = "A senha é obrigatória")
    String password) {    
}