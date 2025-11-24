package com.example.learningenglish;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryManager {
    private static Map<String, String> dictionary = new HashMap<>();
    private static Random random = new Random();
    private static List<String> keys = new ArrayList<>();

    public static void loadDictionary(Context context){
        dictionary = loadFromAssets(context);
        keys = new ArrayList<>(dictionary.keySet());
    }
    private static Map<String, String> loadFromAssets(Context context){
        BufferedReader reader = null;
        List<String> lines = new ArrayList<>();
        Map<String, String> newDictionary = new HashMap<>();
        try{
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("mueller.dict"), StandardCharsets.UTF_8));
            String line;
            int lineIndex = 0;
            int translatedWords = 0;
            List<String> currentWordLines = new ArrayList<>();

            while((line = reader.readLine()) != null){
                // lines.add(line);
                if(!line.isEmpty() && Character.isLetter(line.charAt(0))){
                    if(!currentWordLines.isEmpty()){
                        String word = currentWordLines.get(0);
                        String translation = getTranslation(currentWordLines);
                        if(translation != null){
                            newDictionary.put(word, translation);
                            translatedWords += 1;
                        }
                        currentWordLines = new ArrayList<>();
                    }
                }
                currentWordLines.add(line);
                lineIndex += 1;
            }
            Log.d("Количество слов ", "" + translatedWords);
        }
        catch (IOException e){
            Log.d("Ошибка чтения", "Ошибка чтения файла");
        }
        finally {
            if (reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    Log.d("Ошибка при закрытии", "Ошибка при закрытии буфера чтения");
                }
            }
        }
        return newDictionary;
    }

    private static Map<String, String> parseData(List<String> lines){
        Map<String, String> newDictionary = new HashMap<>();
        int lineIndex = 0;
        int translatedWords = 0;
        List<String> currentWordLines = new ArrayList<>();
        while(lineIndex < lines.size()){
            String line = lines.get(lineIndex);
            if(!line.isEmpty() && Character.isLetter(line.charAt(0))){
                if(!currentWordLines.isEmpty()){
                    String word = currentWordLines.get(0);
                    String translation = getTranslation(currentWordLines);
                    if(translation != null){
                        newDictionary.put(word, translation);
                        translatedWords += 1;
                    }
                    currentWordLines = new ArrayList<>();
                }
            }
            currentWordLines.add(line);
            lineIndex += 1;
        }
        Log.d("Количество слов ", "" + translatedWords);
        return newDictionary;
    }

    private static String getTranslation(List<String> wordLines){
        String pattern = "(?:_[a-zа-я]+\\.\\s*|\\d+\\)\\s*)([а-яА-ЯёЁ][а-яА-ЯёЁ\\s\"\\-,]*?)(?=[,;]|$)";
        Pattern regex = Pattern.compile(pattern);
        for(String line: wordLines){
            Matcher matcher = regex.matcher(line);
            if(matcher.find()){
                return matcher.group(1);
            }
        }
        return null;
    }

    public static String getRandomWord(){
        return keys.get(random.nextInt(keys.size()));
    }

    public static String getTranslation(String word){
        return dictionary.getOrDefault(word, "");
    }

    public static int getDictSize(){
        return keys.size();
    }
}
