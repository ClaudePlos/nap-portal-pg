package pl.kskowronski.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kskowronski.data.Role;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.UserRepository;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.global.NapUserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private NapUserRepo napUserRepo;

    @Autowired
    private SKService skService;

    public Optional<User> loggedUser = null;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            loggedUser = Optional.ofNullable(userRepo.findByUsername(username));
            loggedUser.get().setPassword(getMd5(loggedUser.get().getPassword()));
            if (loggedUser.get().getPrcDgKodEk().equals("EK04")) {// check, maybe he is in EK04
                loggedUser.get().setPassword("");
            }
        } catch (Exception ex){
            Optional<NapUser> napUser = napUserRepo.findByUsername(username);
            if (napUser.isPresent()) {
                loggedUser = userRepo.findById(napUser.get().getPrcId());
                loggedUser.get().setPassword(napUser.get().getPassword());
            }
        }

        if (loggedUser.get() == null) {
            throw new UsernameNotFoundException("Could not find user with this username and pass");
        }
        loggedUser.get().setRoles(Collections.singleton(Role.USER));

        // SUPERVISOR - we use this only when manager want print Pit for workers
        if (  skService.findSkForSupervisor(loggedUser.get().getPrcId()).size() > 0 ) {
            Set<Role> roles = new HashSet<>();
            loggedUser.get().getRoles().stream().forEach( item -> roles.add(item));
            roles.add(Role.SUPERVISOR);
            loggedUser.get().setRoles(roles);
        }

        // MANAGER - we use this for special report for them
        if (  skService.findSkForManager(loggedUser.get().getPrcId()).size() > 0 ) {
            Set<Role> roles = new HashSet<>();
            loggedUser.get().getRoles().stream().forEach( item -> roles.add(item));
            roles.add(Role.MANAGER);
            loggedUser.get().setRoles(roles);
        }

        if (loggedUser.get().getPrcId() == 115442 || loggedUser.get().getPrcId() == 279069  || loggedUser.get().getPrcId() == 340372 ) {
            Set<Role> roles = new HashSet<>();
            loggedUser.get().getRoles().stream().forEach( item -> roles.add(item));
            roles.add(Role.ADMIN);
            loggedUser.get().setRoles(roles);
        }

        if  ( loggedUser.get().getPrcId() == 101401 || loggedUser.get().getPrcId() == 142845) {
            Set<Role> roles = new HashSet<>();
            loggedUser.get().getRoles().stream().forEach( item -> roles.add(item));
            roles.add(Role.HR_MANAGER);
            loggedUser.get().setRoles(roles);
        }

        return new org.springframework.security.core.userdetails.User(loggedUser.get().getUsername(), loggedUser.get().getPassword(),
                getAuthorities(loggedUser.get()));
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());

    }


    private static String getMd5(String input) {
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }
    }

}
