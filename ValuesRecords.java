import java.io.*;


class ValuesRecords {
	
	long HEADER_OFFSET = 0;
	long RECORD_OFFSET = 8;
	long RECORDCOUNT;
	long VALUE_SIZE = 256;
	File file;
	RandomAccessFile thing;
	ValuesRecords(String strFile) throws IOException{
		file = new File(strFile);
		thing = new RandomAccessFile(file, "rwd");
		//is file.exists unnecessary?
		if(file.length()==0) {
			RECORDCOUNT=0;
			thing.seek(HEADER_OFFSET);
			thing.writeLong(RECORDCOUNT);
				
		}
		else {
			//access thing and find location
			thing.seek(HEADER_OFFSET);
			//inspect RECORDCOUNT value
			RECORDCOUNT = thing.readLong();
			//check if RECORDCOUNT=0
			if(RECORDCOUNT!=0) {
				//go to end of file
				thing.seek(RECORD_OFFSET+RECORDCOUNT*VALUE_SIZE);
			}
		}
	}
	//create insert function that allows writing into thing
	void insert (String data) throws IOException{
		//edit the records value
		thing.seek(HEADER_OFFSET);
 
		thing.writeLong(++RECORDCOUNT);
		thing.seek(RECORD_OFFSET+(RECORDCOUNT-1)*VALUE_SIZE);
		thing.writeShort(data.length());
		thing.write(data.getBytes("UTF8"), 0, data.length());


	}
	String select (int key) throws IOException{
		return "";
	}
	void update (String data) throws IOException{
		return;
	}
}
