package controller;

import model.Permissao;
import service.PermissaoService;
import java.util.List;

public class PermissaoController {

    private PermissaoService service = new PermissaoService();

    public void salvar(int idPerfil, int idModulo,
                       boolean visualizar, boolean editar, boolean excluir) {
        service.salvar(idPerfil, idModulo, visualizar, editar, excluir);
    }

    public List<Permissao> listarPorPerfil(int idPerfil) {
        return service.listarPorPerfil(idPerfil);
    }
}
