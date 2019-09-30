import java.io.File;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class GroceryBaggin {

	private static List<Item> itemList;
	private static int totalItems = -2;
	private static int totalItemWeight = 0;
	private static int totalBagsWeight;
	private static int searchType;
	
	static Queue<WorldState> queue;
	
	public static void main(String[] args) {
		parseFile(args[0]);
		//TODO this needs to be taken in as an argument 
		searchType = 0;
		if(searchType==0) {
			queue = new LinkedList();
		}
		
		// Iterate through file and create items objects
		
		
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
	
	private static void createNewItem(String item) {
		// Split item line into its parameters
		// Format: Name Weight constraint itemA ... itemZ
		String itemArr[] = item.split(" ");
		
		if(itemArr.length == 1)
			return;
		
		int itemIndex = Integer.parseInt(itemArr[0].substring(4));
		int itemWeight = Integer.parseInt(itemArr[1]);
		
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
	
	private static void bagging() {
		
		
		
	}
	private static WorldState goalCheck(WorldState toCheck) {
		
		if (itemList.isEmpty()) {
			return toCheck;
		}else {
			return null;
		}
//		for (Bag B :toCheck.WorldState) {
//			
//		}
	}
	
}
