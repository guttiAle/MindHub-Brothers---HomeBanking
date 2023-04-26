package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
public class CardController {
    @Autowired
    private ClientRepository repository;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String color, @RequestParam String type) {


        Client clientOwner = repository.findByEmail(authentication.getName());
        int contDebit = 0;
        int contCredit = 0;
        String randomNum;
        String randomCVV = String.format("%03d", (int) (Math.random() * 1000));
//        do {
//            randomNum = String.format("%016d", (long) (Math.random() * 1000000000000000L));
//        } while (cardRepository.findByNumber(randomNum) != null);

        randomNum = String.format("%016d", (long) (Math.random() * 1000000000000000L));
        for (Card i : clientOwner.getCards()){
            if (i.getType().equals(CardType.CREDIT)){
                contCredit++;
            } else if (i.getType().equals(CardType.DEBIT)){
                contDebit++;
            }
        }
        if(type.equals("CREDIT")){
            if (contCredit < 3){
                Card newCard = new Card(clientOwner.getFirstName() + clientOwner.getLastName(), CardType.valueOf(type) , CardColor.valueOf(color), Long.parseLong(randomNum), Integer.parseInt(randomCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
                clientOwner.addCardHolder(newCard);
                cardRepository.save(newCard);
            } else {
                return new ResponseEntity<>("Already have 3 cards", HttpStatus.FORBIDDEN);
            }
        }

        if(type.equals("DEBIT")) {
            if (contDebit < 3) {
                Card newCard = new Card(clientOwner.getFirstName() + clientOwner.getLastName(), CardType.valueOf(type), CardColor.valueOf(color), Long.parseLong(randomNum), Integer.parseInt(randomCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
                clientOwner.addCardHolder(newCard);
                cardRepository.save(newCard);
            } else {
                return new ResponseEntity<>("Already have 3 cards", HttpStatus.FORBIDDEN);
            }
        }

            return new ResponseEntity<>( HttpStatus.CREATED);
    }
}
