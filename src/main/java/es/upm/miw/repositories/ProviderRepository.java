package es.upm.miw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.upm.miw.documents.Provider;

public interface ProviderRepository extends MongoRepository<Provider, String> {

}
