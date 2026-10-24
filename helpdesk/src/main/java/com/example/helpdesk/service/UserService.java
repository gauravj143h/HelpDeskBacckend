package com.example.helpdesk.service;

import com.example.helpdesk.DTO.LoginRequestDTO;
import com.example.helpdesk.DTO.UserResponseDTO;
import com.example.helpdesk.DTO.UserRequestDTO;
import com.example.helpdesk.entity.UserEntity;
import com.example.helpdesk.exceptions.DuplicateEmail;
import com.example.helpdesk.exceptions.InvalidCredentialsException;
import com.example.helpdesk.exceptions.ResourseNotfoundException;
import com.example.helpdesk.repository.UserRepo;
import com.example.helpdesk.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public UserResponseDTO createUser(UserRequestDTO user){
        if(userRepo.existsByEmail(user.getEmail())){
            throw new DuplicateEmail("We have alredy user with this email "+ user.getEmail());
        }
        UserEntity response = dtoToEntity(user);
        UserEntity responseEntity = userRepo.save(response);
        UserResponseDTO responseDto = entityToDto(responseEntity);
        return responseDto;
    }

    public List<UserResponseDTO> GetAllUser(){
        List<UserEntity> users = userRepo.findAll();       // get all entities
        List<UserResponseDTO> dtos = new ArrayList<>();     // empty list to collect results
        for (UserEntity user : users) {                     // for each entity...
            UserResponseDTO dto = entityToDto(user);        // convert ONE
            dtos.add(dto);                                  // add it to the list
        }
        return dtos;                                        // return the full list
    }

    public UserResponseDTO GetUserByID(long id){
        UserEntity user = userRepo.findById(id).orElseThrow(()-> new ResourseNotfoundException("User not found"));
        UserResponseDTO response = entityToDto(user);
        return response;
    }

    public UserEntity dtoToEntity(UserRequestDTO user){
        UserEntity userTosave = new UserEntity();
        userTosave.setName(user.getName());
        userTosave.setEmail(user.getEmail());
        userTosave.setRole(user.getRole());
        userTosave.setPassword(passwordEncoder.encode(user.getPassword()));

        return userTosave;
    }

    public UserResponseDTO entityToDto(UserEntity user){
        UserResponseDTO userToDto = new UserResponseDTO();
        userToDto.setId(user.getId());
        userToDto.setName(user.getName());
        userToDto.setEmail((user.getEmail()));
        userToDto.setRole(user.getRole());
        return userToDto;
    }

    public String login(LoginRequestDTO loginRequest){
        UserEntity user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail(),user.getRole(),user.getId());
    }

}
