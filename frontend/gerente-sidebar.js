(function(){
function getAuth(){
 try{
   return JSON.parse(localStorage.getItem('cleo_auth')||localStorage.getItem('auth')||'{}');
 }catch(e){return {};}
}
const auth=getAuth();
const perfil=(auth.nomePerfil||'').toLowerCase();
if(!perfil.includes('gerente')) return;

const style=document.createElement('style');
style.textContent=`
body{margin-left:220px!important;}
#gerente-sidebar{position:fixed;left:0;top:0;width:220px;height:100vh;background:#222;color:#fff;z-index:99999;padding-top:20px}
#gerente-sidebar a{display:block;color:#fff;text-decoration:none;padding:12px 16px}
#gerente-sidebar a:hover{background:#444}
#gerente-sidebar h3{padding:0 16px}
`;
document.head.appendChild(style);

const nav=document.createElement('div');
nav.id='gerente-sidebar';
nav.innerHTML=`<h3>Painel Gerente</h3>
<a href="configuracao-de-usuario.html">Usuários</a>
<a href="configuracao-de-perfil.html">Perfis</a>
<a href="configuracao-de-modulos.html">Módulos</a>
<a href="estoqueadm.html">Estoque</a>`;
document.body.appendChild(nav);
})();