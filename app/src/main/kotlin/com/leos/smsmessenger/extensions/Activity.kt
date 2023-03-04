package com.leos.smsmessenger.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.leos.commons.dialogs.NewAppDialog
import com.leos.commons.extensions.*
import com.leos.commons.helpers.CONTACT_ID
import com.leos.commons.helpers.IS_PRIVATE
import com.leos.commons.helpers.SimpleContactsHelper
import com.leos.commons.helpers.ensureBackgroundThread
import com.leos.commons.models.SimpleContact
import com.leos.smsmessenger.R

fun Activity.dialNumber(phoneNumber: String, callback: (() -> Unit)? = null) {
    hideKeyboard()
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.fromParts("tel", phoneNumber, null)

        try {
            startActivity(this)
            callback?.invoke()
        } catch (e: ActivityNotFoundException) {
            toast(R.string.no_app_found)
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

// handle private contacts differently, only Goodwy Contacts can open them
fun Activity.startContactDetailsIntent(contact: SimpleContact) {
    val simpleContacts = "com.leos.contacts"
    val simpleContactsDebug = "com.leos.contacts.debug"
    if ((0..config.appRecommendationDialogCount).random() == 2 && (!isPackageInstalled(simpleContacts) && !isPackageInstalled(simpleContactsDebug))) {
        NewAppDialog(this, simpleContacts, getString(R.string.recommendation_dialog_contacts_g), getString(R.string.right_contacts),
            getDrawable(R.drawable.ic_launcher_contacts)) {}
    } else {
        if (contact.rawId > 1000000 && contact.contactId > 1000000 && contact.rawId == contact.contactId &&
            (isPackageInstalled(simpleContacts) || isPackageInstalled(simpleContactsDebug))
        ) {
            Intent().apply {
                action = Intent.ACTION_VIEW
                putExtra(CONTACT_ID, contact.rawId)
                putExtra(IS_PRIVATE, true)
                `package` = if (isPackageInstalled(simpleContacts)) simpleContacts else simpleContactsDebug
                setDataAndType(ContactsContract.Contacts.CONTENT_LOOKUP_URI, "vnd.android.cursor.dir/person")
                launchActivityIntent(this)
            }
        } else {
            ensureBackgroundThread {
                val lookupKey = SimpleContactsHelper(this).getContactLookupKey((contact).rawId.toString())
                val publicUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
                runOnUiThread {
                    launchViewContactIntent(publicUri)
                }
            }
        }
    }
}
