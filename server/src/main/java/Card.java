import java.util.TreeMap;

/**
 * Created by yuruh on 21/11/16.
 */
public class Card
{
    private Color   color;
    private Type    type;
    private int     highValue;
    private int     lowValue;
    private static TreeMap<Type, ServerData.Data.CardType> typeCorresp;
    private static TreeMap<Card.Color, ServerData.Data.CardColor> colorCorresp;
    private static TreeMap<ClientData.Data.CardColor, Color>  clientColorCorresp;
    private static TreeMap<ClientData.Data.CardType, Type> clientTypeCorresp;


    Card(Type type, Color color, int lowValue, int highValue)
    {
        this.type = type;
        this.color = color;
        this.highValue = highValue;
        this.lowValue = lowValue;

        this.typeCorresp = new TreeMap<Type, ServerData.Data.CardType>();
        this.colorCorresp = new TreeMap<Color, ServerData.Data.CardColor>();
        this.clientColorCorresp = new TreeMap<ClientData.Data.CardColor, Color>();
        this.clientTypeCorresp = new TreeMap<ClientData.Data.CardType, Type>();

        typeCorresp.put(Type.SEVEN, ServerData.Data.CardType.SEVEN);
        typeCorresp.put(Type.EIGHT, ServerData.Data.CardType.EIGHT);
        typeCorresp.put(Type.NINE, ServerData.Data.CardType.NINE);
        typeCorresp.put(Type.TEN, ServerData.Data.CardType.TEN);
        typeCorresp.put(Type.JACK, ServerData.Data.CardType.JACK);
        typeCorresp.put(Type.QUEEN, ServerData.Data.CardType.QUEEN);
        typeCorresp.put(Type.KING, ServerData.Data.CardType.KING);
        typeCorresp.put(Type.ACE, ServerData.Data.CardType.ACE);

        colorCorresp.put(Color.SPADES, ServerData.Data.CardColor.SPADES);
        colorCorresp.put(Color.DIAMONDS, ServerData.Data.CardColor.DIAMONDS);
        colorCorresp.put(Color.CLUBS, ServerData.Data.CardColor.CLUBS);
        colorCorresp.put(Color.HEARTS, ServerData.Data.CardColor.HEARTS);

        clientColorCorresp.put(ClientData.Data.CardColor.SPADES, Color.SPADES);
        clientColorCorresp.put(ClientData.Data.CardColor.HEARTS, Color.HEARTS);
        clientColorCorresp.put(ClientData.Data.CardColor.CLUBS, Color.CLUBS);
        clientColorCorresp.put(ClientData.Data.CardColor.DIAMONDS, Color.DIAMONDS);

        clientTypeCorresp.put(ClientData.Data.CardType.ACE, Type.ACE);
        clientTypeCorresp.put(ClientData.Data.CardType.KING, Type.KING);
        clientTypeCorresp.put(ClientData.Data.CardType.QUEEN, Type.QUEEN);
        clientTypeCorresp.put(ClientData.Data.CardType.JACK, Type.JACK);
        clientTypeCorresp.put(ClientData.Data.CardType.TEN, Type.TEN);
        clientTypeCorresp.put(ClientData.Data.CardType.NINE, Type.NINE);
        clientTypeCorresp.put(ClientData.Data.CardType.EIGHT, Type.EIGHT);
        clientTypeCorresp.put(ClientData.Data.CardType.SEVEN, Type.SEVEN);
    }

    public boolean equals(ClientData.Data.Card obj)
    {
        return convert(obj.getColor()) == this.color && convert(obj.getType()) == this.type;
    }

    public static Color convert(ClientData.Data.CardColor trump)
    {
        return clientColorCorresp.get(trump);
    }

    public static Type convert(ClientData.Data.CardType type)
    {
        return clientTypeCorresp.get(type);
    }


    public Color getColor()
    {
        return color;
    }

    public int getHighValue()
    {
        return highValue;
    }

    public boolean beats(Card leading, Color trump)
    {
        if (this.color == trump)
            return (leading.color != trump || this.highValue > leading.highValue);
        else
            return (leading.color != trump && this.lowValue > leading.lowValue);
    }

    public int getValue(Color trump)
    {
        if (this.color == trump)
            return this.highValue;
        else
            return this.lowValue;
    }

    public static ServerData.Data.CardColor convert(Color convert)
    {
        return colorCorresp.get(convert);
    }

    public enum Type
    {
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }

    public enum Color
    {
        SPADES,
        DIAMONDS,
        HEARTS,
        CLUBS
    }

    public ServerData.Data.Card.Builder toServerDataCard()
    {
        return (ServerData.Data.Card.newBuilder()
                .setType(typeCorresp.get(this.type))
                .setColor(colorCorresp.get(this.color)));
    }
}
