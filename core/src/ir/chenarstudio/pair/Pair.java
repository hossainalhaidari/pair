package ir.chenarstudio.pair;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import ir.chenarstudio.helpers.Assets;
import ir.chenarstudio.screens.GameScreen;

public class Pair extends Game {

    public Pair() {

    }

    @Override
    public void create() {
        Assets.load();
        if(Assets.getMusic()) Assets.bg.play();
        setScreen(new GameScreen());
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}