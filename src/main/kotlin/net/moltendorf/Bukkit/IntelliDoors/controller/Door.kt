package net.moltendorf.Bukkit.IntelliDoors.controller

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.BlockFace

/**
 * Created by moltendorf on 16/5/10.
 */
abstract class Door {
  abstract val facing: BlockFace
  abstract val location: Location
  abstract val type: Material

  abstract var open: Boolean

  open fun onInteract(onDoor: Door) {
    val isOpen = !open

    when (type) {
      Material.IRON_DOOR_BLOCK, Material.IRON_TRAPDOOR -> {
        location.world.playSound(location, sound(isOpen), 1f, 1f)

        open = isOpen
      }
      else -> overrideOpen(isOpen)
    }
  }

  fun toggle() {
    open = !open
  }

  abstract fun overrideOpen(value: Boolean)
  abstract fun sound(open: Boolean): Sound

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }

    if (other?.javaClass != javaClass) {
      return false
    }

    other as Door

    if (location != other.location) {
      return false
    }

    return true
  }

  override fun hashCode(): Int {
    return location.hashCode()
  }

  companion object {
    val FACING = arrayOf(BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST)
  }
}
