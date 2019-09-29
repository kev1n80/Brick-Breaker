// Kevin Cam
// Jared Chen

import acm.graphics.*;     // GOval, GRect, etc.
import acm.program.*;      // GraphicsProgram
import acm.util.*;         // RandomGenerator
import java.awt.*;         // Color
import java.awt.event.*;   // MouseEvent

public class Breakout extends BreakoutProgram {
	
	
	
	public void run() {
		
		setTitle("CS 106A Breakout");
		setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		int turns = NTURNS;
		int winScore = NBRICK_COLUMNS * NBRICK_ROWS;
		int score = 0;
		
		
		makeBricks();
		add(paddle);
		GOval ball = (ball());
		add(ball);
		double y = ballVy;
		double x = ballVx;
		String label = "Score: 0, Turns: " + turns;
		GLabel scoreTurns = newGlabel(label);
		
		while (turns > 0 && score != winScore) {  
			ball.setLocation(getWidth()/2 - BALL_RADIUS, getHeight()/2 - BALL_RADIUS);
			while (!hasHitBottom(ball) && score != winScore) {
				ball.move(x, y);
				pause(DELAY);
				
				if (hasHitTop(ball)) {
					y = -y;
				}
				else if (hasHitRight(ball)) {
					x = -x;
				}
				else if (hasHitLeft(ball)) {
					x = -x;
				}
				if (collision(ball, scoreTurns)) {
					score = winScore - getElementCount() + 3;
					y = -y;
					scoreTurns.setLabel("Score: " + score + ", Turns: " + turns);
				}
				if (paddleBounce(ball)) {
					y = -y;
				}
			
			}
			score = winScore - getElementCount() + 3;
			if (score != winScore) {
				turns = turns - 1;
			}
			scoreTurns.setLabel("Score: " + score + ", Turns: " + turns);
		}
		
		remove(paddle);
		remove(ball);
		if (turns == 0) {
			loseScreen();
		}
		else if (score == winScore) {
			winScreen();
		}
	}
	
	public void mouseMoved(MouseEvent movePaddle) {
		double x = movePaddle.getX();
		if (x > PADDLE_WIDTH/2 && x < getWidth() - PADDLE_WIDTH/2) {
			paddle.setLocation(x - PADDLE_WIDTH/2 , CANVAS_HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT));
		}
	}
	
	
	
	private GLabel newGlabel(String string) {
		GLabel scoreTurns = new GLabel(string);
		scoreTurns.setFont(SCREEN_FONT);
		add (scoreTurns, 0, scoreTurns.getHeight());
		return scoreTurns;
	}
	
	
	private void makeBricks() {
		int colorCount = 1;
		
		double border = (CANVAS_WIDTH - (BRICK_WIDTH * NBRICK_COLUMNS + 
				BRICK_SEP * (NBRICK_COLUMNS-1)))/2;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			int y = (int) (BRICK_Y_OFFSET + (i * BRICK_HEIGHT) + (i * BRICK_SEP));
			for (int j = 0; j < NBRICK_COLUMNS; j++) {
				int x = (int) (j * (BRICK_SEP + BRICK_WIDTH) + border);
				GRect bricks = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				bricks.setFilled(true);
				makeColor(bricks, colorCount);
				
				add(bricks);
				if (colorCount > 10) {
					colorCount = 1;
				}
			}
			colorCount = colorCount + 1;
		}	
	}
	
	private void makeColor (GRect bricks, int colorCount) {
		if (colorCount == 1 || colorCount == 2 || colorCount == 11) {
			bricks.setColor(Color.RED);
			bricks.setFillColor(Color.RED);
		}
		else if (colorCount == 3 || colorCount == 4) {
			bricks.setColor(Color.ORANGE);
			bricks.setFillColor(Color.ORANGE);
		}
		else if (colorCount == 5 || colorCount == 6) {
			bricks.setColor(Color.YELLOW);
			bricks.setFillColor(Color.YELLOW);
		}
		else if (colorCount == 7 || colorCount == 8) {
			bricks.setColor(Color.GREEN);
			bricks.setFillColor(Color.GREEN);
		}
		else if (colorCount == 9 || colorCount == 10) {
			bricks.setColor(Color.CYAN);
			bricks.setFillColor(Color.CYAN);
		}
		
	}
	

  	private GRect makePaddle() {
  		double x = getWidth()/2 - PADDLE_WIDTH/2;
  		double y = CANVAS_HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT);
  		GRect newPaddle = new GRect(x ,y , PADDLE_WIDTH, PADDLE_HEIGHT);
		add(newPaddle);
		newPaddle.setFilled(true);
		newPaddle.setFillColor(Color.BLACK);
		newPaddle.setColor(Color.BLACK);
		return newPaddle;
		}

  	// paddle needs too be accessible everywhere in order to let public void
  	// mouse moved change the location of the paddle without adding more 
  	// paddles.
  	private GRect paddle = makePaddle();
  	
  	private GOval ball() {
  		double x = getWidth()/2 - BALL_RADIUS;
  		double y = getHeight()/2 - BALL_RADIUS;
  		GOval ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		add(ball);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		ball.setColor(Color.BLACK);
		return ball;
		}		
	
  	// in order to maintain ball Velocity constant. Also, it is used
  	// extensively throughout the program.
	private double ballVx = randomVx();
		
	private double randomVx() {
		RandomGenerator rg = RandomGenerator.getInstance();
		double Vx = rg.nextDouble(VELOCITY_X_MIN, VELOCITY_X_MAX);
		return Vx;
	}
	
	// in order to maintain ball Velocity constant. Also, it is used
	// extensively throughout the program.
	private double ballVy = VELOCITY_Y;
	
	private boolean hasHitBottom(GOval ball) {
		double minY = getHeight() - BALL_RADIUS * 2; 
		return ball.getY() >= minY;
	}
	
	private boolean hasHitTop(GOval ball) {
		double maxY = 0; 
		return ball.getY() <= maxY;
	}
	
	private boolean hasHitRight(GOval ball) {
		double maxX = getWidth() - BALL_RADIUS * 2;
		return ball.getX() >= maxX;
	}
	
	private boolean hasHitLeft(GOval ball) {
		double minX = 0;
		return ball.getX() <= minX;
	}
	
	private int scorePlusCount(GOval ball, GLabel scoreTurns) {
		int scorePlus = 1;
		double x = ball.getX();
		double y = ball.getY();
		double diameter = BALL_RADIUS * 2;
		if (getElementAt(x, y) != null && getElementAt(x, y) 
				!= paddle && getElementAt(x, y) != scoreTurns) {
			remove(getElementAt(x, y));
			scorePlus = scorePlus + 1;
		}
		if (getElementAt(x + diameter, y) != null && getElementAt(x + diameter, y)
				!= paddle && getElementAt(x + diameter, y) != scoreTurns) {
			remove(getElementAt(x + diameter, y));
			scorePlus = scorePlus + 1;
		}
		if (getElementAt(x, y + diameter) != null && getElementAt(x, y + diameter)
				!= paddle && getElementAt(x, y + diameter) != scoreTurns) {
			remove(getElementAt(x, y + diameter));
			scorePlus = scorePlus + 1;
		}
		if (getElementAt(x + diameter, y + diameter) != null && getElementAt(x + diameter, y + diameter )
				!= paddle && getElementAt(x + diameter, y + diameter) != scoreTurns) {
			remove(getElementAt(x + diameter, y + diameter));
			scorePlus = scorePlus + 1;
		}
		return scorePlus;
	}
	
	private boolean collision(GOval ball, GLabel scoreTurns) {
		double x = ball.getX();
		double y = ball.getY();
		double diameter = BALL_RADIUS * 2;
		boolean bounce = false;
		if (getElementAt(x, y) != null && getElementAt(x, y) != paddle && getElementAt(x, y) != scoreTurns) {
			remove(getElementAt(x, y));
			bounce = true;
		}
		if (getElementAt(x + diameter, y) != null && getElementAt(x + diameter, y) != paddle 
				&& getElementAt(x + diameter, y) != scoreTurns) {
			remove(getElementAt(x + diameter, y));
			bounce = true;
		}
		if (getElementAt(x, y + diameter) != null && getElementAt(x, y + diameter) != paddle 
				&& getElementAt(x, y + diameter) != scoreTurns) {
			remove(getElementAt(x, y + diameter));
			bounce = true;
		}
		if (getElementAt(x + diameter, y + diameter) != null && getElementAt(x + diameter, y + diameter )
				!= paddle && getElementAt(x + diameter, y + diameter) != scoreTurns) {
			remove(getElementAt(x + diameter, y + diameter));
			bounce = true;
		}
		return bounce;
	}
	
	private boolean paddleBounce(GOval ball) {
		double x = ball.getX();
		double y = ball.getY();
		double diameter = BALL_RADIUS * 2;
		boolean bounce = false;
		if (getElementAt(x, y + diameter) != null && getElementAt(x, y + diameter) == paddle) {
			bounce = true;
		}
		if (getElementAt(x + diameter, y + diameter) != null 
				&& getElementAt(x + diameter, y + diameter) == paddle) {
			bounce = true;
		}
		return bounce;
	}
	
	private void winScreen() {
		GLabel win = new GLabel("YOU WIN!");
		win.setFont(SCREEN_FONT);
		add (win, getWidth()/2 - win.getWidth() / 2, getHeight()/2 - win.getHeight() / 2);
	}
	
	private void loseScreen () {
		GLabel lose = new GLabel("GAME OVER");
		lose.setFont(SCREEN_FONT);
		add (lose, getWidth()/2 - lose.getWidth() / 2, getHeight()/2 - lose.getHeight() / 2);
	}
}


