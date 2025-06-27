package org.example.utils;

import javafx.scene.control.*;
import javafx.util.Callback;
import org.example.Model.Salle;

public class AvailabilityButtonFactory implements Callback<TableColumn<Salle, Boolean>, TableCell<Salle, Boolean>> {

    public AvailabilityButtonFactory() {}

    @Override
    public TableCell<Salle, Boolean> call(TableColumn<Salle, Boolean> param) {
        return new TableCell<>() {
            private final Button button = new Button();

            {
                button.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());
                    salle.setDisponible(!salle.isDisponible());
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Boolean disponible, boolean empty) {
                super.updateItem(disponible, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Salle salle = getTableView().getItems().get(getIndex());
                    button.setText(salle.isDisponible() ? "Oui" : "Non");
                    setGraphic(button);
                }
            }
        };
    }
}
