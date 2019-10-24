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
public static int first=0;
	public static void main(String[] args) {
		parseFile(args[0]);//uses parseFile() and CreateNewItem() to create item list
		
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(i,totalItems,bagWeight);
			bagList.add(newbag);
		}
		WorldState result = null;
		WorldState init = new WorldState(bagList,itemList);
		stack = new Stack<WorldState>();
		
		stack.add(init);
		
//		System.out.println(totalItems);
//		System.out.println(init.itemList.get(0).constraints.get(4));
		//result = depthSearch();
		//result = localSearch();

		result = depthSearch();
		if(result!=null) {
			System.out.println("success");
			System.out.print(result.toString());
//			for(Bag b:result.bagList) {
//				System.out.println("space " +b.space);
//				System.out.println(b);
//				}
			
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
	public static List<Item> CopyItems(List<Item> src) {
		List<Item> newItems = new ArrayList<Item>();
		for (Item I : src) {
			newItems.add(I.clone());
		}
		
		return newItems;
		
	}
	
	
	public static void updateDomains(List<Item> copyItems, BitSet itemConstraint, Bag bag,int j) {//||(i<totalItems && bag.space < copyItems.get(i).getSize()  )
//		//System.out.println(copyItems.size());
//		for(Item I: copyItems) {
//			if(!itemConstraint.get(I.getID())) {
//				I.constraints.set(j,false);
//			}
//			if(bag.space < I.getSize() ) {
//				I.constraints.set(j,false);
//			}
//		}
////		
////		for(int i = 0; (i<totalItems && !itemConstraint.get(i));i++ ) {//iterate through list of constraints provided 
////			//System.out.println(copyItems.get(i).constraints.length()+"  inside");
////			System.out.println(j);
////			copyItems.get(i);
////			copyItems.get(i).constraints.set(j,false);//grab the item and change its domain 
////		}
	}
	
	//#########################bags
	public static void LCD(List<Item> copyItems,List<Bag> copyBags, Item I){
		for(int j=0; j<bags;j++) {
			if(I.domain.get(copyBags.get(j).ID)) {
				
			  copyBags.get(j).LCD = 0;
			
			
		//	for(int i = 0;i<totalItems && !I.constraints.get(i) && copyItems.get(i).domain.get(j) ;i++) {//part of I's constraints and had J as a domain value
			  for(Item o: copyItems) {
				  if(!I.constraints.get(o.getID()) && o.domain.get(copyBags.get(j).ID)) {
				  	copyBags.get(j).LCD++;	
				  }
			  }
			}
		}
		
		Collections.sort(copyBags);
	
	}

	public static WorldState depthSearch() {

		while (!stack.isEmpty()) {
			WorldState temp = stack.pop();
			if (goal(temp)) {
				stack.add(temp);
				return temp;

			}
			if(first==0) {
				for(Item b: temp.itemList) {
					System.out.println(b.getID()+"  "+b.constraints);
				}
				first++;
			}
			Collections.sort(temp.itemList);

			for (Item I : temp.itemList) {

				// sort bags for lcd when thinking about I
				if (temp.itemList.size() > 1) {
					LCD(temp.itemList, temp.bagList, I);
				}

				for (Bag b : temp.bagList) {
					if (I.domain.get(b.ID)&& I.getSize()<b.space) {//bag needs to be in domain and have enough space
						int index = temp.itemList.indexOf(I);
						List<Item> copyItems = new ArrayList<Item>(CopyItems(temp.itemList));// make copies of current																							
						List<Bag> copyBags = new ArrayList<Bag>(CopyBags(temp.bagList));// Make copy of current WS Bags
						
						//update bagItems bitset and space
						copyBags.get(temp.bagList.indexOf(b)).bagItems.set(I.getID());
						copyBags.get(temp.bagList.indexOf(b)).space = copyBags.get(temp.bagList.indexOf(b)).space- I.getSize();
						
//						System.out.println(I+" : "+I.domain+" : "+b.ID);
						copyItems.remove(index);

						WorldState newState = new WorldState(copyBags, copyItems);
						newState.updateDomains(I.constraints, b.ID);

						stack.add(newState);
						if (goal(newState)) {
							return newState;
						} else {
							WorldState result = depthSearch();
							if(goal(result)){
								if(first<0) {
								for(Bag b1: result.bagList) {
									System.out.println(b1.ID+" , "+b1.space+" , "+b1);
								}
								}
								return result;
							}
						}
						
					}

				}
			}

		}

		// find MRV using cardinality of bit set and item size
		// method of determining LCV
		// the depth search
		return null;

	}
		
	public static WorldState localSearch() {
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(i,totalItems,bagWeight);
			bagList.add(newbag);
		}
		
		Random rand = new Random();
		for(Item i : itemList) {
			bagList.get(rand.nextInt(bags)).addItem(i.getID(),i.getConstraints());
		}
		
		int totalConflicts = 0;
		for(Bag b : bagList) {
			System.out.println(b.getBagItems());
			b.valueOfConflicts();
		}
		
		WorldState current = new WorldState(bagList, itemList);
		
		while(current.isGoalState() != true) {
			// neighbor move
			WorldState neighbor = new WorldState(bagList, itemList);
			
			
			//if neighbor.value <= current.value
			if(neighbor.valueOfConflicts() < current.valueOfConflicts()) {
				
			}
		}
		
		
		return null;
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
			
			int j=0;
			while (sc.hasNextLine()) {
				createNewItem(sc.nextLine(),j);
				j++;
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
//System.out.println("check");
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
	private static void createNewItem(String item,int j) {
		// Split item line into its parameters
		// Format: Name Weight constraint itemA ... itemZ
		String itemArr[] = item.split(" ");
		
		// Save amount of bags and bag weight
		if(index == 0) {
			bags = Integer.parseInt(itemArr[0]);
			BitSet blank = new BitSet(totalItems);	
			blank.set(0, totalItems,false);
			for(int i=0; i<totalItems;i++) {
				Item I = new Item(i,0,(BitSet)blank.clone(),bags);
				itemList.add(I);
			}
			
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
				for(Item I : itemList) {
					if(I.getID()!=itemIndex && I.getID()!=constraintItem) {
						I.constraints.set(itemIndex);
					}
				}
			}
			else { // - constraint
				constraintBits.set(constraintItem); // 1 if dont bag with item
				itemList.get(constraintItem).constraints.set(itemIndex);
			
			}
			
		}
//		System.out.println(constraintBits);
		// Create new Item
	
		Item newItem = new Item(itemIndex, itemWeight, constraintBits,bags);
		itemList.get(itemIndex).size = itemWeight;
		itemList.get(itemIndex).constraints.or(constraintBits);
		
		//itemList.add(newItem);
		// Add to itemList
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
