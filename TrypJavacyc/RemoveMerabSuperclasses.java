import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsebionet.biodata.BioChemicalReaction;
import parsebionet.biodata.BioNetwork;
import parsebionet.biodata.BioPhysicalEntity;
import parsebionet.biodata.BioPhysicalEntityParticipant;
import parsebionet.io.Sbml2Bionetwork;


public class RemoveMerabSuperclasses {

	private HashMap<String,String> metab = new HashMap<String,String>();
	
	
	public static void main(String[] args) {
		
		String f1 = args[0];
		String f2 = args[1];
		
		RemoveMerabSuperclasses RMS = new RemoveMerabSuperclasses();
				
		getGeneList GL = new getGeneList();
		List<String> SBML = GL.readReport(f1);
			
		
		RMS.makeMetabTable(f2);
		
		Sbml2Bionetwork S2B = new Sbml2Bionetwork(f1);
		BioNetwork BioNet = S2B.getBioNetwork();
		
		HashMap<String,BioChemicalReaction> rxnlist = BioNet.getBiochemicalReactionList();
		
		int numbering = 0;			
		
		
		
		
		for(String rxnid: rxnlist.keySet()){
			HashMap<String,BioPhysicalEntity> metabolites = rxnlist.get(rxnid).getListOfProducts();
			for(String metaboliteId:metabolites.keySet()){
			if(RMS.metab.containsValue(metaboliteId)){
					System.out.println(metaboliteId);
					for(String metabkey: RMS.metab.keySet()){
						BioChemicalReaction rxn = rxnlist.get(rxnid);
						if(RMS.metab.get(metabkey).equals(metaboliteId)){
							BioChemicalReaction rxncpy = rxn;
							BioPhysicalEntity BPE = rxncpy.getListOfProducts().get(metaboliteId);
							BioPhysicalEntityParticipant BPEP = rxncpy.getRightParticipantList().get(rxnid+"__With__"+metaboliteId);
							BPE.setName(metabkey);
							BPEP.setPhysicalEntity(BPE);
							BPEP.setId(BPE.getName());
							BPEP.setName(BPE.getName());
							System.out.println(BPEP.getId());
							//if(numbering>0){
								rxncpy.setId(rxn.getId()+"_"+numbering);
								rxncpy.setName(rxn.getId()+"-"+metabkey);
							//}
							numbering++;
							System.out.println(rxncpy.getId());
							BioNet.removeBioChemicalReaction(rxn.getId());
							//if(BioNet.getBiochemicalReactionList().keySet().contains(rxn.getId())){
							//	System.out.println("Remove reaction failed");
							//}
							BioNet.addBiochemicalReaction(rxncpy);
							System.out.println(BioNet.getBiochemicalReactionList().get(rxncpy.getId()).getName());
							System.out.println(BioNet.getBiochemicalReactionList().get(rxn.getId()).getName());
							//System.out.println(rxncpy.getRightParticipantList());
							//System.out.println(BPEP.getName());
							System.exit(0);
						}
					}
				}
			}
			
		}
	
		
		
		
	}
	
	public void makeMetabTable(String f2){
		getGeneList GL = new getGeneList();
		List<String> metabolites = GL.readReport(f2);
		String temp="";
		
		int flag = 0;
		for(String n:metabolites){
			
			if(n.contains("UNIQUE-ID - ")){
				flag = 1;
				//System.out.println(n);
				//System.out.print(n.split("ID -")[1]);
				temp = n.split("ID - ")[1];
				continue;
			}
			
			if(flag == 1){
				flag = 0;
				this.metab.put(temp,n.split("S - ")[1]);
				//System.out.println("\t"+n.split("S - ")[1]);
			}
			
		}
		
		for(String o:this.metab.keySet()){
			//System.out.println(o+"\t"+metab.get(o));
		}
	}

}
