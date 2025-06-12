package modelo;

public class Filme {
    private int id;
    private String titulo;
    private int ano;
    private String genero;

    // Construtor original
    public Filme(int id, String titulo, int ano, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.genero = genero;
    }

    // requisito: polimorfismo (sobrecarga)
    // Este é um construtor sobrecarregado. Ele tem o mesmo nome do original,
    // mas uma lista de parâmetros diferente (não recebe o 'id').
    public Filme(String titulo, int ano, String genero) {
        // A palavra-chave 'this(...)' chama o outro construtor desta mesma classe,
        // passando 0 como um ID temporário.
        this(0, titulo, ano, genero);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}