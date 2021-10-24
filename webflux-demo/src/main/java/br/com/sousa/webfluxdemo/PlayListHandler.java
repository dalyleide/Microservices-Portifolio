package br.com.sousa.webfluxdemo;

import br.com.sousa.webfluxdemo.document.PlayList;
import br.com.sousa.webfluxdemo.service.PlayListService;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

//@Component
public class PlayListHandler {
    
    private final PlayListService service;

    public PlayListHandler(final PlayListService service) {
        this.service = service;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), PlayList.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findById(id), PlayList.class);
    }

    public Mono<ServerResponse> save(ServerRequest request){
        final Mono<PlayList> playlist = request.bodyToMono(PlayList.class);
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(playlist.flatMap(service::save), PlayList.class));
    }
}
