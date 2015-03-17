/**
 * This script can be used to update the annotations form the annotation database. 
 * Script designed for Trypanocyc, but options would be added to use it for other Organism
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

//import baobab.hypercyc.connection.Javacyc;





import parsebionet.projects.trypanocyc.GetStats;
import parsebionet.projects.trypanocyc.ReactionCommentCreator;
//import parsebionet.utils.StringUtils;


//import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author lcottret  
 * 6 janv. 2012
 * 
 * edited:
 * @author sshameer
 * 9 oct. 2013
 * 
 */

public class CreateComments{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreateComments CC = new CreateComments();
		//StringEscapeUtils SEU = new StringEscapeUtils();
		
		System.out.println("-----------------\n"+new java.util.Date()+"\n");
		
		GetStats GS = new GetStats();
		String Palsson = "<p> Confidence scoring is based on Palsson\'s Confidence score for metabolic reconstruction. </p> <br></br> <table border> <tr> <td>Evidence type </td> <td>Confidence score </td> <td>Examples</td> </tr> <tr> <td>Biochemical data</td> <td>4</td> <td>Direct evidence for gene product function and biochemical reaction: Protein purification, biochemical assays, experimentally solved protein structures and comparitive gene-expression studies.</td> </tr> <tr> <td>Genetic data</td> <td>3</td> <td>Direct and indirect evidence for gene function: Knock-out charecterization, knock-in charecterization and over expression.</td></tr><tr><td>Physiological data</td><td>2</td><td>Indirect evidence for biochemical reaction based on physiological data: secretion products or defined medium components serve as evidence for transport and metabolic reactions.</td></tr><tr><td>Sequence data</td><td>2</td><td>Evidence for gene function: Genome annotation, SEED annotation.</td></tr><tr><td>Modeling data</td><td>1</td><td>No evidence is available but reaction is required for modeling. The included function is a hypothesis and needs experimental verification. The reaction mechanism may be different from the included reaction(s).</td></tr><tr><td>Not evaluated</td><td>0</td><td></td></tr></table><br></br><br></br>";
		
		
		// Connexion
		System.err.println("Connexion to cyc");
		Javacyc cyc = new Javacyc("TRYPANO");
				
		System.err.println("Get all reactions");
		// Get all reactions
		ArrayList<String> reactions = cyc.getClassAllInstances("|Reactions|");
		reactions=GS.refineList(reactions);
		
		//System.out.println(cyc.getSlotValues("6PGLUCONOLACT-RXN","COMMENT"));
		
		
		System.err.println("Populate comments");
				
		int n = reactions.size();
		//int r = reactions.indexOf("IMP-DEHYDROG-RXN");		
				for (int i = 0; i < n; i++) {
				//for (int i = r; i < r+1; i++){	
					if(i==reactions.indexOf("GLUTAMATE-DEHYDROGENASE-NADP+-RXN")||i==reactions.indexOf("GCVT-RXN")||i==reactions.indexOf("AMPSYN-RXN")||i==reactions.indexOf("DIHYDROFOLATEREDUCT-RXN")||i==reactions.indexOf("GLYCINE-DEHYDROGENASE-RXN")||i==reactions.indexOf("ADENPRIBOSYLTRAN-RXN")||i==reactions.indexOf("THYMIDYLATESYN-RXN")||i==reactions.indexOf("GCVMULTI-RXN")){                                            //Unable to resolve java.lang.OutOfMemoryError: Requested array size exceeds VM limit at String sum....   
						//System.out.println("############"+reactions.get(i)+"###########");
						continue;																									//GLUTAMATE-DEHYDROGENASE-NADP+-RXN,THYMIDYLATESYN-RXN, DIHYDROFOLATEREDUCT-RXN because of error java.util.NoSuchElementException at ArrayList temp4
					}//PROBLEMATIC REACTIONS																						//TRANS-RXN1V8-1 doesnt havent COMMENT
					//System.out.println("="+reactions.get(i)+"=");
					String reactionId = reactions.get(i);
					
//					String oldComment = cyc.getSlotValue(reactionId, "COMMENT");
					String tempstr = cyc.getSlotValue(reactions.get(i),"COMMENT");
					String score="";
					
					//if(!tempstrarr.contains("href")&& !tempstrarr.get(0).startsWith("<a href=\"http://www.metexplore.fr")){
					//	System.out.println(reactions.get(i)+"<---------------");
					//	continue;
					//}else{
					//	continue;
					//}
					
					if(!tempstr.equals("")){
						if(tempstr.contains("Annotator")){
							score = "Confidence score: 0\n";
							System.out.println("+++++++++++++++++++");
						}
						else {
							score = tempstr.replace("\"", "");
						}
					}else{
						score = "Confidence score: 0<br></br>";
					}
						//score = tempstrarr.get(0)+"\n";
						System.out.println(score);
					//}
					//else{
					//	score = "Confidence score: 0\n";
					//}
					
					String commentInHtml = ReactionCommentCreator.getCommentInHTML(reactionId);
					//HashMap<String,ArrayList<String>> table = CC.GetSummaryTable(commentInHtml);
					//System.err.println("Comment : "+commentInHtml);
					
					if(! commentInHtml.equalsIgnoreCase("")) {
						String contents=score+Palsson+commentInHtml;
						//commentInHtml=SEU.unescapeHtml4(commentInHtml);
						//contents=CC.htmlspecialchars_decode(contents);
						//commentInHtml=htmlspecialchars_decode(commentInHtml);
						//System.out.println(contents);
						contents=CC.ConserveText(contents);
						//contents=SEU.escapeHtml4(contents);
					
						
						//System.out.println("--------------\n"+contents);
						
						
						cyc.removeSlotValues(reactionId, "COMMENT");
						//System.out.println(cyc.getSlotValue(reactionId, "COMMENT")+"----------here it should be");
						String temp = "\""+contents+"\"";
						System.out.println(temp);
						cyc.putSlotValue(reactionId, "COMMENT",temp);
						System.out.println("Comments created for "+reactionId);
					}
					
					
					
				}
				
				//cyc.saveKB();
				
				System.out.println("-----------------");
				
				
				
	}
	
	public HashMap<String,ArrayList<String>> GetSummaryTable(String sum){
		HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
		
		String[] sumsplit = sum.split("</*tr*>");
		for(int j=0;j<sumsplit.length;j++){
			sumsplit[j]=sumsplit[j].replaceAll("</*td>", "\t");
			sumsplit[j]=sumsplit[j].replaceAll("</*i>", "");
			sumsplit[j]=sumsplit[j].replaceFirst("<tr .*>", "");
			String[] sentsplit= sumsplit[j].split("\t");
							
			ArrayList<String> another = new ArrayList<String>();
			if(sentsplit.length>1){
				for(int k=0;k<sentsplit.length;k++){
					//System.out.println(sentsplit[k]+"--");
					if(!sentsplit[k].equals("")){
						sentsplit[k]=sentsplit[k].replace("\"", "");
						another.add(sentsplit[k]);
						//System.out.println("Somethigs wrong");
					}
					//System.out.println("Somethigs wrong");
				}
				table.put(another.get(0), another);
				//System.out.println(another.get(0));
			}
				
		}
		
		//for(String kl:table.keySet()){
		//	System.out.println(table.get(kl));
		//}
		
		//System.exit(0);
		return table;
	}
	
	public String ConserveText(String m){
		ArrayList<Integer> n = new ArrayList<Integer>();
		int i=0;
		n.add(0);
		m=m.replaceAll("\"","&quot;");
		m=m.replace("\\&quot;", "\\\"");
		
		do{
			n.add(m.indexOf('"',n.get(i)+1));
			if(n.get(i+1)!=-1 && !m.substring(n.get(i+1)-1, n.get(i+1)).equals("\\")){
				//System.out.println(m.substring(n.get(i+1)-1));
			}
			//System.out.println(n.get(i+1));
			i++;
		}while(n.get(i)!=-1);
		//System.out.println(m);
		//System.exit(0);
		return m;
	}
}