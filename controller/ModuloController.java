package controller;

import model.Modulo;
import service.ModuloService;
import java.util.List;

public class ModuloController {

    private ModuloService service = new ModuloService();

    public void cadastrar(String nome, String descricao) {
        service.cadastrar(nome, descricao);
    }

    public void alterar(int id, String novoNome, String novaDescricao) {
        service.alterar(id, novoNome, novaDescricao);
    }

    public void desativar(int id) {
        service.desativar(id);
    }

    public List<Modulo> listar() {
        return service.listarTodos();
    }

    public Modulo buscarPorId(int id) {
        return service.buscarPorId(id);
    }
}
