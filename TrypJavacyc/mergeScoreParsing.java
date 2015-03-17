import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsebionet.biodata.BioNetwork;
import parsebionet.projects.trypanocyc.GenerateUpdateReports;
import parsebionet.projects.trypanocyc.GetStats;

/**
 * This script exports or import Merge_score values from a tab separated list
 * @author shameer
 *
 */
public class mergeScoreParsing {
	
	String inSbmlFile = new String();
	String outSbmlFile = new String();
	String inMergeScoreFile = new String();
	String outMergeScoreFile = new String();
	int flag = 0;
	
	public static void main(String[] args) {
		
		mergeScoreParsing MSP = new mergeScoreParsing();
		
		
		//options
		for(int i=0;i<args.length;i=i+2){
			if(args[i].equals("-insbml")){
				MSP.inSbmlFile = args[i+1];
			}else if(args[i].equals("-outsbml")){
				MSP.outSbmlFile = args[i+1];
			}else if(args[i].equals("-inMS")){
				MSP.inMergeScoreFile = args[i+1];
			}else if(args[i].equals("-outMS")){
				MSP.outMergeScoreFile = args[i+1];
			}else{
				System.out.println("Please check input arguments");
				System.exit(0);
			}
		}
		
		if(MSP.inSbmlFile.isEmpty()){
			System.out.println("One SBML file required as input");
			System.exit(0);
		}else if( MSP.outMergeScoreFile.isEmpty() && (MSP.inMergeScoreFile.isEmpty() || MSP.outSbmlFile.isEmpty())){
			System.out.println("Please check input aurguments");
			System.exit(0);
		}else if( !MSP.outMergeScoreFile.isEmpty() ){
			MSP.flag = 1;
		}else if( !MSP.inMergeScoreFile.isEmpty() && !MSP.outSbmlFile.isEmpty()){
			MSP.flag = 2;
		}
		
		System.out.println("Mode\t"+MSP.flag);
		//System.out.println("Check!!");
		GenerateUpdateReports GUR = new GenerateUpdateReports();
		GetStats GS = new GetStats();
		
		GUR.readReport(MSP.inSbmlFile);
		List<String> SBMLcontents = GUR.getFcontents();
		String ID ="";
		
		if(MSP.flag == 1){
						
			String MergeScore="";
		
			HashMap<String,String> contents = new HashMap<String,String>();
		
			for(String k:SBMLcontents){
				if(k.matches(".*<reaction id=\".*")){
					ID = (k.split("\""))[1];
					//System.out.print("\n"+ID);
				}else if(k.matches(".*SBML_MERGE_SCORE.*")){
					MergeScore = k.substring(32,33);
					//System.out.print("\t"+MergeScore);
				}
			
				contents.put(ID, MergeScore);
			
			}
		
			ArrayList<String> temp = new ArrayList<String>();
		
			for(String ind:contents.keySet()){
				if(!ind.equals("")){
					System.out.println(ind+"\t"+contents.get(ind));
					temp.add(ind+"\t"+contents.get(ind));
				}
			}
		
			GS.makeList(MSP.outMergeScoreFile,temp);
		}else if(MSP.flag == 2){
			GUR.readReport(MSP.inMergeScoreFile);
			List<String> MergeScoreFile = GUR.getFcontents();
			
			ArrayList<String> tempstrarr = new ArrayList<String>();
			
			HashMap<String,String> MergeScoreMap = new HashMap<String,String>();
			
			for(String l:MergeScoreFile){			
				MergeScoreMap.put((l.split("\t"))[0],(l.split("\t"))[1]);
			}
			
			int Strflag = 0;
			
			for(String k:SBMLcontents){
				if(k.matches(".*<reaction id=\".*")){
					ID = (k.split("\""))[1];
					Strflag = 1;
					
					if(!MergeScoreMap.keySet().contains(ID)){
						ID = ID.substring(2);
						if(ID.substring(0,1).matches("[1-9]")){
							ID="_"+ID;
						}
					}
					//System.out.print("\n"+ID);
				}else if(k.matches(".*SBML_MERGE_SCORE.*")){
					String tempstr = k.substring(32,33);
					if(tempstr.equals("0")){
						k=k.replace(k.substring(32,33),MergeScoreMap.get(ID));
						//System.out.print("\n"+MergeScoreMap.get(ID)+"---------"+ k+"\n");
						Strflag = 0;
					}else{
						System.out.println(ID+" already has a Merge Score");
						continue;
					}
				}else if(Strflag == 1 && k.matches(".*/notes.*")){
					k="      <html:p>SBML_MERGE_SCORE: "+MergeScoreMap.get(ID)+"</html:p>\n"+k;
					
					Strflag = 0;
					//MergeScore = k.substring(32,33);
					//System.out.print("\n"+k+"\n");
				}
				
				tempstrarr.add(k);
			
				//contents.put(ID, MergeScore);
			
			}
			
			
			GS.makeList(MSP.outSbmlFile, tempstrarr);
			
		}
	}

}
