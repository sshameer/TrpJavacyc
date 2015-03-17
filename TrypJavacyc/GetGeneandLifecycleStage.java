import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import parsebionet.biodata.BioChemicalReaction;
import parsebionet.biodata.BioNetwork;
import parsebionet.io.Sbml2Bionetwork;
import parsebionet.projects.trypanocyc.GenerateUpdateReports;
import parsebionet.projects.trypanocyc.GetStats;

/**
 * 
 * @author sshameer
 *
 */
public class GetGeneandLifecycleStage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetGeneandLifecycleStage GGLC = new GetGeneandLifecycleStage();
		Javacyc cyc = new Javacyc("TRYPANO");
		Sbml2Bionetwork S2B = new Sbml2Bionetwork("/home/leo/trypanocyc_08072014.xml",false);
		updateCompartmentFromComments uCFC = new updateCompartmentFromComments();
				
		BioNetwork bionet = S2B.getBioNetwork();
		
		HashMap<String,BioChemicalReaction> rxnlist = bionet.getBiochemicalReactionList();
		for(String m:rxnlist.keySet()){
			System.out.println(m);
			String comment = cyc.getSlotValue(m, "COMMENT");
			HashMap<String,ArrayList<String>> table = uCFC.getAnnotationTable(cyc, m);
			HashMap<String, List<String>> annotation = uCFC.getAnnotation(table);
			Set<String> stage = GGLC.CommentStage(annotation);
			System.out.println(stage);
			String Note = rxnlist.get(m).getSbmlNote();
			Note = Note+"\n";
			rxnlist.get(m).setSbmlNote(Note);
		}
		
		
	}
	
	public Set<String> CommentStage(HashMap<String, List<String>> annotation){
		Set<String> stage = new HashSet<String>();
		
		Set<String> allstage = new HashSet<String>(){{
			add("Procyclic"); add("Slender"); add("Stumpy"); add("Metacyclic");
		}};	
		
		for(String m:allstage){
			//System.out.println(m);
			int tag0=0;
			for(String n:annotation.get(m)){
				//System.out.println(n);
				if(n.contains("Yes")){
					tag0++;
				}else if(n.contains("No")){
					tag0--;
				}
			}
			//System.out.println(tag0);
			if(tag0 == annotation.get(m).size()){
				stage.add(m);
			}
		}
				
		return stage;
	}
	
	public Set<String> CommentComp(HashMap<String, List<String>> annotation){
		Set<String> comp = new HashSet<String>();
		
		Set<String> allcomp = new HashSet<String>(){{
			add("Cytosol"); add("Mitochondria"); add("Glycosome"); add("Reticulum"); add("Lysosome"); add("Acidocalcisome"); add("Nucleus"); add("Flagellum"); add("Extracellular");
		}};	
		
		for(String m:allcomp){
			//System.out.println(m);
			int tag0=0;
			for(String n:annotation.get(m)){
				//System.out.println(n);
				if(n.contains("Yes")){
					tag0++;
				}else if(n.contains("No")){
					tag0--;
				}
			}
			//System.out.println(tag0);
			if(tag0 == annotation.get(m).size()){
				comp.add(m);
			}
		}
				
		return comp;
	}

}
