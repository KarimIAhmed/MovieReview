package com.example.movies.Controller;

import com.example.movies.Model.Movie;
import com.example.movies.Service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
//@RequestMapping("api/v1/movies")
@CrossOrigin(origins = "*")
public class MovieController {
    @Value("${api.token}")
    String bearerToken;
    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(){
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Optional<Movie>> getMovieById(@PathVariable int id){
        Optional<Movie> movie=movieService.getMovieById(id);
        if(movie.isPresent()){
             return new ResponseEntity<>(movie,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/movies")
    public ResponseEntity<Void> updateDatabase(){
        movieService.updateDatabase();
        return new ResponseEntity<>(HttpStatus.CREATED); //todo modify to appropriate return
    }
}
