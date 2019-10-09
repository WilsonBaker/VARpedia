package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CreateAudioTask extends Task{
	private ObservableList audioList;
	
	
	public CreateAudioTask(ObservableList list) {
		audioList=list;
	}
	public static void runCommand(String com) {
		 try {
	            
	            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", com);

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
			cmd = "echo \"file '" + path + "/Audio/" + item.toString() + ".wav'\" >> ./Audio/mylist.txt";
			runCommand(cmd);
		}
		
		cmd = "ffmpeg -y -f concat -safe 0 -i ./Audio/mylist.txt -c copy ./Creations/output.wav";
		runCommand(cmd);
		
		return null;
		
	}

}
