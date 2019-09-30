import java.util.BitSet;
import java.util.Comparator;

public class Item implements Comparable{
	private int itemNum;// item num is the index value representing the item
	private int size;// space that an item takes up
	private BitSet constraints;// 0 if allowed 1 if not
	/**
	 * Item constructor initializes Item values
	 * @param itemNum
	 * @param size
	 * @param constraints
	 */
	public Item(int itemNum, int size, BitSet constraints) {
		
		this.itemNum = itemNum;
		this.size = size;
		this.constraints = constraints;
				
	}
	/**
	 * Returns item index value to caller
	 * @return
	 */
	public int getNum() {
		return itemNum;
	}
	/** 
	 * Returns item size to caller
	 * @return
	 */
	public int getSize() {
		return size;
	}
	/**
	 * returns BitSet of item constraints to caller
	 * @return
	 */
	public BitSet getConstraints() {
		return constraints;
	}
	
	public static Comparator<Item> ItemSizeComparator = new Comparator<Item>() {
		public int compare(Item s1, Item s2) {
		   int item1 = s1.getSize();
		   int item2 = s2.getSize();

		   //ascending order
		   return item2-item1;

		   //descending order
		   //return StudentName2.compareTo(StudentName1);
	    }
	};

	public int compareTo(Object o) {
		int compareage=((Item)o).getSize();
        /* For Ascending order*/
        return compareage-this.size;
	}
}
