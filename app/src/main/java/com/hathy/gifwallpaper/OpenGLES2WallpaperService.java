package com.hathy.gifwallpaper;

/**
 * Created by Ross on 27/04/2015.
 *
 * following this tut
 * http://www.learnopengles.com/how-to-use-opengl-es-2-in-an-android-live-wallpaper/
 *
 */
public abstract class OpenGLES2WallpaperService extends GLWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new OpenGLES2Engine();
    }

    class OpenGLES2Engine extends GLWallpaperService.GLEngine {

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // Check if the system supports OpenGL ES 2.0.
            final ActivityManager activityManager =
                    (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            final ConfigurationInfo configurationInfo =
                    activityManager.getDeviceConfigurationInfo();
            final boolean supportsEs2 =
                    configurationInfo.reqGlEsVersion >= 0x20000;

            if (supportsEs2)
            {
                // Request an OpenGL ES 2.0 compatible context.
                setEGLContextClientVersion(2);

                // On Honeycomb+ devices, this improves the performance when
                // leaving and resuming the live wallpaper.
                setPreserveEGLContextOnPause(true);

                // Set the renderer to our user-defined renderer.
                setRenderer(getNewRenderer());
            }
            else
            {
                // This is where you could create an OpenGL ES 1.x compatible
                // renderer if you wanted to support both ES 1 and ES 2.
                return;
            }
        }
    }

    abstract Renderer getNewRenderer();
}
