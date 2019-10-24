package sample;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CreateFlickrTask extends Task{
	private ArrayList<String> _rimages;
	private ArrayList<String> _images = new ArrayList<String>();
	private String _wikitSearch ;
	private String _name;
	private int _number;
	/*private ObservableList audioList;*/

	private Float _duration = 0.0f;
	public CreateFlickrTask(String search, String name , String number, ArrayList<String> rimages/*,ObservableList list*/) {
		_wikitSearch=search;
		_name=name;
		_rimages=rimages;
		/*audioList = list;*/
		try {
			_number=Integer.parseInt(number);

		}catch(NumberFormatException e){

		}

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
	/* This method grabs tha API key from the txt file*/
	public static String getAPIKey(String key) throws Exception {


		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 


		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 

		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}
	@Override
	/*When the task is run the 10 photos are stored in an array*/
	protected Object call() throws Exception {
		String cmd;
		String path = System.getProperty("user.dir");




		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

			String query = _wikitSearch;
			int resultsPerPage = 10/*_number*/;
			int page = 0;


			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos"); 
			params.setText(query);

			PhotoList<Photo> results = photos.search(params, resultsPerPage, page);

			/* Make all images the same size*/
			for (Photo photo: results) {

				try {
					BufferedImage image = photos.getImage(photo,Size.LARGE);
					String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
					File outputfile = new File(path+ "/Creations",filename);
					_images.add(path+ "/Creations/"+filename);
					ImageIO.write(image, "jpg", outputfile);

				} catch (FlickrException fe) {
					System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}

			}

			for (String i : _images) {
				String rimage = i.replace(".","new." );
				_rimages.add(rimage);
				runCommand("ffmpeg -y -i "+ i + " -vf scale=400:400 "+ rimage);


			}


		} catch (Exception e) {

			e.printStackTrace();
		}


		return null;
	}
}
