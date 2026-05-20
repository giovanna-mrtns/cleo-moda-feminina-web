package service;

import dao.PerfilDAO;
import model.Perfil;
import java.util.List;

public class PerfilService {

    private PerfilDAO dao = new PerfilDAO();

    // Cadastra um novo perfil com validação
    public void cadastrar(String nome) {
        if (nome == null || nome.isBlank()) {
            System.out.println("Erro: o nome do perfil é obrigatório.");
            return;
        }
        Perfil p = new Perfil();
        p.setNome(nome.trim());
        dao.cadastrar(p);
    }

    // Altera o nome de um perfil existente
    public void alterar(int id, String novoNome) {
        Perfil p = new Perfil();
        p.setId(id);
        p.setNome(novoNome.trim());
        dao.alterar(p);
    }

    // Desativa um perfil pelo ID
    public void desativar(int id) {
        dao.desativar(id);
    }

    // Retorna todos os perfis para listar na tela
    public List<Perfil> listarTodos() {
        return dao.listarTodos();
    }

    // Busca um perfil pelo ID
    public Perfil buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
}
