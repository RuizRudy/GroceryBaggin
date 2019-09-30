import java.util.List;

public class WorldState {
	
	private static List<Item> itemList;
	public List<Bag> WorldState;
	public WorldState(List<Bag> bags,List<Item> itemList) {
		WorldState = bags;
		this.itemList = itemList;
	}
}
