import java.util.BitSet;

public class Bag implements Comparable{
	
	public int totalItems;//used for bit set size
	public int space;//will be decremented when items are added
	public BitSet bagItems; //when an item is added to the bag its corresponding bit index will be flipped to 1
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
	
	/**
	 * Add item to the bag
	 * @return
	 */
	public void addItem(int itemID) {
		bagItems.set(itemID);
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

}
