package modelo;

// requisito: pelo menos uma classe abstrata com métodos abstratos e concretos
public abstract class Pessoa {
    // Atributos comuns a todas as pessoas do sistema
    private int id;
    private String nome;

    // Construtor da superclasse
    public Pessoa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Métodos concretos que serão herdados pelas subclasses
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
