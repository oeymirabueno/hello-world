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
			//invalid command rejector
			else if (!comm[0].equals("insert")||!comm[0].equals("select")||!comm[0].equals("update")) {
				System.out.println("ERROR: invalid command");
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

		if(command.equals("insert")){
			insert(values, btree, record, key);
		}
		else if(command.equals("update")){
			update(values, btree, record, key);
		}
		else if(command.equals("select")){
			select(values, btree, key);
		}

	}
	public static void insert(ValuesRecords values, BTreeStructure btree, String record, long key) throws IOException{
		values.insert(record);
		btree.insert(key, values.RECORDCOUNT);
	}
	public static void update(ValuesRecords values, BTreeStructure btree, String record, long key) throws IOException{
		long key_offsetvalue = btree.select(key);
		values.update(record, key_offsetvalue);
	}
	public static void select(ValuesRecords values, BTreeStructure btree, long key) throws IOException{
		long key_offsetvalue = btree.select(key);
		values.select(key_offsetvalue);
	}
}
