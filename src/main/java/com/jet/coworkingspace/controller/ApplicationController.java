package com.jet.coworkingspace.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jet.coworkingspace.model.AssociatedPermissions;
import com.jet.coworkingspace.model.Permissions;
import com.jet.coworkingspace.model.User;
import com.jet.coworkingspace.repository.UserRepository;
import com.jet.coworkingspace.repository.AssociatedPermissionsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("restapi")
@RequestMapping("restapi")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    //attributes
    @Autowired
    UserRepository userRepository;

    @Autowired
    AssociatedPermissionsRepository associatedPermissionsRepository;

    //methods
    //add a new user
    @PostMapping("users")
    public ResponseEntity<User> create(@RequestBody User newUser) {
        AssociatedPermissions ap = new AssociatedPermissions();
        ap.setPermission(newUser.getPermissions().getPermission());
        User user = userRepository.save(newUser);
        ap.setUserId(user.getId());
        ap.setGrantedAt(LocalDateTime.now());
        associatedPermissionsRepository.save(ap);
        if (user == null) {
            System.out.println("user is null!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    //Show all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> showUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Delete user by id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Optional<User>> deleteUser(@PathVariable(value = "id") Long id) {
        Optional<User> deletedUser = userRepository.findById(id);
        if (deletedUser.isPresent()) {
            System.out.println(deletedUser.get());
            userRepository.deleteById(id);
            return new ResponseEntity<>(deletedUser, HttpStatus.OK);
        } else {
            System.out.printf("No user found with id %d%n", id);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Show user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> showUserById(@PathVariable(value = "id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            System.out.println(user.get());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            System.out.printf("No user found with id %d%n", id);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Show user by family name
    @GetMapping("/users/show/{lastname}")
    public ResponseEntity<Optional<User>> showUserByFamilyName(@PathVariable(value = "lastname") String lastname) {
        Long id = userRepository.findByLastname(lastname);
        return this.showUserById(id);
        //500
    }

    //Show active user
    @GetMapping("/users/active")
    public ResponseEntity<User> showActiveUser() {
        List<User> users = userRepository.findAll();
        User activeUser = users.get(users.size() - 1);
        return new ResponseEntity<>(activeUser, HttpStatus.OK);
    }

    //Grant permission to user
    @PutMapping("/users/grant/{id}")
    public ResponseEntity<AssociatedPermissions> grantPermission(
            @PathVariable(value = "id") Long userId,
            @RequestBody Map<String, Permissions> json) throws ResourceNotFoundException {

        String val = new String();
        if(json.get("permission").name().equals("Admin")){
            val = new String("0");
        }
        else if(json.get("permission").name().equals("User")){
           val = new String("1");
        }
        else{
            val = new String("100");
        }
        Long id = associatedPermissionsRepository.findPermissionByUID(userId,val);
        System.out.println(id);
        if(id==null) {
            AssociatedPermissions newAp = new AssociatedPermissions(userId,json.get("permission"),LocalDateTime.now());
            associatedPermissionsRepository.save(newAp);
            return ResponseEntity.ok(newAp);
        }
        else {
            System.out.println("Permission Already Granted.");
            return ResponseEntity.ok(null);
        }
    }


    //revoke permission from a user
    @PutMapping("/users/revoke/{id}")
    public ResponseEntity<AssociatedPermissions> revokePermission(
            @PathVariable(value = "id") Long userId,
            @RequestBody Map<String, Permissions> json) throws ResourceNotFoundException {
        String val = new String();
        if(json.get("permission").name().equals("Admin")){
            val = "0";
        }
        else if(json.get("permission").name().equals("User")){
            val = "1";
        }
        else{}
        Long id = associatedPermissionsRepository.findPermissionByUID(userId,val);
        if(id==null){
            return ResponseEntity.ok(null);
        }
        else{
        AssociatedPermissions ap = associatedPermissionsRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("associated P not found on :: " + userId));
                System.out.println("Permission to delete found");
                associatedPermissionsRepository.deleteById(ap.getId());
                return ResponseEntity.ok(ap);
            }
        }

        //All other routes:
    @RequestMapping(method = RequestMethod.GET, value = {"/**"})
    public String notFoundError(){
        return "404 NOT FOUND, PLEASE MAKE SURE TO USE THE RIGHT ROUTE";
        }
    }