package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


/**
 * Classe che rappresenta un evento di Blackout letto dal database
 * @author carlo
 *
 */
public class PowerOutage implements Comparable<PowerOutage>{	
	int id;							// id dell'evento
	Nerc nerc;						// nerc
	int customers_affected;			// numero di clienti affetti dall'evento
	LocalDateTime start_time;		// data di inizio dell'evento
	LocalDateTime end_time;			// data di fine dell'evento
	private long durationInMinutes;	// durata in minuti
	private int yearStart;			// anno di inizio dell'evento
	private int yearEnd;			// anno di fine dell'evento
	
	
	
	public PowerOutage(int id, Nerc nerc, int customers_affected, LocalDateTime start_time, LocalDateTime end_time) {
		super();
		this.id = id;
		this.customers_affected = customers_affected;
		this.start_time = start_time;
		this.end_time = end_time;
		// Nota bene: l'esercizio richiede che la durata totale degli eventi, in ore, sia al di sotto di un valore indicato
		// Però, si sono eventi che durano meno di un'ora, e quindi verrebbero troncati a zero se si leggesse la loro durata in ore.
		// Per questo esercizio va ben uguale, però una soluzione migliore è leggere la durata in minuti e poi dividere per 60 per avere la 
		// durata in ore, non troncata.
		this.durationInMinutes = start_time.until(end_time, ChronoUnit.MINUTES);
		this.yearStart = start_time.getYear();
		this.yearEnd = end_time.getYear();
	}
	
	
	public int getId() {
		return this.id;
	}
	
	
	public Nerc getNerc() {
		return this.nerc;
	}
	
	
	public int getYearStart() {
		return this.yearStart;
	}
	
	
	public int getYearEnd() {
		return this.yearEnd;
	}
	
	
	public long getDurationMinutes() {
		return this.durationInMinutes;
	}
	
	
	public int getCustomersAffected() {
		return this.customers_affected;
	}


	public LocalDateTime getStart_time() {
		return start_time;
	}


	public LocalDateTime getEnd_time() {
		return end_time;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerOutage other = (PowerOutage) obj;
		return id == other.id;
	}


	@Override
	public String toString() {
		return "PowerOutage [id=" + id + ", nerc=" + nerc + ", customers_affected=" + customers_affected
				+ ", start_time=" + start_time + ", end_time=" + end_time + ", durationInMinutes=" + durationInMinutes
				+ ", yearStart=" + yearStart + ", yearEnd=" + yearEnd + "]";
	}


	@Override
	public int compareTo(PowerOutage o) {
		return this.start_time.compareTo(o.start_time);
	}
	
	
	

}
