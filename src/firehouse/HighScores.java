package firehouse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public final class HighScores {
    private final int[] best = {-1, -1, -1};
    private final File file = new File(System.getProperty("user.home"), "FirehouseHighScores.txt");

    public HighScores() {
        try (Scanner input = new Scanner(file)) {
            for (int i = 0; i < best.length && input.hasNextInt(); i++) {
                best[i] = input.nextInt();
            }
        } catch (FileNotFoundException e) {
        }
    }

    public String get(Difficulty difficulty) {
        int score = best[difficulty.ordinal()];
        return score < 0 ? "---" : score + "%";
    }

    public boolean record(Difficulty difficulty, int score) {
        int index = difficulty.ordinal();
        if (score <= best[index]) {
            return false;
        }
        best[index] = score;
        try (PrintWriter output = new PrintWriter(file)) {
            for (int value : best) {
                output.println(value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not save high scores.");
        }
        return true;
    }
}
