package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class DownloadBackgroundMusicTask extends Task {
	
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
		runCommand( "wget http://ccmixter.org/content/hansatom/hansatom_-_Paint_the_Sky.mp3 -P ./Background");
		
		return null;
	}

}
