/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.shell.commands;

import java.io.File;

import javax.inject.Inject;

import org.jboss.aesh.console.Config;
import org.jboss.aesh.console.Console;
import org.jboss.aesh.extensions.less.Less;
import org.jboss.forge.addon.shell.ui.AbstractShellCommand;
import org.jboss.forge.addon.shell.ui.ShellContext;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class LessCommand extends AbstractShellCommand
{

   @Inject
   private UIInputMany<File> arguments;

   @Override
   public Metadata getMetadata()
   {
      return super.getMetadata()
               .name("less")
               .description("less - opposite of more");
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      arguments.setLabel("");
      arguments.setRequired(false);
      builder.add(arguments);
   }

   @Override
   public Result execute(ShellContext context) throws Exception
   {
      if (arguments.getValue() != null)
      {

         Console console = context.getProvider().getConsole();
         File file = arguments.getValue().iterator().next();
         // a simple hack that we should try to avoid
         // probably the converter that add the cwd dir if the
         // user input start with ~/
         if (file.getAbsolutePath().contains("~/"))
         {
            file = new File(Config.getHomeDir() + file.getAbsolutePath().substring(
                     file.getAbsolutePath().indexOf("~/") + 1));
         }
         if (file.isFile())
         {
            Less less = new Less(console);
            less.setFile(file);
            less.attach(context.getConsoleOperation());
            return Results.success();
         }
         else if (file.isDirectory())
         {
            return Results.fail(file.getAbsolutePath() + " is a directory");
         }
         else
         {
            return Results.fail(file.getAbsolutePath() + " No such file or directory");
         }
      }
      else
      {
         return Results.fail("Missing filename (\"less --help\" for help)");
      }
   }

}
