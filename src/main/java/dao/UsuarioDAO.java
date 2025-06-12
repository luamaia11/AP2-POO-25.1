package dao;

import java.sql.*;
import modelo.Usuario;
import java.util.ArrayList;

// Requisito: Uso obrigatório do padrão DAO
public class UsuarioDAO implements BaseDAO {

    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Requisito: Operações CRUD (Create)
    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario u = (Usuario) objeto;
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, u.getNome());
            pstm.setString(2, u.getEmail());
            pstm.setString(3, u.getSenha());
            pstm.execute();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    // Requisito: Operações CRUD (Read)
    @Override
    public Object buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }

        return null;
    }

    // Método para autenticar um usuário, conforme caso de uso "fazer login"
    public Usuario login(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setString(1, email);
            pstm.setString(2, senha);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar fazer login: " + e.getMessage(), e);
        }
        return null;
    }

    // Requisito: Operações CRUD (Read)
    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return listarTodosLazyLoading();
    }

    // Requisito: Operações CRUD (Update)
    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario u = (Usuario) objeto;
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, u.getNome());
            pstm.setString(2, u.getEmail());
            pstm.setString(3, u.getSenha());
            pstm.setInt(4, u.getId());
            pstm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    // Requisito: Operações CRUD (Delete)
    @Override
    public void excluir(int id) {
        try (PreparedStatement pstm = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            pstm.setInt(1, id);
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }
}