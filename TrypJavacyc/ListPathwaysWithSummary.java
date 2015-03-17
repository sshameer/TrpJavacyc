import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsebionet.projects.trypanocyc.GetStats;


public class ListPathwaysWithSummary {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Javacyc cyc = new Javacyc("TRYPANO");
		GetStats GS = new GetStats();
		updateCompartmentFromComments uCFC = new  updateCompartmentFromComments();
		
		
		ArrayList<String> Pathlist = cyc.getClassAllInstances("|Pathways|");
		Pathlist=GS.refinePathList(Pathlist);
		
		int i=0;
		for(String m:Pathlist){
			
			if(!cyc.getSlotValue(m, "COMMENT").equals("NIL")){
				i++;
				System.out.println(i+") "+m+"----"+cyc.getSlotValue(m,"COMMON-NAME"));
			}
			
		}
		
		ArrayList<String> Rxnlist = cyc.getClassAllInstances("|Reactions|");
		Rxnlist=GS.refineList(Rxnlist);
		
		int j=0;
		int k=0;
		for(String n:Rxnlist){
			System.out.println(n);
			
			if(n.equals("GCVMULTI-RXN")||n.equals("IMP-DEHYDROG-RXN")||n.equals("THYMIDYLATESYN-RXN")||n.equals("AMPSYN-RXN")||n.equals("DIHYDROFOLATEREDUCT-RXN")||n.equals("GLUTAMATE-DEHYDROGENASE-NADP+-RXN")||n.equals("GLYCINE-DEHYDROGENASE-RXN")||n.equals("ADENPRIBOSYLTRAN-RXN")||n.equals("GCVT-RXN")){
				continue;
			}
			
			HashMap<String,ArrayList<String>> table = uCFC.getAnnotationTable(cyc, n);
			
			if(table==null||table.keySet().size()<2){
				continue;																		//skip reaction is the summary section is empty;
			}
			
			HashMap<String,List<String>> annotation = uCFC.getAnnotation(table);
			
			
				
				System.out.println(annotation.get("Annotator"));
				k=k+annotation.get("Annotator").size();
				j++;
				System.out.println(j+") "+n+"----"+cyc.getSlotValue(n,"COMMON-NAME")+" "+annotation.get("Annotator").size()+" annotations");
			
			//if(k>200){
			//	System.exit(0);
			//}
			
		}
		
		System.out.println(i+" Pathway summaries and "+j+" reaction's with comments ("+k+" annotations)");
	}

}
