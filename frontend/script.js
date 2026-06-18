// ============================================================
// Menu hambúrguer (mobile)
// Alterna a exibição do menu de categorias em telas pequenas.
// ============================================================
document.addEventListener('DOMContentLoaded', function () {
    const botao = document.getElementById('menu-toggle');
    const menu = document.querySelector('nav.menu');

    if (!botao || !menu) return;

    function alternarMenu() {
        const aberto = menu.classList.toggle('aberto');
        botao.classList.toggle('ativo', aberto);
        botao.setAttribute('aria-expanded', aberto ? 'true' : 'false');
    }

    botao.addEventListener('click', alternarMenu);

    // Fecha o menu ao clicar em um link (facilita a navegação no celular)
    menu.querySelectorAll('a').forEach(function (link) {
        link.addEventListener('click', function () {
            menu.classList.remove('aberto');
            botao.classList.remove('ativo');
            botao.setAttribute('aria-expanded', 'false');
        });
    });

    // Garante que o menu não fique "aberto" caso a tela seja redimensionada para desktop
    window.addEventListener('resize', function () {
        if (window.innerWidth > 768) {
            menu.classList.remove('aberto');
            botao.classList.remove('ativo');
            botao.setAttribute('aria-expanded', 'false');
        }
    });
});
