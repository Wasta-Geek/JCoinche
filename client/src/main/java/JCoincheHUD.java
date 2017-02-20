import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.util.EnumMap;

/**
 * Created by wasta-geek on 27/11/16.
 * Project : JCoinche.
 */
class JCoincheHUD {
    private static JCoincheHUD ourInstance = new JCoincheHUD();
    private EnumMap<ClientData.Data.CardColor, String> _mapCardColor;
    private EnumMap<ServerData.Data.CardColor, String> _mapCardColorServer;
    private TextArea _myTeam;
    private TextArea _enemyTeam;
    private TextArea _round;
    private TextArea _myTeamComposition;
    private int _myTeamScore;
    private int _enemyTeamScore;

    public static JCoincheHUD getInstance() {
        return ourInstance;
    }

    private JCoincheHUD() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/police.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 55;

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(generator.generateFont(parameter),
                Color.BLACK,
                null,
                null,
                JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/emptyCard.png"))));


        this._round = new TextArea("", textFieldStyle);
        this._myTeamComposition = new TextArea("", textFieldStyle);

        parameter.size = Gdx.graphics.getHeight() / 30;
        TextField.TextFieldStyle textFieldStyleScore = new TextField.TextFieldStyle(generator.generateFont(parameter),
                Color.BLACK,
                null,
                null,
                JCoincheEvents.drawableFromTexture(new Texture(Gdx.files.internal("assets/emptyCard.png"))));


        this._myTeam = new TextArea("", textFieldStyleScore);
        this._enemyTeam = new TextArea("", textFieldStyleScore);

        generator.dispose();

        this._myTeam.setDisabled(true);
        this._enemyTeam.setDisabled(true);
        this._round.setDisabled(true);
        this._myTeamComposition.setDisabled(true);

        this._myTeam.setVisible(false);
        this._enemyTeam.setVisible(false);
        this._round.setVisible(false);
        this._myTeamComposition.setVisible(false);

        this._round.setSize(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() * 0.18f);
        this._myTeamComposition.setSize(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() * 0.18f);
        this._myTeam.setSize(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() * 0.18f);
        this._enemyTeam.setSize(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() * 0.18f);

        this._myTeamComposition.setPosition(this._myTeamComposition.getWidth() / 2 + 5,
                this._myTeamComposition.getHeight() / 2 + 5, Align.center);

        this._round.setPosition(Gdx.graphics.getWidth() - (this._round.getWidth() / 2) - 5,
                this._round.getHeight() / 2 + 5, Align.center);

        this._myTeam.setPosition(this._myTeam.getWidth() / 2 + 5,
                Gdx.graphics.getHeight() - this._myTeam.getHeight() / 2 - 5, Align.center);

        this._enemyTeam.setPosition(Gdx.graphics.getWidth() - (this._enemyTeam.getWidth() / 2) - 5,
                Gdx.graphics.getHeight() - (this._enemyTeam.getHeight() / 2) - 5, Align.center);


        this._mapCardColor = new EnumMap<ClientData.Data.CardColor, String>(ClientData.Data.CardColor.class);
        this._mapCardColor.put(ClientData.Data.CardColor.CLUBS, "clubs");
        this._mapCardColor.put(ClientData.Data.CardColor.DIAMONDS, "diamonds");
        this._mapCardColor.put(ClientData.Data.CardColor.HEARTS, "hearts");
        this._mapCardColor.put(ClientData.Data.CardColor.SPADES, "spades");

        this._mapCardColorServer = new EnumMap<ServerData.Data.CardColor, String>(ServerData.Data.CardColor.class);
        this._mapCardColorServer.put(ServerData.Data.CardColor.CLUBS, "clubs");
        this._mapCardColorServer.put(ServerData.Data.CardColor.DIAMONDS, "diamonds");
        this._mapCardColorServer.put(ServerData.Data.CardColor.HEARTS, "hearts");
        this._mapCardColorServer.put(ServerData.Data.CardColor.SPADES, "spades");
    }

    void addElementsToStage(Stage stage)
    {
        stage.addActor(this._myTeam);
        stage.addActor(this._enemyTeam);
        stage.addActor(this._round);
        stage.addActor(_myTeamComposition);
    }

    void endOfPli()
    {
        this._enemyTeam.setColor(Color.WHITE);
        this._myTeam.setColor(Color.WHITE);
    }

    void endOfRound()
    {
        this._round.setVisible(false);
    }

    private void addWord(String word, TextArea toFill, int maxCarac) {
        int counter = 0;

        while (counter <= (maxCarac - word.length()) / 2)
        {
            toFill.appendText(" ");
            counter++;
        }
        toFill.appendText(word);
    }
    
    void setTeams(ServerData.Data.Team myTeam, ServerData.Data.Team enemyTeam)
    {
        this._myTeamComposition.setText("\n\n");
        this.addWord(myTeam.getPlayer1() + "\n", this._myTeamComposition, 14);
        this.addWord("+\n", this._myTeamComposition, 14);
        this.addWord(myTeam.getPlayer2() + "\n", this._myTeamComposition, 14);
        this.addWord("VS\n", this._myTeamComposition, 14);
        this.addWord(enemyTeam.getPlayer1() + "\n", this._myTeamComposition, 14);
        this.addWord("+\n", this._myTeamComposition, 14);
        this.addWord(enemyTeam.getPlayer2() + "\n", this._myTeamComposition, 14);

        this._myTeamScore = 0;
        this._enemyTeamScore = 0;
        this.setScoreTeam(0, 0);

        this._myTeamComposition.setVisible(true);
        this._myTeam.setVisible(true);
        this._enemyTeam.setVisible(true);
    }

    void setBet(String value, ClientData.Data.CardColor color, String pseudo)
    {
        this._round.setText("\n\n");
        this.addWord("CONTRACT\n", this._round, 14);
        this.addWord("VALUE:\n", this._round, 14);
        this.addWord(value + "\n", this._round, 14);
        this.addWord("TRUMP:\n", this._round, 14);
        this.addWord(this._mapCardColor.get(color) + "\n", this._round, 14);
        this.addWord("OWNER:\n", this._round, 14);
        this.addWord(pseudo + "\n", this._round, 14);
        this._round.setVisible(true);
    }

    void setBet(String value, ServerData.Data.CardColor color, String pseudo)
    {
        this._round.setText("\n\n");
        this.addWord("CONTRACT\n", this._round, 14);
        this.addWord("VALUE:\n", this._round, 14);
        this.addWord(value + "\n", this._round, 14);
        this.addWord("TRUMP:\n", this._round, 14);
        this.addWord(this._mapCardColorServer.get(color) + "\n", this._round, 14);
        this.addWord("OWNER:\n", this._round, 14);
        this.addWord(pseudo + "\n", this._round, 14);
        this._round.setVisible(true);
    }

    void setScoreTeam(int myTeamValue, int oppositeTeamValue)
    {
        this._enemyTeamScore = oppositeTeamValue;
        this._myTeamScore = myTeamValue;

        this._myTeam.setText("\n");
        this.addWord("OUR\n", this._myTeam, 6);
        this.addWord("SCORE\n", this._myTeam, 6);
        this.addWord(String.valueOf(_myTeamScore) + "\n", this._myTeam, 6);

        this._enemyTeam.setText("\n");
        this.addWord("THEIR\n", this._enemyTeam, 6);
        this.addWord("SCORE\n", this._enemyTeam, 6);
        this.addWord(String.valueOf(_enemyTeamScore) + "\n", this._enemyTeam, 6);
    }

    void highLightWinner(float myTeam, float enemyTeam) {
        if (myTeam > this._myTeamScore) {
            this._myTeam.setColor(Color.YELLOW);
            this._enemyTeam.setColor(Color.LIGHT_GRAY);
        } else {
            this._myTeam.setColor(Color.LIGHT_GRAY);
            this._enemyTeam.setColor(Color.YELLOW);
        }
    }
}
