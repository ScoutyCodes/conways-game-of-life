package com.game.of.life;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game{
	
	public static JFrame f;
	public static JLabel [] cells;
	public static ArrayList<Boolean> alive = new ArrayList<Boolean>();
	public static ArrayList<Integer> nextFrame = new ArrayList<Integer>();
	public static int frame;
	public static JPanel panel;
	public static int gameState = 0;
	
	public static void main(String[] args) throws IOException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();
		
		//Creates the window
		f = new JFrame("Game of Life");
		f.setSize(1015, 1040);
	
		f.setLocation(width.intValue() / 2 - 540, height.intValue() / 2 - 540);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creates the panel that holds the components
		panel = new JPanel();
		panel.setLayout(null);
		
		cells = new JLabel[100];
		
		frame = 0;
		
		int x = 0, y = 0;
		
		//Creates all the cells
		for (int i = 0; i < cells.length; i++) {
			JLabel cell = new JLabel(new ImageIcon(ImageIO.read(new File("src/resources/dead.png"))));
			cell.setBounds(new Rectangle(new Point(x, y), cell.getPreferredSize()));
			
			panel.add(cell);
			cells[i] = cell;
			alive.add(false);
			nextFrame.add(0);
			
			x += 100;
			
			if (x >= 1000) {
				x = 0;
				y += 100;
			}
			
		}
		
		//Creates an ActionListener for the frameTimer
		ActionListener frames = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame == 0) {
					for (int i = 0; i < cells.length; i++) {
						int n = 0;
						
						//All of these methods check for living cell neighbors
						
						if (i > 10 && notOnTheLeft(i)) {
							if (alive.get(i - 11) == true) {
								n++;
							}
						}
						
						if (i > 9) {
							if (alive.get(i - 10) == true) {
								n++;
							}
						}
						
						if (i > 8 && notOnTheRight(i)) {
							if (alive.get(i - 9) == true) {
								n++;
							}
						}
						
						if (i > 0 && notOnTheLeft(i)) {
							if (alive.get(i - 1) == true) {
								n++;
							}
						}
						
						if (i < 99 && notOnTheRight(i)) {
							if (alive.get(i + 1) == true) {
								n++;
							}
						}
						
						if (i < 90 && notOnTheLeft(i)) {
							if (alive.get(i + 9) == true) {
								n++;
							}
						}
						
						if (i < 90) {
							if (alive.get(i + 10) == true) {
								n++;
							}
						}
						
						if (i < 89 && notOnTheRight(i)) {
							if (alive.get(i + 11) == true) {
								n++;
							}
						}
						
						//These methods change the cell depending on it's current state
						//and it's amount of living neighbors
						if (n == 3 && alive.get(i) == false) {
							nextFrame.set(i, 2);
							
						}
						else if (n != 2 && n != 3 && alive.get(i) == true) {
							nextFrame.set(i, 1);
						}
						else {
							nextFrame.set(i, 0);
						}
						
					}
					
					frame++;
				}
				else if (frame == 1) {
					for (int i = 0; i < cells.length; i++) {
						JLabel cell = cells[i];
						
						//Makes all the changes that are supposed to happen to the cell
						if (nextFrame.get(i) == 1) {
							alive.set(i, false);
							
							try {
								cell.setIcon(new ImageIcon(ImageIO.read(new File("src/resources/dead.png"))));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						else if (nextFrame.get(i) == 2) {
							alive.set(i, true);
							
							try {
								cell.setIcon(new ImageIcon(ImageIO.read(new File("src/resources/alive.png"))));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						
						nextFrame.set(i, 0);
						
						frame = 0;
					}
				}
			}
			
		};
		
		//Creates the frameTimer, that runs when you press "Start".
		Timer frameTimer = new Timer(0, frames);
		frameTimer.setDelay(250);
		
		//Creates the two buttons, "Start" and "Random"
		JButton start = new JButton("Start");
		JButton random = new JButton("Random");
		
		//Starts the frameTimer and removes the buttons
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameTimer.start();
				start.setEnabled(false);
				start.setVisible(false);
				random.setEnabled(false);
				random.setVisible(false);
			}
			
		});
		
		//Randomizes the board
		random.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (int i = 0; i < cells.length; i++) {
					Random r = new Random();
					JLabel cell = cells[i];
					
					alive.set(i, r.nextBoolean());
					
					if (alive.get(i) == true) {
						try {
							cell.setIcon(new ImageIcon(ImageIO.read(new File("src/resources/alive.png"))));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else {
						try {
							cell.setIcon(new ImageIcon(ImageIO.read(new File("src/resources/dead.png"))));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
				}
				
			}
			
		});
		
		//Sets the location of the buttons
		start.setBounds(new Rectangle(new Point(900, 950), new Dimension(100, 50)));
		random.setBounds(new Rectangle(new Point(900, 890), new Dimension(100, 50)));
		
		//Packs everything up
		panel.add(start);
		panel.add(random);
		f.add(panel);
		f.setVisible(true);
		
		//Adds the mouse listener and checks if you clicked on a cell
		f.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Point mousepos = MouseInfo.getPointerInfo().getLocation();
				
				for (int i = 0; i < cells.length; i++) {
					JLabel cell = (JLabel) cells[i];
					
					if ((mousepos.x - f.getX()) > cell.getX() && (mousepos.x - f.getX() < (cell.getX() + cell.getWidth())) && (mousepos.y - f.getY()) > cell.getY() && (mousepos.y - f.getY() < (cell.getY() + cell.getHeight()))) {
						if (alive.get(i) == false) {
							try {
								cells[i].setIcon(new ImageIcon(ImageIO.read(new File("src/resources/alive.png"))));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							alive.set(i, true);
						}
						else {
							try {
								cells[i].setIcon(new ImageIcon(ImageIO.read(new File("src/resources/dead.png"))));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							alive.set(i, false);
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});
	}
	
	//Checks if the given cell is not on the left
	public static boolean notOnTheLeft(int i) {
		return (i != 0 && i != 10 && i != 20 && i != 30 && i != 40 && i != 50 && i != 60 && i != 70 && i != 80 && i != 90);
	}
	
	//Checks if the given cell is not on the right
	public static boolean notOnTheRight(int i) {
		return (i != 9 && i != 19 && i != 29 && i != 39 && i != 49 && i != 59 && i != 69 && i != 79 && i != 89 && i != 99);
	}

	

}
