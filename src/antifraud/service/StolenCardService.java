package antifraud.service;

import antifraud.api.dto.StolenCardDTO;
import antifraud.api.mapper.StolenCardMapper;
import antifraud.domain.entity.StolenCard;
import antifraud.domain.exceptions.BadRequestException;
import antifraud.domain.exceptions.ConflictDataException;
import antifraud.domain.exceptions.NotFoundException;
import antifraud.domain.repository.StolenCardRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Service
public class StolenCardService {

   private StolenCardRep stolenCardRep;
   private StolenCardMapper stolenCardMapper;

    public StolenCard addStolenCard(StolenCardDTO stolenCardDTO) {
        String number = stolenCardDTO.getNumber();
        stolenCardCheck(number);
        if (stolenCardRep.findStolenCardByNumberIgnoreCase(number).isPresent()) {
            throw new ConflictDataException("Card is exists!");
        }
        return stolenCardRep.save(stolenCardMapper.toStolenCardFromStolenCardDTO(stolenCardDTO));
    }

    public void stolenCardCheck(String cardNumber) {
        int[] cardIntArray = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;
            if (num > 9) {
                num = num % 10 + num / 10;
            }
            cardIntArray[i] = num;
        }

        int sum = Arrays.stream(cardIntArray).sum();

        if (sum % 10 != 0) {
            throw new BadRequestException("Wrong card format!");
        }
    }

    public void deleteStolenCardByNumber(String number) {
        if (isValidCreditCardNumber(number)) {
            StolenCard stolenCard = stolenCardRep.findStolenCardByNumberIgnoreCase(number)
                    .orElseThrow(() -> new NotFoundException("Card not found"));
            stolenCardRep.delete(stolenCard);
        } else {
            throw new BadRequestException("Not valid data!");
        }
    }

    public List<StolenCard> getAllStolenCard() {
        List<StolenCard> result = new ArrayList<>();
        stolenCardRep.findAll().forEach(result::add);

        return result.stream().filter(a -> a.getNumber() != null).collect(Collectors.toList());
    }

    public boolean isValidCreditCardNumber(String cardNumber) {
        // int array for processing the cardNumber
        int[] cardIntArray = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;
            if (num > 9) {
                num = num % 10 + num / 10;
            }
            cardIntArray[i] = num;
        }

        int sum = addAllNumbers(cardIntArray);
        return sum % 10 == 0;
    }

    private int addAllNumbers(int[] array) {
        return Arrays.stream(array).sum();
    }
}
