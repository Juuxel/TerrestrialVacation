/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package juuxel.terrestrialvacation.biome.fabric;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

// Copied from FabricMC/fabric@06c939b
/**
 * Picks biomes with arbitrary double weights using a binary search.
 */
public final class WeightedBiomePicker {
    private double currentTotal;
    private List<ContinentalBiomeEntry> entries;

    public WeightedBiomePicker() {
        currentTotal = 0;
        entries = new ArrayList<>();
    }

    public void addBiome(final Biome biome, final double weight) {
        currentTotal += weight;

        entries.add(new ContinentalBiomeEntry(biome, weight, currentTotal));
    }

    double getCurrentWeightTotal() {
        return currentTotal;
    }

    public Biome pickRandom(LayerRandomnessSource random) {
        double target = random.nextInt(Integer.MAX_VALUE) * getCurrentWeightTotal() / Integer.MAX_VALUE;

        return search(target).getBiome();
    }

    /**
     * Searches with the specified target value.
     *
     * @param target The target value, must satisfy the constraint 0 <= target <= currentTotal
     * @return The result of the search
     */
    ContinentalBiomeEntry search(final double target) {
        // Sanity checks, fail fast if stuff is going wrong.
        Preconditions.checkArgument(target <= currentTotal, "The provided target value for biome selection must be less than or equal to the weight total");
        Preconditions.checkArgument(target >= 0, "The provided target value for biome selection cannot be negative");

        int low = 0;
        int high = entries.size() - 1;

        while (low < high) {
            int mid = (high + low) >>> 1;

            if (target < entries.get(mid).getUpperWeightBound()) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        return entries.get(low);
    }
}
