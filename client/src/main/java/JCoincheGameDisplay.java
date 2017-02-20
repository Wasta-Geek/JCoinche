import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;


/**
 *
 * Created by wasta-geek on 15/11/16.
 * Project : JCoinche.
 */
class JCoincheGameDisplay implements IDisplay, Screen {
    private SpriteBatch _batch;
    private Stage _stage;
    private Sprite _background;
    private java.util.Vector<Table> _players;
    private java.util.Vector<ServerData.Data.Card> _hand;
    private java.util.Vector<ServerData.Data.Card> _playedCard;
    private EnumMap<ServerData.Data.CardColor, String> _mapCardColor;
    private EnumMap<ServerData.Data.CardType, String> _mapCardValue;
    private EnumMap<ServerData.Data.CardColor, ClientData.Data.CardColor> _serverToClientColor;
    private EnumMap<ServerData.Data.CardType, ClientData.Data.CardType> _serverToClientType;
    private Label _yourTurnLabel;
    private AssetManager _assetManager;
    private boolean _haveLoaded;
    private JCoincheBidManager _bidManager;
    private boolean _isRendering;
    private boolean _haveAddCard;

    JCoincheGameDisplay() {
    }

    void setInput() {
        Gdx.input.setInputProcessor(this._stage);
        this._isRendering = true;
    }

    private void fillMapCard() {
        this._mapCardColor = new EnumMap<ServerData.Data.CardColor, String>(ServerData.Data.CardColor.class);
        this._mapCardValue = new EnumMap<ServerData.Data.CardType, String>(ServerData.Data.CardType.class);
        this._serverToClientColor = new EnumMap<ServerData.Data.CardColor, ClientData.Data.CardColor>(ServerData.Data.CardColor.class);
        this._serverToClientType = new EnumMap<ServerData.Data.CardType, ClientData.Data.CardType>(ServerData.Data.CardType.class);

        // map of correspondig ServerData / clientData

        this._serverToClientColor.put(ServerData.Data.CardColor.HEARTS, ClientData.Data.CardColor.HEARTS);
        this._serverToClientColor.put(ServerData.Data.CardColor.SPADES, ClientData.Data.CardColor.SPADES);
        this._serverToClientColor.put(ServerData.Data.CardColor.CLUBS, ClientData.Data.CardColor.CLUBS);
        this._serverToClientColor.put(ServerData.Data.CardColor.DIAMONDS, ClientData.Data.CardColor.DIAMONDS);

        this._serverToClientType.put(ServerData.Data.CardType.SEVEN, ClientData.Data.CardType.SEVEN);
        this._serverToClientType.put(ServerData.Data.CardType.EIGHT, ClientData.Data.CardType.EIGHT);
        this._serverToClientType.put(ServerData.Data.CardType.NINE, ClientData.Data.CardType.NINE);
        this._serverToClientType.put(ServerData.Data.CardType.TEN, ClientData.Data.CardType.TEN);
        this._serverToClientType.put(ServerData.Data.CardType.JACK, ClientData.Data.CardType.JACK);
        this._serverToClientType.put(ServerData.Data.CardType.QUEEN, ClientData.Data.CardType.QUEEN);
        this._serverToClientType.put(ServerData.Data.CardType.KING, ClientData.Data.CardType.KING);
        this._serverToClientType.put(ServerData.Data.CardType.ACE, ClientData.Data.CardType.ACE);

        //map of corresponding enum / string
        this._mapCardColor.put(ServerData.Data.CardColor.CLUBS, "clubs");
        this._mapCardColor.put(ServerData.Data.CardColor.DIAMONDS, "diamonds");
        this._mapCardColor.put(ServerData.Data.CardColor.HEARTS, "hearts");
        this._mapCardColor.put(ServerData.Data.CardColor.SPADES, "spades");

        this._mapCardValue.put(ServerData.Data.CardType.SEVEN, "7");
        this._mapCardValue.put(ServerData.Data.CardType.EIGHT, "8");
        this._mapCardValue.put(ServerData.Data.CardType.NINE, "9");
        this._mapCardValue.put(ServerData.Data.CardType.TEN, "10");
        this._mapCardValue.put(ServerData.Data.CardType.JACK, "jack");
        this._mapCardValue.put(ServerData.Data.CardType.QUEEN, "queen");
        this._mapCardValue.put(ServerData.Data.CardType.KING, "king");
        this._mapCardValue.put(ServerData.Data.CardType.ACE, "ace");
    }

    public void init() {
        this._isRendering = false;
        this._batch = new SpriteBatch();
        this._stage = new myGdxStage();

        Texture texture = new Texture(Gdx.files.internal("assets/tapis-belotte.jpeg"));
        this._background = JCoincheEvents.createScaledSprite(texture);
        this._players = new java.util.Vector<Table>();
        this._hand = new java.util.Vector<ServerData.Data.Card>();
        this._playedCard = new java.util.Vector<ServerData.Data.Card>();
        this._assetManager = new AssetManager();
        this._haveLoaded = false;
        this._bidManager = new JCoincheBidManager(this._stage);

        // FAIRE PROGRESS BAR JOUEUR CONNECTES

        this.fillMapCard();
        float offsetX = 0.06f;
        float offsetY = 0.13f;
        float tableWidth = Gdx.graphics.getWidth() * 0.6f;
        float tableHeight = Gdx.graphics.getHeight() * 0.18f;


        // Bottom player
        Table _tableBottom = new Table();
        _tableBottom.setSize(tableWidth, tableHeight);
        _tableBottom.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * offsetY, Align.center);

        //Left Player
        Table _tableLeft = new Table();
        _tableLeft.setSize(tableWidth, tableHeight);
        _tableLeft.setTransform(true);
        _tableLeft.setPosition(tableWidth / 2, tableHeight / 2);
        _tableLeft.setRotation(270);
        _tableLeft.setPosition(Gdx.graphics.getWidth() * offsetX, Gdx.graphics.getHeight() / 2 + tableWidth / 2);

        //Front Player
        Table _tableFront = new Table();
        _tableFront.setSize(tableWidth, tableHeight);
        _tableFront.setTransform(true);
        _tableFront.setPosition(tableWidth / 2, tableHeight / 2);
        _tableFront.setRotation(180);
        _tableFront.setPosition(Gdx.graphics.getWidth() / 2 + tableWidth, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * offsetY + tableHeight, Align.center);

        //Right Player
        Table _tableRight = new Table();
        _tableRight.setSize(tableWidth, tableHeight);
        _tableRight.setTransform(true);
        _tableRight.setPosition(tableWidth / 2, tableHeight / 2);
        _tableRight.setRotation(90);
        _tableRight.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * offsetX, Gdx.graphics.getHeight() / 2 - tableWidth / 2);

        this._players.add(0, _tableBottom);
        this._players.add(1, _tableLeft);
        this._players.add(2, _tableFront);
        this._players.add(3, _tableRight);

        this._stage.addActor(this._players.elementAt(0));
        this._stage.addActor(this._players.elementAt(1));
        this._stage.addActor(this._players.elementAt(2));
        this._stage.addActor(this._players.elementAt(3));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/police.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 20;
        Label.LabelStyle labelStyle = new Label.LabelStyle(generator.generateFont(parameter), Color.BLACK);
        this._yourTurnLabel = new Label("It's your turn !", labelStyle);
        this._yourTurnLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.25f, Align.center);
        this._yourTurnLabel.setVisible(false);

        this._stage.addActor(this._yourTurnLabel);
        JCoincheHUD.getInstance().addElementsToStage(this._stage);
    }

    public void show() {
    }

    private ClientData.Data.Card.Builder cardToSend(String name) {
        ClientData.Data.CardColor color = null;
        ClientData.Data.CardType type = null;
        Set<ServerData.Data.CardColor> keysColor = this._mapCardColor.keySet();
        Set<ServerData.Data.CardType> keysValues = this._mapCardValue.keySet();

        for (ServerData.Data.CardColor keyColor : keysColor) {
            if (name.contains(this._mapCardColor.get(keyColor))) {
                color = this._serverToClientColor.get(keyColor);
            }
        }
        for (ServerData.Data.CardType keyValue : keysValues) {
            if (name.contains(this._mapCardValue.get(keyValue))) {
                type = this._serverToClientType.get(keyValue);
            }
        }
        return ClientData.Data.Card.newBuilder().setColor(color).setType(type);
    }

    private void loadAllCards() {
        Enumeration<ServerData.Data.Card> cards = this._hand.elements();
        final float widthCard = this._players.elementAt(0).getWidth() / 8;
        final float heightCard = this._players.elementAt(0).getHeight();
        while (cards.hasMoreElements()) {
//            System.out.println("OUAI JE LOAD UNE CARTE");
            final ServerData.Data.Card card = cards.nextElement();
            String path = "assets/cards/" + this._mapCardValue.get(card.getType()) + "_of_" + this._mapCardColor.get(card.getColor()) + ".png";
            this._assetManager.load(path, Texture.class);
            this._assetManager.load("assets/cards/backHeartstone.png", Texture.class);
            this._assetManager.load("assets/cards/backYugioh.png", Texture.class);
            this._assetManager.load("assets/cards/backMagic.png", Texture.class);
            this._assetManager.finishLoading();

            Button _leftPlayerCard = new ImageButton(JCoincheEvents.drawableFromTexture(this._assetManager.get("assets/cards/backHeartstone.png", Texture.class)));
            ImageButton _frontPlayerCard = new ImageButton(JCoincheEvents.drawableFromTexture(this._assetManager.get("assets/cards/backYugioh.png", Texture.class)));
            ImageButton _rightPlayerCard = new ImageButton(JCoincheEvents.drawableFromTexture(this._assetManager.get("assets/cards/backMagic.png", Texture.class)));
            final ImageButton myCard = new ImageButton(JCoincheEvents.drawableFromTexture(this._assetManager.get(path, Texture.class)));
            myCard.setDisabled(true);
            myCard.setName(this._mapCardValue.get(card.getType()) + " " + this._mapCardColor.get(card.getColor()));
            myCard.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (getTapCount() == 2 && !myCard.isDisabled()) {
                        ClientData.Data.Card.Builder builderCard = cardToSend(myCard.getName());
                        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                                .setId(5)
                                .setCard(builderCard)
                                .build());
                        _players.elementAt(0).getCell(myCard).clearActor();

                        placeACard(card);
                        myCard.clear();
                        myCard.remove();
                        endGameTurn();
                    }
                    super.clicked(event, x, y);
                }
            });

            this._players.elementAt(0).add(myCard).height(heightCard).width(widthCard).align(Align.bottom);
            this._players.elementAt(1).add(_leftPlayerCard).height(heightCard).width(widthCard).align(Align.bottom);
            this._players.elementAt(2).add(_frontPlayerCard).height(heightCard).width(widthCard).align(Align.bottom);
            this._players.elementAt(3).add(_rightPlayerCard).height(heightCard).width(widthCard).align(Align.bottom);
        }
        this._haveLoaded = true;
    }

    public void render(float v) {
        if (this._isRendering)
            this._stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        System.out.println("HAVE LOAD" + this._haveLoaded);
//        System.out.println("SIZE : " + this._hand.size());
        if (!this._haveLoaded && this._hand.size() == 8)
            this.loadAllCards();
        if (this._haveAddCard)
            this.displayPlayedCard();
        this._batch.begin();
        _background.draw(this._batch);
        this._batch.end();
        this._stage.act();
        try {
            this._stage.draw();
        } catch (Exception error) {
            this._stage.getBatch().end();
            System.out.println("An error occured inside LibGdx");
        }
    }

    public void resize(int width, int height) {
        if (this._isRendering)
            this._stage.getViewport().update(width, height, true);
    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }


    void reset() {
        this._isRendering = false;
        for (Actor actor : this._stage.getActors())
            actor.remove();
        this._playedCard.clear();
        this._playedCard.removeAllElements();
        this._stage.clear();
        this._playedCard.removeAllElements();
        this._hand.clear();
        this._hand = new java.util.Vector<ServerData.Data.Card>();
        this._haveLoaded = false;
        this._players.elementAt(0).clear();
        this._players.elementAt(1).clear();
        this._players.elementAt(2).clear();
        this._players.elementAt(3).clear();
        this._stage.addActor(this._players.elementAt(0));
        this._stage.addActor(this._players.elementAt(1));
        this._stage.addActor(this._players.elementAt(2));
        this._stage.addActor(this._players.elementAt(3));
        this._stage.addActor(this._yourTurnLabel);
        this._bidManager.addElements();
        JCoincheHUD.getInstance().addElementsToStage(this._stage);
        this._isRendering = true;
    }

    public void dispose() {
        this._batch.dispose();
    }


    void displayHand(ServerData.Data.Card card) {
        this._hand.add(card);
    }

    void displayBid(ServerData.Data.Bid bid) {
        this._bidManager.beginBidTurn(bid.getMin(), bid.getMax());
    }

    private void endGameTurn() {
        this._yourTurnLabel.setVisible(false);
        Array<Cell> cells = this._players.elementAt(0).getCells();
        int counter = 0;
        boolean haveErrase;

        while (counter < cells.size) {
            haveErrase = false;
            for (int iter = 0; iter < cells.size; iter++)
                if (cells.get(iter).getActor() != null)
                    ((ImageButton) cells.get(iter).getActor()).setDisabled(true);
                else {
                    cells.removeIndex(iter);
                    haveErrase = true;
                }
            if (haveErrase)
                counter = 0;
            else
                counter++;
        }
    }

    void launchGameTurn(List<ServerData.Data.Card> cardList) {
        this._yourTurnLabel.setVisible(true);
        Array<Cell> cells = this._players.elementAt(0).getCells();
        boolean test;
        for (Cell cell : cells) {
            test = false;
            for (ServerData.Data.Card card : cardList)
                if (cell.getActor().getName().contains(this._mapCardColor.get(card.getColor())) && cell.getActor().getName().contains(this._mapCardValue.get(card.getType()))) {
                    test = true;
                    ((ImageButton) cell.getActor()).setDisabled(false);
                }
            if (!test)
                ((ImageButton) cell.getActor()).setDisabled(true);
        }
    }


    void placeACard(ServerData.Data.Card card) {
        this._playedCard.add(card);
        this._haveAddCard = true;
    }

    void endOfPli(ServerData.Data.Score score) {
        JCoincheHUD.getInstance().highLightWinner(score.getMyScore(), score.getEnemyScore());
        TimeUtils   time = new TimeUtils();
        long startTime = time.millis();
        Array<Actor> actors = this._stage.getActors();
        int counter = 0;
        boolean haveErrase;

        while (TimeUtils.millis() - startTime < 4000);

        while (counter < actors.size) {
            haveErrase = false;
            for (int iter = 0; iter < actors.size; iter++)
                if (actors.get(iter) instanceof Image) {
                    actors.removeIndex(iter);
                    haveErrase = true;
                }
            if (haveErrase)
                counter = 0;
            else
                counter++;
        }
        this._playedCard.removeAllElements();
        this._playedCard.clear();
        JCoincheHUD.getInstance().setScoreTeam(score.getMyScore(), score.getEnemyScore());
        JCoincheHUD.getInstance().endOfPli();
    }

    private void displayPlayedCard() {
        ServerData.Data.Card card = this._playedCard.get(this._playedCard.size() - 1);
        String path = "assets/cards/" + this._mapCardValue.get(card.getType()) + "_of_" + this._mapCardColor.get(card.getColor()) + ".png";
        Image cardToDisplay = new Image(new Texture(Gdx.files.internal(path)));
        float decalCard = 0.05f * (this._playedCard.size() - 1);

        cardToDisplay.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.2f);
        cardToDisplay.setPosition(Gdx.graphics.getWidth() * (0.4f + decalCard), Gdx.graphics.getHeight() / 2, Align.center);
        this._haveAddCard = false;
        this._stage.addActor(cardToDisplay);

    }

    void endOfRound(ServerData.Data.Score score)
    {
        this.reset();
        JCoincheHUD.getInstance().endOfRound();
        JCoincheHUD.getInstance().setScoreTeam(score.getMyScore(), score.getEnemyScore());
    }

    void displayCoinche() {
        this._bidManager.beginCoincheTurn();
    }

    void displaySurcoinche() {
        this._bidManager.beginSurcoincheTurn();
    }
}