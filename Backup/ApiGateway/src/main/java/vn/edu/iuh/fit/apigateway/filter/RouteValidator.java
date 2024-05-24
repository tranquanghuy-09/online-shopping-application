package vn.edu.iuh.fit.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/login", "/api/v1/auth/register",
            "/eureka", "/api/v1/auth/validate-token", "/api/v1/auth/refresh-token",
            "/api/v1/products", "/api/v1/products/{id}"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
