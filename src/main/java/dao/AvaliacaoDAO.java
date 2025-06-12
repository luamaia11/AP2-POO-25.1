package dao;

import modelo.Avaliacao;
import java.sql.*;
import java.util.ArrayList;

// requisito: uso obrigatório do padrão dao
public class AvaliacaoDAO implements BaseDAO {

    private Connection connection;

    public AvaliacaoDAO(Connection connection) {
        this.connection = connection;
    }

    // requisito: operacoes crud (create)
    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Avaliacao)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Avaliacao.");
        }
        Avaliacao a = (Avaliacao) objeto;
        String sql = "INSERT INTO avaliacoes (usuario_id, filme_id, nota, comentario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, a.getUsuarioId());
            pstm.setInt(2, a.getFilmeId());
            pstm.setInt(3, a.getNota());
            pstm.setString(4, a.getComentario());
            pstm.execute();
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar avaliação: " + e.getMessage(), e);
        }
    }

    // requisito: operacoes crud (read)
    @Override
    public Object buscarPorId(int id) {
        String sql = "SELECT * FROM avaliacoes WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Avaliacao a = new Avaliacao();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuario_id"));
                    a.setFilmeId(rs.getInt("filme_id"));
                    a.setNota(rs.getInt("nota"));
                    a.setComentario(rs.getString("comentario"));
                    return a;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliação: " + e.getMessage(), e);
        }
        return null;
    }

    public ArrayList<Avaliacao> buscarPorFilmeId(int filmeId) {
        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes WHERE filme_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, filmeId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Avaliacao a = new Avaliacao();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuario_id"));
                    a.setFilmeId(rs.getInt("filme_id"));
                    a.setNota(rs.getInt("nota"));
                    a.setComentario(rs.getString("comentario"));
                    avaliacoes.add(a);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliações por filme: " + e.getMessage(), e);
        }
        return avaliacoes;
    }

    public ArrayList<Avaliacao> buscarPorUsuarioId(int usuarioId) {
        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes WHERE usuario_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Avaliacao a = new Avaliacao();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuario_id"));
                    a.setFilmeId(rs.getInt("filme_id"));
                    a.setNota(rs.getInt("nota"));
                    a.setComentario(rs.getString("comentario"));
                    avaliacoes.add(a);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliações por usuário: " + e.getMessage(), e);
        }
        return avaliacoes;
    }

    public double getMediaDeNotasPorFilme(int filmeId) {
        String sql = "SELECT AVG(nota) as media FROM avaliacoes WHERE filme_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, filmeId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("media");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular a média de notas: " + e.getMessage(), e);
        }
        return 0.0;
    }

    // requisito: operacoes crud (read)
    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes";
        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Avaliacao a = new Avaliacao();
                a.setId(rs.getInt("id"));
                a.setUsuarioId(rs.getInt("usuario_id"));
                a.setFilmeId(rs.getInt("filme_id"));
                a.setNota(rs.getInt("nota"));
                a.setComentario(rs.getString("comentario"));
                lista.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar avaliações: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT a.id, u.nome AS usuario_nome, f.titulo AS filme_titulo, a.nota, a.comentario " +
                "FROM avaliacoes a " +
                "LEFT JOIN usuarios u ON a.usuario_id = u.id " +
                "LEFT JOIN filmes f ON a.filme_id = f.id";
        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                String linha = String.format("Avaliação ID: %d | Usuário: %s | Filme: %s | Nota: %d | Comentário: %s",
                        rs.getInt("id"),
                        rs.getString("usuario_nome"),
                        rs.getString("filme_titulo"),
                        rs.getInt("nota"),
                        rs.getString("comentario"));
                lista.add(linha);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar avaliações detalhadas: " + e.getMessage(), e);
        }
        return lista;
    }

    // requisito: operacoes crud (update)
    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Avaliacao)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Avaliacao.");
        }
        Avaliacao a = (Avaliacao) objeto;
        String sql = "UPDATE avaliacoes SET nota = ?, comentario = ? WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, a.getNota());
            pstm.setString(2, a.getComentario());
            pstm.setInt(3, a.getId());
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar avaliação: " + e.getMessage(), e);
        }
    }

    // requisito: operacoes crud (delete)
    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM avaliacoes WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            int linhasAfetadas = pstm.executeUpdate(); // Correção: usar executeUpdate()
            if (linhasAfetadas == 0) {
                System.out.println("Aviso: Nenhuma avaliação encontrada com o ID " + id + " para excluir.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir avaliação: " + e.getMessage(), e);
        }
    }
}