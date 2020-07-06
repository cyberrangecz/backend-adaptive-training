package com.example.demo;

import com.example.demo.domain.GameLevel;
import com.example.demo.domain.Person;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(PersonRepository personRepository) {
		return args -> {

			personRepository.deleteAll();

			Person greg = new Person("Greg");
			Person roy = new Person("Roy");
			Person craig = new Person("Craig");

			List<Person> team = Arrays.asList(greg, roy, craig);

			log.info("Before linking up with Neo4j...");

			team.stream().forEach(person -> log.info("\t" + person.toString()));

			personRepository.save(greg);
			personRepository.save(roy);
			personRepository.save(craig);

			greg = personRepository.findByName(greg.getName());
			greg.worksWith(roy);
			greg.worksWith(craig);
			personRepository.save(greg);

			roy = personRepository.findByName(roy.getName());
			roy.worksWith(craig);
			// We already know that roy works with greg
			personRepository.save(roy);

			// We already know craig works with roy and greg

			log.info("Lookup each person by name...");
			team.stream().forEach(person -> log.info(
					"\t" + personRepository.findByName(person.getName()).toString()));
		};
	}

	@Bean
	CommandLineRunner anotherDemo(GameLevelRepository gameLevelRepository) {
		return args -> {

			gameLevelRepository.deleteAll();

			GameLevel gameLevel = new GameLevel();
			gameLevel.setTitle("Title");


			GameLevel savedGameLevel = gameLevelRepository.save(gameLevel);
			log.info("Saved game level: {}", savedGameLevel);

			Optional<GameLevel> foundGameLevel = gameLevelRepository.findById(savedGameLevel.getId());
			log.info("Saved game level: {}", foundGameLevel);
		};
	}
}
