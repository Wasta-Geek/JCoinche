import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by wasta-geek on 26/11/16.
 * Project : JCoinche.
 */

public class myGdxStage extends Stage {
    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE)
            Gdx.app.exit();
        return true;
    }
}
