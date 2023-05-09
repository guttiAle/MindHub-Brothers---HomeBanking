package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(loan1 -> new LoanDTO(loan1)).collect(toList());
    }
    @Override
    public Loan findById(long id){
        return loanRepository.findById(id);
    }

//    public Account findByNumber(String account) {
//        return accountRepository.findByNumber(account);
//    }
}
