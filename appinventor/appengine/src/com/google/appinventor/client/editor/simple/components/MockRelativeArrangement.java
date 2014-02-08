package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MockRelativeArrangement extends MockContainer {
  public static String TYPE = "RelativeArrangement";

  protected final AbsolutePanel layoutWidget;

  /**
   * Creates a new MockHVArrangement component.
   */
  public MockRelativeArrangement(SimpleEditor editor) {
    super(editor, TYPE, images.horizontal(), MockHVArrangementHelper.makeLayout(ComponentConstants.LAYOUT_ORIENTATION_HORIZONTAL));

    // Note(hal): There better not be any calls to MockHVArrangementHelper before the
    // next instruction.  Note that the Helper methods are synchronized to avoid possible
    // future problems if we ever have threads creating arrangements in parallel.

    layoutWidget = new AbsolutePanel();
    layoutWidget.setStylePrimaryName("ode-SimpleMockContainer");
    layoutWidget.add(rootPanel);

    initComponent(layoutWidget);
  }


  @Override
  public void onPropertyChange(String propertyName, String newValue) {
    super.onPropertyChange(propertyName, newValue);
  }
}
