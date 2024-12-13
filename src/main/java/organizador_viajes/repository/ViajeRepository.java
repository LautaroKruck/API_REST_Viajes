// ViajeRepository con métodos adicionales
package organizador_viajes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import organizador_viajes.model.Viaje;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    // Buscar viajes en los que un usuario participa
    List<Viaje> findByParticipantes_Id(Long participanteId);

    // Buscar un viaje por su ID
    Optional<Viaje> findById(Long idViaje);
}