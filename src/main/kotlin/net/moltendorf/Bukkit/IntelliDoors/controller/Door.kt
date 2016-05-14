package net.moltendorf.Bukkit.IntelliDoors.controller

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound

/**
 * Created by moltendorf on 16/5/10.
 */
abstract class Door {
  abstract val location: Location
  abstract val type: Material

  abstract var open: Boolean

  fun onInteract(onDoor: Door) {
    val isOpen = !open

    when (type) {
      Material.IRON_DOOR_BLOCK, Material.IRON_TRAPDOOR -> {
        location.world.playSound(location, sound(isOpen), 1f, 1f)

        open = isOpen
      }
    }
  }

  fun toggle() {
    open = !open
  }

  abstract fun overrideOpen(value: Boolean)
  abstract fun sound(open: Boolean): Sound
}