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

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        Log.d("vivian", "hi");
        return words.contains(word);
    }

    public String binarySearchDict(String word, ArrayList dict) {
        Log.d("vivian", "running binary");
        int min = 0;
        int max = dict.size() - 1;
        int mid = (int) ((max + 1 - min) / 2);
        String value = (String)(dict.get(mid));

//        if (dict.isEmpty()) {
//            Log.d("vivian", "55");
//            return false;
//        }

        if (value.toLowerCase().contains(word.toLowerCase())) {
            Log.d("vivian", "44");
            return value;
        }

        if ((value).equals(word)) {
            Log.d("vivian", "33");
            return value;
        }

        // if mid is "b.." and word is "a"
        if ((value).compareTo(word) > 0) {
            max = mid - 1;
            ArrayList<String> newdict = new ArrayList<String> (dict.subList(min, max));
            Log.d("vivian", "11");
            return binarySearchDict(word, newdict);
        }

        if ((value).compareTo(word) < 0) {
            min = mid + 1;
            ArrayList<String> newdict = new ArrayList<String> (dict.subList(min, max));
            Log.d("vivian", "22");
            return binarySearchDict(word, newdict);
        }
        return "-1";

    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Log.d("vivian", "time 2");
        // if string is empty
        if (prefix == "") {
            Random random = new Random();
            int num = random.nextInt(words.size());
            return words.get(num);
        }

        // if prefix is a word
        else if (prefix.length() > 3 && isWord(prefix)) {
            return prefix;
        }

        // if string is not empty, complete binary search for next word
        // if no such word, return null
        else {
            Log.d("vivian", "here");
            if (binarySearchDict(prefix, words) == "-1") {
                Log.d("vivian", "apple");
//                Log.d("vivian", ((String) binarySearchDict(prefix, words)));
                return null;
            } else {
                Log.d("vivian", "swift");
//                Log.d("vivian", ((String) binarySearchDict(prefix, words)));
                return binarySearchDict(prefix, words);
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
