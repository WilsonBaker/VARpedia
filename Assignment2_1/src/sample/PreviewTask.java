package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;
/* This task has a purpose of previewing the audio chunks with a voice*/
public class PreviewTask extends Task {

	private String _lines;
	private Object _voice;
	public PreviewTask(String lines, Object voice) {
		_lines=lines;
		_voice=voice;
	}
	
	@Override
	/*When the task is run, the lines can be played in both espeak and festival*/
    protected Object call() throws Exception {
    	if(_voice.equals("espeak")) {
    		try {
            	
            	String preview = "espeak \""+_lines+"\" "  ;
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", preview);

                Process process = builder.start();

                InputStream out = process.getInputStream();
                BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
            	
            } catch(Exception ex) {
                ex.printStackTrace();
            }
    	} else if (_voice.equals("festival")) {
    		 try {
    	        	String preview = "echo \""+_lines+"\" | festival --tts"  ;
    	            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", preview);

    	            Process process = builder.start();

    	            InputStream out = process.getInputStream();
    	            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
    	        	
    	        } catch(Exception ex) {
    	            ex.printStackTrace();
    	        }
    	}
        

        return null;
	}
}


