package juuxel.terrestrialvacation.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

data class Config(
    @get:JvmName("isOverworldGenerationDisabled") val disableOverworldGeneration: Boolean = false,
    val blacklistedBaseBiomes: List<Identifier> = emptyList()
) {
    fun toJson(): JsonObject =
        JsonObject().also { json ->
            json.addProperty("disableOverworldGeneration", disableOverworldGeneration)
            json.add("blacklistedBaseBiomes", JsonArray().also {
                for (biome in blacklistedBaseBiomes) {
                    it.add(biome.toString())
                }
            })
        }

    companion object {
        @get:JvmStatic
        @get:JvmName("get")
        val INSTANCE: Config by lazy {
            val file = FabricLoader.getInstance().configDirectory.resolve("TerrestrialVacation.json")
            val gson = GsonBuilder().setPrettyPrinting().create()

            if (!file.exists()) {
                val config = Config()
                file.writer().use { gson.toJson(config.toJson(), it) }
                config
            } else {
                file.reader().use { fromJson(gson.fromJson(it, JsonObject::class.java)) }
            }
        }

        fun fromJson(obj: JsonObject): Config {
            return Config(
                disableOverworldGeneration = obj.getAsJsonPrimitive("disableOverworldGeneration").asBoolean,
                blacklistedBaseBiomes = obj.getAsJsonArray("blacklistedBaseBiomes").map { Identifier(it.asString) }
            )
        }
    }
}
