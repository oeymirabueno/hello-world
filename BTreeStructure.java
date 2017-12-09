import java.io.*;

public class BTreeStructure {
	
	long HEADER_OFFSET=0;
	long ROOT_OFFSET = 8;
	long NODECOUNT;
	long ROOT_VALUE;
	long DATA_OFFSET = 16;
	long PARENT_ID_OFFSET = 8;
	long CHILD_ID_OFFSET = 8;
	long CURRENT_NODE_POSITION;
	long CURRENT_KEY_POSITION;
	long DEFAULT = -1;
	File file;
	BNode node;
	RandomAccessFile thing;
	BTreeStructure(String strFile) throws IOException{
		file = new File(strFile);
		thing = new RandomAccessFile(file, "rwd");
		
		if(file.length()==0) {
			NODECOUNT=0;
			ROOT_VALUE=DEFAULT;
			thing.writeLong(NODECOUNT);
			thing.writeLong(ROOT_VALUE);
		}
		else {
			thing.seek(HEADER_OFFSET);
			NODECOUNT = thing.readLong();
			thing.seek(ROOT_OFFSET);
			ROOT_VALUE=thing.readLong();
			thing.seek(DATA_OFFSET);
		}

		//number of nodes
		//number of records
		//23 long ints per node
	}
	//Node with 23 long ints
	void insert(long key, long key_offsetvalue) throws IOException{
		thing.seek(ROOT_OFFSET);
		//inspect  root value!
		if(thing.readLong()==-1) {
			thing.seek(HEADER_OFFSET);
			thing.writeLong(++NODECOUNT);
			thing.writeLong(ROOT_VALUE=0);
			//write parent_id of root = -1
			thing.writeLong(DEFAULT);
			//write child1_id = -1;
			thing.writeLong(DEFAULT);
			//write key and keyoffsetvalue for first time
			thing.writeLong(key);
			thing.writeLong(key_offsetvalue);
			//fill up the rest of the 184 bytes with -1!
			for(int i=0;i<144;i=i+8) {
				thing.writeLong(-1);
			}
		}
		else {
			//find the correct node!!!! node offset = 23*8 = 184 bytes
			//steps:
			//1. set current_node_position = root_value, current_key_position = 0.
			CURRENT_NODE_POSITION = ROOT_VALUE;
			CURRENT_KEY_POSITION = 0;
			long key_rightChild = DEFAULT;
			//CREATE A FUNCTION TO ENABLE RECURRENCE AND RISK OVERFLOW? OR LOOP?
			//set location to after records and root value, then find the root,
			//				then move to after parent ID and after child ID
			for(long i=CURRENT_KEY_POSITION;i<7;i++) {
				thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
				//check if empty
				if(i==6) {
					CURRENT_KEY_POSITION = 4;
					break;
				}
				if(thing.readLong()==-1) {
					thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET
							+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
					thing.writeLong(key);
					thing.writeLong(key_offsetvalue);
					thing.writeLong(key_rightChild);
					break;
				}
				thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
				//	if new > old, i++
				if(key>thing.readLong()) {
				}
				thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
				//	if new < old, check if child is nonempty, then move. else shift:
				//		store old into temp slot, replace old with new,
				//		recur using temp and temp_offsetvalue
				if(key<thing.readLong()) {
					thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
					if(thing.readLong()!=-1) {
						thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
						CURRENT_NODE_POSITION = thing.readLong();
						i=0;
					}
					else {
						thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
						long temp = thing.readLong();
						thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET)));
						thing.writeLong(key);
						long temp_offsetvalue = thing.readLong();
						thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET))+8);
						thing.writeLong(key_offsetvalue);
						long temp_rightChild = thing.readLong();
						thing.seek(DATA_OFFSET+(CURRENT_NODE_POSITION*184)+PARENT_ID_OFFSET+CHILD_ID_OFFSET+(i*(DATA_OFFSET+CHILD_ID_OFFSET))+16);
						thing.writeLong(key_rightChild);
						key = temp;
						key_offsetvalue = temp_offsetvalue;
						key_rightChild = temp_rightChild;
					}
				}
			}


			
	//for splitting
		//algorithm:
			//1. store the last 3 key/offset/child sets in temp variables and replace them with -1.
			//		Use CURRENT_KEY_POSITION as starting point.
			//		set child to the left of the 3 = NODECOUNT+1
			//2. create new node at position NODECOUNT+1. Store the 3 temps there.
			//3. go back to old node, promote 4th key/offset/child:
			//		if parent of current node is -1, create new node at NODECOUNT+2.
			//		if parent of current node is P, insert 4th key/offset/child into P.
			

		}
		

	}
	long select(long key) {
		//search for key_offsetvalue of given key within the tree
		//algorithm:
			//1. proceed to root node.
			//		CURRENT_NODE_POSITION = ROOT_VALUE,
			//		CURRENT_KEY_POSITION = 0;
			//2. compare per key. if key=comparedTo, return key_offsetvalue.
			//2.1. if key>comparedTo, next key (CURRENT_KEY_POSITION++).
			//2.2 if key<comparedTo, check if child is empty.
			//2.2.1 if child is empty, perform shifting.
			//2.2.2 if child is !empty, set:
			//		CURRENT_NODE_POSITION = Child_ID
			//		CURRENT_KEY_POSITION = 0;
		return 0;//key_offsetvalue
	}
}
//maybe we can just use a "node" system, where instead of invoking actual nodes we just have strings of numbers
class BNode {
	//keep as reference!
	long Parent_ID;
	long Child1_ID;
	long Key1;
	long Key1_OffsetValue;
	long Child2_ID;
	long Key2;
	long Key2_OffsetValue;
	long Child3_ID;
	long Key3;
	long Key3_OffsetValue;
	long Child4_ID;
	long Key4;
	long Key4_OffsetValue;
	long Child5_ID;
	long Key5;
	long Key5_OffsetValue;
	long Child6_ID;
	long Key6;
	long Key6_OffsetValue;
	long Child7_ID;

	BNode(){
		Parent_ID=
		Child1_ID=Key1=Key1_OffsetValue=
		Child2_ID=Key2=Key2_OffsetValue=
		Child3_ID=Key3=Key3_OffsetValue=
		Child4_ID=Key4=Key4_OffsetValue=
		Child5_ID=Key5=Key5_OffsetValue=
		Child6_ID=Key6=Key6_OffsetValue=
		Child7_ID=-1;
	}
	
}
