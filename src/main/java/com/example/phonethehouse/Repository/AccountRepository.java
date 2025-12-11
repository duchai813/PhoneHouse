package com.example.phonethehouse.Repository;

import com.example.phonethehouse.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByUsernameAndPassword(String username, String password);
}
