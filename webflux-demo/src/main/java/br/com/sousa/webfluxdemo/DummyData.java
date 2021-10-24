package br.com.sousa.webfluxdemo;

import br.com.sousa.webfluxdemo.document.PlayList;
import br.com.sousa.webfluxdemo.repository.PlayListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Component
public class DummyData implements CommandLineRunner {

    private final PlayListRepository repository;

    public DummyData(final PlayListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

//        repository.deleteAll()
//                .thenMany(
//                        Flux.just("PlayList 01", "PlayList 02", "PlayList 03").map(name -> new PlayList(UUID.randomUUID().toString(), name))
//                        .flatMap(repository::save))
//                .subscribe(System.out::println);
    }
}
