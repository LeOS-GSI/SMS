package com.leos.smsmessenger.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.leos.commons.extensions.copyToClipboard
import com.leos.commons.extensions.notificationManager
import com.leos.commons.helpers.ensureBackgroundThread
import com.leos.smsmessenger.extensions.conversationsDB
import com.leos.smsmessenger.extensions.markThreadMessagesRead
import com.leos.smsmessenger.extensions.updateUnreadCountBadge
import com.leos.smsmessenger.helpers.*

class CopyNumberReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            COPY_NUMBER -> {
                val body = intent.getStringExtra(THREAD_TEXT)
                ensureBackgroundThread {
                    context.copyToClipboard(body!!)
                }
            }
        }
    }
}
