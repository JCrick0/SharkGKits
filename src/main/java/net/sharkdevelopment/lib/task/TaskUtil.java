package net.sharkdevelopment.lib.task;

/*
 * Copyright (c) 2020, Jordi Xavier. All rights reserved.
 *
 * Do not redistribute without permission from the author.
 */

import net.sharkdevelopment.gkits.SharkGKits;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

  private static final SharkGKits panda = SharkGKits.getInstance();

  public static void runTaskAsync(Runnable runnable) {
    panda.getServer().getScheduler().runTaskAsynchronously(panda, runnable);
  }

  public static void runTaskLater(Runnable runnable, long delay) {
    panda.getServer().getScheduler().runTaskLater(panda, runnable, delay);
  }

  public static void runTaskLaterAsync(Runnable runnable, long delay) {
    panda.getServer().getScheduler().runTaskLaterAsynchronously(panda, runnable, delay);
  }

  public static void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
    runnable.runTaskTimer(panda, delay, timer);
  }

  public static void runTaskTimer(Runnable runnable, long delay, long timer) {
    panda.getServer().getScheduler().runTaskTimer(panda, runnable, delay, timer);
  }

  public static void runTaskTimerAsync(Runnable runnable, long delay, long timer) {
    panda
      .getServer()
      .getScheduler()
      .runTaskTimerAsynchronously(panda, runnable, delay, timer);
  }

  public static void run(Runnable runnable) {
    panda.getServer().getScheduler().runTask(panda, runnable);
  }
}
