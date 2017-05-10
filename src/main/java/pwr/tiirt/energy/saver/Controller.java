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
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.presentation.GuiSupplier;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    private Label energyUsageHolder;

    @FXML
    private FileChooser chooser;

    @FXML
    private Label topologyImportedLabel;

    @FXML
    private Label coverageLabel;

    @FXML
    private Label energyUsageLabel;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        try {
            this.guiSupplier = GuiSupplier.create(null);
            canvas.setWidth(guiSupplier.getTranslate() * 2 + guiSupplier.getBoardWidth());
            canvas.setHeight(guiSupplier.getTranslate() * 2 + guiSupplier.getBoardHeight());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final AntennaOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    public void importTopology(final ActionEvent actionEvent) {
        chooser = new FileChooser();
        final File file = chooser.showOpenDialog(null);
        try {
            guiSupplier = GuiSupplier.create(file.getPath());
        } catch (AntennaOutOfBoundException | IOException e) {
            e.printStackTrace();
        }
        enableControls();
    }

    public void solveTopology(final ActionEvent actionEvent) {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        final List<AntennaWithRadius> antennas = guiSupplier.drawAntennas(gc);
        guiSupplier.drawRectangle(gc);
        fillTextFields(antennas);
    }

    private void enableControls() {
        topologyImportedLabel.setVisible(true);
        solveTopologyBtn.setVisible(true);
        coverageHolder.setVisible(true);
        energyUsageHolder.setVisible(true);
    }

    private void fillTextFields(final List<AntennaWithRadius> antennas) {
        final double coverage = guiSupplier.getCoverage();
        coverageLabel.setVisible(true);
        coverageHolder.setText((Double.toString(coverage * 100)).substring(0, 5));
        final double score = antennas.stream()
                                     .filter(AntennaWithRadius::isActive)
                                     .mapToDouble(a -> Math.pow(a.getR(), 2)).sum() / Algorithm.EFFICIENCY_COEFFICIENT;
        energyUsageLabel.setVisible(true);
        energyUsageHolder.setText((String.format("%.2f", score)));
    }
}
