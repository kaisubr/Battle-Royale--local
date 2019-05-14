import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class InputManager implements KeyListener, MouseListener {
	private Camera cam;

	private boolean[] move; //n, e, w, s
	boolean ctdMove;
	private ContentPaneHandler cph;
	
	public InputManager(Camera cam) {
		this.cam = cam;

		move = new boolean[(int)KeyEvent.VK_F12];
		ctdMove = false;

		cam.getFrame().addKeyListener(this);
		cam.getFrame().addMouseListener(this);
		cph = cam.getContentPaneHandler();
	}
	
	@Override
	public void keyPressed(KeyEvent v) {
		// TODO Auto-generated method stub
//		//cph.deltaTranslate(i, i); i++;
//		
//		if (!ctdMove) {
//			//cph.repaint();
//			ctdMove = true;
//		}
		
		if (v.getKeyCode() < move.length) {
			move[v.getKeyCode()] = true;
		}
	}

	public void setPosition() {
		// TODO Auto-generated method stub
		if (cph.getPlayer() != null) {
			if (move[KeyEvent.VK_W]) cph.getPlayer().deltaSet(-5, 5*(1.5/3));				//0,-5
			if (move[KeyEvent.VK_D]) cph.getPlayer().deltaSet(-5, -5*(1.5/3));		//5,0	//x*sin(30)/3
			if (move[KeyEvent.VK_A]) cph.getPlayer().deltaSet(5, 5*(1.5/3));		//-5,0
			if (move[KeyEvent.VK_S]) cph.getPlayer().deltaSet(5, -5*(1.5/3));				//0,5
			
			if (move[KeyEvent.VK_W] || move[KeyEvent.VK_A] || move[KeyEvent.VK_S] || move[KeyEvent.VK_D]) {
				cph.getPlayer().setMoving(true);
			} else {
				cph.getPlayer().setMoving(false);
			}
			
			try {
				cph.getPlayer().orientationSet(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//
//	private void right() {
//		// TODO Auto-generated method stub
//		  move[0] = false;
//		move[1] = true;
//		move[2] = false;
//		  move[3] = false;
//	}
//
//	private void down() {
//		// TODO Auto-generated method stub
//		  move[0] = false;
//		move[1] = false;
//		move[2] = false;
//		move[3] = true;
//	}
//
//	private void left() {
//		// TODO Auto-generated method stub
//		  move[0] = false;
//		move[1] = false;
//		move[2] = true;
//		  move[3] = false;
//	}
//
//	private void up() {
//		// TODO Auto-generated method stub
//		move[0] = true;
//		move[1] = false;
//		move[2] = false;
//		  move[3] = false;
//	}

	@Override
	public void keyReleased(KeyEvent v) {
		if (v.getKeyCode() < move.length) {
			move[v.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent v) {

//		switch (v.getKeyCode()) {
//		case KeyEvent.VK_W: up(); break;
//		case KeyEvent.VK_A: left(); break;
//		case KeyEvent.VK_S: down(); break;
//		case KeyEvent.VK_D: right(); break;
//		}
//		
//		move();
	}

	public boolean[] getKeys() {
		// TODO Auto-generated method stub	
		return move;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("click");

		Coordinate tlactPl = cph.getPlayer().c;
		Coordinate centrPl = Coordinate.fromCart(tlactPl.getCartX() + cph.getPlayer().getImg().getWidth()/2, tlactPl.getCartY() + cph.getPlayer().getImg().getHeight()/2);
		//from screen width/2, screen height/2 TO relative x, y mouse
		World.getQueueBullets().add(
				new Bullet(
						cph, 
						Coordinate.fromCart(Launcher.GAME_WIDTH/2, Launcher.GAME_HEIGHT/2), 
						Coordinate.fromCart(e.getX(), e.getY()),
						centrPl,
						cph.getPlayer(),
						20
				));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
