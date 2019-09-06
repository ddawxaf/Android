package com.gmcp.gm.ui.home.sidebar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmcp.gm.R;


public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private String[] groups;
    private String[][] children;

    public MyExpandableAdapter(Context context, String[] groups,
                               String[][] children) {
        this.mContext = context;
        this.groups = groups;
        this.children = children;

    }

    // 组的个数
    @Override
    public int getGroupCount() {

        return groups.length;
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    // 根据组的位置，组的成员个数
    @Override
    public int getChildrenCount(int groupPosition) {
        // 根据groupPosition获取某一个组的长度
        return children[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groups[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return children[groupPosition][childPosition].length();
    }



    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GpViewHolder gpViewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sidebar, null);

            gpViewHolder = new GpViewHolder();
            gpViewHolder.titleIcon = convertView.findViewById(R.id.titleIcon);
            gpViewHolder.img = convertView.findViewById(R.id.img);
            gpViewHolder.title = convertView
                    .findViewById(R.id.title);
            convertView.setTag(gpViewHolder);
        } else {
            gpViewHolder = (GpViewHolder) convertView.getTag();
        }
        onDrawable(gpViewHolder.titleIcon,groupPosition);
        if(isExpanded){
            gpViewHolder.img.setImageResource(R.mipmap.icon_ex_down);
        }else{
            gpViewHolder.img.setImageResource(R.mipmap.icon_ex__right);
        }
        if(groupPosition>3){
            gpViewHolder.img.setVisibility(View.GONE);
        }else{
            gpViewHolder.img.setVisibility(View.VISIBLE);
        }
        gpViewHolder.title.setText(groups[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        GpViewHolder gpViewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sidebar_child, null);
            gpViewHolder = new GpViewHolder();
            gpViewHolder.img = convertView
                    .findViewById(R.id.child_img);
            gpViewHolder.title = convertView
                    .findViewById(R.id.child_title);
            convertView.setTag(gpViewHolder);
        } else {
            gpViewHolder = (GpViewHolder) convertView.getTag();
        }
        gpViewHolder.img.setImageResource(R.drawable.dot_sidebar);
        gpViewHolder.title.setText(children[groupPosition][childPosition]);
        return convertView;
    }

    private void onDrawable(ImageView img ,int childPosition){

        switch (childPosition){
            case 0:
                img.setImageResource(R.mipmap.me_game_records);
                break;
            case 1:
                img.setImageResource(R.mipmap.sidebar_table_manage);
                break;
            case 2:
                img.setImageResource(R.mipmap.sidebar_account_manage);
                break;
            /*case 3:
                img.setImageResource(R.mipmap.sidebar_agency);
                break;*/
            case 3:
                img.setImageResource(R.mipmap.me_instation_infor);
                break;
            case 4:
                img.setImageResource(R.mipmap.sidebar_lottery);
                break;
            case 5:
                img.setImageResource(R.mipmap.sidebar_back_home);
                break;
            case 6:
                img.setImageResource(R.mipmap.me_logout);
                break;
            /*case 8:
                img.setImageResource(R.mipmap.me_logout);
                break;*/
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    static class GpViewHolder {
        public ImageView titleIcon;
        public ImageView img;
        TextView title;
    }

}