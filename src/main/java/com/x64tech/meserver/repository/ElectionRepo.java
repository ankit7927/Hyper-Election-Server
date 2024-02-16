package com.x64tech.meserver.repository;

import com.x64tech.meserver.models.ElectionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepo extends MongoRepository<ElectionModel, String> {
}
