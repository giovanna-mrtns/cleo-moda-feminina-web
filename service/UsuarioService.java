package service;

import dao.UsuarioDAO;
import model.Usuario;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO dao = new UsuarioDAO();

    // Realiza o login: valida os campos, autentica e atualiza o último acesso
    public Usuario realizarLogin(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            return null;
        }
        Usuario usuario = dao.autenticar(login.trim(), senha.trim());
        if (usuario != null) {
            dao.atualizarUltimoAcesso(usuario.getId());
        }
        return usuario;
    }

    // Retorna todos os usuários para listar na tela
    public List<Usuario> listarTodos() {
        return dao.listarTodos();
    }

    // Cadastra um novo usuário com validação dos campos obrigatórios
    public void cadastrar(String nome, String login, String senha, int idPerfil) {
        if (nome == null || nome.isBlank() ||
            login == null || login.isBlank() ||
            senha == null || senha.isBlank()) {
            System.out.println("Erro: nome, login e senha são obrigatórios.");
            return;
        }
        Usuario u = new Usuario();
        u.setNome(nome.trim());
        u.setLogin(login.trim());
        u.setSenha(senha.trim());
        u.setIdPerfil(idPerfil);
        dao.cadastrar(u);
    }

    // Altera os dados de um usuário existente
    public void alterar(int id, String nome, String login, String senha, int idPerfil) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNome(nome);
        u.setLogin(login);
        u.setSenha(senha);
        u.setIdPerfil(idPerfil);
        dao.alterar(u);
    }

    // Desativa um usuário pelo ID
    public void desativar(int id) {
        dao.desativar(id);
    }

    // Altera a senha após confirmar a senha antiga
    public boolean alterarSenha(int id, String antiga, String nova) {
        return dao.alterarSenha(id, antiga, nova);
    }
}
