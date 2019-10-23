import java.util.List;

public class WorldState {
	
	public List<Item> itemList;
	public List<Bag> bagList;
	
	public WorldState(List<Bag> bags, List<Item> items) {
		bagList = bags;
		itemList = items;
	}
	

}
