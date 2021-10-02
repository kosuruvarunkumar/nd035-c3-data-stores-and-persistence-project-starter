package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try{
            return getScheduleDTO(scheduleService.createSchedule(getSchedule(scheduleDTO), scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : scheduleService.getAllSchedules()) {
            scheduleDTOS.add(getScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        try {
            List<ScheduleDTO> schedules =  new ArrayList<>();
            for(Schedule schedule : scheduleService.getScheduleForPet(petId)) {
                schedules.add(getScheduleDTO(schedule));
            }
            return schedules;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try {
            List<ScheduleDTO> schedules =  new ArrayList<>();
            for(Schedule schedule : scheduleService.getScheduleForEmployee(employeeId)) {
                schedules.add(getScheduleDTO(schedule));
            }
            return schedules;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        try {
            List<ScheduleDTO> schedules =  new ArrayList<>();
            for(Schedule schedule : scheduleService.getScheduleForCustomer(customerId)) {
                schedules.add(getScheduleDTO(schedule));
            }
            return schedules;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
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

    private Schedule getSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        return schedule;
    }

}
