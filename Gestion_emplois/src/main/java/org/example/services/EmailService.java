package org.example.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.example.Model.Seance;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.sql.*;



public class EmailService {

    @FXML
    private static ObservableList<Seance> seanceList = FXCollections.observableArrayList();

    public static void sendEmploiTo(String recipientEmail) {
        final String senderEmail = "hammami.chaima@gmail.com";
        final String username = "18aab14995ea8a";
        final String password = "5955a598373bd3";

        // SMTP configuration for Mailtrap
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
          //  String emploiContent = fetchSeancesFromDB(); // Or build HTML if needed

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Emploi du temps");

            List<Seance> seances = fetchSeancesFromDB();
            String emploiContent = new EmailService().buildHtmlTable(seances);
            message.setContent(emploiContent, "text/html; charset=utf-8");
            // For sending as HTML, use the below instead:
            // message.setContent(buildHtmlTable(seances), "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully to: " + recipientEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Seance> fetchSeancesFromDB() {
        List<Seance> seances = new ArrayList<>();

        String query = "SELECT id,  date, heure_debut, heure_fin, salle_id, module, enseignant FROM seance";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-JBT79SP8\\MSSQLSERVER01;databaseName=Schedule_Managment;TrustServerCertificate=true;integratedSecurity=true;\n")) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seance s = new Seance();
                s.setId(rs.getInt("id"));

                s.setDate(rs.getDate("date").toString());
                s.setHeureDebut(rs.getString("heure_debut"));
                s.setHeureFin(rs.getString("heure_fin"));
                s.setSalleId(rs.getInt("salle_id"));
                s.setModule(rs.getString("module"));
                s.setEnseignant(rs.getString("enseignant"));
                System.out.println("Enseignant: " + rs.getString("enseignant"));
                seances.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }


    public static String buildHtmlTable(List<Seance> seances) {
        StringBuilder html = new StringBuilder();

        html.append("<h2>Emploi du Temps</h2>");
        html.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>");
        html.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>Date</th>")
                .append("<th>Heure Début</th>")
                .append("<th>Heure Fin</th>")
                .append("<th>Salle</th>")
                .append("<th>Module</th>")
                .append("<th>Enseignant</th>")

                .append("</tr>");

        for (Seance s : seances) {
            html.append("<tr>")
                    .append("<td>").append(s.getId()).append("</td>")
                    .append("<td>").append(s.getDate()).append("</td>")
                    .append("<td>").append(s.getHeureDebut()).append("</td>")
                    .append("<td>").append(s.getHeureFin()).append("</td>")
                    .append("<td>").append(s.getSalleId()).append("</td>")
                    .append("<td>").append(s.getModule()).append("</td>")
                    .append("<td>").append(s.getEnseignant()).append("</td>")

                    .append("</tr>");
        }

        html.append("</table>");
        html.append("<br><p>Cordialement,<br>Votre Administration</p>");

        return html.toString();
    }

    // Send to mail directly



    public static void sendEmail(String toEmail, String subject, String htmlContent) {
        final String fromEmail = "hammami.chaima1998@gmail.com";
        final String password = "ottqhanzrkykhcmk"; // mot de passe d'application

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("hammami.chaima1998@gmail.com", "ottqhanzrkykhcmk");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");

            Transport.send(message);

            System.out.println("Mail envoyé avec succès à " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
