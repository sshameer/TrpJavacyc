import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;




public class tcpElement {
	private String ID;			//syslog connection ID
	private String month;
	private String date;
	private String time;
	private String systemid;	//server name/id
	private Boolean kernel;
	private String IN;			//input connection
	private String OUT;			//output connection
	private String SRC;			//source ip
	private String DST;		//destination ip
	private String PROTO;		//protocol
	private int SPT;			//source port
	private int DPT;			//destination port
	private Boolean blocked=false;	//Blocked or not
	
	public String getID(){ 
		return ID;
	}
	
	public String getMonth(){
		return month;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getTime(){
		return time;
	}
	
	public String getSystemID(){
		return systemid;
	}
	
	public Boolean getKernelFlag(){
		return kernel;
	}
	
	public String getInputDevice(){
		return IN;
	}
	
	public String getOutputDevice(){
		return OUT;
	}
	
	public String getSourceIP(){
		return SRC;
	}
	
	public String getDestinationIP(){
		return DST;
	}
	
	public String getProtocol(){
		return PROTO;
	}
	
	public int getSourcePort(){
		return SPT;
	}
	
	public int getDestinationPort(){
		return DPT;
	}
	
	public Boolean getBlockedStatus(){
		return blocked;
	}
	
	
	private void setID(String input){ 
		ID=input;
	}
	
	private void setMonth(String input){
		month=input;
	}
	
	private void setDate(String input){
		date=input;
	}
	
	private void setTime(String input){
		time=input;
	}
	
	private void setSystemID(String input){
		systemid=input;
	}
	
	private void setKernelFlag(Boolean input){
		kernel=input;
	}
	
	private void setInputDevice(String input){
		IN=input;
	}
	
	private void setOutputDevice(String input){
		OUT=input;
	}
	
	private void setSourceIP(String input){
		SRC=input;
	}
	
	private void setDestinationIP(String input){
		DST=input;
	}
	
	private void setProtocol(String input){
		PROTO=input;
	}
	
	private void setSourcePort(String input){
		SPT=Integer.parseInt(input);
	}
	
	private void setDestinationPort(String input){
		DPT=Integer.parseInt(input);
	}
	
	private void setBlockedstatus(Boolean input){
		blocked=input;
	}
	
	public void String2tcpElement(String line){
		this.setMonth(line.substring(0, 3));
		this.setDate(line.substring(4,6));
		this.setTime(line.substring(7,14));
	//	System.out.println(month+"\n"+date+"\n"+time);
		String[] lineparts = line.substring(16).split(" ");
		this.setSystemID(lineparts[0]);
		if(lineparts[1].equals("kernel:")){
			this.setKernelFlag(true);
		}else{
			this.setKernelFlag(false);
		}
	//	System.out.println(kernel);
		for(int i=3;i<lineparts.length;i++){
			if(lineparts[i].contains("IN=")){
				this.setInputDevice(lineparts[i].replaceFirst("IN=", ""));
	//			System.out.println(this.getInputDevice());
			}else if(lineparts[i].contains("OUT=")){
				this.setOutputDevice(lineparts[i+1]);
				i++;
	//			System.out.println(this.getOutputDevice());
			}else if(lineparts[i].contains("SRC=")){
				this.setSourceIP(lineparts[i].replaceFirst("SRC=", ""));
	//			System.out.println(this.getSourceIP());
			}else if(lineparts[i].contains("DST=")){
				this.setDestinationIP(lineparts[i].replaceFirst("DST=", ""));
	//			System.out.println(this.getDestinationIP());
			}else if(lineparts[i].contains("ID=")){
				this.setID(lineparts[i].replaceFirst("ID=", ""));
	//			System.out.println(this.getID());
			}else if(lineparts[i].contains("PROTO=")){
				this.setProtocol(lineparts[i].replaceFirst("PROTO=", ""));
	//			System.out.println(this.getProtocol());
			}else if(lineparts[i].contains("SPT=")){
				this.setSourcePort(lineparts[i].replaceFirst("SPT=", ""));
	//			System.out.println(this.getSourcePort());
			}else if(lineparts[i].contains("DPT=")){
				this.setDestinationPort(lineparts[i].replaceFirst("DPT=", ""));
	//			System.out.println(this.getDestinationPort());
			}else if(lineparts[i].contains("BLOCK")){
				this.setBlockedstatus(true);
			}
//			System.out.println(lineparts[i]);
		}
	}
	
	public static void main(String[] args){
		
		ArrayList<String> logcontents = new ArrayList<String>();
		ArrayList<tcpElement> tcpList = new ArrayList<tcpElement>();
		
		
		BufferedReader reader; 
		
		try {
			reader = new BufferedReader(new FileReader("/home/leo/Documents/syslogbackup.log"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				tcpElement tempTCP = new tcpElement();
			    tempTCP.String2tcpElement(line);
			    tcpList.add(tempTCP);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//tcpElement A = new tcpElement();
		//A.String2tcpElement("Nov  4 09:47:27 vm-trypanocyc kernel: [75533.587889] IN=eth0 OUT= MAC=00:50:56:ba:00:85:02:00:00:3e:0e:4b:08:00 SRC=147.99.104.180 DST=147.99.108.66 LEN=52 TOS=0x10 PREC=0x00 TTL=59 ID=54308 DF PROTO=TCP SPT=41613 DPT=80 WINDOW=1444 RES=0x00 ACK URGP=0");
		String localhostIP="147.99.108.66";
		
		List<String> allowedIP= new ArrayList<String>();
		allowedIP.add("147.99.104.180");
		allowedIP.add("147.99.104.5");
		
		int httpconn = 1;
		//tcpList.add(A);
		
		HashMap<String,Integer> IPrecorded = new HashMap<String,Integer>();
		
		for(tcpElement tcp:tcpList){
//			System.out.println("Source IP ="+tcp.getSourceIP());
			if(tcp.getSourceIP().equals(localhostIP)){
//				System.out.println("Destination IP ="+tcp.getDestinationIP());
				if(allowedIP.contains(tcp.getDestinationIP())){
					continue;
				}else{
					if(tcp.getSourcePort()<=1024){
						System.out.println("WARNING - Server connected to "+tcp.getDestinationIP()+" through "+tcp.getDestinationPort());
					}
				}
			}else if(allowedIP.contains(tcp.getSourceIP())){
				if(tcp.getDestinationPort() == 80){
					System.out.println(tcp.getSourceIP()+" verified user accessed "+tcp.getDestinationPort()+" on "+tcp.getMonth()+tcp.getDate()+" "+tcp.getTime());
					httpconn++;
				}
				continue;
			}else{
				if(tcp.getDestinationPort()<= 1024 && tcp.getDestinationPort()!= 80){					
					if(!tcp.getBlockedStatus()){
						System.err.println("ALERT - "+tcp.getMonth()+tcp.getDate()+" "+tcp.getTime()+" "+tcp.getSourceIP()+" accessed "+tcp.getDestinationPort());
					}
				}else{
					if(tcp.getDestinationPort() == 80){
						httpconn++;
						if(IPrecorded.containsKey(tcp.getSourceIP())){
							IPrecorded.put(tcp.getSourceIP(), IPrecorded.get(tcp.getSourceIP())+1);
						}else{
							IPrecorded.put(tcp.getSourceIP(), 1);
						}
						//System.out.println(tcp.getSourceIP()+" non-verified user accessed "+tcp.getDestinationPort()+" on "+tcp.getMonth()+tcp.getDate()+" "+tcp.getTime());
					}
					//System.out.println(tcp.getSourceIP()+" accessed "+tcp.getDestinationPort());
				}
			}
		}
		System.out.println("HTTPs "+httpconn);
		
		//for(String ip:IPrecorded.keySet()){
		//	System.out.println(ip+"\t"+IPrecorded.get(ip));
		//-}
		
		List<String> list = new ArrayList<String>(IPrecorded.keySet());
		Collections.sort(list);
		
		for(String ip:list){
			System.out.println(ip+"\t"+IPrecorded.get(ip));
		}
		
	}
}

