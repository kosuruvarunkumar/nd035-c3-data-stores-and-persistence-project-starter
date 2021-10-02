package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return getPetDTO(petService.savePet(getEntity(petDTO), petDTO.getOwnerId()));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return getPetDTO(petService.getPetById(petId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet pet : pets) {
            petDTOS.add(getPetDTO(pet));
        }
        return petDTOS;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petDTOS = new ArrayList<>();
        try{
            List<Pet> pets = petService.getPetsByOwnerID(ownerId);
            for(Pet pet : pets) {
                petDTOS.add(getPetDTO(pet));
            }
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }

        return petDTOS;
    }

    public Pet getEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setNotes(petDTO.getNotes());
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());

        return pet;
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
