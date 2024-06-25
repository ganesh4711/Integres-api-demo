package com.integrations.orderprocessing.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.integrations.orderprocessing.filter.JwtAuthFilter;
import com.integrations.orderprocessing.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;
	
	@Bean
	JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter(exceptionResolver);
	}

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
        	    .authorizeHttpRequests(auth ->
                 auth.requestMatchers(
                                 "/api/v1/optioryx/getItems",
                                 "/api/v1/optioryx/getItemCount",
                                 "/api/v1/optioryx/getItemById",
                                 "/api/v1/optioryx/getItemsByBarcodes",
                                 "/api/v1/optioryx/getItemCountByBarcodes",
                		 "/api/v1/healthz",
                		 "/sei-docs",
                		 "/api/v1/sendGoodsReceipt",
                		 "/api/v1/sendShipmentConfirm",
                         "/api/v1/sendAdjustment",
                		 "/api/v1/onManifestAssignedByTest",
                		 "/api/v1/onManifestAssigned",
                		 "/api/v1/integrations/authenticate",
                		 "/api/v1/integrations/onSendFleetEnableOrderData",
                		 "/api/v1/integrations/onSendWMSData",
                		 "/api/v1/FreightSnap/getDimData",
                		 "/api/v1/FreightSnap/getDimScanData",
                		 "/api/v1/FreightSnap/getDateRangeDimScanData",
                		 "/v3/api-docs/**", 
    	                 "/swagger-ui/**",
    	                 "/swagger-ui.html"
    	                 ).permitAll()
                        .requestMatchers("/api/v1/integrations/**")
                        .authenticated()
                 )
                .sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class).build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
