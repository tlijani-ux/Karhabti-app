package com.karhabtiapp.repositories;

import com.karhabtiapp.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarRepsitory extends JpaRepository<Car, Long> {

}
