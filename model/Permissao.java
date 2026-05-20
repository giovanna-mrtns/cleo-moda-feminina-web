package model;

public class Permissao {

    private int id;
    private int idPerfil;
    private int idModulo;
    private boolean podeVisualizar;
    private boolean podeEditar;
    private boolean podeExcluir;

    // Campos extras para exibição na tela (preenchidos via JOIN)
    private String nomeModulo;
    private String nomePerfil;

    public Permissao() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public int getIdModulo() { return idModulo; }
    public void setIdModulo(int idModulo) { this.idModulo = idModulo; }

    public boolean isPodeVisualizar() { return podeVisualizar; }
    public void setPodeVisualizar(boolean podeVisualizar) { this.podeVisualizar = podeVisualizar; }

    public boolean isPodeEditar() { return podeEditar; }
    public void setPodeEditar(boolean podeEditar) { this.podeEditar = podeEditar; }

    public boolean isPodeExcluir() { return podeExcluir; }
    public void setPodeExcluir(boolean podeExcluir) { this.podeExcluir = podeExcluir; }

    public String getNomeModulo() { return nomeModulo; }
    public void setNomeModulo(String nomeModulo) { this.nomeModulo = nomeModulo; }

    public String getNomePerfil() { return nomePerfil; }
    public void setNomePerfil(String nomePerfil) { this.nomePerfil = nomePerfil; }
}
