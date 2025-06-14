/*package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Enseignant;
import org.example.dao.EnseignantDAO;

public class MainViewController {

    @FXML private TableView<Enseignant> tableEnseignants;
    @FXML private TableColumn<Enseignant, String> nomCol;
    @FXML private TableColumn<Enseignant, String> prenomCol;
    @FXML private TableColumn<Enseignant, String> emailCol;
    @FXML private TableColumn<Enseignant, String> departementCol;

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField departementField;

    private ObservableList<Enseignant> enseignants;
    private final EnseignantDAO dao = new EnseignantDAO();

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        departementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartement()));

        // Charger les enseignants depuis la base (ou autre source)
        enseignants = FXCollections.observableArrayList(dao.getAll());
        tableEnseignants.setItems(enseignants);




        // Remplir les champs quand on sélectionne une ligne
        tableEnseignants.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                prenomField.setText(newSelection.getPrenom());
                emailField.setText(newSelection.getEmail());
                departementField.setText(newSelection.getDepartement());
            }
        });


    }

    @FXML
    private void ajouterEnseignant() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();


        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || departement.isEmpty()) {
            showAlert("Tous les champs sont requis.");
            return;
        }

        int id = 0;
        Enseignant e = new Enseignant(id, nom, prenom, email, departement);
        dao.add(e); // si ta DAO l’enregistre en base
        enseignants.add(e);

        clearFields();
    }

    @FXML
    private void supprimerEnseignant() {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId()); // facultatif selon DAO
            enseignants.remove(selected);
        } else {
            showAlert("Veuillez sélectionner un enseignant à supprimer.");
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        departementField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void modifierEnseignant() {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Veuillez sélectionner un enseignant à modifier.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || departement.isEmpty()) {
            showAlert("Tous les champs sont requis pour la modification.");
            return;
        }

        // Update the selected enseignant
        selected.setNom(nom);
        selected.setPrenom(prenom);
        selected.setEmail(email);
        selected.setDepartement(departement);

        // Update in database
        dao.update(selected); // You need to implement this method in your DAO

        // Refresh table
        tableEnseignants.refresh();

        showAlert("L'enseignant a été modifié avec succès.");
        clearFields();
    }
}
*/
package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jfxtras.scene.control.agenda.Agenda;
import org.example.Model.Enseignant;
import jfxtras.scene.control.agenda.Agenda;
import org.example.Model.Seance;

import java.time.*;
import java.io.IOException;

public class MainViewController {
    @FXML
    private AnchorPane seanceView;
    @FXML private TabPane mainTabPane;
    @FXML private Tab enseignantsTab;
    @FXML private Tab sallesTab;
    @FXML private Tab seancesTab;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField departementField;
    @FXML private AnchorPane seanceContainer;
    private SeanceController seanceController;
    @FXML private Agenda agenda;
    @FXML private TableView<Enseignant> tableEnseignants;
     private ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();
    @FXML
    private AnchorPane seanceInclude;

    @FXML
    private TableView<Seance> seanceTable;

    @FXML
    private AnchorPane agendaPane;






    @FXML
    public void initialize() throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/seance_view.fxml"));
            StackPane seanceView = loader.load();
            seanceController = loader.getController();
            seanceContainer.getChildren().setAll(seanceView);

            //agenda.setDisplayedLocalTimeRange(LocalTime.of(8, 0), LocalTime.of(18, 0));
            agenda.setDisplayedLocalDateTime(LocalDateTime.of(2025, 6, 9, 0, 0));
            chargerSeancesDepuisTable(agenda);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public AnchorPane getAgendaPane() {
        return agendaPane;
    }





    public void chargerSeancesDepuisTable(Agenda agenda) {
        if (seanceController == null) {
            System.err.println("Le contrôleur de la vue Séances n’est pas initialisé !");
            return;
        }

        TableView<Seance> table = seanceController.getSeanceTable();
        if (table == null) {
            System.err.println(" TableView<Seance> est null !");
            return;
        }

        ObservableList<Seance> seances = table.getItems();
        System.out.println("Nombre de séances dans la table : " + seances.size());
        agenda.appointments().clear(); // nettoyer avant tout
        for (Seance seance : seances) {
            try {
                String cleanDebut = seance.getHeureDebut().split("\\.")[0];
                String cleanFin = seance.getHeureFin().split("\\.")[0];

                LocalDate date = LocalDate.parse(seance.getDate());
                LocalTime debut = LocalTime.parse(cleanDebut);
                LocalTime fin = LocalTime.parse(cleanFin);

                LocalDateTime start = LocalDateTime.of(date, debut);
                LocalDateTime end = LocalDateTime.of(date, fin);

                System.out.println("Séance ajoutée : " + start + " → " + end);

                Agenda.Appointment appointment = new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime(start)
                        .withEndLocalDateTime(end)
                        .withSummary(seance.getModule())
                        .withDescription("Salle " + seance.getSalleId() + " - " + seance.getEnseignant())
                        .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));

                agenda.appointments().add(appointment);
            } catch (Exception e) {
                System.err.println(" Erreur conversion séance : " + e.getMessage());
            }
        }
        System.out.println("Séances ajoutées dans l’agenda.");

        if (!seances.isEmpty()) {
            try {
                String dateStr = seances.get(0).getDate();
                String timeStr = seances.get(0).getHeureDebut().split("\\.")[0];
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime time = LocalTime.parse(timeStr);
                agenda.setDisplayedLocalDateTime(LocalDateTime.of(date, time));
            } catch (Exception e) {
                System.err.println(" Impossible de centrer l’agenda : " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleChargerAgenda() {
        chargerSeancesDepuisTable(agenda);
    }
}
