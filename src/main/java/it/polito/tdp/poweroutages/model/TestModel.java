package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList() + "\n\n");
		
		
		final int oreMax = 200;
		final int anniMax = 4;
		final int nercID = 3;
		
		System.out.println("Analizziamo il seguente Nerc:");
		Nerc n = model.getNercByID(nercID);
		System.out.println("Nerc: " + n.getValue() + " \tID: " + n.getId());
		
		
		
		/*
		 * Qui dimostriamo la differenza di efficienza dei due metodi ricorsivi.
		 * La differenza è tanto più evidente quanti sono gli eventi nel nerc considerato. Quando ci sono 1 o 2 eventi,
		 * la differenza è praticamente nulla.
		 * Però, se testate il metodo sul nerc 11, 
		 */
		System.out.println("PRIMO METODO");
		long start = System.currentTimeMillis();
		WorstCaseAnalysis soluzione = model.getWorstCase(n, anniMax, oreMax);
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) / 1000F + " s \n");
		
		if (soluzione==null) {
			System.out.println("Non ci sono disservizi nel Nerc selezionato.");
		}else {
			System.out.println("Tot people affected: " + soluzione.getCustomersAffected());
			System.out.println(String.format("Tot hours of outage: %4.2f", soluzione.getOreDisservizio()));
			for (PowerOutage e : soluzione.getEventi()) {
				System.out.println(String.format("Id: %5d\t Anno: %d\t Data Inizio: %s\t"
						+ "Data Fine: %s\t Durata (ore): %3.2f\t Customers: %d", 
						e.getId(), e.getYearStart(), e.getStart_time(), e.getEnd_time(), e.getDurationMinutes()/60.0, e.getCustomersAffected()) );
			}
		}
		
		
		
		System.out.println("\n\n\nSECONDO METODO");
		long start2 = System.currentTimeMillis();
		WorstCaseAnalysis soluzione2 = model.getWorstCasev2(n, anniMax, oreMax);
		long end2 = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end2 - start2) / 1000F + " s \n");
		
		if (soluzione2==null) {
			System.out.println("Non ci sono disservizi nel Nerc selezionato.");
		}else {
			System.out.println("Tot people affected: " + soluzione2.getCustomersAffected());
			System.out.println(String.format("Tot hours of outage: %4.2f", soluzione2.getOreDisservizio()));
			for (PowerOutage e : soluzione2.getEventi()) {
				System.out.println(String.format("Id: %5d\t Anno: %d\t Data Inizio: %s\t"
						+ "Data Fine: %s\t Durata (ore): %3.2f\t Customers: %d", 
						e.getId(), e.getYearStart(), e.getStart_time(), e.getEnd_time(), e.getDurationMinutes()/60.0, e.getCustomersAffected()) );
			}
		}
		

		
		

	}

}
