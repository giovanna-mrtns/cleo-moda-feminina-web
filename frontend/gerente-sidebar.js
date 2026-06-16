
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
body{margin-top:60px!important;}
#gerente-sidebar{position:fixed;left:0;top:0;width:100%;height:60px;background:#222;color:#fff;z-index:99999;padding:0 16px;box-sizing:border-box;font-family:Arial,sans-serif;display:flex;align-items:center}
#gerente-sidebar a{display:inline-block;color:#fff;text-decoration:none;padding:8px 12px;margin-right:8px}
#gerente-sidebar a:hover{background:#444}
#gerente-sidebar h3{margin:0 16px 0 0;font-size:16px}
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
