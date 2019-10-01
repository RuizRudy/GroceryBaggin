import java.util.BitSet;

public class Bag {
	
	private int spaceRemaining;//Space remaining in bag
	public BitSet constraints;//0 if allowed in bag 1 if not
	public BitSet bagItems;//1 for items contained in bag
	public int totalItems;
	
	/**
	 * Initialize variables to represent Bag space
	 * @param totalItems
	 * @param size
	 */
	public Bag(int totalItems, int size) {
		this.totalItems=totalItems;
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
		
		
		if(newItem.getConstraints().get(newItem.getNum())) {
			
			return false;
		}
		
		if((spaceRemaining >= newItem.getSize()) && !constraints.get(newItem.getNum()) && !newItem.getConstraints().intersects(bagItems) ) {
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
	public String toString() {
		String s = "";
		s+=bagItems;
		return s;
	}
	public Bag clone() {
		
		Bag bag = new Bag(this.totalItems,this.spaceRemaining);
		bag.totalItems = this.totalItems;
		bag.constraints=new BitSet();
		bag.constraints= (BitSet) this.constraints.clone();
		bag.bagItems=new BitSet();
		bag.bagItems= (BitSet) this.bagItems.clone();
		
		return bag;
		
		
	}

}






