package ir.chenarstudio.helpers;

public class Constants {

    public static final float GAME_WIDTH = 320;
    public static final float GAME_HEIGHT = 480;
    public static final float GAME_TOP = 45;
    public static final int MAX_HIGHSCORES = 5;
    public static final int COMBO_TIME = 7;

    public static final float BOX_X = 10;
    public static final float BOX_Y = GAME_TOP + 15;
    public static final float BOX_WIDTH = 40;
    public static final float BOX_HEIGHT = 40;
    public static final int BOX_MAX = 24;
    public static final int BOX_GAP = 10;
    public static final int BOX_RADIUS = 20;

    public static final float MENU_X = 20;
    public static final float MENU_Y = 15;
    public static final float MENU_HEIGHT = 25;
    public static final float MENU_GAP = 15;

    public enum GameState {
        MENU, MODE,
        READY, RUNNING, PAUSE, GAMEOVER,
        HIGHSCORE, OPTIONS, ABOUT
    }
}
