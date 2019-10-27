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
    public static List<Bag> bagList = new ArrayList<Bag>(); 
    
    
	public static void main(String[] args) {
	
		parseFile(args[0]);//uses parseFile() and CreateNewItem() to create item list
		// Check if all items weigh more than bags can hold

		bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(i,totalItems,bagWeight);
			bagList.add(newbag);
		}
		
		
		WorldState result = null;
		WorldState init = new WorldState(bagList,itemList);
		for(Item I: itemList) {
			I.linkDomain.addAll(init.bagList);

		}

		
		
		
		
		stack = new Stack<WorldState>();
		
		stack.add(init);

		if((bags * bagWeight) < totalItemWeight) {
			System.out.println("failure");
			return;
		} 
		if(itemList.isEmpty()||(itemList.get(0).getSize()>bagWeight)) {
			System.out.println("failure");
			return;
		}
			
		
		

		//result = localSearch();
		

		result = depthSearch();
		
		
		if(result!=null) {
			System.out.println("success ");
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
	public static List<Item> CopyItems(List<Item> src) {
		List<Item> newItems = new ArrayList<Item>();
		for (Item I : src) {
			newItems.add(I.clone());
		}
		
		return newItems;
		
	}
	
	
	
	//#########################bags
	public static void LCV(Item item){		

		for(Bag b: item.linkDomain) {
			b.LCD=0;
			for(Item I: item.linkCST) {
				b.LCD++;
			}
		}		
		Collections.sort(item.linkDomain);
	
	}
	public static void ARC(WorldState state) {
		Queue<Item> queue = new LinkedList<Item>();
		queue.addAll(state.itemList);
		
		Item temp = queue.poll();
		
		for(Item I : itemList) {
			if(temp.constraints.get(I.getID())) {
				
			}
		}		
	}
	public static boolean Forward(Bag bag,List<Item> items) {
		for(Item I: items) {			
				if(I.linkDomain.size()>1) {
					
				}else if(I.linkDomain.size()>0 && I.linkDomain.contains(bag)) {
					return true;
					
				}
		}
		return false;
	}
	public static WorldState depthSearch() {

		while (!stack.isEmpty()) {
			WorldState temp = stack.pop();
			if (goal(temp)) {
				stack.add(temp);
				return temp;

			}
			Collections.sort(temp.itemList);

			
			for (Item I : temp.itemList) {
				// sort bags for lcv when thinking about I
				LCV(I);				
				if(I.domain.size()==0) {
					System.out.println("domain empty: ");
							return null;
				}
				for(Bag b : I.linkDomain) {
					
					if(Forward(b,I.linkCST)) {
						return null;
					}
					
					if(I.size <= temp.bagList.get(b.ID).space) {
						
					List<Bag> copyBags = new ArrayList<Bag>(CopyBags(temp.bagList));// Make copy of current WS Bags
					List<Item> copyItems = new ArrayList<Item>(CopyItems(temp.itemList));// make copies of current																							
					
					//update bagItems bitset and space
					copyBags.get(b.ID).bagItems.set(I.getID());					
					copyBags.get(b.ID).space-= I.size;
					
					
					copyItems.remove(temp.itemList.lastIndexOf(I));					
					for(Item I1: copyItems) {
						if(I.constraints.get(I1.getID()) || I1.size > copyBags.get(b.ID).space) {
							copyItems.get(copyItems.lastIndexOf(I1)).linkDomain.remove(b);
							I1.constraints.set(b.ID);
						}
					}
						WorldState newState = new WorldState(copyBags, copyItems);

					stack.add(newState);
					if (goal(newState)) {
						return newState;
					} else {
						WorldState result = depthSearch();
						if(result!=null) {					
							if(goal(result)){
								return result;
							}
						}
					}
				}
					
				}
			}

		}
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
			bagList.get(rand.nextInt(3)+1).addItem(i.getID());
		}
		for(Bag b : bagList) {
			System.out.println(b.getBagItems());
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
			
			itemList.get(itemIndex).linkCST.addAll(itemList);
			itemList.get(itemIndex).linkCST.remove(itemIndex);
			
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
						I.linkCST.add(itemList.get(itemIndex));
					}
				}
			}
			else { // - constraint
				constraintBits.set(constraintItem); // 1 if dont bag with item
				itemList.get(constraintItem).linkCST.add(itemList.get(itemIndex));
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
