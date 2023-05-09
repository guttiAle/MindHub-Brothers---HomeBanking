package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {
    void saveAccount(Account account);
    Account findByNumber(String account);
    List<AccountDTO> getAccounts();
    AccountDTO getAccount(Long id);

}
