// ============================================================
//  carrinho.js
//  Lógica compartilhada do carrinho de compras (localStorage)
//  Incluir esse script em TODAS as páginas que usam o carrinho:
//  index.html, pagina-produto.html, carrinho.html, tela-de-pagamento.html
// ============================================================

const CARRINHO_KEY = 'cleo_carrinho';
const AUTH_KEY     = 'cleo_auth'; // { token, id, nome, login }

// ------------------------------------------------------------
// API base — funciona tanto local quanto em produção,
// porque o frontend é servido pelo mesmo servidor da API.
// ------------------------------------------------------------
const API_BASE = '/api';

// ------------------------------------------------------------
// Leitura e escrita do carrinho no localStorage
// Estrutura: [{ idProduto, nome, preco, imagemUrl, quantidade, estoque }]
// ------------------------------------------------------------
function getCarrinho() {
    const raw = localStorage.getItem(CARRINHO_KEY);
    return raw ? JSON.parse(raw) : [];
}

function salvarCarrinho(itens) {
    localStorage.setItem(CARRINHO_KEY, JSON.stringify(itens));
    atualizarContadorCarrinho();
}

// Adiciona um produto ao carrinho (ou aumenta a quantidade se já existir)
function adicionarAoCarrinho(produto, quantidade = 1) {
    const itens = getCarrinho();
    const existente = itens.find(i => i.idProduto === produto.id);

    if (existente) {
        existente.quantidade = Math.min(existente.quantidade + quantidade, produto.estoque);
    } else {
        itens.push({
            idProduto: produto.id,
            nome: produto.nome,
            preco: produto.preco,
            imagemUrl: produto.imagemUrl,
            quantidade: Math.min(quantidade, produto.estoque),
            estoque: produto.estoque
        });
    }

    salvarCarrinho(itens);
}

// Atualiza a quantidade de um item (remove se chegar a 0)
function atualizarQuantidade(idProduto, novaQtd) {
    let itens = getCarrinho();
    if (novaQtd <= 0) {
        itens = itens.filter(i => i.idProduto !== idProduto);
    } else {
        const item = itens.find(i => i.idProduto === idProduto);
        if (item) item.quantidade = Math.min(novaQtd, item.estoque);
    }
    salvarCarrinho(itens);
}

// Remove um item do carrinho
function removerDoCarrinho(idProduto) {
    const itens = getCarrinho().filter(i => i.idProduto !== idProduto);
    salvarCarrinho(itens);
}

// Esvazia o carrinho (usado após finalizar o pedido)
function limparCarrinho() {
    localStorage.removeItem(CARRINHO_KEY);
    atualizarContadorCarrinho();
}

// Calcula o total do carrinho
function calcularTotalCarrinho() {
    return getCarrinho().reduce((soma, item) => soma + item.preco * item.quantidade, 0);
}

// Atualiza o número exibido no ícone do carrinho (se existir na página)
function atualizarContadorCarrinho() {
    const el = document.getElementById('contador-carrinho');
    if (!el) return;
    const totalItens = getCarrinho().reduce((soma, i) => soma + i.quantidade, 0);
    el.textContent = totalItens > 0 ? totalItens : '';
    el.style.display = totalItens > 0 ? 'inline-block' : 'none';
}

// ------------------------------------------------------------
// Autenticação — token salvo após login (ver login.html)
// ------------------------------------------------------------
function getAuth() {
    const raw = localStorage.getItem(AUTH_KEY);
    return raw ? JSON.parse(raw) : null;
}

function salvarAuth(dados) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(dados));
}

function logout() {
    const auth = getAuth();
    if (auth) {
        fetch(`${API_BASE}/auth/logout`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${auth.token}` }
        }).catch(() => {});
    }
    localStorage.removeItem(AUTH_KEY);
    window.location.href = 'login.html';
}

// ------------------------------------------------------------
// Util: formatar número como moeda brasileira
// ------------------------------------------------------------
function formatarPreco(valor) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

// Atualiza o contador assim que o script carrega em qualquer página
document.addEventListener('DOMContentLoaded', atualizarContadorCarrinho);
