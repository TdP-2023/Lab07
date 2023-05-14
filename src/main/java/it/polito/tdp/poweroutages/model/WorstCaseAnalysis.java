package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe di appoggio per ritornare il risultato della ricerca insieme al numero di customers affected e
 * alle ore totali di disservizio.  
 * @author carlo
 */
public class WorstCaseAnalysis {
	
	private List<PowerOutage> eventi;
	private int customersAffected;
	private double oreDisservizio;
	
	
	public WorstCaseAnalysis() {
		super();
		this.eventi = new ArrayList<PowerOutage>();
		this.customersAffected = 0;
		this.oreDisservizio = 0;
	}
	
	
	public WorstCaseAnalysis(List<PowerOutage> eventi, int customersAffected, int oreDisservizio) {
		super();
		this.eventi = eventi;
		this.customersAffected = customersAffected;
		this.oreDisservizio = oreDisservizio;
	}


	public List<PowerOutage> getEventi() {
		return eventi;
	}


	public int getCustomersAffected() {
		return customersAffected;
	}


	public void setEventi(List<PowerOutage> eventi) {
		this.eventi = eventi;
	}


	public void setCustomersAffected(int customersAffected) {
		this.customersAffected = customersAffected;
	}


	public double getOreDisservizio() {
		return oreDisservizio;
	}


	public void setOreDisservizio(double oreDisservizio) {
		this.oreDisservizio = oreDisservizio;
	}
	
	
	
	

}
