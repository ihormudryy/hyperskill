package engine.configs;

import engine.user.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableConfigurationProperties
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public void configure(AuthenticationManagerBuilder auth)
        throws Exception {

        auth.userDetailsService(userDetailServiceImpl)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
            .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/register").permitAll()
                .antMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
            .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

}
