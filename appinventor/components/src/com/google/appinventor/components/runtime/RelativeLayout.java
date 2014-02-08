package com.google.appinventor.components.runtime;

import android.content.Context;
import android.view.ViewGroup;

public class RelativeLayout implements Layout {

  private final android.widget.RelativeLayout layoutManager;

  public RelativeLayout(Context context) {
    layoutManager = new android.widget.RelativeLayout(context);
  }

  @Override
  public ViewGroup getLayoutManager() {
    return layoutManager;
  }

  @Override
  public void add(AndroidViewComponent component) {
    layoutManager.addView(component.getView(), new android.widget.LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,  // width
            ViewGroup.LayoutParams.WRAP_CONTENT,  // height
            0f));                                 // weight
  }
}
