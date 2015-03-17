import java.util.ArrayList;
import java.util.HashMap;


public class GenerateScoreRequest {

	public static void main(String[] args) {
		Javacyc cyc=new Javacyc("TRYPANO");
		
		ArrayList<String> rxnlist = cyc.getClassAllInstances("|Reactions|");
		HashMap<String,ArrayList<String>> reactionannotators = new HashMap<String,ArrayList<String>>();
		
		
		for(String m:rxnlist){
			CreateComments CC = new CreateComments();
			
			String[] tempstrarr=cyc.getSlotValue(m, "COMMENT").split("Click here to annotate this reaction");
			if(tempstrarr.length<2){
				continue;
			}
			String comment = tempstrarr[1];
			
			HashMap<String,ArrayList<String>> table = CC.GetSummaryTable(comment);
			//System.out.println("--------------\n");
			if(!(table.keySet().size()< 2)){
				continue;
			}
			
			ArrayList<String> temp =new ArrayList<String>();
			
			for(String n:table.keySet()){
				if(!n.equals("Annotator")){
					temp.add(n);
				}
			}
			
			if(temp.size()!=0){
				reactionannotators.put(m,temp);
			}
		}
		
		for(String o:reactionannotators.keySet()){
			System.out.println(o+"----"+reactionannotators.get(o));
			
		}
		System.out.println("Printed data");
		
		
	}

}
