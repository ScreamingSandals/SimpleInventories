package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import net.kyori.adventure.inventory.Book;
import org.screamingsandals.simpleinventories.utils.CloneMethod;

@Data
public class Clone implements Cloneable {
    private CloneMethod cloneMethod = CloneMethod.MISSING;
    private String cloneLink;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Clone clone() {
        var clone = new Clone();
        clone.cloneMethod = cloneMethod;
        clone.cloneLink = cloneLink;
        return clone;
    }

    public void cloneInto(GenericItemInfo original, GenericItemInfo newOne) {
        if (original != null) {
            if (original.getItem() != null && !original.getItem().getMaterial().isAir() && (cloneMethod.isOverride() || newOne.getItem() == null || newOne.getItem().getMaterial().isAir())) {
                newOne.setItem(original.getItem());
            }
            if (original.hasAnimation()) {
                if (!newOne.hasAnimation() || cloneMethod.isIncrement()) {
                    newOne.getAnimation().addAll(original.getAnimation());
                } else if (cloneMethod.isOverride()) {
                    newOne.getAnimation().clear();
                    newOne.getAnimation().addAll(original.getAnimation());
                }
            }
            if (original.getVisible() != null && (cloneMethod.isOverride() || newOne.getVisible() == null)) {
                newOne.setVisible(original.getVisible());
            }
            if (original.getDisabled() != null && (cloneMethod.isOverride() || newOne.getDisabled() == null)) {
                newOne.setDisabled(original.getDisabled());
            }
            newOne.getProperties().addAll(original.getProperties());
            if (original.hasBook() && (cloneMethod.isOverride() || !newOne.hasBook())) {
                newOne.setBook(Book.book(original.getBook().title(), original.getBook().author(), original.getBook().pages()));
            }
            if (original.getWritten() != null && (cloneMethod.isOverride() || newOne.getWritten() == null)) {
                newOne.setWritten(original.getWritten());
            }
            newOne.getEventManager().cloneEventManager(original.getEventManager());
            if (original.hasChildInventory()) {
                if (!newOne.hasChildInventory()) {
                    newOne.setChildInventory(new SubInventory(false, newOne, newOne.getFormat()));
                    original.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(newOne.getChildInventory().getWaitingQueue()::add);
                } else if (cloneMethod.isIncrement()) {
                    original.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(newOne.getChildInventory().getWaitingQueue()::add);
                } else if (cloneMethod.isOverride()) {
                    newOne.setChildInventory(new SubInventory(false, newOne, newOne.getFormat()));
                    original.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(newOne.getChildInventory().getWaitingQueue()::add);
                }
            }
            if (original.getLocate() != null && (newOne.getLocate() == null || cloneMethod.isOverride())) {
                newOne.setLocate(original.getLocate());
            }
            if (!original.getExecutions().isEmpty()) {
                if (cloneMethod.isIncrement() || newOne.getExecutions().isEmpty()) {
                    newOne.getExecutions().addAll(original.getExecutions());
                } else if (cloneMethod.isOverride()) {
                    newOne.getExecutions().clear();
                    newOne.getExecutions().addAll(original.getExecutions());
                }
            }
        }
    }
}
