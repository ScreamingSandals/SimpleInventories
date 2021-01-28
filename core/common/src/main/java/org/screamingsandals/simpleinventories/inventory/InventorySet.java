package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.ToString;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PagePlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.ThisPlaceholderParser;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.*;
import java.util.regex.Pattern;

@Data
public class InventorySet implements Openable {
    private boolean genericShop = false;
    private boolean genericShopPriceTypeRequired = true;
    private boolean animationsEnabled = false;
    private boolean allowAccessToConsole = false;
    private boolean allowBungeecordPlayerSending = false;

    private final EventManager eventManager = new EventManager();

    private final Map<String, IPlaceholderParser> placeholders = new HashMap<>();
    private final Map<String, GenericItemInfo> ids = new HashMap<>();
    private final Map<String, String> variableToPropertyMap = new HashMap<>();
    private final SubInventory mainSubInventory = new SubInventory(true, null, this);
    @ToString.Exclude
    private final List<Insert> insertQueue = new ArrayList<>();

    {
        var thisPlaceholder = new ThisPlaceholderParser();
        placeholders.put("this", thisPlaceholder);
        placeholders.put("self", thisPlaceholder);

        placeholders.put("page", new PagePlaceholderParser());

        SimpleInventoriesCore.registerPlatformSpecificPlaceholders(placeholders);
    }

    public boolean registerPlaceholder(String name, String value) {
        return registerPlaceholder(name, (a,b,c,d) -> value);
    }

    public boolean registerPlaceholder(String name, IPlaceholderParser parser) {
        if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
            return false;
        }
        placeholders.put(name, parser);
        return true;
    }

    public String processPlaceholders(PlayerWrapper player, String text, PlayerItemInfo info) {
        var characters = text.toCharArray();
        var lastEscapeIndex = -2;
        var buf = "";
        for (var i = 0; i < characters.length; i++) {
            var c = characters[i];
            if (c == '{' && lastEscapeIndex != (i - 1)) {
                var bracketEnd = characters.length;
                var alastEscapeIndex = -2;
                var bracketBuf = "";
                for (var j = i + 1; j < characters.length; j++) {
                    var cc = characters[j];
                    if (cc == '\\' && alastEscapeIndex != (j - 1)) {
                        alastEscapeIndex = j;
                    } else if (cc == '}' && alastEscapeIndex != (j - 1)) {
                        bracketEnd = j;
                        break;
                    } else {
                        bracketBuf += cc;
                    }
                }
                i = bracketEnd;
                buf += String
                        .valueOf(OperationParser.getFinalOperation(this, bracketBuf).resolveFor(player, info));
            } else if (c == '\\' && lastEscapeIndex != (i - 1)) {
                lastEscapeIndex = i;
            } else {
                buf += c;
            }
        }

        text = buf;

        var pat = Pattern.compile("%[^%]+%");
        var matcher = pat.matcher(text);
        var sb = new StringBuffer();
        while (matcher.find()) {
            var matched = matcher.group();
            matched = matcher.group().substring(1, matched.length() - 1);
            var args = matched.split("(?<!\\.)\\.(?!\\.)");
            var gargs = new String[args.length - 1];
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].replaceAll("\\.+", ".");
                if (i > 0) {
                    gargs[i - 1] = args[i];
                }
            }
            var key = args[0];
            if (placeholders.containsKey(key)) {
                matcher.appendReplacement(sb, placeholders.get(key).processPlaceholder(key, player, info, gargs));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public Optional<GenericItemInfo> resolveItemLink(String link) {
        if (link.startsWith("$") || link.startsWith("§")) {
            return Optional.ofNullable(ids.get(link.substring(1)));
        }
        return Optional.empty();
    }

    public Optional<SubInventory> resolveCategoryLink(String link) {
        if (link.equalsIgnoreCase("main")) {
            return Optional.of(mainSubInventory);
        }
        var itemOpt = resolveItemLink(link);
        if (itemOpt.isPresent()) {
            var itemInfo = itemOpt.get();
            if (!itemInfo.hasChildInventory()) {
                itemInfo.setChildInventory(new SubInventory(false, itemInfo, itemInfo.getFormat()));
            }
            return Optional.of(itemInfo.getChildInventory());
        }
        return Optional.empty();
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        mainSubInventory.openInventory(wrapper);
    }
}
