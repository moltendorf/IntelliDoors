package net.moltendorf.Bukkit.IntelliDoors.listener

import net.moltendorf.Bukkit.IntelliDoors.IntelliDoors
import net.moltendorf.Bukkit.IntelliDoors.Settings
import net.moltendorf.Bukkit.IntelliDoors.controller.DoubleDoor
import net.moltendorf.Bukkit.IntelliDoors.controller.FenceGate
import net.moltendorf.Bukkit.IntelliDoors.controller.SingleDoor
import net.moltendorf.Bukkit.IntelliDoors.controller.TrapDoor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Created by moltendorf on 15/05/23.

 * @author moltendorf
 */
class Interact() : Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  fun PlayerInteractEventHandler(event: PlayerInteractEvent) {
    if (!IntelliDoors.enabled) {
      return
    }

    if (event.action == Action.RIGHT_CLICK_BLOCK) {
      val instance = IntelliDoors.instance
      val block = event.clickedBlock
      val material = block.type
      val settings = instance.settings[material] ?: return
      val type = settings.type
      val timer = instance.timer

      val door = when (type) {
        Settings.Type.DOOR -> {
          val singleDoor = SingleDoor[block] ?: return

          if (settings.pairInteract && settings.pairInteractSync) {
            val doubleDoor = DoubleDoor[singleDoor]

            if (doubleDoor != null) {
              doubleDoor.onInteract(singleDoor)

              if (doubleDoor.open && settings.pairInteractReset) {
                timer.shutDoorIn(doubleDoor, settings.pairInteractResetTicks)
              }

              return
            }
          }

          singleDoor
        }

        Settings.Type.TRAP -> TrapDoor[block]
        Settings.Type.GATE -> FenceGate[block]
      } ?: return

      if (settings.singleInteract) {
        door.onInteract(door)

        if (door.open && settings.singleInteractReset) {
          timer.shutDoorIn(door, settings.singleInteractResetTicks)
        }
      } else {
        when (material) {
          Material.IRON_DOOR_BLOCK, Material.IRON_TRAPDOOR -> {
            // Do nothing as it won't open anyway.
          }
          else -> event.isCancelled = true // Prevent door from opening.
        }
      }
    }
  }
}
