package com.udacity.jdnd.course3.critter.repositories;

import com.udacity.jdnd.course3.critter.entities.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Iterable<Employee> findByDaysAvailable(DayOfWeek dayOfWeek);
}
