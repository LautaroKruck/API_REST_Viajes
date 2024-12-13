package organizador_viajes.controller;

import organizador_viajes.dto.ViajeDTO;
import organizador_viajes.error.exception.BadRequestException;
import organizador_viajes.error.exception.NotFoundException;
import organizador_viajes.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @PostMapping
    public ResponseEntity<ViajeDTO> crearViaje(@RequestBody ViajeDTO viajeDTO) {
        if (viajeDTO == null) {
            throw new BadRequestException("El objeto Viaje no puede ser null.");
        }
        ViajeDTO creado = viajeService.crearViaje(viajeDTO);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeDTO> obtenerViajePorId(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null.");
        }
        ViajeDTO viaje = viajeService.obtenerViajesPorUsuario(id).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Viaje con ID " + id + " no encontrado"));
        return ResponseEntity.ok(viaje);
    }

    @GetMapping
    public ResponseEntity<List<ViajeDTO>> obtenerTodosLosViajes(@RequestParam Long usuarioId) {
        if (usuarioId == null) {
            throw new BadRequestException("El ID de usuario no puede ser null.");
        }
        List<ViajeDTO> viajes = viajeService.obtenerViajesPorUsuario(usuarioId);
        return ResponseEntity.ok(viajes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeDTO> actualizarViaje(@PathVariable Long id, @RequestBody ViajeDTO viajeDTO) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null.");
        }
        if (viajeDTO == null) {
            throw new BadRequestException("El objeto Viaje no puede ser null.");
        }
        ViajeDTO actualizado = viajeService.editarViaje(id, viajeDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null.");
        }
        viajeService.eliminarViaje(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unirse")
    public ResponseEntity<Void> unirseViaje(@PathVariable Long id, @RequestParam String password, @RequestParam Long usuarioId) {
        viajeService.unirseViaje(id, password, usuarioId);
        return ResponseEntity.ok().build();
    }

}
