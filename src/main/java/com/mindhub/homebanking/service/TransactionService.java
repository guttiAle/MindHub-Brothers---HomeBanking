package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    public List<Transaction> getTransactionsByAccountAndDate (Account account, LocalDateTime start, LocalDateTime end);
    public List<Transaction> getTransactionsByAccount (Account account);
}
