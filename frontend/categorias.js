// ============================================================
//  categorias.js
//  Regras compartilhadas pelas páginas de categoria do menu:
//  lancamentos.html, roupas.html, acessorios.html e promocoes.html
//
//  O campo "categoria" de cada produto é texto livre (preenchido
//  no estoque, ex: "Vestidos", "Bolsas"). Para decidir se um
//  produto é "Roupa" ou "Acessório", comparamos a categoria do
//  produto com listas de palavras-chave abaixo.
//
//  >>> PARA CADASTRAR NOVAS PEÇAS NO FUTURO <<<
//  Basta que a categoria do produto contenha uma das palavras já
//  existentes nas listas (ex: categoria "Saia Midi" já cai em
//  Roupas, porque contém "saia"). Se for um tipo de peça
//  totalmente novo, adicione a palavra na lista correspondente.
// ============================================================

const PALAVRAS_ROUPAS = [
    'vestido', 'blusa', 'calca', 'saia', 'casaco', 'jaqueta',
    'short', 'macacao', 'camisa', 'camiseta', 'moletom',
    'cardigan', 'kimono', 'conjunto', 'jardineira', 'sueter',
    'colete', 'jeans', 'legging', 'regata', 'cropped', 'blazer',
    'roupa', 'top'
];

const PALAVRAS_ACESSORIOS = [
    'bolsa', 'cinto', 'oculos', 'colar', 'brinco', 'pulseira',
    'anel', 'lenco', 'chapeu', 'bone', 'carteira', 'mochila',
    'sapato', 'sandalia', 'tenis', 'bota', 'echarpe',
    'gargantilha', 'tiara', 'acessorio'
];

// Remove acentos e deixa em minúsculo, para a comparação não
// depender de como a categoria foi digitada no estoque.
function normalizarTexto(texto) {
    return (texto || '')
        .toString()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase()
        .trim();
}

function produtoContemPalavra(produto, listaPalavrasChave) {
    const categoria = normalizarTexto(produto.categoria);
    if (!categoria) return false;
    return listaPalavrasChave.some(palavra => categoria.includes(palavra));
}

function ehRoupa(produto) {
    return produtoContemPalavra(produto, PALAVRAS_ROUPAS);
}

function ehAcessorio(produto) {
    return produtoContemPalavra(produto, PALAVRAS_ACESSORIOS);
}

// "Lançamentos" = produtos mais recentes (por ID, igual à ordenação
// "Mais Recentes" que já existe na home).
function ordenarPorMaisRecente(produtos) {
    return [...produtos].sort((a, b) => b.id - a.id);
}

// "Promoções" — ainda não existe um campo de desconto no banco.
// Quando o backend adicionar esse campo (ex: precoPromocional,
// emPromocao, percentualDesconto), ajuste apenas a condição abaixo
// e a página de Promoções passa a funcionar automaticamente.
function estaEmPromocao(produto) {
    return produto.emPromocao === true ||
        (produto.precoPromocional != null &&
         produto.precoPromocional > 0 &&
         produto.precoPromocional < produto.preco);
}
