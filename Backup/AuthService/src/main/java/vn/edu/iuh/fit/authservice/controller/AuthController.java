package vn.edu.iuh.fit.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import vn.edu.iuh.fit.authservice.authen.UserPrincipal;
import vn.edu.iuh.fit.authservice.dto.UserDTO;
import vn.edu.iuh.fit.authservice.entity.Token;
import vn.edu.iuh.fit.authservice.entity.User;
import vn.edu.iuh.fit.authservice.service.TokenService;
import vn.edu.iuh.fit.authservice.service.UserService;
import vn.edu.iuh.fit.authservice.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

//    @PostMapping("/register")
//    public User register(@RequestBody User user){
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//
//        return userService.createUser(user);
//    }

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO){
//        User user = new User();
//        user.setUsername(userDTO.getUsername());
//        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

        return userService.createUser(userDTO, userDTO.getRoleKeys());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session){

        UserPrincipal userPrincipal =
                userService.findByUsername(user.getUsername());

        if (null == user || !new BCryptPasswordEncoder()
                .matches(user.getPassword(), userPrincipal.getPassword())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account or password is not valid!");
        }

        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));

        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(token);

        session.setAttribute("token", token.getToken());

        return ResponseEntity.ok(token.getToken());
    }


    @GetMapping("/test")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("Hello World!");
    }

//    @GetMapping("/user-id")
//    public ResponseEntity<?> getUserIdFromToken(@RequestParam String token){
//        Set<String> rolesFromToken = jwtUtil.getRolesFromToken(token);
//        if (rolesFromToken.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
//        }
//        return ResponseEntity.ok(rolesFromToken);
//    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> getUserIdFromToken(@RequestParam String token){
        UserPrincipal userFromToken = jwtUtil.getUserFromToken(token);
        if (userFromToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        return ResponseEntity.ok(userFromToken);
    }


//    Object principal = SecurityContextHolder
//            .getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//        UserPrincipal userPrincipal = (UserPrincipal) principal;
//    }

}
