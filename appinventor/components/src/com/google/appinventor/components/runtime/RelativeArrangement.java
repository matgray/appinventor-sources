package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.view.View;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;

@DesignerComponent(version = YaVersion.RELATIVEARRANGEMENT_COMPONENT_VERSION,
        description = "<p>A view group that displays child views in relative positions.</p>",
        category = ComponentCategory.LAYOUT)
@SimpleObject
public class RelativeArrangement extends AndroidViewComponent implements Component, ComponentContainer {

  private final Activity context;
  private final RelativeLayout viewLayout;

  /**
   * Creates a new Relative Arrangement
   *
   * @param container container, component will be placed in
   */
  public RelativeArrangement(ComponentContainer container) {
    super(container);
    context = container.$context();
    viewLayout = new RelativeLayout(context);
    container.$add(this);
  }

  @Override
  public View getView() {
    return viewLayout.getLayoutManager();
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
    System.out.println("relative-setChildWidth");
  }

  @Override
  public void setChildHeight(AndroidViewComponent component, int height) {
    System.out.println("relative-setChildHeight");
  }

  @SimpleProperty(
          category = PropertyCategory.APPEARANCE,
          description = "A number that encodes how contents of the arrangement are aligned " +
                  " horizontally. The choices are: 1 = left aligned, 2 = horizontally centered, " +
                  " 3 = right aligned.  Alignment has no effect if the arrangement's width is " +
                  "automatic.")
  public int RelativeAlign() {
    return 1;
  }
}
