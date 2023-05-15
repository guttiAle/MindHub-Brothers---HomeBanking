package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String color, @RequestParam String type) {
        Client clientOwner = clientService.findByEmail(authentication.getName());
        String cardNumber = CardUtils.randomNumber();
        int cvv = CardUtils.randomCvv();
        int contDebit = 0;
        int contCredit = 0;
        List<String> colorCREDIT = new ArrayList<>();
        List<String> colorDEBIT = new ArrayList<>(); 

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
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type) , CardColor.valueOf(color), cardNumber, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardService.saveCard(newCard);
        } else if(type.equals("DEBIT") && (contDebit < 3) && !colorDEBIT.contains(color)) {
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardService.saveCard(newCard);
        } else {
            return new ResponseEntity<>("Already have 3 cards", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }



}


