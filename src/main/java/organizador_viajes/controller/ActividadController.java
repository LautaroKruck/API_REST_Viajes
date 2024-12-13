package organizador_viajes.controller;


import organizador_viajes.dto.ActividadDTO;
import organizador_viajes.error.exception.BadRequestException;
import organizador_viajes.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viajes/{viajeId}/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> obtenerActividades(@PathVariable Long viajeId) {
        if (viajeId == null) {
            throw new BadRequestException("El ID del viaje no puede ser null.");
        }
        List<ActividadDTO> actividades = actividadService.obtenerActividadesPorViaje(viajeId);
        return ResponseEntity.ok(actividades);
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> crearActividad(@PathVariable Long viajeId, @RequestBody ActividadDTO actividadDTO) {
        if (actividadDTO == null) {
            throw new BadRequestException("El objeto Actividad no puede ser null.");
        }
        ActividadDTO creada = actividadService.crearActividad(viajeId, actividadDTO);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/{actividadId}")
    public ResponseEntity<ActividadDTO> actualizarActividad(@PathVariable Long actividadId, @PathVariable Long viajeId, @RequestBody ActividadDTO actividadDTO) {
        if (actividadDTO == null) {
            throw new BadRequestException("El objeto Actividad no puede ser null.");
        }
        ActividadDTO actualizada = actividadService.editarActividad(actividadId, viajeId, actividadDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{actividadId}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long actividadId, @PathVariable Long viajeId) {
        actividadService.eliminarActividad(actividadId, viajeId);
        return ResponseEntity.noContent().build();
    }
}
