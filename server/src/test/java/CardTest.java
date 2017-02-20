import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yuruh on 25/11/16.
 */
public class CardTest
{
    @Test
    public void equals() throws Exception
    {
        ClientData.Data data = ClientData.Data.newBuilder()
                .setCard(ClientData.Data.Card.newBuilder()
                        .setColor(ClientData.Data.CardColor.HEARTS)
                        .setType(ClientData.Data.CardType.JACK)
                        .build())
                .setPseudo("test")
                .setId(1)
                .build();

        Card card = new Card(Card.Type.JACK, Card.Color.HEARTS, 0, 0);
        assertEquals(card.equals(data.getCard()), true);
        card = new Card(Card.Type.ACE, Card.Color.HEARTS, 0, 0);
        assertEquals(card.equals(data.getCard()), false);
    }

}