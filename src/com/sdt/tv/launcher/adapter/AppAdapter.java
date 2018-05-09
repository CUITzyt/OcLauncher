
package com.sdt.tv.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import com.sdt.tv.launcher.R;
import com.sdt.tv.launcher.fragment.AppFragment;
import com.sdt.tv.launcher.model.AppBean;
import com.sdt.tv.launcher.model.AppModel;

/**
 * AppFragment adapter
 *
 * @author jacky
 * @version 1.0
 * @since 2016.5.10
 */
public class AppAdapter extends BaseAdapter {

    private List<AppModel> mAppBeanList;
    private Context mContext;
    public Holder mHolder;
    private AppFragment mAppFragment;

    private int[] drawableIds = { R.drawable.app_blue, R.drawable.app_green, R.drawable.app_jasper,
            R.drawable.app_lawngreen, R.drawable.app_red, R.drawable.app_yellow };

    public AppAdapter(AppFragment appFragment, Context context, List<AppModel> appBeanList) {
        mAppFragment = appFragment;
        mContext = context;
        mAppBeanList = appBeanList;
    }

    @Override
    public int getCount() {
        return mAppBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_app, null);
            mHolder.name = (TextView) convertView.findViewById(R.id.item_app_name);
            mHolder.icon = (ImageView) convertView.findViewById(R.id.item_app_icon);
            mHolder.bg = convertView.findViewById(R.id.item_app_bg);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        Random random = new Random();
        mHolder.bg.setBackgroundResource(drawableIds[random.nextInt(6)]);
        AppModel appModel = mAppBeanList.get(position);
        mHolder.icon.setImageDrawable(appModel.getIcon());
        mHolder.name.setText(appModel.getName());

        final int pos = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = mContext.getPackageManager()
                        .getLaunchIntentForPackage(mAppBeanList.get(pos).getPackageName());
                if (launchIntent != null) {
                    mContext.startActivity(launchIntent);
                }
            }
        });
        return convertView;
    }

    public class Holder {
        private TextView name;
        private ImageView icon;
        private View bg;
    }
}
