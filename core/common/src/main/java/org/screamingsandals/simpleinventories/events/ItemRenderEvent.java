package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class ItemRenderEvent {
    private final Inventory format;
    private final PlayerItemInfo item;
    private final PlayerWrapper player;

    /*
    @Deprecated
    public ItemInfo getOriginalInfo() {
        return info.getOriginal();
    }

    public Item getStack() {
        return info.getStack();
    }

    public void setStack(Item stack) {
        info.setStack(stack);
    }

    public boolean isVisible() {
        return info.isVisible();
    }

    public void setVisible(boolean visible) {
        info.setVisible(visible);
    }

    public boolean isDisabled() {
        return info.isDisabled();
    }

    public void setDisabled(boolean disabled) {
        info.setDisabled(disabled);
    }

        @RequiredArgsConstructor
    public static class GroovyRenderBuilder {
        @Getter
        private final PlayerItemInfo info;
        private List<Object> animationList;
        private Map<String,Object> purgeStack;

        public void disabled(boolean disabled) {
            info.setDisabled(disabled);
        }

        public void visible(boolean visible) {
            info.setVisible(visible);
        }

        public GroovyAnimationBuilder getAnimation() {
            if (animationList == null) {
                animationList = new ArrayList<>();
            }

            return new GroovyAnimationBuilder(animationList);
        }

        public void animation(Closure<GroovyAnimationBuilder> closure) {
            internalCallClosure(closure, getAnimation());
        }

        public IGroovyStackBuilder getStack() {
            if (purgeStack != null) {
                return new GroovyLongStackBuilder(purgeStack);
            }

            return new GroovyBukkitStackBuilder(info.getStack());
        }

        public IGroovyStackBuilder getClearStack() {
            if (purgeStack == null) {
                purgeStack = new HashMap<>();
            }

            return new GroovyLongStackBuilder(purgeStack);
        }

        public void stack(Closure<IGroovyStackBuilder> closure) {
            internalCallClosure(closure, getStack());
        }

        public void clearStack(Closure<IGroovyStackBuilder> closure) {
            internalCallClosure(closure, getClearStack());
        }

        public String process(String raw) {
            return info.getFormat().processPlaceholders(info.getPlayer(), raw, info);
        }

        public Player getPlayer() {
            return info.getPlayer();
        }

        public void player(Closure<Player> closure) {
            internalCallClosure(closure, getPlayer());
        }
    }*/
}
