package com.daveguy.matchmaker;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private final String newLine = System.getProperty("line.separator");
    private final String GAMES = "games";
    private final String PLAYERS = "players";

    private String gamesList;
    private String playerList;
    private String NOT_ENOUGH_PLAYERS;
    private String NOT_ENOUGH_GAMES;
    private GroupsMaker groupsMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NOT_ENOUGH_PLAYERS = getString(R.string.not_enough_players);
        NOT_ENOUGH_GAMES = getString(R.string.not_enough_machines);

        groupsMaker = new GroupsMaker();
        gamesList = load(GAMES);
        playerList = load(PLAYERS);
        EditText text = (EditText) findViewById(R.id.games_list);
        text.setText(gamesList);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView t = (TextView) findViewById(R.id.games_saved_text);
                t.setVisibility(View.INVISIBLE);
                Button b = (Button) findViewById(R.id.games_list_save);
                b.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        text = (EditText) findViewById(R.id.player_list);
        text.setText(playerList);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView t = (TextView) findViewById(R.id.players_saved_text);
                t.setVisibility(View.INVISIBLE);
                Button b = (Button) findViewById(R.id.player_list_save);
                b.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void toGames(View view){
        findViewById(R.id.match).setVisibility(View.INVISIBLE);
        findViewById(R.id.players).setVisibility(View.INVISIBLE);
        findViewById(R.id.games).setVisibility(View.VISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()));
    }

    public void toPlayers(View view){
        findViewById(R.id.match).setVisibility(View.INVISIBLE);
        findViewById(R.id.players).setVisibility(View.VISIBLE);
        findViewById(R.id.games).setVisibility(View.INVISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()));
    }

    public void Match(View view) {
        findViewById(R.id.match).setVisibility(View.VISIBLE);
        findViewById(R.id.players).setVisibility(View.INVISIBLE);
        findViewById(R.id.games).setVisibility(View.INVISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()));
    }

    public void makeMatches(View view){
        findViewById(R.id.match_list).setVisibility(View.VISIBLE);

        TextView matches = (TextView) findViewById(R.id.match_list);

        EditText playersText = (EditText) findViewById(R.id.player_list);
        LinkedList<String[]> playerGroups = groupsMaker.getPlayerGroups(playersText.getText().toString().split(newLine));
        if(playerGroups == null){
            matches.setText(NOT_ENOUGH_PLAYERS);
            return;
        }
        LinkedList<String[]> gameBanks = groupsMaker.getGamePicks(playerGroups.size(), gamesList.split(newLine));
        if(gameBanks == null){
            matches.setText(NOT_ENOUGH_GAMES);
            return;
        }
        StringBuilder outString = new StringBuilder();
        for(int i = 0; i < playerGroups.size(); i++){
            outString.append("Group ").append(i).append(newLine).append("\t").append(Arrays.toString(playerGroups.get(i))).append(newLine).append("\t").append(Arrays.toString(gameBanks.get(i)));
            if(i != playerGroups.size()-1){
                outString.append(newLine);
            }
        }
        matches.setText(outString);
    }

    private String load(String fileIn){
        byte[] bytes = new byte[0];
        try {
            File file = new File(getFilesDir(), fileIn);
            FileInputStream in = new FileInputStream(file);
            bytes = new byte[(int)file.length()];
            in.read(bytes);
            in.close();
        }
        catch(Exception e){
            //first time there will be no file, that's fine.
            System.out.println("crashed here");
        }

        return new String(bytes);
    }

    public void resetTextGames(View view){
        EditText text = (EditText) findViewById(R.id.games_list);
        text.setText(null);
    }

    public void resetTextPlayers(View view){
        EditText text = (EditText) findViewById(R.id.player_list);
        text.setText(null);
    }

    public void saveGames(View view){
        view.setVisibility(View.INVISIBLE);
        TextView saved = (TextView) findViewById(R.id.games_saved_text);
        saved.setVisibility(View.VISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()));

        EditText text = (EditText) findViewById(R.id.games_list);
        String textString = text.getText().toString();
        gamesList = textString;
        File file = new File(getFilesDir(), GAMES);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(textString.getBytes());
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void savePlayers(View view){
        view.setVisibility(View.INVISIBLE);
        TextView saved = (TextView) findViewById(R.id.players_saved_text);
        saved.setVisibility(View.VISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()));

        EditText text = (EditText) findViewById(R.id.player_list);
        String textString = text.getText().toString();
        playerList = textString;
        File file = new File(getFilesDir(), PLAYERS);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(textString.getBytes());
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
