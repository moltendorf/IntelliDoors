package net.moltendorf.Bukkit.IntelliDoors.controller

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

/**
 * Created by moltendorf on 16/5/2.
 */
abstract class AbstractDoor(val block: Block) : Door() {
  override val facing: BlockFace
    get() = FACING[data and 3]

  override val location: Location
    get() = block.location

  override val type: Material
    get() = block.type

  var data: Int = block.data.toInt()
    protected set

  override var open: Boolean
    get() {
      return data and 4 == 4
    }
    set(value) {
      if (open != value) {
        data += if (value) 4 else -4
        block.data = data.toByte()
      }
    }

  override fun overrideOpen(value: Boolean) {
    if (open != value) {
      data += if (value) 4 else -4
    }
  }
}
