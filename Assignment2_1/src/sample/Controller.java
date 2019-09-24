package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ListView listView;


    public void buttonCreate(ActionEvent event) throws IOException {
        Parent createParent = FXMLLoader.load(getClass().getResource("create.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }

    public void buttonPlay(ActionEvent event) {
        String playString = listView.getSelectionModel().getSelectedItem().toString();

        playString = "./" + playString + ".mp4";

        try {
            String cmd = "ffplay -autoexit " + playString;

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);

            Process process = pb.start();

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
            } else {
                System.out.println("Nay!");
            }

        }catch (Exception e) {

        }

    }

    public String[]  getCreations() {

        try {
            String cmd = "ls -1 | egrep '\\.mp4$' | sed -e 's/\\..*$//'";

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);

            Process process = pb.start();


            ArrayList<String> viewList = new ArrayList<String>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                viewList.add(line);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                String[] strings = viewList.toArray(new String[viewList.size()]);
                return strings;
            } else {
                //abnormal..
            }

        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
   }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] strings = getCreations();
        listView.getItems().addAll(strings);
    }


    /*
    try {
        String command = "echo \"==============================================================\"";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

        Process process = pb.start();

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        int exitStatus = process.waitFor();

        if (exitStatus == 0) {
            String line;
            while((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
        } else {
            String line;
            while ((line = stderr.readLine()) != null) {
                System.err.println("ya boi");
            }
        }
    } catch (Exception e) {
        System.out.println("Yeest");

    }

     */
}
