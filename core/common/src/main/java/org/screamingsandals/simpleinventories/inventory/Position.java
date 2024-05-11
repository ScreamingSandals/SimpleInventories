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
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.utils.BreakType;

import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class Position implements Cloneable {
    @Setter(onMethod_ = @Deprecated)
    @Nullable
    private Supplier<Integer> absolute;
    @Nullable
    private Supplier<Integer> row;
    @Nullable
    private Function<Integer, Integer> column;
    @Nullable
    private Supplier<Integer> skip;
    @Nullable
    private Supplier<BreakType> linebreak;
    @Nullable
    private Supplier<BreakType> pagebreak;

    public int calculateThatPosition(int nextPosition, int columns, int rows) {
        int itemsOnPage = columns * rows;
        if (pagebreak != null && pagebreak.get().isBreakingBefore()) {
            nextPosition += itemsOnPage - (nextPosition % itemsOnPage);
        }
        if (linebreak != null && linebreak.get().isBreakingBefore()) {
            nextPosition += columns - nextPosition % columns;
        }
        if (row != null) {
            nextPosition -= nextPosition % itemsOnPage;
            nextPosition += (row.get() - 1) * columns + nextPosition % columns;
        }
        if (column != null) {
            nextPosition -= nextPosition % columns;
            nextPosition += column.apply(columns);
        }
        if (skip != null) {
            nextPosition += skip.get();
        }
        if (absolute != null) {
            nextPosition = absolute.get();
        }

        return nextPosition;
    }

    public int calculateNextPosition(int nextPosition, int columns, int rows) {
        boolean broken = false;
        if (linebreak != null && linebreak.get().isBreakingAfter()) {
            broken = true;
            nextPosition += columns - nextPosition % columns;
        }
        if (pagebreak != null && pagebreak.get().isBreakingAfter()) {
            int itemsOnPage = columns * rows;
            broken = true;
            nextPosition += itemsOnPage - nextPosition % itemsOnPage;
        }
        if (!broken) {
            nextPosition++;
        }
        return nextPosition;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Position clone() {
        var position = new Position();
        position.absolute = absolute;
        position.row = row;
        position.column = column;
        position.skip = skip;
        position.linebreak = linebreak;
        position.pagebreak = pagebreak;
        return position;
    }
}
