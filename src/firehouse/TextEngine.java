package firehouse;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public final class TextEngine {
    private final Scanner in;
    private final PrintStream out;

    public TextEngine() {
        this(System.in, System.out);
    }

    public TextEngine(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public void println(String line) {
        out.println(line);
    }

    public String readCommand(String prompt) {
        out.print(prompt);
        out.flush();
        // End of input closes the current prompt the same way as quit.
        if (!in.hasNextLine()) {
            return "quit";
        }
        return in.nextLine().trim().toLowerCase();
    }
}
