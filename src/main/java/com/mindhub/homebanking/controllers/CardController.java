package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
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
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type) , CardColor.valueOf(color), randomNumber(), randomCvv(), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardService.saveCard(newCard);
        } else if(type.equals("DEBIT") && (contDebit < 3) && !colorDEBIT.contains(color)) {
            Card newCard = new Card(clientOwner.getFirstName() + " " + clientOwner.getLastName(), CardType.valueOf(type), CardColor.valueOf(color), randomNumber(), randomCvv(), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
            clientOwner.addCardHolder(newCard);
            cardService.saveCard(newCard);
        } else {
            return new ResponseEntity<>("Already have 3 cards", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

//    Number generator methods
    public int randomCvv() {
        int num = (int) ((Math.random() * 899) + 100);
        return num;
    }

    public  String randomNumber() {
        Random randomNum = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int num = randomNum.nextInt(10000);
            sb.append(String.format("%04d", num));
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

}


