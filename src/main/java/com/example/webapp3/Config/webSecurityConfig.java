    package com.example.webapp3.Config;

    import com.example.webapp3.Models.Role;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.NoOpPasswordEncoder;
    import org.springframework.security.web.DefaultSecurityFilterChain;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
    import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
    import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

    import javax.sql.DataSource;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(securedEnabled = true)

    public class webSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

        @Autowired
        private DataSource dataSource;

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder(){
            return new BCryptPasswordEncoder(12);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler("/");
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((auth) -> auth
                            .requestMatchers(new AntPathRequestMatcher("/", "/resources/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/registration")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/user")).hasAuthority("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/something")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/test/**")).hasAnyAuthority("USER", "ADMIN")
                            .anyRequest().authenticated())
                    .formLogin((form) -> form
                            .loginPage("/login")
                            //.successHandler(successHandler)
                            .permitAll())
                    .logout((logout) -> logout
                            .logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/"))
                            .permitAll());
            return http.build();
        }

        @Autowired
        protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
           auth.jdbcAuthentication()
                   .dataSource(dataSource)
                   //.passwordEncoder(NoOpPasswordEncoder.getInstance())
                   .passwordEncoder(bCryptPasswordEncoder())
                   .usersByUsernameQuery("select username, password, active from usr where username=?")
                   .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?")
                   ;

        }
    }
