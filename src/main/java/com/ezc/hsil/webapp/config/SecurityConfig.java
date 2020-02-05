package com.ezc.hsil.webapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.ezc.hsil.webapp.security.CustomAuthenticationProvider;
import com.ezc.hsil.webapp.security.CustomRememberMeServices;

@Configuration
@EnableWebSecurity
@PropertySource({ "classpath:messages_en.properties" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 @Autowired
	 PersistenceJPAConfig pJPAConfig;
	 //private DataSource dataSource;;
	 
	
   // private CustomUserServiceImpl userDetailsService;
	@Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private LogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

//    @Autowired
//    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

//    @Autowired
//    private UserRepository userRepository;

    public SecurityConfig() {
        super();
    }

    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
//    	auth
//    	.userDetailsService(userDetailsService)
//    	.passwordEncoder(encoder());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Value("${message.sessionExpired}") 
    String sessionExpired;
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
       // @formatter:off
        http
//        	.csrf().disable() 
        	     .authorizeRequests()
        	     .antMatchers("/bootstrap/**", "/dist/**", "/plugins/**").permitAll()
                .antMatchers("/login*","/login*","/favicon.ico" ,
                		//"/logout*",
                		"/signin/**", "/signup/**", "/customLogin",
                        "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*",
                        "/badUser*", "/user/resendRegistrationToken*" ,"/forgetPassword*", "/user/resetPassword*",
                        "/user/changePassword*", "/emailError*", "/resources/**","/old/user/registration*","/successRegister*","/qrcode*"
                       )
                .permitAll()
                .antMatchers("/invalidSession*").anonymous()
                .antMatchers("/user/updatePassword*","/user/savePassword*","/updatePassword*")
                .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
//                .antMatchers("/master/**","/modals/**","/modal/**").fullyAuthenticated()	
                
                //.hasAuthority("READ_PRIVILEGE")
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard/index")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                //.authenticationDetailsSource(authenticationDetailsSource)
            .permitAll()
                .and()
            .sessionManagement()
                .invalidSessionUrl("/login?message="+sessionExpired)
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .sessionFixation().none()
            .and()
            .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/logout.html?logSucc=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
             .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices()).key("theKey");
                
//             .rememberMe().rememberMeParameter("remember-me").
//             tokenRepository(tokenRepository()); 
    // @formatter:on
    }

    // beans

    
    @Bean
    public PersistentTokenRepository tokenRepository() {
      JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
      jdbcTokenRepositoryImpl.setDataSource(pJPAConfig.dataSource());
      return jdbcTokenRepositoryImpl;
    }
    
    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider; 
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        CustomRememberMeServices rememberMeServices = new CustomRememberMeServices("theKey", userDetailsService, tokenRepository()); //new InMemoryTokenRepositoryImpl()
        return rememberMeServices;
    }
}
	
	
	
	
