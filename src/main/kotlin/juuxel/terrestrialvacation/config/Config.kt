package juuxel.terrestrialvacation.config

import com.google.common.collect.ImmutableList
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

data class Config(val blacklistedBiomes: List<Identifier> = DEFAULT_BLACKLIST) {
    fun toJson(): JsonObject =
        JsonObject().also { json ->
            json.add("blacklistedBiomes", JsonArray().also {
                for (biome in blacklistedBiomes) {
                    it.add(biome.toString())
                }
            })
        }

    companion object {
        private val DEFAULT_BLACKLIST = ImmutableList.of(
            Identifier("terrestria", "caldera"),
            Identifier("terrestria", "caldera_beach"),
            Identifier("terrestria", "caldera_foothills")
        )

        fun fromJson(obj: JsonObject): Config {
            return Config(blacklistedBiomes = obj.getAsJsonArray("blacklistedBiomes").map { Identifier(it.asString) })
        }

        fun load(): Config {
            val file = FabricLoader.getInstance().configDirectory.resolve("td_wip.json")
            val gson = GsonBuilder().setPrettyPrinting().create()
            return if (!file.exists()) {
                val config = Config()
                file.writer().use { gson.toJson(config.toJson(), it) }
                config
            } else {
                file.reader().use { fromJson(gson.fromJson(it, JsonObject::class.java)) }
            }
        }
    }
}
