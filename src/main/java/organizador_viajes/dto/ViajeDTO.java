package organizador_viajes.dto;


import java.time.LocalDate;
import java.util.List;

// DTO de Viaje
public class ViajeDTO {

    private Long id;
    private String nombre;
    private String password;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long organizadorId;
    private List<Long> participantesIds;
    private List<ActividadDTO> actividades;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(Long organizadorId) {
        this.organizadorId = organizadorId;
    }

    public List<Long> getParticipantesIds() {
        return participantesIds;
    }

    public void setParticipantesIds(List<Long> participantesIds) {
        this.participantesIds = participantesIds;
    }

    public List<ActividadDTO> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadDTO> actividades) {
        this.actividades = actividades;
    }
}
