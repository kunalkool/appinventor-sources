// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.widgets.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.explorer.project.ComponentDatabaseChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;
import java.util.Map;

/**
 * Panel to display properties.
 *
 */
public class PropertiesPanel extends Composite implements ComponentDatabaseChangeListener {

  // UI elements
  private final VerticalPanel panel;
  private final Label componentName;
  private final Map<String, VerticalPanel> propertyPanels;
  private final Map<String, DisclosurePanel> headers;

  /**
   * Creates a new properties panel.
   */
  public PropertiesPanel() {
    // Initialize UI
    VerticalPanel outerPanel = new VerticalPanel();
    outerPanel.setWidth("100%");

    propertyPanels = new HashMap<String, VerticalPanel>();
    headers = new HashMap<String, DisclosurePanel>();

    componentName = new Label("");
    componentName.setStyleName("ode-PropertiesComponentName");
    outerPanel.add(componentName);

    panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setStylePrimaryName("ode-PropertiesPanel");
    outerPanel.add(panel);

    initWidget(outerPanel);
  }

  boolean hasValidDescription(EditableProperty p) {
    return p.getDescription() != null &&
        !p.getDescription().isEmpty() &&
        !p.getDescription().equals(p.getName());
  }

  private final VerticalPanel getContainer(String category) {
    if ( category == null || category.equals( "Internal" ) ) {
      return null;
    }
    if ( !propertyPanels.containsKey( category ) ) {
      VerticalPanel child = new VerticalPanel();
      child.setWidth( "100%" );
      propertyPanels.put( category, child );
      DisclosurePanel disclosure = new DisclosurePanel( category );
      disclosure.add( child );
      disclosure.setOpen( true );
      disclosure.setWidth( "100%" );
      headers.put( category, disclosure );
    }
    return propertyPanels.get( category );
  }

  private final void updateStackPanel() {
    Set<String> categories = new TreeSet<String>( headers.keySet() );
    for ( String category : categories ) {
      panel.add( headers.get( category ) );
    }
  }

  /**
   * Adds a new property to be displayed in the UI.
   *
   * @param property  new property to be shown
   */
  void addProperty(EditableProperty property) {
    VerticalPanel parent = getContainer(property.getCategory());
    if ( parent != null ) {
      HorizontalPanel header = new HorizontalPanel();
      Label label = new Label(property.getCaption());
      label.setStyleName("ode-PropertyLabel");
      header.add(label);
      header.setStyleName("ode-PropertyHeader");
      if ( hasValidDescription(property) ) {
        PropertyHelpWidget helpImage = new PropertyHelpWidget(property);
        header.add(helpImage);
        helpImage.setStylePrimaryName("ode-PropertyHelpWidget");
      }
      parent.add(header);
      PropertyEditor editor = property.getEditor();
      if (!editor.getStyleName().contains("PropertyEditor")) {
        editor.setStyleName("ode-PropertyEditor");
      }
      parent.add(editor);
      parent.setWidth("100%");
    }
  }

  /**
   * Removes all properties from the properties panel.
   */
  public void clear() {
    propertyPanels.clear();
    headers.clear();
    panel.clear();
    componentName.setText("");
  }

  /**
   * Shows a set of properties in the panel. Any previous content will be
   * removed.
   *
   * @param properties  properties to be shown
   */
  public void setProperties(EditableProperties properties) {
    clear();
    properties.addToPropertiesPanel(this);
    updateStackPanel();
  }

  /**
   * Set the label at the top of the properties panel. Note that you have
   * to do this after calling setProperties because it clears the label!
   * @param name
   */
  public void setPropertiesCaption(String name) {
    componentName.setText(name);
  }

  @Override
  public void onComponentTypeAdded(List<String> componentTypes) {

  }

  @Override
  public boolean beforeComponentTypeRemoved(List<String> componentTypes) {
    return true;
  }

  @Override
  public void onComponentTypeRemoved(Map<String, String> componentTypes) {

  }

  @Override
  public void onResetDatabase() {

  }
}
