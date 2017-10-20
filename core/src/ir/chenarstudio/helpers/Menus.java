package ir.chenarstudio.helpers;

import com.badlogic.gdx.utils.Array;

import ir.chenarstudio.data.ModeEntry;
import ir.chenarstudio.gameobjects.Menu;
import ir.chenarstudio.gameobjects.MenuList;

public class Menus {

    public static Array<Menu> getMainMenus() {
        MenuList menuList = new MenuList(MenuList.MenuSide.LEFT);
        menuList.add("Let's Pair!", "singleplayer");
        menuList.add("High Scores", "highscore");
        menuList.add("Options", "options");
        menuList.add("About", "about");
        menuList.add("Quit", "quit");

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(menuList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getModeMenus() {
        MenuList menuList = new MenuList(MenuList.MenuSide.RIGHT);

        for(ModeEntry mode : Assets.gameData.modes) {
            menuList.add(mode.title, "mode", mode.name);
        }

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(menuList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getReadyMenus(float runTime, int wave) {
        MenuList leftList = new MenuList(MenuList.MenuSide.LEFT);
        MenuList rightList = new MenuList(MenuList.MenuSide.RIGHT);

        if(wave <= 1) {
            leftList.add("Ready?");
            if(runTime > 0.5f) rightList.add("Set!");
        } else {
            leftList.add("Wave");
            if(runTime > 0.5f) rightList.add(wave+"");
        }


        Array<Menu> menu = new Array<Menu>();
        menu.addAll(leftList.get());
        menu.addAll(rightList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getRunningMenus(int score, ModeEntry mode) {
        Array<Menu> menu = new Array<Menu>();
        menu.add(new Menu(mode.topTitle, "null", "null", Constants.MENU_X, Constants.MENU_Y));
        menu.add(new Menu(score + "", "null", "null", (Constants.GAME_WIDTH / 2) - (Methods.getStringWidth(score + "") / 2), Constants.MENU_Y));
        menu.add(new Menu("Pause", "pause", "null", Constants.GAME_WIDTH - (Constants.MENU_X + Methods.getStringWidth("Pause")), Constants.MENU_Y));
        return menu;
    }

    public static Array<Menu> getPauseMenus(int score, ModeEntry mode) {
        MenuList leftList = new MenuList(MenuList.MenuSide.LEFT);
        leftList.add("Game");
        leftList.add("Resume", "resume");

        MenuList rightList = new MenuList(MenuList.MenuSide.RIGHT);
        rightList.add("Paused");
        rightList.add("Quit", "home");

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(leftList.get());
        menu.addAll(rightList.get());
        menu.add(new Menu(mode.topTitle, "null", "null", Constants.MENU_X, Constants.MENU_Y));
        menu.add(new Menu(score + "", "null", "null", (Constants.GAME_WIDTH / 2) - (Methods.getStringWidth(score + "") / 2), Constants.MENU_Y));

        return menu;
    }

    public static Array<Menu> getGameOverMenus(int score, ModeEntry mode) {
        MenuList leftList = new MenuList(MenuList.MenuSide.LEFT);
        leftList.add("Game");
        leftList.add("Score");
        leftList.add("Retry", "retry");

        MenuList rightList = new MenuList(MenuList.MenuSide.RIGHT);
        rightList .add("Over");
        rightList.add(score + "");
        rightList .add("Quit", "home");

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(leftList.get());
        menu.addAll(rightList.get());
        menu.add(new Menu(mode.title, "null", "null", (Constants.GAME_WIDTH / 2) - (Methods.getStringWidth(mode.title) / 2), Constants.MENU_Y));

        return menu;
    }

    public static Array<Menu> getHighScoreMenus(ModeEntry mode) {
        MenuList menuList = new MenuList(MenuList.MenuSide.RIGHT);

        if(mode == null) {
            for(ModeEntry modeEntry : Assets.gameData.modes) {
                menuList.add(modeEntry.title, "showHighScores", modeEntry.name);
            }
        } else {
            int i = 1;
            Array<Integer> highScores = Assets.getHighScores(mode);
            for (Integer highScore : highScores) {
                if(highScore == 0)
                    menuList.add(i+". N/A");
                else
                    menuList.add(i+". "+highScore);
                i++;
            }
        }

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(menuList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getOptionMenus() {
        MenuList menuList = new MenuList(MenuList.MenuSide.RIGHT);

        if(Assets.getMusic())
            menuList.add("Music On", "setOption", "music");
        else
            menuList.add("Music Off", "setOption", "music");

        if(Assets.getSound())
            menuList.add("Sound On", "setOption","sound");
        else
            menuList.add("Sound Off", "setOption", "sound");

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(menuList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getAboutMenus() {
        MenuList menuList = new MenuList(MenuList.MenuSide.RIGHT);
        menuList.add("ChenarStudio", "website");
        menuList.add("H.Alhaidari");
        menuList.add("S.Hekmatnia");

        Array<Menu> menu = new Array<Menu>();
        menu.addAll(menuList.get());
        menu.addAll(getTopMenu());

        return menu;
    }

    public static Array<Menu> getTopMenu() {
        Array<Menu> menu = new Array<Menu>();
        menu.add(new Menu("Pair", "null", "null", (Constants.GAME_WIDTH / 2) - (Methods.getStringWidth("Pair") / 2), Constants.MENU_Y));
        return menu;
    }
}
