package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class PreviewFestivalTask extends Task {
/*echo "hello" | festival --tts*/
	private String _lines;
	
	public PreviewFestivalTask(String lines) {
		_lines=lines;
	}
	@Override
    protected Object call() throws Exception {
    	
        try {
        	String preview = "echo \""+_lines+"\" | festival --tts"  ;
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", preview);

            Process process = builder.start();

            InputStream out = process.getInputStream();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
        	
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
	}
}
