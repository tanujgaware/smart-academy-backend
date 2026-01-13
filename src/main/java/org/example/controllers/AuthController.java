package org.example.controllers;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.JwtUtil;
import org.example.dtos.ApiResponse;
import org.example.dtos.LoginRequest;
import org.example.entities.User;
import org.example.entities.UserResponse;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.services.UserService;
import org.example.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtutil;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request, HttpServletResponse response){
        User user=userService.findByEmail(request.getEmail());
        if(user==null){
            throw new ResourceNotFoundException("User Not Found");
        }

        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        if(!encoder.matches(request.getPassword(),user.getPassword()))
            throw new BadRequestException("Invalid Password");
        String token=jwtutil.generateToken(user);
        Cookie cookie =new  Cookie("token",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setSecure(false);      // false for localhost
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);
        return new ApiResponse(true,"Login Success",user);
    }

    @GetMapping("/me")
    public ApiResponse me(Authentication authentication){
        if(authentication==null){
            throw new BadRequestException("Unauthorized");
        }

        UUID uuid= UUID.fromString(authentication.getPrincipal().toString());
        User user=userService.findById(uuid);

        return new ApiResponse(
                true,
                "User fetched Successfully",
                new UserResponse(
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole()
                )
        );
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletResponse response){
        Cookie cookie=new Cookie("token",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ApiResponse(
                true,
                "Logout successful",
                null
        );
    }
}
