package modelo;

// requisito: herança - hierarquia de classes com reutilização de código
public class Usuario extends Pessoa {
    // Atributos específicos de Usuario
    private String email;
    private String senha;

    // Construtor da subclasse
    public Usuario(int id, String nome, String email, String senha) {
        // requisito: polimorfismo - a chamada 'super' invoca o construtor da superclasse (pessoa)
        super(id, nome); // Envia 'id' e 'nome' para o construtor da classe Pessoa
        this.email = email;
        this.senha = senha;
    }

    // Métodos específicos de Usuario
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}