package com.futhark.android.seevoice.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.model.domain.ItemElement

import butterknife.BindView
import butterknife.ButterKnife


/**
 * Home action adapter
 */
class HomeActionGridAdapter(context: Context) : ArrayAdapter<ItemElement>(context, 0) {
    private val layoutInflater: LayoutInflater

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        val holder: ItemHolder
        if (v == null) {
            v = this.layoutInflater.inflate(R.layout.item_of_home_action, parent, false)
            holder = ItemHolder(v)
            v!!.tag = holder
        } else {
            holder = v.tag as ItemHolder
        }
        holder.displayItem(getItem(position))
        return v
    }


    internal inner class ItemHolder(itemView: View) {
        @BindView(R.id.img_home_action_icon)
        var itemIcon: ImageView? = null
        @BindView(R.id.text_home_action_text)
        var itemText: TextView? = null
        private var itemElement: ItemElement? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun displayItem(itemElement: ItemElement?) {
            this.itemElement = itemElement
            this.itemIcon!!.setImageResource(itemElement!!.src)
            this.itemText!!.setText(itemElement.text)
        }
    }
}
