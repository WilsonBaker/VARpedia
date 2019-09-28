package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CombineAudioTask extends Task {
	
	private ObservableList audioList;
	
	public CombineAudioTask (ObservableList list) {
		audioList = list;
	}
	
	public static void runCommand(String cmd) {
		 try {
	            
	            ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);

	            Process process = builder.start();

	            InputStream out = process.getInputStream();
	            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
	            int exitStatus = process.waitFor();
	            
	            if(exitStatus ==0) {
	            	
	            }
	            
		 }catch(Exception ex) {
	            ex.printStackTrace();
	        }
	}

	@Override
	protected Object call() throws Exception {
		String cmd;
		String path = System.getProperty("user.dir");
		for (Object item: this.audioList) {
			cmd = "echo \"file '" + path + "/Audio/" + item.toString() + ".mp3'\" >> ./Audio/mylist.txt";
			runCommand(cmd);
		}
		
		cmd = "ffmpeg -f concat -safe 0 -i ./Audio/mylist.txt -c copy ./Creations/output.mp3";
		runCommand(cmd);
		
		return null;
	}
}
