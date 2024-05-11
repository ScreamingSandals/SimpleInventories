/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.lib.spectator.Book;
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
            if (original.getItem() != null && !original.getItem().getType().isAir() && (cloneMethod.isOverride() || newOne.getItem() == null || newOne.getItem().getType().isAir())) {
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
                newOne.setBook(Book.builder().title(original.getBook().title()).author(original.getBook().author()).pages(original.getBook().pages()).build());
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
