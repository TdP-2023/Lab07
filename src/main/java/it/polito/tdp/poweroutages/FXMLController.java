/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import it.polito.tdp.poweroutages.model.WorstCaseAnalysis;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	try {
			Nerc selectedNerc = cmbNerc.getSelectionModel().getSelectedItem();
			if (selectedNerc == null) {
				txtResult.setText("Select a NERC (area identifier)");
				return;
			}

			int maxAnni = Integer.parseInt(txtYears.getText());
			if (maxAnni <= 0) {
				txtResult.setText("Select a number of years greater than 0");
				return;
			}

			int maxOre = Integer.parseInt(txtHours.getText());
			if (maxOre <= 0) {
				txtResult.setText("Select a number of hours greater than 0");
				return;
			}

			txtResult.setText(
					String.format("Computing the worst case analysis... for %d hours and %d years", maxOre, maxAnni));
			WorstCaseAnalysis worstCase = model.getWorstCasev2(selectedNerc, maxAnni, maxOre );

			txtResult.clear();
			
			if (worstCase==null) {
				txtResult.setText("Non ci sono disservizi nel Nerc selezionato.");
			}else {
				txtResult.appendText("Tot people affected: " + worstCase.getCustomersAffected() + "\n");
				txtResult.appendText(String.format("Tot hours of outage: %4.2f\n", worstCase.getOreDisservizio()));
				for (PowerOutage e : worstCase.getEventi()) {
					txtResult.appendText(String.format("Id: %5d\t Anno: %d\t Data Inizio: %s\t"
							+ "Data Fine: %s\t Durata (ore): %3.2f\t Customers: %d\n", 
							e.getId(), e.getYearStart(), e.getStart_time(), e.getEnd_time(), e.getDurationMinutes()/60.0, e.getCustomersAffected()) );
				}
			}
		} catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}
    }
    
    
    
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbNerc.getItems().addAll(this.model.getNercList());
    }
}
