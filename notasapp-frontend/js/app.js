// Configuración
const API_URL = 'http://localhost:8080/api';

// Gestión de tokens
function saveToken(token, mantenerSesion) {
    if (mantenerSesion) {
        localStorage.setItem('authToken', token);
    } else {
        sessionStorage.setItem('authToken', token);
    }
}

function getToken() {
    return localStorage.getItem('authToken') || sessionStorage.getItem('authToken');
}

function removeToken() {
    localStorage.removeItem('authToken');
    sessionStorage.removeItem('authToken');
}

function logout() {
    removeToken();
    window.location.href = 'login.html';
}

// Función para hacer peticiones autenticadas
async function fetchWithAuth(url, options = {}) {
    const token = getToken();

    if (!token) {
        logout();
        return;
    }

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };

    const response = await fetch(url, {
        ...options,
        headers
    });

    // Si el token es inválido o expiró, cerrar sesión
    if (response.status === 401) {
        alert('Tu sesión ha expirado. Por favor, inicia sesión nuevamente.');
        logout();
        return;
    }

    return response;
}
