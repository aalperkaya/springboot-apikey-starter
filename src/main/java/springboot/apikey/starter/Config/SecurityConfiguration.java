package springboot.apikey.starter.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Value("${springboot.apikey.header-name}")
	private String principalRequestHeader;

	@Value("${springboot.apikey.value}")
	private String principalRequestValue;

//	@Bean
//	WebSecurityCustomizer webSecurityCustomizer() {
//		// WARNING: If you are configuring WebSecurity to ignore requests, consider
//		// using permitAll via HttpSecurity#authorizeHttpRequests instead
//		// return (web) -> web.ignoring().requestMatchers("/test/**", "/actuator/**");
//	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);
		filter.setAuthenticationManager(authentication -> {
			String principal = (String) authentication.getPrincipal();
			
			if (!Objects.equals(principalRequestValue, principal)) {
				throw new BadCredentialsException("API Key missing or invalid");
			}
			authentication.setAuthenticated(true);
			return authentication;
		});

		http
			.sessionManagement( (sm) -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(filter)
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/test/**", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
					.anyRequest().authenticated());

		return http.build();
	}
	
	@Bean
    UserDetailsService userDetailsService() {
//            UserDetails user = User.withDefaultPasswordEncoder()
//                    .username("user")
//                    .password("password")
//                    .roles("USER")
//                    .build();
//            return new InMemoryUserDetailsManager(user);
		return new InMemoryUserDetailsManager();
    }

}