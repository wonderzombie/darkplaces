package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.TheGame;
import com.mygdx.game.constants.AppConstants;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = AppConstants.DEFAULT_WIN_WIDTH;
    config.height = AppConstants.DEFAULT_WIN_HEIGHT;
    new LwjglApplication(new TheGame(), config);
  }
}
