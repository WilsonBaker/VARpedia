package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class PreviewESpeakTask extends Task {
/*espeak "string"*/
	private String _lines;
	public PreviewESpeakTask(String lines) {
		_lines=lines;
	}
	
	@Override
    protected Object call() throws Exception {
    	
        try {
        	System.out.println("hoh");
        	String preview = "espeak "+_lines+" "  ;
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


