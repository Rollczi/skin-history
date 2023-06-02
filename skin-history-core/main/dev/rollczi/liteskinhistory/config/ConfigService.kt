package dev.rollczi.liteskinhistory.config

import net.dzikoysk.cdn.*
import net.dzikoysk.cdn.reflect.Visibility
import net.dzikoysk.cdn.source.Resource
import net.dzikoysk.cdn.source.Source
import java.io.File

class ConfigService(private val dataFolder: File) {

    private val cdn: Cdn = CdnFactory.createYamlLike().settings
        .registerKotlinModule()
        .withMemberResolver(Visibility.PACKAGE_PRIVATE)
        .build()

    private val configs: Map<Class<*>, Any> = HashMap()

    fun reloadAll() {
        for (config in configs.values) {
            load(config)
        }
    }

    fun <CONFIG : Any> load(config: CONFIG) {
        load(config, config.javaClass.resource())
    }

    inline fun <reified CONFIG : Any> load(): CONFIG {
        return load(CONFIG::class.java)
    }

    fun <CONFIG : Any> load(clazz: Class<CONFIG>): CONFIG {
        return cdn.load(clazz.resource(), clazz).orThrow { cause: CdnException -> RuntimeException(cause) }
    }

    fun <CONFIG : Any> save(config: CONFIG, resource: Resource) {
        cdn.render(config, resource).orThrow { cause: CdnException -> RuntimeException(cause) }
    }

    private fun <CONFIG : Any> load(config: CONFIG, resource: Resource): CONFIG {
        return cdn.load(resource, config).orThrow { cause: CdnException -> RuntimeException(cause) }
    }

    private fun Class<*>.resource(): Resource {
        return Source.of(dataFolder, javaClass.getAnnotation(ConfigFile::class.java).fileName)
    }

}