import java.util.BitSet;

public class Bag {
	
	private int spaceRemaining;
	private BitSet constraints;//0 if allowed in bag 1 if not
	private BitSet bagItems;
	
	
	public Bag(int totalItems, int size) {
		
		this.spaceRemaining = size;
		constraints = new BitSet(totalItems);
		constraints.set(0,totalItems,false);
		bagItems = new BitSet(totalItems);
		bagItems.set(0,totalItems,false);
		
		
	}
	public boolean itemCheck(Item newItem) {
		
		if((spaceRemaining >= newItem.getSize()) && constraints.get(newItem.getNum()) && !newItem.getConstraints().intersects(bagItems) ) {
			putItem(newItem);
		return true;	
		
		}else return false;		
	}
	
	public void putItem(Item newItem) {
		
		spaceRemaining -= newItem.getSize();
		bagItems.set(newItem.getNum());		
		constraints.or(newItem.getConstraints());
		
	}
	public boolean isFull() {		
		return (spaceRemaining>0);
	}

}
