package ir.chenarstudio.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ir.chenarstudio.data.ModeEntry;

public class Methods {

    public static ModeEntry getMode(String name) {
        for(ModeEntry mode : Assets.gameData.modes) {
            if(mode.name.equals(name)) return mode;
        }

        return null;
    }

    public static Color getColor(String color)
    {
        String[] colors = color.split(",");
        if(colors.length < 3) return new Color(0, 0, 0, 1);

        return new Color(Float.parseFloat(colors[0]) / 255f, Float.parseFloat(colors[1]) / 255f, Float.parseFloat(colors[2]) / 255f, 1);
    }

    public static float getStringWidth(String text) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Assets.font, text);
        return layout.width;
    }

    public static void print(SpriteBatch batcher, String text, float x, float y) {
        batcher.begin();
        Assets.font.draw(batcher, text, x, y);
        batcher.end();
    }
}
