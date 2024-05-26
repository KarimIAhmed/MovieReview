package com.example.movies.Repository;

import com.example.movies.Model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface MovieRepository extends MongoRepository<Movie, Integer> {

}
