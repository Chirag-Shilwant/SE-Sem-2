import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class NewLane  {
	private int roll;
	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;

	JButton maintenance;
	Lane lane;


	public NewLane() {

	

		// initDone = true;
		frame = new JFrame("Lane Extension");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}


	private JPanel makeFrame(Vector<String> Bowlers) {
		// bowlers = party.getMembers();
		int numBowlers = 2;

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 7; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 3; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j+1], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 2], BorderLayout.EAST);
			}
			int j = 0;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(Bowlers.get(i)));
			pins[i].setLayout(new GridLayout(0, 3));
			for (int k = 0; k != 3; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		return panel;
	}

	public void changeScore(int bowlerID,int score,int frameNumber) {
		scoreLabel[bowlerID][frameNumber].setText(String.valueOf(score));
	}

	public void changeBall(int bowlerID,int score,int ballCell) {
		if(score == 10)
			ballLabel[bowlerID][ballCell].setText("X");
		else if(score == -4)
			ballLabel[bowlerID][ballCell].setText("/");
		else if(score == -2)
			ballLabel[bowlerID][ballCell].setText("F");
		else
			ballLabel[bowlerID][ballCell].setText(String.valueOf(score));
	}

	public void receiveLaneEvent(Vector<String> Bowlers){
		System.out.println("Here");
		System.out.println("Making the frame.");
		cpanel.removeAll();
		cpanel.add(makeFrame(Bowlers), "Center");

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);
		int numBowlers = 2;
		// maintenance = new JButton("Maintenance Call");
		// JPanel maintenancePanel = new JPanel();
		// maintenancePanel.setLayout(new FlowLayout());
		// maintenance.addActionListener(this);
		// maintenancePanel.add(maintenance);

		// buttonPanel.add(maintenancePanel);
		// cpanel.add(buttonPanel, "South");
		// for (int k = 0; k < numBowlers; k++) {
		// 		for (int i = 0; i <= frameNumber - 1; i++) {
		// 			if (Vector.get(k) != 0)
		// 				scoreLabel[k][i].setText(
		// 					(Vector.get(k)).toString());
		// 		}
		// 		for (int i = 0; i < 7; i++) {
		// 			if (((int[]) ((HashMap) le.getScore())
		// 				.get(bowlers.get(k)))[i]
		// 				!= -1)
		// 				if (((int[]) ((HashMap) le.getScore())
		// 					.get(bowlers.get(k)))[i]
		// 					== 10
		// 					&& (i % 2 == 0 || i == 19))
		// 					ballLabel[k][i].setText("X");
		// 				else if (
		// 					i > 0
		// 						&& ((int[]) ((HashMap) le.getScore())
		// 							.get(bowlers.get(k)))[i]
		// 							+ ((int[]) ((HashMap) le.getScore())
		// 								.get(bowlers.get(k)))[i
		// 							- 1]
		// 							== 10
		// 						&& i % 2 == 1)
		// 					ballLabel[k][i].setText("/");
		// 				else if ( ((int[])((HashMap) le.getScore()).get(bowlers.get(k)))[i] == -2 ){
							
		// 					ballLabel[k][i].setText("F");
		// 				} else
		// 					ballLabel[k][i].setText(
		// 						(new Integer(((int[]) ((HashMap) le.getScore())
		// 							.get(bowlers.get(k)))[i]))
		// 							.toString());
		// 		}
			// }




		frame.pack();
	}
}