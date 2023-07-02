package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Account;
import com.parking.imd.domain.Client;
import com.parking.imd.domain.Vehicle;

import java.util.List;

public interface ClientDAO extends DAO {
    Integer insert(Client client);
    Client find(Client cLient);
    void  update(Client cLient);
    void delete(int idClient);
    List<Client> list(int limit, int offset);
    int getClientsCount();
    Account getAccount(Integer idClient);
    List<Vehicle> getClientVehicles(Integer idClient);
    boolean checkClientCPF(String text);
}
