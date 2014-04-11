// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.view.View;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.runtime.util.ViewUtil;

/**
 * A container for components where their positions are described in relation
 * their parent.
 */

@SimpleObject
public class RelativeArrangement extends AndroidViewComponent implements Component, ComponentContainer {
  private final Activity context;
  private final RelativeLayout viewLayout;

  /**
   * Creates a new RelativeArrangement component.
   *
   * @param container  container, component will be placed in
  */
  public RelativeArrangement(ComponentContainer container) {
    super(container);
    context = container.$context();

    viewLayout = new RelativeLayout(context);
    container.$add(this);
  }

  @Override
  public Activity $context() {
    return context;
  }

  @Override
  public Form $form() {
    return container.$form();
  }

  @Override
  public void $add(AndroidViewComponent component) {
    viewLayout.add(component);
  }

  @Override
  public void setChildWidth(AndroidViewComponent component, int width) {
    ViewUtil.setChildWidthForRelativeLayout(component.getView(), width);
  }

  @Override
  public void setChildHeight(AndroidViewComponent component, int height) {
    ViewUtil.setChildHeightForRelativeLayout(component.getView(), height);
  }

  // AndroidViewComponent implementation

  @Override
  public View getView() {
    return viewLayout.getLayoutManager();
  }

}
