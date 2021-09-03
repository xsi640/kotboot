package com.github.xsi640.kotboot.core.boot

import com.github.xsi640.kotboot.core.config.ApplicationConfig
import com.github.xsi640.kotboot.core.extension.logger
import com.github.xsi640.kotboot.core.inject.*
import com.github.xsi640.kotboot.core.routing.RestController
import com.github.xsi640.kotboot.core.scanner.ClassType
import com.github.xsi640.kotboot.core.scanner.StanderClassScanner
import kotlin.reflect.KClass

@Bean(singleton = true)
class ApplicationContext(val packages: Array<String>) : Boot {
    private val preloadingClasses = mutableMapOf<KClass<*>, Any>()
    override lateinit var injectorContext: InjectorContext
    override val order = 999

    init {
        showBanner()
        val cache: MutableMap<ClassType, Any> = mutableMapOf()
        val classScanner = StanderClassScanner()
        val beanFactory = StandardBeanFactory(cache)
        val dependenciesProcessor = StandardDependenciesProcessor()
        injectorContext = InjectorContextImpl(
            packages,
            classScanner,
            dependenciesProcessor,
            beanFactory,
            cache
        )
        preloadingClasses[InjectorContext::class] = injectorContext
        preloadingClasses[ApplicationContext::class] = this
    }

    private fun showBanner() {
        val text = this.javaClass.getResource("/banner.txt").readText(Charsets.UTF_8)
        val banner = text.replace("{version}", ApplicationConfig.getString("version"))
        logger.info(banner)
    }

    override fun run() {
        initInject()
    }

    private fun initInject() {
        logger.info("Preparing related components for dependency injection...")
        injectorContext.registerAnnotation(Bean::class)
        injectorContext.registerAnnotation(RestController::class)
        injectorContext.registerInterface(InjectProvider::class)
        injectorContext.registerSuperClass(Boot::class)

        preloadingClasses.keys.forEach {
            injectorContext.excluded(it)
            injectorContext.addBean(preloadingClasses[it]!!)
        }

        injectorContext.initialize()
        logger.info("Dependency injection ready.")

        val boots: List<Boot> = injectorContext.findBySuperClass(Boot::class)
        boots.sortedBy { it.order }.forEach { boot ->
            boot.injectorContext = this.injectorContext
            boot.run()
        }
    }

    override fun destroy() {
    }

    companion object {
        val logger by logger()
    }
}