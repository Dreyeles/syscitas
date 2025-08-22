// ========================================
// FUNCIONES DE DEBUG Y LOGGING
// ========================================

// Función para debug del formulario
function debugForm() {
    console.log('=== DEBUG DEL FORMULARIO ===');
    const form = document.getElementById('registroForm');
    const formData = new FormData(form);
    
    for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
    }
    
    // Verificar campos específicos
    console.log('DNI:', document.getElementById('dni')?.value);
    console.log('Email:', document.getElementById('correo')?.value);
    console.log('Sexo:', document.getElementById('sexo')?.value);
    console.log('Estado Civil:', document.getElementById('estadoCivil')?.value);
    console.log('Distrito:', document.getElementById('distrito')?.value);
    console.log('Parentesco:', document.getElementById('contactoEmergenciaParentesco')?.value);
}

// ========================================
// FUNCIONES DE VALIDACIÓN DEL FORMULARIO DE REGISTRO
// ========================================

// Función para capitalizar primera letra de cada palabra
function capitalizeWords(str) {
    return str.replace(/\b\w/g, function(char) {
        return char.toUpperCase();
    });
}

// Función para validar campo vacío
function validateField(field, fieldName) {
    const value = field.value.trim();
    const isValid = value.length > 0;
    
    if (!isValid) {
        showFieldError(field, `${fieldName} es obligatorio`);
    } else {
        clearFieldError(field);
    }
    
    return isValid;
}

// Función para validar apellidos (mínimo 2)
function validateApellidos(field) {
    const apellidos = field.value.trim();
    const apellidosArray = apellidos.split(' ').filter(apellido => apellido.length > 0);
    
    if (apellidos.length === 0) {
        showFieldError(field, 'Los apellidos son obligatorios');
        return false;
    } else if (apellidosArray.length < 2) {
        showFieldError(field, 'Debe ingresar al menos 2 apellidos separados por espacio');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar email
function validateEmail(field) {
    const email = field.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(email);
    
    if (email.length === 0) {
        showFieldError(field, 'El correo electrónico es obligatorio');
        return false;
    } else if (!isValid) {
        showFieldError(field, 'Ingrese un correo electrónico válido');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar contraseña
function validatePassword(field) {
    const password = field.value;
    const isValid = password.length >= 6;
    
    if (password.length === 0) {
        showFieldError(field, 'La contraseña es obligatoria');
        return false;
    } else if (!isValid) {
        showFieldError(field, 'La contraseña debe tener al menos 6 caracteres');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar confirmación de contraseña
function validateConfirmPassword(passwordField, confirmField) {
    const password = passwordField.value;
    const confirmPassword = confirmField.value;
    
    if (confirmPassword.length === 0) {
        showFieldError(confirmField, 'Debe confirmar la contraseña');
        return false;
    } else if (password !== confirmPassword) {
        showFieldError(confirmField, 'Las contraseñas no coinciden');
        return false;
    } else {
        clearFieldError(confirmField);
        return true;
    }
}

// Función para validar DNI
function validateDNI(field) {
    const dni = field.value.trim();
    const dniRegex = /^\d{8}$/;
    const isValid = dniRegex.test(dni);
    
    if (dni.length === 0) {
        showFieldError(field, 'El DNI es obligatorio');
        return false;
    } else if (!isValid) {
        showFieldError(field, 'El DNI debe tener 8 dígitos numéricos');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar teléfono
function validatePhone(field) {
    const phone = field.value.trim();
    const phoneRegex = /^\d{9}$/;
    const isValid = phoneRegex.test(phone);
    
    if (phone.length === 0) {
        showFieldError(field, 'El teléfono es obligatorio');
        return false;
    } else if (!isValid) {
        showFieldError(field, 'El teléfono debe tener 9 dígitos numéricos');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar teléfono de emergencia (permite 9-11 dígitos)
function validateEmergencyPhone(field) {
    const phone = field.value.trim();
    const phoneRegex = /^\d{9,11}$/;
    const isValid = phoneRegex.test(phone);
    
    if (phone.length === 0) {
        showFieldError(field, 'El teléfono de emergencia es obligatorio');
        return false;
    } else if (!isValid) {
        showFieldError(field, 'El teléfono de emergencia debe tener entre 9 y 11 dígitos numéricos');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar fecha de nacimiento
function validateDate(field) {
    const date = field.value;
    const isValid = date.length > 0;
    
    if (!isValid) {
        showFieldError(field, 'La fecha de nacimiento es obligatoria');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para validar select
function validateSelect(field, fieldName) {
    const value = field.value;
    const isValid = value.length > 0;
    
    if (!isValid) {
        showFieldError(field, `Debe seleccionar ${fieldName}`);
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

// Función para mostrar error en campo
function showFieldError(field, message) {
    field.classList.add('is-invalid');
    field.classList.remove('is-valid');
    
    // Crear o actualizar mensaje de error
    let errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'invalid-feedback';
        field.parentNode.appendChild(errorDiv);
    }
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
}

// Función para limpiar error de campo
function clearFieldError(field) {
    field.classList.remove('is-invalid');
    field.classList.add('is-valid');
    
    const errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.style.display = 'none';
    }
}

// Función para validar todo el formulario
function validateForm() {
    let isValid = true;
    
    // Validar campos obligatorios
    isValid = validateEmail(document.getElementById('correo')) && isValid;
    isValid = validatePassword(document.getElementById('contrasenia')) && isValid;
    isValid = validateConfirmPassword(document.getElementById('contrasenia'), document.getElementById('confirmarContrasenia')) && isValid;
    isValid = validateField(document.getElementById('nombres'), 'Los nombres') && isValid;
    isValid = validateApellidos(document.getElementById('apellidos')) && isValid;
    isValid = validateDNI(document.getElementById('dni')) && isValid;
    isValid = validateDate(document.getElementById('fechaNacimiento')) && isValid;
    isValid = validateSelect(document.getElementById('sexo'), 'el sexo') && isValid;
    isValid = validateSelect(document.getElementById('estadoCivil'), 'el estado civil') && isValid;
    isValid = validateField(document.getElementById('direccion'), 'La dirección') && isValid;
    isValid = validateField(document.getElementById('distrito'), 'El distrito') && isValid;
    isValid = validatePhone(document.getElementById('telefono')) && isValid;
    isValid = validateField(document.getElementById('contactoEmergenciaNombre'), 'El nombre del contacto de emergencia') && isValid;
    isValid = validateEmergencyPhone(document.getElementById('contactoEmergenciaTelefono')) && isValid;
    isValid = validateSelect(document.getElementById('contactoEmergenciaParentesco'), 'el parentesco del contacto') && isValid;
    
    return isValid;
}

// ========================================
// INICIALIZACIÓN CUANDO EL DOM ESTÉ LISTO
// ========================================
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registroForm');
    const telefonoInputs = document.querySelectorAll('#telefono, #contactoEmergenciaTelefono');
    
    // Validación de teléfonos - solo permitir números
    telefonoInputs.forEach(function(input) {
        input.addEventListener('input', function(e) {
            // Remover cualquier carácter que no sea número
            this.value = this.value.replace(/[^0-9]/g, '');
            
            // Limitar según el tipo de teléfono
            const maxLength = this.id === 'contactoEmergenciaTelefono' ? 11 : 9;
            if (this.value.length > maxLength) {
                this.value = this.value.slice(0, maxLength);
            }
            
            // Validar en tiempo real
            if (this.id === 'contactoEmergenciaTelefono') {
                validateEmergencyPhone(this);
            } else {
                validatePhone(this);
            }
        });
        
        // Prevenir pegar texto que no sean números
        input.addEventListener('paste', function(e) {
            e.preventDefault();
            const pastedText = (e.clipboardData || window.clipboardData).getData('text');
            const maxLength = this.id === 'contactoEmergenciaTelefono' ? 11 : 9;
            const numbersOnly = pastedText.replace(/[^0-9]/g, '').slice(0, maxLength);
            this.value = numbersOnly;
            
            if (this.id === 'contactoEmergenciaTelefono') {
                validateEmergencyPhone(this);
            } else {
                validatePhone(this);
            }
        });
    });

    // Validación en tiempo real para campos de texto
    const textFields = ['direccion', 'distrito', 'contactoEmergenciaNombre'];
    textFields.forEach(function(fieldId) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('blur', function() {
                validateField(this, this.previousElementSibling.textContent.replace(':', ''));
            });
            field.addEventListener('input', function() {
                if (this.value.trim().length > 0) {
                    clearFieldError(this);
                }
            });
        }
    });

    // Validación y capitalización para nombres
    const nombresField = document.getElementById('nombres');
    if (nombresField) {
        nombresField.addEventListener('blur', function() {
            // Capitalizar al perder el foco
            this.value = capitalizeWords(this.value);
            validateField(this, 'Los nombres');
        });
        nombresField.addEventListener('input', function() {
            if (this.value.trim().length > 0) {
                clearFieldError(this);
            }
        });
    }

    // Validación y capitalización para apellidos
    const apellidosField = document.getElementById('apellidos');
    if (apellidosField) {
        apellidosField.addEventListener('blur', function() {
            // Capitalizar al perder el foco
            this.value = capitalizeWords(this.value);
            validateApellidos(this);
        });
        apellidosField.addEventListener('input', function() {
            if (this.value.trim().length > 0) {
                clearFieldError(this);
            }
        });
    }

    // Validación en tiempo real para email
    const emailField = document.getElementById('correo');
    if (emailField) {
        emailField.addEventListener('blur', function() {
            validateEmail(this);
        });
        emailField.addEventListener('input', function() {
            if (this.value.trim().length > 0) {
                clearFieldError(this);
            }
        });
    }

    // Validación en tiempo real para contraseñas
    const passwordField = document.getElementById('contrasenia');
    const confirmPasswordField = document.getElementById('confirmarContrasenia');
    
    if (passwordField) {
        passwordField.addEventListener('blur', function() {
            validatePassword(this);
            if (confirmPasswordField.value.length > 0) {
                validateConfirmPassword(passwordField, confirmPasswordField);
            }
        });
        passwordField.addEventListener('input', function() {
            if (this.value.length >= 6) {
                clearFieldError(this);
            }
            if (confirmPasswordField.value.length > 0) {
                validateConfirmPassword(passwordField, confirmPasswordField);
            }
        });
    }

    if (confirmPasswordField) {
        confirmPasswordField.addEventListener('blur', function() {
            validateConfirmPassword(passwordField, this);
        });
        confirmPasswordField.addEventListener('input', function() {
            if (passwordField.value === this.value && this.value.length > 0) {
                clearFieldError(this);
            }
        });
    }

    // Validación en tiempo real para DNI
    const dniField = document.getElementById('dni');
    if (dniField) {
        dniField.addEventListener('blur', function() {
            validateDNI(this);
        });
        dniField.addEventListener('input', function() {
            // Solo permitir números
            this.value = this.value.replace(/[^0-9]/g, '');
            if (this.value.length > 8) {
                this.value = this.value.slice(0, 8);
            }
            if (this.value.length === 8) {
                clearFieldError(this);
            }
        });
    }

    // Validación en tiempo real para fecha
    const dateField = document.getElementById('fechaNacimiento');
    if (dateField) {
        dateField.addEventListener('change', function() {
            validateDate(this);
        });
    }

    // Validación en tiempo real para selects
    const selectFields = ['sexo', 'estadoCivil', 'contactoEmergenciaParentesco'];
    selectFields.forEach(function(fieldId) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('change', function() {
                validateSelect(this, this.previousElementSibling.textContent.replace(':', ''));
            });
        }
    });

    // Validación al enviar el formulario
    form.addEventListener('submit', function(e) {
        // Debug del formulario antes de validar
        debugForm();
        
        if (!validateForm()) {
            e.preventDefault();
            // Mostrar mensaje general de error
            const submitButton = form.querySelector('button[type="submit"]');
            const originalText = submitButton.textContent;
            submitButton.textContent = 'Por favor, corrija los errores';
            submitButton.disabled = true;
            
            setTimeout(function() {
                submitButton.textContent = originalText;
                submitButton.disabled = false;
            }, 2000);
            
            // Hacer scroll al primer error
            const firstError = form.querySelector('.is-invalid');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    });
}); 