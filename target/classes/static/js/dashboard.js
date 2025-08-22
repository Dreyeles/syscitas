// Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    
    // Toggle del sidebar en móvil
    const sidebarToggle = document.querySelector('[data-bs-toggle="collapse"]');
    const sidebar = document.getElementById('sidebar');
    
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('show');
        });
    }
    
    // Cerrar sidebar al hacer clic fuera en móvil
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 991) {
            if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                sidebar.classList.remove('show');
            }
        }
    });
    
    // Manejo de submenús
    const submenuItems = document.querySelectorAll('.nav-item.has-submenu');
    
    submenuItems.forEach(function(item) {
        const link = item.querySelector('.nav-link');
        const submenu = item.querySelector('.submenu');
        
        if (link && submenu) {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                
                // Cerrar otros submenús abiertos
                submenuItems.forEach(function(otherItem) {
                    if (otherItem !== item) {
                        otherItem.classList.remove('open');
                        const otherSubmenu = otherItem.querySelector('.submenu');
                        if (otherSubmenu) {
                            otherSubmenu.classList.remove('show');
                        }
                    }
                });
                
                // Toggle del submenú actual
                item.classList.toggle('open');
                submenu.classList.toggle('show');
            });
        }
    });
    
    // Activar enlace activo basado en la URL actual
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    
    navLinks.forEach(function(link) {
        const href = link.getAttribute('href');
        if (href && currentPath.includes(href) && href !== '#') {
            link.classList.add('active');
            
            // Si está en un submenú, abrir el submenú padre
            const parentSubmenu = link.closest('.submenu');
            if (parentSubmenu) {
                parentSubmenu.classList.add('show');
                const parentItem = parentSubmenu.closest('.nav-item');
                if (parentItem) {
                    parentItem.classList.add('open');
                }
            }
        }
    });
    
    // Tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Popovers de Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Animaciones de entrada para las cards
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observar todas las cards del dashboard
    const dashboardCards = document.querySelectorAll('.dashboard-card, .stats-card');
    dashboardCards.forEach(function(card) {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(card);
    });
    
    // Función para mostrar notificaciones
    window.showNotification = function(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(notification);
        
        // Auto-remover después de 5 segundos
        setTimeout(function() {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 5000);
    };
    
    // Función para confirmar acciones
    window.confirmAction = function(message, callback) {
        if (confirm(message)) {
            callback();
        }
    };
    
    // Función para cargar contenido dinámicamente
    window.loadContent = function(url, targetId) {
        const target = document.getElementById(targetId);
        if (target) {
            target.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"></div><p class="mt-2">Cargando...</p></div>';
            
            fetch(url)
                .then(response => response.text())
                .then(html => {
                    target.innerHTML = html;
                })
                .catch(error => {
                    target.innerHTML = '<div class="alert alert-danger">Error al cargar el contenido</div>';
                    console.error('Error:', error);
                });
        }
    };
    
    // Manejo de formularios con AJAX
    const dashboardForms = document.querySelectorAll('.dashboard-form');
    dashboardForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            
            // Mostrar loading
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Procesando...';
            submitBtn.disabled = true;
            
            // Aquí puedes agregar lógica AJAX si es necesario
            // Por ahora, permitimos que el formulario se envíe normalmente
            
            // Restaurar botón después de un tiempo (simulación)
            setTimeout(function() {
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            }, 2000);
        });
    });
    
    // Responsive sidebar
    function handleResize() {
        if (window.innerWidth > 991) {
            sidebar.classList.remove('show');
        }
    }
    
    window.addEventListener('resize', handleResize);
    
    // Inicializar componentes cuando se carga contenido dinámicamente
    window.initializeDashboardComponents = function() {
        // Reinicializar tooltips
        const newTooltips = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        newTooltips.forEach(function(el) {
            new bootstrap.Tooltip(el);
        });
        
        // Reinicializar popovers
        const newPopovers = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        newPopovers.forEach(function(el) {
            new bootstrap.Popover(el);
        });
    };
    
    // Exportar funciones para uso global
    window.dashboard = {
        showNotification: window.showNotification,
        confirmAction: window.confirmAction,
        loadContent: window.loadContent,
        initializeComponents: window.initializeDashboardComponents
    };
    
}); 