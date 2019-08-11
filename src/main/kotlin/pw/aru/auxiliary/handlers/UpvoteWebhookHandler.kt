package pw.aru.auxiliary.handlers

import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.db.AruDB
import pw.aru.db.entities.user.PledgeKey
import pw.aru.db.entities.user.UserPledge
import pw.aru.db.entities.user.UserProfile
import pw.aru.db.entities.user.UserSettings
import java.lang.Math.min
import java.time.Clock.systemUTC
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.OffsetDateTime.now

class UpvoteWebhookHandler(override val kodein: Kodein) : KodeinAware {

    companion object : KLogging()

    private val db: AruDB by instance()

    private fun isWeekend() = when (now(systemUTC()).dayOfWeek) {
        SATURDAY, SUNDAY -> true
        else -> false
    }

    fun handleEvent(userId: String) {
        val isWeekend = isWeekend()

        val id = userId.toLong()
        val settings = UserSettings(db, id)

        val pledgeKey = settings.pledgeKey?.let { PledgeKey(db, it) }
        val pledge = pledgeKey?.pledgeId?.let { UserPledge(db, it) }

        if (pledge != null && pledge.enabled && pledgeKey.enabled) {
            handleUpvote(id, min(pledge.amount, 1), isWeekend)
            return
        }

        if (settings.legacyPremium) {
            handleUpvote(id, min(settings.premiumAmount, 1), isWeekend)
            return
        }

        handleUpvote(id, 0, isWeekend)
    }

    private fun handleUpvote(id: Long, amount: Int, weekend: Boolean) {
        val profile = UserProfile(db, id)
        profile.money += amount * if (weekend) 2000 else 1000
    }
}