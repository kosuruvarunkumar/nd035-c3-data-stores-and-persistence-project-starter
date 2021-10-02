package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet savePet(Pet pet, Long ownerID) {
        Optional<Customer> customer = customerRepository.findById(ownerID);
        List<Pet> pets;
        if(customer.isPresent()) {
            pet.setCustomer(customer.get());
            pets = customer.get().getPets();
        } else {
            throw new RuntimeException("Owner of the pet not found");
        }
        pet = petRepository.save(pet);
        pets.add(pet);
        customer.get().setPets(pets);
        return pet;
    }

    public Pet getPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if(pet.isPresent()) {
            return pet.get();
        } else {
            throw new RuntimeException("Pet with id: "+ id+" not found");
        }


    }

    public List<Pet> getAllPets() {
        return (List<Pet>) petRepository.findAll();
    }

    public List<Pet> getPetsByOwnerID(Long ownerID) {
        Optional<Customer> customer = customerRepository.findById(ownerID);
        List<Pet> pets;
        if(customer.isPresent()) {
            pets = customer.get().getPets();
        } else {
            throw new RuntimeException("Owner id: "+ownerID+" not found");
        }
        return pets;
    }
}
