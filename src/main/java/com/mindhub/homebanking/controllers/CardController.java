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
import java.util.ArrayList;
import java.util.List;
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
        List<String> colorCREDIT = new ArrayList<>();
        List<String> colorDEBIT = new ArrayList<>();

        String randomCVV = String.format("%03d", (int) (Math.random() * 899 + 100));
        if (color.isBlank() || type.isBlank()) {return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);}

        String randomNum = "";

        do {
            for (int i = 0; i < 4; i++) {
                String fourDigits = String.format("%04d", (int) (Math.random() * 10000));
                randomNum += fourDigits;
                if (i < 3) {
                    randomNum += "-";
                }
            }
        } while (cardRepository.findByNumber(randomNum) != null);

        for (Card i : clientOwner.getCards()){
            if (i.getType().equals(CardType.CREDIT)){
                contCredit++;
                colorCREDIT.add(i.getColor().name());
            } else if (i.getType().equals(CardType.DEBIT)){
                contDebit++;
                colorDEBIT.add(i.getColor().name());
            }
        }

        if(type.equals("CREDIT") && (contCredit < 3) && !colorCREDIT.contains(color)){
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type) , CardColor.valueOf(color), randomNum, Integer.parseInt(randomCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardRepository.save(newCard);
        } else if(type.equals("DEBIT") && (contDebit < 3) && !colorDEBIT.contains(color)) {
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type), CardColor.valueOf(color), randomNum, Integer.parseInt(randomCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardRepository.save(newCard);
        } else {
            return new ResponseEntity<>("Already have 3 cards", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }
}
