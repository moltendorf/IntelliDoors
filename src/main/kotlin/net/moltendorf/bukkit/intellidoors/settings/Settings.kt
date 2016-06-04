package net.moltendorf.bukkit.intellidoors.settings

data class Settings(
  var interact: Boolean,
  var interactReset: Boolean,
  var interactResetTicks: Long,
  var redstone: Boolean,
  var redstoneReset: Boolean,
  var redstoneResetTicks: Long
)