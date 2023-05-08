package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

public interface AccountService {
    void saveAccount(Account account);
    Account findByNumber(String account);
}
