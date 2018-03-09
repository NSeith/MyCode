package breaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

/** provides the actual functionality of this game.  
 * 
 * See Main.java
 * 
 * @author Nate
 *
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private boolean play = false;
	private int score = 0;

	// to increase difficulty each level need as separate variables.
	private int r = 1; 	// rows
	private int c = 1; 	// columns
	
	private int totalBricks = r * c;
	private int level = 1;

	// used to set speed of ball
	private Timer timer;
	private int delay = 8;

	// sets left edge on start, 300 roughly center
	private int playerX = 300;

	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;

	private Bricks brick;

	// constructor
	public Gameplay() {
		brick = new Bricks(r, c);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {
		// background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(1, 1, 692, 592);

		// draw the bricks
		brick.draw((Graphics2D) g);

		// border settings
		g.setColor(Color.WHITE);
		// top
		g.fillRect(0, 0, 692, 3);
		// left
		g.fillRect(0, 0, 3, 592);
		// right
		g.fillRect(691, 0, 3, 592);

		// scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Score : " + score, 520, 30);

		// paddle element
		g.setColor(Color.WHITE);
		g.fillRect(playerX, 500, 100, 3);

		// the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);

		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("LEVEL : "+ level, 80, 30);

		g.setColor(Color.gray);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Bricks Left: "+ totalBricks, 80, 560);
		
		g.setColor(Color.gray);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Pause = SPACE", 450, 560);

		// game over
		if (ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;

			r = 1;
			c = 1;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("GAME OVER", 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter to Restart", 190, 350);
		}

		// next level
		if (totalBricks == 0) {

			play = !play;

			// reset variables
			ballposX = 120;
			ballposY = 350;
			ballXdir = -1;
			ballYdir = -2;
			playerX = 300;
			level ++; 
			
			// add more difficulty
			r = r + 1;
			c = c + 1;
			totalBricks = r * c;
			brick = new Bricks(r, c);
			delay -= 1;
		
			// repaint();
		}
		// get rid of the graphics content
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// sending action events to the listener
		timer.start();

		// detect if left or right key is pressed
		if (play) {

			// create rectangle for detecting collision with paddle
			// [x(cord),y(cord),width,height]
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 500, 100, 2))) {
				ballYdir = -ballYdir;
			}

			// iterate over each brick
			A: for (int i = 0; i < brick.map.length; i++) {
				for (int j = 0; j < brick.map[0].length; j++) {

					// particular brick
					if (brick.map[i][j] > 0) {
						int brickx = j * brick.brickWidth + 80;
						int bricky = i * brick.brickHeight + 50;
						int brickWidth = brick.brickWidth;
						int brickHeigth = brick.brickHeight;

						// need rectangles for collision detection, like paddle above
						Rectangle rect = new Rectangle(brickx, bricky, brickWidth, brickHeigth);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;

						// collision set block value to 0
						if (ballRect.intersects(brickRect)) {
							brick.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;

							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}

							// must break out of not only inner loop, but also outer
							break A;
						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			// defining the ball behavior in relation to borders
			if (ballposX < 0) {

				// ball trying to got past left border, change direction
				ballXdir = -ballXdir;
			}
			if (ballposY < 0) {

				// ball trying to got past top border, change direction
				ballYdir = -ballYdir;
			}
			if (ballposX > 670) {

				// ball trying to got past right border, change direction
				ballXdir = -ballXdir;
			}
		}

		/*
		 * Repaint must be called over and over again. Each time moveLeft and moveRight
		 * is called the paddle must be redrawn, changes need to be shown, everything
		 * must be redrawn.
		 */
		repaint();

	}

	@Override
	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub

		if (k.getKeyCode() == KeyEvent.VK_RIGHT) {

			// Don't let the player go past the boarder
			if (playerX >= 585) {
				playerX = 585;
			} else {
				moveRight();
			}

		}

		if (k.getKeyCode() == KeyEvent.VK_LEFT) {
			// Don't let the player go past the boarder
			if (playerX <= 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}

		if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 1;
				level = 1; 
				brick = new Bricks(r, c);

				repaint();

			}
		}

		// pause feature
		if (k.getKeyCode() == KeyEvent.VK_SPACE) {

			// if space is pressed, no longer playing
			play = !play;
		}

		// set timer to stop based on above logic
		if (k.getKeyCode() == KeyEvent.VK_SPACE && !play) {
			timer.stop();
		}

		// resume game, start timer again
		else if (k.getKeyCode() == KeyEvent.VK_SPACE && play) {
			timer.start();
		}

	}

	/*
	 * These set how fast to move the ball on the screen based on which key 'left'
	 * or 'right' is pressed.
	 * 
	 */
	public void moveRight() {
		play = true;
		playerX += 25; // move player 25px to right
	}

	public void moveLeft() {
		play = true;
		playerX -= 25; // move player 25px to left
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Unimplemented from interface
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Unimplemented from interface
	}

}
