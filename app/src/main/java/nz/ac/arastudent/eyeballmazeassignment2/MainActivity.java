package nz.ac.arastudent.eyeballmazeassignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import nz.ac.arastudent.eyeballmazeassignment2.model.IGame;
import nz.ac.arastudent.eyeballmazeassignment2.model.Model;

public class MainActivity extends AppCompatActivity {

    Button[][] buttons = new Button[6][4];
    String THE_MAP = "";
    String MOVES = "";
    String GOALS_LEFT = "";

    SharedPreferences sharedPreferences = null;

    public IGame myModel = new Model();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set tool bar
        Toolbar myToolbar = findViewById(R.id.game_toolbar);
        setSupportActionBar(myToolbar);

        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText("0");


        //Set button
        buttons[0][0] = (Button)findViewById(R.id.grid00);
        buttons[0][1] = (Button)findViewById(R.id.grid10);
        buttons[0][2] = (Button)findViewById(R.id.grid20);
        buttons[0][3] = (Button)findViewById(R.id.grid30);

        buttons[1][0] = (Button)findViewById(R.id.grid01);
        buttons[1][1] = (Button)findViewById(R.id.grid11);
        buttons[1][2] = (Button)findViewById(R.id.grid21);
        buttons[1][3] = (Button)findViewById(R.id.grid31);

        buttons[2][0] = (Button)findViewById(R.id.grid02);
        buttons[2][1] = (Button)findViewById(R.id.grid12);
        buttons[2][2] = (Button)findViewById(R.id.grid22);
        buttons[2][3] = (Button)findViewById(R.id.grid32);

        buttons[3][0] = (Button)findViewById(R.id.grid03);
        buttons[3][1] = (Button)findViewById(R.id.grid13);
        buttons[3][2] = (Button)findViewById(R.id.grid23);
        buttons[3][3] = (Button)findViewById(R.id.grid33);

        buttons[4][0] = (Button)findViewById(R.id.grid04);
        buttons[4][1] = (Button)findViewById(R.id.grid14);
        buttons[4][2] = (Button)findViewById(R.id.grid24);
        buttons[4][3] = (Button)findViewById(R.id.grid34);

        buttons[5][0] = (Button)findViewById(R.id.grid05);
        buttons[5][1] = (Button)findViewById(R.id.grid15);
        buttons[5][2] = (Button)findViewById(R.id.grid25);
        buttons[5][3] = (Button)findViewById(R.id.grid35);


        this.initialiseGame();
    }

    private void readALevel() {
        final String originalFileSrc = "level.txt";
        try {
            InputStream inputStream;
            inputStream = getResources().openRawResource(R.raw.level);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            int j = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] line2 = line.split(",");
                for (int i=0; i<line2.length; i++) {
                    line2[i] = line2[i].replace("\"", "");
                    myModel.setMazeCharacter(i, j, line2[i]);
                }
                j++;
            }
            //don't forget to close!
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadLevel() {
        this.sharedPreferences = getSharedPreferences("nz.ac.arastudent." +
                "eyeballmazeassignment2.savedLevel.txt", Context.MODE_PRIVATE);
        String map = sharedPreferences.getString(THE_MAP, "None");

        String[] rows = Objects.requireNonNull(sharedPreferences.
                getString(THE_MAP, "None")).split(":");

        int y = 0;
        for (String aRow : rows){
            String[] blocks = aRow.split(",");

            int x = 0;
            for (String aBlock : blocks){
                System.out.println(aBlock + "()");
                myModel.setMazeCharacter(x, y, aBlock);
                x++;
            }
            y++;
        }
        System.out.println(sharedPreferences.getString(MOVES, "None"));
       // myModel.setMoveCount(sharedPreferences.getString(MOVES, "None"));
        //myModel.setGoalCount(sharedPreferences.getString(GOALS_LEFT, "None"));

        Toast.makeText(MainActivity.this, "Game Loaded!", Toast.LENGTH_SHORT).show();
    }

    private void loadMoves() {
        this.sharedPreferences = getSharedPreferences("nz.ac.arastudent." +
                "eyeballmazeassignment2.savedLevel.txt", Context.MODE_PRIVATE);
        myModel.setMoveCount(sharedPreferences.getString(MOVES, "None"));
    }
    private void saveLevel() {
        String[][] currentState = myModel.getGameMap();
        StringBuilder myMap = new StringBuilder();

        for (int y = 0; y < currentState.length; ++y) {
            for (int x = 0; x < currentState[y].length; ++x) {
                String pos = currentState[y][x];
                System.out.println(pos + ":");
                myMap.append(pos);
                if (x != currentState[y].length) {
                    myMap.append(",");
                }
            }
            if (y != currentState.length - 1){
                myMap.append(":");
            }
        }

        this.sharedPreferences = getSharedPreferences("nz.ac.arastudent." +
                "eyeballmazeassignment2.savedLevel.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        //editor.clear();

        editor.putString(THE_MAP, myMap.toString());
        editor.apply();
        editor.putString(GOALS_LEFT, myModel.getGoalCount());
        editor.apply();
        editor.putString(MOVES, myModel.getMoveCount());
        editor.apply();

        Toast.makeText(MainActivity.this, "Game Saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_load){
            loadLevel();
            this.myModel.updateMaze();
            updateGame();
            loadMoves();
            this.myModel.updateMaze();
            updateGame();
        }

        if(id == R.id.action_save){
            saveLevel();
        }

        if(id == R.id.action_restart){
            readALevel();
            this.myModel.updateMaze();
            updateGame();
        }

        if(id == R.id.action_help){
            Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(helpIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateGame(){
        for(int y = 0; y < this.buttons.length; y++){
            for(int x = 0; x < this.buttons[y].length; x++){
                Button aButton = this.buttons[y][x];
                aButton.setText(this.myModel.getItem(x, y));
            }
        }
        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText(myModel.getGoalCount());

        TextView moveCounter = findViewById(R.id.MoveCounter);
        moveCounter.setText(myModel.getMoveCount());
    }

    public void checkMove(int x, int y){
        this.myModel.updateMaze();
        Integer[] currentPos = this.myModel.getPlayerLocation();
        int currentX = currentPos[0];
        int currentY = currentPos[1];
        String direction;
        int distance = 0;
        if (x == currentX && y == currentY){
            Toast.makeText(getApplicationContext(),
                    "You are already on this position", Toast.LENGTH_SHORT).show();
        }
       // else if (y < currentY && x < currentX || y > currentY && x > currentX) {
         //   Toast.makeText(getApplicationContext(),
           //         "You can only move left, right or forward", Toast.LENGTH_SHORT).show();
        //}
        else {
            if (y < currentY) {
                direction = "W";
                distance = currentY - y;
            } else if (y > currentY) {
                direction = "S";
                distance = y - currentY;
            } else if (x < currentX) {
                direction = "A";
                distance = currentX - x;
            } else {
                direction = "D";
                distance = x - currentX;
            }

            this.myModel.updateMaze();


            //check move isn't backwards
            String isBackwards = this.myModel.makeMove(direction, distance);

            if (!isBackwards.equals("")) {
                Toast.makeText(getApplicationContext(),
                        isBackwards, Toast.LENGTH_SHORT).show();
            }
            //check if complete
            if (myModel.isComplete()){
                Toast.makeText(getApplicationContext(),
                        "You have solved it", Toast.LENGTH_SHORT).show();
            }
            updateGame();
        }
    }

    public void initialiseGame(){
        this.myModel.updateMaze();
        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText(myModel.getGoalCount());

        TextView moveCounter = findViewById(R.id.MoveCounter);
        moveCounter.setText(myModel.getMoveCount());

        for(int y = 0; y < this.buttons.length; y++){
            for(int x = 0; x < this.buttons[y].length; x++){
                Button aButton = this.buttons[y][x];
                aButton.setText(this.myModel.getItem(x, y));
                final int weirdX = x;
                final int weirdY = y;
                aButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        checkMove(weirdX, weirdY);
                    }
                });
            }
        }
    }


}
