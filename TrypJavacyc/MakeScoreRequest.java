import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsebionet.projects.trypanocyc.GetStats;



public class MakeScoreRequest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Javacyc cyc = new Javacyc("TRYPANO");
		updateCompartmentFromComments uCFC = new updateCompartmentFromComments();
		GetStats GS = new GetStats();
		//StringEscapeUtils SEU = new StringEscapeUtils();
		ArrayList<String> text= new ArrayList<String>();
		
		List<String> maxid= new ArrayList<String>();
		int max=0;
		int tempnumber=0;
		
		//System.out.println(cyc.getFrameSlots("GART-RXN"));
		//System.out.println(cyc.getSlotValue("GART-RXN", "ENZYMATIC-REACTION"));
		//System.out.println(cyc.getFrameSlots("ENZRXN1V8-6645"));
		//System.out.println(cyc.getSlotValue("ENZRXN1V8-6645", "COMMON-NAME"));
		//System.exit(0);
		
		
		ArrayList<String> rxnlist=cyc.getClassAllInstances("|Reactions|");
		rxnlist=GS.refineList(rxnlist);
		
		for(String m:rxnlist){
		//String m="F16BDEPHOS-RXN";
			if(m.equals("GLUTAMATE-DEHYDROGENASE-NADP+-RXN")||m.equals("GCVT-RXN")||m.equals("AMPSYN-RXN")||m.equals("DIHYDROFOLATEREDUCT-RXN")||m.equals("GLYCINE-DEHYDROGENASE-RXN")||m.equals("ADENPRIBOSYLTRAN-RXN")||m.equals("THYMIDYLATESYN-RXN")||m.equals("GCVMULTI-RXN")||m.equals("IMP-DEHYDROG-RXN")){                                            //Unable to resolve java.lang.OutOfMemoryError: Requested array size exceeds VM limit at String sum....   
				//System.out.println("############"+reactions.get(i)+"###########");
				continue;																									//GLUTAMATE-DEHYDROGENASE-NADP+-RXN,THYMIDYLATESYN-RXN, DIHYDROFOLATEREDUCT-RXN because of error java.util.NoSuchElementException at ArrayList temp4
			}//PROBLEMATIC REACTIONS			
			
			System.out.print("-------------"+m+"------------");
			
			HashMap<String,ArrayList<String>> table = uCFC.getAnnotationTable(cyc, m);
			
			if(table==null){
				System.out.println("Table is empty");
				continue;
			}
			
			HashMap<String,List<String>> annotation = uCFC.getAnnotation(table);
		
		//String text = cyc.getSlotValue(m, "COMMENT");
		
			//System.out.println("---------------");
		
			String temp="";
			String comma;
			int i=0;
			for(String p:annotation.get("Annotator")){
				if(i==0){
					comma="";
				}else{
					comma=", ";
				}
				temp=temp+comma+p;
				i++;
			}
		
			String enzyme = cyc.getSlotValue(m, "ENZYMATIC-REACTION");
			String enzymename = cyc.getSlotValue(enzyme, "COMMON-NAME");
						
			if(enzyme.equals("NIL")){
				enzymename = "";
			}
			
			text.add(cyc.getSlotValue(m, "COMMON-NAME")+"\t"+m+"\t"+temp+"\t"+enzymename+"\t"+cyc.getSlotValue(m, "EC-NUMBER")+"\t"+"http://vm-trypanocyc.toulouse.inra.fr/TRYPANO/NEW-IMAGE?type=REACTION-IN-PATHWAY&object="+m);
		
			//System.out.println("---------------");
		}
		
		GS.makeList("/home/sshameer/scorelist.txt", text);
		
	}

}
