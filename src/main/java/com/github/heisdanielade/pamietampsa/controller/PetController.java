package com.github.heisdanielade.pamietampsa.controller;


import com.github.heisdanielade.pamietampsa.dto.pet.AddPetDto;
import com.github.heisdanielade.pamietampsa.entity.Pet;
import com.github.heisdanielade.pamietampsa.response.ApiResponse;
import com.github.heisdanielade.pamietampsa.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/pets", produces = "application/json")
public class PetController {

    private final PetService petService;

    @PostMapping(path = "/add")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addPet(@RequestBody AddPetDto input, Principal principal){
        String userEmail = principal.getName();
        Pet savedPet = petService.addPetToUser(userEmail, input);

        Map<String, Object> data = new HashMap<>();
        data.put("petName", savedPet.getName());
        data.put("species", savedPet.getSpecies());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Pet added for user successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
