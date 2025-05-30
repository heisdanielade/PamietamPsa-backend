package com.github.heisdanielade.pamietampsa.controller;

import com.github.heisdanielade.pamietampsa.entity.AppUser;
import com.github.heisdanielade.pamietampsa.exception.auth.AccountNotFoundException;
import com.github.heisdanielade.pamietampsa.repository.AppUserRepository;
import com.github.heisdanielade.pamietampsa.response.ApiResponse;
import com.github.heisdanielade.pamietampsa.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1", produces = "application/json")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    @GetMapping(path = "/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> baseUserInfo(Principal principal){
        String userEmail = principal.getName();
        AppUser currentUser = appUserRepository.findByEmail(userEmail)
                .orElseThrow(AccountNotFoundException::new);

        Map<String, Object> data = new HashMap<>();
        data.put("email", currentUser.getEmail());
        data.put("name", currentUser.getName());
        data.put("initial", currentUser.getInitial());
        data.put("enabled", currentUser.isEnabled());
        data.put("role", currentUser.getRole());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Currently logged in user details.",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Implement role based
    @GetMapping("/users/all")
    public ResponseEntity<List<AppUser>> allUsers(){
        List<AppUser> users = appUserService.allUsers();
        return ResponseEntity.ok(users);
    }

}
