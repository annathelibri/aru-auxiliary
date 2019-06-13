package pw.aru.auxiliary.handlers

import mu.KLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.utils.extensions.lang.environment
import pw.aru.utils.extensions.lib.jsonStringOf
import pw.aru.utils.extensions.lib.newCall

class StatsHandler(override val kodein: Kodein) : KodeinAware {
    companion object : KLogging()

    object Tokens {
        // Discord Bot List - https://discordbots.org/
        val DBL_TOKEN by environment

        // Discord Bots - https://discord.bots.gg/
        val DPW_TOKEN by environment

        // Bots for Discord - https://botsfordiscord.com/
        val BFD_TOKEN by environment

        // Botlist.space - https://botlist.space/
        val BLS_TOKEN by environment

        // Divine Discord Bots - https://divinediscordbots.com/
        val DDB_TOKEN by environment

        // Discord Bot List 2 - https://discordbotlist.com/
        val DBL2_TOKEN by environment
        
        // Bots on Discord - https://bots.ondiscord.xyz/
        val BOD_TOKEN by environment

        // Discord Bot World - https://discordbot.world/
        val DBW_TOKEN by environment

        // Discord Bots Group - https://discordbots.group/
        val DBG_TOKEN by environment

        // Discord Best Bots - https://discordsbestbots.xyz/
        val DBB_TOKEN by environment

        // Discord Boats - https://discord.boats/
        val DBO_TOKEN by environment

        // LBots - https://lbots.org/
        val LBO_TOKEN by environment
    }

    val httpClient: OkHttpClient by instance()
    val jsonType = MediaType.get("application/json")


    fun handleEvent(botId: String, guildCount: Long) {
        // DBL
        try {



            httpClient.newCall {
                url("https://discordbots.org/api/bots/$botId/stats")
                header("Authorization", Tokens.DBL_TOKEN)
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
                header("Authorization", Tokens.DPW_TOKEN)
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
                header("Authorization", Tokens.BFD_TOKEN)
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
                header("Authorization", Tokens.BLS_TOKEN)
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
                header("Authorization", Tokens.DDB_TOKEN)
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
                header("Authorization", Tokens.DBL2_TOKEN)
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
                header("Authorization", Tokens.BOD_TOKEN)
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
                header("Authorization", Tokens.DBW_TOKEN)
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
                header("Authorization", Tokens.DBG_TOKEN)
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
                header("Authorization", Tokens.DBB_TOKEN)
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
                header("Authorization", Tokens.LBO_TOKEN)
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
                header("Authorization", Tokens.DBO_TOKEN)
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