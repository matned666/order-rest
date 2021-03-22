package eu.mrndesign.matned.metalserwisproductionrest.config;

import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    public WebSecurity(DataSource dataSource,
                                 PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/**")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name(), UserRole.Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/users/*", "/users")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name(), UserRole.Role.MANAGER.name(),  UserRole.Role.PUBLISHER.name())
                .antMatchers(HttpMethod.POST, "/users/*/password/**")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name(), UserRole.Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/users/*/add-role/**", "/users/*/take-away-role/**")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name())
                .antMatchers(HttpMethod.GET, "/orders/**")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name(), UserRole.Role.MANAGER.name(),  UserRole.Role.PUBLISHER.name(), UserRole.Role.USER.name())
                .antMatchers(HttpMethod.POST, "/orders/**")
                .hasAnyRole(UserRole.Role.ADMIN.name(), UserRole.Role.CEO.name(), UserRole.Role.MANAGER.name(),  UserRole.Role.PUBLISHER.name())
                .antMatchers("/**")
                .not().hasRole(UserRole.Role.BANNED.name())
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .formLogin().disable();
        http.httpBasic();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("select u.LOGIN, u.PASSWORD, 1 from USER_ENTITY u where u.LOGIN = ?")
                .authoritiesByUsernameQuery(
                        "select u.LOGIN, r.ROLE_NAME from USER_ENTITY u " +
                                "join USER_ENTITY_ROLES ur on u.ID = ur.USER_ID " +
                                "join USER_ROLE r on ur.ROLES_ID = r.ID " +
                                "where u.LOGIN = ?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addAllowedHeader("Content-Type");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
