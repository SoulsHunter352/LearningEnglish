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
    private static final Pattern regex =
            Pattern.compile("(?:_[a-zа-я]+\\.\\s*|\\d+\\)\\s*)([а-яА-ЯёЁ][а-яА-ЯёЁ\\s\"\\-,]*?)(?=[,;]|$)");

    public static void loadDictionary(Context context){
        dictionary = loadFromAssets(context);
        keys = new ArrayList<>(dictionary.keySet());
    }

    private static Map<String, String> loadFromAssets(Context context){
        BufferedReader reader = null;
        Map<String, String> newDictionary = new HashMap<>();
        try{
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("mueller.dict"), StandardCharsets.UTF_8));
            String line;
            int translatedWords = 0;
            boolean isInWord = false;  // Параметр, который указывает, смотрим ли мы сейчас слово
            String currentWord = "";

            while((line = reader.readLine()) != null){
                if(!line.isEmpty() && Character.isLetter(line.charAt(0))){
                    isInWord = true;
                    currentWord = line;
                }
                else if(!line.isEmpty() && isInWord){
                    Matcher matcher = regex.matcher(line);
                    if(matcher.find()){
                        String translation = matcher.group(1);
                        newDictionary.put(currentWord, translation);
                        translatedWords += 1;
                        isInWord = false;
                    }
                }
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
