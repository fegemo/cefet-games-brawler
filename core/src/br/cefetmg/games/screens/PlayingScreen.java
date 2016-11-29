package br.cefetmg.games.screens;

import br.cefetmg.games.networking.NetworkObserver;
import br.cefetmg.games.networking.Server;
import br.cefetmg.games.player.ControlledPlayer;
import br.cefetmg.games.player.NetworkedPlayer;
import br.cefetmg.games.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingScreen extends BaseScreen implements NetworkObserver {

    // personagens
    private Texture hulkTexture;
    private ControlledPlayer myself;
    private Vector2 movement;
    private Array<NetworkedPlayer> enemies;

    // mapa
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final int[] backgroundLayers = new int[]{0, 1, 2};
    private final int[] behindCharactersLayers = new int[]{3, 4};
    private final int[] inFrontOfCharactersLayers = new int[]{5, 6};
    private float mapWidth;
    private MapObjects drowningObjects;

    public PlayingScreen(Game game) {
        super(game);
    }

    @Override
    public void create() {
        // mapa
        map = new TmxMapLoader().load("mapa.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        mapWidth = map.getProperties().get("width", Integer.class)
                * map.getProperties().get("tilewidth", Integer.class);
        drowningObjects = map.getLayers().get("afogamento").getObjects();
        
        // personagem
        hulkTexture = new Texture("hulk-16.png");
        myself = (ControlledPlayer) new ControlledPlayer("me", Color.WHITE)
                .attachToMap(hulkTexture, map, 50, 190);
        enemies = new Array<NetworkedPlayer>();
        newPlayer("fegemo", "133.13.3.1").attachToMap(hulkTexture, map, 30, 390);

        movement = new Vector2();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                    case Keys.A:
                        movement.x = -1;
                        break;
                    case Keys.RIGHT:
                    case Keys.D:
                        movement.x = 1;
                        break;
                    case Keys.UP:
                    case Keys.W:
                        movement.y = 1;
                        myself.avatar.jump();
                        break;
                    case Keys.DOWN:
                    case Keys.S:
                        movement.y = -1;
                        myself.avatar.climbDown();
                        break;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                    case Keys.A:
                    case Keys.RIGHT:
                    case Keys.D:
                        movement.x = 0;
                        break;
                    case Keys.UP:
                    case Keys.W:
                    case Keys.DOWN:
                    case Keys.S:
                        movement.y = 0;
                        break;
                }
                return false;
            }

        });
    }

    private NetworkedPlayer newPlayer(String name, String ipAddress) {
        Color color = new Color(MathUtils.random(2 ^ 24));
        NetworkedPlayer p = new NetworkedPlayer(name, color, ipAddress);
        enemies.add(p);
        return p;
    }

    @Override
    protected void handleInput() {
        // sai do jogo ao pressionar 'Esc'
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Keys.F1)) {
            Server.INSTANCE.start(this);
        }
    }

    private void updateCamera() {
        float screenWidthHalf = viewport.getScreenWidth() / 2f;
        camera.position.x = MathUtils.clamp(
                myself.avatar.sprite.getX(),
                screenWidthHalf, mapWidth - screenWidthHalf);

        camera.update();
    }
    
    private void checkIfDrowned() {
        for (MapObject o : drowningObjects) {
            Rectangle drowningRect = ((RectangleMapObject)o).getRectangle();
            if (drowningRect.overlaps(
                    myself.avatar.sprite.getBoundingRectangle())) {
                respawnPlayer(myself);
            }
        }
    }

    @Override
    public void update(float dt) {
        myself.avatar.update(dt);
        myself.move(movement.x, movement.y);
        checkIfDrowned();
        
        for (NetworkedPlayer net : enemies) {
            net.update(dt);
        }
        updateCamera();
    }

    @Override
    public void draw() {
        mapRenderer.setView(camera);
        mapRenderer.render(backgroundLayers);
        mapRenderer.render(behindCharactersLayers);
        batch.begin();
        myself.draw(batch);
        for (NetworkedPlayer net : enemies) {
            net.avatar.draw(batch);
        }
        batch.end();
        mapRenderer.render(inFrontOfCharactersLayers);
    }

    @Override
    public NetworkedPlayer newPlayerJoined(String ip, String name) {
        return (NetworkedPlayer) newPlayer(name, ip)
                .attachToMap(hulkTexture, map, 50, 200);
    }

    private void respawnPlayer(Player p) {
        p.avatar.setPosition(50, 200);
    }

}
