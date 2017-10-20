package ir.chenarstudio.gameobjects;

import ir.chenarstudio.helpers.Constants;
import ir.chenarstudio.helpers.Methods;

public class Menu {

    private String name, action, param;
    private float x, y;

    public Menu(String name, String action, String param, float x, float y) {
        this.name = name;
        this.action = action;
        this.param = param;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public String getParam() {
        return param;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return Methods.getStringWidth(name);
    }

    public float getHeight() {
        return Constants.MENU_HEIGHT;
    }
}
