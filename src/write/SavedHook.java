package write;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

public class SavedHook {

	private static ArrayList <String> save = new ArrayList<String>();
	
	
	public ArrayList getList() {
		return this.save;
	}
	
	public void saveHookKey(NativeKeyEvent e) {
		save.add(e.paramString());
	}
	
	public void saveHookMouse(NativeMouseEvent e) {
		save.add(e.paramString());
	}
	
	public void saveHookMouseWheel(NativeMouseWheelEvent e) {
		save.add(e.paramString());
	}
	
	public void saveAll(ArrayList<String> e) {
		String str = "";
		try (FileWriter writer = new FileWriter("MouseRecorderFile.mr", false)) {
            for (int i = 0; i < e.size(); i++) {
            	str = e.get(i) + "\r\n";
                writer.write(str);
                str = "";
            }
           
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
	}
	
}
