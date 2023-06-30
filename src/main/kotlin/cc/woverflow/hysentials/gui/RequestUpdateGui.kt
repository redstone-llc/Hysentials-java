package cc.woverflow.hysentials.gui

import cc.woverflow.hysentials.Hysentials
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.basicHeightConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.markdown.MarkdownComponent
import net.minecraft.client.gui.GuiMainMenu

class RequestUpdateGui : WindowScreen(ElementaVersion.V2, newGuiScale = 2) {

    init {
        val updateObj = UpdateChecker.updateGetter.updateObj ?: error("Update object is null")
        UIText("Skytils ${updateObj.tagName} is available!")
            .constrain {
                x = CenterConstraint()
                y = RelativeConstraint(0.1f)
            } childOf window
        UIText("You are currently on version ${Hysentials.VERSION}.")
            .constrain {
                x = CenterConstraint()
                y = SiblingConstraint()
            } childOf window
        val authorText =
            UIText("Uploaded by: ${UpdateChecker.updateAsset.uploader.username}")
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
        MarkdownComponent(updateObj.body.replace("*", ""))
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