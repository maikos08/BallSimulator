package software.ulpgc;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BallDisplay ballDisplay;

    public MainFrame() throws HeadlessException {
        this.setTitle("Bouncing Ball");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 700);
        add((Component) (ballDisplay = createBallDisplay()));
    }

    public BallDisplay getBallDisplay() {
        return ballDisplay;
    }

    private SwingBallDisplay createBallDisplay() {
        return new SwingBallDisplay();
    }
}
