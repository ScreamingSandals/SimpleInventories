package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.simpleinventories.events.EventManager;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PagePlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.ThisPlaceholderParser;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
public class Inventory extends AbstractInventory {
    private String prefix = "Inventory";

    private boolean genericShop = false;
    private boolean genericShopPriceTypeRequired = true;
    private boolean animationsEnabled = false;
    private boolean showPageNumber = true;
    private boolean allowAccessToConsole = false;
    private boolean allowBungeecordPlayerSending = false;

    private final EventManager eventManager = new EventManager(null);

    private final Map<String, IPlaceholderParser> placeholders = new HashMap<>();
    private final Map<String, GenericItemInfo> ids = new HashMap<>();
    private final SubInventory mainSubInventory = new SubInventory(true, null);

    {
        var thisPlaceholder = new ThisPlaceholderParser();
        placeholders.put("this", thisPlaceholder);
        placeholders.put("self", thisPlaceholder);

        placeholders.put("page", new PagePlaceholderParser());
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
}
