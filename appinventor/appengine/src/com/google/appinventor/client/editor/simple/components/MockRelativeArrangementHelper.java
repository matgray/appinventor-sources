package com.google.appinventor.client.editor.simple.components;

public class MockRelativeArrangementHelper {

  private static MockRelativeLayout saveLayout;

  public static synchronized MockRelativeLayout makeLayout() {
    saveLayout = new MockRelativeLayout();
    return saveLayout;
  }

  public static synchronized MockRelativeLayout getLayout() {
    return saveLayout;
  }
}
