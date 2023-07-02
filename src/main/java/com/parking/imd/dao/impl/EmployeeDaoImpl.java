package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.EmployeeDAO;
import com.parking.imd.domain.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDAO {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void delete(Object object) {

    }

    public Employee login(String email){
        String sql = "SELECT users.*, employees.email, employees.password FROM users " +
                "INNER JOIN employees ON users.id = employees.id_user " +
                "WHERE employees.email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Employee result = new Employee();
                result.setIdEmployee(resultSet.getInt("id"));
                result.setNome(resultSet.getString("name"));
                result.setCPF(resultSet.getString("cpf"));
                result.setEmail(resultSet.getString("email"));
                result.setPassword(resultSet.getString("password"));
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Employee find(Employee employee){
        String sql = "SELECT users.*, employees.email, employees.password FROM persons " +
                "INNER JOIN employees ON users.id = employees.id_person " +
                "WHERE users.id = ? OR users.name LIKE ? OR users.cpf LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employee.getIdEmployee());
            statement.setString(2, employee.getNome());
            statement.setString(3, employee.getCPF());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Employee result = new Employee();
                result.setIdEmployee(resultSet.getInt("id"));
                result.setNome(resultSet.getString("nome"));
                result.setCPF(resultSet.getString("cpf"));
                result.setEmail(resultSet.getString("email"));
                result.setPassword(resultSet.getString("password"));
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Integer insert(Employee employee) {
        return null;
    }
    @Override
    public void update(Employee employee) {

    }

    @Override
    public void delete(int idEmployee) {

    }

    @Override
    public List<Employee> list(int limit, int offset) {
        return null;
    }
}
