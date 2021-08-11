package me.iowa.clickreader;


import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private final Stage stage;

    private final Data data;



    @FXML
    private JFXTextField dataNameField;

    @FXML
    private JFXTextArea dataField;

    @FXML
    private Button readButton, clearButton;

    @FXML
    private LineChart<Number, Number> lineChartUp, lineChartDown, lineChartComplete;

    public Controller() {
        this.stage = new Stage();
        this.data = new Data();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            this.stage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            this.stage.setTitle("Click Reader");

            this.stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        this.stage.showAndWait();
    }


    @FXML
    private void initialize() {
        lineChartUp.getStylesheets().add("css/linechart.css");
        lineChartDown.getStylesheets().add("css/linechart.css");
        lineChartComplete.getStylesheets().add("css/linechart.css");
        dataField.getStylesheets().add("css/textfield.css");
        dataNameField.getStylesheets().add("css/textfield.css");
        dataField.setLabelFloat(true);
        dataNameField.setLabelFloat(true);



        readButton.setOnMouseReleased(event -> {
            data.readData(dataField.getText());

            String dataName = dataNameField.getText() == null ? "Data" : dataNameField.getText();

            XYChart.Series<Number, Number> upSeries = new XYChart.Series<>();
            upSeries.setName(dataName);
            XYChart.Series<Number, Number> downSeries = new XYChart.Series<>();
            downSeries.setName(dataName);
            XYChart.Series<Number, Number> clickDelaySeries = new XYChart.Series<>();
            clickDelaySeries.setName(dataName);

            int x = 0;
            for (Integer upDelay : data.getUpDelays()) {
                XYChart.Data<Number, Number> data = new XYChart.Data<>(x++, upDelay);
                data.setNode(new HoveredThresholdNode(upDelay));
                upSeries.getData().add(data);
            }
            x = 0;
            for (Integer downDelay : data.getDownDelays()) {
                XYChart.Data<Number, Number> data = new XYChart.Data<>(x++, downDelay);
                data.setNode(new HoveredThresholdNode(downDelay));
                downSeries.getData().add(data);
            }
            x = 0;
            for (Integer clickDelay : data.getClickDelays()) {
                XYChart.Data<Number, Number> data = new XYChart.Data<>(x++, clickDelay);
                data.setNode(new HoveredThresholdNode(clickDelay));
                clickDelaySeries.getData().add(data);
            }
            lineChartUp.setCursor(Cursor.CROSSHAIR);
            lineChartUp.getXAxis().setLabel("Clicks");
            lineChartUp.getYAxis().setLabel("MS Delay");
            lineChartUp.getData().add(upSeries);
            lineChartUp.setLegendVisible(true);
            lineChartDown.setCursor(Cursor.CROSSHAIR);
            lineChartDown.getXAxis().setLabel("Clicks");
            lineChartDown.getYAxis().setLabel("MS Delay");
            lineChartDown.getData().add(downSeries);
            lineChartDown.setLegendVisible(true);
            lineChartComplete.setCursor(Cursor.CROSSHAIR);
            lineChartComplete.getXAxis().setLabel("Clicks");
            lineChartComplete.getYAxis().setLabel("MS Delay");
            lineChartComplete.getData().add(clickDelaySeries);
            lineChartComplete.setLegendVisible(true);
        });
        clearButton.setOnMouseReleased(event -> {
            lineChartUp.getData().clear();
            lineChartDown.getData().clear();
            lineChartComplete.getData().clear();
        });
        dataField.setPromptText("Please insert data from the click recorder here.");
        dataNameField.setPromptText("Data Name");
    }

    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(int value) {
            setPrefSize(3, 3);

            final Label label = createDataThresholdLabel(value);

            setOnMouseEntered(mouseEvent -> {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            });
            setOnMouseExited(mouseEvent -> {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            });
        }
    }
    private Label createDataThresholdLabel(int value) {
        final Label label = new Label(value + "");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-background-color: #FFFFFF");

        label.setTextFill(Paint.valueOf("#000000"));

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
