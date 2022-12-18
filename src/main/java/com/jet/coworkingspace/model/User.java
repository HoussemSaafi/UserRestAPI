package com.jet.coworkingspace.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import javax.persistence.*;


@Data
@Table(name = "user")
@Entity(name ="user")
public class User {

    //Attributes:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "email")
    private String email;

    @Transient
    @JsonProperty("permissions")
    private AssociatedPermissions permissions;


    //Methods
    public User(String name, String lastname, String birthdate, String email, AssociatedPermissions ap){
        this.firstname = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
        this.permissions = ap;
    }

    //Default Constructor
    public User(){
        this.firstname = "name";
        this.lastname = "lastname";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
