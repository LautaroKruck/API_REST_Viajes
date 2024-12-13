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
| Campo      | Tipo             | Descripción                      |
|------------|------------------|----------------------------------|
| `id`       | PK, autogenerado | Identificador único del usuario. |
| `username` | String, único    | Nombre de usuario.               |
| `password` | String           | Contraseña hasheada.             |
| `edad`     | Integer          | Edad del usuario.                |
| `roles`    | String           | Roles: "user" o "admin".         |

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

# Lógica de Negocio

## Usuarios

### Requisitos de Creación de Usuario
- El `username` y la `contraseña` son obligatorios.
    - Si alguno de estos campos está vacío o no se proporciona, se devolverá un error con el código **400 Bad Request**.
- El `username` debe ser único.
    - No se podrá registrar un usuario con un nombre que ya esté en uso.

### Restricción de Edad
- Un usuario debe tener una edad entre **10 y 100 años**.
    - Si se proporciona una edad fuera de este rango, se devolverá un error **400 Bad Request**.

### Edición de Usuario
- Los usuarios solo pueden editar su propia información.
    - Intentar editar los datos de otro usuario resultará en un error **403 Forbidden**.


## Viajes

### Requisitos para la Creación de Viaje
- Un viaje debe tener un `nombre` y un `organizador` (usuario responsable).
    - Si alguno de estos campos falta, se devolverá un error **400 Bad Request**.
- La `fecha de inicio` tiene que ser anterior a la `fecha de finalización`.

### Participación en Viajes
- Un usuario no puede unirse a un viaje que ya haya finalizado.
    - Si intenta unirse después de la fecha de finalización, se devolverá un error **400 Bad Request**.

### Permisos de Modificación del Viaje
- Un usuario solo puede editar o eliminar los viajes que ha creado.
    - Si intenta modificar o eliminar un viaje que no le pertenece, se devolverá un error **403 Forbidden**.

### Visualización de Viajes
- Los usuarios solo pueden ver los viajes en los que están participando.
    - Intentar acceder a viajes en los que no están involucrados resultará en un error **403 Forbidden**.

### Agregar Actividades al Viaje
- Todos los usuarios pueden agregar actividades a un viaje, independientemente de su rol.
    - Las actividades solo se asociarán al viaje específico en el que el usuario participe.

### Eliminación de Viajes
- Solo el organizador del viaje puede eliminarlo.
    - Intentar eliminar un viaje sin ser el organizador resultará en un error **403 Forbidden**.

### Gestión de Participantes
- Solo el organizador del viaje puede eliminar a otros usuarios del viaje.
    - Intentar eliminar a un participante sin ser el organizador resultará en un error **403 Forbidden**.
- Un usuario puede eliminarse a sí mismo del viaje en cualquier momento, siempre y cuando:
    - No sea el único participante.
    - No sea el organizador.


## Actividades

### Restricción de Fechas de Actividades
- No se puede crear dos actividades con la misma fecha y hora dentro del mismo viaje.
    - Si se intenta crear una actividad con una fecha que ya está ocupada, se devolverá un error **400 Bad Request**.
- No puede haber actividades con una fecha y hora fuera del rango del viaje.
    - Si se intenta crear una actividad con una fecha fuera del rango, se devolverá un error **400 Bad Request**.

### Gestión de Actividades
- Las actividades pueden ser eliminadas únicamente por el organizador del viaje.
    - Si otro participante intenta eliminar una actividad, se devolverá un error **403 Forbidden**.


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

