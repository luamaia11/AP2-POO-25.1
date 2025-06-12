package poo.filmes;

import dao.AvaliacaoDAO;
import dao.Conexao;
import dao.FilmeDAO;
import dao.UsuarioDAO;
import modelo.Usuario;

import java.sql.Connection;
import java.util.Scanner;

public class MenuPrincipal {

    // DAOs que serão utilizados em toda a classe
    private static UsuarioDAO usuarioDAO;
    private static FilmeDAO filmeDAO;
    private static AvaliacaoDAO avaliacaoDAO;

    public static void main(String[] args) {
        // requisito: conexão com banco relacional via jdbc
        Connection conn = Conexao.conectar();
        usuarioDAO = new UsuarioDAO(conn);
        filmeDAO = new FilmeDAO(conn);
        avaliacaoDAO = new AvaliacaoDAO(conn);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- BEM-VINDO AO SISTEMA DE AVALIAÇÃO DE FILMES ---");
            System.out.println("1. Fazer Login");
            System.out.println("2. Cadastrar Novo Usuário");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    fazerLogin(scanner);
                    break;
                case 2:
                    cadastrarNovoUsuario(scanner);
                    break;
                case 3:
                    System.out.println("Saindo do sistema... Até logo!");
                    fecharConexao(conn);
                    scanner.close();
                    return; // Encerra o programa
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
    }

    private static void fazerLogin(Scanner scanner) {
        System.out.println("\n--- TELA DE LOGIN ---");
        System.out.print("Digite seu email: ");
        String emailLogin = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senhaLogin = scanner.nextLine();

        Usuario usuarioLogado = usuarioDAO.login(emailLogin, senhaLogin);

        if (usuarioLogado != null) {
            System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + "!");
            // Chama o menu de usuário logado
            menuUsuarioLogado(usuarioLogado, scanner);
        } else {
            System.out.println("\nEmail ou senha inválidos. Tente novamente.");
        }
    }

    private static void cadastrarNovoUsuario(Scanner scanner) {
        System.out.println("\n--- TELA DE CADASTRO ---");
        System.out.print("Digite seu nome completo: ");
        String nomeCadastro = scanner.nextLine();
        System.out.print("Digite seu email: ");
        String emailCadastro = scanner.nextLine();
        System.out.print("Crie uma senha: ");
        String senhaCadastro = scanner.nextLine();

        // requisito: abstração - modelagem adequada das entidades
        Usuario novoUsuario = new Usuario(0, nomeCadastro, emailCadastro, senhaCadastro);
        try {
            // requisito: uso obrigatório do padrão dao para persistência de dados
            usuarioDAO.salvar(novoUsuario);
            System.out.println("Usuário '" + novoUsuario.getNome() + "' cadastrado com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    // Menu principal para usuários que já fizeram login
    private static void menuUsuarioLogado(Usuario usuario, Scanner scanner) {
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Procurar Filme");
            System.out.println("2. Cadastrar Novo Filme");
            System.out.println("3. Visualizar Meus Reviews");
            System.out.println("4. Fazer Logout (Voltar ao menu inicial)");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    // Lógica para procurar, visualizar, avaliar e editar/excluir review de um filme
                    System.out.println("// Lógica para PROCURAR FILME aqui...");
                    break;
                case 2:
                    // Lógica para cadastrar um novo filme
                    System.out.println("// Lógica para CADASTRAR NOVO FILME aqui...");
                    break;
                case 3:
                    // Lógica para visualizar os reviews feitos pelo usuário
                    System.out.println("// Lógica para VISUALIZAR MEUS REVIEWS aqui...");
                    break;
                case 4:
                    System.out.println("Fazendo logout...");
                    return; // Retorna para o menu de login/cadastro
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    // Função utilitária para ler a opção do usuário e validar a entrada
    private static int lerOpcao(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Descarta a entrada inválida
            System.out.print("Escolha uma opção: ");
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer
        return opcao;
    }

    // Função utilitária para fechar a conexão com o banco de dados
    private static void fecharConexao(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}