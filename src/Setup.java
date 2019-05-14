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


public class Setup extends JFrame implements ActionListener {
	private static final String START = "Launch game";
	private JButton start;
	
	private int players, mode;
	private boolean blitzEnabled;
	
	public Setup() {
		setFonts();
		
		setLayout(new GridLayout(6, 2, 7, 7));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] modes = { "<html><i>Battle Royale</i> - be the last one standing!</html>", "<html><i>Survival</i> - survive the zombie invasion</html>"};

		JComboBox<String> modesList = new JComboBox<>(modes);
		modesList.setSelectedIndex(0);
		modesList.addActionListener(this);
		
		SpinnerModel model = new SpinnerNumberModel(10, 1, 15, 1);     
		JSpinner spinner = new JSpinner(model);

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
		add(new JLabel("Enable a talent:"));
		add(new JCheckBox("<html>Blitz - gain 10% movement speed after every elimination</html>", false));
		add(new JLabel("Number of players in match:"));
		add(spinner);
		
		add(new JLabel("Start the match."));
		add(start);
		
		setSize(625, 340);
		setLocation(Launcher.SCREEN_SIZE.width/2 - getWidth()/2, Launcher.SCREEN_SIZE.height/2 - getHeight()/2);
		setVisible(true);
	}

	private void setFonts() {
		// TODO Auto-generated method stub
		try {
            // Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
		 
	    } 
	    catch (Exception e) {
	       // handle exception
	    }
	 
			
		FontUIResource f = new FontUIResource("Franklin Gothic Book", Font.PLAIN, 16);
		Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	    	Object key = keys.nextElement();
	    	Object value = UIManager.get (key);
	    	if (value instanceof FontUIResource) UIManager.put (key, f);
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Object src = e.getSource();
		if (src instanceof JButton) {
			if (((JButton)src).getText().equals(START)) {
				setVisible(false);
				
				JOptionPane.showMessageDialog(this,
					    "Before we start: Use WASD to move around, and press mouse button to shoot fireballs.\nTry to stay alive for as long as you can!", 
					    "Prepare for battle!", JOptionPane.INFORMATION_MESSAGE);

				
				Launcher.hintInitialize();
			}
		}
		
	}
}
