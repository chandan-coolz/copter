package com.coolcreation.copter;


import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.LevelLoader;

import android.graphics.Color;

public class ResourcesManager {
	
	
	 // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public Font font;
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
    
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region,credit;
    
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    //---------------------------------------------
    // credit texture atlas
    
    public BuildableBitmapTextureAtlas creditTextureAtlas;
    
    public ITextureRegion credit_background_region,profile;
    
    
    
    
    //
 private GameScene musics;
 // Game Texture
 public BuildableBitmapTextureAtlas gameTextureAtlas;
     
 // Game Texture Regions
 public ITextureRegion house1,house2,house3,house4,house5,house6,house7,house8,house9,house10,house11,pad1,pad2,pad3,pad4,car1,car2;
 
 
 
 
 public ITextureRegion game_background;   
    
 public ITextureRegion resume,main_menu;  
 public ITextureRegion game_player_initial,coin,platform;  

 public ITiledTextureRegion bird1,bird2;
 
 // roads

 public ITextureRegion road1,roadcar,roadbus;
 
 
 // sea region
 
 public ITextureRegion seaStart,palm,seaBackground,seaBackground2;
 
 public ITextureRegion ship1,ship2,ship3,ship4,ship5,ship6;
 
 
 
    
    // sound
 
 public Music helicopter,collide,coinTaking;
    
    
    
    
    // CLASS LOGIC
    //---------------------------------------------
    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
        
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1500,1500, TextureOptions.BILINEAR);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
    	credit=BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "credit.png");
    
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    }
    
    private void loadMenuAudio()
    {
        
    }
    
    
    
    private void loadMenuFonts()
    {
    	FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
    	
    	
    }
    
    
    
    
    
    

    private void loadGameGraphics()
    {
    	  BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
    	    gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1400,1400, TextureOptions.BILINEAR);
    	    
    	  
    	    
    	    
    	    
    	    
   
    	    
    	    
    	
    	    
    	    house1= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house1.png");
    	    house2= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house2.png");
    	    house3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house3.png");

    	    house4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house4.png");
    	    house5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house5.png");
    	    house6 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house6.png");
    	    house7 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house7.png");
    	    
    	    house8 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house8.png");
    	    house9 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house9.png");
    	    house10 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house10.png");
    	    
    	    house11 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "house11.png");
    	    
    	    pad1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "paed1.png");
    	    pad2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "paes2.png");
    	   pad3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "paed3.png");
    	   pad4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "paed4.png");
 // car
    	    
    	    car1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "car1.png");
    	    car2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "car2.png");
    	    
    	   // coin
    	    
    	    coin = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
    	    // roads
    	    road1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "road1.png");
    	    roadcar=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "roadcar.png");
    	    roadbus=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "roadbus.png");
    	    // sea
    	    
    	    seaStart=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "seaStart.png");
    	    palm=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "palm.png");
    	    
    	    seaBackground=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "seaBackground.png");
    	    
    	    seaBackground2=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "seaBackground2.png");
    	    
    	    // ship
    	    
    	    ship1=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_1.png");
    	    ship2=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_2.png");
    	    ship3=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_3.png");
    	    ship4=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_4.png");
    	    ship5=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_5.png");
    	    ship6=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ship1_6.png");
    	    
    	    
    	    
    	    
    	    // Resume
    	    
    	    resume = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "resume.png");
    	    
    	    main_menu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Main_menu.png");
    	    //
    	    
    	    game_background = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background_game.png");
    	    game_player_initial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game_player_initial1.png");
    	 
    	    platform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform.png");
    
    	    
    	    
    	    
    	    bird1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,activity, "bird1.png", 2, 1);
    	
    	    
    	    try 
    	    {
    	        this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	      
    	       
    	        this.gameTextureAtlas.load();

    	        
    	    } 
    	    catch (final TextureAtlasBuilderException e)
    	    {
    	        Debug.e(e);
    	    } 
    }
    
    private void loadGameFonts()
    {
        
    }
    
    private void loadGameAudio()
    {
     MusicFactory.setAssetBasePath("mfx/");
    	
        try {
	
		helicopter =MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"helicopter.ogg");
		coinTaking =MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"coin.wav");	

		collide =MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"collide.ogg");
		
		
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
    	

    
        
    }
    
    public void loadSplashScreen()
    {
    
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    	
    	
    	
    }
    
    public void loadCreditResourse()
    {
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	
    	creditTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024, TextureOptions.BILINEAR);
    	credit_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditTextureAtlas, activity, "credit_background.png");
    	profile = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditTextureAtlas, activity, "profile.png");
    	try 
    	{
    	    this.creditTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.creditTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    	
    }
    
    
    
    
    
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
	
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
	
    
   
    
    
    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    	
    	gameTextureAtlas.unload();
    }
    
    
  // unload credit texture
    
    public void unloadCreditTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    	
    	creditTextureAtlas.unload();
    }
    
	// setter
    
    
   // * We use this method at beginning of game loading, to prepare Resources Manager properly,
  //  * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
   // 
   public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
   {
       getInstance().engine = engine;
       getInstance().activity = activity;
       getInstance().camera = camera;
       getInstance().vbom = vbom;
    
   }
    
    
    
    
    // getter 
	
	
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
	
	
	
	
	
	
	
	

}
