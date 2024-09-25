package com.example.SpringSecurity.controller;


import com.example.SpringSecurity.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {


    public List<Student> students= new ArrayList<>(List.of(
            new Student(1,"navin",60),
            new Student(2,"mallik",60)
            ));

    @GetMapping("/students")
public List<Student>getstudents(){
        return students;

}

@PostMapping("/students")
    public Student addstudent(Student student){

        students.add(student);
        return student;
}
}
