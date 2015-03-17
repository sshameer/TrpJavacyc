/**
 * This script can be used to populate notes sections of the SBML from PGDBs
 * Apr 16, 2014 
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



import parsebionet.biodata.BioGene;
import parsebionet.biodata.BioNetwork;
import parsebionet.biodata.BioPhysicalEntity;
import parsebionet.biodata.BioProtein;
import parsebionet.io.BioNetwork2SbmlFile;
import parsebionet.io.Sbml2Bionetwork;
import parsebionet.projects.trypanocyc.RemoveMetabSuperclasses;
 

/**
 * @author leo
 * Apr 16, 2014
 *
 */
public class PopulateGeneAssociation {
	private BioNetwork bionet = new BioNetwork();
	private HashMap<String,String> comMetDict = new HashMap<String,String>();
	private HashMap<String,String> comRxnDict = new HashMap<String,String>();
	
	public static void main(String[] ARGS){
		
		PopulateGeneAssociation PGA = new PopulateGeneAssociation();
		RemoveMetabSuperclasses RMS = new RemoveMetabSuperclasses();
		BioProtein BPE;
		BioGene BG;
		
		String Fin = "/home/leo/Dropbox/data/MERGEDFILEOUTminusCARDIOLIPIN.xml";
		String Cmet = "";
		String Crxn = "";
		String CycID = "TRYPANO";
		
		for(int i=0;i<ARGS.length;i++){
			if(ARGS[i].equals("-fin")){
				Fin = ARGS[i+1];
				i++;
			}else if(ARGS[i].equals("-cm")){
				Cmet = ARGS[i+1];
				i++;
			}else if(ARGS[i].equals("-cr")){
				Crxn = ARGS[i+1];
				i++;
			}else if(ARGS[i].equals("-cyc")){
				CycID = ARGS[i+1];
				i++;
			}
		}
		
		Javacyc cyc =new Javacyc("TRYPANO");
		System.out.println(cyc.getClassAllInstances("Reaction"));
		
		Sbml2Bionetwork S2B = new Sbml2Bionetwork(Fin);
		PGA.bionet = S2B.getBioNetwork();
		
		//System.out.println("SBML import successful for Model "+PGA.bionet.getId());
		
		if(!Cmet.equals("")){
			PGA.comMetDict = PGA.createIDdict(Cmet);
		}
		
		if(!Crxn.equals("")){
			PGA.comRxnDict = PGA.createIDdict(Cmet);
		}
		
		//System.out.println(cyc.getClassAllInstances("|Compounds|"));
		//System.exit(0);
/////////////////////////////////// TEST AREA ///////////////////////////////////		
//		ArrayList<String> Helper = cyc.getSlotValues("ENZRXN1V8-6214", "ENZYME");
//		for(String helper1: Helper){
//			System.out.println(helper1);
//		}
//		System.exit(0);
/////////////////////////////////////////////////////////////////////////////////		
		
		
		
		ArrayList<ArrayList<String>> tempArr;
		ArrayList<String> AllCycMet = cyc.getClassAllInstances("|Compounds|");
		int p= 0;
		for (String met : PGA.bionet.getPhysicalEntityList().keySet()){
			p++;
			String tempID;
			System.out.print(p+") "+met+"\t");
			if(!PGA.comMetDict.isEmpty() && PGA.comMetDict.containsKey(met)){
				tempID = PGA.comMetDict.get(met);
			}else{
				tempID=PGA.Dcompartment(met);
			}
			System.out.print(tempID+"\t");
			String ChemFormula = "";
			
			
			if(AllCycMet.contains(RMS.decodeID(tempID))){
				//System.out.println(cyc.getSlotValues(RMS.encodeId(PGA.comMetDict.get(met)),"CHEMICAL-FORMULA"));
				tempArr = cyc.getSlotValues(RMS.decodeID(tempID),"CHEMICAL-FORMULA");
				for(int i=0;i<tempArr.size();i++){
					for(int j=0;j<tempArr.get(i).size();j++){
						ChemFormula=ChemFormula+tempArr.get(i).get(j);
					}
				}
				String SMILES = cyc.getSlotValue(RMS.decodeID(tempID),"SMILES");
				String INCHI = cyc.getSlotValue(RMS.decodeID(tempID),"INCHI");
				
				if(!ChemFormula.equals("")){
					PGA.bionet.getPhysicalEntityList().get(met).setChemicalFormula(ChemFormula);
					System.out.print("Chemical formula = "+ChemFormula+"\t");
				//System.out.println(PGA.bionet.getBioPhysicalEntityById(met).getChemicalFormula());
				}
				if(!SMILES.equals("") || !SMILES.equals("NIL")){
					PGA.bionet.getPhysicalEntityList().get(met).setSmiles(SMILES);
					System.out.print("SMILES = "+SMILES+"\t");
				}
				if(!INCHI.equals("") || !INCHI.equals("NIL")){
					PGA.bionet.getPhysicalEntityList().get(met).setInchi(INCHI);
					System.out.print("INCHI = "+INCHI);
				}
			}
			System.out.println("\n");
		}
		
		ArrayList<String> AllCycRxn =  cyc.getClassAllInstances("|Reactions|");
		//System.out.println(AllCycRxn);
		//System.exit(0);
		int q=0;
//		for(String rxn : PGA.bionet.getBiochemicalReactionList().keySet()){
			{ String rxn = "R_G6PBDH";
			q++;
			String tempID=rxn;
			String proteinNote = "";
			String geneNote = "";
			HashMap<String,BioPhysicalEntity> EnzList = new HashMap<String,BioPhysicalEntity>();
			
			System.out.print(q+") "+rxn+"\t");
			if(!PGA.comMetDict.isEmpty() && PGA.comMetDict.containsKey(rxn)){
				tempID = PGA.comMetDict.get(rxn);
			}
			
			if(tempID.indexOf("[")!=-1){
				tempID=tempID.substring(0, tempID.indexOf("["));
			}
			
			System.out.print(tempID+"\t");
			String ChemFormula = "";
			
			if(AllCycRxn.contains(tempID)){
				//System.out.print("AYBABTU\t");
				//System.out.println(cyc.getFrameSlots(tempID));
			
				ArrayList<String> EnzAct = cyc.getSlotValues(tempID, "ENZYMATIC-REACTION");
				for(String enzact: EnzAct){
					ArrayList<String> Enzymes = cyc.getSlotValues(enzact, "ENZYME");
					for(String enz : Enzymes){
						
						String temp = cyc.getSlotValue(enz,"COMMON-NAME");
						BPE = new BioProtein(enz,temp);
						EnzList.put(enz, BPE);
						
						
						if(proteinNote.equals("")){
							proteinNote = "("+temp+")";
						}else{
							proteinNote = proteinNote+" OR "+"("+temp+")";
						}
						System.out.print("Enzymes :"+enz+"\t");
						HashMap<String,BioGene> GeneList = new HashMap<String,BioGene>();
						ArrayList<String> Genes = cyc.getSlotValues(enz, "GENE");
						if(geneNote.equals("")){
							geneNote = geneNote+"(";
						}else{
							geneNote = geneNote+" OR (";
						}
						String geneNote1="";
						for(String G:Genes){							
							if(!cyc.getSlotValue(G, "ACCESSION-1").equals("NIL")){
								String tempGeneID = cyc.getSlotValue(G, "ACCESSION-1");
								BG = new BioGene(tempGeneID,tempGeneID);
								GeneList.put(tempGeneID, BG);
								System.out.print("Gene :"+BG.getId()+"\t");
							}else{
								BG = new BioGene(G,G);
								GeneList.put(G, BG);
								System.out.print("Gene :"+BG.getId()+"\t");
							}
							if(geneNote1.equals("")){
								geneNote1 = BG.getId();
							}else{
								geneNote1 = geneNote+" OR "+BG.getId();
							}
						}
						geneNote = geneNote+geneNote1+")";
						BPE.setGeneList(GeneList);
						PGA.bionet.getBiochemicalReactionList().get(rxn).addEnz(BPE);
						
						System.out.print("");
					}
				}
				//System.exit(0);
				
			}
			
		
			PGA.fixReactionNote(rxn, geneNote, proteinNote);
			System.out.print("\n");
		}
			System.out.println("\n\nEXIT CALLED\n\n");
			System.exit(0);
		BioNetwork2SbmlFile B2S = new BioNetwork2SbmlFile(PGA.bionet,false,false,"/home/sshameer/MERGEDFILEOUTminusCARDIOLIPINpopulated.xml");
		try {
			B2S.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fixReactionNote(String rxn,String geneNote,String proteinNote){
		String OldNote = this.bionet.getBiochemicalReactionList().get(rxn).getSbmlNote();
		String NewNote = OldNote.replace("<html:p>GENE_ASSOCIATION: </html:p>", "<html:p>GENE_ASSOCIATION: "+geneNote+"</html:p>");
		NewNote = NewNote.replace("<html:p>PROTEIN_ASSOCIATION: </html:p>","<html:p>PROTEIN_ASSOCIATION: "+proteinNote+"</html:p>");
		this.bionet.getBiochemicalReactionList().get(rxn).setSbmlNote(NewNote);
	}
	
	public HashMap<String,String> createIDdict(String filename){
		HashMap<String,String> IDdict = new HashMap<String,String>();
		
		FileReader fin;
		String templine;
		String[] tempstrarr;
		
		try {
			fin = new FileReader(filename);
			BufferedReader bf = new BufferedReader(fin);
			while((templine=bf.readLine())!=null){
				 tempstrarr = templine.split("\t");
				 IDdict.put(tempstrarr[1], tempstrarr[0]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return IDdict;
	}
	
	public String Dcompartment(String s){
		
		s=s.replaceFirst("_.$", "");
		String k = s;
		s=s.replaceFirst("_CCO.*$","");
		if(!k.equals(s)){
			System.out.println(s);
			//System.exit(0);
		}
		return s;
	}
}
