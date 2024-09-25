package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.model.Users;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends  JpaRepository<Users,Integer>{

    Users findByUsername(String username); //this is will generate automatically a query based on method convention i.e findBy where it looks for the entity followed by filed with intial captial i.e Username where it looks for the filed username in the Users as we passed Users in Jparepository stuff.
}
