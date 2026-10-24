package com.example.helpdesk.controller;

import com.example.helpdesk.DTO.LoginRequestDTO;
import com.example.helpdesk.DTO.UserResponseDTO;
import com.example.helpdesk.DTO.UserRequestDTO;
import com.example.helpdesk.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Users")
public class UserController {
     UserService userService;
     public UserController(UserService userService){
         this.userService = userService;
     }
    @PostMapping("/createuser")
    public ResponseEntity<UserResponseDTO> CreateUser(@Valid @RequestBody UserRequestDTO user){
          UserResponseDTO response =  userService.createUser(user);
          return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/getalluser")
    public ResponseEntity <List<UserResponseDTO>> GetAllUser(){
        List<UserResponseDTO> response =  userService.GetAllUser();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/getuserbyid")
    public ResponseEntity<UserResponseDTO> GetUserById(@RequestParam long id){
         UserResponseDTO user = userService.GetUserByID(id);
         return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/GetLogin")
    public ResponseEntity<String>Login(@RequestBody LoginRequestDTO Loginreq){
         String user = userService.login(Loginreq);
         return ResponseEntity.status(HttpStatus.OK).body(user);
     }


}
