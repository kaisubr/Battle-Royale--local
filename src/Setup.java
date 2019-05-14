/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.NumberFormatter;

/**
 * A  main menu screen that allows you to customize your game
 */
public class Setup extends JFrame implements ActionListener {
	private static final String START = "Launch game";
	private JButton start;
	private JSpinner spinner;
	private JCheckBox box; 
	private JCheckBox boxH;
	private JCheckBox boxW;
	private JCheckBox boxS;
	private JComboBox<String> modesList;
	
	public static int players;
	public static boolean blitzEnabled, healEnabled, experimental, havenEnabled;
	public static int mode;
	public static final int SURVIVAL = 1, BATROY = 0;
	
	/**
	 *  Constructs a setup menu.
	 */
	public Setup() {
		setFonts();
		
		setLayout(new GridLayout(9, 2, 7, 7));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] modes = { "<html><i>Battle Royale</i> - be the last one standing!</html>", "<html><i>Survival</i> - survive the zombie invasion</html>"};

		 modesList = new JComboBox<>(modes);
		modesList.setSelectedIndex(0);
		modesList.addActionListener(this);
		
		SpinnerModel model = new SpinnerNumberModel(10, 1, 100, 1);     
		spinner = new JSpinner(model);
		
		box = new JCheckBox("<html>Blitz - gain 50% movement speed after every elimination</html>", false);
		boxS = new JCheckBox("<html>Haven - gain 40% damage reduction</html>", false);
		boxH = new JCheckBox("<html>Heal - gain 50% of max health after every elimination</html>", false);
		boxW = new JCheckBox("<html>Randomized maps - experimental, but quite interesting</html>", false);
		
		start = new JButton(START);
		start.addActionListener(this);
		start.setFont(new Font("Tahoma", Font.BOLD, 20));
		start.setForeground(Color.BLUE); 
		
	    add(new JLabel("<html><b>Smoke and Dagger:</b></html>", SwingConstants.RIGHT));
		add(new JLabel("<html>... yet another battle royale!</html>"));
		
		add(new JLabel());
		add(new JLabel());
		
		add(new JLabel("Select a game mode:"));
		add(modesList);
		add(new JLabel("Enable certain talent(s):"));
		add(box);
		add(new JLabel("--"));
		add(boxS);
		add(new JLabel("\t--"));
		add(boxH);
		add(new JLabel("--"));
		add(boxW);
		add(new JLabel("Number of players in match:"));
		add(spinner);
		
		add(new JLabel("Start the match."));
		add(start);
		
		setSize(625, 440);
		setLocation(Launcher.SCREEN_SIZE.width/2 - getWidth()/2, Launcher.SCREEN_SIZE.height/2 - getHeight()/2);
		setVisible(true);
	}

	/**
	 * Sets the fonts to Franklin Gothic Book.
	 */
	private void setFonts() {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    }
	 
			
		FontUIResource f = new FontUIResource("Franklin Gothic Book", Font.PLAIN, 16);
		Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	    	Object key = keys.nextElement();
	    	Object value = UIManager.get (key);
	    	if (value instanceof FontUIResource) UIManager.put (key, f);
	    }
	}

	/**
	 * Fired when an action occurs.
	 * @param e the action-event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Object src = e.getSource();
		if (src instanceof JButton) {
			if (((JButton)src).getText().equals(START)) {
				setVisible(false);
				
				players = (int)spinner.getValue();
				blitzEnabled = box.isSelected();
				healEnabled = boxH.isSelected();
				experimental = !boxW.isSelected();
				havenEnabled = boxS.isSelected();
				mode = modesList.getSelectedIndex();
				
				JOptionPane.showMessageDialog(this,
					    "Before we start: Use WASD to move around, and press mouse button to shoot fireballs.\nTry to stay alive for as long as you can!", 
					    "Prepare for battle!", JOptionPane.INFORMATION_MESSAGE);

				
				Launcher.hintInitialize();
			}
		}
		
	}
}
