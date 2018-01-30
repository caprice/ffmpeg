package net.bramp.ffmpeg.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** @author bramp */
public final class ProcessUtils {

  private ProcessUtils() {
    throw new AssertionError("No instances for you!");
  }

  private static class ProcessThread extends Thread {
    final Process p;
    boolean finished = false;
    int exitValue = -1;

    private ProcessThread(Process p) {
      this.p = p;
    }

    @Override
    public void run() {
      try {
        exitValue = p.waitFor();
        finished = true;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    public boolean hasFinished() {
      return finished;
    }

    public int exitValue() {
      return exitValue;
    }
  }

  /**
   * Waits until a process finishes or a timeout occurs
   *
   * @param p process
   * @param timeout timeout in given unit
   * @param unit time unit
   */
  public static void waitForWithTimeout(final Process p, long timeout, TimeUnit unit) {

    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

    try {
      while ((reader.readLine()) != null) {}
      p.waitFor(timeout, unit);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
