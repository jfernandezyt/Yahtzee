package com.company;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    private final String name;
    public  ScoreCard scoreCard = new ScoreCard();
    public Cup cup;

    public Player(String name, Cup cup) {
        this.name = name;
        this.cup = cup;
    }

    public void displayPossibleScores() {
        LinkedHashMap<String, Integer> possibleScores = scoreCard.getPossibleScores(cup);
        AtomicInteger label = new AtomicInteger(1);

        possibleScores.forEach((key, value) -> {
            Console.displayMessage(String.format("%s) %s: %s %n", label.getAndIncrement(), key, value));
        });
    }

    public void markScore(String key) {
        LinkedHashMap<String, Integer> possibleScores = scoreCard.getPossibleScores(cup);
        if (possibleScores.containsKey(key)) {
            if (possibleScores.get(key) == null) {
                scoreCard.markScore(key, 0);
            } else {
                int value = possibleScores.get(key);
                scoreCard.markScore(key, value);
            }
        }else{
            scoreCard.markScore(key, 0);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", scoreCard=" + scoreCard +
                '}';
    }

    public String getName() {
        return name;
    }
}
