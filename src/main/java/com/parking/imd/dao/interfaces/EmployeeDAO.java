package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Employee;

import java.util.List;

public interface EmployeeDAO extends DAO {
    Integer insert(Employee employee);
    Employee find(Employee employee);
    void  update(Employee employee);
    void delete(int idEmployee);
    List<Employee> list(int limit, int offset);
    Employee login(String email);
}
