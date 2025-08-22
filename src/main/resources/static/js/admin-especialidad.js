document.addEventListener('DOMContentLoaded', function () {
    const agregarEspecialidadForm = document.getElementById('agregarEspecialidadForm');

    if (agregarEspecialidadForm) {
        agregarEspecialidadForm.addEventListener('submit', function (event) {
            event.preventDefault();
            
            const formData = new FormData(agregarEspecialidadForm);
            
            fetch('/especialidad/agregar', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                // Limpiar errores previos
                const errorMessages = document.querySelectorAll('.invalid-feedback');
                errorMessages.forEach(error => error.textContent = '');
                const formControls = document.querySelectorAll('.form-control');
                formControls.forEach(control => control.classList.remove('is-invalid'));

                if (data.status === 'success') {
                    // Limpiar formulario
                    agregarEspecialidadForm.reset();
                    
                    // Mostrar modal de éxito
                    const exitoModal = new bootstrap.Modal(document.getElementById('exitoModal'));
                    exitoModal.show();
                    
                } else if (data.status === 'error' && data.errors) {
                    // Mostrar errores de validación
                    for (const field in data.errors) {
                        const input = document.getElementById(field);
                        const errorDiv = input.nextElementSibling;
                        if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
                            input.classList.add('is-invalid');
                            errorDiv.textContent = data.errors[field];
                        }
                    }
                } else {
                    // Mostrar error general
                    const alertContainer = document.querySelector('.card-body');
                    const alert = `
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${data.message || 'Ocurrió un error inesperado.'}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    `;
                    alertContainer.insertAdjacentHTML('afterbegin', alert);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                const alertContainer = document.querySelector('.card-body');
                const alert = `
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        Ocurrió un error de conexión. Por favor, inténtelo más tarde.
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                `;
                alertContainer.insertAdjacentHTML('afterbegin', alert);
            });
        });

        // Limpiar formulario con botón "Limpiar"
        const btnLimpiar = document.getElementById('btnLimpiar');
        if(btnLimpiar) {
            btnLimpiar.addEventListener('click', function() {
                agregarEspecialidadForm.reset();
                const errorMessages = document.querySelectorAll('.invalid-feedback');
                errorMessages.forEach(error => error.textContent = '');
                const formControls = document.querySelectorAll('.form-control');
                formControls.forEach(control => control.classList.remove('is-invalid'));
            });
        }
    }
});

