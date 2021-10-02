package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIDs, List<Long> petIDs){
        schedule.setEmployees(getEmployees(employeeIDs));
        schedule.setPets(getPets(petIDs));
        schedule = scheduleRepository.save(schedule);

        return schedule;
    }

    public List<Schedule> getAllSchedules(){
        return (List<Schedule>) scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petID){
        Optional<Pet> pet = petRepository.findById(petID);
        if(pet.isPresent()) {
            return scheduleRepository.findAllByPets(pet.get());
        } else {
            throw new RuntimeException("Pet with id: "+ petID+" not found");
        }

    }

    public List<Schedule> getScheduleForEmployee(Long employeeID){
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        List<Schedule> scheduleDTOS = new ArrayList<>();
        if(employee.isPresent()) {
            return scheduleRepository.findAllByEmployees(employee.get());
        } else {
            throw new RuntimeException("Employee with id: "+ employeeID+" not found");
        }
    }

    public List<Schedule> getScheduleForCustomer(Long customerID){
        Optional<Customer> customer = customerRepository.findById(customerID);
        List<Pet> pets;
        List<Schedule> scheduleDTOS = new ArrayList<>();
        if(customer.isPresent()) {
            pets = customer.get().getPets();
        }else {
            throw new RuntimeException("Customer with ID: "+customerID + " not found");
        }
        for(Pet pet : pets) {
            List<Schedule> petDTOS = getScheduleForPet(pet.getId());
            for(Schedule scheduleDTO : petDTOS) {
                scheduleDTOS.add(scheduleDTO);
            }
        }

        return scheduleDTOS;
    }

    private List<Employee> getEmployees(List<Long> employeeIDs) {
        List<Employee> employees = new ArrayList<>();
        for(Long id : employeeIDs) {
            Optional<Employee> employee = employeeRepository.findById(id);
            if(employee.isPresent()) {
                employees.add(employee.get());
            } else {
                throw new RuntimeException("Employee with ID: "+ id +" not found");
            }
        }

        return employees;
    }

    private List<Pet> getPets(List<Long> petIDs) {
        List<Pet> pets = new ArrayList<>();
        for(Long id : petIDs) {
            Optional<Pet> pet = petRepository.findById(id);
            if(pet.isPresent()) {
                pets.add(pet.get());
            } else {
                throw new RuntimeException("Pet with ID: "+ id +" not found");
            }
        }

        return pets;
    }

}
