package tsp.gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import tsp.game.threads.Drawer;
import tsp.game.threads.Gamer;


public class MainWindow extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	
	//GUI components
	private final JPanel container = new JPanel();
	private final JMenuBar menu = new JMenuBar();
	private final Canvas canvas = new Canvas(this);
	
	//Game threads
	private Drawer drawer;
	private Gamer gamer;
	
	//Audio
	private Clip currentMusic;
	private static final String FIRST_SONG = new String("/music/empire.wav");
	private static final String END_SONG_DEATH = new String("/music/zanarkand.wav");
	
	public MainWindow() {
		super("TSP Game");
		drawer = new Drawer(canvas);
		gamer = new Gamer(this);
		drawer.start();
		
		makeMenu();
		this.setLayout(new BorderLayout());
		this.getContentPane().add(container, BorderLayout.CENTER);
		this.getContentPane().add(menu, BorderLayout.NORTH);
		container.setLayout(new GridLayout(1,1));
		container.add(new StartScreen(this));
		this.addKeyListener(this);
		setSize(800,600);
		setLocationRelativeTo(this.getParent());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowListener(){
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				drawer.interrupt();
				gamer.interrupt();
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	private void makeMenu() {
		JMenu file = new JMenu("Options");
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //you can't see me!
				dispose(); //Destroy the JFrame object
			}
		});
		file.add(close);
		menu.add(file);
	}
	
	protected void startGame(){
		container.remove(0);
		container.add(canvas);
		revalidate();
		playMusic(FIRST_SONG);
		gamer.start();
	}
	
	protected void endGame(boolean death){
		container.remove(0);
		gamer.interrupt();
		final EndScreen endScreen = new EndScreen(this, death);
		container.add(endScreen);
		revalidate();
		endScreen.repaint();
		if (death){
			playMusic(END_SONG_DEATH);
		}
//		long startTime = System.currentTimeMillis();
//		while(!endScreen.isLabelWhite){
//			endScreen.lighterLabel(startTime, System.currentTimeMillis());
//			endScreen.repaint();
//		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		canvas.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		canvas.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		canvas.keyTyped(e);
	}
	
	private void playMusic(String path){
		if (null != currentMusic){
			currentMusic.stop();
		}
		try{
	        AudioInputStream audio = AudioSystem.getAudioInputStream(MainWindow.class.getResource(path));
	        currentMusic = AudioSystem.getClip();
	        currentMusic.open(audio);
	        currentMusic.start();
	        currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
	    }catch(Exception ex){
	        System.out.println("Error with playing sound at: " + path);
	        ex.printStackTrace();
	    }
	}

	public void tick() {
		
		canvas.moveEnemy();
		
		if (canvas.player.x+20 >= canvas.enemyX && canvas.player.x <= canvas.enemyX+100){
			if (canvas.player.y+20 >= canvas.enemyY && canvas.player.y <= canvas.enemyY+100){
				canvas.end(true);
			}
		}
		
		canvas.repaint();
	}
	
}
