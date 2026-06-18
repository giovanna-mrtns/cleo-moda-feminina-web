package service;

import dao.EnderecoDAO;
import model.Endereco;
import java.util.List;

public class EnderecoService {

    private EnderecoDAO dao = new EnderecoDAO();

    public List<Endereco> listarPorUsuario(int idUsuario) {
        return dao.listarPorUsuario(idUsuario);
    }

    public Endereco cadastrar(Endereco e) {
        validar(e);

        boolean primeiroEndereco = dao.listarPorUsuario(e.getIdUsuario()).isEmpty();
        boolean deveSerPrincipal = e.isPrincipal() || primeiroEndereco;

        e.setPrincipal(false); // insere neutro, e ajusta abaixo se preciso
        int novoId = dao.cadastrar(e);
        e.setId(novoId);

        if (deveSerPrincipal) {
            dao.definirPrincipal(e.getIdUsuario(), novoId);
            e.setPrincipal(true);
        }
        return e;
    }

    public void alterar(Endereco e) {
        validar(e);
        dao.alterar(e);
        if (e.isPrincipal()) {
            dao.definirPrincipal(e.getIdUsuario(), e.getId());
        }
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public void definirPrincipal(int idUsuario, int idEndereco) {
        dao.definirPrincipal(idUsuario, idEndereco);
    }

    private void validar(Endereco e) {
        if (vazio(e.getNomeDestinatario()) || vazio(e.getRua()) || vazio(e.getNumero()) ||
            vazio(e.getBairro()) || vazio(e.getCidade()) || vazio(e.getEstado()) || vazio(e.getCep())) {
            throw new IllegalArgumentException("Campos obrigatórios do endereço não preenchidos.");
        }
    }

    private boolean vazio(String s) { return s == null || s.isBlank(); }
}