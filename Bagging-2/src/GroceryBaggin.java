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
	//private static int index = 0;
	private static Stack<WorldState> stack;
	public static int first=0;
	static int iterations = 0;
	static int totalIterations = 0;
	private static int itemIndexG = 0;
	
	public static void main(String[] args) {
		parseFile(args[0]);//uses parseFile() and CreateNewItem() to create item list

		if(bags * bagWeight < totalItemWeight) {
			System.out.println("failure");
			return;
		}
		
		List<Bag> bagList = new ArrayList<Bag>();
		for(int i=0;i<bags;i++) {
			Bag newbag = new Bag(i,totalItems,bagWeight);
			bagList.add(newbag);
		}
		WorldState result = null;
		WorldState init = new WorldState(bagList,itemList);
		
		searchType = parseSearchType(args[1]);
		if(searchType==0) { // 
			// Without Arc Consistency
			
		}
		else if(searchType==1) {
			// standard deviation
			
		}	
		else if(searchType == 2) {
			// Local search
			result = localSearch();
		}
		
		stack = new Stack<WorldState>();
		
		stack.add(init);
		

		//result = localSearch();

		//result = depthSearch();
		result = localSearch();
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
	public static void LCD(List<Item> copyItems,List<Bag> copyBags, Item I){
		for(int j=0; j<bags;j++) {
			if(I.domain.get(copyBags.get(j).ID)) {
			  copyBags.get(j).LCD = 0;			
			  for(Item o: copyItems) {
				  if(!I.constraints.get(o.getID()) && o.domain.get(copyBags.get(j).ID)) {
				  	copyBags.get(j).LCD++;	
				  }
			  }
			}
		}
		
		Collections.sort(copyBags);
	
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
	public static WorldState depthSearch() {

		while (!stack.isEmpty()) {
			WorldState temp = stack.pop();
			if (goal(temp)) {
				stack.add(temp);
				return temp;

			}
			Collections.sort(temp.itemList);
			for (Item I : temp.itemList) {

				// sort bags for lcd when thinking about I
				if (temp.itemList.size() > 1) {
					LCD(temp.itemList, temp.bagList, I);
				}

				for (Bag b : temp.bagList) {
					if (I.domain.get(b.ID)&& I.getSize()<=b.space) {//bag needs to be in domain and have enough space
						int index = temp.itemList.indexOf(I);
						List<Item> copyItems = new ArrayList<Item>(CopyItems(temp.itemList));// make copies of current																							
						List<Bag> copyBags = new ArrayList<Bag>(CopyBags(temp.bagList));// Make copy of current WS Bags
						
						//update bagItems bitset and space
						copyBags.get(temp.bagList.indexOf(b)).bagItems.set(I.getID());
						copyBags.get(temp.bagList.indexOf(b)).space = copyBags.get(temp.bagList.indexOf(b)).space- I.getSize();
						
						//System.out.println(I+" : "+I.domain+" : "+b.ID+"  space "+b.space);
						copyItems.remove(index);
						
						WorldState newState = new WorldState(copyBags, copyItems);
						newState.updateDomains(I.constraints, b.ID);

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
			bagList.get(rand.nextInt(bags)).addItem(i.getID(),i.getConstraints(), i.getSize());
		}

		WorldState current = new WorldState(bagList, itemList);

		while(current.isGoalState() != true) {
			boolean movedItem = false;

			List<Bag> currentBags = current.getBags();


			// Check every bag
			for(int i = 0; i < bags; i++) {
				// If there is conflicts in bag, then grab item and move to other bag
				
				if(currentBags.get(i).valueOfConflicts() != 0) {
					BitSet items = currentBags.get(i).getBagItems();
					
					// j = item that is moving to other bag if fits
					for (int j = items.nextSetBit(0); j >= 0; j = items.nextSetBit(j+1)) {
					     // Try moving item to other bag that has no constraints
						//if(itemList.get(j).getConstraints().get(j) != true) {}
						int bagNum = 0;
						for(Bag b : currentBags) {

							if(!b.willConflict(itemList.get(j).getID(), itemList.get(j).getConstraints(), itemList.get(j).getSize()) && bagNum != i){
								//temp.removeItem(itemList.get(j).getID(), itemList.get(j).getConstraints(), itemList.get(j).getSize());
								//System.out.println("\nMoving " + itemList.get(j).getID() + " from Bag " + currentBags.get(i).getID() + " to Bag " + b.getID());
								//System.out.println("CurrentBag befor " + currentBags.get(i).getID() +  currentBags.get(i).getConstraintBits());
								currentBags.get(i).removeItem(itemList.get(j).getID(), itemList.get(j).getConstraints(), itemList.get(j).getSize());
								movedItem = true;

								BitSet newConstraints = new BitSet(totalItems);

								BitSet tmp = currentBags.get(i).getBagItems();
								//System.out.println("items after removal from bag " + currentBags.get(i).getID() + " " + currentBags.get(i).getBagItems());
								for (int k = tmp.nextSetBit(0); k >= 0; k = tmp.nextSetBit(k+1)) {
								     newConstraints.or(itemList.get(k).getConstraints());
								     
								}
								currentBags.get(i).setNewConstraints(newConstraints);
								//System.out.println("CurrentBag after " + currentBags.get(i).getConstraintBits());
								b.addItem(j, itemList.get(j).getConstraints(), itemList.get(j).getSize());
								
								iterations++;
								totalIterations++;
								bagNum = 0;
								break;
							}
							bagNum++;
						}
						if(movedItem)
							break;
					}
				}
				if(movedItem)
					break;
			}
			
			if(movedItem == true && iterations != 50) {
				current = new WorldState(currentBags, itemList);
				
				movedItem = false;
			}
			else {
				if(totalIterations > 1000000)
					return null;

				iterations = 0;
				rand = new Random();
				for(Bag b: bagList)
					b.clearBag(bagWeight);
				for(Item i : itemList) {
					bagList.get(rand.nextInt(bags)).addItem(i.getID(),i.getConstraints(), i.getSize());
				}
			}
		}
		
		/*while(current.isGoalState() != true) {
			rand = new Random();
			for(Bag b: bagList)
				b.clearBag(bagWeight);
			for(Item i : itemList) {
				bagList.get(rand.nextInt(bags)).addItem(i.getID(),i.getConstraints(), i.getSize());
			}
			iterations++;
			if(iterations == Integer.MAX_VALUE)
				return null;
		}*/
		
		
		return current;
	}
	
	
	private static int parseSearchType(String arg){
		if(arg.compareTo("-slow") == 0)
			return 0;
		else if (arg.compareTo("-minstdv")==0)
			return 1;
		else if (arg.compareTo("-local") == 0)
			return 2;
		else 
			return 9;
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
				createNewItem(sc.nextLine(), j);
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
	private static void createNewItem(String item,int index) {
		// Split item line into its parameters
		// Format: Name Weight constraint itemA ... itemZ
		String itemArr[] = item.split(" ");

		// Save amount of bags and bag weight
		if(index == 0) {
			bags = Integer.parseInt(itemArr[0]);
			BitSet blank = new BitSet(totalItems);	
			blank.set(0, totalItems,false);
			for(int i=0; i<totalItems;i++) {
				Item I = new Item(i, -1, 0,(BitSet)blank.clone(),bags);
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
				//itemList.get(constraintItem).constraints.set(itemIndex);
				itemList.get(constraintItem).constraints.set(itemIndex);
				//constraintBits.clear(itemIndex);
			}
		}
		// Create new Item
		Item newItem = new Item(itemIndex, itemIndexG, itemWeight, constraintBits,bags);
		itemList.get(itemIndexG).size = itemWeight;
		itemList.get(itemIndexG).constraints.or(constraintBits);
		itemIndexG++;
		
		//itemList.add(newItem);
		// Add to itemList
	}
	
	
	
	
	
	
	
	/*
	
	private static void createNewItem2(String item, int index) {
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

		// Create new Item
	
		Item newItem = new Item(itemIndex, -1, itemWeight, constraintBits,bags);
		itemList.add(newItem);
		// Add to itemList
	}
	*/
}
