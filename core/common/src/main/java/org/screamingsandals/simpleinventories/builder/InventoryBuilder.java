package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.placeholders.RuntimeDefinedPlaceholder;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class InventoryBuilder extends ParametrizedCategoryBuilder {
    private final Inventory inventory;

    @Override
    public LocalOptionsBuilder getCategoryOptions() {
        return new LocalOptionsBuilder(inventory.getLocalOptions());
    }

    public InventoryBuilder define(String definition) {
        var defsplit = definition.split(" as ", 2);
        var key = defsplit[0].trim();
        if (key.startsWith("%")) {
            key = key.substring(1);
        }
        if (key.endsWith("%")) {
            key = key.substring(0, key.length() - 1);
        }
        var placeholderFormat = key.split("(?<!\\.)\\.(?!\\.)", 2);
        key = placeholderFormat[0];
        var placeholderArguments = placeholderFormat.length == 1 ? "" : placeholderFormat[1];
        if (inventory.getPlaceholders().containsKey(key) && !(inventory.getPlaceholders().get(key) instanceof RuntimeDefinedPlaceholder)) {
            SimpleInventoriesCore.getLogger().severe("Placeholder " + key + " is already defined as non-dynamic placeholder!");
            return this;
        }
        if (!inventory.getPlaceholders().containsKey(key)) {
            inventory.registerPlaceholder(key, new RuntimeDefinedPlaceholder());
        }
        var parser = (RuntimeDefinedPlaceholder) inventory.getPlaceholders().get(key);
        if (defsplit.length == 2) {
            if (!placeholderArguments.isEmpty()) {
                parser.register(placeholderArguments, defsplit[1].trim());
            } else {
                parser.putDefault(defsplit[1].trim());
            }
        }
        return this;
    }

    public InventoryBuilder render(Consumer<ItemRenderEvent> consumer) {
        inventory.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public InventoryBuilder preClick(Consumer<PreClickEvent> consumer) {
        inventory.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public InventoryBuilder click(Consumer<PostClickEvent> consumer) {
        inventory.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public InventoryBuilder open(Consumer<SubInventoryOpenEvent> consumer) {
        inventory.getEventManager().register(SubInventoryOpenEvent.class, consumer);
        return this;
    }

    public InventoryBuilder close(Consumer<SubInventoryCloseEvent> consumer) {
        inventory.getEventManager().register(SubInventoryCloseEvent.class, consumer);
        return this;
    }

    public InventoryBuilder buy(Consumer<OnTradeEvent> consumer) {
        inventory.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }
}
