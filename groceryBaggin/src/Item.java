import java.util.BitSet;

public class Item {
	private int itemNum;
	private int size;
	private BitSet constraints;
	
	public Item(int itemNum, int size, BitSet constraints) {
		
		this.itemNum = itemNum;
		this.size = size;
		this.constraints = constraints;
				
	}
	
	public int getNum() {
		return itemNum;
	}
	public int getSize() {
		return size;
	}
	public BitSet getConstraints() {
		return constraints;
	}

}
