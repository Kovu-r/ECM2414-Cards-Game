// Card class, contains card struct
// A card consists of a card denomination, dont need a setter a cards value is set when the game starts

// dependencies
import ExceptionWrapper.*;


public class Card {

    private final int CardDenomination;


    // constructor
    public Card(int SerialisedCardDenomination) {

        // try setting card value

        if (SerialisedCardDenomination < 0) {
            // negative, throw exception, card denominator cannot be negative
            Exception NegetiveException = new NumberFormatException("Denomination number is negative");
            ExceptionWrapper.HandleException.NegetiveDenominationException(NegetiveException, SerialisedCardDenomination);

        }

        CardDenomination = SerialisedCardDenomination;


    }




    public int GetCardDenomination() {
        return CardDenomination;
    }


}
