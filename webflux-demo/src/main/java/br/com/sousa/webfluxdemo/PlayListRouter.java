package br.com.sousa.webfluxdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

//@Configuration
public class PlayListRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PlayListHandler handler){
        return RouterFunctions
                .route(GET("/v1/playlist").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/v1/playlist/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST("/v1/playlist").and(accept(MediaType.APPLICATION_JSON)), handler::save);

    }
}
