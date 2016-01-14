/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.projects.stacks;

import org.jboss.forge.addon.facets.Facet;
import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * A {@link StackFacet} represents a set of {@link Facet}s allowed in a project
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public interface StackFacet extends ProjectFacet
{
   /**
    * @return the {@link Stack} bound to this project
    */
   Stack getStack();
}
