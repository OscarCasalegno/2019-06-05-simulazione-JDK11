/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.DistrictLength;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxAnno"
	private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxMese"
	private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

	@FXML // fx:id="boxGiorno"
	private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaReteCittadina"
	private Button btnCreaReteCittadina; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="txtN"
	private TextField txtN; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCreaReteCittadina(ActionEvent event) {
		this.txtResult.clear();
		Integer year = this.boxAnno.getValue();
		if (year == null) {
			this.txtResult.appendText("Scegliere un anno");
			return;
		}

		this.model.createGraph(year);

		Map<Integer, List<DistrictLength>> mappa = this.model.getConnections();

		for (Integer i : mappa.keySet()) {
			this.txtResult.appendText("Distretto " + i + ": \n");
			for (DistrictLength d : mappa.get(i)) {
				this.txtResult.appendText(d.toString() + "\n");
			}
			this.txtResult.appendText("\n");
		}

		this.boxMese.getItems().addAll(this.model.getMesi(year));
		this.boxGiorno.getItems().addAll(this.model.getGiorni(year));

	}

	@FXML
	void doSimula(ActionEvent event) {
		this.txtResult.clear();

		Integer anno = this.boxAnno.getValue();
		Integer mese = this.boxMese.getValue();
		Integer giorno = this.boxGiorno.getValue();
		Integer agenti;

		try {
			agenti = Integer.parseInt(this.txtN.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("scrivi un numero da 1 a 10");
			return;
		}

		if (agenti < 1 || agenti > 10) {
			this.txtResult.appendText("scrivi un numero da 1 a 10");
			return;
		}

		if (anno == null || mese == null || giorno == null) {
			this.txtResult.appendText("scegli giorno, mese ed anno");
			return;
		}

		if (this.model.simula(giorno, mese, anno, agenti)) {
			this.txtResult.appendText(
					"Numero di crimini mal gestiti con " + agenti + " agenti: " + this.model.getMalGestiti());
		} else {
			this.txtResult.appendText("Nessun reato quel giorno");
		}

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getAnni());
	}
}
