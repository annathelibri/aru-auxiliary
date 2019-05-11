package pw.aru.auxiliary.handlers

import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.db.AruDB
import pw.aru.libs.patreonwebhooks.PatreonWebhookEvent
import pw.aru.libs.patreonwebhooks.entity.EventType.*

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