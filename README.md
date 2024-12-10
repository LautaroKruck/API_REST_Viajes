# Organizador de Viajes en Grupo - API REST

## Descripción del Proyecto
La API REST permite a los usuarios gestionar viajes grupales, facilitando la creación de viajes, la asignación de participantes y la planificación de actividades. Es un sistema seguro y funcional que se puede expandir fácilmente.

---

## Tecnologías Utilizadas
- **Spring Boot**: Framework para construir aplicaciones Java modernas.
- **JWT (JSON Web Tokens)**: Autenticación y autorización.
- **MySQL**: Base de datos relacional para la gestión de entidades y relaciones.
- **Insomnia**: Herramienta para probar los endpoints.

---

## Idea del Proyecto
La API REST permite a los usuarios:
- Crear, editar y eliminar viajes grupales.
- Agregar participantes a los viajes.
- Planificar actividades relacionadas con cada viaje.

---

## Diseño de Tablas

### **a. Usuarios**
| Campo       | Tipo             | Descripción                              |
|-------------|------------------|------------------------------------------|
| `id`        | PK, autogenerado | Identificador único del usuario.         |
| `username`  | String, único    | Nombre de usuario.                       |
| `password`  | String           | Contraseña hasheada.                     |
| `roles`     | String           | Roles: "user" o "admin".                 |

### **b. Viajes**
| Campo           | Tipo             | Descripción                              |
|------------------|------------------|------------------------------------------|
| `id`            | PK, autogenerado | Identificador único del viaje.           |
| `nombre`        | String           | Nombre del viaje.                        |
| `descripcion`   | String           | Descripción del viaje.                   |
| `fecha_inicio`  | Date             | Fecha estimada de inicio del viaje.      |
| `fecha_fin`     | Date             | Fecha estimada de conclusión del viaje.  |
| `organizador_id`| FK a Usuarios    | Usuario organizador del viaje.           |
| `participantes` | List<Usuario>    | Lista de participantes del viaje.        |
| `actividades`   | List<Actividad>  | Lista de actividades planificadas.       |

### **c. Actividades**
| Campo         | Tipo             | Descripción                                |
|---------------|:-----------------|:-------------------------------------------|
| `id`          | PK, autogenerado | Identificador único de la actividad.       |
| `nombre`      | String           | Nombre de la actividad.                    |
| `descripcion` | String          | Descripción de la actividad.               |
| `fecha_hora`  | DateTime         | Fecha y hora de la actividad.              |
| `ubicacion`   | String           | Ubicación de la actividad.                 |

---

## Endpoints

### **Usuarios**
- `POST /usuarios` - Registrar un usuario.
- `POST /login` - Autenticar usuario y devolver JWT.

### **Viajes**
- `GET /viajes` - Obtener todos los viajes del usuario autenticado.
- `POST /viajes` - Crear un viaje (solo usuarios autenticados).
- `PUT /viajes/{id}` - Editar un viaje existente (solo organizador).
- `DELETE /viajes/{id}` - Eliminar un viaje (solo organizador).

### **Actividades**
- `GET /viajes/{id}/actividades` - Listar actividades de un viaje.
- `POST /viajes/{id}/actividades` - Añadir una actividad a un viaje.
- `DELETE /actividades/{id}` - Eliminar una actividad (solo organizador).

---

## Lógica de Negocio
La API sigue una estructura centrada en el usuario:
1. **Gestión de Usuarios**: Los usuarios pueden registrarse, autenticarse y gestionar sus viajes.
2. **Gestión de Viajes**: Solo el organizador del viaje puede editar o eliminar los viajes que ha creado.
3. **Gestión de Actividades**: Las actividades están ligadas a un viaje específico y pueden ser gestionadas únicamente por el organizador.

---

## Excepciones y Códigos de Estado
- `400 Bad Request`: Datos de entrada no válidos.
- `401 Unauthorized`: Usuario no autenticado.
- `403 Forbidden`: Usuario no tiene permisos para realizar la acción.
- `404 Not Found`: Recurso no encontrado.
- `500 Internal Server Error`: Error inesperado en el servidor.

---

## Seguridad
La API implementa las siguientes medidas de seguridad:
- Contraseñas hasheadas con **BCrypt**.
- Autenticación y autorización mediante **JWT**.
- Roles y permisos:
  - **user**: Puede gestionar sus propios viajes.
  - **admin**: Tiene acceso completo a los datos del sistema (opcional).
