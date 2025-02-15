package organizador_viajes.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Modelo de Viaje
@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String password;

    private String descripcion;

    @Column(name = "fecha_inicio",nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin",nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    @ManyToMany
    @JoinTable(
            name = "viaje_participantes",
            joinColumns = @JoinColumn(name = "viaje_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> participantes = new ArrayList<>();

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actividad> actividades = new ArrayList<>();

    public Viaje(Long id, String nombre, String password, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Usuario organizador, List<Usuario> participantes, List<Actividad> actividades) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.organizador = organizador;
        this.participantes = participantes != null ? participantes : new ArrayList<>(); // Asegura que no sea null
        this.actividades = actividades != null ? actividades : new ArrayList<>(); // Asegura que no sea null
    }

    public Viaje(String nombre, String password, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Usuario organizador, List<Usuario> participantes, List<Actividad> actividades) {
        this.nombre = nombre;
        this.password = password;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.organizador = organizador;
        this.participantes = participantes != null ? participantes : new ArrayList<>(); // Asegura que no sea null
        this.actividades = actividades != null ? actividades : new ArrayList<>(); // Asegura que no sea null
    }

    public Viaje(String nombre, String password, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Usuario organizador) {
        this.nombre = nombre;
        this.password = password;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.organizador = organizador;
    }

    public Viaje() {
    }

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

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }

    public List<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Usuario> participantes) {
        this.participantes = participantes;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}
