import java.util.ArrayList;

import parsebionet.projects.trypanocyc.GetStats;

/**
 * This script can be used to add Links from PGDB pathways and/or metabolites to Metxplore
 * @author sshameer
 *
 */
public class AddMetexploreLinksinPGDB {
	
	private String org;
	private int pwyflag=0;
	private int cpdflag=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AddMetexploreLinksinPGDB AMLP = new AddMetexploreLinksinPGDB();
		
		for(int i=0;i<args.length;i++){
			if(args[i].equals("-org")){
				i++;
				AMLP.org = args[i];
			}else if(args[i].equals("-pwy")){
				AMLP.pwyflag = 1;
			}else if(args[i].equals("-cpd")){
				AMLP.cpdflag = 1;
			}else{
				System.out.println("Check syntax");
			}
		}
		
		if(AMLP.pwyflag == 0 && AMLP.cpdflag == 0){
			System.err.println("Select atleast one of the these: Metabolites(-cpd) and Pathways(-pwy)");
		}
		
		Javacyc cyc = new Javacyc(AMLP.org);
		GetStats GS = new GetStats();
		
		if(AMLP.pwyflag == 1){
			ArrayList<String> pwylist = cyc.getClassAllInstances("|Pathways|");
			pwylist = GS.refinePathList(pwylist);
			for(String pwy : pwylist){
				//ArrayList<String> rxnlist = cyc.getSlotValues(pwy, "REACTION-LIST");
				//rxnlist = GS.refineList(rxnlist);
				//String ID = "";
				//for(String rxn:rxnlist){
				//	ArrayList<String> complist = cyc.getSlotValues(rxn, "RXN-LOCATIONS");
				//	
				//	if(complist.size()>1){
				//		for(int i=0;i<complist.size();i++){
				//			if(ID == ""){
				//				ID = rxn+"_IN_"+complist.get(i);
				//			}else{
				//				ID = ID+"__+__"+rxn+"_IN_"+complist.get(i);
				//			}
				//		}
				//	}else{
				//	
				//		if(ID == ""){
				//			ID = rxn;
				//		}else{
				//			ID = ID+"__+__"+rxn;
				//		}
				//	}
				//}
				//System.out.println(pwy+"---"+ID);
				if(!cyc.getSlotValue(pwy, "DBLINKS").equals("NIL")){
					cyc.removeSlotValues(pwy, "DBLINKS");
					System.out.println("removed");
				}				
				
				cyc.addSlotValue(pwy,"DBLINKS","(METEXPLORE \""+pwy+"\" NIL |sshameer| NIL NIL NIL)");
			}
		}
		
		
		if(AMLP.cpdflag == 1){
			ArrayList<String> cpdlist = cyc.getClassAllInstances("|Compounds|");
			cpdlist = GS.refineList(cpdlist);
			for(String cpd : cpdlist){
				ArrayList<String> rxnlistL = cyc.getSlotValues(cpd, "APPEARS-IN-LEFT-SIDE-OF");
				ArrayList<String> rxnlistR = cyc.getSlotValues(cpd, "APPEARS-IN-RIGHT-SIDE-OF");
				
				rxnlistL = GS.refineList(rxnlistL);
				rxnlistR = GS.refineList(rxnlistR);
				
				String ID = "";
				for(String rxn:rxnlistL){
					
					ArrayList<String> complist = cyc.getSlotValues(rxn, "RXN-LOCATIONS");
					
					if(complist.size()>1){
						for(int i=0;i<complist.size();i++){
							if(ID == ""){
								ID = rxn+"_IN_"+complist.get(i);
							}else{
								ID = ID+"__+__"+rxn+"_IN_"+complist.get(i);
							}
						}
					}else{
					
						if(ID == ""){
							ID = rxn;
						}else{
							ID = ID+"__+__"+rxn;
						}
					}
				}
				
				for(String rxn:rxnlistR){
					
					ArrayList<String> complist = cyc.getSlotValues(rxn, "RXN-LOCATIONS");
					
					if(complist.size()>1){
						for(int i=0;i<complist.size();i++){
							if(ID == ""){
								ID = rxn+"_IN_"+complist.get(i);
							}else{
								ID = ID+"__+__"+rxn+"_IN_"+complist.get(i);
							}
						}
					}else{
						if(ID == ""){
							ID = rxn;
						}else{
							ID = ID+"__+__"+rxn;
						}
					}
				}
				System.out.println(cpd+"---"+ID);
				cyc.addSlotValue(cpd,"DBLINKS","(METEXPLORE \""+ID+"\" NIL |sshameer| NIL NIL NIL)");
			}
		}
		
	}
}
