package ir.chenarstudio.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Sort;

import java.util.Collections;

import ir.chenarstudio.data.ColorEntry;
import ir.chenarstudio.data.GameData;
import ir.chenarstudio.data.ModeEntry;

public class Assets {

    public static Preferences prefs;
    public static Sound paired, notPaired;
    public static Music bg;
    public static BitmapFont font;
    public static GameData gameData;

    public static void load() {
        bg = Gdx.audio.newMusic(Gdx.files.internal("sounds/BG.ogg"));
        bg.setLooping(true);
        paired = Gdx.audio.newSound(Gdx.files.internal("sounds/Paired.ogg"));
        notPaired = Gdx.audio.newSound(Gdx.files.internal("sounds/NotPaired.ogg"));

        font = new BitmapFont(Gdx.files.internal("fonts/text.fnt"));
        font.getData().setScale(.3f, -.35f);

        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        gameData = json.fromJson(GameData.class, Gdx.files.internal("data/data.json"));

        prefs = Gdx.app.getPreferences("Pair");
        if(!prefs.contains("sound")) {
            prefs.putBoolean("sound", true);
            prefs.flush();
        }
        if(!prefs.contains("music")) {
            prefs.putBoolean("music", true);
            prefs.flush();
        }
    }

    public static Array<Color> generateColors(ModeEntry mode) {
        Array<Color> colors = new Array<Color>();
        Array<ColorEntry> entries = new Array<ColorEntry>();
        int curLevel, levels;
        ColorEntry temp;

        if(mode.colors.equals("all")) {
            entries = gameData.colors;
        } else {
            curLevel = 1;
            levels = Integer.parseInt(mode.colors);
            if(levels > gameData.colors.size) levels = gameData.colors.size;

            while (curLevel <= levels) {
                temp = gameData.colors.get(MathUtils.random(0, gameData.colors.size - 1));
                while (entries.contains(temp, false))
                    temp = gameData.colors.get(MathUtils.random(0, gameData.colors.size - 1));
                entries.add(temp);
                curLevel++;
            }

        }

        for(ColorEntry entry : entries) {
            if(mode.colorLevel.equals("all")) {
                for(String color : entry.data) {
                    colors.add(Methods.getColor(color));
                }
            } else {
                curLevel = 1;
                levels = Integer.parseInt(mode.colorLevel);
                for(String color : entry.data) {
                    colors.add(Methods.getColor(color));
                    curLevel++;
                    if(curLevel > levels) break;
                }
            }
        }

        return colors;
    }

    public static void addHighScore(int val, ModeEntry mode) {
        Array<Integer> highScores = getHighScores(mode);

        if(highScores.size >= Constants.MAX_HIGHSCORES && val < highScores.get(highScores.size - 1)) return;

        if(highScores.size < 1 || val > highScores.get(highScores.size - 1)) {

            if(highScores.size < Constants.MAX_HIGHSCORES) {
                highScores.add(val);
            } else {
                int lastItem = (highScores.size > 0) ? highScores.size - 1 : 0;
                highScores.set(lastItem, val);
            }

            String scores = "";
            for(Integer highScore : highScores) {
                scores += highScore+",";
            }

            prefs.putString("highScore:"+mode.name, scores.substring(0, scores.length() - 1));
            prefs.flush();
        }
    }

    public static Array<Integer> getHighScores(ModeEntry mode) {
        if(mode.name.isEmpty()) return getEmptyHighScores();

        String highScore = prefs.getString("highScore:" + mode.name);
        if(highScore.isEmpty()) return getEmptyHighScores();

        String[] highScores = highScore.split(",");
        if(highScores.length < 1) return getEmptyHighScores();

        Array<Integer> scores = new Array<Integer>();
        for(String score : highScores) {
            scores.add(Integer.parseInt(score));
        }

        Sort sort = new Sort();
        sort.sort(scores, Collections.reverseOrder());

        return scores;
    }

    public static Array<Integer> getEmptyHighScores() {
        Array<Integer> highScores = new Array<Integer>();
        for(int i = 0; i < Constants.MAX_HIGHSCORES; i++)
            highScores.add(0);
        return highScores;
    }

    public static void toggleSound() {
        boolean val = getSound();
        prefs.putBoolean("sound", !val);
        prefs.flush();
    }

    public static boolean getSound() {
        return prefs.getBoolean("sound");
    }

    public static void toggleMusic() {
        boolean val = getMusic();
        prefs.putBoolean("music", !val);
        prefs.flush();

        if(!val)
            bg.play();
        else
            bg.stop();
    }

    public static boolean getMusic() {
        return prefs.getBoolean("music");
    }

    public static void dispose() {
        bg.dispose();
        paired.dispose();
        notPaired.dispose();
        font.dispose();
    }
}
