// JavaScript para el Dashboard de Médicos
document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard de Médicos cargado');
    
    // Obtener el rol del usuario desde el atributo data del body o desde una variable global
    const userRole = document.body.getAttribute('data-user-role') || 'ROLE_MEDICO';
    
    // Inicializar funcionalidades específicas para médicos
    initializeMedicoDashboard();
    
    // Configurar funcionalidades específicas del sidebar para médicos
    if (window.Sidebar) {
        window.Sidebar.setupMedicoSidebar();
    }
});

function initializeMedicoDashboard() {
    console.log('Inicializando dashboard médico...');
    
    // Aquí se pueden agregar inicializaciones específicas para médicos
    // Por ejemplo, cargar datos de agenda, historiales, etc.
    
    // Ejemplo: Cargar agenda del médico
    loadMedicoAgenda();
    
    // Ejemplo: Configurar notificaciones
    setupMedicoNotifications();
}

// Las funciones del sidebar se manejan ahora en sidebar.js

function loadMedicoAgenda() {
    console.log('Cargando agenda del médico...');
    
    // Aquí se implementaría la lógica para cargar la agenda del médico
    // Por ejemplo, hacer una llamada AJAX para obtener las citas del día
    
    // Ejemplo de implementación:
    /*
    fetch('/api/medico/agenda')
        .then(response => response.json())
        .then(data => {
            updateAgendaDisplay(data);
        })
        .catch(error => {
            console.error('Error cargando agenda:', error);
        });
    */
}

function setupMedicoNotifications() {
    console.log('Configurando notificaciones para médico...');
    
    // Aquí se implementaría la lógica para configurar notificaciones
    // Por ejemplo, notificaciones de nuevas citas, recordatorios, etc.
    
    // Ejemplo de implementación:
    /*
    // Configurar WebSocket para notificaciones en tiempo real
    const socket = new WebSocket('ws://localhost:8080/notifications');
    
    socket.onmessage = function(event) {
        const notification = JSON.parse(event.data);
        showNotification(notification);
    };
    */
}

function showNotification(notification) {
    // Implementar lógica para mostrar notificaciones
    console.log('Nueva notificación:', notification);
    
    // Ejemplo: Mostrar toast notification
    if ('Notification' in window && Notification.permission === 'granted') {
        new Notification(notification.title, {
            body: notification.message,
            icon: '/img/logo.svg'
        });
    }
}

// Funciones específicas para cada sección del dashboard médico

function loadAgendaAtencion() {
    console.log('Cargando agenda de atención médica...');
    // Implementar lógica específica para cargar agenda de atención
}

function loadRegistroAtencion() {
    console.log('Cargando registro de atención médica...');
    // Implementar lógica específica para registro de atención
}

function loadConsultarHistorial() {
    console.log('Cargando consulta de historial clínico...');
    // Implementar lógica específica para consultar historial
}

function loadRegistrarHistorial() {
    console.log('Cargando registro de historial clínico...');
    // Implementar lógica específica para registrar historial
}

// Exportar funciones para uso global si es necesario
window.MedicoDashboard = {
    loadAgendaAtencion,
    loadRegistroAtencion,
    loadConsultarHistorial,
    loadRegistrarHistorial,
    activateTab
}; 