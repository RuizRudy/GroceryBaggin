import java.util.BitSet;

public class Item {
	
	private int ID;//item number identity 
	private int size;
	private BitSet constraints;
	
	/**
	 * Constructor for each item
	 * @param itemNum
	 * @param size
	 * @param constraints
	 */
	public Item(int itemNum, int size, BitSet constraints) {		
		this.ID = itemNum;
		this.size = size;
		this.constraints = constraints;
	}
	/**
	 * Returns item index value to caller
	 * @return
	 */		
	public int getNum() {
		return ID;
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
	
	public void updateBitSet(int index, int value) {
		if(value == 0) // clear
			constraints.clear(index);
		else
			constraints.set(index);
	}
	
	public boolean equals(Object object) {
		boolean same = false;
		if (this.ID == ((Item)object).ID) {
			same = this.ID == ((Item)object).ID;
		}
		return same;
	}

}
