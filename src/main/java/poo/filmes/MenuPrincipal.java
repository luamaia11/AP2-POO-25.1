package poo.filmes;

import dao.AvaliacaoDAO;
import dao.Conexao;
import dao.FilmeDAO;
import dao.UsuarioDAO;
import modelo.Avaliacao;
import modelo.Filme;
import modelo.Usuario;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {

    private static UsuarioDAO usuarioDAO;
    private static FilmeDAO filmeDAO;
    private static AvaliacaoDAO avaliacaoDAO;

    public static void main(String[] args) {
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
                    return;
                default:
                    System.out.println("Opção inválida.");
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
        Usuario novoUsuario = new Usuario(0, nomeCadastro, emailCadastro, senhaCadastro);
        try {
            usuarioDAO.salvar(novoUsuario);
            System.out.println("Usuário '" + novoUsuario.getNome() + "' cadastrado com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void menuUsuarioLogado(Usuario usuario, Scanner scanner) {
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Procurar Filme");
            System.out.println("2. Cadastrar Novo Filme");
            System.out.println("3. Visualizar Meus Reviews");
            System.out.println("4. Fazer Logout");
            System.out.print("Escolha uma opção: ");
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    procurarFilme(usuario, scanner);
                    break;
                case 2:
                    cadastrarNovoFilme(scanner);
                    break;
                case 3:
                    visualizarMeusReviews(usuario, scanner);
                    break;
                case 4:
                    System.out.println("Fazendo logout...");
                    return;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private static void procurarFilme(Usuario usuario, Scanner scanner) {
        System.out.println("\n--- PROCURAR FILME ---");
        System.out.print("Digite o título do filme: ");
        String titulo = scanner.nextLine();
        Filme filme = filmeDAO.buscarPorTitulo(titulo);
        if (filme != null) {
            double media = avaliacaoDAO.getMediaDeNotasPorFilme(filme.getId());
            System.out.println("\n--- FILME ENCONTRADO ---");
            System.out.printf("Título: %s (Ano: %d)\n", filme.getTitulo(), filme.getAno());
            System.out.printf("Média de Notas da Comunidade: %.1f / 10\n", media);
            exibirReviewsDeUmFilme(filme.getId());
            System.out.print("\nDeseja avaliar este filme? (S/N): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("S")) {
                avaliarFilme(filme, usuario, scanner);
            }
        } else {
            System.out.println("Filme não encontrado.");
            System.out.print("Deseja cadastrá-lo agora? (S/N): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("S")) {
                cadastrarNovoFilme(scanner);
            }
        }
    }

    private static void exibirReviewsDeUmFilme(int filmeId) {
        System.out.println("\n--- Reviews da Comunidade ---");
        ArrayList<Avaliacao> avaliacoes = avaliacaoDAO.buscarPorFilmeId(filmeId);
        if (avaliacoes.isEmpty()) {
            System.out.println("Este filme ainda não possui reviews.");
            return;
        }
        for (Avaliacao avaliacao : avaliacoes) {
            Usuario autor = (Usuario) usuarioDAO.buscarPorId(avaliacao.getUsuarioId());
            String nomeAutor = (autor != null) ? autor.getNome() : "Usuário desconhecido";
            System.out.println("--------------------");
            System.out.printf("Usuário: %s | Nota: %d\n", nomeAutor, avaliacao.getNota());
            if (avaliacao.getComentario() != null && !avaliacao.getComentario().trim().isEmpty()) {
                System.out.println("Comentário: " + avaliacao.getComentario());
            }
        }
    }

    private static void visualizarMeusReviews(Usuario usuario, Scanner scanner) {
        System.out.println("\n--- SEUS REVIEWS ---");
        ArrayList<Avaliacao> minhasAvaliacoes = avaliacaoDAO.buscarPorUsuarioId(usuario.getId());

        if (minhasAvaliacoes.isEmpty()) {
            System.out.println("Você ainda não fez nenhum review.");
        } else {
            for (Avaliacao avaliacao : minhasAvaliacoes) {
                Filme filme = (Filme) filmeDAO.buscarPorId(avaliacao.getFilmeId());
                String tituloFilme = (filme != null) ? filme.getTitulo() : "Filme desconhecido";
                System.out.println("--------------------");
                System.out.printf("Review ID: %d | Filme: %s\n", avaliacao.getId(), tituloFilme);
                System.out.printf("Sua Nota: %d\n", avaliacao.getNota());
                if (avaliacao.getComentario() != null && !avaliacao.getComentario().trim().isEmpty()) {
                    System.out.println("Seu Comentário: " + avaliacao.getComentario());
                }
            }
        }

        while (true) {
            System.out.println("\n--- Opções de Review ---");
            System.out.println("1. Excluir um review");
            System.out.println("2. Fazer um novo review");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    excluirReview(usuario, scanner);
                    return;
                case 2:
                    fazerUmNovoReview(usuario, scanner);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private static void excluirReview(Usuario usuario, Scanner scanner) {
        System.out.print("\nDigite o ID do review que deseja excluir: ");
        int idReview = lerOpcao(scanner);
        Avaliacao review = (Avaliacao) avaliacaoDAO.buscarPorId(idReview);

        if (review != null && review.getUsuarioId() == usuario.getId()) {
            avaliacaoDAO.excluir(idReview);
            System.out.println("Review ID " + idReview + " excluído com sucesso!");
        } else {
            System.out.println("Erro: Review não encontrado ou não pertence a você.");
        }
    }

    private static void fazerUmNovoReview(Usuario usuario, Scanner scanner) {
        System.out.println("\n--- FAZER NOVO REVIEW ---");
        System.out.print("Digite o título do filme que deseja avaliar: ");
        String titulo = scanner.nextLine();
        Filme filme = filmeDAO.buscarPorTitulo(titulo);
        if (filme != null) {
            avaliarFilme(filme, usuario, scanner);
        } else {
            System.out.println("Filme não encontrado.");
        }
    }

    private static void avaliarFilme(Filme filme, Usuario usuario, Scanner scanner) {
        System.out.println("\n--- AVALIAR: " + filme.getTitulo() + " ---");
        System.out.print("Sua nota (de 0 a 10): ");
        int nota = lerOpcao(scanner);
        System.out.print("Seu comentário (opcional, pressione Enter para pular): ");
        String comentario = scanner.nextLine();
        Avaliacao novaAvaliacao = new Avaliacao(0, usuario.getId(), filme.getId(), nota, comentario);
        try {
            avaliacaoDAO.salvar(novaAvaliacao);
            System.out.println("Avaliação registrada com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("Erro ao salvar avaliação: " + e.getMessage());
        }
    }

    private static void cadastrarNovoFilme(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE NOVO FILME ---");
        System.out.print("Título do filme: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano de lançamento: ");
        int ano = lerOpcao(scanner);
        System.out.print("Gênero: ");
        String genero = scanner.nextLine();

        // Usando o novo construtor sobrecarregado (mais limpo, sem o ID '0')
        Filme novoFilme = new Filme(titulo, ano, genero);

        try {
            filmeDAO.salvar(novoFilme);
            System.out.println("Filme '" + novoFilme.getTitulo() + "' cadastrado com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("Erro ao cadastrar filme: " + e.getMessage());
        }
    }

    private static int lerOpcao(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next();
            System.out.print("Escolha uma opção: ");
        }
        int opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

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