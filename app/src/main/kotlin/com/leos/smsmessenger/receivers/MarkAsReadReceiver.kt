package com.leos.smsmessenger.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.leos.commons.extensions.notificationManager
import com.leos.commons.helpers.ensureBackgroundThread
import com.leos.smsmessenger.extensions.conversationsDB
import com.leos.smsmessenger.extensions.markThreadMessagesRead
import com.leos.smsmessenger.extensions.updateUnreadCountBadge
import com.leos.smsmessenger.helpers.MARK_AS_READ
import com.leos.smsmessenger.helpers.THREAD_ID
import com.leos.smsmessenger.helpers.refreshMessages

class MarkAsReadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MARK_AS_READ -> {
                val threadId = intent.getLongExtra(THREAD_ID, 0L)
                context.notificationManager.cancel(threadId.hashCode())
                ensureBackgroundThread {
                    context.markThreadMessagesRead(threadId)
                    context.conversationsDB.markRead(threadId)
                    context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
                    refreshMessages()
                }
            }
        }
    }
}
