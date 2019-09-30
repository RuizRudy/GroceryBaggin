import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class GroceryBaggin {

	public static List<Item> itemList = new ArrayList();
	private static int totalItems = -2;
	private static int totalItemWeight = 0;
	private static int bags;
	private static int bagWeight;
	private static int searchType;
	private static int index = 0;
	
	static Queue<WorldState> queue;
	static Stack<WorldState> stack;
	
	public static void main(String[] args) {
		parseFile(args[0]);
		
		// Check if all items weigh more than bags can hold
		if((bags * bagWeight) < totalItemWeight) {
			System.out.println("Weight of all items exceeds total amount of bag weight. Exiting");
			return;
		}
		
		//TODO this needs to be taken in as an argument 
		searchType = parseSearchType(args[1]);
		if(searchType==0) {
			queue = new LinkedList();
		}
		else if(searchType==1) {
			stack = new Stack();
		
		}		
		
		// Iterate through file and create items objects
		Collections.sort(itemList);
		for(Item i : itemList) {
			System.out.println("ItemIndex: " + i.getNum() + ", size: " + i.getSize());
		}		
		//create empty bags list for worldstate
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(totalItems,bagWeight);
			bagList.add(newbag);
		}
		
		WorldState start = new WorldState(bagList,itemList);
		WorldState result = bagging(start);//The start of bagging
		
		
		if(result!=null) {
			System.out.println("SUCCESS");
			System.out.print(result.toString());
		}
		
		//
		
	}

	private static void parseFile(String file) {
		// Open file
		try {
			File openFile = new File(file);

			// Iterate through file, get total items, check if sum(itemWeight) < sum(bagWeight)
			Scanner sc = new Scanner(openFile);
			while (sc.hasNextLine()) {
				sc.nextLine();

				// Add to total Items
				totalItems++;
			}
			sc.close();

			// Iterate through file and create items
			sc = new Scanner(openFile);
			while (sc.hasNextLine()) {
				createNewItem(sc.nextLine());
			}

			sc.close();
		}
		catch(Exception e) {
			System.out.println("Could not open file!");
		}
	}
	
	private static int parseSearchType(String arg){
		if(arg.compareTo("-breadth") == 0)
			return 0;
		else if (arg == "-depth")
			return 1;
		else 
			return 9;
	}
	
	private static void createNewItem(String item) {
		// Split item line into its parameters
		// Format: Name Weight constraint itemA ... itemZ
		String itemArr[] = item.split(" ");
		
		// Save amount of bags and bag weight
		if(index == 0) {
			bags = Integer.parseInt(itemArr[0]);
			index++;
			return;
		}
		else if(index == 1) {
			bagWeight = Integer.parseInt(itemArr[0]);
			index++;
			return;
		}
		
		// parse item index
		int itemIndex = Integer.parseInt(itemArr[0].substring(4));
		
		// parse item weight and add to total
		int itemWeight = Integer.parseInt(itemArr[1]);
		totalItemWeight += itemWeight;
		
		// + constraint or - constraint or no constraint
		char itemSymbol;
		if(itemArr.length > 2) {
			itemSymbol = itemArr[2].charAt(0);
		}
		else {
			itemSymbol = ' '; //empty
		}
		
		// Loop through all constraint items and create constraint bit
		BitSet constraintBits = new BitSet(totalItems);
		if(itemSymbol == '+') {
			constraintBits.set(0, totalItems);
			constraintBits.clear(itemIndex);
		}
		
		//System.out.println(constraintBits);
		for(int i = 3; i < itemArr.length; i++) {
			int constraintItem = Integer.parseInt(itemArr[i].substring(4));
			
			if(itemSymbol == '+') {
				constraintBits.clear(constraintItem);
			}
			else { // - constraint
				constraintBits.set(constraintItem);
			}
		}
		System.out.println(constraintBits);
		// Create new Item
		Item newItem = new Item(itemIndex, itemWeight, constraintBits);
		itemList.add(newItem);
		// Add to itemList
		
	}
	
	private static WorldState bagging(WorldState initial) {//queue		
		if(totalItemWeight>(bagWeight*bags)) {
			return null;
		}
		queue.add(initial);
		
		while(!queue.isEmpty()) {
		
			WorldState temp = queue.poll();

			for(Item I : temp.itemList) {

				WorldState nextState = temp.putNextItem(I);

				if(nextState!=null) {					
					queue.add(nextState);				
					if(goal(nextState)) {						
						return nextState;
					}
				}
			}			
		}
		return null;	
	}
	
	private static boolean goal(WorldState toCheck) {

		if (toCheck.itemList.isEmpty()) {
			return true;
		}else {
			return false;
		}

	}
	
}
