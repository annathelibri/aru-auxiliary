package pw.aru.auxiliary.handlers

import mu.KLogging
import org.json.JSONObject
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.db.AruDB
import pw.aru.patreon.PatreonWebhookEvent
import pw.aru.patreon.PatreonWebhooksJava
import pw.aru.patreon.entity.EventType.*
import java.lang.IllegalStateException

class PatreonWebhookHandler(override val kodein: Kodein) : KodeinAware {

    companion object : KLogging()
    private val db: AruDB by instance()

    fun handleEvent(event: PatreonWebhookEvent) {
        //TODO("This is untested! Please check it on dev against Patreon APIv2 first!")

        when (event.eventType()) {
            CREATE_PLEDGE -> {

            }
            UPDATE_PLEDGE -> {

            }
            DELETE_PLEDGE -> {

            }
            else -> throw IllegalStateException("Illegal event type ${event.eventType()}")
        }
    }
}