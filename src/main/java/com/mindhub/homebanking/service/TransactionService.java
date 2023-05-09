package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
