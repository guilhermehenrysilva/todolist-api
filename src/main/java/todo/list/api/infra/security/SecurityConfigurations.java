package todo.list.api.infra.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import todo.list.api.infra.security.filter.SecurityExceptionHandlerFilter;
import todo.list.api.infra.security.filter.SecurityFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private SecurityExceptionHandlerFilter securityExceptionHandlerFilter;

    @Getter
    private final List<AntPathRequestMatcher> allowedRequestMatchers = Arrays.asList(
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/login/**"),
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/sign-in"),
            new AntPathRequestMatcher("/sign-up"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/swagger-ui/**")
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(allowedRequestMatchers.toArray(new AntPathRequestMatcher[0])).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(securityExceptionHandlerFilter, SecurityFilter.class)
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults());

        return http.getOrBuild();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
