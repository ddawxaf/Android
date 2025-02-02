package com.youjie.cfcpnew.view.floatingball.menu;

import android.graphics.drawable.Drawable;

public abstract class MenuItem {
    /**
     * 菜单icon
     */
    Drawable mDrawable;

    public MenuItem(Drawable drawable) {
        this.mDrawable = drawable;
    }

    /**
     * 点击次菜单执行的操作
     */
    public abstract void action();
}
