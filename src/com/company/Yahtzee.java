package com.company;

import java.util.ArrayList;
import java.util.List;

public class Yahtzee {
    private final List<Player> players = new ArrayList<>();
    private final int AMOUNT_OF_ROLLS_PER_TURN = 3;
    private final int ROUNDS;
    private final int MAX_PLAYERS = 6;
    private final int MIN_PLAYERS = 1;


    public Yahtzee() {
        setup();
        ROUNDS = (players.size() * 13);
    }
    private void setup() {
        Cup gamesCup = new Cup();
        int numberOfPlayers = 0;

        do {
            numberOfPlayers = Console.getNumberInput("How many players are playing? ");
            Console.nextLine();
        } while (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS);


        for (int i = 0; i < numberOfPlayers; i++) {
            String name = Console.getStringInput("\nPlayer" + (i + 1) + " name: ");
            players.add(new Player(name, gamesCup));
        }
    }
    public void runGame() {
        int roundNumber = 0;

        while (roundNumber < ROUNDS) {
            runRound(roundNumber);
            roundNumber++;
        }
        determineWinner();
    }
    private void runRound(int roundNumber) {
        for (Player activePlayer : players) {
            Console.displayMessage("\n\n-----------------------------------------------------------------------\n");
            Console.displayMessage("\nCurrent round: " + (roundNumber + 1));
            Console.displayMessage("\nCurrent Player Turn: " + activePlayer.getName() + "\n\n");
            runTurn(activePlayer);
        }
    }
    private void runTurn(Player currentPlayer) {
        int currentRoll = 0;
        Console.displayMessage(String.format("New turn, it is %s('s) turn !! %n", currentPlayer.getName()));
        currentPlayer.cup.roll();
        while (currentRoll < AMOUNT_OF_ROLLS_PER_TURN) {
            Console.displayMessage("\nCurrent roll: " + (currentRoll + 1) + "\n");
            showDiceAndPossibleScores(currentPlayer);
            if (currentRoll < 2) {
                String decision = makeDecision("\nTo re-roll type 'roll', otherwise please enter the name of the row to mark for your score (Ex: ones or Full House): ");
                if (decision.equals("roll")) {
                    currentPlayer.cup.roll(pickDice(currentPlayer.getName()));
                } else {
                    currentPlayer.markScore(decision);
                    currentPlayer.scoreCard.displayScoreCard();
                    return;
                }
            }else{
                currentPlayer.scoreCard.displayScoreCard();
                String key = makeDecision("\nPlease enter the name of the row to mark for your score (Ex: ones or Full House): ");
                currentPlayer.markScore(key);
                currentPlayer.scoreCard.displayScoreCard();
            }
            currentRoll++;
        }
    }
    private String makeDecision(String message){
        String decision = Console.getStringInput(message);
        boolean isValidDecision = isValidDecision(decision);
        while(!isValidDecision){
            decision = Console.getStringInput(message);
            isValidDecision = isValidDecision(decision);
        }
        return decision.trim();
    }
    private void determineWinner() {
        Player winner = players.get(0);

        for (Player player : players) {
            if (winner.scoreCard.rows.get("Total") < player.scoreCard.rows.get("Total"))
                winner = player;

            Console.displayMessage(String.format("Name: %s | Score = %s %n", player.getName(), player.scoreCard.rows.get("Total")));
        }
        Console.displayMessage(String.format("Our winner is %s with a score of %s !!!", winner.getName(), winner.scoreCard.rows.get("Total")));
    }
    private List<Integer> pickDice(String name) {
        String temp = Console.getNumbersToReRoll(name);
        return Console.parseUserSelections((temp == null) ? "0" : temp);
    }
    private boolean isValidDecision(String decision){
        ScoreCard temp = new ScoreCard();
        decision = decision.trim();
        return temp.rows.containsKey(decision) || decision.equals("roll");
    }
    private void showDiceAndPossibleScores(Player currentPlayer){
        Console.displayMessage("\n" + currentPlayer.cup.displayDice());
        Console.displayMessage("\nThese are your current possible scores: \n");
        currentPlayer.displayPossibleScores();
    }

}
