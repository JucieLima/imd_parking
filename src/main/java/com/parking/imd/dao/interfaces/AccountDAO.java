package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Account;

public interface AccountDAO extends DAO {
    void insert(Account account);
    Account find(Account account);
    void  update(Account account);
    void delete(int idAccount);
}
