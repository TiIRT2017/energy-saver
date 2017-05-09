package pwr.tiirt.energy.saver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import pwr.tiirt.energy.saver.model.AntennaOutOfBoundException;
import pwr.tiirt.energy.saver.presentation.GuiSupplier;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private GuiSupplier guiSupplier;

    @FXML
    private Button solveTopologyBtn;

    @FXML
    private Button importTopologyBtn;

    @FXML
    private Label coverageHolder;

    @FXML
    private FileChooser chooser;

    @FXML
    private Label topologyImportedLabel;

    @FXML
    private Label coverageLabel;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.guiSupplier = GuiSupplier.create(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AntennaOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    public void importTopology(ActionEvent actionEvent) {
        System.out.println("Importing topology...");
        chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);
        System.out.println("FILEPATH: " + file.getPath());
        try {
            guiSupplier = GuiSupplier.create(file.getPath());
        } catch (AntennaOutOfBoundException | IOException e) {
            e.printStackTrace();
        }
        enableControls();
    }

    public void solveTopology(ActionEvent actionEvent) {
        System.out.println("SOLVING TOPOLOGY...");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        guiSupplier.drawAntennas(gc);
        guiSupplier.drawRectangle(gc);
        fillTextFields();
        System.out.println("TOPOLOGY SOLVED!");
    }

    private void enableControls() {
        topologyImportedLabel.setVisible(true);
        solveTopologyBtn.setVisible(true);
        coverageHolder.setVisible(true);
    }

    private void fillTextFields() {
        double coverage = guiSupplier.getCoverage();
        coverageLabel.setVisible(true);
        coverageHolder.setText((Double.toString(coverage * 100)).substring(0, 5));
    }
}
