// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime;

import android.content.Context;
import android.view.ViewGroup;
import com.google.appinventor.components.annotations.SimpleObject;

/**
 * A Layout component where the positions of the children can be described in
 * relation to the parent.
 */
@SimpleObject
public class RelativeLayout implements Layout {

  private final android.widget.RelativeLayout layoutManager;

  /**
   * Creates a new relative layout.
   *
   * @param context  view context
   */
  RelativeLayout(Context context) {
    layoutManager = new android.widget.RelativeLayout(context);
  }

  public ViewGroup getLayoutManager() {
    return layoutManager;
  }

  public void add(AndroidViewComponent component) {
    layoutManager.addView(component.getView(),
        new android.widget.RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,  // width
            ViewGroup.LayoutParams.WRAP_CONTENT)); // height
  }

  public void addWithParams(AndroidViewComponent component,
                            android.widget.RelativeLayout.LayoutParams params) {
    layoutManager.addView(component.getView(), params);
  }
}
