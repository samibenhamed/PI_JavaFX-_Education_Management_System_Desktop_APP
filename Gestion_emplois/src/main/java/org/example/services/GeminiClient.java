package org.example.services;

import org.example.Model.Seance;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeminiClient {
    private static final String API_KEY = "AIzaSyCr_BeXQ6NstCNPe0cWPFZjDDovmB5kxjI";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY;



    public static String generateEmailText(String enseignantNom, List<Seance> seances) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("ne commence par le mail par '```html' ")
                .append("Commence le mail par Objet")
                .append("Rédige un email professionnel et bienveillant en français à destination d’un enseignant nommé ")
                .append(enseignantNom)
                .append(". Le message doit être structuré en HTML en respectant les consignes suivantes :<br>")
                .append("• Commencer par une salutation personnalisée (Bonjour [Nom]), suivie d’un saut de ligne (<br>)<br>")
                .append("• Résumer l’objet de l’email (confirmation d’emploi du temps)<br>")
                .append("• Présenter les détails de l’emploi du temps dans un tableau HTML clair<br>")
                .append("• Terminer par une formule de politesse :<br><br>")
                .append("Cordialement,<br>")
                .append("Votre administration<br><br>")
                .append("Voici les détails de son emploi du temps à insérer dans le tableau :<br><br>");

        prompt.append("<table border='1' cellspacing='0' cellpadding='6'>")
                .append("<tr><th>Date</th><th>Heure</th><th>Module</th><th>Salle</th></tr>");

        for (Seance s : seances) {
            prompt.append("<tr>")
                    .append("<td>").append(s.getDate()).append("</td>")
                    .append("<td>").append(s.getHeureDebut()).append(" - ").append(s.getHeureFin()).append("</td>")
                    .append("<td>").append(s.getModule()).append("</td>")
                    .append("<td>").append(s.getSalleId()).append("</td>")
                    .append("</tr>");
        }

        prompt.append("</table>");



        String jsonPayload = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", prompt.toString()))
                                )
                        )
                ).toString();



        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return extractTextFromGeminiResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération de l'email avec Gemini.";
        }
    }


    private static String extractTextFromGeminiResponse(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray candidates = obj.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                return parts.getJSONObject(0).getString("text");
            } else {
                return "Aucune réponse générée.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur d'extraction du texte Gemini.";
        }
    }
}

