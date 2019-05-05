package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DynamicGuiCreator implements GuiCreator {
	private final SimpleGuiFormat guiFormat;
	private final String prefix;
	private ItemStack backItem, pageBackItem, pageForwardItem, cosmeticItem;
	
	private final Map<ItemInfo, Map<Integer, List<ItemInfo>>> infoByAbsolutePosition = new HashMap<ItemInfo, Map<Integer, List<ItemInfo>>>();
	private final Map<ItemInfo, Integer> lastPageNumbers = new HashMap<ItemInfo, Integer>();
	
	public DynamicGuiCreator(String prefix, SimpleGuiFormat guiFormat, ItemStack backItem, ItemStack pageBackItem,
			ItemStack pageForwardItem, ItemStack cosmeticItem) {
		this.prefix = prefix;
		this.guiFormat = guiFormat;
		this.backItem = backItem;
		this.pageBackItem = pageBackItem;
		this.pageForwardItem = pageForwardItem;
		this.cosmeticItem = cosmeticItem;
	}
	
	public void prepare() {
		List<ItemInfo> infos = guiFormat.getPreparedData();
		
		for (ItemInfo info : infos) {
			if (!infoByAbsolutePosition.containsKey(info.getParent())) {
				infoByAbsolutePosition.put(info.getParent(), new HashMap<Integer, List<ItemInfo>>());
			}
			int page = (info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE);
			Map<Integer, List<ItemInfo>> map = infoByAbsolutePosition.get(info.getParent());
			if (!map.containsKey(page)) {
				map.put(page, new ArrayList<>());
			}
			map.get(page).add(info);
			if (!lastPageNumbers.containsKey(info.getParent())) {
				lastPageNumbers.put(info.getParent(), page);
			} else {
				int lastPage = lastPageNumbers.get(info.getParent());
				if (page > lastPage) {
					lastPageNumbers.put(info.getParent(), page);
				}
			}
		}
	}
	
	public Map<ItemInfo, Map<Integer, List<ItemInfo>>> getDynamicInfo(){
		return this.infoByAbsolutePosition;
	}
	
	public Map<ItemInfo, Integer> getLastPageNumbers() {
		return this.lastPageNumbers;
	}

	@Override
	public SimpleGuiFormat getFormat() {
		return guiFormat;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public ItemStack getBackItem() {
		return backItem;
	}

	@Override
	public ItemStack getPageBackItem() {
		return pageBackItem;
	}

	@Override
	public ItemStack getPageForwardItem() {
		return pageForwardItem;
	}

	@Override
	public ItemStack getCosmeticItem() {
		return cosmeticItem;
	}

	@Override
	public void openForPlayer(Player player) {
		new DynamicGuiHolder(player, this, null, 0);
	}

}
