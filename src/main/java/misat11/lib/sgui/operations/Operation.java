package misat11.lib.sgui.operations;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;

public interface Operation {
	@Deprecated
	default Object resolveFor(Player player) {
		return resolveFor(player, null);
	}
	
	public Object resolveFor(Player player, PlayerItemInfo info);
}
