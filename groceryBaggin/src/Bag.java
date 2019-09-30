import java.util.BitSet;

public class Bag {
	
	private int spaceRemaining;//Space remaining in bag
	private BitSet constraints;//0 if allowed in bag 1 if not
	public BitSet bagItems;//1 for items contained in bag
	
	/**
	 * Initialize variables to represent Bag space
	 * @param totalItems
	 * @param size
	 */
	public Bag(int totalItems, int size) {
		
		this.spaceRemaining = size;
		constraints = new BitSet(totalItems);
		constraints.set(0,totalItems,false);
		bagItems = new BitSet(totalItems);
		bagItems.set(0,totalItems,false);
		
		
	}
	/**
	 * Checks if newItem can be placed in bag 
	 *  
	 * @param newItem
	 * @return boolean based on its ability to be placed in bag
	 */
	public boolean itemCheck(Item newItem) {
		
		if((spaceRemaining >= newItem.getSize()) && constraints.get(newItem.getNum()) && !newItem.getConstraints().intersects(bagItems) ) {
			spaceRemaining -= newItem.getSize();
			bagItems.set(newItem.getNum());		
			constraints.or(newItem.getConstraints());
		return true;	
		
		}else return false;		
	}

	/**
	 * Checks if the bag has space left
	 * @return
	 */
	public boolean isFull() {		
		return (spaceRemaining>=0);
	}

}
