package reproduction;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.sun.glass.events.KeyEvent;

import write.SavedHook;

public class Reproduct {

	public void Rep(boolean fileOrArrayList) {
		if(fileOrArrayList == false) {      
			
			SavedHook sh = new SavedHook();
			String[] hookMass;
			char[] buff;
			
			for(int i=0; i<sh.getList().size(); i++) {
				hookMass = hookToString(sh.getList().get(i).toString());
				
				buff = hookMass[1].toCharArray();
				if(buff[0]=='(') {
				char[] buff2 = new char[buff.length - 1];
				
				for(int q=1; q<buff.length; q++) {
					buff2[q-1] =buff[q]; 
				}
				hookMass[1] =new String(buff2);
				
				buff = hookMass[2].toCharArray();       
				buff2 = new char[buff.length - 1];
				
				for(int q=0; q<buff.length-1; q++) {
					buff2[q] =buff[q]; 
				}
				hookMass[2] =new String(buff2);
				}
				robotHook(hookMass);
			}
		}
	}
	
	public String[] hookToString(String hook) {
		String delimeter = ",";
		String[] subStr = hook.split(delimeter);
		return subStr;
	}
	
	public void robotHook(String hookMass[]) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen=env.getDefaultScreenDevice();
        try {
			Robot robot = new Robot(screen);
			if(hookMass[0].equals("NATIVE_MOUSE_MOVED")){
				robot.mouseMove(Integer.parseInt(hookMass[1]), Integer.parseInt(hookMass[2]));
				robot.delay(25); 
			}
			if(hookMass[0].equals("NATIVE_MOUSE_PRESSED")) {
				char[] mouseKey = hookMass[3].toCharArray();
				//robot.mousePress((int)mouseKey[7]);
				if(mouseKey[7] == '1') {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.delay(150); 
				}
				if(mouseKey[7] == '2') {
					robot.mousePress(InputEvent.BUTTON3_MASK);
					robot.delay(150); 
					}
				/*if(mouseKey[7] == '3') {
					robot.mousePress(InputEvent.BUTTON3_MASK);
					robot.delay(150); 
					}*/
			}
			if(hookMass[0].equals("NATIVE_MOUSE_RELEASED")) {
				char[] mouseKey = hookMass[3].toCharArray();
				//robot.mouseRelease((int)mouseKey[7]);
				if(mouseKey[7] == '1') {
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
				if(mouseKey[7] == '2') {
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
					}
				/*if(mouseKey[7] == '3') {
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
					}*/
			}
			
			if(hookMass[0].equals("NATIVE_MOUSE_CLICKED")) {
				
			}
			
			
			if(hookMass[0].equals("NATIVE_KEY_PRESSED")) {
				String delemiter = "=";
				String[] buff = hookMass[hookMass.length-1].split(delemiter);
				if(buff[1].equals("162")) {
					robot.keyPress(17);
					robot.delay(50);
				}
				else {
					if(buff[1].equals("160")) {
						robot.keyPress(16);
						robot.delay(50);
					}
					else {
						if(buff[1].equals("164")) {
							robot.keyPress(KeyEvent.VK_ALT);
							robot.delay(50);
						}
						else {
							robot.keyPress(Integer.parseInt(buff[1]));
							robot.delay(50);
						}
					}
				}
			}
			if(hookMass[0].equals("NATIVE_KEY_RELEASED")) {
				String delemiter = "=";
				String[] buff = hookMass[hookMass.length-1].split(delemiter);
				if(buff[1].equals("162")) {
					robot.keyRelease(17);
					robot.delay(50);
				}
				else {
					if(buff[1].equals("160")) {
						robot.keyRelease(16);
						robot.delay(50);
					}
					else {
						if(buff[1].equals("164")) {
							robot.keyRelease(KeyEvent.VK_ALT);
							robot.delay(50);
						}
						else {
							robot.keyRelease(Integer.parseInt(buff[1]));
							robot.delay(50);
						}
					}
				}
			}
			
			
			if(hookMass[0].equals("NATIVE_MOUSE_WHEEL")) {
				String delemiter = "=";
				String[] buff = hookMass[7].split(delemiter);
				//System.out.println(buff[1]);
				robot.mouseWheel(Integer.parseInt(buff[1]));
				robot.delay(50);
			}
			if(hookMass[0].equals("NATIVE_MOUSE_DRAGGED")) {	
				robot.mouseMove(Integer.parseInt(hookMass[1]), Integer.parseInt(hookMass[2]));
				robot.delay(25); 
			}
			
			}
			catch(AWTException ex) {
				
			}
		
	}
	
}
