package com.coolcreation.copter;

import java.util.regex.Pattern;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import com.coolcreation.copter.SceneManager.SceneType;
import com.startapp.android.publish.StartAppAd;

import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;




public class Credit extends BaseScene implements IOnMenuItemClickListener{

	
	// variable
	
	private final int Cool_Profile = 0;
	private MenuScene menuChildScene;
	
	
	
	
	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		
		

		createBackground();
		
		 createMenuChildScene();
		//
		

		
		
		
		
	}

	

	private void createBackground() {
		// TODO Auto-generated method stub
		Sprite credit_background = new Sprite(0,0,
				resourcesManager.credit_background_region, vbom);
		
		attachChild(credit_background);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}



	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		SceneManager.getInstance().loadAfterCreditScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_CREDIT;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	private void createMenuChildScene()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(100,100);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(Cool_Profile, resourcesManager.profile, vbom), 1.2f,1 );
	    
	
	    
	    menuChildScene.addMenuItem(playMenuItem);
	  
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(playMenuItem.getX()-100, playMenuItem.getY() );

	   
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}   

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	        switch(pMenuItem.getID())
	        {                                             
	        case Cool_Profile:
	        	
	        		Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/chandan.coolz"));
	    		
	    		activity.startActivity(i);
	        	
	        	 return true;
	  
	        default:
	            return false;
	    }
	}
		

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
