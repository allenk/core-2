/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.bootstrap;

import java.io.File;
import java.util.ServiceLoader;

import org.jboss.forge.addon.dependency.spi.DependencyResolver;
import org.jboss.forge.addon.manager.AddonManager;
import org.jboss.forge.addon.manager.InstallRequest;
import org.jboss.forge.container.AddonId;
import org.jboss.forge.container.Forge;

/**
 * A class with a main method to bootstrap Forge.
 * 
 * You can deploy addons by calling {@link Bootstrap#deploy(String)}
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * 
 */
public class Bootstrap
{
   private final Forge forge;
   private final AddonManager addonManager;
   private boolean exitAfter = false;

   public static void main(final String[] args)
   {
      Bootstrap bootstrap = new Bootstrap(args);
      bootstrap.start();
   }

   public Bootstrap(String[] args)
   {
      String installAddon = null;
      forge = new Forge();
      if (args.length > 0 && args.length % 2 == 0)
      {
         for (int i = 0; i < args.length; i++)
         {
            if ("--install".equals(args[i]))
            {
               installAddon = args[++i];
            }
            if ("--addonDir".equals(args[i]))
            {
               forge.setAddonDir(new File(args[++i]));
            }
            if ("--batchMode".equals(args[i]))
            {
               forge.setServerMode(false);
            }
         }
      }

      final DependencyResolver dependencyResolver = lookup(DependencyResolver.class);
      addonManager = new AddonManager(forge.getRepository(), dependencyResolver);

      if (installAddon != null)
      {
         deploy(installAddon);
      }
   }

   public void start()
   {
      if (!exitAfter)
         forge.start();
   }

   public void deploy(String addonCoordinates)
   {
      try
      {
         AddonId addon = AddonId.fromCoordinates(addonCoordinates);
         InstallRequest request = addonManager.install(addon);
         request.perform();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         exitAfter = true;
      }
   }

   private <T> T lookup(Class<? extends T> service)
   {
      return ServiceLoader.load(service).iterator().next();
   }

}
