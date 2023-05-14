package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	private PowerOutageDAO podao;
	
	// soluzione trovata nella ricorsione. Qui uso la classe di appoggio `WorstCaseAnalysis`
	// per restituire diverse cose insieme (lista di eventi, numero di clienti, numero di ore totali di disservizio
	// Alternativamente, avrei potuto creare diverse variabili senza usare una classe extra
	private WorstCaseAnalysis soluzione;	
	
	
	public Model() {
		podao = new PowerOutageDAO();
		soluzione = new WorstCaseAnalysis();
	}
	
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public Nerc getNercByID(int id) {
		return podao.getNercByID(id);
	}
	
	public WorstCaseAnalysis getAnalysisResult() {
		return soluzione;
	}
	
	
	/**
	 * Questo è il metodo che effettua la ricerca della sequenza di eventi richiesta.
	 * Qui prima facciamo un po' di cose preparatorie (leggere la lista di eventi dal dao,
	 * inizializzare la soluzione) e poi invochiamo la ricorsione vera e propria.
	 * 
	 * @param n il Nerc selezionato
	 * @param max_anni il rane massimo di anni da considerare per l'analisi
	 * @param max_ore il massimo numero di ore di disservizio
	 * @return
	 */
	public WorstCaseAnalysis getWorstCase(Nerc n, int max_anni, int max_ore){
		//leggi la lista di tutti gli eventi nel Nerc selezionato
		List<PowerOutage> eventi = this.podao.getPowerOutagesByNerc(n);
		if (eventi.isEmpty()) {
			return null;
		}
		
		//resetta soluzione
		this.soluzione.setCustomersAffected(0);
		this.soluzione.setOreDisservizio(0);
		soluzione.setEventi( new ArrayList<PowerOutage>() );
		
		//avvia la ricorsione
		ricorsione(new ArrayList<PowerOutage>(), eventi, max_anni, max_ore);
		
		//ritorna il risultato
		return this.soluzione;
	}

	
	/**
	 * Questa è la vera funzione ricorsiva.
	 * @param parziale
	 * @param eventi
	 * @param max_anni
	 * @param max_ore
	 */
	public void ricorsione(List<PowerOutage> parziale, List<PowerOutage> eventi, int max_anni, int max_ore) {
		//Condizione di Terminazione
		if (this.getDurataOreTotaleEventi(parziale) > max_ore || eventi.isEmpty()) {
			return;
		}
		
		// se non siamo nella condizione ricorsiva andiamo avanti
		for (PowerOutage e : eventi) {
			// se l'evento non è stato ancora considerato, lo includo e provo a vedere se va bene
			if (!parziale.contains(e)) {
				parziale.add(e);
				//Faccio il sort di parziale perché per il metodo con cui ne calcolo il range di anni
				// dopo, presuppone che gli eventi nella lista siano ordinati per date crescenti.
				Collections.sort(parziale); 
				
				// a questo punto, verifico se l'evento aggiunto rispetta le condizioni. In caso affermativo
				// proseguo controllo se é una soluzione migliore di quella che ho e poi proseguo nella ricorsione.
				// altrimenti passo direttamente al backtracking ed esploro un'altro ramo della ricorsione.
				if ( this.getRangeAnni(parziale)<=max_anni &&  this.getDurataOreTotaleEventi(parziale) <= max_ore){
					
					//check se parziale è migliore della mia soluzione attuale
					int parzialeCustomers = this.countCustomersAffected(parziale);
					if (parzialeCustomers > this.soluzione.getCustomersAffected()){
						this.soluzione.setCustomersAffected( parzialeCustomers );
						this.soluzione.setOreDisservizio(this.getDurataOreTotaleEventi(parziale));
						this.soluzione.setEventi( new ArrayList<PowerOutage>(parziale) );
					}
					// Quindi vado avanti nella ricorsione
					ricorsione(parziale, eventi, max_anni, max_ore);			
				}
				
				//backtracking
				parziale.remove(e);
			}
		}
	}
	
	
	
	
	/**
	 * Questo è il secondo che effettua la ricerca della sequenza di eventi richiesta.
	 * Rispetto al primo metodo, è stata inserita qualche ottimizzazione. Principalmente,
	 * l'idea di fondo è che nella ricorsione, non ciclo ogni volta su tutti glie eventi,
	 * ma vado a pescare solo quelli che ancora non ho scorto nella lista. Quindi, il ciclo for lo faccio 
	 * per indici.
	 * Per vedere la differenza potete eseguire il file TestModel.
	 * 
	 * @param n il Nerc selezionato
	 * @param max_anni il rane massimo di anni da considerare per l'analisi
	 * @param max_ore il massimo numero di ore di disservizio
	 * @return
	 */
	public WorstCaseAnalysis getWorstCasev2(Nerc n, int max_anni, int max_ore){
		//leggi la lista di tutti gli eventi nel Nerc selezionato
		List<PowerOutage> eventi = this.podao.getPowerOutagesByNerc(n);
		if (eventi.isEmpty()) {
			return null;
		}
		
		//resetta soluzione
		this.soluzione.setCustomersAffected(0);
		this.soluzione.setOreDisservizio(0);
		soluzione.setEventi( new ArrayList<PowerOutage>() );
		
		//avvia la ricorsione, aggiungendo il primo elemento alla soluzione
		for (PowerOutage e : eventi) {
			ricorsionev2(new ArrayList<PowerOutage>(), eventi, max_anni, max_ore);
		}
		
		//ritorna il risultato
		return this.soluzione;
	}

	
	/**
	 * Questa è la vera funzione ricorsiva.
	 * @param parziale
	 * @param eventi
	 * @param max_anni
	 * @param max_ore
	 */
	public void ricorsionev2(List<PowerOutage> parziale, List<PowerOutage> eventi, int max_anni, int max_ore) {
		//Condizione di Terminazione
		if (this.getDurataOreTotaleEventi(parziale) > max_ore || eventi.isEmpty()) {
			return;
		}
		
		// se non siamo nella condizione ricorsiva andiamo avanti
		for (PowerOutage e : eventi) {
			// se l'evento non è stato ancora considerato, lo includo e provo a vedere se va bene
			if ( (parziale.isEmpty()) || (e.compareTo(parziale.get(parziale.size()-1)) >0 ) ) {
				parziale.add(e);
				
				// a questo punto, verifico se l'evento aggiunto rispetta le condizioni. In caso affermativo
				// proseguo controllo se é una soluzione migliore di quella che ho e poi proseguo nella ricorsione.
				// altrimenti passo direttamente al backtracking ed esploro un'altro ramo della ricorsione.
				if ( this.getRangeAnni(parziale)<=max_anni &&  this.getDurataOreTotaleEventi(parziale) <= max_ore){
					
					//check se parziale è migliore della mia soluzione attuale
					int parzialeCustomers = this.countCustomersAffected(parziale);
					if (parzialeCustomers > this.soluzione.getCustomersAffected()){
						this.soluzione.setCustomersAffected( parzialeCustomers );
						this.soluzione.setOreDisservizio(this.getDurataOreTotaleEventi(parziale));
						this.soluzione.setEventi( new ArrayList<PowerOutage>(parziale) );
					}
					// Quindi vado avanti nella ricorsione
					ricorsionev2(parziale, eventi, max_anni, max_ore);			
				}
				
				//backtracking
				parziale.remove(e);
			}
		}
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////
	////////////////////   HELPER FUNCTIONS  /////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	/**
	 * Funzione di aiuto per calcolare la durata totale (in ore) degli eventi di Blackout in una lista
	 * @param eventi
	 * @return
	 */
	public double getDurataOreTotaleEventi(List<PowerOutage> eventi) {
		long res = 0;
		for (PowerOutage e : eventi) {
			res += e.getDurationMinutes();
		}
		return res/60.0;
	}
	
	
	/**
	 * Funzione di aiuto per calcolare il range (in anni) degli eventi di Blackout in una lista.
	 * NOTA BENE: questo metodo presuppone che gli eventi nella lista siano ordinati per 
	 * data crescente!!!
	 * @param eventi
	 * @return
	 */
	public int getRangeAnni(List<PowerOutage> parziale) {
		int res = 0;
		if (parziale.size()>1) {
			int yMin = parziale.get(0).getYearStart();
			int yMax = parziale.get(parziale.size()-1).getYearEnd();
			res = yMax - yMin;
		}
		return res;
	}
	
	
	/**
	 * Funzione di aiuto che restituisce il numero totale di clienti condizionati 
	 * dagli eventi di blackout in una lista
	 * @param eventi
	 * @return
	 */
	public int countCustomersAffected(List<PowerOutage> eventi) {
		int res = 0;
		for (PowerOutage e : eventi) {
			res += e.getCustomersAffected();
		}
		return res;
	}
	
}
