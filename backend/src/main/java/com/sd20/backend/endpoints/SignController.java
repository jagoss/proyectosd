package com.sd20.backend.endpoints;

import com.sd20.backend.repositories.UserRepo;
import com.sd20.backend.utils.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("users")
public class SignController {

    private final UserRepo userRepo;

    @Autowired
    public SignController(UserRepo ur) {
        this.userRepo = ur;
    }

    @PostMapping("/new")
    public ResponseEntity<User> signup(@RequestBody User newUser) {
        if (userRepo.findUserByUser(newUser.getUser()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        User savedUser = userRepo.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestBody User userIn) {
        User user = userRepo.findUserByUserAndPassword(userIn.getUser(), userIn.getPassword());
        if (user == null)
            return ResponseEntity.notFound().build();
        String token = getJWTToken(userIn.getUser());
        user.setToken(token);
        return ResponseEntity.ok(user);
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
