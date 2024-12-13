package organizador_viajes.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organizador_viajes.dto.ViajeDTO;
import organizador_viajes.model.Usuario;
import organizador_viajes.model.Viaje;

import java.util.List;
import java.util.stream.Collectors;


// Clase ViajeMapper
@Component
public class ViajeMapper {

    @Autowired
    private ActividadMapper actividadMapper;


    public ViajeDTO viajeToDto(Viaje viaje) {
        if (viaje == null) {
            return null;
        }
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setNombre(viaje.getNombre());
        dto.setDescripcion(viaje.getDescripcion());
        dto.setFechaInicio(viaje.getFechaInicio());
        dto.setFechaFin(viaje.getFechaFin());
        dto.setOrganizadorId(viaje.getOrganizador().getId());
        dto.setParticipantesIds(
                viaje.getParticipantes().stream().map(Usuario::getId).collect(Collectors.toList())
        );
        dto.setActividades(
                viaje.getActividades().stream().map(actividadMapper::actToDto).collect(Collectors.toList())
        );
        return dto;
    }

    public Viaje viajeToEntity(ViajeDTO dto, Usuario organizador, List<Usuario> participantes) {
        if (dto == null) {
            return null;
        }
        Viaje viaje = new Viaje();
        viaje.setId(dto.getId());
        viaje.setNombre(dto.getNombre());
        viaje.setDescripcion(dto.getDescripcion());
        viaje.setFechaInicio(dto.getFechaInicio());
        viaje.setFechaFin(dto.getFechaFin());
        viaje.setOrganizador(organizador);
        viaje.setParticipantes(participantes);
        viaje.setActividades(
                dto.getActividades().stream().map(actividadMapper::actToEntity).collect(Collectors.toList())
        );
        return viaje;
    }
}
