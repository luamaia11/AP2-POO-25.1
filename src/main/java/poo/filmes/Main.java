package main;

import dao.Conexao;
import dao.UsuarioDAO;
import modelo.Usuario;
import java.sql.Connection;
import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) {
        // Requisito: Conexão com banco relacional via JDBC
        Connection conn = Conexao.conectar();
        UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- BEM-VINDO AO SISTEMA DE AVALIAÇÃO DE FILMES ---");
            System.out.println("1. Fazer Login");
            System.out.println("2. Cadastrar Novo Usuário");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            // Validação de entrada para garantir que o usuário digite um número
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.next(); // Descarta a entrada inválida
                System.out.print("Escolha uma opção: ");
            }
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer do scanner após ler o número

            switch (opcao) {
                case 1:
                    // Lógica de Login, conforme o diagrama de caso de uso
                    System.out.println("\n--- TELA DE LOGIN ---");
                    System.out.print("Digite seu email: ");
                    String emailLogin = scanner.nextLine();
                    System.out.print("Digite sua senha: ");
                    String senhaLogin = scanner.nextLine();

                    Usuario usuarioLogado = usuarioDAO.login(emailLogin, senhaLogin);

                    if (usuarioLogado != null) {
                        System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + "!");
                        // Próximo passo: Chamar o menu de usuário logado aqui
                    } else {
                        System.out.println("\nEmail ou senha inválidos. Tente novamente.");
                    }
                    break;

                case 2:
                    // Lógica de Cadastro, conforme o diagrama de caso de uso
                    System.out.println("\n--- TELA DE CADASTRO ---");
                    System.out.print("Digite seu nome completo: ");
                    String nomeCadastro = scanner.nextLine();
                    System.out.print("Digite seu email: ");
                    String emailCadastro = scanner.nextLine();
                    System.out.print("Crie uma senha: ");
                    String senhaCadastro = scanner.nextLine();

                    // Requisito: Abstração - modelagem adequada das entidades
                    Usuario novoUsuario = new Usuario(0, nomeCadastro, emailCadastro, senhaCadastro);
                    try {
                        // Requisito: Uso obrigatório do padrão DAO para persistência de dados
                        usuarioDAO.salvar(novoUsuario);
                        System.out.println("Usuário '" + novoUsuario.getNome() + "' cadastrado com sucesso!");
                    } catch (RuntimeException e) {
                        System.err.println("Erro ao cadastrar: " + e.getMessage());
                    }
                    break;

                case 3:
                    // Encerra o sistema
                    System.out.println("Saindo do sistema... Até logo!");
                    scanner.close();
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                    }
                    return; // Encerra o programa

                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
    }
}