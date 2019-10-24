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
	
	public static List<Item> itemList = new ArrayList<Item>();
	private static int totalItems = -2;
	private static int totalItemWeight = 0;//assigned by create new item when parsing
	private static int bags;//assigned by create new item when parsing
	private static int bagWeight;//assigned by create new item when parsing
	private static int searchType;
	private static int index = 0;

	public static void main(String[] args) {
		parseFile(args[0]);//uses parseFile() and CreateNewItem() to create item list
		
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(totalItems,bagWeight);
			bagList.add(newbag);
		}
		depthSearch();
		
		for(Item i : itemList) {
			System.out.println(i.getConstraints());
		}
		
	}
	public static void depthSearch() {
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(totalItems,bagWeight);
			bagList.add(newbag);
		}
		
		//find MRV 
		//method of determining LCV
		//the depth search
		
		
	}

	
	
	
	/**
	 * File parser 
	 * @param file
	 */
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
	/**
	 * Parses Item line into item object 
	 * @param item
	 */
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
			constraintBits.set(0, totalItems); // set all other items to cant bag
			constraintBits.clear(itemIndex); // can bag with itself
		}
		
		//System.out.println(constraintBits);
		for(int i = 3; i < itemArr.length; i++) {
			int constraintItem = Integer.parseInt(itemArr[i].substring(4));
			
			if(itemSymbol == '+') {
				constraintBits.clear(constraintItem); // 0 if possible to bag item
			}
			else { // - constraint
				constraintBits.set(constraintItem); // 1 if dont bag with item
				if(constraintItem < itemIndex) {
					itemList.get(constraintItem).updateBitSet(itemIndex, 1);
				}
			}
		}
//		System.out.println(constraintBits);
		// Create new Item
		Item newItem = new Item(itemIndex, itemWeight, constraintBits);
		itemList.add(newItem);
		// Add to itemList
	}
	
}
