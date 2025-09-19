package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.TheGame;
import com.mygdx.game.constants.AppConstants;

public class DesktopLauncher {
  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(AppConstants.DEFAULT_WIN_WIDTH, AppConstants.DEFAULT_WIN_HEIGHT);
    config.useVsync(true);
    config.setTitle("dark-places");
    new Lwjgl3Application(new TheGame(), config);
  }
}
