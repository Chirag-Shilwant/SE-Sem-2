/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 *
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class SearchView implements ActionListener {


	private JFrame win;
	private JButton topScore, finished, topPlayer, lowScore, submitNick, topScorePlayer, lowScorePlayer, prevScores;
	private JTextField nickField;
	private JLabel nickLabel;
	private JPanel nickPanel;
	private int type = -1;
 	JLabel ansLabel;
	Search search;

	private static String convertToMultiline(String orig)
	{
		return "<html>" + orig.replaceAll("\n", "<br>");
	}


	public SearchView() {

		search = new Search();


		win = new JFrame("Search Queries");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		win.setPreferredSize(new Dimension(600, 400));

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Patron Panel
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(3,  1));
		searchPanel.setBorder(new TitledBorder("Select Query"));


		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new FlowLayout());
		ansLabel = new JLabel("Answer: ");
		answerPanel.add(ansLabel);

		nickPanel = new JPanel();
		nickPanel.setLayout(new FlowLayout());
		nickLabel = new JLabel("Nick Name");
		nickField = new JTextField("", 15);
		submitNick = new JButton("Submit");
		submitNick.addActionListener(this);
		nickPanel.add(nickLabel);
		nickPanel.add(nickField);
		nickPanel.add(submitNick);
		nickPanel.setVisible(false);

		topScorePlayer = new JButton("Top Player Score");
		JPanel topScorePlayerPanel = new JPanel();
		topScorePlayerPanel.setLayout(new FlowLayout());
		topScorePlayer.addActionListener(this);
		topScorePlayerPanel.add(topScorePlayer);

		lowScorePlayer = new JButton("Lowest Player Score");
		JPanel lowScorePlayerPanel = new JPanel();
		lowScorePlayerPanel.setLayout(new FlowLayout());
		lowScorePlayer.addActionListener(this);
		lowScorePlayerPanel.add(lowScorePlayer);

		prevScores = new JButton("Previous Player Scores");
		JPanel prevScoresPanel = new JPanel();
		prevScoresPanel.setLayout(new FlowLayout());
		prevScores.addActionListener(this);
		prevScoresPanel.add(prevScores);



		finished = new JButton("Finish");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		topScore = new JButton("Top score");
		JPanel topScorePanel = new JPanel();
		topScorePanel.setLayout(new FlowLayout());
		topScore.addActionListener(this);
		topScorePanel.add(topScore);

		lowScore = new JButton("Lowest score");
		JPanel lowScorePanel = new JPanel();
		lowScorePanel.setLayout(new FlowLayout());
		lowScore.addActionListener(this);
		lowScorePanel.add(lowScore);

		topPlayer = new JButton("Top Player");
		JPanel topPlayerPanel = new JPanel();
		topPlayerPanel.setLayout(new FlowLayout());
		topPlayer.addActionListener(this);
		topPlayerPanel.add(topPlayer);

		searchPanel.add(topScorePanel);
		searchPanel.add(lowScorePanel);
		searchPanel.add(topPlayerPanel);
		searchPanel.add(topScorePlayerPanel);
		searchPanel.add(lowScorePlayerPanel);
		searchPanel.add(prevScoresPanel);
		searchPanel.add(nickPanel);
		searchPanel.add(answerPanel);
		searchPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(searchPanel, "Center");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(topScore)) {
			Score res = search.findTopScore();
			printScoreResult(res);
		}
		topPlayer(e);
		if(e.getSource().equals(finished)) {
			win.setVisible(false);
		}
		if(e.getSource().equals(lowScore)){
			Score res = search.findLowScore();
			printScoreResult(res);
		}
		if(e.getSource().equals(topScorePlayer)) {
			type = 0;
			nickPanel.setVisible(true);
		}
		if (e.getSource().equals(lowScorePlayer)) {
			type = 1;
			nickPanel.setVisible(true);
		}
		if (e.getSource().equals(prevScores)) {
			type = 2;
			nickPanel.setVisible(true);
		}
		Input(e);
	}

	private void topPlayer(ActionEvent e) {
		if (e.getSource().equals(topPlayer)) {
			Vector v = search.findTopPlayer();
			if(!v.elementAt(1).equals(0.0))
				ansLabel.setText("Answer: " + v.elementAt(0) + " " + v.elementAt(1));
			else
				ansLabel.setText("Answer: No player crossed the threshold");
			reset();
		}
	}

	private void Input(ActionEvent e) {
		if(e.getSource().equals(submitNick)) {
			if(type == 0){
				Score res = search.findTopPlayerScore(nickField.getText());
				printScoreResult(res);
			}
			else if(type == 1){
				Score res = search.findLowPlayerScore(nickField.getText());
				printScoreResult(res);
			}
			else {
				List<Score> res = search.getLastScores(nickField.getText());
				if(res!= null) {
					String out = "Answer:\n";
					for(Score s: res) {
						out += s.toString() + "\n";
					}
					ansLabel.setText(convertToMultiline(out));
				}
				else
					ansLabel.setText("Answer: No player found");
			}
			reset();
		}
	}

	private void reset() {
		nickPanel.setVisible(false);
		nickField.setText("");
	}

	private void printScoreResult(Score res) {
		if(res != null)
			ansLabel.setText("Answer: " + res.getNickName() +  " " + res.getScore() + " " + res.getDate());
		else
			ansLabel.setText("Answer: No player found");
		reset();
	}

}
