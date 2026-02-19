package com.vasyerp.RestAPI.src.main.java.com.vasyerp.restapi.Service;


import com.vasyerp.RestAPI.src.main.java.com.vasyerp.restapi.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(int theId);

    Employee save(Employee theEmployee);

    void deleteById(int theId);

}