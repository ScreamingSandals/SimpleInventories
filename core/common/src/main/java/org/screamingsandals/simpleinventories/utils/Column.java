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

package org.screamingsandals.simpleinventories.utils;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum Column {
    LEFT(row_size -> 0),
    MIDDLE(row_size -> row_size / 2),
    RIGHT(row_size -> row_size - 1),
    FIRST(row_size -> 0),
    CENTER(row_size -> row_size / 2),
    LAST(row_size -> row_size - 1);

    private final Function<Integer, Integer> convert;

    public int convert(int row_size) {
        return convert.apply(row_size);
    }
}
