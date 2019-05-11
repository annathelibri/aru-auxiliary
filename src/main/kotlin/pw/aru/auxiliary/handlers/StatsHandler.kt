package pw.aru.auxiliary.handlers

import mu.KLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.auxiliary.ConfigManager
import pw.aru.utils.extensions.lib.jsonStringOf
import pw.aru.utils.extensions.lib.newCall
import java.net.http.HttpRequest

class StatsHandler(override val kodein: Kodein) : KodeinAware {
    companion object : KLogging()

    val tokens = ConfigManager<Tokens>("botlist-tokens.properties").config
    val httpClient: OkHttpClient by instance()
    val jsonType = MediaType.get("application/json")


    fun handleEvent(botId: String, guildCount: Long) {
        // DBL
        try {
            
            
            
            httpClient.newCall {
                url("https://discordbots.org/api/bots/$botId/stats")
                header("Authorization", tokens.dblToken)
                header("Content-Type", "application/json")
                post(
                    RequestBody.create(jsonType, jsonStringOf("server_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DBL", t)
        }

        // DBots
        try {
            httpClient.newCall {
                url("https://discord.bots.gg/api/v1/bots/$botId/stats")
                header("Authorization", tokens.dpwToken)
                header("Content-Type", "application/json")
                post(
                    RequestBody.create(jsonType, jsonStringOf("guildCount" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("Dbots.PW", t)
        }

        // Bots for Discord
        try {
            httpClient.newCall {
                url("https://botsfordiscord.com/api/bot/$botId")
                header("Authorization", tokens.bfdToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("BotsForDiscord", t)
        }

        // Botlist.space
        try {
            httpClient.newCall {
                url("https://botlist.space/api/bots/$botId")
                header("Authorization", tokens.blsToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("server_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("Botlist.Space", t)
        }

        // Divine Discord Bots
        try {
            httpClient.newCall {
                url("https://divinediscordbots.com/bots/$botId/stats")
                header("Authorization", tokens.ddbToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("server_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DivineDiscordBots", t)
        }

        // DBL2
        try {
            httpClient.newCall {
                url("https://discordbotlist.com/api/bots/$botId/stats")
                header("Authorization", tokens.dbl2Token)
                post(
                    RequestBody.create(jsonType, jsonStringOf("guilds" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DBL.com", t)
        }

        //Bots on Discord
        try {
            httpClient.newCall {
                url("https://bots.ondiscord.xyz/bot-api/bots/$botId/guilds")
                header("Authorization", tokens.bodToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("guildCount" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("BotsOnDiscord", t)
        }

        //Discord Bot World
        try {
            httpClient.newCall {
                url("https://discordbot.world/api/bot/$botId/stats")
                header("Authorization", tokens.dbwToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("guild_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DiscordBotWorld", t)
        }

        //DiscordBotsGroup
        try {
            httpClient.newCall {
                url("https://api.discordbots.group/v1/bot/$botId")
                header("Authorization", tokens.dbgToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DiscordBotsGroup", t)
        }

        //DiscordsBestBots
        try {
            httpClient.newCall {
                url("https://discordsbestbots.xyz/bots/$botId")
                header("Authorization", tokens.dbbToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("guilds" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DiscordsBestBots", t)
        }

        //LBots
        try {
            httpClient.newCall {
                url("https://lbots.org/api/v1/bot/$botId/stats")
                header("Authorization", tokens.lboToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("guild_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("LBots", t)
        }

        //DiscordBoats
        try {
            httpClient.newCall {
                url("https://discord.boats/api/bot/$botId")
                header("Authorization", tokens.dboToken)
                post(
                    RequestBody.create(jsonType, jsonStringOf("server_count" to guildCount))
                )
            }.execute().close()
        } catch (t: Exception) {
            handleStatsException("DiscordBoats", t)
        }
    }

    private fun handleStatsException(botlistName: String, e: Throwable?) {
        if (e == null) return
        logger.error(e) { "Error while posting stats on $botlistName" }
    }
}

data class Tokens(
    // botlists tokens
    var dblToken: String = "",
    var dpwToken: String = "",
    var bfdToken: String = "",
    var blsToken: String = "",
    var ddbToken: String = "",
    var dbl2Token: String = "",
    var bodToken: String = "",
    var dbwToken: String = "",
    var dbgToken: String = "",
    var dbbToken: String = "",
    var dboToken: String = "",
    var lboToken: String = ""
)