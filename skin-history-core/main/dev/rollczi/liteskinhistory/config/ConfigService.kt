package dev.rollczi.liteskinhistory.config

import net.dzikoysk.cdn.*
import net.dzikoysk.cdn.reflect.Visibility
import net.dzikoysk.cdn.source.Resource
import net.dzikoysk.cdn.source.Source
import java.io.File

class ConfigService(private val dataFolder: File) {

    private val cdn: Cdn = CdnFactory.createYamlLike().settings
        .withMemberResolver(Visibility.PACKAGE_PRIVATE)
        .build()

    private val configs: MutableMap<Class<*>, Any> = HashMap()

    fun reloadAll() {
        for (config in configs.values) {
            reload(config, config.javaClass.resource())
        }
    }

    fun <CONFIG : Any> load(clazz: Class<CONFIG>): CONFIG {
        val resource = clazz.resource()
        val config = cdn.load(resource, clazz).orThrow { cause: CdnException -> RuntimeException(cause) }

        this.configs[clazz] = config
        this.save(config, resource)
        return config
    }

    private fun <CONFIG : Any> save(config: CONFIG, resource: Resource) {
        cdn.render(config, resource).orThrow { cause: CdnException -> RuntimeException(cause) }
    }

    private fun <CONFIG : Any> reload(config: CONFIG, resource: Resource): CONFIG {
        return cdn.load(resource, config).orThrow { cause: CdnException -> RuntimeException(cause) }
    }

    private fun Class<*>.resource(): Resource {
        return Source.of(dataFolder, this.getDeclaredAnnotation(ConfigFile::class.java).fileName)
    }

}