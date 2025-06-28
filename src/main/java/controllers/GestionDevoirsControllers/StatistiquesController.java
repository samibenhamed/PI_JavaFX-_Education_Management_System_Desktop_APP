package controllers.GestionDevoirsControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.SoumissionDAO;
import entities.Soumission;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesController {

    @FXML private BarChart<String, Number> barChartEtudiantsParDevoir;
    @FXML private TableView<Soumission> tableClassement;
    @FXML private TableColumn<Soumission, Integer> colEleveId;
    @FXML private TableColumn<Soumission, Integer> colDevoirId;
    @FXML private TableColumn<Soumission, Integer> colScore;

    @FXML
    public void initialize() {
        // Configurer l'axe Y pour avoir des ticks entiers
        NumberAxis yAxis = (NumberAxis) barChartEtudiantsParDevoir.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setForceZeroInRange(true);

        chargerStatistiqueEtudiantsParDevoir();
        chargerClassementScores();
    }

    private void chargerStatistiqueEtudiantsParDevoir() {
        try {
            List<Soumission> soumissions = SoumissionDAO.getAllSoumissions();

            Map<Integer, Long> etudiantsParDevoir = soumissions.stream()
                    .collect(Collectors.groupingBy(Soumission::getDevoirId, Collectors.counting()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<Integer, Long> entry : etudiantsParDevoir.entrySet()) {
                series.getData().add(new XYChart.Data<>("Devoir " + entry.getKey(), entry.getValue()));
            }

            barChartEtudiantsParDevoir.getData().clear();
            barChartEtudiantsParDevoir.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerClassementScores() {
        try {
            List<Soumission> soumissions = SoumissionDAO.getAllSoumissions();

            List<Soumission> topScores = soumissions.stream()
                    .sorted(Comparator.comparingInt(Soumission::getScore).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            ObservableList<Soumission> observableList = FXCollections.observableArrayList(topScores);

            colEleveId.setCellValueFactory(new PropertyValueFactory<>("eleveId"));
            colDevoirId.setCellValueFactory(new PropertyValueFactory<>("devoirId"));
            colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

            tableClassement.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
