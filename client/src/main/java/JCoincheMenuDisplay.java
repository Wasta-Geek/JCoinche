import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by wasta-geek on 17/11/16.
 * Project : JCoinche.
 */

class JCoincheMenuDisplay implements IDisplay, Screen{

    private Stage _stage;
    private Skin _skin;
    private SpriteBatch _batch;
    private Sprite  _background;
    private BitmapFont _font;
    private Label _errorMessage;

    JCoincheMenuDisplay() {    }

    void setInput()
    {
        Gdx.input.setInputProcessor(this._stage);
    }

    void displayError(String error)
    {
        this._errorMessage.setText("" + error);
    }

    private void createSkin()
    {
        this._skin = new Skin();
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        BitmapFont font = new BitmapFont();
        this._skin.add("default", font);
        //BLACK
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        this._skin.add("black", new Texture(pixmap));
        //WHITE
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this._skin.add("white", new Texture(pixmap));
        //RED
        pixmap.setColor(Color.RED);
        pixmap.fill();
        this._skin.add("red", new Texture(pixmap));
    }

    private void setBackground()
    {
        Texture texture = new Texture(Gdx.files.internal("assets/menuBack.jpg"));
        this._background = JCoincheEvents.createScaledSprite(texture);

    }

    private void setUpStyles()
    {
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = this._skin.getFont("default");
        checkBoxStyle.checkboxOn = this._skin.newDrawable("black", Color.BLACK);
        checkBoxStyle.checkboxOff = this._skin.newDrawable("white", Color.WHITE);
        this._skin.add("default", checkBoxStyle);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        this._skin.add("default", buttonStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = this._skin.getFont("default");
        textButtonStyle.up = _skin.getDrawable("black");
        this._skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.fontColor=Color.BLACK;
        textFieldStyle.messageFontColor=Color.BLACK;
        textFieldStyle.focusedFontColor=Color.BLACK;
        textFieldStyle.font = this._skin.getFont("default");
        Texture texturefieldStyle = new Texture(Gdx.files.internal("assets/textfieldBackground.png"));
        textFieldStyle.background = new SpriteDrawable(JCoincheEvents.createScaledSprite(texturefieldStyle));
        textFieldStyle.cursor= this._skin.getDrawable("black");
        textFieldStyle.cursor.setMinWidth(2f);
        this._skin.add("default", textFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this._skin.getFont("default");
        labelStyle.fontColor=Color.BLACK;
        this._skin.add("default", labelStyle);

        Label.LabelStyle errorLabelStyle = new Label.LabelStyle();
        errorLabelStyle.font = this._skin.getFont("default");
        errorLabelStyle.fontColor=Color.RED;
        this._skin.add("errorLabel", errorLabelStyle);
    }

    private void setButtons()
    {
        float widthTable = Gdx.graphics.getWidth() * 0.25f;
        float heightTable = Gdx.graphics.getHeight() * 0.5f;

       // Creating table
        Table menu = new Table();
        final Drawable tableBackground = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/emptyCard.png"))));
        menu.setSize(widthTable, heightTable);
        menu.setPosition(Gdx.graphics.getWidth() / 2.0f - (widthTable / 2.0f), (Gdx.graphics.getHeight() / 2.0f) - heightTable / 1.50f);
        menu.setBackground(tableBackground);

        // PSEUDO
        final Label labelPseudo = new Label("Pseudo: ", this._skin);
        final TextField inputPseudo = new TextField("Chelmi", this._skin);
        inputPseudo.setMaxLength(10);
        inputPseudo.setAlignment(Align.center);
        // IP
        final Label labelIP = new Label("IP: ", this._skin);
        final TextField inputIP = new TextField("127.0.0.1", this._skin);
        inputIP.setMaxLength(15);
        inputIP.setAlignment(Align.center);
        // PORT
        final Label labelPort = new Label("Port: ", this._skin);
        final TextField inputPort = new TextField("8080", this._skin);
        inputPort.setMaxLength(5);
        inputPort.setAlignment(Align.center);
        // QUITER
        final TextButton quitButton = new TextButton("Quitter", this._skin);
        quitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Core.getInstance().shutdown();
            }
        });
        _stage.setKeyboardFocus(inputPseudo);
        inputPseudo.setCursorPosition(inputPseudo.getText().length());
        inputPseudo.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                if ((c == '\n' || c == '\r'))
                {
                    if (inputIP.getText().length() <= 0 || inputPort.getText().length() <= 0)
                        return;
                    if (!inputIP.isDisabled()) {
                        boolean ret = Core.getInstance().connectServer(inputIP.getText(), Integer.parseInt(inputPort.getText(), 10));
                        String pseudo = inputPseudo.getText();
                        if (ret) {
                            inputIP.setDisabled(true);
                            inputIP.setColor(Color.GREEN);
                            inputPort.setDisabled(true);
                            inputPort.setColor(Color.GREEN);
                            _stage.unfocus(inputPort);
                        }
                        if (ret && pseudo.length() <= 0)
                            inputPseudo.setColor(Color.RED);
                        else if (ret) {
                            Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                                    .setId(1)
                                    .setPseudo(pseudo)
                                    .build());
                        }
                        else {
                            inputPort.setColor(Color.RED);
                            inputIP.setColor(Color.RED);
                        }}
                    else
                        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                                .setId(1)
                                .setPseudo(inputPseudo.getText())
                                .build());
                }}});
        inputIP.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                if ((c == '\n' || c == '\r'))
                {
                    if (inputIP.getText().length() <= 0 || inputPort.getText().length() <= 0)
                        return;
                    boolean ret = Core.getInstance().connectServer(inputIP.getText(), Integer.parseInt(inputPort.getText(), 10));
                    String pseudo = inputPseudo.getText();
                    if (ret)
                    {
                        inputIP.setDisabled(true);
                        inputIP.setColor(Color.GREEN);
                        inputPort.setDisabled(true);
                        inputPort.setColor(Color.GREEN);
                        _stage.unfocus(inputPort);
                    }
                    if (ret && pseudo.length() <= 0)
                        inputPseudo.setColor(Color.RED);
                    else if (ret) {
                        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                                .setId(1)
                                .setPseudo(pseudo)
                                .build());
                    }
                    else
                    {
                        inputPort.setColor(Color.RED);
                        inputIP.setColor(Color.RED);
                    }
                }
                else if (c != '.' && !Character.isDigit(c) && inputIP.getText().indexOf(c) != -1) {
                    String tmp = inputIP.getText().replace(c, '/');
                    if (tmp.length() == 1)
                        inputIP.setText("");
                    else
                        inputIP.setText(tmp.substring(0, tmp.indexOf('/'))
                                + tmp.substring(tmp.indexOf('/') + 1));
                    inputIP.setCursorPosition(inputIP.getText().length());
                }
            }
        });
        inputPort.setTextFieldListener(new TextField.TextFieldListener() {

            public void keyTyped(TextField textField, char c) {
                if ((c == '\n' || c == '\r'))
                {
                    if (inputIP.getText().length() <= 0 || inputPort.getText().length() <= 0)
                        return;
                    boolean ret = Core.getInstance().connectServer(inputIP.getText(), Integer.parseInt(inputPort.getText(), 10));
                    String pseudo = inputPseudo.getText();
                    if (ret)
                    {
                        inputIP.setDisabled(true);
                        inputIP.setColor(Color.GREEN);
                        inputPort.setDisabled(true);
                        inputPort.setColor(Color.GREEN);
                        _stage.unfocus(inputPort);
                    }
                    if (ret && pseudo.length() <= 0)
                        inputPseudo.setColor(Color.RED);
                    else if (ret) {
                        Core.getInstance().sendMessage(ClientData.Data.newBuilder()
                                .setId(1)
                                .setPseudo(pseudo)
                                .build());
                    }
                    else
                    {
                        inputPort.setColor(Color.RED);
                        inputIP.setColor(Color.RED);
                    }
                }
                else if (inputPort.getText().indexOf(c) != - 1 && !Character.isDigit(c))
                {
                    String tmp = inputPort.getText().replace(c, '/');
                    if (tmp.length() == 1)
                        inputPort.setText("");
                    else
                        inputPort.setText(tmp.substring(0, tmp.indexOf('/'))
                                + tmp.substring(tmp.indexOf('/') + 1));
                    inputPort.setCursorPosition(inputPort.getText().length());
                }
            }
        });

        // Adding in table
        menu.add(labelPseudo).padRight(10).align(Align.left).expandY().padTop(menu.getHeight() / 5);
        menu.add(inputPseudo).height(menu.getHeight() / 10).padTop(menu.getHeight() / 5);
        menu.row();
        menu.add(labelIP).padRight(10).align(Align.left).expandY();
        menu.add(inputIP).height(menu.getHeight() / 10);
        menu.row();
        menu.add(labelPort).padRight(10).align(Align.left).expandY();
        menu.add(inputPort).height(menu.getHeight() / 10);
        menu.row();
        menu.add(quitButton).height(menu.getHeight() / 10).width(menu.getWidth() / 3).colspan(2).align(Align.center).expandY();
        menu.setClip(false);
        // Adding to stage
        this._stage.addActor(menu);
        this._errorMessage = new Label("", this._skin, "errorLabel");
        this._errorMessage.setVisible(true);
        this._errorMessage.setPosition(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() * 0.6f);
        this._errorMessage.setAlignment(Align.center);
        this._errorMessage.setColor(Color.RED);
        this._stage.addActor(this._errorMessage);
    }

    public void init()
    {
        this._batch = new SpriteBatch();
        this._stage = new myGdxStage();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/police.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 10;
        this._font = generator.generateFont(parameter);
        generator.dispose();
        this.createSkin();
        this.setUpStyles();
        this.setBackground();
        this.setButtons();
    }

    public void show() {
    }

    public void render(float v) {
        Gdx.gl.glClearColor( 1, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        this._batch.begin();
        this._background.draw(this._batch);
        String title = "JCOINCHE";
        this._batch.setColor(Color.BLACK);
        this._font.draw(this._batch, title, Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.9f);
        this._batch.end();
        this._stage.act();
        this._stage.draw();
    }

    public void resize(int width, int height) {
        this._stage.getViewport().update(width, height, true);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/police.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 10;
        this._font.dispose();
        this._font = generator.generateFont(parameter);
        generator.dispose();
    }


    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        this._batch.dispose();
        this._skin.dispose();
        this._stage.dispose();
        this._font.dispose();
    }
}
