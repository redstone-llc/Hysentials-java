package cc.woverflow.hysentials.gui

import cc.woverflow.hysentials.Hysentials
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.markdown.MarkdownComponent
import net.minecraft.client.gui.GuiMainMenu
import java.net.URL

class RequestUpdateGui : WindowScreen(ElementaVersion.V2, newGuiScale = 2, drawDefaultBackground = true) {

    init {
        val updateObj = UpdateChecker.updateGetter.updateObj ?: error("Update object is null")
//        UIImage.ofURL(URL("https://redstone.llc/apps/main/public/assets/img/uploads/729e713c6a2a972e6bd70774aa23a252.png")).constrain {
//            width = 100.percent()
//            height = 100.percent()
//        } childOf window
        UIText("${updateObj.name} is available!")
            .constrain {
                x = CenterConstraint()
                y = RelativeConstraint(0.1f)
            } childOf window
        val authorText = UIText("You are currently on version ${Hysentials.VERSION}.")
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint()
            } childOf window
        val changelogWrapper = ScrollComponent()
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint(10f)
                height = basicHeightConstraint { window.getHeight() - 90 - authorText.getBottom() }
                width = RelativeConstraint(0.7f)
            } childOf window
        MarkdownComponent("This will eventually be a changelog.")
            .constrain {
                height = RelativeConstraint()
                width = RelativeConstraint()
            }
            .childOf(changelogWrapper)
        SimpleButton("Update")
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint(5f)
                width = 100.pixels()
                height = 20.pixels()
            }.onMouseClick {
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(true))
            } childOf window
        SimpleButton("Update Later")
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint(5f)
                width = 100.pixels()
                height = 20.pixels()
            }.onMouseClick {
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(false))
            } childOf window
        SimpleButton("Main Menu")
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint(5f)
                width = 100.pixels()
                height = 20.pixels()
            }.onMouseClick {
                UpdateChecker.updateGetter.updateObj = null
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(GuiMainMenu())
            } childOf window
    }
}