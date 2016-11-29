package br.cefetmg.games;

import br.cefetmg.games.screens.ConnectScreen;
import br.cefetmg.games.screens.PlayingScreen;
import com.badlogic.gdx.Game;

public class Brawler extends Game {

    @Override
    public void create() {
        setScreen(new ConnectScreen(this));
//        setScreen(new PlayingScreen());
    }
}
