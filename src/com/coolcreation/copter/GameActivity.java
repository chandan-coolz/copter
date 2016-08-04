package com.coolcreation.copter;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.IGameInterface.OnCreateResourcesCallback;
import org.andengine.ui.activity.BaseGameActivity;

import com.coolcreation.copter.ResourcesManager;
import com.coolcreation.copter.SceneManager;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.banner.bannerstandard.BannerStandard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class GameActivity extends BaseGameActivity {

	// variable declaration
	
	private BoundCamera camera;
	
	private ResourcesManager resourcesManager;
	
	private StartAppAd startAppAd = new StartAppAd(this);

 
	
	
	
	// to work with similar speed on various devices
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}


	
	
	//
	
	
	
	
	
	@Override
	
	    
		public EngineOptions onCreateEngineOptions()
		{
		camera = new BoundCamera(0, 0,800,480);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(800,480), this.camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    return engineOptions;            
	       
		}
	
	 
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onCreate(android.os.Bundle)
	 */
	



	@Override
   
		
	 // on create resourses
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
		          
	       
		
     

 	   ResourcesManager.prepareManager(mEngine, this, camera,getVertexBufferObjectManager());
 	    resourcesManager = ResourcesManager.getInstance();
 	pOnCreateResourcesCallback.onCreateResourcesFinished();
    }
//

		
	

	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onSetContentView()
	 */
	@SuppressLint("NewApi") @Override
	protected void onSetContentView() {
		// TODO Auto-generated method stub
		 
		super.onSetContentView();
		StartAppAd.init(this, "104265810", "204065366");  
		 final FrameLayout frameLayout = new FrameLayout(this);
         final FrameLayout.LayoutParams frameLayoutLayoutParams =
                 new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                              FrameLayout.LayoutParams.MATCH_PARENT,
                                              Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
       //DISPLAY ADD AT BOTTOM
         FrameLayout.LayoutParams adViewLayoutParams =
                         new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                          FrameLayout.LayoutParams.WRAP_CONTENT,
                                                          Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
         this.mRenderSurfaceView = new RenderSurfaceView(this);
         mRenderSurfaceView.setRenderer(mEngine,this);
      
         frameLayout.addView(this.mRenderSurfaceView, createSurfaceViewLayoutParams());
         
         
         
         //CHOOSE TYPE OF STARTAPP AD
//       Banner startAppBanner = new Banner(this);//INCLUDES 3D ROTATING BANNER
     BannerStandard startAppBanner = new BannerStandard(this);//TRADITIONAL BANNER ONLY

         frameLayout.addView(startAppBanner, adViewLayoutParams);
    
         this.setContentView(frameLayout, frameLayoutLayoutParams);

         
         
         
	}




	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		
		
	
		
		 
		
		
		
		
		
		
		 // load the next ad

	    SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	    
	    
	   
	
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{

		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	        public void onTimePassed(final TimerHandler pTimerHandler) 
	        {
	            mEngine.unregisterUpdateHandler(pTimerHandler);
	            SceneManager.getInstance().createMenuScene();
	        }
	    }));
	
	pOnPopulateSceneCallback.onPopulateSceneFinished();;
	}

   //
	


    // to handle on backkey pressed
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	

		
		if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
			 startAppAd.onBackPressed();
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}




	@Override
	public void onResume() {
	    super.onResume();
	    startAppAd.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    startAppAd.onPause();
	}

	
	




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
