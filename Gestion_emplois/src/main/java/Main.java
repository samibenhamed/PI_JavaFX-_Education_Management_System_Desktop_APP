import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import jfxtras.scene.control.agenda.Agenda;
import org.example.Model.Enseignant;
import org.example.Model.Seance;
import org.example.controller.MainViewController;

import org.example.dao.EnseignantDAO;
import org.example.dao.SeanceDAO;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;





public class Main extends Application {

    @FXML
    private TableView<Seance> seanceTable;

    private TableView<Enseignant> table;
    private TextField nomField, prenomField, emailField, departementField;
    private ObservableList<Enseignant> enseignants;
    private final EnseignantDAO dao = new EnseignantDAO();


    @Override
    public void start(Stage primaryStage) {


        System.out.println("java.library.path = " + System.getProperty("java.library.path"));

        try {

            URL fxmlUrl = getClass().getResource("/org/example/view/MainView.fxml");


            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found.");
            }

            System.out.println("Loading FXML from: " + fxmlUrl);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            MainViewController controller = loader.getController();

            Agenda agenda = new Agenda();
            List<Seance> toutesLesSeances = SeanceDAO.getAll(); // ou ObservableList<Seance> convertie en List
            controller.chargerSeancesDepuisTable(agenda);

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("org/example/view/agenda.css").toExternalForm());

            try {
                String cssPath = Objects.requireNonNull(getClass().getResource("/style.css"), "CSS file not found").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (NullPointerException e) {
                System.err.println("Error loading CSS: " + e.getMessage());
            }

            primaryStage.setTitle("Gestion des Emplois");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Failed to launch application:");
            e.printStackTrace();
        }
    }


    private LocalDateTime nextDayOfWeek(DayOfWeek day) {
        LocalDateTime dt = LocalDateTime.now().with(LocalTime.MIN);
        while (dt.getDayOfWeek() != day) {
            dt = dt.plusDays(1);
        }
        return dt;
    }

    public void chargerSeancesDepuisTable(Agenda agenda) {
        for (Seance seance : seanceTable.getItems()) {
            try {
                LocalDate date = LocalDate.parse(seance.getDate());
                LocalTime debut = LocalTime.parse(seance.getHeureDebut().split("\\.")[0]);
                LocalTime fin = LocalTime.parse(seance.getHeureFin().split("\\.")[0]);
                LocalDateTime start = LocalDateTime.of(date, debut);
                LocalDateTime end = LocalDateTime.of(date, fin);

                Agenda.Appointment appointment = new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime(start)
                        .withEndLocalDateTime(end)
                        .withSummary(seance.getModule())
                        .withDescription("Salle " + seance.getSalleId() + " - " + seance.getEnseignant())
                        .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));

                agenda.appointments().add(appointment);

            } catch (Exception e) {
                System.err.println("Erreur lors de la conversion de la séance : " + e.getMessage());
            }
        }
    }}
