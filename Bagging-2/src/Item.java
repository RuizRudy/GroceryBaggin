import java.util.BitSet;
import java.util.Comparator;

public class Item implements Comparable {
	
	private int ID;//item number identity 
	public int size;
	public BitSet constraints;
	public BitSet domain;
	
	/**
	 * Constructor for each item
	 * @param itemNum
	 * @param size
	 * @param constraints
	 */
	public Item(int itemNum, int size, BitSet constraints,int domainSize) {		
		this.ID = itemNum;
		this.size = size;
		this.constraints = constraints;
		domain = new BitSet(domainSize);
		domain.set(0,domainSize,true);
	}
	/**
	 * Returns item index value to caller
	 * @return
	 */		
	public int getID() {
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
	/**
	 * Returns the size of the domain
	 * @return
	 */
	public int getDomainSize() {
		return domain.cardinality();
	}
	
	/**
	 * Comparator for the value of MVC in remaining item list
	 */
	public static Comparator<Item> mvcComparator = new Comparator<Item>() {
		public int compare(Item a, Item b) {
			return a.compareTo(b);
	    }
	};
	/**
	 * compare the two items
	 */
	@Override
	public int compareTo(Object b) {
		if(this.getDomainSize()<((Item) b).getDomainSize()) {
			return -1;
		}
		if(this.getDomainSize()>((Item) b).getDomainSize()) {
			return 1;
		}
		if(this.getDomainSize() == ((Item) b).getDomainSize()) {//if domains are equal we check constraints 
			if(this.getConstraints().cardinality()>((Item) b).getConstraints().cardinality()) {
				return -1;
			}
			if(this.getConstraints().cardinality()==((Item) b).getConstraints().cardinality()) {
				if(this.getSize()>((Item) b).getSize()) {
					return -1;
				}
				if(this.getSize()==((Item) b).getSize()) {
					return 0;
				}
				return 1;
			}
			return 1;
			
			
		}
		
		return 0;
	}
	public Item clone() {
		
		Item item = new Item(ID,size,constraints,domain.length());
		item.ID= this.ID;
		item.constraints=new BitSet();
		item.constraints= (BitSet) this.constraints.clone();
		item.domain=new BitSet();
		item.domain= (BitSet) this.domain.clone();
		
		return item;
		
		
	}
	public String toString() {
		return "item"+getID();
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
