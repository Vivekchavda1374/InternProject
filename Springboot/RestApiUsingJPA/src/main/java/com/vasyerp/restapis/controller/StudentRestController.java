package com.vasyerp.restapis.controller;

import com.vasyerp.restapis.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {
    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> theStudents = new ArrayList<>();

        theStudents.add(new Student("Poornima", "Patel","purnima@vasyerp.com"));
        theStudents.add(new Student("Mario", "Rossi","purnima@vasyerp.com"));
        theStudents.add(new Student("Mary", "Smith","purnima@vasyerp.com"));
        return theStudents;
    }

}
