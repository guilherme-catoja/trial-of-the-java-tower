package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.objects.JumperGuy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GameEnd {
    public void gameOver(){
        ImageGUI gui = ImageGUI.getInstance();
        gui.showMessage("Game over", "You lost!");
        gui.dispose();
        System.exit(0);
    }

    public void gameWon(JumperGuy jumperGuy, int TIME_LIMIT){
        ImageGUI gui = ImageGUI.getInstance();
        gui.showMessage("Game won", "Congratulations! You saved the princess!");

        //Calculate player score
        int base_score = jumperGuy.getScore();
        int player_hp = jumperGuy.getHp();
        int time_taken = gui.getTicks();

        int time_score = 0;
        if(time_taken <= TIME_LIMIT){
            time_score = (TIME_LIMIT-time_taken)*5;
        }

        int final_score = base_score + time_score;



        String player_name = gui.showInputDialog("Game won", "Base score: " + base_score +
                "\n Health points: " + player_hp +
                "\n Time taken: " + time_taken / 2 + " seconds" +
                "\n Time bonus: " + time_score +
                "\n \n Final score: " + final_score +
                "\n \n Write your name");

        List<String> highScores = loadHighScores();
        List<String> updatedHighScores = saveHighScore(highScores, player_name, final_score);
        writeHighScores(updatedHighScores);

        String printedScores = String.join("\n", highScores);

        gui.showMessage("Game won", "Leaderboard: \n" + printedScores);
        gui.dispose();
        System.exit(0);
    }

    public List<String> loadHighScores() {
        List<String> highScores = new ArrayList<>();
        try{
            Scanner fileScanner = new Scanner(new File("saves/HighScores.txt"));
            while ((fileScanner.hasNextLine())) {
                highScores.add(fileScanner.nextLine());
            }
            fileScanner.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Couldn't find saves file");
        }
        return highScores;
    }

    public List<String> saveHighScore(List<String> highScores, String player_name, int player_score) {
        Map<String, Integer> nameScoreMap = new HashMap<>();
        for (String highScore : highScores) {
            String[] parts = highScore.split(": ");
            if (parts.length == 2) {
                String name = parts[0].trim();
                int score = Integer.parseInt(parts[1].trim());
                nameScoreMap.put(name, score);
            }
        }
        nameScoreMap.put(player_name, player_score);

        // Get a sorted list of entries by score in descending order
        List<Map.Entry<String, Integer>> sortedEntries = nameScoreMap.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())) // Descending order
                .limit(10) // Limit to top 10
                .collect(Collectors.toList());

        // Clear the high scores list and update it with the top 10
        highScores.clear();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            highScores.add(entry.getKey() + ": " + entry.getValue());
        }
        return highScores;
    }

    public void writeHighScores(List<String> highScores) {
        try {
            PrintWriter fileWriter = new PrintWriter(new File("saves/HighScores.txt"));
            for (String highScore : highScores) {
                fileWriter.println(highScore);
            }
            fileWriter.close();
            System.out.println("High scores saved");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find saves file");
        }
    }
}
