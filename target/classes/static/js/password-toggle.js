/**
 * Password Toggle Utility
 * Funcionalidad unificada para mostrar/ocultar contraseñas en toda la aplicación
 */

// Función principal para alternar la visibilidad de contraseñas
function togglePasswordVisibility(targetId) {
    const input = document.getElementById(targetId);
    const icon = document.querySelector(`[data-target="${targetId}"] i`);
    
    if (!input || !icon) {
        console.warn(`No se encontró el input o icono para el target: ${targetId}`);
        return;
    }
    
    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    icon.classList.toggle('bi-eye', isPassword);
    icon.classList.toggle('bi-eye-slash', !isPassword);
}

// Función para inicializar todos los toggles de contraseña en la página
function initializePasswordToggles() {
    console.log('Inicializando password toggles...');
    
    // Buscar todos los elementos con la clase toggle-password
    const toggleButtons = document.querySelectorAll('.toggle-password');
    console.log('Encontrados', toggleButtons.length, 'botones toggle');
    
    toggleButtons.forEach((button, index) => {
        console.log(`Botón ${index + 1}:`, button);
        // Remover event listeners previos para evitar duplicados
        button.removeEventListener('click', handleToggleClick);
        // Agregar el nuevo event listener
        button.addEventListener('click', handleToggleClick);
        console.log(`Event listener agregado al botón ${index + 1}`);
    });
    
    // También buscar elementos con onclick que usen la función antigua
    const oldToggleElements = document.querySelectorAll('[onclick*="togglePasswordVisibility"]');
    oldToggleElements.forEach(element => {
        // Remover el onclick y agregar el event listener moderno
        element.removeAttribute('onclick');
        element.addEventListener('click', handleToggleClick);
    });
}

// Manejador del evento click para los toggles
function handleToggleClick(event) {
    event.preventDefault();
    console.log('Click en toggle de contraseña');
    
    const button = event.currentTarget;
    const targetId = button.getAttribute('data-target');
    console.log('Target ID:', targetId);
    
    if (targetId) {
        togglePasswordVisibility(targetId);
    } else {
        // Fallback para elementos que no usan data-target
        const input = button.closest('.input-group').querySelector('input[type="password"], input[type="text"]');
        if (input) {
            const icon = button.querySelector('i');
            const isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            icon.classList.toggle('bi-eye', isPassword);
            icon.classList.toggle('bi-eye-slash', !isPassword);
            console.log('Contraseña cambiada a:', input.type);
        }
    }
}

// Función para crear dinámicamente un toggle de contraseña
function createPasswordToggle(inputId, options = {}) {
    const input = document.getElementById(inputId);
    if (!input) {
        console.error(`No se encontró el input con ID: ${inputId}`);
        return null;
    }
    
    // Crear el contenedor input-group si no existe
    let inputGroup = input.closest('.input-group');
    if (!inputGroup) {
        inputGroup = document.createElement('div');
        inputGroup.className = 'input-group';
        input.parentNode.insertBefore(inputGroup, input);
        inputGroup.appendChild(input);
    }
    
    // Crear el botón toggle
    const toggleButton = document.createElement('span');
    toggleButton.className = 'input-group-text toggle-password';
    toggleButton.setAttribute('data-target', inputId);
    toggleButton.style.cursor = 'pointer';
    
    // Crear el ícono
    const icon = document.createElement('i');
    icon.className = 'bi bi-eye-slash';
    toggleButton.appendChild(icon);
    
    // Agregar el botón al input-group
    inputGroup.appendChild(toggleButton);
    
    // Agregar el event listener
    toggleButton.addEventListener('click', handleToggleClick);
    
    return toggleButton;
}

// Función para aplicar estilos CSS personalizados
function applyPasswordToggleStyles() {
    if (document.getElementById('password-toggle-styles')) {
        return; // Los estilos ya están aplicados
    }
    
    const style = document.createElement('style');
    style.id = 'password-toggle-styles';
    style.textContent = `
        .toggle-password {
            cursor: pointer;
            transition: all 0.2s ease;
            background-color: #f8f9fa;
            border: 1px solid #ced4da;
            border-left: none;
        }
        
        .toggle-password:hover {
            background-color: #e9ecef;
            color: #0d6efd;
        }
        
        .input-group .form-control:focus + .input-group-text {
            border-color: #0A7A8A;
            box-shadow: 0 0 0 0.2rem rgba(10, 122, 138, 0.25);
        }
        
        .toggle-password i {
            color: #6c757d;
            transition: color 0.2s ease;
        }
        
        .toggle-password:hover i {
            color: #0A7A8A;
        }
        
        .toggle-password:active {
            transform: scale(0.95);
        }
    `;
    
    document.head.appendChild(style);
}

// Inicialización automática cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Aplicar estilos
    applyPasswordToggleStyles();
    
    // Inicializar toggles existentes
    initializePasswordToggles();
    
    // Observar cambios en el DOM para elementos agregados dinámicamente
    if (window.MutationObserver) {
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'childList') {
                    const addedNodes = Array.from(mutation.addedNodes);
                    addedNodes.forEach(function(node) {
                        if (node.nodeType === 1) { // Element node
                            if (node.classList && node.classList.contains('toggle-password')) {
                                node.addEventListener('click', handleToggleClick);
                            } else if (node.querySelectorAll) {
                                const toggles = node.querySelectorAll('.toggle-password');
                                toggles.forEach(toggle => {
                                    toggle.addEventListener('click', handleToggleClick);
                                });
                            }
                        }
                    });
                }
            });
        });
        
        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    }
});

// Exportar funciones para uso global
window.PasswordToggle = {
    toggle: togglePasswordVisibility,
    initialize: initializePasswordToggles,
    create: createPasswordToggle,
    applyStyles: applyPasswordToggleStyles
}; 