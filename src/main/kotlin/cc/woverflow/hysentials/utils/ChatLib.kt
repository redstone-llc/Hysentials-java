package cc.woverflow.hysentials.utils

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UMessage
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import cc.woverflow.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.regex.Pattern
import kotlin.math.roundToInt

object ChatLib {
    /**
     * Prints text in the chat.
     * The text can be a String, a [Message] or a [TextComponent]
     *
     * @param text the text to be printed
     */
    @JvmStatic
    fun chat(text: Any) {
        when (text) {
            is String -> UMessage(text).chat()
            is UMessage -> text.chat()
            is UTextComponent -> text.chat()
            else -> UMessage(text.toString()).chat()
        }
    }

    /**
     * Shows text in the action bar.
     * The text can be a String, a [Message] or a [TextComponent]
     *
     * @param text the text to show
     */
    @JvmStatic
    fun actionBar(text: Any) {
        when (text) {
            is String -> UMessage(text).actionBar()
            is UMessage -> text.actionBar()
            is UTextComponent -> text.actionBar()
            else -> UMessage(text.toString()).actionBar()
        }
    }

    /**
     * Says chat message.
     * This message is actually sent to the server.
     *
     * @param text the message to be sent
     */
    @JvmStatic
    fun say(text: String) = Minecraft.getMinecraft().thePlayer?.sendChatMessage(text)

    /**
     * Runs a command.
     *
     * @param text the command to run, without the leading slash (Ex. "help")
     * @param clientSide should the command be ran as a client side command
     */
    @JvmOverloads
    @JvmStatic
    fun command(text: String, clientSide: Boolean = false) {
        if (clientSide) ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer, "/$text")
        else say("/$text")
    }

    /**
     * Gets the width of Minecraft's chat
     *
     * @return the width of chat
     */
    @JvmStatic
    fun getChatWidth(): Int {
        return Minecraft.getMinecraft().ingameGUI.chatGUI?.chatWidth ?: 0
    }

    /**
     * Remove all formatting
     *
     * @param text the string to un-format
     * @return the unformatted string
     */
    @JvmStatic
    fun removeFormatting(text: String): String {
        return text.replace("[\u00a7&][0-9a-fk-or]".toRegex(), "")
    }

    /**
     * Replaces Minecraft formatted text with normal formatted text
     *
     * @param text the formatted string
     * @return the unformatted string
     */
    @JvmStatic
    fun replaceFormatting(text: String): String {
        return text.replace("\u00a7(?![^0-9a-fk-or]|$)".toRegex(), "&")
    }

    /**
     * Get a message that will be perfectly centered in chat.
     *
     * @param text the text to be centered
     * @return the centered message
     */
    @JvmStatic
    fun getCenteredText(text: String): String {
        val textWidth = Renderer.getStringWidth(addColor(text))
        val chatWidth = getChatWidth()

        if (textWidth >= chatWidth)
            return text

        val spaceWidth = (chatWidth - textWidth) / 2f
        val spaceBuilder = StringBuilder().apply {
            repeat((spaceWidth / Renderer.getStringWidth(" ")).roundToInt()) {
                append(' ')
            }
        }

        return spaceBuilder.append(text).toString()
    }

    /**
     * Replaces the easier to type '&' color codes with proper color codes in a string.
     *
     * @param message The string to add color codes to
     * @return the formatted message
     */
    @JvmStatic
    fun addColor(message: String?): String {
        return message.toString().replace("(?<!\\\\)&(?![^0-9a-fk-or]|$)".toRegex(), "\u00a7")
    }
}