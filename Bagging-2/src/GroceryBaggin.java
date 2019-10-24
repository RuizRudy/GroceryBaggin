import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Random;

public class GroceryBaggin {
	
	public static List<Item> itemList = new ArrayList<Item>();
	private static int totalItems = -2;
	private static int totalItemWeight = 0;//assigned by create new item when parsing
	private static int bags;//assigned by create new item when parsing
	private static int bagWeight;//assigned by create new item when parsing
	private static int searchType;
	private static int index = 0;
	private static Stack<WorldState> stack;

	public static void main(String[] args) {
		parseFile(args[0]);//uses parseFile() and CreateNewItem() to create item list
		
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(totalItems,bagWeight);
			bagList.add(newbag);
		}
		WorldState result = null;
		WorldState init = new WorldState(bagList,itemList);
		stack = new Stack<WorldState>();
		
		stack.add(init);
		
//		System.out.println(totalItems);
//		System.out.println(init.itemList.get(0).constraints.get(4));
		result = depthSearch();
		if(result!=null) {
			System.out.println("success");
			System.out.print(result.toString());
		}
		else
			System.out.println("failure");
		
	}
	public static List<Bag> CopyBags(List<Bag> src) {
		List<Bag> newBags = new ArrayList<Bag>();
		for (Bag b : src) {
			newBags.add(b.clone());
		}
		
		return newBags;
		
	}
	public static void updateDomains(List<Item> copyItems, BitSet itemConstraint, Bag bag,int j) {//||(i<totalItems && bag.space < copyItems.get(i).getSize()  )
		System.out.println(itemConstraint.length());
		for(int i = 0; (i<totalItems && !itemConstraint.get(i));i++ ) {//iterate through list of constraints provided 
			System.out.println(copyItems.get(i).constraints.length());
			copyItems.get(i).constraints.set(j,false);//grab the item and change its domain 
		}
	}
	public static void LCD(List<Item> copyItems,List<Bag> copyBags, Item I){
		for(int j=0; I.domain.get(j)&& j<bags;j++) {
			copyBags.get(j).LCD = 0;
			
			
			for(int i = 0;i<totalItems && !I.constraints.get(i) && copyItems.get(i).domain.get(j) ;i++) {//part of I's constraints and had J as a domain value
				copyBags.get(j).LCD++;				
			}
		}
		
		Collections.sort(copyBags);
	
	}
	public static WorldState depthSearch() {
		while(!stack.isEmpty()) {			
			WorldState temp = stack.pop();
			Collections.sort(temp.itemList);
			
			for(Item I : temp.itemList) {
			
					
				//sort bags for lcd when thinking about I
					LCD(temp.itemList,temp.bagList,I);
				
					for(int j=0; I.domain.get(j)&& j<bags;j++) {
						List<Item> copyItems = new ArrayList<Item>(CopyItems(temp.itemList));//make copies of current WS item list
						List<Bag> copyBags = new ArrayList<Bag>(CopyBags(temp.bagList));//Make copy of current WS Bags					
						copyBags.get(j).bagItems.set(I.getID());	
						copyBags.get(j).space=copyBags.get(j).space-I.getSize();
						copyItems.remove(I);
						updateDomains(copyItems,I.constraints,copyBags.get(j),j);
						
						WorldState newState = new WorldState(copyBags,copyItems);
						stack.add(newState);
						
						if(goal(newState)) {
							return newState;
						}else {
						
						}			
					}
					depthSearch();
			}			
		}
		//find MRV using cardinality of bit set and item size	
		//method of determining LCV
		//the depth search
		return null;
		
	}
	public static void localSearch() {
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(totalItems,bagWeight);
			bagList.add(newbag);
		}
		
		Random rand = new Random();
		for(Item i : itemList) {
			
		}
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
	 * used to see if this state is a goal state
	 * @param toCheck
	 * @return
	 */
	private static boolean goal(WorldState toCheck) {

		if (toCheck.itemList.isEmpty()) {
			return true;
		}else {
			return false;
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
	
		Item newItem = new Item(itemIndex, itemWeight, constraintBits,bags);
		itemList.add(newItem);
		// Add to itemList
	}
	
}
