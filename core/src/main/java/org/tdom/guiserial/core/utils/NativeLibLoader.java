package org.tdom.guiserial.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class NativeLibLoader {

  private static final HashSet<String> loaded = new HashSet<>();

  public static synchronized void loadLibrary(String libname) {
    if (!loaded.contains(libname)) {
      StackWalker walker = AccessController.doPrivileged((PrivilegedAction<StackWalker>) () ->
          StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE));
      Class<?> caller = walker.getCallerClass();
      loadLibraryInternal(libname, null, caller);
      loaded.add(libname);
    }
  }

  public static synchronized void loadLibrary(String libname, List<String> dependencies) {
    if (!loaded.contains(libname)) {
      StackWalker walker = AccessController.doPrivileged((PrivilegedAction<StackWalker>) () ->
          StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE));
      Class<?> caller = walker.getCallerClass();
      loadLibraryInternal(libname, dependencies, caller);
      loaded.add(libname);
    }
  }

  private static boolean verbose = false;

  private static boolean usingModules = false;
  private static File libDir = null;
  private static String libPrefix = "";
  private static String libSuffix = "";

  static {
    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
      verbose = Boolean.getBoolean("javafx.verbose");
      return null;
    });
  }

  private static String[] initializePath(String propname) {
    String ldpath = System.getProperty(propname, "");
    String ps = File.pathSeparator;
    int ldlen = ldpath.length();
    int i, j, n;
    // Count the separators in the path
    i = ldpath.indexOf(ps);
    n = 0;
    while (i >= 0) {
      n++;
      i = ldpath.indexOf(ps, i + 1);
    }

    // allocate the array of paths - n :'s = n + 1 path elements
    String[] paths = new String[n + 1];

    // Fill the array with paths from the ldpath
    n = i = 0;
    j = ldpath.indexOf(ps);
    while (j >= 0) {
      if (j - i > 0) {
        paths[n++] = ldpath.substring(i, j);
      } else if (j - i == 0) {
        paths[n++] = ".";
      }
      i = j + 1;
      j = ldpath.indexOf(ps, i);
    }
    paths[n] = ldpath.substring(i, ldlen);
    return paths;
  }

  private static void loadLibraryInternal(String libraryName, List<String> dependencies, Class caller) {
    // The search order for native library loading is:
    // - try to load the native library from the same folder as this jar
    //   (only on non-modular builds)
    // - if the native library comes bundled as a resource it is extracted
    //   and loaded
    // - the java.library.path is searched for the library in definition
    //   order
    // - the library is loaded via System#loadLibrary
    // - on iOS native library is staticly linked and detected from the
    //   existence of a JNI_OnLoad_libraryname funtion
    try {
      // FIXME: JIGSAW -- We should eventually remove this legacy path,
      // since it isn't applicable to Jigsaw.
      loadLibraryFullPath(libraryName);
    } catch (UnsatisfiedLinkError ex) {
      if (verbose && !usingModules) {
        System.err.println("WARNING: " + ex);
      }

      // if the library is available in the jar, copy it to cache and load it from there
      if (loadLibraryFromResource(libraryName, dependencies, caller)) {
        return;
      }

      // NOTE: First attempt to load the libraries from the java.library.path.
      // This allows FX to find more recent versions of the shared libraries
      // from java.library.path instead of ones that might be part of the JRE
      //
      String [] libPath = initializePath("java.library.path");
      for (String s : libPath) {
        try {
          String path = s;
          if (!path.endsWith(File.separator))
            path += File.separator;
          String fileName = System.mapLibraryName(libraryName);
          File libFile = new File(path + fileName);
          System.load(libFile.getAbsolutePath());
          if (verbose) {
            System.err.println("Loaded " + libFile.getAbsolutePath()
                + " from java.library.path");
          }
          return;
        } catch (UnsatisfiedLinkError ex3) {
          // Fail silently and try the next directory in java.library.path
        }
      }

      // Finally we will use System.loadLibrary.
      try {
        System.loadLibrary(libraryName);
        if (verbose) {
          System.err.println("System.loadLibrary("
              + libraryName + ") succeeded");
        }
      } catch (UnsatisfiedLinkError ex2) {
        //On iOS we link all libraries staticaly. Presence of library
        //is recognized by existence of JNI_OnLoad_libraryname() C function.
        //If libraryname contains hyphen, it needs to be translated
        //to underscore to form valid C function indentifier.
        if ("ios".equals(System.getProperty("os.name").toLowerCase(Locale.ROOT))
            && libraryName.contains("-")) {
          libraryName = libraryName.replace("-", "_");
          System.loadLibrary(libraryName);
          return;
        }
        // Rethrow exception
        throw ex2;
      }
    }
  }

  /**
   * If there is a library with the platform-correct name at the
   * root of the resources in this jar, use that.
   */
  private static boolean loadLibraryFromResource(String libraryName, List<String> dependencies, Class caller) {
    return installLibraryFromResource(libraryName, dependencies, caller, true);
  }

  /**
   * If there is a library with the platform-correct name at the
   * root of the resources in this jar, install it. If load is true, also load it.
   */
  private static boolean installLibraryFromResource(String libraryName, List<String> dependencies, Class caller, boolean load) {
    try {
      // first preload dependencies
      if (dependencies != null) {
        for (String dep: dependencies) {
          boolean hasdep = installLibraryFromResource(dep, null, caller, false);
        }
      }
      String reallib = "/"+System.mapLibraryName(libraryName);
      InputStream is = caller.getResourceAsStream(reallib);
      if (is != null) {
        String fp = cacheLibrary(is, reallib, caller);
        if (load) {
          System.load(fp);
          if (verbose) {
            System.err.println("Loaded library " + reallib + " from resource");
          }
        } else if (verbose) {
          System.err.println("Unpacked library " + reallib + " from resource");
        }
        return true;
      }
    } catch (Throwable t) {
      // we should only be here if the resource exists in the module, but
      // for some reasons it can't be loaded.
      System.err.println("Loading library " + libraryName + " from resource failed: " + t);
      t.printStackTrace();
    }
    return false;
  }

  private static String cacheLibrary(InputStream is, String name, Class caller) throws IOException {
    String jfxVersion = System.getProperty("javafx.version", "versionless");
    String userCache = System.getProperty("javafx.cachedir", "");
    if (userCache.isEmpty()) {
      userCache = System.getProperty("user.home") + "/.openjfx/cache/" + jfxVersion;
    }
    File cacheDir = new File(userCache);
    boolean cacheDirOk = true;
    if (cacheDir.exists()) {
      if (!cacheDir.isDirectory()) {
        System.err.println("Cache exists but is not a directory: "+cacheDir);
        cacheDirOk = false;
      }
    } else {
      if (!cacheDir.mkdirs()) {
        System.err.println("Can not create cache at "+cacheDir);
        cacheDirOk = false;
      }
    }
    if (!cacheDir.canRead()) {
      // on some systems, directories in user.home can be written but not read.
      cacheDirOk = false;
    }
    if (!cacheDirOk) {
      String username = System.getProperty("user.name", "anonymous");
      String tmpCache = System.getProperty("java.io.tmpdir") + "/.openjfx_" + username + "/cache/" + jfxVersion;
      cacheDir = new File(tmpCache);
      if (cacheDir.exists()) {
        if (!cacheDir.isDirectory()) {
          throw new IOException("Cache exists but is not a directory: "+cacheDir);
        }
      } else {
        if (!cacheDir.mkdirs()) {
          throw new IOException("Can not create cache at "+cacheDir);
        }
      }
    }
    // we have a cache directory. Add the file here
    File f = new File(cacheDir, name);
    // if it exists, calculate checksum and keep if same as inputstream.
    boolean write = true;
    if (f.exists()) {
      byte[] isHash;
      byte[] fileHash;
      try {
        DigestInputStream dis = new DigestInputStream(is, MessageDigest.getInstance("MD5"));
        dis.getMessageDigest().reset();
        byte[] buffer = new byte[4096];
        while (dis.read(buffer) != -1) { /* empty loop body is intentional */ }
        isHash = dis.getMessageDigest().digest();
        is.close();
        is = caller.getResourceAsStream(name); // mark/reset not supported, we have to reread
      }
      catch (NoSuchAlgorithmException nsa) {
        isHash = new byte[1];
      }
      fileHash = calculateCheckSum(f);
      if (!Arrays.equals(isHash, fileHash)) {
        Files.delete(f.toPath());
      } else {
        // hashes are the same, we already have the file.
        write = false;
      }
    }
    if (write) {
      Path path = f.toPath();
      assert is != null;
      Files.copy(is, path);
    }

    return f.getAbsolutePath();
  }

  static byte[] calculateCheckSum(File file) {
    try {
      // not looking for security, just a checksum. MD5 should be faster than SHA
      try (final InputStream stream = new FileInputStream(file);
          final DigestInputStream dis = new DigestInputStream(stream, MessageDigest.getInstance("MD5")); ) {
        dis.getMessageDigest().reset();
        byte[] buffer = new byte[4096];
        while (dis.read(buffer) != -1) { /* empty loop body is intentional */ }
        return dis.getMessageDigest().digest();
      }

    } catch (IllegalArgumentException | NoSuchAlgorithmException | IOException | SecurityException e) {
      // IOException also covers MalformedURLException
      // SecurityException means some untrusted applet

      // Fall through...
    }
    return new byte[0];
  }


  /**
   * Load the native library from the same directory as the jar file
   * containing this class.
   */
  private static void loadLibraryFullPath(String libraryName) {
    try {
      if (usingModules) {
        throw new UnsatisfiedLinkError("ignored");
      }
      if (libDir == null) {
        // Get the URL for this class, if it is a jar URL, then get the
        // filename associated with it.
        String theClassFile = "NativeLibLoader.class";
        Class<NativeLibLoader> theClass = NativeLibLoader.class;
        String classUrlString = theClass.getResource(theClassFile).toString();
        if (classUrlString.startsWith("jrt:")) {
          // Suppress warning messages
          usingModules = true;
          throw new UnsatisfiedLinkError("ignored");
        }
        if (!classUrlString.startsWith("jar:file:") || classUrlString.indexOf('!') == -1) {
          throw new UnsatisfiedLinkError("Invalid URL for class: " + classUrlString);
        }
        // Strip out the "jar:" and everything after and including the "!"
        String tmpStr = classUrlString.substring(4, classUrlString.lastIndexOf('!'));
        // Strip everything after the last "/" or "\" to get rid of the jar filename
        int lastIndexOfSlash = Math.max(tmpStr.lastIndexOf('/'), tmpStr.lastIndexOf('\\'));

        // Set the native directory based on the OS
        String osName = System.getProperty("os.name");
        String relativeDir = null;
        if (osName.startsWith("Windows")) {
          relativeDir = "../bin";
        } else if (osName.startsWith("Mac")) {
          relativeDir = ".";
        } else if (osName.startsWith("Linux")) {
          relativeDir = ".";
        }

        // Location of native libraries relative to jar file
        String libDirUrlString = tmpStr.substring(0, lastIndexOfSlash)
            + "/" + relativeDir;
        libDir = new File(new URI(libDirUrlString).getPath());

        // Set the lib prefix and suffix based on the OS
        if (osName.startsWith("Windows")) {
          libPrefix = "";
          libSuffix = ".dll";
        } else if (osName.startsWith("Mac")) {
          libPrefix = "lib";
          libSuffix = ".dylib";
        } else if (osName.startsWith("Linux")) {
          libPrefix = "lib";
          libSuffix = ".so";
        }
      }

      File libFile = new File(libDir, libPrefix + libraryName + libSuffix);
      String libFileName = libFile.getCanonicalPath();
      System.load(libFileName);
      if (verbose) {
        System.err.println("Loaded " + libFile.getAbsolutePath()
            + " from relative path");
      }
    } catch (Exception e) {
      // Throw UnsatisfiedLinkError for best compatibility with System.loadLibrary()
      throw (UnsatisfiedLinkError) new UnsatisfiedLinkError().initCause(e);
    }
  }

}

