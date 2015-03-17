
public class retrieveKeggIDs {
	public static void main(String[] args){
		
		Javacyc cyc = new Javacyc("META");
		
		System.out.println(cyc.getClassAllInstances("|Compounds|"));
		
	}
	
}
