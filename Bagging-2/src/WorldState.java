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
	public int LCD() {
		return 0;
	}
	public void updateDomains(BitSet itemConstraint, int j) {//||(i<totalItems && bag.space < copyItems.get(i).getSize()  )
		//System.out.println(copyItems.size());
		for(Item I: itemList) {
			if(!itemConstraint.get(I.getID())) {
				I.constraints.set(j,false);
			}
			if(bagList.get(j).space < I.getSize() ) {
				I.constraints.set(j,false);
			}
		}
//		
//		for(int i = 0; (i<totalItems && !itemConstraint.get(i));i++ ) {//iterate through list of constraints provided 
//			//System.out.println(copyItems.get(i).constraints.length()+"  inside");
//			System.out.println(j);
//			copyItems.get(i);
//			copyItems.get(i).constraints.set(j,false);//grab the item and change its domain 
//		}
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
	

}
