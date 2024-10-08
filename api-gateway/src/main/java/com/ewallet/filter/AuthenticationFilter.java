package com.ewallet.filter;

import com.ewallet.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

//https://medium.com/@kadampritesh46/securing-micro-services-implementing-authentication-and-api-gateway-part-3-4c3930f6bcd4
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtService jwtService;

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = jwtService.getJwtTokenFromHeader(exchange.getRequest());
                if (!jwtService.validateJwtToken(authHeader)) {
                    System.out.println("Invalid access...!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to the application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}
