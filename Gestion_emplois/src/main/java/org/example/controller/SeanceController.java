package org.example.controller;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.itextpdf.text.Document;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import java.awt.*;
import com.itextpdf.text.Element;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.io.FileOutputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import org.example.dao.EnseignantDAO;
import org.example.dao.SeanceDAO;
import org.example.Model.Seance;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.scene.control.TableView;
import org.example.services.EmailService;
import org.example.services.GeminiClient;
import org.example.utils.DatabaseConnection;


public class SeanceController implements Initializable {
    @FXML
    private TableView<Seance> tableSeances;


    @FXML
    private TextField EmailField;
    @FXML
    private TextField emailField;

    private Stage primaryStage;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField heureDebutField;

    @FXML
    private TextField heureFinField;

    @FXML
    private TextField salleIdField;

    @FXML
    private TextField moduleField;

    @FXML
    private TextField enseignantField;



    @FXML
    private TableColumn<Seance, String> jourCol;

    @FXML
    private TableColumn<Seance, String> heureDebutCol;

    @FXML
    private TableColumn<Seance, String> heureFinCol;

    @FXML
    private TableColumn<Seance, String> EmailCol;


    @FXML
    private TableColumn<Seance, Integer> salleCol;

    @FXML
    private TableColumn<Seance, String> moduleCol;
    @FXML
    private StackPane rootStack;
    @FXML
    private BorderPane myBorderPane;

    @FXML
    private TableColumn<Seance, String> enseignantCol;

    @FXML
    private TextField jourField;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button modifierBoutton;




    @FXML
    private ObservableList<Seance> seanceList = FXCollections.observableArrayList();

    private SeanceDAO seanceDAO = new SeanceDAO();

    private Seance selectedSeance;

    // Method to handle adding a Seance
    @FXML
    public void addSeance() {
        try {
            String date = dateField.getValue().toString();
            String heureDebut = heureDebutField.getText();
            String heureFin = heureFinField.getText();
            int salleId = Integer.parseInt(salleIdField.getText());
            String module = moduleField.getText();
            String enseignant = enseignantField.getText();
            String email = EmailField.getText();

            if (date == null || heureDebut.isEmpty() || heureFin.isEmpty()) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.", AlertType.WARNING);
                return;
            }

            // 👇 Use 0 or omit if ID is auto-incremented
            Seance seance = new Seance(0, date, heureDebut, heureFin, salleId, module, enseignant, email);
            seanceDAO.add(seance);
            seanceList.add(seance);

            showAlert("Séance ajoutée", "La séance a été ajoutée avec succès.", AlertType.INFORMATION);

            showToast("Séance ajoutée avec succès");



        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Les champs ID doivent être des nombres valides.", AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace(); // for debugging
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la séance.", AlertType.ERROR);
        }
    }



    @FXML
    public void updateSeance() {
        if (selectedSeance == null) {
            showAlert("Aucune séance sélectionnée.");
            return;
        }

        try {
            if (dateField.getValue() == null ||
                    heureDebutField.getText().isEmpty() ||
                    heureFinField.getText().isEmpty() ||
                    salleIdField.getText().isEmpty() ||
                    moduleField.getText().isEmpty() ||
                    enseignantField.getText().isEmpty() ||
                    EmailField.getText().isEmpty())
            {

                showAlert("Tous les champs doivent être remplis.");
                return;
            }

            String date = dateField.getValue().toString();
            String heureDebut = heureDebutField.getText();
            String heureFin = heureFinField.getText();
            int salleId = Integer.parseInt(salleIdField.getText());
            String module = moduleField.getText();
            String enseignant = enseignantField.getText();
            String email = EmailField.getText();

            Seance seance = new Seance(selectedSeance.getId(), date, heureDebut, heureFin, salleId, module, enseignant, email);
            seanceDAO.update(seance);
            loadSeances();

            showAlert("Séance mise à jour", "La séance a été mise à jour avec succès.", AlertType.INFORMATION);

            showToast("Séance mise à jour !");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des ID valides.", AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de mettre à jour la séance.", AlertType.ERROR);
        }
    }


    @FXML
    public void deleteSeance () {



        if (selectedSeance == null) {
            showAlert("Aucune séance sélectionnée.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette séance ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                seanceDAO.delete(selectedSeance.getId());
                seanceList.remove(selectedSeance);
               selectedSeance = null;
                refreshTable();


                showAlert("Séance supprimée", "La séance a été supprimée avec succès.", AlertType.INFORMATION);
                showToast("Séance supprimée");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de supprimer la séance.", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void refreshTable() {
        seanceList.setAll(SeanceDAO.getAll());
        tableSeances.setItems(seanceList);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jourCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureDebutCol.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinCol.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        salleCol.setCellValueFactory(new PropertyValueFactory<>("salleId"));
        moduleCol.setCellValueFactory(new PropertyValueFactory<>("module"));
        enseignantCol.setCellValueFactory(new PropertyValueFactory<>("enseignant"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableSeances.setItems(seanceList);

        loadSeances();

        tableSeances.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSeance = newSelection;
                dateField.setValue(LocalDate.parse(selectedSeance.getDate()));
                heureDebutField.setText(selectedSeance.getHeureDebut());
                heureFinField.setText(selectedSeance.getHeureFin());
                salleIdField.setText(String.valueOf(selectedSeance.getSalleId()));
                moduleField.setText(selectedSeance.getModule());
                enseignantField.setText(selectedSeance.getEnseignant());
                EmailField.setText(selectedSeance.getEmail());
            }
        });

        tableSeances.setRowFactory(tv -> {
            TableRow<Seance> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableSeances.getSelectionModel().clearSelection(); // ensure selection is cleared
                    clearFormFields(); // clear form
                }
            });
            return row;
        });
        /// end handle click function
    }


    private void clearFormFields() {
        dateField.setValue(null);
        heureDebutField.clear();
        heureFinField.clear();
        salleIdField.clear();
        moduleField.clear();
        enseignantField.clear();
        EmailField.clear();
        selectedSeance = null;
    }


    private void loadSeances() {
        seanceList.clear();
        seanceList.addAll(seanceDAO.getAll()); // tu dois avoir une méthode getAll() dans ton DAO
    }
    // Helper method to show an alert box
    public void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void envoyerEmailsPersonnalises() {
        List<Seance> toutesSeances = seanceDAO.getAll();  // Utilise ton DAO

        // Groupement par enseignant
        Map<String, List<Seance>> seancesParEnseignant = new HashMap<>();

        for (Seance seance : toutesSeances) {
            String nom = seance.getEnseignant();
            if (nom == null || nom.isEmpty()) continue;

            seancesParEnseignant.computeIfAbsent(nom, k -> new ArrayList<>()).add(seance);
        }

        for (Map.Entry<String, List<Seance>> entry : seancesParEnseignant.entrySet()) {
            String nomEnseignant = entry.getKey();
            List<Seance> seances = entry.getValue();

            String email = seances.get(0).getEmail();

            if (email == null || email.trim().isEmpty()) {
                System.out.println("Aucune adresse email pour l’enseignant : " + nomEnseignant);
                continue;
            }

            String contenu = GeminiClient.generateEmailText(nomEnseignant, seances);
            EmailService.sendEmploiTo( contenu);
        }

        showToast("Les emails ont été envoyés avec succès !");
    }




    public void exportSeancesToPdf(List<Seance> seanceList) {
        Document document = new Document();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Schedule PDF");
            fileChooser.setInitialFileName("emploi_du_temps.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            java.io.File file = fileChooser.showSaveDialog(null);
            if (file == null) return;

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Emploi du Temps", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable pdfTable = new PdfPTable(6); // 6 colonnes
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10);

            // En-têtes
            pdfTable.addCell("Jour");
            pdfTable.addCell("Heure");
            pdfTable.addCell("Module");
            pdfTable.addCell("Enseignant");
            pdfTable.addCell("Salle");
            pdfTable.addCell("Email");

            // Contenu
            for (Seance seance : seanceList) {
                pdfTable.addCell(seance.getDate());
                pdfTable.addCell(seance.getHeureDebut() + " - " + seance.getHeureFin());
                pdfTable.addCell(seance.getModule() != null ? seance.getModule().toString() : "");
                pdfTable.addCell(seance.getEnseignant() != null ? seance.getEnseignant() : "");
                pdfTable.addCell(seance.getSalleId() != 0 ? String.valueOf(seance.getSalleId()) : "");
                pdfTable.addCell(seance.getEmail() != null ? seance.getEmail() : ""); // AJOUT DE CETTE LIGNE
            }

            document.add(pdfTable);
            document.close();

            System.out.println("PDF generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleExportPDF() {
        exportSeancesToPdf(tableSeances.getItems());
    }


    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }



    public void showToast(String message) {
        Label toastLabel = new Label(message);
        toastLabel.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5;");
        toastLabel.setOpacity(0);

        // Position en haut à droite
        StackPane.setAlignment(toastLabel, Pos.TOP_RIGHT);

        // Décalage (marge) depuis le coin supérieur droit
        StackPane.setMargin(toastLabel, new Insets(20, 20, 0, 0));

        rootStack.getChildren().add(toastLabel);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toastLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toastLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.setOnFinished(e -> rootStack.getChildren().remove(toastLabel));
        sequence.play();
    }


    @FXML
    private void sendEmailDirectly() {
        String toEmail = emailField.getText().trim();
        if (toEmail.isEmpty()) {
            System.out.println("Please enter an email address.");
            return;
        }

        final String username = "hammami.chaima1998@gmail.com";      // your Gmail
        final String password = "ottqhanzrkykhcmk";         // your Gmail app password

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Subject from JavaFX");
            message.setText("Hello! This is a message sent directly from JavaFX controller.");

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleSendEmail(javafx.event.ActionEvent event) {
        String email = emailField.getText();
        if (email != null && !email.trim().isEmpty()) {
            List<Seance> seances = EmailService.fetchSeancesFromDB(); // méthode existante
            String content = EmailService.buildHtmlTable(seances);
            EmailService.sendEmail(email, "Emploi du temps", content); // envoi via Gmail
        } else {
            System.out.println("Veuillez entrer une adresse email valide.");
        }
    }


    public TableView<Seance> getSeanceTable() {
        return tableSeances;
    }




    private String extractNameFromEmail(String email) {
        // Supprime le domaine
        String base = email.split("@")[0];

        // Remplace les points/underscores par des espaces
        base = base.replace(".", " ").replace("_", " ");

        // Met en majuscule la première lettre de chaque mot
        String[] parts = base.split(" ");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    @FXML
    private void sendAIEmailsToAll() {
        List<String> allEmails = seanceDAO.getAllDistinctEmails();

        for (String email : allEmails) {
            List<Seance> seances = seanceDAO.getSeancesByEmail(email);
            if (seances.isEmpty()) continue;

            String enseignantNom = seances.get(0).getEnseignant(); // car tous les seances de ce mail ont le même enseignant
            String contenuIA = GeminiClient.generateEmailText(enseignantNom, seances);

            EmailService.sendEmail(email, "Votre emploi du temps IA", contenuIA);
            System.out.println("Email IA envoyé à " + email);
        }

        showAlert("Tous les emplois du temps ont été envoyés avec succès.");
    }


}
