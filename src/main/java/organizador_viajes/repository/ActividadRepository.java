package organizador_viajes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import organizador_viajes.model.Actividad;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Encuentra actividades por viaje
    List<Actividad> findByViajeId(Long viajeId);

    // Encuentra actividad por ID y viaje
    Actividad findByIdAndViajeId(Long actividadId, Long viajeId);
}

