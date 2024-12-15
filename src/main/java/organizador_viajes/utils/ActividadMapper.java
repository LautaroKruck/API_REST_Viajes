package organizador_viajes.utils;

import org.springframework.stereotype.Component;
import organizador_viajes.dto.ActividadDTO;
import organizador_viajes.model.Actividad;

// Clase ActividadMapper
@Component
public class ActividadMapper {

    public ActividadDTO actToDto(Actividad actividad) {
        if (actividad == null) {
            return null;
        }
        ActividadDTO dto = new ActividadDTO();
        dto.setNombre(actividad.getNombre());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setFechaHora(actividad.getFechaHora());
        dto.setUbicacion(actividad.getUbicacion());
        return dto;
    }

    public Actividad actToEntity(ActividadDTO dto) {
        if (dto == null) {
            return null;
        }
        Actividad actividad = new Actividad();
        actividad.setNombre(dto.getNombre());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setFechaHora(dto.getFechaHora());
        actividad.setUbicacion(dto.getUbicacion());
        return actividad;
    }
}
