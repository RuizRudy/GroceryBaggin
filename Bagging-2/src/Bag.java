import java.util.BitSet;

public class Bag implements Comparable{
	
	public int totalItems;//used for bit set size
	public int space;//will be decremented when items are added
	public BitSet bagItems; //when an item is added to the bag its corresponding bit index will be flipped to 1
	public BitSet constraintBits; // bits that show if the bag is constraint to an item index (0 ok - 1 not)
	public int LCD;
	public int ID;
	/**
	 * Bag constructor
	 * @param totalItems
	 * @param size
	 */
	public Bag(int ID,int totalItems, int size) {
		this.ID = ID;
		LCD=0;
		this.totalItems=totalItems;
		this.space = size;
		bagItems = new BitSet(totalItems);
		bagItems.set(0,totalItems,false);
		constraintBits = new BitSet(totalItems);
		constraintBits.set(0,totalItems,false);
	}
	/**
	 * getter for bags remaining space
	 * @return
	 */	
	public int getSpace() {
		return space;
	}
	/**
	 * Returns the items in the bag
	 * @return
	 */
	public BitSet getBagItems() {
		return bagItems;
	}
	public BitSet getConstraintBits() {
		return constraintBits;
	}
	public void setNewConstraints(BitSet bits) {
		constraintBits = bits;
	}
	public int getID() {
		return ID;
	}
	
	/**
	 * Add item to the bag
	 * @return
	 */
	public void addItem(int itemID) {
		bagItems.set(itemID);
	}
	
	public void addItem(int itemID, BitSet itemConstraints, int itemSize) {
		bagItems.set(itemID);
		constraintBits.or(itemConstraints);
		space -= itemSize;
	}
	/**
	 * used for making proper clones of bag
	 */
	public Bag clone() {
		
		Bag bag = new Bag(this.ID,this.totalItems,this.space);
		bag.totalItems = this.totalItems;
		bag.bagItems=new BitSet();
		bag.bagItems= (BitSet) this.bagItems.clone();
		
		return bag;
		
		
	}
	@Override
	public int compareTo(Object o) {
		if(this.LCD>((Bag)o).LCD) {
			return 1;
		}
		if(this.LCD==((Bag)o).LCD) {
			return 0;
		}
		return -1;
	}
	
	public String toString() {
		String s = "";
		s+=bagItems;
		return s;
	}
	
	public int valueOfConflicts() {
		int totalConflicts = 0;
		for (int i = bagItems.nextSetBit(0); i >= 0; i = bagItems.nextSetBit(i+1)) {
			if(bagItems.cardinality() == 1 && constraintBits.get(i) == bagItems.get(i)) {
				
			}
			if(constraintBits.get(i) == true) { // true being there exists conflicts in bag
		    	 //System.out.println("Item" + i + " conflicting in bag");
		    	 totalConflicts++;
		    }
		}
		if(space < 0)
			totalConflicts++;
		return totalConflicts;
	}
	
	public boolean willConflict(int itemID, BitSet itemConstraints, int size) {
		BitSet original = (BitSet)constraintBits.clone();
		original.or(itemConstraints);
		
		if(original.get(itemID) == true || space - size < 0) { // true being there exists conflict in the bag
			return true;
		}
		else {
			//constraintBits = original;
			//bagItems.set(itemID);
			//space -= size;
			return false;
		}
	}
	
	public void removeItem(int itemID, BitSet itemConstraints, int size) {
		bagItems.clear(itemID);
		//constraintBits.or(itemConstraints);
		
		space += size;
		
	}
	
	public void clearBag(int size) {
		bagItems.set(0,totalItems,false);
		constraintBits.set(0,totalItems,false);
		this.space = size;
	}

}
