package com.futhark.android.seevoice.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.model.domain.ItemElement;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Home action adapter
 **/
public class HomeActionGridAdapter extends ArrayAdapter<ItemElement>{
    private LayoutInflater layoutInflater;
    public HomeActionGridAdapter(@NonNull Context context) {
        super(context, 0);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ItemHolder holder;
        if(v == null){
            v = this.layoutInflater.inflate(R.layout.item_of_home_action, parent, false);
            holder = new ItemHolder(v);
            v.setTag(holder);
        }else{
            holder = (ItemHolder)v.getTag();
        }
        holder.displayItem(getItem(position));
        return v;
    }



    class ItemHolder {
        @BindView(R.id.img_home_action_icon)
        ImageView itemIcon;
        @BindView(R.id.text_home_action_text)
        TextView itemText;
        private ItemElement itemElement;
        ItemHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
        void displayItem(ItemElement itemElement){
            this.itemElement = itemElement;
            this.itemIcon.setImageResource(itemElement.getSrc());
            this.itemText.setText(itemElement.getText());
        }
    }
}
