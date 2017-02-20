import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * Created by wasta-geek on 25/11/16.
 * Project : JCoinche.
 */
class JCoincheBidManager {
    private final Slider _bidSlider;
    private Label.LabelStyle _labelStyle;
    private Stage  _stage;
    private TextButton _bidButton;
    private TextButton _coincheButton;
    private TextButton _surcoincheButton;
    private TextButton _passButton;
    private Table _colorTable;
    private Label   _betValue;
    private Image _emptyBackground;
    private ClientData.Data.CardColor _colorChoice;
    private int _max;
    private int _min;

    JCoincheBidManager(Stage stage) {
        this._stage = stage;

        // set empty circle in background while bet
        this._emptyBackground = new Image(JCoincheEvents.createScaledSprite(new Texture(Gdx.files.internal("assets/emptyCercleBid.png"))));
        this._emptyBackground.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        this._emptyBackground.setVisible(false);
        this._stage.addActor(this._emptyBackground);

        // Police font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/police.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 30;

        //PASS OR BET
        Drawable pass = JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/pass.png")));
        Drawable bid = JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/bid.png")));

        TextButton.TextButtonStyle passButtonStyle = new TextButton.TextButtonStyle(
                pass, pass, pass, generator.generateFont(parameter));
        passButtonStyle.fontColor = Color.BLACK;
        TextButton.TextButtonStyle bidButtonStyle = new TextButton.TextButtonStyle(
                bid, bid, bid, generator.generateFont(parameter));
        bidButtonStyle.fontColor = Color.BLACK;
        this._bidButton = new TextButton("BID", bidButtonStyle);
        this._passButton = new TextButton("SKIP", passButtonStyle);

        this._bidButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                _bidButton.setVisible(false);
                _bidButton.setDisabled(true);
                _passButton.setVisible(false);
                _passButton.setDisabled(true);
                chooseBid();
            }
        });

        this._passButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!_bidButton.isDisabled())
                    Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                            .setId(2)
                            .setBid(ClientData.Data.Bid.newBuilder().setTrump(ClientData.Data.CardColor.SPADES).setType(ClientData.Data.BidType.SKIP))
                            .build());
                if (!_coincheButton.isDisabled())
                    Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                            .setId(3)
                            .setSuccess(false)
                            .build());
                if (!_surcoincheButton.isDisabled())
                    Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                            .setId(4)
                            .setSuccess(false)
                            .build());

                _bidButton.setVisible(false);
                _bidButton.setDisabled(true);

                _coincheButton.setVisible(false);
                _coincheButton.setDisabled(true);

                _surcoincheButton.setVisible(false);
                _surcoincheButton.setDisabled(true);

                _passButton.setVisible(false);
                _passButton.setDisabled(true);

                _emptyBackground.setVisible(false);
            }
        });

        _bidButton.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.15f);
        _bidButton.setPosition(Gdx.graphics.getWidth() * 0.7f - _bidButton.getWidth(), Gdx.graphics.getHeight() * 0.5f - _bidButton.getHeight() / 2);

        _passButton.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.15f);
        _passButton.setPosition(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.5f - _passButton.getHeight() / 2);

        _bidButton.setDisabled(true);
        _bidButton.setVisible(false);
        _passButton.setDisabled(true);
        _passButton.setVisible(false);

        this._stage.addActor(_passButton);
        this._stage.addActor(_bidButton);
        
        //COINCHE OR PASS
        
        Drawable coinche = JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/bid.png")));
        parameter.size = Gdx.graphics.getHeight() / 50;
        TextButton.TextButtonStyle coincheButtonStyle = new TextButton.TextButtonStyle(
                coinche, coinche, coinche, generator.generateFont(parameter));
        coincheButtonStyle.fontColor = Color.BLACK;
        this._coincheButton = new TextButton("coinche", coincheButtonStyle);

        this._coincheButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _coincheButton.setVisible(false);
                _emptyBackground.setVisible(false);
                _coincheButton.setDisabled(true);
                _passButton.setVisible(false);
                _passButton.setDisabled(true);
                chooseCoinche();
            }
        });

        _coincheButton.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.15f);
        _coincheButton.setPosition(Gdx.graphics.getWidth() * 0.7f - _coincheButton.getWidth(), Gdx.graphics.getHeight() * 0.5f - _coincheButton.getHeight() / 2);

        _coincheButton.setDisabled(true);
        _coincheButton.setVisible(false);

        this._stage.addActor(_coincheButton);

        // SURCOINCHE OR PASS
        
        Drawable surcoinche = JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/bid.png")));
        parameter.size = Gdx.graphics.getHeight() / 80;
        TextButton.TextButtonStyle surcoincheButtonStyle = new TextButton.TextButtonStyle(
                surcoinche, surcoinche, surcoinche, generator.generateFont(parameter));
        surcoincheButtonStyle.fontColor = Color.BLACK;
        this._surcoincheButton = new TextButton("surcoinche", surcoincheButtonStyle);

        this._surcoincheButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _emptyBackground.setVisible(false);
                _surcoincheButton.setVisible(false);
                _surcoincheButton.setDisabled(true);
                _passButton.setVisible(false);
                _passButton.setDisabled(true);
                chooseSurcoinche();
            }
        });

        this._surcoincheButton.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.15f);
        this._surcoincheButton.setPosition(Gdx.graphics.getWidth() * 0.7f - _coincheButton.getWidth(), Gdx.graphics.getHeight() * 0.5f - _coincheButton.getHeight() / 2);

        this._surcoincheButton.setDisabled(true);
        this._surcoincheButton.setVisible(false);

        this._stage.addActor(this._surcoincheButton);

        // BET

        // betSlider
        Sprite knob = new Sprite(new Texture(Gdx.files.internal("assets/emptyCard.png")));
        knob.setSize(30, 40);
        Sprite bar = new Sprite(new Texture(Gdx.files.internal("assets/bidBar.png")));
        Drawable barDrawable = new SpriteDrawable(bar);
        Drawable knobDrawable = new SpriteDrawable(knob);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(barDrawable, knobDrawable);
        this._bidSlider = new Slider(this._min, this._max, 10, false, sliderStyle);
        _bidSlider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                int value = (int)_bidSlider.getValue();
                _betValue.setText(String.valueOf(value) + " points");
            }
        });
        _bidSlider.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 25);
        _bidSlider.setPosition(Gdx.graphics.getWidth() / 2 - _bidSlider.getWidth() / 2,
                Gdx.graphics.getHeight() * 0.6f - _bidSlider.getHeight());

        // bet value text
        parameter.size = Gdx.graphics.getHeight() / 25;
        this._labelStyle = new Label.LabelStyle(generator.generateFont(parameter), Color.BLACK);
        this._betValue = new Label(null, this._labelStyle);
        this._betValue.setPosition(_bidSlider.getX(), _bidSlider.getY() + _bidSlider.getHeight() / 2 + 40);
        this._betValue.setColor(Color.WHITE);
        this._betValue.setVisible(false);

        this._stage.addActor(this._betValue);


        // bet color & validate

        this._colorTable = new Table();
        this._colorTable.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.4f, Align.center);

        final Button heart = new Button(JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/heart.png"))));
        final Button club = new Button(JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/club.png"))));
        final Button spade = new Button(JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/spade.png"))));
        final Button diamond = new Button(JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/diamond.png"))));
        heart.setDisabled(true);
        club.setDisabled(true);
        spade.setDisabled(true);
        diamond.setDisabled(true);

        heart.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _colorChoice = ClientData.Data.CardColor.HEARTS;
                heart.setColor(Color.GREEN);
                club.setColor(Color.WHITE);
                spade.setColor(Color.WHITE);
                diamond.setColor(Color.WHITE);
                super.clicked(event, x, y);
            }
        });

        club.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _colorChoice = ClientData.Data.CardColor.CLUBS;
                heart.setColor(Color.WHITE);
                club.setColor(Color.GREEN);
                spade.setColor(Color.WHITE);
                diamond.setColor(Color.WHITE);
                super.clicked(event, x, y);
            }
        });

        spade.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _colorChoice = ClientData.Data.CardColor.SPADES;
                heart.setColor(Color.WHITE);
                club.setColor(Color.WHITE);
                spade.setColor(Color.GREEN);
                diamond.setColor(Color.WHITE);
                super.clicked(event, x, y);
            }
        });

        diamond.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _colorChoice = ClientData.Data.CardColor.DIAMONDS;
                heart.setColor(Color.WHITE);
                club.setColor(Color.WHITE);
                spade.setColor(Color.WHITE);
                diamond.setColor(Color.GREEN);
                super.clicked(event, x, y);
            }
        });

        this._colorTable.defaults().size(Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        this._colorTable.add(heart).padRight(5);
        this._colorTable.add(club).padRight(5);
        this._colorTable.add(spade).padRight(5);
        this._colorTable.add(diamond).row();

        Button okButton = new Button(JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/okButton.png"))));
        okButton.setDisabled(true);
        okButton.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.35f, Align.center);
        okButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean test = false;
                Array<Cell> cells = _colorTable.getCells();

                for (int counter = 0; counter < cells.size - 1; counter++)
                    if (((Button)(cells.get(counter).getActor())).isChecked())
                        test = true;
                if (test) {
                    Core.getInstance().sendMessage(ClientData.Data.newBuilder().setId(2).
                            setBid(ClientData.Data.Bid.newBuilder()
                                    .setType(ClientData.Data.BidType.BID)
                                    .setBet(Integer.valueOf(_betValue.getText().substring(0, _betValue.getText().indexOf(" "))))
                                    .setTrump(_colorChoice))
                            .build());
                    JCoincheHUD.getInstance().setBet(_betValue.getText().substring(0, _betValue.getText().indexOf(" ")),
                            _colorChoice, "ME");
                    endBid();
                }
                else {
                    heart.setColor(Color.RED);
                    club.setColor(Color.RED);
                    spade.setColor(Color.RED);
                    diamond.setColor(Color.RED);
                }
                super.clicked(event, x, y);
            }
        });
        this._colorTable.add(okButton).size(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 15).padTop(5).colspan(4).center();

        this._stage.addActor(this._colorTable);
        this._colorTable.setVisible(false);
        generator.dispose();
    }

    void addElements()
    {
        this._bidSlider.setVisible(false);
        this._bidSlider.setDisabled(true);

        this._stage.addActor(this._emptyBackground);
        this._stage.addActor(_coincheButton);
        this._stage.addActor(_surcoincheButton);
        this._stage.addActor(_passButton);
        this._stage.addActor(_bidButton);
        this._stage.addActor(this._colorTable);
        this._stage.addActor(this._betValue);
        this._stage.addActor(_bidSlider);
    }

    private void chooseSurcoinche()
    {
        this._surcoincheButton.setVisible(false);
        this._surcoincheButton.setDisabled(true);

        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                .setId(4)
                .setSuccess(true)
                .build());
    }

    private void chooseCoinche()
    {
        this._coincheButton.setVisible(false);
        this._coincheButton.setDisabled(true);

        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                .setId(3)
                .setSuccess(true)
                .build());
    }

    private void endBid()
    {
        this._colorTable.setVisible(false);
        for (Cell cell : this._colorTable.getCells()){
            ((Button)(cell.getActor())).setDisabled(true);
        }
        this._bidSlider.setDisabled(true);
        this._bidSlider.setVisible(false);

        this._betValue.setVisible(false);
        this._emptyBackground.setVisible(false);
    }

    private void chooseBid()
    {
        this._betValue.setText(String.valueOf(this._min) + " points");
        this._betValue.setVisible(true);

        this._bidSlider.setRange(this._min, this._max);
        this._bidSlider.setValue(this._min);

        this._bidSlider.setDisabled(false);
        this._bidSlider.setVisible(true);
        this._stage.addActor(_bidSlider);

        for (Cell cell : this._colorTable.getCells()){
            ((Button)(cell.getActor())).setDisabled(false);
        }
        this._colorTable.setVisible(true);
    }

    void beginBidTurn(int min, int max)
    {
        this._emptyBackground.setVisible(true);
        this._min = min;
        this._max = max;

        this._bidButton.setVisible(true);
        this._bidButton.setDisabled(false);

        this._passButton.setVisible(true);
        this._passButton.setDisabled(false);
    }

    void beginCoincheTurn()
    {
        this._emptyBackground.setVisible(true);

        this._coincheButton.setVisible(true);
        this._coincheButton.setDisabled(false);

        this._passButton.setVisible(true);
        this._passButton.setDisabled(false);
    }

    void beginSurcoincheTurn()
    {
        this._emptyBackground.setVisible(true);

        this._surcoincheButton.setVisible(true);
        this._surcoincheButton.setDisabled(false);

        this._passButton.setVisible(true);
        this._passButton.setDisabled(false);
    }

}
