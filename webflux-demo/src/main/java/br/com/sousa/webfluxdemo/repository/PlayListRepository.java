package br.com.sousa.webfluxdemo.repository;

import br.com.sousa.webfluxdemo.document.PlayList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PlayListRepository extends ReactiveMongoRepository<PlayList, String> {

}
