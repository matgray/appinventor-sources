package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.components.common.ComponentConstants;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import java.util.Map;

public class MockRelativeLayout extends MockLayout {

  // Gap between adjacent components to allow for the insertion divider
  private static final int COMPONENT_SPACING = 5;
  private static final int SNAP_BUFFER = 20;

  // The color of the insertion divider
  private static final String DIVIDER_COLOR = "#0000ff";

  private static final int EMPTY_WIDTH = ComponentConstants.EMPTY_RELATIVE_ARRANGEMENT_WIDTH;
  private static final int EMPTY_HEIGHT = ComponentConstants.EMPTY_RELATIVE_ARRANGEMENT_HEIGHT;

  private Element verticalDividerElement; // lazily initialized
  private Element horizontalDividerElement; // lazily initialized

  /**
   * Creates a new relative layout with the specified orientation.
   */
  MockRelativeLayout() {
    layoutWidth = EMPTY_WIDTH;
    layoutHeight = EMPTY_HEIGHT;
  }

  private void ensureDividerInited() {

    if (verticalDividerElement == null) {
      verticalDividerElement = DOM.createDiv();
      DOM.setStyleAttribute(verticalDividerElement, "backgroundColor", DIVIDER_COLOR);
      setDividerVisible(verticalDividerElement, false);
      DOM.appendChild(container.getRootPanel().getElement(), verticalDividerElement);
    }

    if (horizontalDividerElement == null) {
      horizontalDividerElement = DOM.createDiv();
      DOM.setStyleAttribute(horizontalDividerElement, "backgroundColor", DIVIDER_COLOR);
      setDividerVisible(horizontalDividerElement, false);
      DOM.appendChild(container.getRootPanel().getElement(), horizontalDividerElement);
    }
  }

  private void setDividerLocation(int x, int y, boolean xFull, boolean yFull) {
    if (x == -1 || y == -1) {
      setDividerVisible(verticalDividerElement, false);
      setDividerVisible(horizontalDividerElement, false);
      return;
    }

    int containerHeight = container.getRootPanel().getOffsetHeight();
    int containerWidth = container.getRootPanel().getOffsetWidth();
    // Show Vertical placement
    if (yFull) {
      setDividerBoundsAndShow(
          verticalDividerElement,
          x, 0,
          COMPONENT_SPACING, containerHeight);
    } else {
      if (y < containerHeight / 2) {
        setDividerBoundsAndShow(
            verticalDividerElement,
            x, 0,
            COMPONENT_SPACING, y);
      } else {
        setDividerBoundsAndShow(
            verticalDividerElement,
            x, y,
            COMPONENT_SPACING, containerHeight);
      }
    }

    if (xFull) {
      setDividerBoundsAndShow(
          horizontalDividerElement,
          0, y,
          containerWidth, COMPONENT_SPACING);
    } else {
      // Show Horizontal Placement
      if (x < containerWidth / 2) {
        setDividerBoundsAndShow(
            horizontalDividerElement,
            0, y,
            x, COMPONENT_SPACING);
      } else {
        setDividerBoundsAndShow(
            horizontalDividerElement,
            x, y,
            containerWidth, COMPONENT_SPACING);
      }
    }
  }

  private void setDividerVisible(Element element, boolean visible) {
    DOM.setStyleAttribute(element, "visibility", visible ? "visible" : "hidden");
  }

  private void setDividerBoundsAndShow(Element element, int x, int y, int width, int height) {
    DOM.setStyleAttribute(element, "position", "absolute");
    DOM.setStyleAttribute(element, "left", x + "px");
    DOM.setStyleAttribute(element, "top", y + "px");
    DOM.setStyleAttribute(element, "width", width + "px");
    DOM.setStyleAttribute(element, "height", height + "px");
    setDividerVisible(element, true);
  }

  @Override
  LayoutInfo createContainerLayoutInfo(Map<MockComponent, LayoutInfo> layoutInfoMap) {
    ensureDividerInited();

    return new LayoutInfo(layoutInfoMap, container) {
      @Override
      void calculateAndStoreAutomaticWidth() {
          for (MockComponent child : visibleChildren) {
            LayoutInfo childLayoutInfo = layoutInfoMap.get(child);
            if (childLayoutInfo.width == MockVisibleComponent.LENGTH_FILL_PARENT) {
              childLayoutInfo.calculateAndStoreAutomaticWidth();
            }
          }
        super.calculateAndStoreAutomaticWidth();
      }

      @Override
      void calculateAndStoreAutomaticHeight() {
          for (MockComponent child : visibleChildren) {
            LayoutInfo childLayoutInfo = layoutInfoMap.get(child);
            if (childLayoutInfo.height == MockVisibleComponent.LENGTH_FILL_PARENT) {
              childLayoutInfo.calculateAndStoreAutomaticHeight();
            }
          }
        super.calculateAndStoreAutomaticHeight();
      }

      @Override
      int calculateAutomaticWidth() {
          return EMPTY_WIDTH;
      }

      @Override
      int calculateAutomaticHeight() {
          return EMPTY_HEIGHT;
      }
    };
  }

  @Override
  void layoutChildren(LayoutInfo containerLayoutInfo) {
    int visibleChildrenSize = containerLayoutInfo.visibleChildren.size();
    if (visibleChildrenSize > 0) {
      layoutComponents(containerLayoutInfo);
    } else {
      layoutWidth = EMPTY_WIDTH;
      layoutHeight = EMPTY_HEIGHT;
    }
  }

  private void layoutComponents(LayoutInfo containerLayoutInfo) {
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      container.setChildSizeAndPosition(child, childLayoutInfo, child.getTop(), child.getLeft());
    }
  }

  @Override
  void onDragContinue(MockComponent source, int x, int y, int sourceLeft, int sourceTop) {
    Style style = source.getElement().getStyle();

    // This will be true only if the component has already been added to the screen
    if (style.getWidth().length() != 0 && style.getHeight().length() != 0) {
      x = (int) (sourceLeft + (parseSourceStyle(style.getWidth()) / 2));
      y = (int) (sourceTop + (parseSourceStyle(style.getHeight()) / 2));
    }

    int containerWidth = container.getRootPanel().getOffsetWidth();
    int containerHeight = container.getRootPanel().getOffsetHeight();
    boolean yFill = false;
    boolean xFill = false;

    if (Math.abs(y - (containerHeight / 2)) < SNAP_BUFFER) {
      y = containerHeight / 2;
      xFill = true;
    }
    if (Math.abs(x - (containerWidth / 2)) < SNAP_BUFFER) {
      x = containerWidth / 2;
      yFill = true;
    }

    setDividerLocation(x, y, xFill, yFill);
  }

  @Override
  void onDragLeave() {
    // Hide the divider and clean up
    setDividerLocation(-1, -1, false, false);
  }

  float parseSourceStyle(String sourceStyle) {
    try {
      // -2 because of trailint 'px'
      return Float.parseFloat(sourceStyle.substring(0, sourceStyle.length() - 2));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  @Override
  boolean onDrop(MockComponent source, int x, int y, int offsetX, int offsetY) {
    if (x != -1 && y != -1) {

      // Hide the divider.
      setDividerLocation(-1, -1, false, false);

      // Calculate drop information
      MockContainer srcContainer = source.getContainer();
      MockContainer dstContainer = container;

      // Perform drop
      if (srcContainer != null) {
        // Pass false to indicate that the component isn't being permanently deleted.
        // It's just being moved from one container to another.
        srcContainer.removeComponent(source, false);
      }

      Style style = source.getElement().getStyle();

      if (style.getWidth().length() != 0) {
        float dropTop = parseSourceStyle(DOM.getStyleAttribute(verticalDividerElement, "left"));
        float dropLeft = parseSourceStyle(DOM.getStyleAttribute(horizontalDividerElement, "top"));

        float offsetWidth = parseSourceStyle(style.getWidth()) / 2;
        float offsetHeight = parseSourceStyle(style.getHeight()) / 2;

        dstContainer.addVisibleComponentAtAbsolutePosition(source, (int) (dropTop - offsetWidth), (int) (dropLeft - offsetHeight));
      } else {
        dstContainer.addVisibleComponentAtAbsolutePosition(source, x - offsetX, y - offsetY);
      }
      return true;
    }
    return false;
  }

  @Override
  void dispose() {
    if (verticalDividerElement != null) {
      DOM.removeChild(container.getRootPanel().getElement(), verticalDividerElement);
    }
    if (horizontalDividerElement != null) {
      DOM.removeChild(container.getRootPanel().getElement(), horizontalDividerElement);
    }
  }
}

