package organizador_viajes.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ruta_protegida")
public class RutaProtegidaController {


    @GetMapping("/")
    public String rutaProtegida(Authentication authentication) {

        return "Hola "+authentication.getName()+" esto es una ruta protegida";
    }
}
