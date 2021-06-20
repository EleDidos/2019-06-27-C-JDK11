/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<DefaultWeightedEdge> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    SimpleWeightedGraph< String , DefaultWeightedEdge>graph;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String cat;
    	LocalDate ld;
    	try {
    		cat= boxCategoria.getValue();
    		ld=boxGiorno.getValue();
    				
    	}catch(NullPointerException npe) {
    		txtResult.setText("Scegli una categorie e un giorno");
    		return;
    	}
    	
    	model.creaGrafo(cat,ld);
    	txtResult.appendText("Caratteristiche del grafo:\n#VERTICI = "+model.getNVertici()+"\n#ARCHI = "+model.getNArchi());
    	
    	txtResult.appendText("\n\nGli archi con il peso inferiore a quello medio sono:\n");
    	List <DefaultWeightedEdge> archiMIN = model.getArchiMIN();
    	graph=this.model.getGraph();
    	for(DefaultWeightedEdge e: archiMIN)
    		txtResult.appendText(graph.getEdgeSource(e)+" - "+graph.getEdgeTarget(e)+" ( "+graph.getEdgeWeight(e)+" )\n");
    	
    	boxArco.getItems().addAll(archiMIN);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	if(model.getGraph()==null)
    		txtResult.setText("Crea prima il grafo!");
    	
    	String partenza;
    	String arrivo;
    	try {
    		DefaultWeightedEdge e = boxArco.getValue();
    		partenza=graph.getEdgeSource(e);
    		arrivo=graph.getEdgeTarget(e);
    				
    	}catch(NullPointerException npe) {
    		txtResult.setText("Scegli un arco");
    		return;
    	}
    	
    	txtResult.appendText("\n\nIl cammino con il peso massimo da: "+partenza +" a "+arrivo+" è:\n");
    	for(String s: model.trovaPercorso(partenza, arrivo))
    		txtResult.appendText(s+"\n ");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(model.getCategorie());
    	boxGiorno.getItems().addAll(model.getGiorni());
    }
}
