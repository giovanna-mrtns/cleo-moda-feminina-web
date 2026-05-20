package service;

import dao.ModuloDAO;
import model.Modulo;
import java.util.List;

public class ModuloService {

    private ModuloDAO dao = new ModuloDAO();

    // Cadastra um novo módulo com validação
    public void cadastrar(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            System.out.println("Erro: o nome do módulo é obrigatório.");
            return;
        }
        Modulo m = new Modulo();
        m.setNome(nome.trim());
        m.setDescricao(descricao);
        dao.cadastrar(m);
    }

    // Altera nome e descrição de um módulo existente
    public void alterar(int id, String novoNome, String novaDescricao) {
        Modulo m = new Modulo();
        m.setId(id);
        m.setNome(novoNome.trim());
        m.setDescricao(novaDescricao);
        dao.alterar(m);
    }

    // Desativa um módulo pelo ID
    public void desativar(int id) {
        dao.desativar(id);
    }

    // Retorna todos os módulos para listar na tela
    public List<Modulo> listarTodos() {
        return dao.listarTodos();
    }

    // Busca um módulo pelo ID
    public Modulo buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
}
