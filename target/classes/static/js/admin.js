// Funcionalidad completa del dashboard administrativo incluyendo médicos, especialidades, servicios, pacientes y personal administrativo
document.addEventListener('DOMContentLoaded', function() {
    
    // ========================================
    // FUNCIONALIDADES GENERALES DEL DASHBOARD
    // ========================================
    
    // El sidebar se maneja ahora en sidebar.js
    // Configurar funcionalidades específicas para administrativos
    if (window.Sidebar) {
        window.Sidebar.setupAdminSidebar();
    }
    
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
    
    // ========================================
    // INICIALIZACIÓN DE BOOTSTRAP TABS
    // ========================================
    
    // Los tabs de Bootstrap se manejan automáticamente
    // Solo necesitamos inicializar las funcionalidades específicas
    
    // ========================================
    // MANEJO DE NAVEGACIÓN DEL SIDEBAR
    // ========================================
    
    // La navegación del sidebar se maneja ahora en sidebar.js
    
    // ========================================
    // FUNCIONALIDAD PARA REGISTRO DE MÉDICOS
    // ========================================
    
    function inicializarRegistroMedico() {
        const registroForm = document.getElementById('registroMedicoForm');
        if (registroForm) {
            registroForm.addEventListener('submit', function(event) {
                // Validar que las contraseñas coincidan antes de enviar
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                
                if (password !== confirmPassword) {
                    event.preventDefault();
                    event.stopPropagation();
                    document.getElementById('confirmPassword').setCustomValidity('Las contraseñas no coinciden');
                    registroForm.classList.add('was-validated');
                    return;
                } else {
                    document.getElementById('confirmPassword').setCustomValidity('');
                }
                
                // Permitir que el formulario se envíe normalmente si es válido
                if (!registroForm.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    registroForm.classList.add('was-validated');
                } else {
                    // Mostrar mensaje de éxito
                    mostrarMensaje('Médico registrado exitosamente', 'success');

                    // Limpiar formulario después de 3 segundos
                    setTimeout(() => {
                        registroForm.reset();
                        registroForm.classList.remove('was-validated');
                        // Limpiar también las horas de turno
                        document.getElementById('horaInicioTurno').value = '';
                        document.getElementById('horaFinTurno').value = '';
                    }, 3000);
                }
            });
        }
        
        // Configurar horas automáticas según el turno seleccionado
        const turnoSelect = document.getElementById('turno');
        if (turnoSelect) {
            turnoSelect.addEventListener('change', function() {
                const horaInicioTurno = document.getElementById('horaInicioTurno');
                const horaFinTurno = document.getElementById('horaFinTurno');
                
                if (this.value === 'MANANA') {
                    horaInicioTurno.value = '07:00';
                    horaFinTurno.value = '14:00';
                } else if (this.value === 'TARDE') {
                    horaInicioTurno.value = '13:00';
                    horaFinTurno.value = '19:00';
                } else {
                    // Si no se selecciona turno, limpiar las horas
                    horaInicioTurno.value = '';
                    horaFinTurno.value = '';
                }
            });
            // Forzar seteo inicial por si el select ya viene con valor
            turnoSelect.dispatchEvent(new Event('change'));
        }
        
        // Toggle de visibilidad de contraseñas
        const togglePassword = document.getElementById('togglePassword');
        const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
        
        if (togglePassword) {
            togglePassword.addEventListener('click', function() {
                const passwordInput = document.getElementById('password');
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                this.querySelector('i').classList.toggle('bi-eye');
                this.querySelector('i').classList.toggle('bi-eye-slash');
            });
        }
        
        if (toggleConfirmPassword) {
            toggleConfirmPassword.addEventListener('click', function() {
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                confirmPasswordInput.setAttribute('type', type);
                this.querySelector('i').classList.toggle('bi-eye');
                this.querySelector('i').classList.toggle('bi-eye-slash');
            });
        }
        
        // Botón limpiar formulario
        const btnLimpiar = document.getElementById('btnLimpiar');
        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                registroForm.reset();
                registroForm.classList.remove('was-validated');
                // Limpiar también las horas de turno
                document.getElementById('horaInicioTurno').value = '';
                document.getElementById('horaFinTurno').value = '';
                mostrarMensaje('Formulario limpiado', 'info');
            });
        }
        
        // Botón cancelar
        const btnCancelar = document.getElementById('btnCancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', function() {
                if (confirm('¿Está seguro que desea cancelar? Los datos ingresados se perderán.')) {
                    registroForm.reset();
                    registroForm.classList.remove('was-validated');
                    // Limpiar también las horas de turno
                    document.getElementById('horaInicioTurno').value = '';
                    document.getElementById('horaFinTurno').value = '';
                    mostrarMensaje('Operación cancelada', 'warning');
                }
            });
        }
    }
    
    // ========================================
    // FUNCIONALIDAD PARA CONSULTA DE MÉDICOS
    // ========================================
    
    function inicializarConsultarMedico() {
        // Botón buscar
        const btnBuscar = document.getElementById('btnBuscar');
        if (btnBuscar) {
            btnBuscar.addEventListener('click', function() {
                console.log('Buscando médicos...');
                mostrarMensajeBusqueda();
            });
        }
        
        // Botón limpiar filtros
        const btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
        if (btnLimpiarFiltros) {
            btnLimpiarFiltros.addEventListener('click', function() {
                document.getElementById('filtroNombre').value = '';
                document.getElementById('filtroEspecialidad').value = '';
                document.getElementById('filtroEstado').value = '';
                document.getElementById('filtroDNI').value = '';
            });
        }
        
        // Botones de exportación
        const btnExportarExcel = document.getElementById('btnExportarExcel');
        if (btnExportarExcel) {
            btnExportarExcel.addEventListener('click', function() {
                console.log('Exportando a Excel...');
                mostrarMensajeExportacion('Excel');
            });
        }
        
        const btnExportarPDF = document.getElementById('btnExportarPDF');
        if (btnExportarPDF) {
            btnExportarPDF.addEventListener('click', function() {
                console.log('Exportando a PDF...');
                mostrarMensajeExportacion('PDF');
            });
        }
        
        // Botones de acciones en la tabla
        document.addEventListener('click', function(event) {
            // Botón ver detalles
            if (event.target.closest('.btn-info')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent + ' ' + row.cells[2].textContent;
                mostrarDetallesMedico(nombre);
            }
            
            // Botón editar
            if (event.target.closest('.btn-warning')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent + ' ' + row.cells[2].textContent;
                console.log('Editando médico:', nombre);
            }
            
            // Botón eliminar
            if (event.target.closest('.btn-danger')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent + ' ' + row.cells[2].textContent;
                mostrarConfirmacionEliminacion(nombre);
            }
        });
        
        // Confirmar eliminación
        const btnConfirmarEliminar = document.getElementById('btnConfirmarEliminar');
        if (btnConfirmarEliminar) {
            btnConfirmarEliminar.addEventListener('click', function() {
                console.log('Eliminando médico...');
                
                const eliminarModal = bootstrap.Modal.getInstance(document.getElementById('eliminarMedicoModal'));
                eliminarModal.hide();
                
                mostrarMensaje('Médico eliminado exitosamente', 'success');
            });
        }
    }
    
    // ========================================
    // FUNCIONALIDAD PARA REPORTE DE ESPECIALIDADES
    // ========================================
    
    function inicializarReporteEspecialidades() {
        // Botón buscar
        const btnBuscar = document.getElementById('btnBuscar');
        if (btnBuscar) {
            btnBuscar.addEventListener('click', function() {
                console.log('Buscando especialidades...');
                mostrarMensajeBusquedaEspecialidades();
            });
        }
        
        // Botón limpiar filtros
        const btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
        if (btnLimpiarFiltros) {
            btnLimpiarFiltros.addEventListener('click', function() {
                document.getElementById('filtroNombre').value = '';
                document.getElementById('filtroDescripcion').value = '';
            });
        }
        
        // Botones de exportación
        const btnExportarExcel = document.getElementById('btnExportarExcel');
        if (btnExportarExcel) {
            btnExportarExcel.addEventListener('click', function() {
                console.log('Exportando especialidades a Excel...');
                mostrarMensajeExportacionEspecialidades('Excel');
            });
        }
        
        const btnExportarPDF = document.getElementById('btnExportarPDF');
        if (btnExportarPDF) {
            btnExportarPDF.addEventListener('click', function() {
                console.log('Exportando especialidades a PDF...');
                mostrarMensajeExportacionEspecialidades('PDF');
            });
        }
        
        // Botones de acciones en la tabla (ya manejados por el script en el HTML)
        // Los botones de detalles ahora usan data attributes y modales de Bootstrap
        
        // Confirmar eliminación
        const btnConfirmarEliminar = document.getElementById('btnConfirmarEliminar');
        if (btnConfirmarEliminar) {
            btnConfirmarEliminar.addEventListener('click', function() {
                console.log('Eliminando especialidad...');
                
                const eliminarModal = bootstrap.Modal.getInstance(document.getElementById('eliminarEspecialidadModal'));
                eliminarModal.hide();
                
                mostrarMensaje('Especialidad eliminada exitosamente', 'success');
            });
        }
    }
    
    // ========================================
    // FUNCIONALIDAD PARA AGREGAR ESPECIALIDAD
    // ========================================
    
    function inicializarAgregarEspecialidad() {
        // Validación del formulario de especialidad
        const especialidadForm = document.getElementById('agregarEspecialidadForm');
        if (especialidadForm) {
            especialidadForm.addEventListener('submit', function(event) {
                // Permitir que el formulario se envíe normalmente
                // Solo validar antes de enviar
                if (!especialidadForm.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    especialidadForm.classList.add('was-validated');
                } else {
                    // Mostrar mensaje de éxito
                    mostrarMensaje('Especialidad agregada exitosamente', 'success');
                    
                    // Limpiar formulario después de 3 segundos
                    setTimeout(() => {
                        especialidadForm.reset();
                        especialidadForm.classList.remove('was-validated');
                    }, 3000);
                }
            });
        }
        
        // Botón limpiar formulario
        const btnLimpiar = document.getElementById('btnLimpiar');
        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                especialidadForm.reset();
                especialidadForm.classList.remove('was-validated');
                mostrarMensaje('Formulario limpiado', 'info');
            });
        }
        
        // Botón cancelar
        const btnCancelar = document.getElementById('btnCancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', function() {
                if (confirm('¿Está seguro que desea cancelar? Los datos ingresados se perderán.')) {
                    especialidadForm.reset();
                    especialidadForm.classList.remove('was-validated');
                    mostrarMensaje('Operación cancelada', 'warning');
                }
            });
        }
    }
    
    // ========================================
    // FUNCIONALIDAD PARA REPORTE DE SERVICIOS
    // ========================================
    
    function inicializarReporteServicios() {
        // Botón buscar
        const btnBuscar = document.getElementById('btnBuscar');
        if (btnBuscar) {
            btnBuscar.addEventListener('click', function() {
                console.log('Buscando servicios...');
                mostrarMensajeBusquedaServicios();
            });
        }
        
        // Botón limpiar filtros
        const btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
        if (btnLimpiarFiltros) {
            btnLimpiarFiltros.addEventListener('click', function() {
                document.getElementById('filtroNombre').value = '';
                document.getElementById('filtroEstado').value = '';
                document.getElementById('filtroCategoria').value = '';
            });
        }
        
        // Botones de exportación
        const btnExportarExcel = document.getElementById('btnExportarExcel');
        if (btnExportarExcel) {
            btnExportarExcel.addEventListener('click', function() {
                console.log('Exportando servicios a Excel...');
                mostrarMensajeExportacionServicios('Excel');
            });
        }
        
        const btnExportarPDF = document.getElementById('btnExportarPDF');
        if (btnExportarPDF) {
            btnExportarPDF.addEventListener('click', function() {
                console.log('Exportando servicios a PDF...');
                mostrarMensajeExportacionServicios('PDF');
            });
        }
        
        // Botones de acciones en la tabla
        document.addEventListener('click', function(event) {
            // Botón ver detalles
            if (event.target.closest('.btn-info')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent;
                mostrarDetallesServicio(nombre);
            }
            
            // Botón editar
            if (event.target.closest('.btn-warning')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent;
                console.log('Editando servicio:', nombre);
            }
            
            // Botón eliminar
            if (event.target.closest('.btn-danger')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[1].textContent;
                mostrarConfirmacionEliminacionServicio(nombre);
            }
        });
        
        // Confirmar eliminación
        const btnConfirmarEliminar = document.getElementById('btnConfirmarEliminar');
        if (btnConfirmarEliminar) {
            btnConfirmarEliminar.addEventListener('click', function() {
                console.log('Eliminando servicio...');
                
                const eliminarModal = bootstrap.Modal.getInstance(document.getElementById('eliminarServicioModal'));
                eliminarModal.hide();
                
                mostrarMensaje('Servicio eliminado exitosamente', 'success');
            });
        }
    }
    
    // ========================================
    // FUNCIONALIDAD PARA AGREGAR SERVICIO
    // ========================================
    
    function inicializarAgregarServicio() {
        // Validación del formulario de servicio
        const servicioForm = document.getElementById('agregarServicioForm');
        if (servicioForm) {
            servicioForm.addEventListener('submit', function(event) {
                // Si no es válido, prevenir envío y marcar validación
                if (!servicioForm.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    servicioForm.classList.add('was-validated');
                }
            });
        }
        
        // Botón limpiar formulario
        const btnLimpiar = document.getElementById('btnLimpiar');
        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                servicioForm.reset();
                servicioForm.classList.remove('was-validated');
            });
        }
        
        // Botón cancelar
        const btnCancelar = document.getElementById('btnCancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', function() {
                if (confirm('¿Está seguro que desea cancelar? Los datos ingresados se perderán.')) {
                    // Cambiar al tab del dashboard
                    const dashboardTab = document.getElementById('dashboard-tab');
                    const dashboardTabInstance = new bootstrap.Tab(dashboardTab);
                    dashboardTabInstance.show();
                }
            });
        }
        
        // Confirmar registro
        const btnConfirmarRegistroServicio = document.getElementById('btnConfirmarRegistroServicio');
        if (btnConfirmarRegistroServicio) {
            btnConfirmarRegistroServicio.addEventListener('click', function() {
                const confirmacionModal = bootstrap.Modal.getInstance(document.getElementById('confirmacionModal'));
                if (confirmacionModal) confirmacionModal.hide();
                // Enviar el formulario real al backend
                servicioForm.submit();
            });
        }
    }
    
    // ========================================
    // FUNCIONES AUXILIARES
    // ========================================
    
    function mostrarMensajeBusqueda() {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-primary border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-search me-2"></i>Buscando médicos...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeBusquedaEspecialidades() {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-info border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-search me-2"></i>Buscando especialidades...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeExportacion(tipo) {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-file-earmark-${tipo.toLowerCase()} me-2"></i>Exportando médicos a ${tipo}...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeExportacionEspecialidades(tipo) {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-file-earmark-${tipo.toLowerCase()} me-2"></i>Exportando especialidades a ${tipo}...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeBusquedaServicios() {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-info border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-search me-2"></i>Buscando servicios...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeExportacionServicios(tipo) {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-file-earmark-${tipo.toLowerCase()} me-2"></i>Exportando servicios a ${tipo}...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarDetallesServicio(nombre) {
        console.log('Mostrando detalles de servicio:', nombre);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesServicioModal'));
        detallesModal.show();
    }
    
    function mostrarConfirmacionEliminacionServicio(nombre) {
        document.getElementById('nombreServicioEliminar').textContent = nombre;
        const eliminarModal = new bootstrap.Modal(document.getElementById('eliminarServicioModal'));
        eliminarModal.show();
    }
    
    function mostrarResumenDatosServicio() {
        const resumenDiv = document.getElementById('resumenDatos');
        if (resumenDiv) {
            const nombre = document.getElementById('nombre').value;
            const precio = document.getElementById('precio').value;
            const estado = document.getElementById('estado')?.value || 'N/A';
            const duracion = document.getElementById('duracion')?.value || '';
            const requiereCita = document.getElementById('requiereCita').checked ? 'Sí' : 'No';
            const requierePago = document.getElementById('requierePago').checked ? 'Sí' : 'No';
            const especialidad = document.getElementById('especialidadId')?.selectedOptions[0]?.text || '';
            
            resumenDiv.innerHTML = `
                <div class="alert alert-info">
                    <h6>Resumen de Datos del Servicio:</h6>
                    <p><strong>Nombre:</strong> ${nombre}</p>
                    <p><strong>Especialidad:</strong> ${especialidad}</p>
                    <p><strong>Precio:</strong> S/. ${precio}</p>
                    <p><strong>Estado:</strong> ${estado}</p>
                    <p><strong>Duración:</strong> ${duracion} minutos</p>
                    <p><strong>Requiere Cita:</strong> ${requiereCita}</p>
                    <p><strong>Requiere Pago:</strong> ${requierePago}</p>
                </div>
            `;
        }
    }
    
    function mostrarDetallesMedico(nombre) {
        console.log('Mostrando detalles de médico:', nombre);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesMedicoModal'));
        detallesModal.show();
    }
    
    function mostrarDetallesEspecialidad(nombre) {
        console.log('Mostrando detalles de especialidad:', nombre);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesEspecialidadModal'));
        detallesModal.show();
    }
    
    function mostrarConfirmacionEliminacion(nombre) {
        document.getElementById('nombreMedicoEliminar').textContent = nombre;
        const eliminarModal = new bootstrap.Modal(document.getElementById('eliminarMedicoModal'));
        eliminarModal.show();
    }
    
    function mostrarConfirmacionEliminacionEspecialidad(nombre) {
        document.getElementById('nombreEspecialidadEliminar').textContent = nombre;
        const eliminarModal = new bootstrap.Modal(document.getElementById('eliminarEspecialidadModal'));
        eliminarModal.show();
    }
    
    function mostrarResumenDatos() {
        const resumenDiv = document.getElementById('resumenDatos');
        if (resumenDiv) {
            const nombre = document.getElementById('nombre').value;
            const codigo = document.getElementById('codigo').value;
            const categoria = document.getElementById('categoria').value;
            const estado = document.getElementById('estado').value;
            
            resumenDiv.innerHTML = `
                <div class="alert alert-info">
                    <h6>Resumen de Datos:</h6>
                    <p><strong>Nombre:</strong> ${nombre}</p>
                    <p><strong>Código:</strong> ${codigo}</p>
                    <p><strong>Categoría:</strong> ${categoria}</p>
                    <p><strong>Estado:</strong> ${estado}</p>
                </div>
            `;
        }
    }
    
    function mostrarMensaje(mensaje, tipo) {
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${tipo} border-0 position-fixed top-0 end-0 m-3`;
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-check-circle me-2"></i>${mensaje}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    // ========================================
    // INICIALIZACIÓN DE COMPONENTES
    // ========================================
    
    function inicializarComponentes() {
        // Inicializar tooltips
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
        
        // Inicializar popovers
        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl);
        });
    }
    
    // ========================================
    // FUNCIONALIDAD PARA PACIENTES
    // ========================================
    
    function inicializarReportePacientes() {
        // Botón Buscar
        const btnBuscarPacientes = document.getElementById('btnBuscarPacientes');
        if (btnBuscarPacientes) {
            btnBuscarPacientes.addEventListener('click', function() {
                mostrarMensajeBusquedaPacientes();
            });
        }
        
        // Botón Limpiar Filtros
        const btnLimpiarFiltrosPacientes = document.getElementById('btnLimpiarFiltrosPacientes');
        if (btnLimpiarFiltrosPacientes) {
            btnLimpiarFiltrosPacientes.addEventListener('click', function() {
                document.getElementById('filtroNombrePaciente').value = '';
                document.getElementById('filtroDNIPaciente').value = '';
                document.getElementById('filtroEstadoPaciente').value = '';
                document.getElementById('filtroEdadPaciente').value = '';
                mostrarMensaje('Filtros limpiados', 'info');
            });
        }
        
        // Botón Exportar Excel
        const btnExportarExcelPacientes = document.getElementById('btnExportarExcelPacientes');
        if (btnExportarExcelPacientes) {
            btnExportarExcelPacientes.addEventListener('click', function() {
                mostrarMensajeExportacionPacientes('Excel');
            });
        }
        
        // Botón Exportar PDF
        const btnExportarPDFPacientes = document.getElementById('btnExportarPDFPacientes');
        if (btnExportarPDFPacientes) {
            btnExportarPDFPacientes.addEventListener('click', function() {
                mostrarMensajeExportacionPacientes('PDF');
            });
        }
    }
    
    function inicializarHistorialPaciente() {
        // Botón Buscar
        const btnBuscarHistorial = document.getElementById('btnBuscarHistorial');
        if (btnBuscarHistorial) {
            btnBuscarHistorial.addEventListener('click', function() {
                mostrarMensajeBusquedaHistorial();
            });
        }
        
        // Botón Limpiar Filtros
        const btnLimpiarFiltrosHistorial = document.getElementById('btnLimpiarFiltrosHistorial');
        if (btnLimpiarFiltrosHistorial) {
            btnLimpiarFiltrosHistorial.addEventListener('click', function() {
                document.getElementById('filtroFechaHistorial').value = '';
                document.getElementById('filtroTipoConsulta').value = '';
                document.getElementById('filtroMedicoHistorial').value = '';
                mostrarMensaje('Filtros limpiados', 'info');
            });
        }
        
        // Botón Exportar Excel
        const btnExportarExcelHistorial = document.getElementById('btnExportarExcelHistorial');
        if (btnExportarExcelHistorial) {
            btnExportarExcelHistorial.addEventListener('click', function() {
                mostrarMensajeExportacionHistorial('Excel');
            });
        }
        
        // Botón Exportar PDF
        const btnExportarPDFHistorial = document.getElementById('btnExportarPDFHistorial');
        if (btnExportarPDFHistorial) {
            btnExportarPDFHistorial.addEventListener('click', function() {
                mostrarMensajeExportacionHistorial('PDF');
            });
        }
        
        // Botón Nueva Consulta
        const btnNuevaConsulta = document.getElementById('btnNuevaConsulta');
        if (btnNuevaConsulta) {
            btnNuevaConsulta.addEventListener('click', function() {
                mostrarMensaje('Redirigiendo a nueva consulta...', 'info');
            });
        }
    }
    
    function inicializarAñadirPaciente() {
        console.log('Inicializando formulario de añadir paciente...');
        
        const form = document.getElementById('añadirPacienteForm');
        if (!form) return;
        
        // Validación del formulario
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
        
        // Botón limpiar
        const btnLimpiar = document.getElementById('btnLimpiar');
        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                form.reset();
                form.classList.remove('was-validated');
                // Limpiar clases de validación
                form.querySelectorAll('.is-invalid').forEach(field => {
                    field.classList.remove('is-invalid');
                });
            });
        }
        
        // Botón cancelar
        const btnCancelar = document.getElementById('btnCancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', function() {
                if (confirm('¿Está seguro de que desea cancelar? Los datos ingresados se perderán.')) {
                    form.reset();
                    form.classList.remove('was-validated');
                    // Limpiar clases de validación
                    form.querySelectorAll('.is-invalid').forEach(field => {
                        field.classList.remove('is-invalid');
                    });
                }
            });
        }
        
        // Validación en tiempo real para DNI
        const dniInput = document.getElementById('dni');
        if (dniInput) {
            dniInput.addEventListener('input', function() {
                const dni = this.value.replace(/\D/g, '');
                if (dni.length > 8) {
                    this.value = dni.substring(0, 8);
                }
            });
        }
        
        // Validación en tiempo real para teléfonos
        const telefonoInput = document.getElementById('telefono');
        if (telefonoInput) {
            telefonoInput.addEventListener('input', function() {
                const telefono = this.value.replace(/\D/g, '');
                if (telefono.length > 20) {
                    this.value = telefono.substring(0, 20);
                }
            });
        }
        
        const telefonoEmergenciaInput = document.getElementById('contactoEmergenciaTelefono');
        if (telefonoEmergenciaInput) {
            telefonoEmergenciaInput.addEventListener('input', function() {
                const telefono = this.value.replace(/\D/g, '');
                if (telefono.length > 11) {
                    this.value = telefono.substring(0, 11);
                }
            });
        }
        
        // Mostrar modal de éxito si existe mensaje de éxito
        const mensajeExito = document.querySelector('.alert-success');
        if (mensajeExito) {
            const modal = new bootstrap.Modal(document.getElementById('registroExitosoModal'));
            modal.show();
        }
    }
    
    // ========================================
    // FUNCIONES AUXILIARES PARA PACIENTES
    // ========================================
    
    function mostrarMensajeBusquedaPacientes() {
        const nombre = document.getElementById('filtroNombrePaciente')?.value || '';
        const dni = document.getElementById('filtroDNIPaciente')?.value || '';
        const estado = document.getElementById('filtroEstadoPaciente')?.value || '';
        const edad = document.getElementById('filtroEdadPaciente')?.value || '';
        
        let filtros = [];
        if (nombre) filtros.push(`Nombre: ${nombre}`);
        if (dni) filtros.push(`DNI: ${dni}`);
        if (estado) filtros.push(`Estado: ${estado}`);
        if (edad) filtros.push(`Edad: ${edad}`);
        
        const mensaje = filtros.length > 0 ? 
            `Buscando pacientes con filtros: ${filtros.join(', ')}` : 
            'Buscando todos los pacientes';
        
        mostrarMensaje(mensaje, 'info');
    }
    
    function mostrarMensajeBusquedaHistorial() {
        const fecha = document.getElementById('filtroFechaHistorial')?.value || '';
        const tipo = document.getElementById('filtroTipoConsulta')?.value || '';
        const medico = document.getElementById('filtroMedicoHistorial')?.value || '';
        
        let filtros = [];
        if (fecha) filtros.push(`Fecha: ${fecha}`);
        if (tipo) filtros.push(`Tipo: ${tipo}`);
        if (medico) filtros.push(`Médico: ${medico}`);
        
        const mensaje = filtros.length > 0 ? 
            `Buscando historial con filtros: ${filtros.join(', ')}` : 
            'Mostrando todo el historial';
        
        mostrarMensaje(mensaje, 'info');
    }
    
    function mostrarMensajeExportacionPacientes(tipo) {
        mostrarMensaje(`Exportando reporte de pacientes a ${tipo}...`, 'success');
    }
    
    function mostrarMensajeExportacionHistorial(tipo) {
        mostrarMensaje(`Exportando historial médico a ${tipo}...`, 'success');
    }
    
    function mostrarDetallesPaciente(nombre) {
        console.log('Mostrando detalles de paciente:', nombre);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesPacienteModal'));
        detallesModal.show();
    }
    
    function mostrarConfirmacionEliminacionPaciente(nombre) {
        document.getElementById('nombrePacienteEliminar').textContent = nombre;
        const eliminarModal = new bootstrap.Modal(document.getElementById('eliminarPacienteModal'));
        eliminarModal.show();
    }
    
    function mostrarResumenDatosPaciente() {
        const resumenDiv = document.getElementById('resumenDatosPaciente');
        if (resumenDiv) {
            const nombre = document.getElementById('nombrePaciente')?.value || '';
            const dni = document.getElementById('dniPaciente')?.value || '';
            const fechaNacimiento = document.getElementById('fechaNacimientoPaciente')?.value || '';
            const genero = document.getElementById('generoPaciente')?.value || '';
            const telefono = document.getElementById('telefonoPaciente')?.value || '';
            const email = document.getElementById('emailPaciente')?.value || '';
            const direccion = document.getElementById('direccionPaciente')?.value || '';
            
            resumenDiv.innerHTML = `
                <div class="alert alert-info">
                    <h6>Resumen de Datos del Paciente:</h6>
                    <p><strong>Nombre:</strong> ${nombre}</p>
                    <p><strong>DNI:</strong> ${dni}</p>
                    <p><strong>Fecha de Nacimiento:</strong> ${fechaNacimiento}</p>
                    <p><strong>Género:</strong> ${genero}</p>
                    <p><strong>Teléfono:</strong> ${telefono}</p>
                    <p><strong>Email:</strong> ${email}</p>
                    <p><strong>Dirección:</strong> ${direccion}</p>
                </div>
            `;
        }
    }
    
    function verDetallesConsulta(idConsulta) {
        console.log('Mostrando detalles de consulta:', idConsulta);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesConsultaModal'));
        detallesModal.show();
    }
    
    function editarConsulta(idConsulta) {
        console.log('Editando consulta:', idConsulta);
        mostrarMensaje('Redirigiendo a edición de consulta...', 'info');
    }
    
    function editarPaciente(idPaciente) {
        console.log('Editando paciente:', idPaciente);
        mostrarMensaje('Redirigiendo a edición de paciente...', 'info');
    }
    
    // ========================================
    // FUNCIONALIDAD PARA ADMINISTRATIVOS
    // ========================================
    
    function inicializarRegistroPersonal() {
        // Validación del formulario de registro
        const registroForm = document.getElementById('registroPersonalForm');
        if (registroForm) {
            registroForm.addEventListener('submit', function(event) {
                event.preventDefault();
                event.stopPropagation();
                
                if (registroForm.checkValidity()) {
                    // Validar que las contraseñas coincidan
                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;
                    
                    if (password !== confirmPassword) {
                        document.getElementById('confirmPassword').setCustomValidity('Las contraseñas no coinciden');
                        registroForm.classList.add('was-validated');
                        return;
                    } else {
                        document.getElementById('confirmPassword').setCustomValidity('');
                    }
                    
                    // Mostrar resumen de datos en el modal
                    mostrarResumenDatosPersonal();
                    
                    // Mostrar modal de confirmación
                    const confirmacionModal = new bootstrap.Modal(document.getElementById('confirmacionModal'));
                    confirmacionModal.show();
                } else {
                    registroForm.classList.add('was-validated');
                }
            });
        }
        
        // Toggle de visibilidad de contraseñas
        const togglePassword = document.getElementById('togglePassword');
        const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
        
        if (togglePassword) {
            togglePassword.addEventListener('click', function() {
                const passwordInput = document.getElementById('password');
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                this.querySelector('i').classList.toggle('bi-eye');
                this.querySelector('i').classList.toggle('bi-eye-slash');
            });
        }
        
        if (toggleConfirmPassword) {
            toggleConfirmPassword.addEventListener('click', function() {
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                confirmPasswordInput.setAttribute('type', type);
                this.querySelector('i').classList.toggle('bi-eye');
                this.querySelector('i').classList.toggle('bi-eye-slash');
            });
        }
        
        // Botón limpiar formulario
        const btnLimpiar = document.getElementById('btnLimpiar');
        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                registroForm.reset();
                registroForm.classList.remove('was-validated');
            });
        }
        
        // Botón cancelar
        const btnCancelar = document.getElementById('btnCancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', function() {
                if (confirm('¿Está seguro que desea cancelar el registro? Los datos ingresados se perderán.')) {
                    // Cambiar al tab del dashboard
                    const dashboardTab = document.getElementById('dashboard-tab');
                    const dashboardTabInstance = new bootstrap.Tab(dashboardTab);
                    dashboardTabInstance.show();
                }
            });
        }
        
        // Confirmar registro
        const btnConfirmarRegistro = document.getElementById('btnConfirmarRegistro');
        if (btnConfirmarRegistro) {
            btnConfirmarRegistro.addEventListener('click', function() {
                // Aquí se enviaría el formulario al backend
                console.log('Registrando personal administrativo...');
                
                // Simular envío exitoso
                setTimeout(() => {
                    const confirmacionModal = bootstrap.Modal.getInstance(document.getElementById('confirmacionModal'));
                    confirmacionModal.hide();
                    
                    const exitoModal = new bootstrap.Modal(document.getElementById('exitoModal'));
                    exitoModal.show();
                    
                    // Limpiar formulario después del registro exitoso
                    setTimeout(() => {
                        registroForm.reset();
                        registroForm.classList.remove('was-validated');
                        exitoModal.hide();
                    }, 2000);
                }, 1000);
            });
        }
    }
    
    function inicializarConsultarPersonal() {
        // Botón buscar
        const btnBuscar = document.getElementById('btnBuscar');
        if (btnBuscar) {
            btnBuscar.addEventListener('click', function() {
                console.log('Buscando personal administrativo...');
                mostrarMensajeBusquedaPersonal();
            });
        }
        
        // Botón limpiar filtros
        const btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
        if (btnLimpiarFiltros) {
            btnLimpiarFiltros.addEventListener('click', function() {
                document.getElementById('filtroNombre').value = '';
                document.getElementById('filtroDNI').value = '';
                document.getElementById('filtroCargo').value = '';
                document.getElementById('filtroEstado').value = '';
            });
        }
        
        // Botones de exportación
        const btnExportarExcel = document.getElementById('btnExportarExcel');
        if (btnExportarExcel) {
            btnExportarExcel.addEventListener('click', function() {
                console.log('Exportando personal administrativo a Excel...');
                mostrarMensajeExportacionPersonal('Excel');
            });
        }
        
        const btnExportarPDF = document.getElementById('btnExportarPDF');
        if (btnExportarPDF) {
            btnExportarPDF.addEventListener('click', function() {
                console.log('Exportando personal administrativo a PDF...');
                mostrarMensajeExportacionPersonal('PDF');
            });
        }
        
        // Botones de acciones en la tabla
        document.addEventListener('click', function(event) {
            // Botón ver detalles
            if (event.target.closest('.btn-info')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[2].textContent + ' ' + row.cells[3].textContent;
                mostrarDetallesPersonal(nombre);
            }
            
            // Botón editar
            if (event.target.closest('.btn-warning')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[2].textContent + ' ' + row.cells[3].textContent;
                console.log('Editando personal administrativo:', nombre);
            }
            
            // Botón eliminar
            if (event.target.closest('.btn-danger')) {
                const row = event.target.closest('tr');
                const nombre = row.cells[2].textContent + ' ' + row.cells[3].textContent;
                mostrarConfirmacionEliminacionPersonal(nombre);
            }
        });
        
        // Confirmar eliminación
        const btnConfirmarEliminar = document.getElementById('btnConfirmarEliminar');
        if (btnConfirmarEliminar) {
            btnConfirmarEliminar.addEventListener('click', function() {
                console.log('Eliminando personal administrativo...');
                
                const eliminarModal = bootstrap.Modal.getInstance(document.getElementById('eliminarPersonalModal'));
                eliminarModal.hide();
                
                mostrarMensaje('Personal administrativo eliminado exitosamente', 'success');
            });
        }
    }
    
    // ========================================
    // FUNCIONES AUXILIARES PARA ADMINISTRATIVOS
    // ========================================
    
    function mostrarMensajeBusquedaPersonal() {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-info border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-search me-2"></i>Buscando personal administrativo...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarMensajeExportacionPersonal(tipo) {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0 position-fixed top-0 end-0 m-3';
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-file-earmark-${tipo.toLowerCase()} me-2"></i>Exportando personal administrativo a ${tipo}...
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        document.body.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
    
    function mostrarDetallesPersonal(nombre) {
        console.log('Mostrando detalles de personal administrativo:', nombre);
        const detallesModal = new bootstrap.Modal(document.getElementById('detallesPersonalModal'));
        detallesModal.show();
    }
    
    function mostrarConfirmacionEliminacionPersonal(nombre) {
        document.getElementById('nombrePersonalEliminar').textContent = nombre;
        const eliminarModal = new bootstrap.Modal(document.getElementById('eliminarPersonalModal'));
        eliminarModal.show();
    }
    
    function mostrarResumenDatosPersonal() {
        const resumenDiv = document.getElementById('resumenDatos');
        if (resumenDiv) {
            const dni = document.getElementById('dni')?.value || '';
            const nombres = document.getElementById('nombres')?.value || '';
            const apellidos = document.getElementById('apellidos')?.value || '';
            const fechaNacimiento = document.getElementById('fechaNacimiento')?.value || '';
            const genero = document.getElementById('genero')?.value || '';
            const estadoCivil = document.getElementById('estadoCivil')?.value || '';
            const telefono = document.getElementById('telefono')?.value || '';
            const email = document.getElementById('email')?.value || '';
            const direccion = document.getElementById('direccion')?.value || '';
            const cargo = document.getElementById('cargo')?.value || '';
            const departamento = document.getElementById('departamento')?.value || '';
            const fechaContratacion = document.getElementById('fechaContratacion')?.value || '';
            const tipoContrato = document.getElementById('tipoContrato')?.value || '';
            const salario = document.getElementById('salario')?.value || '';
            const estado = document.getElementById('estado')?.value || '';
            const username = document.getElementById('username')?.value || '';
            const nivelAcceso = document.getElementById('nivelAcceso')?.value || '';
            
            resumenDiv.innerHTML = `
                <div class="alert alert-info">
                    <h6>Resumen de Datos del Personal Administrativo:</h6>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>DNI:</strong> ${dni}</p>
                            <p><strong>Nombres:</strong> ${nombres}</p>
                            <p><strong>Apellidos:</strong> ${apellidos}</p>
                            <p><strong>Fecha de Nacimiento:</strong> ${fechaNacimiento}</p>
                            <p><strong>Género:</strong> ${genero}</p>
                            <p><strong>Estado Civil:</strong> ${estadoCivil}</p>
                            <p><strong>Teléfono:</strong> ${telefono}</p>
                            <p><strong>Email:</strong> ${email}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Dirección:</strong> ${direccion}</p>
                            <p><strong>Cargo:</strong> ${cargo}</p>
                            <p><strong>Departamento:</strong> ${departamento}</p>
                            <p><strong>Fecha de Contratación:</strong> ${fechaContratacion}</p>
                            <p><strong>Tipo de Contrato:</strong> ${tipoContrato}</p>
                            <p><strong>Salario Base:</strong> S/. ${salario}</p>
                            <p><strong>Estado:</strong> ${estado}</p>
                            <p><strong>Usuario:</strong> ${username}</p>
                            <p><strong>Nivel de Acceso:</strong> ${nivelAcceso}</p>
                        </div>
                    </div>
                </div>
            `;
        }
    }
    
    // ========================================
    // INICIALIZACIÓN INICIAL
    // ========================================
    
    // Inicializar funcionalidades al cargar la página
    inicializarRegistroMedico();
         // Activar tab vía parámetro ?tab=...
    (function activarTabPorParametro() {
        const params = new URLSearchParams(location.search);
        const tab = params.get('tab');
        const ok = params.get('ok');
        if (!tab) return;
        const trigger = document.querySelector(`.nav-link[data-bs-target="#${tab}"]`);
        if (trigger) {
            new bootstrap.Tab(trigger).show();
            if (ok === '1') {
                // mostrar modal de éxito si existe en el DOM del tab
                const tabPane = document.querySelector(`#${tab}`);
                    const modalEl = tabPane && tabPane.querySelector('#registroExitosoModal');
                if (modalEl) {
                    const modal = new bootstrap.Modal(modalEl);
                    modal.show();
                }
            }
        }
    })();
    inicializarConsultarMedico();
    inicializarReporteEspecialidades();
    inicializarAgregarEspecialidad();
    inicializarReporteServicios();
    inicializarAgregarServicio();
    inicializarReportePacientes();
    inicializarHistorialPaciente();
    inicializarAñadirPaciente();
    inicializarRegistroPersonal();
    inicializarConsultarPersonal();
    inicializarComponentes();
    
    console.log('Script principal del dashboard administrativo cargado correctamente - incluye funcionalidades para médicos, especialidades, servicios, pacientes y personal administrativo');
}); 