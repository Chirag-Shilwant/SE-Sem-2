import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.*;

public class LoadSavedView implements ActionListener, ListSelectionListener {

    private ControlDeskView controlDeskView;
    private int maxSize;
    private JFrame win;
    private JButton loadGame;
    private Vector<LaneEvent> saveStatesDB;
    private Vector<String> saveStatesStrings;
    private JList savedStates;
    private String selectedState;

    public LoadSavedView(ControlDeskView cdv){

        controlDeskView = cdv;
        win = new JFrame("Load Game");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        JPanel savedStatesPanel = new JPanel();
        savedStatesPanel.setLayout(new FlowLayout());
        savedStatesPanel.setBorder(new TitledBorder("Saved Games"));

        try {
            saveStatesDB = SaveFile.getAllSavedGames();
            System.err.println("yeet");
            saveStatesStrings = new Vector<String>();

            for (LaneEvent le: saveStatesDB){
                String s = saveTitle(le);
                saveStatesStrings.add(s);
            }
        } catch (Exception e) {
            System.err.println("File Error" + e);
            saveStatesDB = new Vector<>();
            saveStatesStrings = new Vector<>();
        }
        savedStates = new JList(saveStatesStrings);
        savedStates.setVisibleRowCount(8);
        savedStates.setFixedCellWidth(200);
        JScrollPane bowlerPane = new JScrollPane(savedStates);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        savedStates.addListSelectionListener(this);
        savedStatesPanel.add(bowlerPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        Insets buttonMargin = new Insets(4, 4, 4, 4);

        loadGame = new JButton("Load Game");
        JPanel loadGamePanel = new JPanel();
        loadGamePanel.setLayout(new FlowLayout());
        loadGame.addActionListener(this);
        loadGamePanel.add(loadGame);

        buttonPanel.add(loadGamePanel);

        colPanel.add(savedStatesPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();

    }

    private String saveTitle(LaneEvent le) {

        Bowler b = (Bowler) le.getParty().getMembers().get(0);
        String saveTitle = b.getNick() + "'s party" + saveStatesStrings.size();
        return saveTitle;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(selectedState != null){
            int i = saveStatesStrings.indexOf(selectedState);
            System.out.println("selected state: " + i);
            LaneEvent le = saveStatesDB.get(i);
            controlDeskView.setLoadedLE(le);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        selectedState = ((String) ((JList) e.getSource()).getSelectedValue());
    }

}
