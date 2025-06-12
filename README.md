## Alunos - Sistema de Avaliação de Filmes

| Matrícula       | Aluno                                         |
|----------------|-----------------------------------------------|
| 202301222401    | Luã Japiassu Macedo Maia                      |
| 202308161946    | Gabriel Mathias Do Carmo Sica                 |
| 202302902448    | João Victor Bathomarco Correa Carneiro        |

---

### ▶️ Instruções de Execução

1. **Pré-requisitos:**
   - Java JDK 11 ou superior
   - MySQL instalado e rodando localmente
   - Maven instalado 
   - Uma IDE compatível com projetos Java Maven (ex: IntelliJ IDEA, Eclipse)

2. **Configuração do Banco de Dados:**
   - Crie um banco no MySQL chamado `filmes_db` (ou outro nome, desde que compatível com o arquivo `Conexao.java`).
   - Execute o script SQL de criação das tabelas (arquivo `create_tables.sql`, se presente).

3. **Importação do Projeto:**
   - Clone o repositório:
     ```bash
     git clone <https://github.com/luamaia11/AP2-POO-25.1.git>
     ```
   - Importe como projeto Maven em sua IDE.

4. **Execução:**
   - Acesse o seguinte caminho no projeto:
     ```
     src/main/java/poo/filmes/MenuPrincipal.java
     ```
   - Execute essa classe para iniciar o sistema via console.

5. **Funcionamento:**
   - O sistema abrirá um menu com as opções:
     - Fazer login
     - Cadastrar novo usuário
     - Cadastrar filme
     - Avaliar filme
