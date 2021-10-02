package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer saveCustomer(Customer customer, List<Long> petIDs) {
        List<Pet> petList = new ArrayList<>();
        if(petIDs != null) {
            for(Long id : petIDs) {
                Optional<Pet> pet = petRepository.findById(id);
                if(pet.isPresent()) {
                    petList.add(pet.get());
                } else {
                    throw new RuntimeException("Pet with id: "+ id+" not found in the system");
                }
            }
        }
        customer.setPets(petList);
        return customerRepository.save(customer);
    }

    public List<Customer> findAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Customer findOwnerByPet(Long petID) {
        Optional<Pet> pet = petRepository.findById(petID);
        Customer customer;
        if(pet.isPresent()) {
            return pet.get().getCustomer();
        } else {
            throw new RuntimeException("Pet with id: "+petID+" not found");
        }
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long employeeID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        if(employee.isPresent()) {
            return employee.get();
        } else {
            throw new RuntimeException("Employee with ID: "+employeeID+" not found");
        }
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        if(employee.isPresent()) {
            employee.get().setDaysAvailable(daysAvailable);
            employeeRepository.save(employee.get());
        } else {
            throw new RuntimeException("Employee with ID: "+employeeID+" not found");
        }
    }

    public List<Employee> findEmployeesForService(LocalDate localDate, Set<EmployeeSkill> skills) {
        Iterable<Employee> allEmployees = employeeRepository.findByDaysAvailable(localDate.getDayOfWeek());
        List<Employee> employeeList = new ArrayList<>();
        for(Employee employee : allEmployees) {
            if(employee.getSkills().containsAll(skills)) {
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

}
