package tray;

import static java.awt.SystemTray.getSystemTray;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.JFrame;

import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;


public class Tray extends JFrame{
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;
	
	
	
	public Tray(JFrame jf) throws IOException {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		trayIcon = new TrayIcon(ImageIO.read(new File("icon.png")), "Mouse Recorder");
		 
	    trayIcon.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        jf.setVisible(true);
	        jf.setState(JFrame.NORMAL);
	        removeTrayIcon();
	      }
	    });
	    addWindowStateListener(new WindowStateListener()
	    {
	      public void windowStateChanged(WindowEvent e)
	      {
	        if(e.getNewState() == JFrame.ICONIFIED)
	        {
	          jf.setVisible(false);
	          
	          
	        }
	        
	      }
	  	
	    });	
	}
	
	public void setTray(JFrame jf, boolean writeOrReprod) throws IOException{
		if(writeOrReprod == true) {
	          addTrayIcon("Идет запись");
	          }
	          else {
	        	  addTrayIcon("Идет воспроизведение");
	          }
		jf.setVisible(false);
    }
	
	public void delTray(JFrame jf) throws IOException{
		removeTrayIcon();
		jf.setVisible(true);
    }
	
	
	public void removeTrayIcon()
	  {
	    systemTray.remove(trayIcon);
	  }
	 
	  public void addTrayIcon(String mess)
	  {
	    try
	    {
	      systemTray.add(trayIcon);
	      trayIcon.displayMessage("MouseRecorder", mess, TrayIcon.MessageType.INFO);
	    }
	    catch(AWTException ex)
	    {
	      ex.printStackTrace();
	    }
	  }
}
	