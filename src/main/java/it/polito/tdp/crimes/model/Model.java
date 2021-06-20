package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private SimpleWeightedGraph< String , DefaultWeightedEdge>graph;
	private List <String> reati; //vertici

	private List <String> best;
	private double pesoMax=0.0;
	
	public Model(){
		dao= new EventsDao ();
	}

	public List <String> getCategorie() {
		List <String> categorie = dao.listAllCategories();
		Collections.sort(categorie);
		return categorie;
	}

	public List <LocalDate> getGiorni() {
		List <LocalDate> giorni = dao.listAllDays();
		Collections.sort(giorni);
		return giorni;
	}
	
	public void creaGrafo(String cat, LocalDate ld) {
		graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		reati=dao.listAllReati(cat,ld);
		
		Graphs.addAllVertices(graph, reati);
		
		for(Arco a : dao.listArchi(cat,ld))
			Graphs.addEdge(graph,a.getReato1(),a.getReato2(),a.getPeso());
		
		
	}
	
	public Integer getNVertici() {
		return graph.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return graph.edgeSet().size();
	}
	
	

	public SimpleWeightedGraph< String , DefaultWeightedEdge> getGraph() {
		return graph;
	}
	
	/**
	 * con peso minore di quello medio
	 * @return
	 */
	public List<DefaultWeightedEdge> getArchiMIN() {
		List <DefaultWeightedEdge> min=new ArrayList <DefaultWeightedEdge>();
		double medio=this.getPesoMedio();
		
		for(DefaultWeightedEdge e: graph.edgeSet())
			if(graph.getEdgeWeight(e)<medio){
				min.add(e);
			}
		return min;
	}
	
	public double getPesoMedio() {
		double tot=0.0;
		for(DefaultWeightedEdge e: graph.edgeSet())
			tot+=graph.getEdgeWeight(e);
		return tot/graph.edgeSet().size();
				
	}
	
	
	//lista di reati
	public List <String> trovaPercorso(String partenza, String arrivo){
		
		best=new ArrayList <String>();
		List <String> parziale = new ArrayList <String>();
		parziale.add(partenza);
		cerca(parziale,arrivo);
		return best;
	}
	
	private void cerca(List <String> parziale, String arrivo) {
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(best.size()==0 || calcolaPeso(parziale)>pesoMax) {
				best=new ArrayList <String> (parziale);
				pesoMax=calcolaPeso(parziale);
				return;
			}
		}
		
		for(String p: Graphs.neighborListOf(graph, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				cerca(parziale,arrivo);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	
	private Integer calcolaPeso(List <String> lista) {
		Integer peso=0;
		
		for(int i=0;i<lista.size()-1;i++) {
			String p1=lista.get(i);
			String p2=lista.get(i+1);
			DefaultWeightedEdge e=graph.getEdge(p1, p2);
			peso+=(int)graph.getEdgeWeight(e);
		}
		return peso;
	}
	
	
}
