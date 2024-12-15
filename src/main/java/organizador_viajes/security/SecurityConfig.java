package organizador_viajes.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RsaKeyProperties rsaKeys;

    /**
     * BEAN QUE ESTABLECE EL SECURITY FILTER CHAIN
     * Método que establece una serie de filtros de seguridad para nuestra API
     * ¿Para qué sirve este método?
     * Este método sirve para establecer los filtros de seguridad que las peticiones deberán cumplir antes de
     * llegar al endpoint al que se dirijan
     *
     * request ------> filtro1 -> filtro2 -> filtro3 ... -> endpoint
     *
     * Por así decirlo, al cargar la aplicación, Spring Security coge lo que tengamos aquí definido y lo "pone"
     * delante de nuestra app a modo de filtros de seguridad. Esto lo hace automáticamente.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF (no lo trataremos en este ciclo)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas sin autenticación
                        .requestMatchers("/usuarios/register", "/usuarios/login").permitAll()

                        // Rutas relacionadas con USUARIOS
                        .requestMatchers(HttpMethod.GET, "/usuarios/{idUsuario}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/{idUsuario}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{idUsuario}").hasRole("ADMIN")

                        // Rutas relacionadas con VIAJES
                        .requestMatchers(HttpMethod.GET, "/viajes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/viajes/").authenticated()
                        .requestMatchers(HttpMethod.POST, "/viajes").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/viajes/").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/viajes/").authenticated()

                        // Rutas relacionadas con ACTIVIDADES
                        .requestMatchers(HttpMethod.GET, "/viajes/{viajeId}/actividades").authenticated()
                        .requestMatchers(HttpMethod.GET, "/actividades/").authenticated()
                        .requestMatchers(HttpMethod.POST, "/viajes/{idViaje}/actividades").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/actividades/").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/actividades/").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())) // Establecemos el que el control de autenticación se realice por JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Método que carga en el Spring ApplicationContext un Bean de tipo PasswordEncoder
     * ¿Por qué este método? ¿Para qué sirve este método?
     * Este método está aquí para que podamos inyectar donde nos plazca objetos de tipo PasswordEncoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Método que carga en el Spring ApplicationContext un Bean de tipo AuthenticationManager
     * ¿Por qué este método? ¿Para qué sirve este método?
     * Este método está aquí para que podamos inyectar donde nos plazca objetos de tipo AuthenticationManager
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * JWTDECODER decodifica un token
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    /**
     * JWTENCODER codifica un token
     * @return
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}