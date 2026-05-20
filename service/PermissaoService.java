package service;

import dao.PermissaoDAO;
import model.Permissao;
import java.util.List;

public class PermissaoService {

    private PermissaoDAO dao = new PermissaoDAO();

    // Salva (insere ou atualiza) uma permissão para um par perfil + módulo
    public void salvar(int idPerfil, int idModulo,
                       boolean visualizar, boolean editar, boolean excluir) {
        // Regra: se não pode visualizar, não pode editar nem excluir
        if (!visualizar) {
            editar   = false;
            excluir  = false;
        }
        Permissao p = new Permissao();
        p.setIdPerfil(idPerfil);
        p.setIdModulo(idModulo);
        p.setPodeVisualizar(visualizar);
        p.setPodeEditar(editar);
        p.setPodeExcluir(excluir);
        dao.salvar(p);
    }

    // Retorna todas as permissões de um perfil (com nome do módulo)
    public List<Permissao> listarPorPerfil(int idPerfil) {
        return dao.listarPorPerfil(idPerfil);
    }
}
