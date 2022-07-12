package pk.first.application.pkspringapplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pk.first.application.pkspringapplication.security.AuthEntryPointJwt;
import pk.first.application.pkspringapplication.security.AuthTokenFilter;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/auth/**",
                        "/webjars/**",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/api/alien/**").permitAll()
                .anyRequest().authenticated().and()
                //When an exception is caught, the JSON string is returned directly to the front end
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON.toString());

                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setContentType(MediaType.APPLICATION_JSON.toString());

                })
                .and();
                http.csrf().disable();
                http.headers().frameOptions().sameOrigin();
//                http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
                return http.build();
            }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
