package com.example.demo;

import com.example.demo.domain.GameLevel;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.service.GameLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    @Bean
//    CommandLineRunner anotherDemo(GameLevelRepository gameLevelRepository) {
//        return args -> {
//
//            gameLevelRepository.deleteAll();
//
//            GameLevel gameLevel = new GameLevel();
//            gameLevel.setTitle("Title");
//
//
//            GameLevel savedGameLevel = gameLevelRepository.save(gameLevel);
//            log.info("Saved game level: {}", savedGameLevel);
//
//            Optional<GameLevel> foundGameLevel = gameLevelRepository.findById(savedGameLevel.getId());
//            log.info("Saved game level: {}", foundGameLevel);
//        };
//    }
//
//    @Bean
//    CommandLineRunner yetAnotherDemo(GameLevelService gameLevelService) {
//        return args -> {
//
//            List<GameLevelDto> allGameLevels = gameLevelService.findAllGameLevels();
//            allGameLevels.forEach(gameLevel -> log.info("Found game level with ID {} and title {}", gameLevel.getId(),
//                                                        gameLevel.getTitle()));
//        };
//    }
}
