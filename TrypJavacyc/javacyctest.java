import java.util.ArrayList;




public class javacyctest{
	public static void main(String[] arg){
		Javacyc cyc= new Javacyc("TRYPANO");
		ArrayList<String> rxnlist = cyc.getClassAllInstances("|Reactions|");
		
		System.out.println(rxnlist);
	}
}