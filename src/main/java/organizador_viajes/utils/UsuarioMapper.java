package organizador_viajes.utils;

import org.springframework.stereotype.Component;
import organizador_viajes.dto.UsuarioDTO;
import organizador_viajes.model.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioDTO entityToDto (Usuario u) {

        String[] roles = u.getRoles().split(",");
        return new UsuarioDTO(
                u.getUsername(),
                u.getPassword(),
                roles
        );
    }

}
