package ir.chenarstudio.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Box {

    private Vector2 position;
    private float width;
    private float height;
    private int index;
    private Color color;
    private boolean isSelected = false;
    private boolean isNull = false;
    private BoxSide boxSide;

    public enum BoxSide {LEFT, RIGHT}

    public Box(int index, float x, float y, float width, float height, Color color, BoxSide boxSide) {
        position = new Vector2(x, y);
        this.index = index;
        this.width = width;
        this.height = height;
        this.color = color;
        this.boxSide = boxSide;
    }

    public Box() {
        position = new Vector2(0, 0);
        this.width = 0;
        this.height = 0;
        this.color = new Color(0, 0, 0, 0);
        isNull = true;
    }

    public int getIndex() { return index; }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public BoxSide getBoxSide() {
        return boxSide;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isNull() {
        return isNull;
    }
}