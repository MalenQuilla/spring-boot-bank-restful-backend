package malenquillaa.java.spring.bank.services;

import jakarta.transaction.Transactional;
import malenquillaa.java.spring.bank.models.*;
import malenquillaa.java.spring.bank.models.payloads.requests.LoginRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.SignupRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.UpdatePasswordRequest;
import malenquillaa.java.spring.bank.models.payloads.responses.JwtResponse;
import malenquillaa.java.spring.bank.repositories.CustomerAccountRepository;
import malenquillaa.java.spring.bank.repositories.RoleRepository;
import malenquillaa.java.spring.bank.repositories.UserRepository;
import malenquillaa.java.spring.bank.services.security.jwt.JwtUtils;
import malenquillaa.java.spring.bank.services.security.services.UserDetailsImpl;
import malenquillaa.java.spring.bank.services.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class AccountService {

    UserDetailsServiceImpl userDetailsServiceImpl;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    CustomerAccountRepository customerAccountRepository;
    JwtUtils jwtUtils;
    PasswordEncoder passwordEncoder;
    AccountNumberService accountNumberService;

    @Autowired
    AccountService(UserRepository userRepository,
                   CustomerAccountRepository customerAccountRepository,
                   JwtUtils jwtUtils,
                   PasswordEncoder passwordEncoder,
                   RoleRepository roleRepository,
                   AuthenticationManager authenticationManager,
                   UserDetailsServiceImpl userDetailsServiceImpl,
                   AccountNumberService accountNumberService) {
        this.userRepository = userRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.accountNumberService = accountNumberService;
    }

    public void createAccount(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            throw new RuntimeException("Username already exists");


        if (userRepository.existsByEmail(signupRequest.getEmail()))
            throw new RuntimeException("Email already exists");

//        Create new User's account
        User newUser = new User(
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getDOB(),
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                EStatus.STATUS_ACTIVE
        );

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);

                        break;
                    case "staff":
                        Role modRole = roleRepository.findByName(ERole.ROLE_STAFF)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(userRole);
                }
            });
        }
        newUser.setRoles(roles);
        userRepository.save(newUser);
        createAccountForRole(newUser);
    }

    private void createAccountForRole(User user) {
        Set<Role> roles = user.getRoles();

        roles.forEach(role -> {
            switch (role.getName()) {
                case ERole.ROLE_CUSTOMER:
                    CustomerAccount customerAccount = new CustomerAccount(user, accountNumberService.generate(user));
                    customerAccountRepository.save(customerAccount);
                    break;
                case ERole.ROLE_STAFF, ERole.ROLE_ADMIN:
                    break;
            }
        });
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public void updatePassword(UpdatePasswordRequest updateRequest) {
        String password = updateRequest.getOldPassword();
        String newPassword = updateRequest.getNewPassword();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        String username = user.getUsername();
        String currentPassword = user.getPassword();

        if (!passwordEncoder.matches(password, currentPassword))
            throw new RuntimeException("Password doesn't match");
        userRepository.updatePasswordByUsername(username, passwordEncoder.encode(newPassword));
    }

    public void updateEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        String username = user.getUsername();

        userRepository.updateEmailByUsername(username, email);
    }
}
