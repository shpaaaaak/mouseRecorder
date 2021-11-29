/* JNativeHook: Global keyboard and mouse hooking for Java.
 * Copyright (C) 2006-2018 Alexander Barker.  All Rights Received.
 * https://github.com/kwhat/jnativehook/
 *
 * JNativeHook is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNativeHook is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package write;

// Imports.
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import tray.Tray;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A demonstration of how to use the JNativeHook library.
 *
 * @author	Alexander Barker (<a href="mailto:alex@1stleg.com">alex@1stleg.com</a>)
 * @version	2.0
 * @since	1.0
 *
 * @see GlobalScreen
 * @see NativeKeyListener
 */
public class Tracking extends JFrame implements ActionListener, ItemListener, NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener, WindowListener {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1541183202160543102L;

	private JCheckBoxMenuItem menuItemEnable;

	/** The text area to display event info. */

	/** Logging */
	private static final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
	
	public SavedHook s = new SavedHook();
	

	/**
	 * Instantiates a new native hook demo.
	 */
	public Tracking() {
		
		s.getList().clear();   
		
		// Setup the main window.
		setTitle("JNativeHook Demo");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 300);
		addWindowListener(this);
		

		// Create the view.
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		menuItemEnable = new JCheckBoxMenuItem("Enable Native Hook");
		menuItemEnable.addItemListener(this);
		menuItemEnable.setMnemonic(KeyEvent.VK_H);
		menuItemEnable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuView.add(menuItemEnable);


		// Disable parent logger and set the desired level.
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);

		// Add our custom formatter to a console handler.
		/*ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new LogFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);*/

		/* Note: JNativeHook does *NOT* operate on the event dispatching thread.
		 * Because Swing components must be accessed on the event dispatching
		 * thread, you *MUST* wrap access to Swing components using the
		 * SwingUtilities.invokeLater() or EventQueue.invokeLater() methods.
		 */
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
		
		//setVisible(true);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		try {
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		GlobalScreen.addNativeMouseMotionListener(this);
		GlobalScreen.addNativeMouseWheelListener(this);
		}
		catch (NativeHookException ex) {
		}
		
	}

	/**
	 * @see org.jnativehook.keyboard.NativeKeyListener#nativeKeyPressed(org.jnativehook.keyboard.NativeKeyEvent)
	 */
	public void nativeKeyPressed(NativeKeyEvent e) {
		int k = e.getKeyCode();
		if(k==88) {			
			//s.saveAll(s.getList());
			
			try {
				GlobalScreen.unregisterNativeHook();
			}       
			catch (NativeHookException ex) {
				ex.printStackTrace();
			}
		}
		else {
		s.saveHookKey(e);
		}
		//displayEventInfo(e);
	}

	/**
	 * @see org.jnativehook.keyboard.NativeKeyListener#nativeKeyReleased(org.jnativehook.keyboard.NativeKeyEvent)
	 */
	public void nativeKeyReleased(NativeKeyEvent e) {
		//displayEventInfo(e);
		s.saveHookKey(e);
	}

	/**
	 * @see org.jnativehook.keyboard.NativeKeyListener#nativeKeyTyped(org.jnativehook.keyboard.NativeKeyEvent)
	 */
	public void nativeKeyTyped(NativeKeyEvent e) {
		//displayEventInfo(e);
		s.saveHookKey(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseListener#nativeMouseClicked(org.jnativehook.mouse.NativeMouseEvent)
	 */
	public void nativeMouseClicked(NativeMouseEvent e) {
		//displayEventInfo(e);
		s.saveHookMouse(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseListener#nativeMousePressed(org.jnativehook.mouse.NativeMouseEvent)
	 */
	public void nativeMousePressed(NativeMouseEvent e) {
		//displayEventInfo(e);
		s.saveHookMouse(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseListener#nativeMouseReleased(org.jnativehook.mouse.NativeMouseEvent)
	 */
	public void nativeMouseReleased(NativeMouseEvent e) {
		//displayEventInfo(e);
		s.saveHookMouse(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseMotionListener#nativeMouseMoved(org.jnativehook.mouse.NativeMouseEvent)
	 */
	public void nativeMouseMoved(NativeMouseEvent e) {
		//displayEventInfo(e);
		s.saveHookMouse(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseMotionListener#nativeMouseDragged(org.jnativehook.mouse.NativeMouseEvent)
	 */
	public void nativeMouseDragged(NativeMouseEvent e) {
		//displayEventInfo(e);
		s.saveHookMouse(e);
	}

	/**
	 * @see org.jnativehook.mouse.NativeMouseWheelListener#nativeMouseWheelMoved(org.jnativehook.mouse.NativeMouseWheelEvent)
	 */
	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		//displayEventInfo(e);
		s.saveHookMouseWheel(e);
	}

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Display information about the native keyboard and mouse along with any
	 * errors that may have occurred.
	 *
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
		// Return the focus to the window.
		this.requestFocusInWindow();

		// Enable the hook, this will cause the GlobalScreen to be initilized.
		menuItemEnable.setSelected(true);
	}

	/**
	 * Finalize and exit the program.
	 *
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
		// Clean up the native hook.
	}
}