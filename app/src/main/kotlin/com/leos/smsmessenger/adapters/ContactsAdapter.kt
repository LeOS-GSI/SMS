package com.leos.smsmessenger.adapters

import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.leos.commons.adapters.MyRecyclerViewAdapter
import com.leos.commons.extensions.beGoneIf
import com.leos.commons.extensions.getTextSize
import com.leos.commons.helpers.SimpleContactsHelper
import com.leos.commons.models.SimpleContact
import com.leos.commons.views.MyRecyclerView
import com.leos.smsmessenger.R
import com.leos.smsmessenger.activities.SimpleActivity
import com.leos.smsmessenger.extensions.config
import java.util.*

class ContactsAdapter(
    activity: SimpleActivity, var contacts: ArrayList<SimpleContact>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, itemClick) {
    private var fontSize = activity.getTextSize()

    override fun getActionMenuId() = 0

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = contacts.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = contacts.getOrNull(position)?.rawId

    override fun getItemKeyPosition(key: Int) = contacts.indexOfFirst { it.rawId == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createViewHolder(R.layout.item_contact_with_number, parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bindView(contact, true, false) { itemView, layoutPosition ->
            setupView(itemView, contact)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = contacts.size

    fun updateContacts(newContacts: ArrayList<SimpleContact>) {
        val oldHashCode = contacts.hashCode()
        val newHashCode = newContacts.hashCode()
        if (newHashCode != oldHashCode) {
            contacts = newContacts
            notifyDataSetChanged()
        }
    }

    private fun setupView(view: View, contact: SimpleContact) {
        view.apply {
            findViewById<TextView>(R.id.item_contact_name).apply {
                text = contact.name
                setTextColor(textColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 1.2f)
            }

            findViewById<TextView>(R.id.item_contact_number).apply {
                text = TextUtils.join(", ", contact.phoneNumbers.map { it.normalizedNumber })
                setTextColor(textColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
            }

            findViewById<ImageView>(R.id.item_contact_image).beGoneIf(!activity.config.showContactThumbnails)
            SimpleContactsHelper(context).loadContactImage(contact.photoUri, findViewById(R.id.item_contact_image), contact.name)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            Glide.with(activity).clear(holder.itemView.findViewById<ImageView>(R.id.item_contact_image))
        }
    }
}
