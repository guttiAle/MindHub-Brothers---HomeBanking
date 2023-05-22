package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public List<CardDTO> getActiveCards(ClientDTO clientDTO) {
        Set<CardDTO> cards = clientDTO.getCards();
        return cards.stream().filter(card1 -> card1.isStatus() == true).collect(toList());
    }

    @Override
    public void deleteCard(String number) {
        Card selectCard = cardRepository.findByNumber(number);
        selectCard.setStatus(false);
        cardRepository.save(selectCard);
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

}
