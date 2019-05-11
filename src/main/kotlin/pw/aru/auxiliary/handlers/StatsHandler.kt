package pw.aru.auxiliary.handlers

import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import pw.aru.auxiliary.ConfigManager
import pw.aru.utils.extensions.lang.sendAsync
import pw.aru.utils.extensions.lib.jsonStringOf
import java.net.URI.create
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.discarding

class StatsHandler(override val kodein: Kodein) : KodeinAware {
    companion object : KLogging()

    val tokens = ConfigManager<Tokens>("botlist-tokens.properties").config
    val httpClient: HttpClient by instance()

    fun handleEvent(botId: String, guildCount: Long) {
        // DBL
        httpClient.sendAsync(discarding()) {
            uri(create("https://discordbots.org/api/bots/$botId/stats"))
            header("Authorization", tokens.dblToken)
            header("Content-Type", "application/json")
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("server_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DBL", t) }

        // DBots
        httpClient.sendAsync(discarding()) {
            uri(create("https://discord.bots.gg/api/v1/bots/$botId/stats"))
            header("Authorization", tokens.dpwToken)
            header("Content-Type", "application/json")
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guildCount" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("Dbots.PW", t) }

        // Bots for Discord
        httpClient.sendAsync(discarding()) {
            uri(create("https://botsfordiscord.com/api/bot/$botId"))
            header("Authorization", tokens.bfdToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("BotsForDiscord", t) }

        // Botlist.space
        httpClient.sendAsync(discarding()) {
            uri(create("https://botlist.space/api/bots/$botId"))
            header("Authorization", tokens.blsToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("server_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("Botlist.Space", t) }

        // Divine Discord Bots
        httpClient.sendAsync(discarding()) {
            uri(create("https://divinediscordbots.com/bots/$botId/stats"))
            header("Authorization", tokens.ddbToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("server_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DivineDiscordBots", t) }

        // DBL2
        httpClient.sendAsync(discarding()) {
            uri(create("https://discordbotlist.com/api/bots/$botId/stats"))
            header("Authorization", tokens.dbl2Token)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guilds" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DBL.com", t) }

        //Bots on Discord
        httpClient.sendAsync(discarding()) {
            uri(create("https://bots.ondiscord.xyz/bot-api/bots/$botId/guilds"))
            header("Authorization", tokens.bodToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guildCount" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("BotsOnDiscord", t) }

        //Discord Bot World
        httpClient.sendAsync(discarding()) {
            uri(create("https://discordbot.world/api/bot/$botId/stats"))
            header("Authorization", tokens.dbwToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guild_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DiscordBotWorld", t) }

        //DiscordBotsGroup
        httpClient.sendAsync(discarding()) {
            uri(create("https://api.discordbots.group/v1/bot/$botId"))
            header("Authorization", tokens.dbgToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DiscordBotsGroup", t) }

        //DiscordsBestBots
        httpClient.sendAsync(discarding()) {
            uri(create("https://discordsbestbots.xyz/bots/$botId"))
            header("Authorization", tokens.dbbToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guilds" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DiscordsBestBots", t) }

        //LBots
        httpClient.sendAsync(discarding()) {
            uri(create("https://lbots.org/api/v1/bot/$botId/stats"))
            header("Authorization", tokens.lboToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("guild_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("LBots", t) }

        //DiscordBoats
        httpClient.sendAsync(discarding()) {
            uri(create("https://discord.boats/api/bot/$botId"))
            header("Authorization", tokens.dboToken)
            POST(
                HttpRequest.BodyPublishers.ofString(jsonStringOf("server_count" to guildCount))
            )
        }.whenComplete { _, t -> handleStatsException("DiscordBoats", t) }
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