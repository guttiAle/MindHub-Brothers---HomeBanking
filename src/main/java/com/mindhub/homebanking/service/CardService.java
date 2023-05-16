package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    void saveCard(Card card);
    List<CardDTO> getActiveCards(ClientDTO clientDTO);
    void deleteCard(String number);
    Card getCardByNumber(String number);
}
