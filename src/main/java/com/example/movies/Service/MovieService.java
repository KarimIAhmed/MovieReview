package com.example.movies.Service;

import com.example.movies.Model.Movie;
import com.example.movies.Model.UpcomingMovies;
import com.example.movies.Repository.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class MovieService {

    @Value("${api.token}")
    String bearerToken;
    @Autowired
    private MovieRepository movieRepository;
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
    public Optional<Movie> getMovieById(int id){

        return movieRepository.findById(id);
    }
    public void updateDatabase(){
        try {
            String uri="https://api.themoviedb.org/3/movie/upcoming";
            RestTemplate restTemplate= new RestTemplate();
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + bearerToken);

            HttpEntity<String> httpEntity= new HttpEntity<>(httpHeaders);
            ResponseEntity<String> response= restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );

            ObjectMapper objectMapper=new ObjectMapper();
            UpcomingMovies upcomingMovies= objectMapper.readValue(response.getBody(), UpcomingMovies.class);
            List<Movie> moviesToInsert= upcomingMovies.getResults().stream()
                            .filter(movie -> !movieRepository.existsById(movie.getId()))
                            .toList();

            log.info("Inserting " + moviesToInsert.size() + " movies in database");
            movieRepository.insert(moviesToInsert);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
