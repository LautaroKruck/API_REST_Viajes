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
                        // Rutas públicas
                        .requestMatchers("/usuarios/register", "/usuarios/login").permitAll()

                        // Rutas protegidas - SEGUROS
                        .requestMatchers(HttpMethod.GET, "/seguros").hasRole("ADMIN") // Solo ADMIN puede listar seguros
                        .requestMatchers(HttpMethod.GET, "/seguros/{idSeguro}").authenticated() // Usuarios autenticados pueden ver detalles
                        .requestMatchers(HttpMethod.POST, "/seguros").hasRole("ADMIN") // Solo ADMIN puede crear seguros
                        .requestMatchers(HttpMethod.PUT, "/seguros/**").hasRole("ADMIN") // Solo ADMIN puede editar seguros
                        .requestMatchers(HttpMethod.DELETE, "/seguros/**").hasRole("ADMIN") // Solo ADMIN puede eliminar seguros

                        // Rutas protegidas - ASISTENCIAS MÉDICAS
                        .requestMatchers(HttpMethod.GET, "/asistencias").hasRole("ADMIN") // Solo ADMIN puede listar asistencias
                        .requestMatchers(HttpMethod.GET, "/asistencias/{idAsistenciaMedica}").authenticated() // Usuarios autenticados pueden ver detalles
                        .requestMatchers(HttpMethod.POST, "/seguros/{idSeguro}/asistencias").hasRole("ADMIN") // Solo ADMIN puede crear asistencias
                        .requestMatchers(HttpMethod.PUT, "/asistencias/**").hasRole("ADMIN") // Solo ADMIN puede editar asistencias
                        .requestMatchers(HttpMethod.DELETE, "/asistencias/**").hasRole("ADMIN") // Solo ADMIN puede eliminar asistencias

                        // Rutas restantes
                        .anyRequest().authenticated() // El resto de endpoints requieren autenticación
                        .anyRequest().authenticated() // El resto de endpoints requieren autenticación
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

