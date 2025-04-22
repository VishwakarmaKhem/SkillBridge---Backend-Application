package com.skillbridgebackend.skillBridge.backend.Service.impl;

import com.skillbridgebackend.skillBridge.backend.Dto.LoginDto;
import com.skillbridgebackend.skillBridge.backend.Dto.RegisterDto;
import com.skillbridgebackend.skillBridge.backend.Entity.Role;
import com.skillbridgebackend.skillBridge.backend.Entity.User;
import com.skillbridgebackend.skillBridge.backend.Exception.SkillBridgeAPIException;
import com.skillbridgebackend.skillBridge.backend.Repository.RoleRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.UserRepository;
import com.skillbridgebackend.skillBridge.backend.Security.JwtTokenProvider;
import com.skillbridgebackend.skillBridge.backend.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager
            , UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder
            , JwtTokenProvider jwtTokenProvider ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {

       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {
        //checking if the username already exist in DB or not
        if (userRepository.existsByUsername(registerDto.getUsername())){
            throw new SkillBridgeAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())){
            throw new SkillBridgeAPIException(HttpStatus.BAD_REQUEST, "User with this email already exists!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userrole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userrole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully!";
    }
}
