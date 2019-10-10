package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CreateAudioTask extends Task{
	private ObservableList audioList;
	private String _music;
	
	
	public CreateAudioTask(ObservableList list, String music) {
		audioList=list;
		_music = music;
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
		
		cmd = "ffmpeg -y -f concat -safe 0 -i ./Audio/mylist.txt -c copy ./Audio/speech.wav";
		runCommand(cmd);
		runCommand("ffmpeg -y -i ./Audio/speech.wav -i ./Music/" + _music + ".mp3 -filter_complex \"[0:0]volume=2.0[a];[1:0]volume=1.0[b];[a][b]amix=inputs=2:duration=shortest\" ./Creations/output.wav");
		
		return null;
		
	}

}
