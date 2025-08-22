// ========================================
// SCRIPT MODAL LOGIN
// ========================================
document.addEventListener('DOMContentLoaded', function() {
  // Solo abrir el modal si viene de una redirección específica después del registro
  var showLoginModal = window.showLoginModal || false;

  // Solo abrir el modal si viene de una redirección específica después del registro
  if (showLoginModal && window.location.search.includes('showLoginModal=true')) {
    if (typeof bootstrap !== 'undefined' && bootstrap.Modal) {
      var loginModalElement = document.getElementById('loginModal');
      if (loginModalElement) {
        var loginModal = new bootstrap.Modal(loginModalElement);
        loginModal.show();
      } else {
        console.error("Elemento modal con ID 'loginModal' no encontrado.");
      }
    } else {
      console.error("Bootstrap JavaScript no está cargado o 'bootstrap.Modal' no está definido.");
    }
  }
});

// ========================================
// FUNCIONES DE PERMISOS
// ========================================
// Función para mostrar alerta de acceso denegado
function mostrarAccesoDenegado(mensaje = 'No tienes permisos para acceder a esta sección.') {
  const alertDiv = document.createElement('div');
  alertDiv.className = 'alert alert-warning alert-dismissible fade show position-fixed';
  alertDiv.style.cssText = 'top: 80px; right: 20px; z-index: 9999; min-width: 300px;';
  alertDiv.innerHTML = `
    <strong>Acceso Denegado</strong><br>
    ${mensaje}
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
  `;
  document.body.appendChild(alertDiv);
  setTimeout(() => {
    if (alertDiv.parentNode) {
      alertDiv.remove();
    }
  }, 5000);
}

// Exportar función globalmente
window.mostrarAccesoDenegado = mostrarAccesoDenegado;

// ========================================
// SCRIPT DROPDOWN USUARIO
// ========================================
document.addEventListener('DOMContentLoaded', function() {
  var userBtn = document.getElementById('enlaceUsuarioBtn');
  if (userBtn) {
    userBtn.addEventListener('click', function(e) {
      e.stopPropagation();
      var old = document.getElementById('dropdownPerfil');
      if (old) {
        old.remove();
        return;
      }
      // Obtén datos del usuario
      var nombres = userBtn.getAttribute('data-nombres') || '';
      var apellidos = userBtn.getAttribute('data-apellidos') || '';
      var correo = userBtn.getAttribute('data-correo') || '';
      var circulo = userBtn.getAttribute('data-circulo') || '';
      // Crea el dropdown
      var dropdown = document.createElement('div');
      dropdown.id = 'dropdownPerfil';
      dropdown.className = 'dropdown-perfil show';
      dropdown.innerHTML = `
        <div class="dropdown-header">
          <span class="circle-iniciales"><span>${circulo}</span></span>
          <div class="nombre-perfil">${nombres} ${apellidos}</div>
          <div class="correo-perfil">${correo}</div>
        </div>
        <a class="perfil-link" href="/mi-cuenta" id="miCuentaLink"><i class="bi bi-person"></i> Mi cuenta</a>
        <a class="perfil-link logout" href="/auth/logout" id="cerrarSesionLink"><i class="bi bi-box-arrow-right"></i> Cerrar sesión</a>
      `;
      // Posiciona el dropdown
      var rect = userBtn.getBoundingClientRect();
      dropdown.style.position = 'fixed';
      dropdown.style.left = (rect.left + window.scrollX) + 'px';
      dropdown.style.top = (rect.bottom + 8 + window.scrollY) + 'px';
      dropdown.style.zIndex = 99999;
      document.body.appendChild(dropdown);
      // Funcionalidad de logout manejada por header.html
      // Funcionalidad para ir a Mi cuenta y cerrar el dropdown
      var miCuenta = document.getElementById('miCuentaLink');
      if (miCuenta) {
        miCuenta.addEventListener('click', function(ev) {
          // Cierra el dropdown antes de redirigir
          var dd = document.getElementById('dropdownPerfil');
          if (dd) dd.remove();
          // Redirige a la página de mi cuenta
          window.location.href = '/mi-cuenta';
        });
      }
    });
    document.addEventListener('click', function(e) {
      var dropdown = document.getElementById('dropdownPerfil');
      if (dropdown && (!userBtn.contains(e.target) && !dropdown.contains(e.target))) {
        dropdown.remove();
      }
    });
  }
}); 