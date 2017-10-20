package ir.chenarstudio.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import ir.chenarstudio.gameobjects.Box;
import ir.chenarstudio.gameobjects.Menu;
import ir.chenarstudio.helpers.Constants;
import ir.chenarstudio.helpers.InputHandler;
import ir.chenarstudio.helpers.Methods;

public class GameRenderer {

    private GameWorld world;
    private OrthographicCamera cam;
    private ShapeRenderer renderer;
    private SpriteBatch batcher;

    public GameRenderer(GameWorld world) {
        this.world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        Gdx.input.setInputProcessor(new InputHandler(cam, world));
    }

    public void render() {
        drawBackground(0, 0, 0, 1);

        if (world.isRunning())
            drawRunning();

        drawMenus();
    }

    private void drawMenus() {
        Array<Menu> menus = world.getMenus();
        for(Menu menu : menus) {
            Methods.print(batcher, menu.getName(), menu.getX(), menu.getY());
        }
    }

    private void drawRunning() {
        for(Box box : world.getBoxes()) {
            renderer.begin(ShapeType.Filled);
            renderer.setColor(box.getColor());
            if(box.isSelected())
                renderer.circle(box.getX() + (box.getWidth() / 2), box.getY() + (box.getHeight() / 2), Constants.BOX_RADIUS);
            else
                renderer.rect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
            renderer.end();
        }
    }

    private void drawBackground(float r, float g, float b, float a)
    {
        Gdx.gl.glClearColor(r, g, b, a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeType.Filled);
        renderer.setColor(new Color(1 - r, 1 - g, 1 - b, 1));
        renderer.rect((Constants.GAME_WIDTH / 2) - 2.5f, Constants.GAME_TOP, 2, Constants.GAME_HEIGHT);
        renderer.rect((Constants.GAME_WIDTH / 2) + 2.5f, Constants.GAME_TOP, 2, Constants.GAME_HEIGHT);
        renderer.rect(0, Constants.GAME_TOP - 5, Constants.GAME_WIDTH, 5);

        float time = world.getTime();
        float x, y, height;
        if(time == -1) {
            x = (Constants.GAME_WIDTH / 2) - 2.5f;
            y = Constants.GAME_TOP;
            height = (Constants.GAME_HEIGHT - Constants.GAME_TOP);
        } else {
            x = (Constants.GAME_WIDTH / 2) - 2.5f;
            y = Constants.GAME_TOP;
            height = (time * (Constants.GAME_HEIGHT - Constants.GAME_TOP)) / world.getGameTime();
        }

        renderer.rect(x, y, 5, height);
        renderer.rect(0, Constants.GAME_TOP - 5, Constants.GAME_WIDTH, 5);
        renderer.end();
    }

}
