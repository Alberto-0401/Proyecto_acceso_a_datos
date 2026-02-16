# NotasApp - Aplicación de Gestión de Notas

Aplicación completa de gestión de notas con autenticación JWT, desarrollada con Spring Boot, MongoDB y frontend en HTML/JavaScript.

## Características

- **Autenticación segura** con JWT y Spring Security
- **Encriptación de contraseñas** con BCrypt
- **Gestión completa de notas** (crear, leer, actualizar, eliminar)
- **Sesiones persistentes** - Opción de mantener sesión por 30 días
- **Interfaz moderna y responsive** con diseño limpio
- **MongoDB** con creación automática de colecciones
- **Validación** en frontend y backend

## Tecnologías

### Backend
- Spring Boot 3.2.1
- Spring Security
- MongoDB
- JWT (jsonwebtoken 0.12.3)
- Maven
- Java 17

### Frontend
- HTML5
- CSS3 (diseño responsive)
- JavaScript (Vanilla)

## Requisitos Previos

1. **Java 17** o superior
2. **Maven** 3.6 o superior
3. **MongoDB** instalado y corriendo en `localhost:27017`

## Instalación y Configuración

### 1. Configurar MongoDB

Asegúrate de que MongoDB esté instalado y corriendo:

```bash
# Windows (si MongoDB está instalado como servicio)
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
# o
mongod --dbpath /ruta/a/datos
```

La base de datos `notasapp` y las colecciones se crearán automáticamente.

### 2. Backend - Spring Boot

```bash
# Navegar al directorio del backend
cd notasapp-backend

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

**Usuario de prueba creado automáticamente:**
- Username: `demo`
- Password: `demo123`

### 3. Frontend

El frontend es estático y se puede abrir de varias formas:

**Opción 1: Live Server (Visual Studio Code)**
1. Instala la extensión "Live Server"
2. Abre la carpeta `notasapp-frontend`
3. Click derecho en `login.html` → "Open with Live Server"
4. El navegador abrirá en `http://localhost:5500` o `http://127.0.0.1:5500`

**Opción 2: Abrir directamente**
1. Navega a `notasapp-frontend`
2. Abre `login.html` en tu navegador

**Nota:** Si usas otro servidor local, asegúrate de que el puerto esté configurado en el CORS del backend (SecurityConfig.java).

## Estructura del Proyecto

```
PSP/
├── notasapp-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/notasapp/
│   │   │   │   ├── config/          # Configuración Spring Security y CORS
│   │   │   │   ├── controller/      # Controladores REST
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── filter/          # Filtro JWT
│   │   │   │   ├── model/           # Entidades MongoDB
│   │   │   │   ├── repository/      # Repositorios MongoDB
│   │   │   │   ├── service/         # Lógica de negocio
│   │   │   │   ├── util/            # Utilidades (JWT)
│   │   │   │   └── NotasappApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── notasapp-frontend/
    ├── css/
    │   └── styles.css
    ├── js/
    │   └── app.js
    ├── login.html
    ├── registro.html
    └── home.html
```

## API Endpoints

### Autenticación

#### Registro
```http
POST /api/auth/registro
Content-Type: application/json

{
  "username": "usuario",
  "email": "usuario@email.com",
  "password": "contraseña123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario",
  "password": "contraseña123",
  "mantenerSesion": true  // true = 30 días, false = 1 día
}
```

#### Perfil de Usuario
```http
GET /api/auth/perfil
Authorization: Bearer {token}
```

### Notas (requieren autenticación)

#### Obtener todas las notas del usuario
```http
GET /api/notas
Authorization: Bearer {token}
```

#### Crear nota
```http
POST /api/notas
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Mi nota",
  "contenido": "Contenido de la nota"
}
```

#### Actualizar nota
```http
PUT /api/notas/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Nota actualizada",
  "contenido": "Nuevo contenido"
}
```

#### Eliminar nota
```http
DELETE /api/notas/{id}
Authorization: Bearer {token}
```

## Seguridad

- **Contraseñas encriptadas** con BCrypt (factor de costo predeterminado)
- **Tokens JWT** firmados con HS256
- **Validación** de tokens en cada petición protegida
- **CORS configurado** para permitir solo orígenes específicos
- **Sesiones stateless** - No se almacena estado en el servidor
- **Índices únicos** en username y email

## Flujo de Autenticación

1. Usuario se registra o inicia sesión
2. Backend valida credenciales y genera JWT
3. Token se almacena en:
   - `localStorage` si "Mantener sesión" está activado (30 días)
   - `sessionStorage` si no (1 día, se borra al cerrar navegador)
4. Frontend incluye token en header `Authorization: Bearer {token}`
5. Filtro JWT valida token y extrae userId
6. Usuario solo accede a sus propias notas

## Configuración

### application.properties

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/notasapp
spring.data.mongodb.auto-index-creation=true

# Servidor
server.port=8080

# Logging
logging.level.org.springframework.data.mongodb=DEBUG
logging.level.com.notasapp=DEBUG
```

### Cambiar Puerto del Backend

Si necesitas cambiar el puerto del backend:

1. Edita `application.properties`:
   ```properties
   server.port=NUEVO_PUERTO
   ```

2. Actualiza `app.js` en el frontend:
   ```javascript
   const API_URL = 'http://localhost:NUEVO_PUERTO/api';
   ```

### Configurar CORS

Para permitir otros orígenes, edita `SecurityConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:5500",
    "http://tu-nuevo-origen.com"
));
```

## Validaciones

### Frontend
- Username: mínimo 3 caracteres
- Email: formato válido
- Password: mínimo 6 caracteres
- Confirmación de contraseña

### Backend
- Username: requerido, único
- Email: requerido, único, formato válido
- Password: mínimo 6 caracteres
- Título de nota: requerido
- Contenido de nota: requerido
- Autorización: solo acceso a notas propias

## Solución de Problemas

### MongoDB no se conecta
```bash
# Verificar que MongoDB está corriendo
# Windows
sc query MongoDB

# Linux/Mac
sudo systemctl status mongod
```

### Error de CORS
- Verifica que el frontend esté corriendo en un puerto permitido (5500, 5501)
- Agrega el puerto a `SecurityConfig.java` si es diferente

### Token inválido
- Los tokens expiran después de 1 día (sesión normal) o 30 días (mantener sesión)
- Cierra sesión y vuelve a iniciar

### Puerto 8080 en uso
- Cambia el puerto en `application.properties`
- Actualiza `API_URL` en `app.js`

## Usuario de Prueba

Al iniciar la aplicación, se crea automáticamente:

- **Username:** demo
- **Password:** demo123
- **Email:** demo@notasapp.com

## Desarrollo

### Agregar dependencias
Edita `pom.xml` y ejecuta:
```bash
mvn clean install
```

### Hot reload (backend)
Spring Boot DevTools está incluido - los cambios se recargan automáticamente.

### Logs
Los logs se muestran en la consola con nivel DEBUG para MongoDB y la aplicación.

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## Autor

Desarrollado con Spring Boot, MongoDB y mucho café.
# Proyecto_acceso_a_datos
