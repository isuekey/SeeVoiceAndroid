package com.futhark.android.seevoice.model.message

import java.security.MessageDigest
import kotlin.experimental.and

object MessageUtil {
  private const val FF: Byte = 0xFF.toByte()
  fun md5(message: String?) : String? {
    if (message == null) return null
    val md5Instance = MessageDigest.getInstance("MD5")
    md5Instance.update(message!!.toByteArray())
    val bytes = md5Instance.digest()
    val builder = StringBuilder()
    for (b in bytes) {
      b.toString()
      builder.append(String.format("%1$02s",(FF.and(b)).toString(16)))
    }
    return builder.toString()
  }
}