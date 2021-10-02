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

    public PetDTO savePet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setNotes(petDTO.getNotes());
        Optional<Customer> customer = customerRepository.findById(petDTO.getOwnerId());
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
        petDTO.setId(pet.getId());
        return getPetDTO(pet);
    }

    public PetDTO getPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        PetDTO petDTO;
        if(pet.isPresent()) {
            petDTO = getPetDTO(pet.get());
        } else {
            throw new RuntimeException("Pet with id: "+ id+" not found");
        }

        return petDTO;
    }

    public List<PetDTO> getAllPets() {
        List<PetDTO> petDTOS = new ArrayList<>();
        Iterable<Pet> pets = petRepository.findAll();
        for(Pet pet: pets) {
            petDTOS.add(getPetDTO(pet));
        }

        return petDTOS;
    }

    public List<PetDTO> getPetsByOwnerID(Long ownerID) {
        Optional<Customer> customer = customerRepository.findById(ownerID);
        List<PetDTO> petDTOS = new ArrayList<>();
        if(customer.isPresent()) {
            List<Pet> pets = customer.get().getPets();
            for(Pet pet : pets) {
                petDTOS.add(getPetDTO(pet));
            }
        } else {
            throw new RuntimeException("Owner id: "+ownerID+" not found");
        }
        return petDTOS;
    }

    private PetDTO getPetDTO(Pet pet) {
        PetDTO petDTO= new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getCustomer().getId());

        return petDTO;
    }

}
