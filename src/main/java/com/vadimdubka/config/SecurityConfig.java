package com.vadimdubka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

/**
 * The following listing shows the simplest possible Java configuration for Spring Security.
 * <p>
 * The @EnableWebSecurity annotation enables web security.It is useless on its own, however.
 * Spring Security must be configured in a bean that implements WebSecurityConfigurer or (for convenience)
 * extends WebSecurityConfigurerAdapter. Any bean in the Spring application context that implements WebSecurityConfigurer
 * can contribute to Spring Security configuration, but it’s often most convenient for the configuration class
 * to extend WebSecurityConfigurerAdapter.
 * <p>
 * The @EnableWebSecurity is generally useful for enabling security in any web application.
 * But if you happen to be developing a Spring MVC application, you should consider using @EnableWebMvcSecurity instead.
 * <p>
 * Among other things, the @EnableWebMvcSecurity annotation configures a Spring MVC argument resolver
 * so that handler methods can receive the authenticated user’s principal (or username) via @AuthenticationPrincipal-annotated parameters.
 * It also configures a bean that automatically adds a hidden cross-site request forgery (CSRF) token field on forms using Spring’s form-binding tag library.
 * <p>
 * You can configure web security by overriding WebSecurityConfigurerAdapter’s three configure() methods
 * and setting behavior on the parameter passed in.
 * <p>
 * You’re going to need to add a bit more configuration to bend Spring Security to fit your application’s needs.
 * Specifically, you’ll need to:
 * - Configure a user store {@link SecurityConfig#configure(AuthenticationManagerBuilder)}
 * - Specify which requests should and should not require authentication, as well as what authorities they require
 * {@link SecurityConfig#configure(HttpSecurity)}
 * - Provide a custom login screen to replace the plain default login screen
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    /*Override to configure Spring Security’s fil- ter chain.*/
    /*protected void configure(WebSecurity webSecurity) {}*/
    
    /**
     * Override to configure how requests are secured by interceptors.
     * Specifies how HTTP requests should be secured and what options a client has for authenticating the user
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            /* authentication via a form-based predefined login page*/
            .formLogin().loginPage("/spitter/login")
            /*9.4.4	Logging out*/
            .and()
            .logout()
            .logoutSuccessUrl("/spitter/login")
            .logoutUrl("/signout")
            /*9.4.2	Enabling HTTP Basic authentication*/
            .and()
            .httpBasic().realmName("Spittr")
            /*9.4.3	Enabling remember-me functionality*/
            .and()
            .rememberMe().tokenRepository(new InMemoryTokenRepositoryImpl())
            .tokenValiditySeconds(2419200).key("spittrKey")
            /*To ensure that the registration form is sent over HTTPS.
             * Any time a request comes in for /spitter/form, Spring Security will see
             * that it requires a secure channel (per the call to requiresSecure())
             * and automatically redirect the request to go over HTTPS.
             * */
            /*.and()
            .requiresChannel()
            .antMatchers("/spitter/register").requiresSecure()*/
            /*If a request for / comes in over HTTPS, Spring Security will redirect the request to flow over the insecure HTTP.*/
            /*.antMatchers("/").requiresInsecure()*/
            
            .and()
            /*9.3.2	Enforcing channel security over HTTPS*/
            /*defines what HTTP requests coming into the application must be authenticated*/
            /*configure request-level security details*/
            .authorizeRequests()
            /*requests whose path is /spitters/me should be authenticated*/
            .antMatchers("/spitter/me").hasRole("USER")
            .antMatchers(HttpMethod.POST, "/spittles").authenticated()
            .antMatchers(HttpMethod.GET, "/spittles").authenticated()
            /*all other requests should be permitted, not requiring authentication or any authorities*/
            .anyRequest().permitAll();
        
            
            /*The path given to antMatchers() supports Ant-style wildcarding.
            Although we’re not using it here, you could specify a path with a wildcard like this:
            .antMatchers("/spitters/**", "/spittles/mine").authenticated();
            
            Whereas the antMatchers() method works with paths that may contain Ant-style wildcards,
            there’s also a regexMatchers() method that accepts regular expressions to define request paths. For example, the following snippet uses a regular expression that’s equivalent to /spitters/** (Ant-style):
            .regexMatchers("/spitters/.*").authenticated();
            
            Configuration methods to define how a path is to be secured:
            - access(String) Allows access if the given SpEL expression evaluates to true
            - anonymous() Allows access to anonymous users
            - authenticated() Allows access to authenticated users
            - denyAll() Denies access unconditionally
            - fullyAuthenticated() Allows access if the user is fully authenticated (not remembered)
            - hasAnyAuthority(String…) Allows access if the user has any of the given authorities
            - hasAnyRole(String…) Allows access if the user has any of the given roles
            - hasAuthority(String) Allows access if the user has the given authority
            - hasIpAddress(String) Allows access if the request comes from the given IP address
            - hasRole(String) Allows access if the user has the given role
            - not() Negates the effect of any of the other access methods
            - permitAll() Allows access unconditionally
            - rememberMe() Allows access for users who are authenticated via remember-me
            
            You can chain as many calls to antMatchers(), regexMatchers(), and anyRequest() as you need
            to fully establish the security rules around your web application.
            You should know, however, that they’ll be applied in the order given.
            For that reason, it’s important to configure the most specific request path patterns first and the least spe
            last. If not, then the least specific paths will trump the more specific ones.
            
            */
        
        /*CSRF protection disabled*/
        http.csrf().disable();
    }
    
    /*Override to configure user-details services.
     * Defines user store backing the authentication process.
     * With no user store, there are effectively no users. Therefore, all requests require authentication,
     * but there’s nobody who can log in.
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            /*calling inMemoryAuthentication() will enable an in-memory user store.
             * But you’ll also need some users in there, or else it’s as if you have no user store at all.*/
            .inMemoryAuthentication()
            /* Add a new user to the in-memory user store.
             * The parameter given is the username.
             * Method withUser() returns a UserDetailsManagerConfigurer.UserDetailsBuilder,
             * which has several methods for further configuration of the user,
             * including password() to set the user’s password and roles() to give the user one or more role authorities.
             * */
            .withUser("user").password("password").roles("USER").and()
            .withUser("admin").password("password").roles("USER", "ADMIN");
            
        /* Here you’re adding two users, “user” and “admin”, both with “password” for a password.
        The “user” user has the USER role, while the “admin” user has both USER and ADMIN roles.
        As you can see, the and() method is used to chain together multiple user configurations.
         */
        
        /*UserDetailsManagerConfigurer.UserDetailsBuilder methods:
        - accountExpired(boolean) Defines if the account is expired or not
        - accountLocked(boolean) Defines if the account is locked or not
        - and() Used for chaining configuration
        - authorities(GrantedAuthority…) Specifies one or more authorities to grant to the user
        - authorities(List<? extends GrantedAuthority>) Specifies one or more authorities to grant to the user
        - authorities(String…) Specifies one or more authorities to grant to the user
        - credentialsExpired(boolean) Defines if the credentials are expired or not
        - disabled(boolean) Defines if the account is disabled or not
        - password(String) Specifies the user’s password
        - roles(String…)	Specifies one or more roles to assign to the user
        *
        * Note that the roles() method is a shortcut for the authorities() methods.
        * Any values given to roles() are prefixed with ROLE_ and assigned as authorities to the user.
        * In effect, the following user configuration is equivalent to that is above:
            auth
            .inMemoryAuthentication()
                .withUser("user").password("password")
                                .authorities("ROLE_USER").and()
                .withUser("admin").password("password")
                                .authorities("ROLE_USER", "ROLE_ADMIN");
        */
    }
    
    /**
     * 9.2.2	Authenticating against database tables
     * SQL queries that will be performed when looking up user details:
     * <p>
     * public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled " +
     * "from users " + "where username = ?";
     * public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
     * "select username,authority " + "from authorities " +
     * "where username = ?";
     * public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority " +
     * "from groups g, group_members gm, group_authorities ga " +
     * "where gm.username = ? " + "and g.id = ga.group_id " + "and g.id = gm.group_id";
     */
    /*@Autowired
    DataSource dataSource;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource);
    }*/
    
    /*OVERRIDING THE DEFAULT USER QUERIES*/
   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(
                "select username, password, true " + "from Spitter where username=?")
            .authoritiesByUsernameQuery(
                "select username, 'ROLE_USER' from Spitter where username=?")
            .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
    }*/
}

