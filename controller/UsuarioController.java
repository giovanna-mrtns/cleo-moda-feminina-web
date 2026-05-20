package controller;

import model.Usuario;
import service.UsuarioService;
import java.util.List;

public class UsuarioController {

    private UsuarioService service = new UsuarioService();

    public Usuario login(String login, String senha) {
        return service.realizarLogin(login, senha);
    }

    public List<Usuario> listar() {
        return service.listarTodos();
    }

    public void cadastrar(String nome, String login, String senha, int idPerfil) {
        service.cadastrar(nome, login, senha, idPerfil);
    }

    public void alterar(int id, String nome, String login, String senha, int idPerfil) {
        service.alterar(id, nome, login, senha, idPerfil);
    }

    public void desativar(int id) {
        service.desativar(id);
    }

    public boolean alterarSenha(int id, String antiga, String nova) {
        return service.alterarSenha(id, antiga, nova);
    }
}
