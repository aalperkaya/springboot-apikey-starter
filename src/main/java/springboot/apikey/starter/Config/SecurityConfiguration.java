package springboot.apikey.starter.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(ApiKeyProperties.class)
public class SecurityConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	private final ApiKeyProperties apiKeyProperties;
	private final ApiKeyAuthenticationEntryPoint authenticationEntryPoint;

	public SecurityConfiguration(ApiKeyProperties apiKeyProperties, ApiKeyAuthenticationEntryPoint authenticationEntryPoint) {
		this.apiKeyProperties = apiKeyProperties;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

//	@Bean
//	WebSecurityCustomizer webSecurityCustomizer() {
//		// WARNING: If you are configuring WebSecurity to ignore requests, consider
//		// using permitAll via HttpSecurity#authorizeHttpRequests instead
//		// return (web) -> web.ignoring().requestMatchers("/test/**", "/actuator/**");
//	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		ApiKeyAuthFilter filter = new ApiKeyAuthFilter(apiKeyProperties.getHeaderName());
		filter.setAuthenticationManager(authentication -> {
			String principal = (String) authentication.getPrincipal();
			
			Optional<ApiKeyProperties.KeyConfig> matchedKey = apiKeyProperties.getKeys().stream()
					.filter(key -> key.getValue().equals(principal))
					.findFirst();

			if (matchedKey.isEmpty()) {
				throw new BadCredentialsException("API Key missing or invalid");
			}

			ApiKeyProperties.KeyConfig keyConfig = matchedKey.get();
			List<SimpleGrantedAuthority> authorities = keyConfig.getRoles().stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			authentication = new PreAuthenticatedAuthenticationToken(principal, null, authorities);
			authentication.setAuthenticated(true);
			return authentication;
		});

		http
			.sessionManagement( (sm) -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(filter)
			.exceptionHandling( (eh) -> eh.authenticationEntryPoint(authenticationEntryPoint))
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/test/**", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
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