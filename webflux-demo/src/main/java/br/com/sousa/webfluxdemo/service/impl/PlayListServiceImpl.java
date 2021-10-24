package br.com.sousa.webfluxdemo.service.impl;

import br.com.sousa.webfluxdemo.document.PlayList;
import br.com.sousa.webfluxdemo.repository.PlayListRepository;
import br.com.sousa.webfluxdemo.service.PlayListService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PlayListServiceImpl implements PlayListService {

    private final PlayListRepository repository;

    public PlayListServiceImpl(final PlayListRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<PlayList> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<PlayList> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<PlayList> save(PlayList playList) {
        playList.setId(UUID.randomUUID().toString());
        return repository.save(playList);
    }
}
