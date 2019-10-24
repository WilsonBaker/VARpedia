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

		runCommand("echo \"http://ccmixter.org/content/snowflake/snowflake_-_Our_Big_House.mp3\" >> ./Background/list.txt");
		runCommand("echo \"http://ccmixter.org/content/gurdonark/gurdonark_-_House_of_Ghosts.mp3\" >> ./Background/list.txt");
		runCommand("echo \"http://ccmixter.org/content/jlbrock44/jlbrock44_-_Waves_Of_Tranquility_.mp3\" >> ./Background/list.txt");
		runCommand("echo \"http://ccmixter.org/content/texasradiofish/texasradiofish_-_Funk_n_Jazz.mp3\" >> ./Background/list.txt");
		runCommand("echo \"http://ccmixter.org/content/cdk/cdk_-_Like_Music_(cdk_Mix)_1.mp3\" >> ./Background/list.txt");


		runCommand( "wget -i ./Background/list.txt -P ./Background");



		return null;
	}

}
