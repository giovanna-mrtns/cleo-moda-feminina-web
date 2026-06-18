package controller;

import model.Endereco;
import service.EnderecoService;
import java.util.List;

public class EnderecoController {

    private EnderecoService service = new EnderecoService();

    public List<Endereco> listarPorUsuario(int idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    public Endereco cadastrar(Endereco e) {
        return service.cadastrar(e);
    }

    public void alterar(Endereco e) {
        service.alterar(e);
    }

    public void excluir(int id) {
        service.excluir(id);
    }

    public void definirPrincipal(int idUsuario, int idEndereco) {
        service.definirPrincipal(idUsuario, idEndereco);
    }
}