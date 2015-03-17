import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import parsebionet.projects.trypanocyc.CompareStatLists;
import parsebionet.projects.trypanocyc.GetStats;


public class tempJob {
	public static void main(String[] args){
		
		Javacyc cyc = new Javacyc("TRYPANO");
		GetStats GS = new GetStats();
		CompareStatLists CSL = new CompareStatLists();
		
		ArrayList<String> rxnlist = cyc.getClassAllInstances("|Reactions|");
		rxnlist = GS.refineList(rxnlist);
		File inputfile = new File("/home/leo/hypoReactioninOldDB");
		
		
		HashMap<String, String> input = CSL.getListFromFile( inputfile );
		ArrayList<String> output = new ArrayList<String>();
		
		
		int i =0;
		
		for(String rxn:input.keySet()){
			if(rxnlist.contains(rxn)){
				if(!cyc.getSlotValue(rxn, "ENZYMATIC-REACTION").equals("NIL")){
					System.out.println(rxn);
				}
			}
		}
		
		//GS.makeList("/home/leo/hypoReactioninNewDB", output);
		
	}
}
