package com.example.demo.repository;

import com.example.demo.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

	Person findByName(String name);
}
