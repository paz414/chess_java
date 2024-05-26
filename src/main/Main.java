package main;
import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {
        JFrame window=new JFrame("Chess Window");// JFrame is a class in javax.swing package that creates a window
                                                        // to hold all the components

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the application
                                                                // when the window is closed
        window.setResizable(false); // window size cannot be changed

        GamePanel gamepanel=new GamePanel(); // create a new GamePanel object
        window.add(gamepanel); // add the GamePanel to the window
        window.pack(); // window is packed which means that the window is resized
                        // to fit the preferred size of its subcomponents

        window.setLocationRelativeTo(null); // window is centered
        window.setVisible(true); // window is visible

        gamepanel.launchGame();


    }
}
