/**
 * Created by wasta-geek on 14/11/16.
 * Project : JCoinche.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class JCoincheEvents extends Game implements IEventManager{
    private JCoincheMenuDisplay _menu;
    private JCoincheGameDisplay _game;

    public JCoincheEvents()
    {
        super();
    }

    public void launchMenu()
    {
        this.setScreen(this._menu);
        this._menu.setInput();
    }

    public void launchGame()
    {
        this.setScreen(this._game);
        this._game.setInput();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public Screen getScreen() {
        return super.getScreen();
    }

    public void init() {
        myGDX _gdx = new myGDX();
        try {
            _gdx.init(this);
        }
        catch (java.awt.HeadlessException error)
        {
            System.out.println("Il n'y a pas d'environnement :)");
        }
    }

    public void interpretMessage(ServerData.Data data) {
        if (data.getId() == 1) {
            if (data.getIsSuccess()) {
                this.launchGame();
            } else if (data.hasErrorMsg()) {
                this._menu.displayError(data.getErrorMsg());
            }
        } else if (data.getId() == 2) {
            for (int i = 0; i < data.getCardCount(); i++)
                this._game.displayHand(data.getCard(i));
        } else if (data.getId() == 3) {
            JCoincheHUD.getInstance().setTeams(data.getMyTeam(), data.getEnemyTeam());
        } else if (data.getId() == 4) {
            this._game.displayBid(data.getBid());
        } else if (data.getId() == 5 && data.getPlayerBid().getType() != ServerData.Data.BidType.SKIP) {
            JCoincheHUD.getInstance().setBet(String.valueOf(data.getPlayerBid().getValue()), data.getPlayerBid().getTrump(), data.getPlayerBid().getNick());
        } else if (data.getId() == 6) {
            this._game.displayCoinche();
        } else if (data.getId() == 7) {
            this._game.displaySurcoinche();
        }
        else if (data.getId() == 8) {
            this._game.launchGameTurn(data.getCardList());
        }
        else if (data.getId() == 9)
            this._game.placeACard(data.getCard(0));
        else if (data.getId() == 10)
            this._game.endOfPli(data.getScore());
        else if (data.getId() == 11)
            this._game.endOfRound(data.getScore());
        else if (data.getId() == 666) {
            this._game.reset();
        }
    }

    public void create() {
        this._menu = new JCoincheMenuDisplay();
        this._menu.init();
        this._game = new JCoincheGameDisplay();
        this._game.init();
        this.launchMenu();
    }

    static Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return sprite;
    }

    static Drawable drawableFromTexture(Texture texture)
    {
        return new Image(JCoincheEvents.createScaledSprite(texture)).getDrawable();
    }

    public void dispose() {
        this._menu.dispose();
        this._game.dispose();
    }
}
