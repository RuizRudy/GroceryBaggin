import java.util.ArrayList;
import java.util.BitSet;
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
	public void updateDomains(BitSet itemConstraint, int j) {//||(i<totalItems && bag.space < copyItems.get(i).getSize()  )
		//System.out.println(itemConstraint);
		for(Item I: this.itemList) {
			if(itemConstraint.get(I.getID())) {
				//System.out.println(I.getID());
				I.domain.set(j,false);
			}
			if(bagList.get(j).space < I.getSize() ) {
				I.domain.set(j,false);
			}
		}

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
