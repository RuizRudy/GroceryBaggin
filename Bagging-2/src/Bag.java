import java.util.BitSet;

public class Bag {
	
	public int totalItems;//used for bit set size
	public int space;//will be decremented when items are added
	public BitSet bagItems; //when an item is added to the bag its corresponding bit index will be flipped to 1
	/**
	 * Bag constructor
	 * @param totalItems
	 * @param size
	 */
	public Bag(int totalItems, int size) {
		this.totalItems=totalItems;
		this.space = size;
		bagItems = new BitSet(totalItems);
		bagItems.set(0,totalItems,false);
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

}
