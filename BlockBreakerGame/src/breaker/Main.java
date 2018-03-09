package breaker;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This is the driver class for the brick breaker game, provides the base frame
 * all other elements work within
 * 
 * Some basic functionality provided by this tutorial:
 * https://www.youtube.com/watch?v=K9qMm3JbOH0
 * 
 * Added multiple auto incrementing levels and increasing levels of difficulty
 * 
 * @author Nate
 *
 */

public class Main {

	public static void main(String[] args) {
		// Outer Window for game
		JFrame frame = new JFrame();

		// Inner
		Gameplay game = new Gameplay();

		// Setting parameters of outer window
		frame.setBounds(10, 10, 700, 600);
		frame.setTitle("BREAK");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Centers frame on first load
		frame.setLocationRelativeTo(null);

		// Add GamePlay panel to the JFrame
		frame.add(game);
	}

}
