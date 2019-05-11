package pw.aru.auxiliary

import mu.KotlinLogging.logger
import org.json.JSONObject
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import pw.aru.auxiliary.handlers.PatreonWebhookHandler
import pw.aru.auxiliary.handlers.StatsHandler
import pw.aru.auxiliary.handlers.UpvoteWebhookHandler
import pw.aru.db.AruDB
import pw.aru.io.AruIO
import pw.aru.libs.kodein.jit.installJit
import pw.aru.patreon.PatreonWebhooksJava
import pw.aru.sides.AruSide
import pw.aru.utils.extensions.lang.threadGroupBasedFactory
import pw.aru.utils.extensions.lib.bindSelf
import java.net.http.HttpClient
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

fun main() {
    val log = logger {}

    val kodein = Kodein {
        installJit()
        bindSelf()

        bind<HttpClient>() with singleton {
            HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .executor(Executors.newFixedThreadPool(16, threadGroupBasedFactory("HttpClient")))
                .build()
        }

        bind<AruDB>() with singleton { AruDB(AruSide.AUXILIARY, 0) }
        bind<AruIO>() with singleton { instance<AruDB>().io() }
    }

    val db: AruDB by kodein.instance()
    val io: AruIO = db.io()

    val statsHandler = StatsHandler(kodein)
    val patreonHandler = PatreonWebhookHandler(kodein)
    val upvoteHandler = UpvoteWebhookHandler(kodein)

    io.configure {
        feed(AruSide.MAIN, "bot-stats") {
            log.info("Posting stats to botlists...")
            statsHandler.handleEvent(
                it.getString("user_id"),
                it.getString("guild_count").toLong()
            )
            log.info("Done.")
        }

        feed(AruSide.WEB, "webhook:patreon") {
            log.info("Parsing webhook event...")
            patreonHandler.handleEvent(
                PatreonWebhooksJava.parse(
                    it.getString("type"),
                    it.getJSONObject("data")
                )
            )
            log.info("Done.")
        }

        feed(AruSide.WEB, "webhook:botlist") {
            log.info("Handling botlist event...")
            upvoteHandler.handleEvent(it.getString("userId"))
            log.info("Done.")
        }

        configureFeed {
            sides(*AruSide.values())
            types("auxiliary:shutdown")
            onFeed {
                log.info("Handling shutdown... Goodbye.")
                System.exit(0)
            }
        }
    }

    log.info("Sending startup!")
    io.sendFeed("startup", JSONObject())

    val lock = ReentrantLock()
    lock.lock()

    thread(name = "Lock-Thread") {
        lock.lock()
    }
}