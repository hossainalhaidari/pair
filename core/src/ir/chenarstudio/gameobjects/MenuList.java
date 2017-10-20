package ir.chenarstudio.gameobjects;

import com.badlogic.gdx.utils.Array;

import ir.chenarstudio.helpers.Constants;
import ir.chenarstudio.helpers.Methods;

public class MenuList {

    private Array<Menu> menus;
    private Array<String> tempMenus;
    private MenuSide side;

    public enum MenuSide {
        LEFT, RIGHT
    }

    public MenuList(MenuSide side) {
        tempMenus = new Array<String>();
        this.side = side;
    }

    public void add(String name) {
        tempMenus.add(name + "-null-null");
    }

    public void add(String name, String action) {
        tempMenus.add(name + "-" + action + "-null");
    }

    public void add(String name, String action, String param) {
        tempMenus.add(name + "-" + action + "-" + param);
    }

    public Array<Menu> get() {
        menus = new Array<Menu>();

        int total = tempMenus.size;
        float totalSize = (total * Constants.MENU_HEIGHT) + ((total - 1) * Constants.MENU_GAP);
        float midPointY = (Constants.GAME_HEIGHT - Constants.GAME_TOP) / 2;
        float x;
        float y = midPointY - (totalSize / 2);

        for(String key : tempMenus) {
            String[] keys = key.split("-");
            String name = keys[0];
            String action = keys[1];
            String param = keys[2];

            x = (side == MenuSide.LEFT) ? ((Constants.GAME_WIDTH / 2) - (Methods.getStringWidth(name) + Constants.MENU_X)) : Constants.MENU_X + (Constants.GAME_WIDTH / 2);
            Menu menu = new Menu(name, action, param, x, y);
            menus.add(menu);
            y += Constants.MENU_HEIGHT + Constants.MENU_GAP;
        }

        return menus;
    }
}
