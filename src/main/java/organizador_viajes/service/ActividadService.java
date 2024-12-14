package organizador_viajes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import organizador_viajes.dto.ActividadDTO;
import organizador_viajes.model.Actividad;
import organizador_viajes.model.Viaje;
import organizador_viajes.repository.ActividadRepository;
import organizador_viajes.repository.ViajeRepository;
import organizador_viajes.utils.ActividadMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ActividadMapper actividadMapper;

    @PreAuthorize("@viajeService.isParticipant(#idViaje)")
    public List<ActividadDTO> obtenerActividadesPorViaje(Long viajeId) {
        List<Actividad> actividades = actividadRepository.findByViajeId(viajeId);
        return actividades.stream().map(actividadMapper::actToDto).collect(Collectors.toList());
    }

    @PreAuthorize("@viajeService.isParticipant(#idViaje)")
    public ActividadDTO crearActividad(Long viajeId, ActividadDTO actividadDTO) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        Actividad actividad = actividadMapper.actToEntity(actividadDTO);
        actividad.setViaje(viaje);

        Actividad actividadGuardada = actividadRepository.save(actividad);
        return actividadMapper.actToDto(actividadGuardada);
    }

    @PreAuthorize("@viajeService.isParticipant(#idViaje)")
    public ActividadDTO editarActividad(Long actividadId, Long viajeId, ActividadDTO actividadDTO) {
        Actividad actividad = actividadRepository.findByIdAndViajeId(actividadId, viajeId);
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no encontrada para el viaje especificado");
        }

        actividad.setNombre(actividadDTO.getNombre());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFechaHora(actividadDTO.getFechaHora());
        actividad.setUbicacion(actividadDTO.getUbicacion());

        Actividad actividadActualizada = actividadRepository.save(actividad);
        return actividadMapper.actToDto(actividadActualizada);
    }

    @PreAuthorize("@viajeService.isParticipant(#idViaje)")
    public void eliminarActividad(Long actividadId, Long viajeId) {
        Actividad actividad = actividadRepository.findByIdAndViajeId(actividadId, viajeId);
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no encontrada para el viaje especificado");
        }
        actividadRepository.delete(actividad);
    }
}

