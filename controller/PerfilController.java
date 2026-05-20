package controller;

import model.Perfil;
import service.PerfilService;
import java.util.List;

public class PerfilController {

    private PerfilService service = new PerfilService();

    public void cadastrar(String nome) {
        service.cadastrar(nome);
    }

    public void alterar(int id, String novoNome) {
        service.alterar(id, novoNome);
    }

    public void desativar(int id) {
        service.desativar(id);
    }

    public List<Perfil> listar() {
        return service.listarTodos();
    }

    public Perfil buscarPorId(int id) {
        return service.buscarPorId(id);
    }
}
