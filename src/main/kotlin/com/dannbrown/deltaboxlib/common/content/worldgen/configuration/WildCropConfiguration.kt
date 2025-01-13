package com.dannbrown.deltaboxlib.common.content.worldgen.configuration

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.Optional

data class WildCropConfiguration(
    val tries: Int,
    val xzSpread: Int,
    val ySpread: Int,
    val primaryFeature: Holder<PlacedFeature>,
    val secondaryFeature: Holder<PlacedFeature>,
    val floorFeature: Holder<PlacedFeature>?
) : FeatureConfiguration {

    companion object {
        val CODEC: Codec<WildCropConfiguration> = RecordCodecBuilder.create { config ->
            config.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter { it.tries },
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter { it.xzSpread },
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter { it.ySpread },
                PlacedFeature.CODEC.fieldOf("primary_feature").forGetter { it.primaryFeature },
                PlacedFeature.CODEC.fieldOf("secondary_feature").forGetter { it.secondaryFeature },
                PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter { it.floorFeature?.let { feature -> Optional.of(feature) } }
            ).apply(config) { tries, xzSpread, ySpread, primary, secondary, floor ->
                WildCropConfiguration(tries, xzSpread, ySpread, primary, secondary, floor.orElse(null))
            }
        }
    }
}