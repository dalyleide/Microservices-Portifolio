package br.com.sousa.webfluxdemo.controller.v1;

import br.com.sousa.webfluxdemo.document.PlayList;
import br.com.sousa.webfluxdemo.service.PlayListService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/playlist")
public class PlayListController {

    private final PlayListService service;

    public PlayListController(PlayListService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<PlayList> getPlayList(){
        return service.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<PlayList> getPlayListById(@PathVariable final String id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<PlayList> save(@RequestBody final PlayList playList) {
        return service.save(playList);
    }

    @GetMapping(value="/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Long, PlayList>> getPlaylistByWebflux(){

        System.out.println("Start Events: " + LocalDateTime.now());
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
        Flux<PlayList> playlistFlux = service.findAll();

        return Flux.zip(interval, playlistFlux);

    }
}
