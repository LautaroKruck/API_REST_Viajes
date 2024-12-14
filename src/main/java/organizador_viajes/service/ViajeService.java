// ViajeService actualizado
package organizador_viajes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import organizador_viajes.dto.ViajeDTO;
import organizador_viajes.error.exception.BadRequestException;
import organizador_viajes.error.exception.NotFoundException;
import organizador_viajes.model.Usuario;
import organizador_viajes.model.Viaje;
import organizador_viajes.repository.UsuarioRepository;
import organizador_viajes.repository.ViajeRepository;
import organizador_viajes.utils.ViajeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ViajeMapper viajeMapper;

    // Método para obtener todos los viajes de un usuario
    public List<ViajeDTO> obtenerViajesPorUsuario(Long usuarioId) {
        List<Viaje> viajes = viajeRepository.findByParticipantes_Id(usuarioId);
        return viajes.stream().map(viajeMapper::viajeToDto).collect(Collectors.toList());
    }

    // Crear un nuevo viaje
    public ViajeDTO crearViaje(ViajeDTO viajeDTO) {
        Usuario organizador = usuarioRepository.findById(viajeDTO.getOrganizadorId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario organizador no encontrado"));

        Viaje viaje = viajeMapper.viajeToEntity(viajeDTO, organizador, null);

        viaje.getParticipantes().add(organizador);

        if (viaje.getPassword() != null) {
            String passHashed = passwordEncoder.encode(viaje.getPassword());
            viaje.setPassword(passHashed);
        }

        Viaje viajeGuardado = viajeRepository.save(viaje);
        return viajeMapper.viajeToDto(viajeGuardado);
    }

    @PreAuthorize("@viajeService.isOrganizer(#idViaje)")
    public ViajeDTO editarViaje(Long viajeId, ViajeDTO viajeDTO) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        viaje.setNombre(viajeDTO.getNombre());
        viaje.setDescripcion(viajeDTO.getDescripcion());
        viaje.setFechaInicio(viajeDTO.getFechaInicio());
        viaje.setFechaFin(viajeDTO.getFechaFin());

        Viaje viajeActualizado = viajeRepository.save(viaje);
        return viajeMapper.viajeToDto(viajeActualizado);
    }

    @PreAuthorize("@viajeService.isOrganizer(#idViaje)")
    public void eliminarViaje(Long viajeId) {
        if (!viajeRepository.existsById(viajeId)) {
            throw new IllegalArgumentException("Viaje no encontrado");
        }
        viajeRepository.deleteById(viajeId);
    }

    // Unirse a un viaje
    public void unirseViaje(Long viajeId, String password, Long usuarioId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NotFoundException("Viaje no encontrado"));

        if (!viaje.getPassword().equals(password)) {
            throw new BadRequestException("Contraseña incorrecta");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (viaje.getParticipantes().contains(usuario)) {
            throw new BadRequestException("El usuario ya está unido al viaje");
        }

        viaje.getParticipantes().add(usuario);
        viajeRepository.save(viaje);
    }

    @PreAuthorize("@viajeService.isParticipant(#idViaje)")
    public boolean isParticipant(Long idViaje) {
        Long currentUserId = usuarioService.getCurrentUserId();
        return viajeRepository.findById(idViaje)
                .map(viaje -> viaje.getParticipantes().stream()
                        .anyMatch(participante -> participante.getId().equals(currentUserId)))
                .orElse(false);
    }

    @PreAuthorize("@viajeService.isOrganizer(#idViaje)")
    public boolean isOrganizer(Long idViaje) {
        Long currentUserId = usuarioService.getCurrentUserId();
        return viajeRepository.findById(idViaje)
                .map(viaje -> viaje.getOrganizador().getId().equals(currentUserId))
                .orElse(false);
    }

    @PreAuthorize("@viajeService.isOrganizer(#idViaje)")
    public boolean canRemoveParticipant(Long idViaje, Long idUsuario) {
        Long currentUserId = usuarioService.getCurrentUserId();
        return viajeRepository.findById(idViaje).map(viaje -> {
            boolean isOrganizer = viaje.getOrganizador().getId().equals(currentUserId);
            boolean isRemovingSelf = currentUserId.equals(idUsuario);
            boolean notOnlyParticipant = viaje.getParticipantes().size() > 1;
            return isOrganizer || (isRemovingSelf && notOnlyParticipant);
        }).orElse(false);
    }
}
