import java.util.BitSet;

public class Item {
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

}
