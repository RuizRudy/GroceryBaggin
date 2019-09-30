import java.util.List;

public class WorldState {
	
	public List<Item> itemList;
	public List<Bag> wState;
	public WorldState(List<Bag> bags,List<Item> itemList) {
		wState = bags;
		this.itemList = itemList;
	}

	public WorldState putNextItem(Item newItem) {
		List<Item> copyItems = itemList;
		List<Bag> copyState = wState;
		
		
		if(!copyItems.isEmpty()) {			
			
			for (Bag B : copyState) {
				if(B.itemCheck(copyItems.get(0))) {
					copyItems.remove(0);
					WorldState newState = new WorldState(copyState,copyItems);
					
					return newState;
				}
			}
			
			
		}
		
		return null;
		
	}

}
