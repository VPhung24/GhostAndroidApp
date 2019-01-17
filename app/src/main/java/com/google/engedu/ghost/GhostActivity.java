/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String topText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        // initialize button values
        Button buttonForChallenge = (Button) findViewById(R.id.challengeButton);
        buttonForChallenge.setText("Challenge");

        Button buttonForReset = (Button) findViewById(R.id.resetButton);
        buttonForReset.setText("Reset");

        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView mainText = (TextView) findViewById(R.id.ghostText);
        if (!userTurn) {
            if (topText.length() == 0) {
                String word = dictionary.getAnyWordStartingWith(topText);
                topText += word.charAt(0);
                mainText.setText(topText);
                Log.d("vivian", "pi");

                label.setText(USER_TURN);
                userTurn = true;
            } else if (topText.length() > 3 && dictionary.isWord(topText)) {
                label.setText("This is a word. The computer challenges and wins!!");
                Log.d("vivian", "2");
            } else if (dictionary.getAnyWordStartingWith(topText) == null) {
                label.setText("This can't be a word. The computer challenges and wins!!");
                Log.d("vivian", "3");
            } else if (dictionary.getAnyWordStartingWith(topText) != null) {
                Log.d("vivian", "shrimp");
                String word = dictionary.getAnyWordStartingWith(topText);
                int strLength = topText.length();
                topText += word.charAt(strLength);
                mainText.setText(topText);

                label.setText(USER_TURN);
                userTurn = true;
            }
        }
        Log.d("vivian", "turn over");
    }

    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode > 28 && keyCode < 54 && userTurn) {
            char characterKey = (char) event.getUnicodeChar();
            topText += characterKey;
            TextView mainText = (TextView) findViewById(R.id.ghostText);
            mainText.setText(topText);

            userTurn = false;
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }
}
