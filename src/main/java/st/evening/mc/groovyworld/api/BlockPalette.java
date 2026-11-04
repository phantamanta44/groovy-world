package st.evening.mc.groovyworld.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import st.evening.mc.groovyworld.util.random.Weighted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A palette of block states that can be randomly sampled.
 */
@FunctionalInterface
public interface BlockPalette {
    /**
     * Samples a block state from this palette. This operation should not modify the world!
     *
     * @param rng   The world generation RNG.
     * @param world The world in which the palette is being used.
     * @param pos   The position in the world at which the palette is being used.
     * @return The sampled block state.
     */
    IBlockState pickBlock(Random rng, World world, BlockPos pos);

    /**
     * A builder for constructing block palettes with weighted random sampling.
     */
    class Builder {
        private final List<Weighted<BlockPalette>> entries = new ArrayList<>();
        private int totalWeight;
        private boolean nonTrivialWeight = false;

        /**
         * Adds a sub-palette as an outcome of this palette. If that palette is the outcome of a roll, then it gets
         * subsequently sampled to get the ultimate block state outcome.
         *
         * @param entry The weighted sub-palette to add.
         */
        public Builder addEntry(final Weighted<BlockPalette> entry) {
            entries.add(entry);
            totalWeight += entry.getWeight();
            if (entry.getWeight() != 1) {
                nonTrivialWeight = true;
            }
            return this;
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome.
         *
         * @param entries The weighted sub-palettes to add.
         */
        public Builder addEntries(final Iterable<Weighted<BlockPalette>> entries) {
            for (final Weighted<BlockPalette> entry : entries) {
                addEntry(entry);
            }
            return this;
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome.
         *
         * @param entries The weighted sub-palettes to add.
         */
        @SafeVarargs
        public final Builder addEntries(final Weighted<BlockPalette>... entries) {
            for (final Weighted<BlockPalette> entry : entries) {
                addEntry(entry);
            }
            return this;
        }

        /**
         * Adds a sub-palette as an outcome of this palette. If that palette is the outcome of a roll, then it gets
         * subsequently sampled to get the ultimate block state outcome.
         *
         * @param weight  The weight of the outcome in this palette.
         * @param palette The sub-palette to add.
         */
        public Builder addPalette(final int weight, final BlockPalette palette) {
            return addEntry(new Weighted<>(palette, weight));
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome. All the sub-palettes receive the
         * same weight.
         *
         * @param weight   The weight of the outcomes in this palette.
         * @param palettes The sub-palettes to add.
         */
        public Builder addPalettes(final int weight, final Iterable<BlockPalette> palettes) {
            for (final BlockPalette palette : palettes) {
                addPalette(weight, palette);
            }
            return this;
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome. All the sub-palettes receive the
         * same weight.
         *
         * @param weight   The weight of the outcomes in this palette.
         * @param palettes The sub-palettes to add.
         */
        public Builder addPalettes(final int weight, final BlockPalette... palettes) {
            for (final BlockPalette palette : palettes) {
                addPalette(weight, palette);
            }
            return this;
        }

        /**
         * Adds a sub-palette as an outcome of this palette. If that palette is the outcome of a roll, then it gets
         * subsequently sampled to get the ultimate block state outcome. The outcome is given weight 1.
         *
         * @param palette The sub-palette to add.
         */
        public Builder addPalette(final BlockPalette palette) {
            return addPalette(1, palette);
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome. All the sub-palettes are given
         * weight 1.
         *
         * @param palettes The sub-palettes to add.
         */
        public Builder addPalettes(final List<BlockPalette> palettes) {
            for (final BlockPalette palette : palettes) {
                addPalette(palette);
            }
            return this;
        }

        /**
         * Adds a series of sub-palettes as outcomes of this palette. If one of these palettes is the outcome of a roll,
         * then it gets subsequently sampled to get the ultimate block state outcome. All the sub-palettes are given
         * weight 1.
         *
         * @param palettes The sub-palettes to add.
         */
        public Builder addPalettes(final BlockPalette... palettes) {
            for (final BlockPalette palette : palettes) {
                addPalette(palette);
            }
            return this;
        }

        /**
         * Adds a block state as an outcome of this palette.
         *
         * @param weight The weight of the outcome in this palette.
         * @param state  The block state to add.
         */
        public Builder addState(final int weight, final IBlockState state) {
            return addPalette(weight, (rng, world, pos) -> state);
        }

        /**
         * Adds a series of block states as outcomes of this palette. All the block states receive the same weight.
         *
         * @param weight The weight of the outcomes in this palette.
         * @param states The block states to add.
         */
        public Builder addStates(final int weight, final Iterable<IBlockState> states) {
            for (final IBlockState state : states) {
                addState(weight, state);
            }
            return this;
        }

        /**
         * Adds a series of block states as outcomes of this palette. All the block states receive the same weight.
         *
         * @param weight The weight of the outcomes in this palette.
         * @param states The block states to add.
         */
        public Builder addStates(final int weight, final IBlockState... states) {
            for (final IBlockState state : states) {
                addState(weight, state);
            }
            return this;
        }

        /**
         * Adds a block state as an outcome of this palette. The outcome is given weight 1.
         *
         * @param state The block state to add.
         */
        public Builder addState(final IBlockState state) {
            return addState(1, state);
        }

        /**
         * Adds a series of block states as outcomes of this palette. All the block states are given weight 1.
         *
         * @param states The block states to add.
         */
        public Builder addStates(final Iterable<IBlockState> states) {
            for (final IBlockState state : states) {
                addState(state);
            }
            return this;
        }

        /**
         * Adds a series of block states as outcomes of this palette. All the block states are given weight 1.
         *
         * @param states The block states to add.
         */
        public Builder addStates(final IBlockState... states) {
            for (final IBlockState state : states) {
                addState(state);
            }
            return this;
        }

        /**
         * Adds a block as an outcome of this palette. The block's default state is used.
         *
         * @param weight The weight of the outcome in this palette.
         * @param block  The block to add.
         */
        public Builder addBlock(final int weight, final Block block) {
            return addPalette(weight, (rng, world, pos) -> block.getDefaultState());
        }

        /**
         * Adds a series of blocks as outcomes of this palette. The blocks' default states are used. All the outcomes
         * receive the same weight.
         *
         * @param weight The weight of the outcomes in this palette.
         * @param blocks The blocks to add.
         */
        public Builder addBlocks(final int weight, final Iterable<Block> blocks) {
            for (final Block block : blocks) {
                addBlock(weight, block);
            }
            return this;
        }

        /**
         * Adds a series of blocks as outcomes of this palette. The blocks' default states are used. All the outcomes
         * receive the same weight.
         *
         * @param weight The weight of the outcomes in this palette.
         * @param blocks The blocks to add.
         */
        public Builder addBlocks(final int weight, final Block... blocks) {
            for (final Block block : blocks) {
                addBlock(weight, block);
            }
            return this;
        }

        /**
         * Adds a block as an outcome of this palette. The block's default state is used. The outcome is given weight 1.
         *
         * @param block The block to add.
         */
        public Builder addBlock(final Block block) {
            return addBlock(1, block);
        }

        /**
         * Adds a series of blocks as outcomes of this palette. The blocks' default states are used. All the outcomes
         * are given weight 1.
         *
         * @param blocks The blocks to add.
         */
        public Builder addBlocks(final Iterable<Block> blocks) {
            for (final Block block : blocks) {
                addBlock(block);
            }
            return this;
        }

        /**
         * Adds a series of blocks as outcomes of this palette. The blocks' default states are used. All the outcomes
         * are given weight 1.
         *
         * @param blocks The blocks to add.
         */
        public Builder addBlocks(final Block... blocks) {
            for (final Block block : blocks) {
                addBlock(block);
            }
            return this;
        }

        /**
         * Builds the palette from the inputs to this builder.
         *
         * @return The newly-constructed palette.
         */
        public BlockPalette build() {
            if (entries.isEmpty()) throw new IllegalStateException("Cannot build an empty palette!");
            if (entries.size() == 1) {
                return entries.get(0).getValue();
            } else if (nonTrivialWeight) {
                return new BuiltWeightedPalette(entries, totalWeight);
            } else {
                return new BuiltUniformPalette(entries);
            }
        }

        private static class BuiltUniformPalette implements BlockPalette {
            private final BlockPalette[] entries;

            private BuiltUniformPalette(final List<Weighted<BlockPalette>> entries) {
                this.entries = new BlockPalette[entries.size()];
                for (int i = 0; i < this.entries.length; i++) {
                    this.entries[i] = entries.get(i).getValue();
                }
            }

            @Override
            public IBlockState pickBlock(final Random rng, final World world, final BlockPos pos) {
                return entries[rng.nextInt(entries.length)].pickBlock(rng, world, pos);
            }
        }

        private static class BuiltWeightedPalette implements BlockPalette {
            private final Weighted<BlockPalette>[] entries;
            private final int totalWeight;

            @SuppressWarnings("unchecked")
            public BuiltWeightedPalette(final List<Weighted<BlockPalette>> entries, final int totalWeight) {
                this.entries = entries.toArray(new Weighted[0]);
                this.totalWeight = totalWeight;
            }

            @Override
            public IBlockState pickBlock(final Random rng, final World world, final BlockPos pos) {
                int k = rng.nextInt(totalWeight);
                for (final Weighted<BlockPalette> entry : entries) {
                    if (k < entry.getWeight()) {
                        return entry.getValue().pickBlock(rng, world, pos);
                    }
                    k -= entry.getWeight();
                }
                return entries[0].getValue().pickBlock(rng, world, pos); // failsafe, but this should never happen
            }
        }
    }
}
