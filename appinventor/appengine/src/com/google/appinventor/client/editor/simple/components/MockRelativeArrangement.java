package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MockRelativeArrangement extends MockContainer {
  public static final String TYPE = "RelativeArrangement";

  // Form UI components
  protected final AbsolutePanel layoutWidget;

  /**
   * Creates a new MockHVArrangement component.
   */
  public MockRelativeArrangement(SimpleEditor editor) {
    // Note(Hal): This helper thing is a kludge because I really want to write:
    // myLayout = new MockHVLayout(orientation);
    // super(editor, type, icon, myLayout);
    // but Java won't let me do that.

    super(editor, TYPE, images.relative(), MockRelativeArrangementHelper.makeLayout());
    // Note(hal): There better not be any calls to MockHVArrangementHelper before the
    // next instruction.  Note that the Helper methods are synchronized to avoid possible
    // future problems if we ever have threads creating arrangements in parallel.
    rootPanel.setHeight("100%");

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
