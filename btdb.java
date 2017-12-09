import java.util.*;
import java.io.*;
public class btdb {
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		//System.out.println(args[0]);
		//System.out.println(args[1]);
		Scanner sc = new Scanner(System.in);
		ValuesRecords values = new ValuesRecords(args[1]);
		BTreeStructure btree = new BTreeStructure(args[0]);
		while(sc.hasNext()) {
			//get command here!
			String commline = sc.nextLine();
			//split, then check comm[0] if exit; break if true, else commandLine
			
			String[] comm = commline.split(" ");
			
			if(comm[0].equals("exit")) {
				break;
			}
			else {
				commandLine(values, btree, commline);
			}
		}
		sc.close();
	}
	public static void commandLine (ValuesRecords values, BTreeStructure btree, String commline) throws IOException{
		String[] comm = commline.split(" ");
		String command = comm[0];
		if(command.equals("exit")) {
			return;
		}
		long key = Long.valueOf(comm[1]);
		String record = comm[2];
		//insert function. will resolve commands eventually
		//functions: insert, update, select, exit
		//separate function calls per set? or if functions?
		values.insert(record);
		btree.insert(key, values.RECORDCOUNT);
	}
	public static void insert() {
		
	}
}
