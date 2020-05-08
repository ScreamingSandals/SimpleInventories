package org.screamingsandals.simpleinventories.groovy.callback;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.groovy.builder.GroovyAnimationBuilder;
import org.screamingsandals.simpleinventories.groovy.builder.GroovyBukkitStackBuilder;
import org.screamingsandals.simpleinventories.groovy.builder.GroovyLongStackBuilder;
import org.screamingsandals.simpleinventories.groovy.builder.IGroovyStackBuilder;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.item.RenderCallback;
import org.screamingsandals.simpleinventories.utils.StackParser;

import java.util.*;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@Getter
@AllArgsConstructor
public class GroovyRenderCallback implements RenderCallback {
    private final Closure<GroovyRenderBuilder> closure;

    @Override
    public void render(PlayerItemInfo info) {
        GroovyRenderBuilder builder = new GroovyRenderBuilder(info);
        internalCallClosure(closure, builder);
        if (builder.animationList != null) {
            info.setAnimation(StackParser.parseAll(builder.animationList));
        }
        if (builder.purgeStack != null) {
            info.setStack(StackParser.parse(builder.purgeStack));
        }
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
    }
}
