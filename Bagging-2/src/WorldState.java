import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorldState {
	
	public List<Item> itemList;
	public List<Bag> bagList;
	
	public WorldState(List<Bag> bags, List<Item> items) {
		bagList = bags;
		itemList = items;
	}
	
	/**
	 * used to get the next MRV for this State
	 * @return
	 */
	public int MRV() {		
		Item mrv = Collections.max(itemList);		
		return mrv.getID();
	}
	public int LCD() {
		return 0;
	}

	
	public List<Bag> CopyBags(List<Bag> src) {
		List<Bag> newBags = new ArrayList<Bag>();
		for (Bag b : src) {
			newBags.add(b.clone());
		}
		
		return newBags;
		
	}
	public List<Item> CopyItems(List<Item> src) {
		List<Item> newItems = new ArrayList<Item>();
		for (Item I : src) {
			newItems.add(I.clone());
		}
		
		return newItems;
		
	}
	public List<Bag> getBags(){
		return bagList;
	}
	public List<Item> getItems(){
		return itemList;
	}

	public String toString() {
		String toS = "";
		for(Bag b : bagList) {
			for(int i=0;i<b.bagItems.size();i++) {
				if(b.bagItems.get(i))
				 toS+="item"+i+"	";
			}
			toS+="\n";
			
		}
		
		
		return toS;
		
	}
	public boolean isGoalState() {
		for(Bag b : bagList) {
			if(b.valueOfConflicts() != 0) {
				return false;
			}
		}
		
		return true;
	}

}
