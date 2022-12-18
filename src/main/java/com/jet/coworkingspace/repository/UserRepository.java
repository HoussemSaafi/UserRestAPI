package com.jet.coworkingspace.repository;

import com.jet.coworkingspace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u.id FROM user u where u.lastname = :lastname")
    Long findByLastname(@Param("lastname") String lastname);
}

