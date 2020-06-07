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
@RequestMapping("user")
public class SignController {

    UserRepo userRepo;

    @Autowired
    public SignController(UserRepo ur){
        this.userRepo = ur;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User userIn){
        // TODO hacer signup para nuevos usuarios, por ahora los metemos a mano
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestBody User userIn) {
        System.out.println("ffff");
        User user = null;
        if ((user = userRepo.findByUserAndPassword(userIn.getUser(), userIn.getPassword())) == null) {
            System.out.println("hola");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token = getJWTToken(userIn.getUser());
        user.setToken(token);
        return new ResponseEntity<User>(user, HttpStatus.OK);
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
