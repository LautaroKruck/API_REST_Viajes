// ViajeService actualizado
package organizador_viajes.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    // Obtener todos los viajes de un usuario
    public List<ViajeDTO> obtenerViajesPorUsuario(Long usuarioId) {
        List<Viaje> viajes = viajeRepository.findByParticipantes_Id(usuarioId);
        return viajes.stream().map(viajeMapper::viajeToDto).collect(Collectors.toList());
    }

    // Crear un nuevo viaje
    public ViajeDTO crearViaje(ViajeDTO viajeDTO) {
        // Buscar el organizador por ID
        Usuario organizador = usuarioRepository.findById(viajeDTO.getOrganizadorId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario organizador no encontrado"));

        // Convertir el DTO de viaje a la entidad
        Viaje viaje = viajeMapper.viajeToEntity(viajeDTO, organizador, null);

        // 'viaje' tiene un campo llamado 'password'
        // Si existe este campo, lo hasheamos antes de guardarlo
        if (viaje.getPassword() != null) {
            String passHashed = passwordEncoder.encode(viaje.getPassword());
            viaje.setPassword(passHashed);  // Reemplazamos el valor original con la versión hasheada
        }

        // Guardamos el viaje en la base de datos
        Viaje viajeGuardado = viajeRepository.save(viaje);

        // Retornamos el DTO con los datos del viaje guardado
        return viajeMapper.viajeToDto(viajeGuardado);
    }

    // Editar un viaje existente
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

    // Eliminar un viaje
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

    public boolean isParticipant(Long idViaje) {
        Long currentUserId = usuarioService.getCurrentUserId();
        return viajeRepository.findById(idViaje)
                .map(viaje -> viaje.getParticipantes().stream()
                        .anyMatch(participante -> participante.getId().equals(currentUserId)))
                .orElse(false);
    }

    public boolean isOrganizer(Long idViaje) {
        Long currentUserId = usuarioService.getCurrentUserId();
        return viajeRepository.findById(idViaje)
                .map(viaje -> viaje.getOrganizador().getId().equals(currentUserId))
                .orElse(false);
    }

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
