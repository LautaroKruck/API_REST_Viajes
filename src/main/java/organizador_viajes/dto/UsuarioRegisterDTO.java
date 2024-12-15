package organizador_viajes.dto;

public class UsuarioRegisterDTO {

    private String username;
    private String password;
    private Integer edad;
    private String roles;

    public UsuarioRegisterDTO(String username, String password,Integer edad, String roles) {
        this.username = username;
        this.password = password;
        this.edad = edad;
        this.roles = roles;
    }

    public UsuarioRegisterDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
