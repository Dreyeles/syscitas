// Funcionalidades del Sidebar - Reutilizable para todos los roles
document.addEventListener('DOMContentLoaded', function() {
    console.log('Sidebar inicializado');
    
    // Inicializar funcionalidades del sidebar
    initializeSidebar();
});

function initializeSidebar() {
    // Configurar toggle del sidebar en móvil
    setupMobileToggle();
    
    // Configurar cierre del sidebar al hacer clic fuera
    setupOutsideClick();
    
    // Configurar manejo de submenús
    setupSubmenus();
    
    // Configurar navegación de tabs
    setupTabNavigation();
    
    // Configurar estado activo basado en la URL
    setupActiveState();
    
    // Configurar responsive behavior
    setupResponsiveBehavior();
}

function setupMobileToggle() {
    const sidebarToggle = document.querySelector('[data-bs-toggle="collapse"]');
    const sidebar = document.getElementById('sidebar');
    
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('show');
        });
    }
}

function setupOutsideClick() {
    const sidebar = document.getElementById('sidebar');
    const sidebarToggle = document.querySelector('[data-bs-toggle="collapse"]');
    
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 991) {
            if (sidebar && sidebarToggle && !sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                sidebar.classList.remove('show');
            }
        }
    });
}

function setupSubmenus() {
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
                
                // Rotar ícono chevron
                rotateChevron(this);
            });
        }
    });
}

function setupTabNavigation() {
    // Interceptar clicks en enlaces del sidebar con data-tab-target
    document.addEventListener('click', function(event) {
        const link = event.target.closest('a');
        if (link && link.hasAttribute('data-tab-target')) {
            event.preventDefault();
            const tabTarget = link.getAttribute('data-tab-target');
            activateTab(tabTarget);
            
            // Actualizar estado activo en el sidebar
            updateActiveSidebarItem(link);
        }
    });
}

function setupActiveState() {
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
}

function setupResponsiveBehavior() {
    const sidebar = document.getElementById('sidebar');
    
    function handleResize() {
        if (window.innerWidth > 991 && sidebar) {
            sidebar.classList.remove('show');
        }
    }
    
    window.addEventListener('resize', handleResize);
}

// Funciones auxiliares
function activateTab(tabId) {
    const tabElement = document.getElementById(tabId);
    if (tabElement) {
        const tabInstance = new bootstrap.Tab(tabElement);
        tabInstance.show();
        
        // Inicializar funcionalidades específicas según el tab
        setTimeout(() => {
            switch(tabId) {
                case 'dashboard':
                    if (typeof inicializarDashboard === 'function') {
                        inicializarDashboard();
                    }
                    break;
                case 'registro-medico':
                    if (typeof inicializarRegistroMedico === 'function') {
                        inicializarRegistroMedico();
                    }
                    break;
                case 'añadir-paciente':
                    if (typeof inicializarAñadirPaciente === 'function') {
                        inicializarAñadirPaciente();
                    }
                    break;
                case 'agendar-cita':
                    if (typeof inicializarAgendarCita === 'function') {
                        inicializarAgendarCita();
                    }
                    break;
                case 'calendario-citas':
                    if (typeof inicializarCalendarioCitas === 'function') {
                        inicializarCalendarioCitas();
                    }
                    break;
                case 'agregar-especialidad':
                    if (typeof inicializarAgregarEspecialidad === 'function') {
                        inicializarAgregarEspecialidad();
                    }
                    break;
                case 'reporte-especialidad':
                    if (typeof inicializarReporteEspecialidad === 'function') {
                        inicializarReporteEspecialidad();
                    }
                    break;
                case 'agregar-servicio':
                    if (typeof inicializarAgregarServicio === 'function') {
                        inicializarAgregarServicio();
                    }
                    break;
                case 'reporte-servicios':
                    if (typeof inicializarReporteServicios === 'function') {
                        inicializarReporteServicios();
                    }
                    break;
                case 'agregar-personal':
                    if (typeof inicializarAgregarPersonal === 'function') {
                        inicializarAgregarPersonal();
                    }
                    break;
                case 'consultar-personal':
                    if (typeof inicializarConsultarPersonal === 'function') {
                        inicializarConsultarPersonal();
                    }
                    break;
                case 'consultar-medico':
                    if (typeof inicializarConsultarMedico === 'function') {
                        inicializarConsultarMedico();
                    }
                    break;

                case 'reporte-pacientes':
                    if (typeof inicializarReportePacientes === 'function') {
                        inicializarReportePacientes();
                    }
                    break;
                case 'historia-clinica':
                    if (typeof inicializarHistoriaClinica === 'function') {
                        inicializarHistoriaClinica();
                    }
                    break;
            }
        }, 100); // Pequeño delay para asegurar que el tab esté completamente cargado
    }
}

function updateActiveSidebarItem(clickedLink) {
    // Remover clase active de todos los enlaces del sidebar
    const allLinks = document.querySelectorAll('.sidebar .nav-link');
    allLinks.forEach(link => {
        link.classList.remove('active');
    });
    
    // Agregar clase active al enlace clickeado
    clickedLink.classList.add('active');
}

function rotateChevron(element) {
    const chevron = element.querySelector('.bi-chevron-down');
    if (chevron) {
        chevron.style.transform = chevron.style.transform === 'rotate(180deg)' 
            ? 'rotate(0deg)' 
            : 'rotate(180deg)';
    }
}

// Funciones específicas para diferentes roles
function setupMedicoSidebar() {
    console.log('Configurando sidebar específico para médicos');
    // Aquí se pueden agregar funcionalidades específicas para médicos
}

function setupAdminSidebar() {
    console.log('Configurando sidebar específico para administrativos');
    // Aquí se pueden agregar funcionalidades específicas para administrativos
}

// Exportar funciones para uso global
window.Sidebar = {
    initializeSidebar,
    setupMobileToggle,
    setupSubmenus,
    setupTabNavigation,
    updateActiveSidebarItem,
    activateTab,
    setupMedicoSidebar,
    setupAdminSidebar
}; 