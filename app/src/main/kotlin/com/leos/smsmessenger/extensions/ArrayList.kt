package com.leos.smsmessenger.extensions

import android.text.TextUtils
import com.leos.commons.models.SimpleContact

fun ArrayList<SimpleContact>.getThreadTitle() = TextUtils.join(", ", map { it.name }.toTypedArray())

fun ArrayList<SimpleContact>.getThreadSubtitle() = TextUtils.join(", ", map { it.phoneNumbers.first().normalizedNumber }.toTypedArray())
