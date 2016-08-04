package com.coolcreation.copter;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;


import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.coolcreation.copter.SceneManager.SceneType;
import com.startapp.android.publish.StartAppAd;





import android.content.Context;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	
	// variable declaration
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0,CREDIT=1;
	String highScore;
	private Text highScoreText;
int score;

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		

		Read_High_Score();
	    createBackground();
	    createMenuChildScene();
	    createScoreText();
	    displayScoreText();
	    
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		

		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	// creating menu backgrounf
	private void createBackground()
	{
		setBackground(new Background(Color.GREEN));
		Sprite background=new Sprite(camera.getCenterX()-(355/2),camera.getCenterY()-217,ResourcesManager.getInstance().menu_background_region,vbom);
		attachChild(background);
		
		
		
		
	}  //
	
	
	

private void createMenuChildScene()
{
    menuChildScene = new MenuScene(camera);
    menuChildScene.setPosition(100,100);
    
    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f,1 );
    
    final IMenuItem credit = new ScaleMenuItemDecorator(new SpriteMenuItem(CREDIT, resourcesManager.credit, vbom), 1.2f,1 );
    
   
    
    menuChildScene.addMenuItem(playMenuItem);
    menuChildScene.addMenuItem(credit);
  
    menuChildScene.buildAnimations();
    menuChildScene.setBackgroundEnabled(false);
    
    
  
    
    playMenuItem.setPosition(playMenuItem.getX()-80, playMenuItem.getY()+10 );

    credit.setPosition(credit.getX()-80, credit.getY()+15);
    
    menuChildScene.setOnMenuItemClickListener(this);
    
    setChildScene(menuChildScene);
}   

@Override
public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
{
        switch(pMenuItem.getID())
        {                                             
        case MENU_PLAY:
        	 SceneManager.getInstance().loadGameScene(engine);
        	 return true;
        case CREDIT:
        	 SceneManager.getInstance().loadCreditScene(engine);
        	return true;
        default:
            return false;
    }
}
	

private void createScoreText()
{
	highScoreText = new Text(0, 0, resourcesManager.font,"High Score="+highScore, vbom);
}


private void displayScoreText()
{
	
   
    
	highScoreText.setPosition(camera.getCenterX()-140, camera.getCenterY()+10);
    attachChild(highScoreText);
 
    
  
    
    
}  

//
public void Read_High_Score(){
	
	FileInputStream fos = null;
	DataInputStream dos;
	dos=new DataInputStream(fos);
	
	try {
		fos = activity.openFileInput("High_Score");
	}
	
	catch (FileNotFoundException e) {
		 score= 0;
		 WriteHighScore();
		 highScore="0";
		return;

	}
	
	
	
	
	// reading file
	
	try {
		dos=new DataInputStream(fos);
		int t = -1;
		
		t = dos.readInt();
		
		
	  highScore=String.valueOf(t);
		
	  

	    
		
	 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		
	}
	
	
	
	// closing file
	
	try {
		fos.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		
	}
	
	
}



//writing high Score;;;;'

		public void WriteHighScore()
		{
			FileOutputStream fos=null;
			DataOutputStream dos;
			
			try {
				fos = activity.openFileOutput("High_Score", Context.MODE_PRIVATE);
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
			}
			
			// writing
			
			try {
				dos=new DataOutputStream(fos);
				
				//dos.write(unlockedLevelId, 0, Integer.SIZE)
				dos.writeInt(score);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			
			//
			// closing file
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			//
			
			
			
		}
	// end of writing file
	



}
