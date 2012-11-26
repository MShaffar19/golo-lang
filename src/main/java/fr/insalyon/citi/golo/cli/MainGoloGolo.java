package fr.insalyon.citi.golo.cli;

import fr.insalyon.citi.golo.runtime.GoloClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MainGoloGolo {

  public static void main(String... args) throws Throwable {
    if (args.length < 1) {
      System.out.println("No enough arguments given (pass at least a golo file).");
      return;
    }
    GoloClassLoader loader = new GoloClassLoader();
    Class<?> lastClass = null;
    int i = 0;
    for (; i < args.length; i++) {
      if ("--args".equals(args[i])) {
        break;
      }
      File file = new File(args[i]);
      if (!file.exists()) {
        System.out.println("Error: " + file + " does not exist.");
        return;
      }
      if (!file.isFile()) {
        System.out.println("Error: " + file + " is not a file.");
        return;
      }
      try (FileInputStream in = new FileInputStream(file)) {
        lastClass = loader.load(file.getName(), in);
      }
    }
    Object[] appArgs;
    if (i < args.length) {
      appArgs = Arrays.copyOfRange(args, i + 1, args.length);
    } else {
      appArgs = new Object[]{};
    }
    Method main = lastClass.getMethod("main", Object.class);
    main.invoke(null, new Object[]{appArgs});
  }
}
