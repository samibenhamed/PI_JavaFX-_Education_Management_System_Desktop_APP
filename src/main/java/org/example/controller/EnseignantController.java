package org.example.controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.Model.Enseignant;

public class EnseignantController {

    private final ObservableList<Enseignant> enseignants;

    public EnseignantController() {
        // Initialisation de la liste des enseignants (peut être alimentée depuis une BDD plus tard)
        enseignants = FXCollections.observableArrayList();
    }

    // 🔁 Récupérer tous les enseignants
    public ObservableList<Enseignant> getEnseignants() {
        return enseignants;
    }

    // ➕ Ajouter un enseignant
    public void ajouterEnseignant(Enseignant enseignant) {
        enseignants.add(enseignant);
    }

    // 🔄 Modifier un enseignant
    public void modifierEnseignant(int index, Enseignant enseignant) {
        if (index >= 0 && index < enseignants.size()) {
            enseignants.set(index, enseignant);
        }
    }

    // ❌ Supprimer un enseignant
    public void supprimerEnseignant(int index) {
        if (index >= 0 && index < enseignants.size()) {
            enseignants.remove(index);
        }
    }

    // 🔍 Trouver un enseignant par ID (utile si tu rajoutes une base de données plus tard)
    public Enseignant getEnseignantById(int id) {
        for (Enseignant e : enseignants) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
}
