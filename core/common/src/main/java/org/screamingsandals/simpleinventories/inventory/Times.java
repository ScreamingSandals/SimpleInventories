/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.simpleinventories.utils.TimesFlags;

import java.util.ArrayList;
import java.util.List;

@Data
public class Times implements Cloneable {
    private int repeat = 1;
    private final List<TimesFlags> flags = new ArrayList<>();

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Times clone() {
        var times = new Times();
        times.repeat = repeat;
        times.flags.addAll(flags);
        return times;
    }
}
