/**
 * Created by wasta-geek on 14/11/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Cursor;

public class myGDX{
    public void init(JCoincheEvents events) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "JCoinche";
        config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        config.width= config.height;
        config.vSyncEnabled = false;
        config.fullscreen = false;
        new LwjglApplication(events, config);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}
