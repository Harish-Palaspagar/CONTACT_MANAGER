package com.contact.configurations;


//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


//USE SPRING SECURITY DEPENDENCY TO SET PASSWORD ENCRYPTION

@Configuration
@EnableWebSecurity
public class MyConfiguration {

   


	@Bean
    UserDetailsService getUserDetailsService()
	{
		return new UserDetailServiceImpl();
	}

    @Bean
    BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
    
    // CONFIGURE METHOD
//    
//    @Autowired
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }


 
    
	@Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )// to view the custom login page
                .formLogin(login->login
                		.loginPage("/signin")
                		.loginProcessingUrl("/dologin")
                		.defaultSuccessUrl("/user/index")
                	//  activate this when needed failure page	.failureUrl("/login-fail")
                		.permitAll());
        
        return httpSecurity.build();
    }
    
    
    
    
    
    
  
}
    
	

