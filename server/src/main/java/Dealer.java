import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by yuruh on 21/11/16.
 */
public class Dealer implements IDealer
{
    private ArrayList<Card> cards;

    Dealer()
    {
        this.cards = new ArrayList<Card>();
        this.createDeck();
    }

    private void fillDeck(Card.Color color)
    {
        this.cards.add(new Card(Card.Type.SEVEN, color, 0, 0));
        this.cards.add(new Card(Card.Type.EIGHT, color, 0, 0));
        this.cards.add(new Card(Card.Type.NINE, color, 0, 14));
        this.cards.add(new Card(Card.Type.TEN, color, 10, 10));
        this.cards.add(new Card(Card.Type.JACK, color, 2, 20));
        this.cards.add(new Card(Card.Type.QUEEN, color, 3, 3));
        this.cards.add(new Card(Card.Type.KING, color, 4, 4));
        this.cards.add(new Card(Card.Type.ACE, color, 11, 11));
    }

    public void deal(Collection<IPlayer> players)
    {
        int size = cards.size();
        for (IPlayer player : players)
        {
            for (int j = 0; j < size / players.size(); j++)
            {
                int index = new Random().nextInt(this.cards.size());
                player.addCard(this.cards.get(index));
                this.cards.remove(index);
            }
        }
        this.createDeck();
    }

    private void createDeck()
    {
        this.fillDeck(Card.Color.SPADES);
        this.fillDeck(Card.Color.CLUBS);
        this.fillDeck(Card.Color.DIAMONDS);
        this.fillDeck(Card.Color.HEARTS);
    }
}
