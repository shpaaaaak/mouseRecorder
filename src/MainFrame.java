import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import reproduction.Reproduct;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import tray.Tray;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import write.SavedHook;
import write.Tracking;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.util.Arrays;

public class MainFrame extends JFrame {

	private boolean write = true;
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;
	private JPanel contentPane;
	boolean writeOrReprod;
	FileNameExtensionFilter filter = new FileNameExtensionFilter("MR File", "mr");
	SavedHook ss = new SavedHook();
	/**
	 * Create the frame.
	 */
	public MainFrame(){
		setTitle("MouseRecorder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 200);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ImageIcon iconWrite = new ImageIcon("Zapis.png");
		JButton writeButton = new JButton("", iconWrite);
		writeButton.setBorderPainted(false);
		writeButton.setContentAreaFilled(true);
		writeButton.setOpaque(false);
		writeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		writeButton.setForeground(Color.BLACK);
		writeButton.setBackground(Color.WHITE);
		writeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					writeOrReprod = true;
					Tray tray = new Tray(MainFrame.this);
					tray.setTray(MainFrame.this, writeOrReprod);
					}
					catch(IOException io) {
						System.out.println(io);
					}
					
					Tracking track = new Tracking();
					track.setVisible(true);
					track.setVisible(false);
					
					//track.setState(JFrame.ICONIFIED);
					
				}
			
		});
		writeButton.setBounds(150, 10, 75, 75);
		contentPane.add(writeButton);
		
		ImageIcon iconRep= new ImageIcon("Pusk.png");
		JButton reproductionButton = new JButton("", iconRep);
		reproductionButton.setBorderPainted(false);
		reproductionButton.setContentAreaFilled(true);
		reproductionButton.setOpaque(false);
		reproductionButton.setHorizontalTextPosition(SwingConstants.CENTER);
		reproductionButton.setBackground(Color.WHITE);
		reproductionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(ss.getList().size() > 0) {
					writeOrReprod = false;
					Tray tray = new Tray(MainFrame.this);
					tray.setTray(MainFrame.this, writeOrReprod);
					Reproduct rep = new Reproduct();
					rep.Rep(false);
					}
					else {
						JOptionPane.showMessageDialog(MainFrame.this, 
				                 "Ничего не записано!!");
					}
					}
					catch(IOException io) {
						System.out.println(io);
					}
			}
		});
		reproductionButton.setBounds(235, 10, 75, 75);
		contentPane.add(reproductionButton);
		
		ImageIcon iconSave= new ImageIcon("Sokhranit.png");
		JButton saveButton = new JButton("", iconSave);
		
		saveButton.setBorderPainted(false);
		saveButton.setContentAreaFilled(true);
		saveButton.setOpaque(false);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setBackground(Color.WHITE);
		saveButton.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
				if(ss.getList().size() > 0) {
				ss.saveAll(ss.getList());
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Сохранение файла");
				fileChooser.setFileFilter(filter);
		        // Определение режима - только файлы
		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int result = fileChooser.showSaveDialog(MainFrame.this);
		        // Если директория выбрана, покажем ее в сообщении
		        if (result == JFileChooser.APPROVE_OPTION )
		        	JOptionPane.showMessageDialog(MainFrame.this, 
                            "Файл '" + fileChooser.getSelectedFile() + 
                            "' сохранен");
				}
				else {
					JOptionPane.showMessageDialog(MainFrame.this, 
			                 "Ничего не записано!!");
				}
			}
		});
		saveButton.setBounds(320, 10, 75, 75);
		contentPane.add(saveButton);
		
		ImageIcon iconLoad= new ImageIcon("Zagruzit.png");
		JButton loadButton = new JButton("", iconLoad);
		loadButton.setBorderPainted(false);
		loadButton.setContentAreaFilled(true);
		loadButton.setOpaque(false);
		loadButton.setHorizontalTextPosition(SwingConstants.CENTER);
		loadButton.setForeground(Color.BLACK);
		loadButton.setBackground(Color.WHITE);
		loadButton.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Выбор файла");
			fileChooser.setFileFilter(filter);
	        // Определение режима - только файлы
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        int result = fileChooser.showOpenDialog(MainFrame.this);
	        // Если директория выбрана, покажем ее в сообщении
	        if (result == JFileChooser.APPROVE_OPTION )
	        	JOptionPane.showMessageDialog(MainFrame.this, 
                        "Файл '" + fileChooser.getSelectedFile() + 
                        "' загружен");
	        ss.getList().clear();  
	        String str = "";
	        char buf[] = new char[256];
			try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
				int c;
				int i = 0;
	           while((c = reader.read())!=-1) {
	        	   
	        	   if((char)c == '\n') {
	        		  // buf = Arrays.copyOf(buf, c);
	        		   str =new String(buf);
	        		   ss.getList().add(str);
	        		   i = 0;
	        	   }
	        	   else
	        	   {
	        		   buf[i] = (char)c;
		        	   i++;
	        	   }
	        	   
	           }
	           
	        } catch (IOException ex) {

	            System.out.println(ex.getMessage());
	        	}

			}
		});
		loadButton.setBounds(405, 10, 75, 75);
		contentPane.add(loadButton);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon("Logotip.png"));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(10, 10, 130, 130);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setFocusable(false);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon("Fon.png"));
		lblNewLabel_1.setLabelFor(contentPane);
		lblNewLabel_1.setBounds(0, 0, 489, 161);
		contentPane.add(lblNewLabel_1);
		
		
	}
	
	
	public JFrame getFrame() {
		return MainFrame.this;
	}
}
