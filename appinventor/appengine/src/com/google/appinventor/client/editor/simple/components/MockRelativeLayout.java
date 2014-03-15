package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import java.util.Map;

public class MockRelativeLayout extends MockLayout {

  // Gap between adjacent components to allow for the insertion divider
  private static final int COMPONENT_SPACING = 5;

  // The color of the insertion divider
  private static final String DIVIDER_COLOR = "#0000ff";

  private static final int EMPTY_WIDTH = ComponentConstants.EMPTY_HV_ARRANGEMENT_WIDTH;
  private static final int EMPTY_HEIGHT = ComponentConstants.EMPTY_HV_ARRANGEMENT_HEIGHT;

  protected final int orientation;

  // The DIV element that displays the insertion divider.
  // Is added to the root panel of the associated container.
  private Element verticalDividerElement; // lazily initialized
  private Element horizontalDividerElement; // lazily initialized

  // constants to indicate horizontal and vertical alignment
  private enum HorizontalAlignment {
    Left, Center, Right
  }

  ;
  private HorizontalAlignment alignH;

  private enum VerticalAlignment {Top, Center, Bottom}

  ;
  private VerticalAlignment alignV;


  /**
   * Creates a new linear layout with the specified orientation.
   */
  MockRelativeLayout() {
    orientation = ComponentConstants.LAYOUT_ORIENTATION_HORIZONTAL;
    layoutWidth = EMPTY_WIDTH;
    layoutHeight = EMPTY_HEIGHT;
//    dividerPos = -1;

    // These initial values are assuming that the default values in ComponentConstants are
    // defined as LEFT and TOP
    alignH = HorizontalAlignment.Left;
    alignV = VerticalAlignment.Top;
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

  private void setDividerLocation(int x, int y) {
    if (x == -1 || y == -1) {
      setDividerVisible(verticalDividerElement, false);
      setDividerVisible(horizontalDividerElement, false);
      return;
    }

    int containerHeight = container.getRootPanel().getOffsetHeight();
    int containerWidth = container.getRootPanel().getOffsetWidth();
    // Show Vertical placement
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

  // MockLayout methods

  // NOTE(lizlooney) - layout behavior:

  // The Screen component has a Scrollable property. When the Scrollable property is checked, the
  // Screen component behaves like a VerticalArrangement whose Height property is set to Automatic.
  // When the Scrollable property is not checked, the Screen component behaves like a
  // VerticalArrangement whose Height property is specified in pixels.

  // In a VerticalArrangement, components are arranged along the vertical axis, left-aligned.
  // If a VerticalArrangement's Width property is set to Automatic, the actual width of the
  // arrangement is determined by the widest component whose Width property is not set to Fill
  // Parent. If a VerticalArrangement's Width property is set to Automatic and it contains only
  // components whose Width properties are set to Fill Parent, the actual width of the
  // arrangement is calculated using the automatic widths of the components. If a
  // VerticalArrangement's Width property is set to Automatic and it is empty, the width will be
  // 100.
  // If a VerticalArrangement's Height property is set to Automatic, the actual height of the
  // arrangement is determined by the sum of the heights of the components. If a
  // VerticalArrangement's Height property is set to Automatic, any components whose Height
  // properties are set to Fill Parent will behave as if they were set to Automatic.
  // If a VerticalArrangement's Height property is set to Fill Parent or specified in pixels, any
  // components whose Height properties are set to Fill Parent will equally take up the height not
  // occupied by other components.

  // In a HorizontalArrangement, components are arranged along the horizontal axis, vertically
  // center-aligned, even if the vertical aligned is "top", in which case they are
  // center-aligned at the top of the arrangement.  This is unlike VerticalArrangement, where
  // the components are flush-left at the left of the arrangement if the horizontal alignment
  // is "left".

  // If a HorizontalArrangement's Height property is set to Automatic, the actual height of the
  // arrangement is determined by the tallest component whose Height property is not set to Fill
  // Parent. If a HorizontalArrangement's Height property is set to Automatic and it contains only
  // components whose Height properties are set to Fill Parent, the actual height of the
  // arrangement is calculated using the automatic heights of the components. If a
  // HorizontalArrangement's Height property is set to Automatic and it is empty, the height will be
  // 100.
  // If a HorizontalArrangement's Width property is set to Automatic, the actual width of the
  // arrangement is determined by the sum of the widths of the components. If a
  // HorizontalArrangement's Width property is set to Automatic, any components whose Width
  // properties are set to Fill Parent will behave as if they were set to Automatic.
  // If a HorizontalArrangement's Width property is set to Fill Parent or specified in pixels, any
  // components whose Width properties are set to Fill Parent will equally take up the width not
  // occupied by other components.

  @Override
  LayoutInfo createContainerLayoutInfo(Map<MockComponent, LayoutInfo> layoutInfoMap) {
    ensureDividerInited();

    return new LayoutInfo(layoutInfoMap, container) {
      @Override
      void calculateAndStoreAutomaticWidth() {
        if (orientation == ComponentConstants.LAYOUT_ORIENTATION_HORIZONTAL) {
          // In a HorizontalArrangement whose width is automatic, a child whose width is fill
          // parent will behave as if it were automatic.
          for (MockComponent child : visibleChildren) {
            LayoutInfo childLayoutInfo = layoutInfoMap.get(child);
            if (childLayoutInfo.width == MockVisibleComponent.LENGTH_FILL_PARENT) {
              childLayoutInfo.calculateAndStoreAutomaticWidth();
            }
          }
        }
        super.calculateAndStoreAutomaticWidth();
      }

      @Override
      void calculateAndStoreAutomaticHeight() {
        if (orientation == ComponentConstants.LAYOUT_ORIENTATION_VERTICAL) {
          // In a VerticalArrangement whose height is automatic, a child whose height is fill
          // parent will behave as if it were automatic.
          for (MockComponent child : visibleChildren) {
            LayoutInfo childLayoutInfo = layoutInfoMap.get(child);
            if (childLayoutInfo.height == MockVisibleComponent.LENGTH_FILL_PARENT) {
              childLayoutInfo.calculateAndStoreAutomaticHeight();
            }
          }
        }
        super.calculateAndStoreAutomaticHeight();
      }

      @Override
      int calculateAutomaticWidth() {
        if (visibleChildren.isEmpty()) {
          return EMPTY_WIDTH;
        }

        if (orientation == ComponentConstants.LAYOUT_ORIENTATION_VERTICAL) {
          return calculateAutomaticWidthVertical(this);
        } else {
          return calculateAutomaticWidthHorizontal(this);
        }
      }

      @Override
      int calculateAutomaticHeight() {
        if (visibleChildren.isEmpty()) {
          return EMPTY_HEIGHT;
        }

        if (orientation == ComponentConstants.LAYOUT_ORIENTATION_VERTICAL) {
          return calculateAutomaticHeightVertical(this);
        } else {
          return calculateAutomaticHeightHorizontal(this);
        }
      }
    };
  }

  @Override
  void layoutChildren(LayoutInfo containerLayoutInfo) {
    int visibleChildrenSize = containerLayoutInfo.visibleChildren.size();
    if (visibleChildrenSize > 0) {
      layoutHorizontal(containerLayoutInfo);
    } else {
      layoutWidth = EMPTY_WIDTH;
      layoutHeight = EMPTY_HEIGHT;
    }
  }

  private int calculateAutomaticWidthVertical(LayoutInfo containerLayoutInfo) {
    // The width will be the widest child, ignoring any child's width that is fill parent.
    boolean allFillParent = true;
    int width = 0;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      if (childLayoutInfo.width != MockVisibleComponent.LENGTH_FILL_PARENT) {
        width = Math.max(width, childLayoutInfo.width + BORDER_SIZE);
        allFillParent = false;
      }
    }
    // If all children have widths that are fill parent, find the widest child using the
    // automatic widths of the children.
    if (allFillParent) {
      for (MockComponent child : containerLayoutInfo.visibleChildren) {
        LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
        int childWidth = childLayoutInfo.calculateAutomaticWidth();
        width = Math.max(width, childWidth + BORDER_SIZE);
      }
    }
    return width;
  }

  private int calculateAutomaticHeightVertical(LayoutInfo containerLayoutInfo) {
    // The height will be the sum of the child heights.
    int height = 0;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      height += COMPONENT_SPACING;
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      // If the height is fill parent, use automatic height.
      int childHeight = (childLayoutInfo.height == MockVisibleComponent.LENGTH_FILL_PARENT)
          ? childLayoutInfo.calculateAutomaticHeight()
          : childLayoutInfo.height;
      height += childHeight + BORDER_SIZE;
    }
    height += COMPONENT_SPACING;
    return height;
  }

  private int calculateAutomaticHeightHorizontal(LayoutInfo containerLayoutInfo) {
    // The height will be the tallest child, ignoring any child's height that is fill parent.
    boolean allFillParent = true;
    int height = 0;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      if (childLayoutInfo.height != MockVisibleComponent.LENGTH_FILL_PARENT) {
        height = Math.max(height, childLayoutInfo.height + BORDER_SIZE);
        allFillParent = false;
      }
    }
    // If all children have heights that are fill parent, find the tallest child using the
    // automatic heights of the children.
    if (allFillParent) {
      for (MockComponent child : containerLayoutInfo.visibleChildren) {
        LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
        int childHeight = childLayoutInfo.calculateAutomaticHeight();
        height = Math.max(height, childHeight + BORDER_SIZE);
      }
    }
    return height;
  }

  // TODO: (hal)  This next method is incorrect because the width need to be constrained by
  // the room remaining in the parent container.  The overall automatic layout algorithms
  // need to be reviewed.

  private int calculateAutomaticWidthHorizontal(LayoutInfo containerLayoutInfo) {
    // The width will be the sum of the child widths.
    int width = 0;
    boolean firstChild = true;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      // add spacing, but not before first child
      if (firstChild) {
        firstChild = false;
      } else {
        width += COMPONENT_SPACING;
      }
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      // If the width is fill parent, use automatic width.
      int childWidth = (childLayoutInfo.width == MockVisibleComponent.LENGTH_FILL_PARENT)
          ? childLayoutInfo.calculateAutomaticWidth()
          : childLayoutInfo.width;
      width += childWidth + BORDER_SIZE;
    }
    return width;
  }

  // TODO(hal): Check this to see if we're putting the component vertical spacing in the right
  // places and think about rewriting this to match how layoutHorizontal handles width.

  private void layoutHorizontal(LayoutInfo containerLayoutInfo) {
    // Children are arranged along the horizontal axis.   They are
    // and vertically center-aligned at the top of the arrangement by default, and at vertical
    // center of the arrangement if vertical contentCentering is specified.
    // Horizontally, they can be left-aligned, centered, or right-aligned.
    int usedWidth = 0;
    boolean firstChild = true;
    int countFillParent = 0;

    // Calculate the width used up by children whose width is not fill parent.
    // Include spacing between the children
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      // add spacing, but not before first child
      if (firstChild) {
        firstChild = false;
      } else {
        usedWidth += COMPONENT_SPACING;
      }
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      if (childLayoutInfo.width == MockVisibleComponent.LENGTH_FILL_PARENT) {
        countFillParent++;
      } else {
        usedWidth += childLayoutInfo.width + BORDER_SIZE;
      }
    }

    // The remaining width, after allocating horizontal space for the
    // contents, not counting the width of fill-parent children
    // If there are fill-parent children, this remaining width will be
    // divided equally among them.
    int remainingWidth = containerLayoutInfo.width - usedWidth;
    if (remainingWidth < 0) {
      remainingWidth = 0;
    }

    // The final remaining width after all children have been accounted for.  This will be 0 if
    // any of the children have width fill-parent
    int finalRemainingWidth = remainingWidth;

    // Resolve any child's width or height that is fill parent, and call layoutChildren for
    // children that are containers.
    // Figure out the height of the largest child so we can middle-align all the children.
    // Note that layoutVertical does not do the analogous center aligning unless horizontal
    // centering is explicitly specified.
    int maxHeight = 0;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      if (childLayoutInfo.width == MockVisibleComponent.LENGTH_FILL_PARENT) {
        // at this point, remaining width is too small and gets smaller if there
        // are more components
        childLayoutInfo.width = remainingWidth / countFillParent - BORDER_SIZE;
        // if any component has width fill parent then there will be no final remaining width
        finalRemainingWidth = 0;
      }
      if (childLayoutInfo.height == MockVisibleComponent.LENGTH_FILL_PARENT) {
        childLayoutInfo.height = containerLayoutInfo.height - BORDER_SIZE;
      }

      maxHeight = Math.max(maxHeight, childLayoutInfo.height + BORDER_SIZE);

      // If the child is a container then call layoutChildren for it.
      if (child instanceof MockContainer) {
        ((MockContainer) child).getLayout().layoutChildren(childLayoutInfo);
      }
    }

    // centerY is where the middle of each child should be: either vertically
    // centered at the top of the arrangement, or each child at the middle of the
    // arrangement.

    //we have to initialize this, or else Eclipse will whine at us
    int centerY = 0;

    switch (alignV) {
      case Top:
        centerY = maxHeight / 2;
        break;
      case Center:
        centerY = containerLayoutInfo.height / 2;
        break;
      case Bottom:
        centerY = containerLayoutInfo.height - (maxHeight / 2);
      default:
        OdeLog.elog("System error: Bad value for vertical alignment -- MockHVLayoutBase");
    }


    // NOTE(hal) What is this for?
    layoutHeight = 0;

    // Now we've computed the actual widths of the components, so we can lay them out.

    // leftX is where the left edge of the next child should be.  For a horizontal alignment
    // leftX starts out either at zero (left align) or set so the center of the arrangement
    // is at the center of the layout (center-align) or set so the right edge of the arrangement
    // child is at the right edge of the layout (right-align).
    int leftX = 0;
    switch (alignH) {
      case Left:
        leftX = 0;
        break;
      case Center:
        leftX = finalRemainingWidth / 2;
        break;
      case Right:
        leftX = finalRemainingWidth;
        break;
      default:
        OdeLog.elog("System error: Bad value for horizontal justification -- MockHVLayoutBase");
    }

    // Position each child and update layoutWidth and layoutHeight.
    firstChild = true;
    for (MockComponent child : containerLayoutInfo.visibleChildren) {
      // add spacing, but not before first child
      if (firstChild) {
        firstChild = false;
      } else {
        leftX += COMPONENT_SPACING;
      }
      LayoutInfo childLayoutInfo = containerLayoutInfo.layoutInfoMap.get(child);
      int childWidthWithBorder = childLayoutInfo.width + BORDER_SIZE;
      int childHeightWithBorder = childLayoutInfo.height + BORDER_SIZE;

      // The middle of the child needs to be at centerY, so the
      // top of the child is above that by childHeightWithBorder/2
      int topY = centerY - (childHeightWithBorder / 2);

      container.setChildSizeAndPosition(child, childLayoutInfo, child.getTop(), child.getLeft());
      layoutHeight = Math.max(layoutHeight, topY + childHeightWithBorder);
      leftX += childWidthWithBorder;
    }
    layoutWidth = leftX;
  }

  @Override
  void onDragContinue(MockComponent source, int x, int y, int offsetX, int offsetY) {
    Style style = source.getElement().getStyle();
    x = x - offsetX + (parseSourceStyle(style.getWidth()) / 2);
    y = y - offsetY + (parseSourceStyle(style.getHeight()) / 2);

    int containerWidth = container.getRootPanel().getOffsetWidth();
    int containerHeight = container.getRootPanel().getOffsetHeight();

    if (Math.abs(y - (containerHeight / 2)) < 20) {
      y = containerHeight / 2;
    }
    if (Math.abs(x - (containerWidth / 2)) < 20) {
      x = containerWidth / 2;
    }
    setDividerLocation(x, y);
  }

  @Override
  void onDragLeave() {
    // Hide the divider and clean up
    setDividerLocation(-1, -1);
  }

  int parseSourceStyle(String sourceStyle) {
    // -2 because of trailint 'px'
    return (int) Float.parseFloat(sourceStyle.substring(0, sourceStyle.length() - 2));
  }

  @Override
  boolean onDrop(MockComponent source, int x, int y, int offsetX, int offsetY) {
    if (x != -1 && x != -1) {

      // Hide the divider.
      setDividerLocation(-1, -1);

      // Calculate drop information
      MockContainer srcContainer = source.getContainer();
      MockContainer dstContainer = container;

      // Perform drop
      if (srcContainer != null) {
        // Pass false to indicate that the component isn't being permanently deleted.
        // It's just being moved from one container to another.
        srcContainer.removeComponent(source, false);
      }
      int dropTop = parseSourceStyle(DOM.getStyleAttribute(verticalDividerElement, "left"));
      int dropLeft = parseSourceStyle(DOM.getStyleAttribute(horizontalDividerElement, "top"));

      int offsetWidth = parseSourceStyle(source.getElement().getStyle().getWidth()) / 2;
      int offsetHeight = parseSourceStyle(source.getElement().getStyle().getHeight()) / 2;

      dstContainer.addVisibleComponentAtAbsolutePosition(source, dropTop - offsetWidth, dropLeft - offsetHeight);
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

