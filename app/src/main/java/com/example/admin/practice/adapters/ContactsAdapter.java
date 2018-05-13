package com.example.admin.practice.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.practice.ContactsItem;
import com.example.admin.practice.R;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {

    private ArrayList<ContactsItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ContactsItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contacts_list, parent, false);
        }

        //ImageView iv_image = (ImageView) convertView.findViewById(R.id.iv_img);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        //TextView tv_contents = (TextView) convertView.findViewById(R.id.tv_contents) ;
        ProgressBar pgb = (ProgressBar) convertView.findViewById(R.id.pgb);

        ContactsItem myItem = getItem(position);

        //iv_image.setImageBitmap(getAppIcon(myItem.getPhoto()));
        tv_name.setText(myItem.getName());
        //v_contents.setText(myItem.getPhone());
        pgb.setProgress(myItem.getPoint());

        return convertView;
    }

    public Bitmap getAppIcon(byte[] b){
        Bitmap bm = BitmapFactory.decodeByteArray(b,0,b.length);
        return bm;
    }

    public void addItem(ContactsItem mItem) {
        mItems.add(mItem);
    }
}
