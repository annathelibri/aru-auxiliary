package pw.aru.auxiliary

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import pw.aru.libs.properties.Properties
import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.full.createInstance

class ConfigManager<T : Any>(fileName: String, private val typeRef: TypeReference<T>, private val creator: () -> T) {
    companion object {
        private val mapper = jacksonObjectMapper()

        inline operator fun <reified T : Any> invoke(file: String) = ConfigManager<T>(
            file,
            jacksonTypeRef(),
            reflectionCreator()
        )

        inline fun <reified T : Any> reflectionCreator(): () -> T = T::class::createInstance
    }

    private val file = File(fileName)
    private val backupFile = File("$fileName.bkp")

    val config: T by lazy {
        try {
            mapper.convertValue<T>(Properties.fromFile(file), typeRef)
        } catch (e: Exception) {
            if (e !is FileNotFoundException) file.renameTo(backupFile)

            save(creator())
            throw e
        }
    }

    fun save() = save(config)

    private fun save(config: T) = file.writeText(
        Properties().apply { putAll(mapper.convertValue<Map<String, String>>(config)) }.storeToString(config.javaClass.simpleName)
    )
}
