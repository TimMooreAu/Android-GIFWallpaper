package com.hathy.gifwallpaper;

import android.content.Context;
import android.graphics.Movie;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GLWallpaperService extends WallpaperService {

	@Override
	public WallpaperService.Engine onCreateEngine() {
		try {
			Movie movie = Movie.decodeStream(getResources().getAssets().open("girl.gif"));

			return new GLWallpaperEngine(movie);
		}catch(IOException e){
			Log.d("GIF", "Could not load asset");
			return null;
		}
	}



    private class GLWallpaperEngine extends Engine {

        private final int frameDuration = 20;

        private SurfaceHolder holder;
        private Movie movie;
        //private boolean visible;
        private Handler handler;
       // private Runnable drawGIF;

        private List<Touch> touches;

        private static final String TAG = "GLEngine";

        private WallpaperGLSurfaceView glSurfaceView;
        private boolean rendererHasBeenSet;

        public GLWallpaperEngine(Movie movie) {
            this.movie = movie;
            handler = new Handler();
         //   drawGIF = new Runnable() {
            //    public void run() {
           //         draw();
          //      }
          //  };
            setTouchEventsEnabled(true);
            touches = new ArrayList();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //this.holder = surfaceHolder;

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
        }

/*
        private void draw() {
            if (visible) {
                Canvas canvas = holder.lockCanvas();

                canvas.save();
                // Adjust size and position so that
                // the image looks good on your screen
                canvas.scale(3f, 3f);
                movie.draw(canvas, -100, 0);


                Paint p = new Paint();
                p.setARGB(120, 120, 0, 0);
                p.setTextSize(20);
                //canvas.drawText("Hello Tim", 30, 60, p);
                for (Touch t : touches){
                    //canvas.drawRect(t.get_x(), t.get_y(), t.get_x() + 10, t.get_y() + 10, p);
                }

                canvas.restore();

                holder.unlockCanvasAndPost(canvas);

                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF, frameDuration);
            }
        }
*/
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            touches.add(new Touch(event.getX(), event.getY()));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //handler.removeCallbacks(drawGIF);
            glSurfaceView.onDestroy();
        }


        protected void setRenderer(GLSurfaceView.Renderer renderer) {
            glSurfaceView.setRenderer(renderer);
            rendererHasBeenSet = true;
        }

        protected void setEGLContextClientVersion(int version) {
            glSurfaceView.setEGLContextClientVersion(version);
        }

        protected void setPreserveEGLContextOnPause(boolean preserve) {
            glSurfaceView.setPreserveEGLContextOnPause(preserve);
        }
        
        private class WallpaperGLSurfaceView extends GLSurfaceView{
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {

                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                //Since we’re driving a live wallpaper, we don’t want to use this surface.
                //Instead, we override getHolder() to call [WallpaperService.Engine].getSurfaceHolder() to get the surface that’s associated with the live wallpaper.
                return getSurfaceHolder();
            }

            public void onDestroy() {

                super.onDetachedFromWindow();
            }



        }
    }
}
