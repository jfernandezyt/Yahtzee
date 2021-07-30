package com.company;

import java.util.*;

public class ScoreCard {
    public LinkedHashMap<String, Integer> rows;
    private int bonusFieldsTracker = 0;

    public ScoreCard() {
        rows = new LinkedHashMap<>();
        rows.put("ones", null);
        rows.put("twos", null);
        rows.put("threes", null);
        rows.put("fours", null);
        rows.put("fives", null);
        rows.put("sixes", null);
        rows.put("bonus", null);
        rows.put("3 of a kind", null);
        rows.put("4 of a kind", null);
        rows.put("Full House", null);
        rows.put("SM straight", null);
        rows.put("LG straight", null);
        rows.put("Yahtzee", null);
        rows.put("Chance", null);
        rows.put("Total", null);
    }

    public LinkedHashMap<String, Integer> getPossibleScores(Cup currentCup) {
        LinkedHashMap<Integer, Integer> freq = new LinkedHashMap<>();

        currentCup.dice.stream()
                .map(die -> die.faceUpValue)
                .forEach((dieValue) -> {
                    if (!freq.containsKey(dieValue)) {
                        freq.put(dieValue, 1);
                    } else {
                        freq.put(dieValue, freq.get(dieValue) + 1);
                    }
                });

        LinkedHashMap<String, Integer> possibleScoresUpper = calculateUpperSection(freq);
        LinkedHashMap<String, Integer> possibleScoresLower = calculateLowerSection(freq);
        return merge(possibleScoresUpper, possibleScoresLower);
    }
    private LinkedHashMap<String, Integer> calculateUpperSection(LinkedHashMap<Integer, Integer> freq) {
        LinkedHashMap<String, Integer> possibleScores = new LinkedHashMap<>();

        for (Map.Entry entry : freq.entrySet()) {
            if ((Integer) entry.getKey() == 1 && rows.get("ones") == null) {
                possibleScores.put("ones", (Integer) entry.getValue());
            } else if ((Integer) entry.getKey() == 2 && rows.get("twos") == null) {
                possibleScores.put("twos", ((Integer) entry.getValue()) * 2);
            } else if ((Integer) entry.getKey() == 3 && rows.get("threes") == null) {
                possibleScores.put("threes", ((Integer) entry.getValue()) * 3);
            } else if ((Integer) entry.getKey() == 4 && rows.get("fours") == null) {
                possibleScores.put("fours", ((Integer) entry.getValue()) * 4);
            } else if ((Integer) entry.getKey() == 5 && rows.get("fives") == null) {
                possibleScores.put("fives", ((Integer) entry.getValue()) * 5);
            } else if ((Integer) entry.getKey() == 6 && rows.get("sixes") == null) {
                possibleScores.put("sixes", ((Integer) entry.getValue()) * 6);
            }
        }

        return possibleScores;
    }
    private LinkedHashMap<String, Integer> calculateLowerSection(LinkedHashMap<Integer, Integer> freq) {
        LinkedHashMap<String, Integer> possibleScores = new LinkedHashMap<>();
        int sum = sum(freq);
        if (freq.containsValue(5)) {
            if (rows.get("Yahtzee") != null)
                rows.put("Yahtzee #2", 100);
            else
                possibleScores.put("Yahtzee", 50);
        } else if (freq.size() > 3 && rows.get("SM straight") == null || freq.size() > 4 && rows.get("LG straight") == null) {
            if (freq.size() == 5)
                possibleScores.put("LG straight", 40);
            else if (freq.size() == 4) {
                if (isSmallStraight(new ArrayList<>(freq.keySet())))
                    possibleScores.put("SM straight", 30);

            }
        } else if (rows.get("Full House") == null && freq.containsValue(3) && freq.containsValue(2)) {
            possibleScores.put("Full House", 25);
            if (rows.get("3 of a kind") == null)
                possibleScores.put("3 of a kind", sum);

        } else if (rows.get("4 of a kind") == null && freq.containsValue(4) ||
                rows.get("3 of a kind") == null && freq.containsValue(3)) {

            if (freq.containsValue(4)) {
                possibleScores.put("4 of a kind", sum);
                if (rows.get("3 of a kind") == null)
                    possibleScores.put("3 of a kind", sum);

            } else if (freq.containsValue(3))
                possibleScores.put("3 of a kind", sum);

        }
        if (rows.get("Chance") == null) possibleScores.put("Chance", sum);
        return possibleScores;
    }
    private LinkedHashMap<String, Integer> merge(LinkedHashMap<String, Integer> first, LinkedHashMap<String, Integer> second) {
        LinkedHashMap<String, Integer> newList = new LinkedHashMap<>();

        first.forEach(newList::put);
        second.forEach(newList::put);

        return newList;
    }
    public void markScore(String key, int value) {
        rows.put(key, value);
        if(bonusFieldsTracker < 63)
            bonusPointsTrackerUpdate();
        totalPointsUpdate();
    }
    private boolean isSmallStraight(List<Integer> list) {
        boolean isSMStraight = false;
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            if (i == 3) {
                break;
            } else {
                if (list.get(i + 1) - list.get(i) == 1) {
                    isSMStraight = true;
                } else {
                    isSMStraight = false;
                    break;
                }
            }

        }
        return isSMStraight;
    }
    public void displayScoreCard() {
        Console.displayMessage("\n\n");
        for (Map.Entry<String, Integer> entry : rows.entrySet()) {
            if (entry.getValue() != null) {
                Console.displayMessage(String.format("%s: %s", entry.getKey(), entry.getValue()));
            } else {
                Console.displayMessage(String.format("%s: %s", entry.getKey(), ""));
            }
            Console.displayMessage("\n_______________________________________\n");
        }
    }
    private void bonusPointsTrackerUpdate() {
        int counter = 1;
        int bonusPoints = 0;
        for (Map.Entry entry : rows.entrySet()) {
            System.out.println("Entry -------------->" + entry);
            if (entry.getValue() != null) {
                bonusPoints += (Integer) entry.getValue();
            }

            counter++;

            if (counter > 6) break;
        }
        bonusFieldsTracker = bonusPoints;
    }
    private void totalPointsUpdate(){
        int totalPoints = 0;
        if(bonusFieldsTracker >  62) rows.put("bonus", 35);
        for(Map.Entry entry : rows.entrySet()){
            if(entry.getKey() != "Total"){
                if(entry.getValue() != null)
                    totalPoints += (Integer) entry.getValue();
            }
        }
        rows.put("Total", totalPoints);
    }
    private int sum(LinkedHashMap<Integer, Integer> freq){
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            sum += (entry.getKey() * entry.getValue());
        }
        return sum;
    }

    @Override
    public String toString() {
        return "ScoreCard{" +
                "rows=" + rows +
                ", bonusFieldsTracker=" + bonusFieldsTracker +
                '}';
    }
}