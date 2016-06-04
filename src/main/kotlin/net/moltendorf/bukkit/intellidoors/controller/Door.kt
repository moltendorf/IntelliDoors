package net.moltendorf.bukkit.intellidoors.controller

import net.moltendorf.bukkit.intellidoors.IntelliDoors
import net.moltendorf.bukkit.intellidoors.Settings
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.BlockFace

/**
 * Created by moltendorf on 16/5/10.
 */
interface Door {
  val facing: BlockFace
  val location: Location
  val powered: Boolean
  val settings: Settings.TypeSettings
  val type: Material

  var open: Boolean

  fun toggle() {
    open = !open
  }

  fun resetIn(delay: Long): Boolean {
    val timer = IntelliDoors.instance.timer

    return if (open) {
      timer.shutDoorIn(this, delay)
      true
    } else {
      timer.cancel(this)
      false
    }
  }

  fun onRedstone(onDoor: Door): Boolean {
    return if (settings.singleRedstone) {
      if (settings.singleRedstoneReset && resetIn(settings.singleRedstoneResetTicks)) {
        return true
      }

      open = powered

      true
    } else {
      false
    }
  }

  fun playSound(open: Boolean) {
    location.world.playSound(location, sound(open), 1f, 1f)
  }

  fun onInteract(onDoor: Door): Boolean
  fun overrideOpen(value: Boolean)
  fun sound(open: Boolean): Sound

  interface Iron : Door {
    override fun onInteract(onDoor: Door): Boolean {
      if (settings.singleInteract) {
        playSound(!open)
        toggle()

        if (settings.singleInteractReset) {
          resetIn(settings.singleInteractResetTicks)
        }
      }

      return false
    }

    override fun sound(open: Boolean): Sound {
      return if (open) Sound.BLOCK_IRON_DOOR_OPEN else Sound.BLOCK_IRON_DOOR_CLOSE
    }
  }

  interface Wood : Door {
    override fun onInteract(onDoor: Door): Boolean {
      return if (settings.singleInteract) {
        overrideOpen(!open)
        toggle()

        if (settings.singleInteractReset) {
          resetIn(settings.singleInteractResetTicks)
        }

        false
      } else {
        true // Prevent door from opening.
      }
    }

    override fun sound(open: Boolean): Sound {
      return if (open) Sound.BLOCK_IRON_DOOR_OPEN else Sound.BLOCK_IRON_DOOR_CLOSE
    }
  }
}