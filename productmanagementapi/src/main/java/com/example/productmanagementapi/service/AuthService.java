package com.example.productmanagementapi.service;


import com.example.productmanagementapi.dto.LoginRequestDTO;
import com.example.productmanagementapi.dto.LoginResponseDTO;
import com.example.productmanagementapi.dto.UserRequestDTO;
import com.example.productmanagementapi.dto.UserResponseDTO;
import com.example.productmanagementapi.entity.User;
import com.example.productmanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;



    public UserResponseDTO register(UserRequestDTO dto){

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();

        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());

        return response;
    }

    public LoginResponseDTO login(LoginRequestDTO dto){

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with username: " + dto.getUsername()
                        ));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponseDTO(token);
    }
}
