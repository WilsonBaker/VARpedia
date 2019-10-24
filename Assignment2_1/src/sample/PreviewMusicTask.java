package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class PreviewMusicTask extends Task {

	private String _song;

	public PreviewMusicTask(String song) {
		_song = song;
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
		runCommand("ffplay -t 10 ./Music/" + _song + ".mp3 -nodisp -nostats -hide_banner");

		return null;
	}
}
