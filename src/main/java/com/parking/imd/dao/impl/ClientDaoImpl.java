package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.ClientDAO;
import com.parking.imd.domain.Account;
import com.parking.imd.domain.Client;
import com.parking.imd.domain.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl implements ClientDAO {

    Connection connection;
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Integer insert(Client client) {
        Timestamp now = new Timestamp(new java.util.Date().getTime());
        String sql = "INSERT INTO clients (name, CPF, created_at, updated_at) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, client.getNome());
            statement.setString(2, client.getCPF());
            statement.setTimestamp(3, now);
            statement.setTimestamp(4, now);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Client find(Client cLient) {
        return null;
    }

    @Override
    public void update(Client cLient) {

    }

    @Override
    public void delete(int idClient) {

    }

    public boolean checkClientCPF(String cpf) {
        String sql = "SELECT clients.CPF FROM clients WHERE CPF = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int getClientsCount() {
        String sql = "SELECT COUNT(*) AS total FROM clients ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return  0;
    }

    public List<Client> list(int limit, int offset) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name, CPF FROM clients ORDER BY created_at LIMIT ? OFFSET ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Client client = new Client();
                client.setIdClient(resultSet.getInt("id"));
                client.setNome(resultSet.getString("name"));
                client.setCPF(resultSet.getString("CPF"));
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public Account getAccount(Integer idClient) {
        Account account = null;
        String sql = "SELECT id, balance, client FROM account WHERE client = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idClient);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                account = new Account();
                account.setIdAccount(resultSet.getInt("id"));
                account.setBalance(resultSet.getDouble("balance"));
                account.setIdClient(idClient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    public List<Vehicle> getClientVehicles(Integer idClient) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT vehicles.id, licence_plate, type FROM vehicles " +
                "INNER JOIN vehicles_owners ON vehicles_owners.vehicle = vehicles.id " +
                "WHERE vehicles_owners.client = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idClient);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                var vehicle = new Vehicle();
                vehicle.setIdVehicle(resultSet.getInt("id"));
                vehicle.setLicencePlate(resultSet.getString("licence_plate"));
                vehicle.setType(resultSet.getInt("type"));
                vehicle.setTypeName(VehicleDaoImpl.getTypename(resultSet.getInt("type")));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }
}
