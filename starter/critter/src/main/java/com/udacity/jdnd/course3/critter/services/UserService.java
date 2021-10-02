package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

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
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setPetIds(getPetIds(customer.getPets()));
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            customerDTO.setName(customer.getName());
            customerDTO.setNotes(customer.getNotes());
            customerDTO.setId(customer.getId());
            customerDTOS.add(customerDTO);
        }

        return customerDTOS;
    }

    public CustomerDTO findOwnerByPet(Long petID) {
        Optional<Pet> pet = petRepository.findById(petID);
        Customer customer;
        CustomerDTO customerDTO = new CustomerDTO();
        if(pet.isPresent()) {
            customer = pet.get().getCustomer();
        } else {
            throw new RuntimeException("Pet with id: "+petID+" not found");
        }
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setPetIds(getPetIds(customer.getPets()));
        customerDTO.setId(customer.getId());

        return customerDTO;
    }

    private List<Long> getPetIds(List<Pet> pets) {
        List<Long> petIds = new ArrayList<>();
        for(Pet pet: pets) {
            petIds.add(pet.getId());
        }
        return petIds;
    }


}
