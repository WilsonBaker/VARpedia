package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class CreateESpeakTask extends Task{
	private String _lines;
	private String _name;
	public CreateESpeakTask(String lines, String name) {
		_lines=lines;
		_name=name;
	}
	
	@Override
    protected Object call() throws Exception {
    	
        try {
        	
        	String create = "espeak \""+_lines+"\" -w Creations/output.wav ; lame Creations/output.wav Creations/"+_name+".mp3 ; rm -f Creations/output.wav"  ;
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", create);

            Process process = builder.start();

            InputStream out = process.getInputStream();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
        	
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
	}

}
