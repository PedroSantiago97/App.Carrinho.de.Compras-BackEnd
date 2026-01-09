package com.app2.productsCatalog.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) para registro de novos usuários no sistema.
 * Contém os dados necessários para criar uma nova conta de usuário.
 * 
 * Este DTO é utilizado no endpoint de registro (/auth/register) para receber
 * as informações básicas do usuário que deseja se cadastrar no sistema.
 * 
 * @author PedroSantiago97
 * @since 1.0
 * @version 1.0
 * 
 * @see User
 * @see UserRole
 * @see AuthenticationController#register(RegisterDTO)
 */
@Schema(
    name = "RegisterDTO",
    description = """
        Objeto de transferência de dados para registro de novos usuários.
        
        ### Validações:
        - **login**: Obrigatório, deve ser um email válido ou nome de usuário único
        - **password**: Obrigatório, mínimo 6 caracteres para segurança
        - **role**: Obrigatório, define o perfil do usuário (USER ou ADMIN)
        
        ### Regras de negócio:
        1. O login deve ser único no sistema
        2. A senha será criptografada antes do armazenamento
        3. Registro como ADMIN é restrito (geralmente apenas por administradores existentes)
        4. Usuários comuns só podem se registrar como USER
        
        ### Exemplo de uso:
        ```json
        {
          "login": "novousuario@email.com",
          "password": "MinhaSenha123",
          "role": "USER"
        }
        ```
        """,
    example = """
        {
          "login": "joao.silva@email.com",
          "password": "SenhaSegura456",
          "role": "USER"
        }
        """
)
public record RegisterDTO(
    
    @Schema(
        description = """
            Identificador do usuário para login no sistema.
            
            Pode ser um endereço de email ou um nome de usuário único.
            Este valor será utilizado para autenticação em login futuros.
            
            ### Requisitos:
            - Deve ser único no sistema
            - Recomenda-se usar email válido
            - Case-sensitive dependendo da configuração
            
            ### Exemplos válidos:
            - usuario@email.com
            - joao.silva
            - cliente123
            """,
        example = "usuario@email.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 100
    )
    @NotBlank(message = "O login é obrigatório")
    @Size(min = 3, max = 100, message = "O login deve ter entre 3 e 100 caracteres")
    String login,
    
    @Schema(
        description = """
            Senha do usuário para autenticação.
            
            A senha será criptografada usando BCrypt antes de ser armazenada
            no banco de dados. Nunca é armazenada em texto plano.
            
            ### Recomendações de segurança:
            - Mínimo 6 caracteres
            - Combine letras maiúsculas, minúsculas e números
            - Evite senhas comuns ou pessoais
            - Considere usar um gerenciador de senhas
            
            ### Processo de segurança:
            1. Usuário informa senha em texto plano
            2. Sistema aplica BCrypt com custo 10
            3. Hash resultante é armazenado (nunca a senha original)
            4. Senha original é descartada
            """,
        example = "MinhaSenha123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6,
        maxLength = 100,
        writeOnly = true
    )
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    String password,
    
    @Schema(
        description = """
            Perfil do usuário no sistema.
            
            Define as permissões e acesso que o usuário terá.
            Usuários comuns geralmente só podem se registrar como USER.
            
            ### Valores possíveis:
            - **USER**: Usuário comum com acesso básico
            - **ADMIN**: Administrador com acesso completo (geralmente restrito)
            
            ### Regras de registro:
            - Registro público geralmente permite apenas USER
            - ADMIN geralmente é atribuído apenas por administradores existentes
            - O sistema pode recusar registros com role ADMIN
            """,
        example = "USER",
        requiredMode = Schema.RequiredMode.REQUIRED,
        defaultValue = "USER"
    )
    @NotNull(message = "O perfil do usuário é obrigatório")
    UserRole role ) {
	
}