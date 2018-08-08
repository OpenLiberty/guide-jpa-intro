// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.jpaguide.facelets;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PageDispatcher {

    @ManagedProperty(value = "#{pageLoader}")
    public PageLoader pageLoader;

    String currentRole = null;

    public PageLoader getPageLoader() {
        return pageLoader;
    }

    public void setPageLoader(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }


    public void showEventForm() {
        pageLoader.setContent("content/eventForm.xhtml");
        pageLoader.setCurrentPage("Event Creation");
    }

    public void showMainPage() {
        pageLoader.setContent("content/mainPage.xhtml");
        pageLoader.setCurrentPage("Events");
    }

    public void showEditPage(){
        pageLoader.setContent("content/updateEventForm.xhtml");
        pageLoader.setCurrentPage("Edit Event");
    }

}
