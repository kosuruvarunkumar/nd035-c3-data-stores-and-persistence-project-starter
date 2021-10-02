package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
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

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setEmployees(getEmployees(scheduleDTO.getEmployeeIds()));
        schedule.setPets(getPets(scheduleDTO.getPetIds()));
        schedule = scheduleRepository.save(schedule);

        return getScheduleDTO(schedule);
    }

    public List<ScheduleDTO> getAllSchedules(){
        Iterable<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : schedules) {
            scheduleDTOS.add(getScheduleDTO(schedule));
        }

        return scheduleDTOS;
    }

    public List<ScheduleDTO> getScheduleForPet(Long petID){
        Optional<Pet> pet = petRepository.findById(petID);
        List<Schedule> petSchedules;
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if(pet.isPresent()) {
            petSchedules = scheduleRepository.findAllByPets(pet.get());
        } else {
            throw new RuntimeException("Pet with id: "+ petID+" not found");
        }
        for(Schedule schedule: petSchedules) {
            scheduleDTOS.add(getScheduleDTO(schedule));
        }
        return scheduleDTOS;

    }

    public List<ScheduleDTO> getScheduleForEmployee(Long employeeID){
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        List<Schedule> employeeSchedules;
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if(employee.isPresent()) {
            employeeSchedules = scheduleRepository.findAllByEmployees(employee.get());
        } else {
            throw new RuntimeException("Employee with id: "+ employeeID+" not found");
        }
        for(Schedule schedule: employeeSchedules) {
            scheduleDTOS.add(getScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    public List<ScheduleDTO> getScheduleForCustomer(Long customerID){
        Optional<Customer> customer = customerRepository.findById(customerID);
        List<Pet> pets;
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if(customer.isPresent()) {
            pets = customer.get().getPets();
        }else {
            throw new RuntimeException("Customer with ID: "+customerID + " not found");
        }
        for(Pet pet : pets) {
            List<ScheduleDTO> petDTOS = getScheduleForPet(pet.getId());
            for(ScheduleDTO scheduleDTO : petDTOS) {
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

    private ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setActivities(schedule.getActivities());
        scheduleDTO.setDate(schedule.getDate());
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();
        for(Employee employee: schedule.getEmployees()) {
            employeeIds.add(employee.getId());
        }
        for(Pet pet: schedule.getPets()) {
            petIds.add(pet.getId());
        }
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);

        return scheduleDTO;
    }
}
