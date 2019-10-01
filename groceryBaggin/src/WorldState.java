import java.util.ArrayList;
import java.util.List;

public class WorldState {
	
	public List<Item> itemList;
	public List<Bag> wState;

	public WorldState(List<Bag> bags,List<Item> itemList) {
		wState = bags;
		this.itemList = itemList;
	}
	public List<Bag> CopyBags(List<Bag> src) {
		List<Bag> newBags = new ArrayList<Bag>();
		for (Bag b : src) {
			newBags.add(b.clone());
		}
		
		return newBags;
		
	}

	public WorldState putNextItem(Item newItem) {
		
		List<Item> copyItems = new ArrayList<Item>(itemList);//make copies of current WS item list
		List<Bag> copyState = new ArrayList<Bag>(CopyBags(wState));//Make copy of current WS Bags
		if(!copyItems.isEmpty()) {//only go if there are items remaining outside the bags	
			for (Bag B : copyState) {//for each Bag B try to check and put item
				if(B.itemCheck(newItem)) {//if Item is put in remove item from copyItems then make new worldState using new parameters
					copyItems.remove(newItem);
					WorldState newState = new WorldState(copyState,copyItems);
					return newState;
				}
			}			
		}
		
		return null;
		
	}
	
	public String toString() {
		String toS = "";
		for(Bag b : wState) {
			for(int i=0;i<b.bagItems.size();i++) {
				if(b.bagItems.get(i))
				 toS+="item"+i+"	";
			}
			toS+="\n";
			
		}
		
		
		return toS;
		
	}

}
