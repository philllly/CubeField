package mygame;

import java.lang.reflect.Field;

public class Launcher {

    public static void main(String[] arg) {

        //String requiredNativeLibraryPath = "some relative path here";
        String home = System.getProperty("user.home");
        String directory = home + "/Applications/AirspaceApps/CubeField.app/Contents/Java/leapLib";
        System.out.println(directory);
        System.setProperty("java.library.path", directory);

        /*
         change the path for difference mechine, etc, x86, x64 or Mac
         */

        //System.setProperty("java.library.path", System.getProperty("java.library.path") + ";" + requiredNativeLibraryPath);
        Field fieldSysPath;
        try {

            fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);

            //start your app here
            Main.run();


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
