/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The Launcher is the runner class for the game.
 */
public class Launcher {
	
	public static final Dimension SCREEN_SIZE = 
			Toolkit.getDefaultToolkit().getScreenSize();
	private static ContentPaneHandler cph;
	private static World world;
	private static Camera camera;
	private static JFrame fr;
	private static InputManager manager;
	public static final int GAME_WIDTH = SCREEN_SIZE.width, GAME_HEIGHT = SCREEN_SIZE.height- 70;

	/**
	 * Constructs the runner, sets up the game.
	 * @param args the cmd-line arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Setup set = new Setup();
		
	}
	
	/**
	 * Receives hint to initialize the game 
	 */
	public static void hintInitialize() {
		javax.swing.SwingUtilities.invokeLater(() -> {
			launch();
		});
	}

	/**
	 * Launches the game.
	 */
	private static void launch() {
		// TODO Auto-generated method stub

		fr = new JFrame("Loading assets...");
		fr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fr.setSize(450, 0);
		fr.setLocation(SCREEN_SIZE.width/2 - fr.getWidth()/2, SCREEN_SIZE.height/2);
		fr.setVisible(true);
		
		JFrame f = new JFrame("frame");
		GamePanel content = new GamePanel(null);
		f.setContentPane(content);
		
		cph = new ContentPaneHandler(content);

		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);

		f.setSize(GAME_WIDTH, GAME_HEIGHT);

		world = new World(cph);
		
		f.setVisible(true);
		
		camera = new Camera(f, cph);
		manager = new InputManager(camera);
		
		cph.setPlayer(world.getPlayer());
		cph.setCamera(camera);
		cph.setInputManager(manager);
		
		
		f.dispatchEvent(
				new WindowEvent(f, WindowEvent.WINDOW_ACTIVATED));
		
		cph.startGameLoop();
	}
	
	/**
	 * return the content-pane handler
	 * @return the content-pane handler
	 */
	public ContentPaneHandler getCPH() {
		return cph;
	}
	
	/**
	 * return the loading-assets JFrame
	 * @return the loading-assets JFrame
	 */
	public static JFrame getLoadingFrame() {
		return fr;
	}

}
