import java.util.ArrayList;
import java.util.List;

import parsebionet.projects.trypanocyc.GetStats;


public class RetrievePathwaySummary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Javacyc cyc = new Javacyc("TRYPANO");
		Javacyc cyc2 = new Javacyc("META");
		
		GetStats GS = new GetStats();
		ArrayList<String> text = new ArrayList<String>();
		
		ArrayList<String> pathlist = cyc.getClassAllInstances("|Pathways|");
		
		pathlist = GS.refinePathList(pathlist);
		
		//for(String n:pathlist){
		//	System.out.println(n);
		//}
		
		//System.exit(0);
		
		String tmp1;
		String tmp2;
		ArrayList<String> tmp3;
		
		//int i=0;
		//for(String m:pathlist){
			String m="PWY-5097";
			//if (m.equals("PWY-6755") || m.equals("GLUTDEG-PWY") || m.equals("PWY-4041")||m.equals("PWY1G-0")){		//NosuchElememt PWY-6755, PWY-4041
			//	continue;																		//Java heap space GLUTDEG-PWY, PWY-4041, PWY1G-0
			//}
			//System.out.println(cyc.getFrameSlots(m));
			//System.exit(0);
			tmp1=cyc.getSlotValue(m, "COMMENT");
			if(tmp1.equals("NIL")){
				
				if(!m.matches("^PWY1V8.*")){
					tmp1=cyc2.getSlotValue(m,"COMMENT");
					tmp3=cyc2.getSlotValues(m,"COMMENT");
					
					if(tmp1.equals(":error")){
						tmp1 = "NIL";
					}
					//System.out.println(tmp3);
					//System.exit(0);
					
					if(tmp3.size()>2){
						for(int i=0;i<tmp3.size();i++){
							
							System.out.println(tmp3.get(i));
							tmp1=tmp1+" "+tmp3.get(i);
							
						}
					}else{
						tmp1=tmp3.get(0).replaceAll("\r","");
					}
				}
			}
			//if(i==100){
			//	System.exit(0);
			//}
			//i++;
			System.out.println("HAHA");
			System.out.println(tmp1);
			text.add(cyc.getSlotValue(m, "COMMON-NAME")+" # "+m+" # "+tmp1+" # http://vm-trypanocyc.toulouse.inra.fr/TRYPANO/NEW-IMAGE?type=PATHWAY&object="+m);
			
			System.out.println(cyc.getSlotValue(m, "COMMON-NAME")+" # "+m+" # "+tmp1+" # http://vm-trypanocyc.toulouse.inra.fr/TRYPANO/NEW-IMAGE?type=PATHWAY&object="+m);
		//}
		
		GS.makeList("/home/sshameer/pathwaysummary.txt", text);
	}

}
