package ir.chenarstudio.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import ir.chenarstudio.data.ModeEntry;
import ir.chenarstudio.gameobjects.Box;
import ir.chenarstudio.gameobjects.Menu;
import ir.chenarstudio.helpers.Assets;
import ir.chenarstudio.helpers.Constants;
import ir.chenarstudio.helpers.Menus;
import ir.chenarstudio.helpers.Methods;

public class GameWorld {

    private int score = 0;
    private int pairStreak = 0;
    private int currentWave = 1;
    private float runTime = 0;
    private float lastPair = 0;
    private Constants.GameState currentState;
    private ModeEntry currentMode;
    private ModeEntry highScoreMode;
    private Box selectedBox = new Box();
    private Array<Box> boxesLeft;
    private Array<Box> boxesRight;
    private Array<Vector2> positions;
    private Array<Menu> menus;

    public GameWorld() {
        currentState = Constants.GameState.MENU;
    }

    public void update(float delta) {
        runTime += delta;

        switch (currentState) {
            case READY:
                if(runTime >= 1) {
                    runTime = 0;
                    currentState = Constants.GameState.RUNNING;
                }
                break;

            case RUNNING:
                if(runTime >= currentMode.time) gameOver();
                break;

            default:
                break;
        }
    }

    public void ready() {
        currentWave = 1;
        score = 0;
        runTime = 0;
        currentState = Constants.GameState.READY;
        initBoxes();
    }

    public void nextWave() {
        currentWave++;
        runTime = 0;
        currentState = Constants.GameState.READY;
        initBoxes();
    }

    public void gameOver() {
        Assets.addHighScore(getScore(), currentMode);
        currentState = Constants.GameState.GAMEOVER;
    }

    public void addScore() {
        if(Assets.getSound()) Assets.paired.play();

        pairStreak++;
        float time = Constants.COMBO_TIME - (runTime - lastPair);
        if(time < 1) time = 1;
        score += time * pairStreak;
        lastPair = runTime;
    }

    public void subScore() {
        if(Assets.getSound()) Assets.notPaired.play();

        pairStreak = 0;
        float time = Constants.COMBO_TIME - (runTime - lastPair);
        if(time < 1) time = 1;
        score -= time * 2;
        if(score < 0) score = 0;
        lastPair = runTime;
    }

    public void touched (OrthographicCamera cam, float x, float y) {
        Rectangle bounds;

        for (Menu menu : menus) {
            bounds = new Rectangle(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight());
            Vector3 tmp = new Vector3(x, y, 0);
            cam.unproject(tmp);
            if (bounds.contains(tmp.x, tmp.y)) {
                menuAction(menu);
                break;
            }
        }

        if (isRunning()) {
            for (Box box : boxesLeft) {
                bounds = new Rectangle(box.getX(), box.getY(), box.getWidth(), box.getHeight());
                Vector3 tmp = new Vector3(x, y, 0);
                cam.unproject(tmp);
                if (bounds.contains(tmp.x, tmp.y)) {
                    validatePair(box);
                    break;
                }
            }

            for (Box box : boxesRight) {
                bounds = new Rectangle(box.getX(), box.getY(), box.getWidth(), box.getHeight());
                Vector3 tmp = new Vector3(x, y, 0);
                cam.unproject(tmp);
                if (bounds.contains(tmp.x, tmp.y)) {
                    validatePair(box);
                    break;
                }
            }
        }
    }

    public void keyPressed (int keycode) {
        if(keycode == Input.Keys.BACK) {
            switch (currentState) {
                case READY:
                    currentState = Constants.GameState.MENU;
                    break;
                case RUNNING:
                    currentState = Constants.GameState.PAUSE;
                    break;
                case PAUSE:
                    currentState = Constants.GameState.RUNNING;
                    break;
                case GAMEOVER:
                    currentState = Constants.GameState.MENU;
                    break;
                default:
                    Gdx.app.exit();
                    break;
            }
        }
    }

    private void validatePair(Box newBox) {
        if(newBox.isNull()) return;

        if(selectedBox.isNull()) {
            newBox.setSelected(true);
            selectedBox = newBox;
        } else {
            if(selectedBox.getBoxSide() == newBox.getBoxSide()) return;

            if(selectedBox.getColor() == newBox.getColor()) {
                addScore();
                if(selectedBox.getBoxSide() == Box.BoxSide.LEFT) {
                    boxesRight.set(newBox.getIndex(), new Box());
                    boxesLeft.set(selectedBox.getIndex(), new Box());
                } else {
                    boxesLeft.set(newBox.getIndex(), new Box());
                    boxesRight.set(selectedBox.getIndex(), new Box());
                }

                if(currentMode.shuffle)
                    shuffleBoxes();
            } else {
                subScore();
                if(currentMode.singleFail) gameOver();
            }

            selectedBox.setSelected(false);
            selectedBox = new Box();

            boolean finished = true;
            for (Box box : boxesLeft) {
                if(!box.isNull()) {
                    finished = false;
                    break;
                }
            }

            if(finished) {
                if(currentMode.multiWave) {
                    nextWave();
                } else {
                    gameOver();
                }
            }
        }
    }

    public void menuAction(Menu menu) {
        String action = menu.getAction();
        String param = menu.getParam();

        if(action.equals("singleplayer")) {
            currentState = Constants.GameState.MODE;
        } else if(action.equals("highscore")) {
            highScoreMode = null;
            currentState = Constants.GameState.HIGHSCORE;
        } else if(action.equals("options")) {
            currentState = Constants.GameState.OPTIONS;
        } else if(action.equals("about")) {
            currentState = Constants.GameState.ABOUT;
        } else if(action.equals("mode")) {
            currentMode = Methods.getMode(param);
            ready();
        } else if(action.equals("showHighScores")) {
            highScoreMode = Methods.getMode(param);
            currentState = Constants.GameState.HIGHSCORE;
        } else if(action.equals("setOption")) {
            if(param.equals("music"))
                Assets.toggleMusic();
            else if(param.equals("sound"))
                Assets.toggleSound();
        } else if(action.equals("website")) {
            Gdx.net.openURI("http://www.chenarstudio.ir");
        } else if(action.equals("pause")) {
            currentState = Constants.GameState.PAUSE;
        } else if(action.equals("resume")) {
            currentState = Constants.GameState.RUNNING;
        } else if(action.equals("retry")) {
            ready();
        } else if(action.equals("home")) {
            currentState = Constants.GameState.MENU;
        } else if(action.equals("quit")) {
            Gdx.app.exit();
        }
    }

    private void initBoxes() {
        selectedBox = new Box();
        float x = Constants.BOX_X;
        float y = Constants.BOX_Y;
        boxesLeft = new Array<Box>();
        boxesRight = new Array<Box>();
        positions = new Array<Vector2>();
        Array<Color> colors = Assets.generateColors(currentMode);
        Color color;

        for(int i = 0; i < Constants.BOX_MAX; i++) {
            color = colors.get(MathUtils.random(0, colors.size - 1));
            boxesLeft.add(new Box(i, x, y, Constants.BOX_WIDTH, Constants.BOX_HEIGHT, color, Box.BoxSide.LEFT));
            positions.add(new Vector2(x, y));
            x += Constants.BOX_WIDTH + Constants.BOX_GAP;
            if(x >= (Constants.GAME_WIDTH / 2)) {
                x = Constants.BOX_X;
                y += Constants.BOX_HEIGHT + Constants.BOX_GAP;
            }
        }

        int i = 0;
        Vector2 position;
        Array<Vector2> usedPositions = new Array<Vector2>();
        for (Box box : boxesLeft) {
            position = positions.get(MathUtils.random(0, positions.size - 1));
            while (usedPositions.contains(position, false))
                position = positions.get(MathUtils.random(0, positions.size - 1));
            usedPositions.add(position);
            boxesRight.add(new Box(i, position.x + (Constants.GAME_WIDTH / 2), position.y, Constants.BOX_WIDTH, Constants.BOX_HEIGHT, box.getColor(), Box.BoxSide.RIGHT));
            i++;
        }
    }

    private void shuffleBoxes() {
        Box box;
        float x = Constants.BOX_X;
        float y = Constants.BOX_Y;
        Array<Box> boxesTemp = boxesLeft;
        Array<Box> usedBoxes = new Array<Box>();
        boxesLeft = new Array<Box>();
        boxesRight = new Array<Box>();
        positions = new Array<Vector2>();

        for(int i = 0; i < Constants.BOX_MAX; i++) {
            box = boxesTemp.get(MathUtils.random(0, boxesTemp.size - 1));
            while (usedBoxes.contains(box, false))
                box = boxesTemp.get(MathUtils.random(0, boxesTemp.size - 1));
            usedBoxes.add(box);
            if(box.isNull())
                boxesLeft.add(new Box());
            else
                boxesLeft.add(new Box(i, x, y, Constants.BOX_WIDTH, Constants.BOX_HEIGHT, box.getColor(), Box.BoxSide.LEFT));
            positions.add(new Vector2(x, y));
            x += Constants.BOX_WIDTH + Constants.BOX_GAP;
            if(x >= (Constants.GAME_WIDTH / 2)) {
                x = Constants.BOX_X;
                y += Constants.BOX_HEIGHT + Constants.BOX_GAP;
            }
        }

        int i = 0;
        Vector2 position;
        Array<Vector2> usedPositions = new Array<Vector2>();
        for (Box leftBox : boxesLeft) {
            position = positions.get(MathUtils.random(0, positions.size - 1));
            while (usedPositions.contains(position, false))
                position = positions.get(MathUtils.random(0, positions.size - 1));
            usedPositions.add(position);
            boxesRight.add(new Box(i, position.x + (Constants.GAME_WIDTH / 2), position.y, Constants.BOX_WIDTH, Constants.BOX_HEIGHT, leftBox.getColor(), Box.BoxSide.RIGHT));
            i++;
        }
    }

    public int getGameTime() {
        return currentMode.time;
    }

    public int getScore() {
        return score;
    }

    public float getTime() {
        if(isRunning())
            return runTime;
        return -1;
    }

    public Array<Box> getBoxes() {
        Array<Box> boxes = new Array<Box>();
        boxes.addAll(boxesLeft);
        boxes.addAll(boxesRight);
        return boxes;
    }

    public Array<Menu> getMenus() {
        menus = new Array<Menu>();

        if(isMenu())
        {
            menus = Menus.getMainMenus();
        } else if(isMode()) {
            menus = Menus.getMainMenus();
            menus.addAll(Menus.getModeMenus());
        } else if(isReady()) {
            menus = Menus.getReadyMenus(runTime, currentWave);
        } else if(isRunning()) {
            menus = Menus.getRunningMenus(getScore(), currentMode);
        } else if(isPause()) {
            menus = Menus.getPauseMenus(getScore(), currentMode);
        } else if(isGameOver()) {
            menus = Menus.getGameOverMenus(getScore(), currentMode);
        } else if(isHighScore()) {
            menus = Menus.getMainMenus();
            menus.addAll(Menus.getHighScoreMenus(highScoreMode));
        } else if(isOptions()) {
            menus = Menus.getMainMenus();
            menus.addAll(Menus.getOptionMenus());
        } else if(isAbout()) {
            menus = Menus.getMainMenus();
            menus.addAll(Menus.getAboutMenus());
        }

        return menus;
    }

    public boolean isMenu() {
        return currentState == Constants.GameState.MENU;
    }

    public boolean isMode() {
        return currentState == Constants.GameState.MODE;
    }

    public boolean isReady() {
        return currentState == Constants.GameState.READY;
    }

    public boolean isRunning() {
        return currentState == Constants.GameState.RUNNING;
    }

    public boolean isPause() {
        return currentState == Constants.GameState.PAUSE;
    }

    public boolean isGameOver() {
        return currentState == Constants.GameState.GAMEOVER;
    }

    public boolean isHighScore() {
        return currentState == Constants.GameState.HIGHSCORE;
    }

    public boolean isOptions() {
        return currentState == Constants.GameState.OPTIONS;
    }

    public boolean isAbout() {
        return currentState == Constants.GameState.ABOUT;
    }
}
