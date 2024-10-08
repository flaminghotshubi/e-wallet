package com.ewallet.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public final List<String> openEndpoints = List.of(
            "/users/register",
            "/users/login",
            "users/validate",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest ->
            openEndpoints
                    .stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
