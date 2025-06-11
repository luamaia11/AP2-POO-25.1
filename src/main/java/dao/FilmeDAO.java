package dao;

import modelo.Filme;

import java.sql.*;
import java.util.ArrayList;

public class FilmeDAO implements BaseDAO {

    private Connection connection;

    public FilmeDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Filme)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Filme.");
        }

        Filme f = (Filme) objeto;
        String sql = "INSERT INTO filmes (titulo, ano, genero) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, f.getTitulo());
            pstm.setInt(2, f.getAno());
            pstm.setString(3, f.getGenero());
            pstm.execute();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                f.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar filme: " + e.getMessage(), e);
        }
    }

    @Override
    public Object buscarPorId(int id) {
        String sql = "SELECT * FROM filmes WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return new Filme(rs.getInt("id"), rs.getString("titulo"), rs.getInt("ano"), rs.getString("genero"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filme: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Filme f = new Filme(rs.getInt("id"), rs.getString("titulo"), rs.getInt("ano"), rs.getString("genero"));
                lista.add(f);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar filmes: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return listarTodosLazyLoading(); // Não há relacionamentos complexos
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Filme)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Filme.");
        }

        Filme f = (Filme) objeto;
        String sql = "UPDATE filmes SET titulo = ?, ano = ?, genero = ? WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, f.getTitulo());
            pstm.setInt(2, f.getAno());
            pstm.setString(3, f.getGenero());
            pstm.setInt(4, f.getId());
            pstm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar filme: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(int id) {
        try (PreparedStatement pstm = connection.prepareStatement("DELETE FROM filmes WHERE id = ?")) {
            pstm.setInt(1, id);
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir filme: " + e.getMessage(), e);
        }
    }
}