package com.jwtSecurity.controller;

import com.jwtSecurity.dto.AuthResponseDto;
import com.jwtSecurity.dto.LoginDto;
import com.jwtSecurity.dto.RegisterDto;
import com.jwtSecurity.model.Role;
import com.jwtSecurity.model.UserEntity;
import com.jwtSecurity.repository.RoleRepository;
import com.jwtSecurity.repository.UserEntityRepository;
import com.jwtSecurity.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtGenerator jwtGenerator;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto)
    {
      if(userEntityRepository.existsByUsername(registerDto.getUsername()))
      {
          return new ResponseEntity<>("userName is taken", HttpStatus.BAD_REQUEST);
      }
      UserEntity user = new UserEntity();
      user.setUsername(registerDto.getUsername());
      user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

      Role role = roleRepository.findByName("USER").get();
      user.setRoles(Collections.singletonList(role));

      userEntityRepository.save(user);

      return new ResponseEntity<>("User Registered Successfully", HttpStatus.OK);
    }
}
