# Organizador de Viajes en Grupo - PLANIT - API REST Segura

## Descripción del Proyecto
La API REST permite a los usuarios gestionar viajes grupales, facilitando la creación de viajes, la asignación de participantes y la planificación de actividades. Es un sistema seguro y funcional que se puede expandir fácilmente.

---

## Idea de API REST
La API REST permite a los usuarios:
- Crear, editar y eliminar viajes grupales.
- Agregar participantes a los viajes.
- Planificar actividades relacionadas con cada viaje.

---

## Justificación del Proyecto
Organizar viajes grupales puede ser caótico, especialmente cuando hay que gestionar itinerarios, actividades y comunicación entre los participantes.  
El **Organizador de Viajes en Grupo** aborda este problema proporcionando una plataforma centralizada que facilita:
1. La gestión colaborativa de viajes.
2. La planificación de actividades con votaciones y sugerencias.
3. Un sistema seguro y confiable para proteger los datos de los usuarios.

Además, esta API REST cumple con las mejores prácticas en diseño y seguridad, lo que la hace adecuada para su integración en aplicaciones web o móviles.

---

## Tecnologías Utilizadas
- **Spring Boot**: Framework para construir aplicaciones Java modernas.
- **JWT (JSON Web Tokens)**: Autenticación y autorización.
- **Base de Datos Relacional (MySQL)**: Gestión de entidades y relaciones.
- **Insomnia**: Para probar los endpoints.

---

## Diseño de Tablas

### **a. Usuarios**
| Campo       | Tipo              | Descripción                              |
|-------------|-------------------|------------------------------------------|
| `id`        | PK, autogenerado  | Identificador único del usuario.         |
| `username`  | String, único     | Nombre de usuario.                       |
| `password`  | String            | Contraseña hasheada.                     |
| `roles`     | String            | Roles: "user" o "admin".                 |

### **b. Viajes**
| Campo             | Tipo              | Descripción                              |
|-------------------|-------------------|------------------------------------------|
| `id`              | PK, autogenerado  | Identificador único del viaje.           |
| `nombre`          | String            | Nombre del viaje.                        |
| `descripcion`     | String            | Descripción del viaje.                   |
| `fecha_inicio`    | Date              | Fecha estimada de inicio del viaje.      |
| `fecha_fin`       | Date              | Fecha estimada de conclusión del viaje.  |
| `organizador_id`  | FK a Usuarios     | Usuario organizador del viaje.           |
| `participantes`   | List<Usuario>     | Lista de participantes del viaje.        |
| `actividades`     | List<Actividad>   | Lista de actividades planificadas.       |

### **c. Actividades**
| Campo         | Tipo             | Descripción                           |
|---------------|------------------|---------------------------------------|
| `id`          | PK, autogenerado | Identificador único de la actividad.  |
| `nombre`      | String           | Nombre de la actividad.               |
| `descripcion` | String           | Descripción de la actividad.          |
| `fecha_hora`  | DateTime         | Fecha y hora de la actividad.         |
| `ubicacion`   | String           | Ubicación donde se llevará a cabo.    |

---

## Endpoints

### **Usuarios**
- `POST /register` - Registrar un usuario.
- `POST /login` - Autenticar usuario y devolver JWT.
- `GET /usuarios` - Obtener todos los usuarios (solo administrador)

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
1. Los usuarios solo pueden editar o eliminar los viajes que ellos mismos han creado.
2. Los usuarios solo pueden ver los viajes en los que estén participando.
3. Solo el organizador de un viaje puede añadir o eliminar participantes.
3. Cada usuario puede registrarse, autenticarse y gestionar su propia información.
4. Los JWT se generan y verifican en cada petición para garantizar la autenticación y autorización.

---

## Excepciones y Códigos de Estado
| Código | Descripción                                  |
|--------|----------------------------------------------|
| 400    | `Bad Request`: Datos de entrada inválidos.   |
| 401    | `Unauthorized`: Usuario no autenticado.      |
| 403    | `Forbidden`: Usuario sin permisos.           |
| 404    | `Not Found`: Entidad no encontrada.          |
| 500    | `Internal Server Error`: Error del servidor. |

---

## Seguridad
1. Contraseñas almacenadas con **BCrypt**.
2. Autenticación y autorización mediante **JWT**.
3. Roles y permisos:
  - **user**: Puede gestionar sus propios viajes y actividades.
  - **admin** (opcional): Tiene acceso completo a los datos del sistema.

