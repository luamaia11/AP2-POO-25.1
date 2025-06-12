package modelo;

// requisito: abstração - modelagem adequada das entidades
public class Avaliacao {
    private int id;
    private int usuarioId;
    private int filmeId;
    private int nota;
    private String comentario;

    public Avaliacao() {}

    public Avaliacao(int id, int usuarioId, int filmeId, int nota, String comentario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.filmeId = filmeId;
        this.nota = nota;
        this.comentario = comentario;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public int getFilmeId() { return filmeId; }
    public void setFilmeId(int filmeId) { this.filmeId = filmeId; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}