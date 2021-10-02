package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
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

    public Customer saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        List<Pet> petList = new ArrayList<>();
        List<Long> petIds = customerDTO.getPetIds();
        customer.setName(customerDTO.getName());
        if(petIds != null) {
            for(Long id : petIds) {
                Optional<Pet> pet = petRepository.findById(id);
                if(pet.isPresent()) {
                    petList.add(pet.get());
                } else {
                    throw new RuntimeException("Pet with id: "+ id+" not found in the system");
                }
            }
        }
        customer.setPets(petList);
        customer.setNotes(customerDTO.getNotes());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        return customerRepository.save(customer);
    }

    public List<CustomerDTO> findAllCustomers() {
        Iterable<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer : customers) {
            customerDTOS.add(getCustomerDTO(customer));
        }

        return customerDTOS;
    }

    public CustomerDTO findOwnerByPet(Long petID) {
        Optional<Pet> pet = petRepository.findById(petID);
        Customer customer;
        if(pet.isPresent()) {
            customer = pet.get().getCustomer();
        } else {
            throw new RuntimeException("Pet with id: "+petID+" not found");
        }
        return getCustomerDTO(customer);
    }

    private CustomerDTO getCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setPetIds(getPetIds(customer.getPets()));
        customerDTO.setId(customer.getId());

        return customerDTO;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee = employeeRepository.save(employee);
        return getEmployeeDTO(employee);
    }

    public EmployeeDTO getEmployee(Long employeeID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        if(employee.isPresent()) {
            return getEmployeeDTO(employee.get());
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

    public List<EmployeeDTO> findEmployeesForService(LocalDate localDate, Set<EmployeeSkill> skills) {
        Iterable<Employee> allEmployees = employeeRepository.findByDaysAvailable(localDate.getDayOfWeek());
        List<EmployeeDTO> employeeList = new ArrayList<>();
        for(Employee employee : allEmployees) {
            if(employee.getSkills().containsAll(skills)) {
                employeeList.add(getEmployeeDTO(employee));
            }
        }
        return employeeList;
    }

    private EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());

        return employeeDTO;
    }

    private List<Long> getPetIds(List<Pet> pets) {
        List<Long> petIds = new ArrayList<>();
        for(Pet pet: pets) {
            petIds.add(pet.getId());
        }
        return petIds;
    }


}
