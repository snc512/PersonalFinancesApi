package com.example.personalfinancesapi.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinancesapi.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

}
