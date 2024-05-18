package dit.hua.distributedSystems.project.demo.rest;


import dit.hua.distributedSystems.project.demo.entity.Role;
import dit.hua.distributedSystems.project.demo.entity.MUser;
import dit.hua.distributedSystems.project.demo.service.RoleService;
import dit.hua.distributedSystems.project.demo.service.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private RoleService roleService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //Δήλωση μιας μεθόδου setup όπου θα κληθεί μόλις ξεκινήσει η εφαρμογή για την προσθήκη του admin και την προσθήκη των ρόλων στην βάση.
    @PostConstruct
    public void setup() {
        Role role3 = new Role();
        role3.setId(3);
        role3.setRole("ROLE_ADMIN");

        if (roleService.getRoles().isEmpty()) {
            Role role1 = new Role();
            role1.setId(1);
            role1.setRole("ROLE_FARMER");
            roleService.saveRole(role1);
            Role role2 = new Role();
            role2.setId(2);
            role2.setRole("ROLE_INSPECTOR");
            roleService.saveRole(role2);
            roleService.saveRole(role3);
        }

        if (!userService.findByRole(role3)) {
            MUser adminUser = new MUser();

            adminUser.setUsername("admin");
            adminUser.setPassword("pavlosnikolopoulos44");
            String passencode=passwordEncoder.encode(adminUser.getPassword());
            adminUser.setPassword(passencode);
            adminUser.setFirstName("Pavlos");
            adminUser.setLastName("Nikolopoulos");
            adminUser.setEmail("pavlosnikolopoulos@gmail.com");
            adminUser.setPhone("6942553328");
            adminUser.setAddress("Kipon 44");
            adminUser.setRole(role3);
            userService.saveUser(adminUser);
        }
    }

    //Μέθοδος για την προβολή των στοιχείων όλων των χρηστών.
    @GetMapping("")
    public List<MUser> showUsers(){
        return userService.getUsers();
    }

    //Μέθοδος για την αποθήκευση ενός χρήστη στην βάση.
    @PostMapping("/new")
    public ResponseEntity<String> saveUser(@RequestBody MUser user) {
        String passencode=passwordEncoder.encode(user.getPassword());
        user.setPassword(passencode);
        String usrnm=user.getUsername();
        String email=user.getEmail();

        if (userService.findByUsername(usrnm)) { //Αν ο χρήστης υπάρχει ήδη ελέγχοντας το email και το username τότε ο χρήστης δεν μπορεί να
            //αποθηκευτεί στην βάση αλλιώς αποθηκεύεται.

            return new ResponseEntity<>("User already exists! Change Username", HttpStatus.BAD_REQUEST);
        } else if (userService.findByEmail(email)) {
            return new ResponseEntity<>("User already exists! Change Email", HttpStatus.BAD_REQUEST);
        }
        else{
            userService.saveUser(user);
            return new ResponseEntity<>("User has been saved successfully!", HttpStatus.OK);
        }
    }

    //Μέθοδος για την διαγραφή ενός χρήστη από την βάση.
    @DeleteMapping("{user_id}")
    public List<MUser> deleteUser(@PathVariable Integer user_id){
        userService.deleteUser(user_id);
        return userService.getUsers();
    }

    @PutMapping("{user_id}")
    public ResponseEntity<String> editUser(@PathVariable Integer user_id, @RequestBody MUser modifiedUser){

        MUser user = userService.getUser(user_id);

        if(user==null){

            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        //Τροποποίηση στοιχείων ενός χρήστη. Μόνο τα πεδία address,phone και email μπορούν να τροποποιηθούν τα υπόλοιπα όχι. Σε περίπτωση που ένα πεδίο δεν μπορεί να
        //τροποποιηθεί εμφανίζεται κατάλληλο μήνυμα.
        if(modifiedUser.getAddress()!=null){
            user.setAddress(modifiedUser.getAddress());
            userService.saveUser(user);
        }
        if(modifiedUser.getPhone()!=null){
            user.setPhone(modifiedUser.getPhone());
            userService.saveUser(user);
        }
        if(modifiedUser.getEmail()!=null && !modifiedUser.getEmail().equals(user.getEmail())){
            // Check if the new email already exists
            if (userService.findByEmail(modifiedUser.getEmail())) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
            user.setEmail(modifiedUser.getEmail());
            userService.saveUser(user);
        }
        if(modifiedUser.getUsername()!=null){
            return new ResponseEntity<>("Cannot update username", HttpStatus.BAD_REQUEST);
        }
        if(modifiedUser.getPassword()!=null){
            return new ResponseEntity<>("Cannot update password", HttpStatus.BAD_REQUEST);
        }
        if(modifiedUser.getFirstName()!=null){
            return new ResponseEntity<>("Cannot update first name", HttpStatus.BAD_REQUEST);
        }
        if(modifiedUser.getLastName()!=null){
            return new ResponseEntity<>("Cannot update last name", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    //Μέθοδος για την ανάθεση ενός ρόλου σε έναν χρήστη.
    @PostMapping("/role/{user_id}/{role_id}")
    public String assignRole(@PathVariable Integer user_id, @PathVariable Integer role_id) {
        MUser user = userService.getUser(user_id);
        Role role = roleService.getRole(role_id);
        user.setRole(role); //Κλήση της μεθόδου setRole στην κλάση Muser και αποθήκευση του τροποποιημένου(αφού προστέθηκε σε αυτόν ρόλος) χρήστη.
        userService.saveUser(user);
        return "Role: " + role.getRole() + " has been given to user: " + user.getEmail(); //Εμφάνιση κατάλληλου μηνύματος.
    }

}
