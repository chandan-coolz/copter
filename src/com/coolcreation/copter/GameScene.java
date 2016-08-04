package com.coolcreation.copter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.io.FileWriter;
import java.io.IOException;
import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andlabs.andengine.extension.physicsloader.PhysicsEditorLoader;

import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.badlogic.gdx.math.Vector2;
import com.coolcreation.copter.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener,
		IOnMenuItemClickListener {

	// variable

	private HUD gameHUD;
	private Text scoreText;
	private int footContacts = 0;
	private int score = 0, highScore;
	private PhysicsWorld physicsWorld;
	private PhysicsWorld physicsWorld2,physicsWorld3;

	Body body, bird_body1, bird_body2, bird_body3, bird_body4, bird_body5,
			bird_body6, bird_body7, bird_body8, bird_body9, bird_body10,bird_body11, bird_body12, bird_body13, bird_body14, bird_body15,
			bird_body16, bird_body17, bird_body18, bird_body19, bird_body20,bird_body21, bird_body22, bird_body23, bird_body24, bird_body25,
			bird_body26, bird_body27, bird_body28, bird_body29, bird_body30,bird_body31, bird_body32, bird_body33, bird_body34, bird_body35,
			bird_body36, bird_body37, bird_body38, bird_body39, 
			moving_car_body, moving_bus_body, moving_car_body2,
			moving_bus_body2;
	AnimatedSprite bird1, bird2, bird3, bird4, bird5, bird6, bird7, bird8,
			bird9, bird10,bird11, bird12, bird13, bird14, bird15, bird16, bird17, bird18,
			bird19, bird20,bird21, bird22, bird23, bird24, bird25, bird26, bird27, bird28,
			bird29, bird30,bird31, bird32, bird33, bird34, bird35, bird36, bird37, bird38,
			bird39;
			

	private Text gameOverText,winnerText;
	Sprite game_player_initial;
	private boolean canRun = false;
	private boolean firstTouch = false;
	private boolean gameOverDisplayed = false;
	private boolean winnerTextDisplayed = false;
	private boolean moving_car_direction = true;
	private boolean moving_car_direction2 = true;
	private boolean moving_bus_direction = true;
	private boolean moving_bus_direction2= true;
	Sprite moving_car, moving_bus,moving_car2,moving_bus2;

	// sound

	private Music helicopter, coinTaking, collide;
	//

	// resume variable

	private MenuScene menuChildScene;
	private final int Resume_play = 0, mainMenu = 1;

	//

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		createBackground();
		createHUD();
		createPhysics();
		loadlevel();
		loadAudio();
		createGameOverText();
		createWinnerText();

		setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

		if (!gameOverDisplayed) {
			pauseGame();
			pauseMusic();

		} else {
			engine.clearUpdateHandlers();

			SceneManager.getInstance().loadMenuScene(engine);

		}

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		camera.setHUD(null);
		camera.setCenter(400, 240);
		camera.setChaseEntity(null);
	}

	// create background
	private void createBackground() {
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0,
				resourcesManager.game_background, vbom)));
		setBackground(background);

	}

	//
	// creating HUD

	private void createHUD() {
		gameHUD = new HUD();

		// CREATE SCORE TEXT
		scoreText = new Text(10, 10, resourcesManager.font,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setSkewCenter(0, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	//
	// for score

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	//
	// creating physics

	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0,8.0f),
				false);

		physicsWorld2 = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
		
	

		registerUpdateHandler(physicsWorld);
		registerUpdateHandler(physicsWorld2);

	}

	//

	// loading levels

	private void loadlevel() {
		
		
		
		
		
		camera.setBounds(0, 0, 19100, 480); // here we set camera bounds
		camera.setBoundsEnabled(true);

	
		//platform
		
		Sprite platform= new Sprite(20.0f,104,
		resourcesManager.platform, vbom);

	
	 PhysicsFactory.createBoxBody(physicsWorld,
		platform, BodyType.StaticBody,
				PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f));


		// game player

		game_player_initial = new Sprite(30.0f,50.0f,
				resourcesManager.game_player_initial, vbom);
		
	
		//body = PhysicsFactory.createBoxBody(physicsWorld, game_player_initial,
			//	BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		
		
		
		final PhysicsEditorLoader loader=new PhysicsEditorLoader();
		
	loader.setAssetBasePath("xml/");
		
		try{
			
			loader.load(activity, physicsWorld, "copter.xml",game_player_initial,true,false);
			
			
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
		
		
		 
		
	
		

BodyDef pDef=new BodyDef();



pDef.type=BodyType.DynamicBody;



body=physicsWorld.createBody(pDef);

FixtureDef fd=new FixtureDef();
fd.friction=0.0f;
fd.density=0.0f;

body.createFixture(fd);
	
		
		
	
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(
				game_player_initial,body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					body.setLinearVelocity(new Vector2(5, body
							.getLinearVelocity().y));

					if (game_player_initial.getY() <= 0) {

						body.setLinearVelocity(new Vector2(body
								.getLinearVelocity().x, +6));

					}
					// checking pplayer goin out of scean
					if (game_player_initial.getY() >= camera.getHeight()) {
						if (!gameOverDisplayed) {
							displayGameOverText();
						}
					}

					//
              // displaying winning scene
					
					// checking pplayer goin out of scean
					if (game_player_initial.getX() >=19100) {
						if (!winnerTextDisplayed) {
							winnerText();
						}
					}

					//	
					
					
					
					//
					
					
					
					
					
				}
			}

		});

		
		
		
		// moving car 1

		moving_car = new Sprite(7296, camera.getHeight() - 90,
				resourcesManager.roadcar, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		moving_car_body = PhysicsFactory.createBoxBody(physicsWorld2,
				moving_car, BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));

		
		moving_car_body.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(moving_car,
				moving_car_body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {

					if (moving_car_direction) {

						moving_car_body.setLinearVelocity(new Vector2(+3,
								moving_car_body.getLinearVelocity().y));

						if (moving_car.getX() > 8190) {

							moving_car_direction = false;
						}//

					}
					//

					if (!moving_car_direction) {

						moving_car_body.setLinearVelocity(new Vector2(-3,
								moving_car_body.getLinearVelocity().y));

						if (moving_car.getX() < 7295) {

							moving_car_direction = true;
						}//

					}
					//

				}
			}

		});

		//

	
		// moving car 2
		
		
		
		
		
		
		

		moving_car2= new Sprite(9296, camera.getHeight() - 90,
				resourcesManager.roadcar, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		moving_car_body2 = PhysicsFactory.createBoxBody(physicsWorld2,
				moving_car2, BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));

		
		moving_car_body2.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(moving_car2,
				moving_car_body2, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {

					if (moving_car_direction2) {

						moving_car_body2.setLinearVelocity(new Vector2(+4,
								moving_car_body2.getLinearVelocity().y));

						if (moving_car2.getX() > 10190) {

							moving_car_direction2 = false;
						}//

					}
					//

					if (!moving_car_direction2) {

						moving_car_body2.setLinearVelocity(new Vector2(-4,
								moving_car_body2.getLinearVelocity().y));

						if (moving_car2.getX() < 9295) {

							moving_car_direction2 = true;
						}//

					}
					//

				}
			}

		});

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/// end of moving car 2
		
		// moving bus or road bus

		moving_bus = new Sprite(8295, camera.getHeight() - 90,
				resourcesManager.roadbus, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		moving_bus_body = PhysicsFactory.createBoxBody(physicsWorld2,
				moving_bus, BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));

		moving_bus_body.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(moving_bus,
				moving_bus_body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {

					if (moving_bus_direction) {

						moving_bus_body.setLinearVelocity(new Vector2(+3,
								moving_bus_body.getLinearVelocity().y));

						if (moving_bus.getX() > 9169) {

							moving_bus_direction = false;
						}//

					}
					//

					if (!moving_bus_direction) {

						moving_bus_body.setLinearVelocity(new Vector2(-3,
								moving_bus_body.getLinearVelocity().y));

						if (moving_bus.getX() < 8295) {

							moving_bus_direction = true;
						}//

					}
					//

				}
			}

		});

		//
  // moving bus 2
		
	
		
		
		// moving bus or road bus

		moving_bus2 = new Sprite(10295, camera.getHeight() - 90,
				resourcesManager.roadbus, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		moving_bus_body2 = PhysicsFactory.createBoxBody(physicsWorld2,
				moving_bus2, BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));

		moving_bus_body2.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(moving_bus2,
				moving_bus_body2, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {

					if (moving_bus_direction2) {

						moving_bus_body2.setLinearVelocity(new Vector2(+4,
								moving_bus_body2.getLinearVelocity().y));

						if (moving_bus2.getX() >11169) {

							moving_bus_direction2 = false;
						}//

					}
					//

					if (!moving_bus_direction2) {

						moving_bus_body2.setLinearVelocity(new Vector2(-4,
								moving_bus_body2.getLinearVelocity().y));

						if (moving_bus2.getX() <10295) {

							moving_bus_direction2 = true;
						}//

					}
					//

				}
			}

		});

		//
		
		
		
		
		
		
		
		
		
		
		//
		// loading bird 1

		bird1 = new AnimatedSprite(1506, 70, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// loading game sound

		//

		bird_body1 = PhysicsFactory.createBoxBody(physicsWorld2, bird1,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body1.setUserData("bird1");
		bird_body1.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird1,
				bird_body1, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=173)
					{
					bird_body1.setLinearVelocity(new Vector2(-2, bird_body1
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// loading bird 2

		bird2 = new AnimatedSprite(1684, 70, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body2 = PhysicsFactory.createBoxBody(physicsWorld2, bird2,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body2.setUserData("bird2");
		bird_body2.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird2,
				bird_body2, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=351)
					{
					bird_body2.setLinearVelocity(new Vector2(-2, bird_body2
							.getLinearVelocity().y));
					}
				}
			}

		});

		// loading bird 3

		bird3 = new AnimatedSprite(2166, 20, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body3 = PhysicsFactory.createBoxBody(physicsWorld2, bird3,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body3.setUserData("bird3");
		bird_body3.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird3,
				bird_body3, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=833)
					{
					bird_body3.setLinearVelocity(new Vector2(-2, bird_body3
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// bird 4 setup

		bird4 = new AnimatedSprite(2166, 160, resourcesManager.bird1, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body4 = PhysicsFactory.createBoxBody(physicsWorld2, bird4,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body4.setUserData("bird4");
		bird_body4.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird4,
				bird_body4, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=833)
					{
					bird_body4.setLinearVelocity(new Vector2(-2, bird_body4
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// bird 5 setup

		bird5 = new AnimatedSprite(2589, 100, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body5 = PhysicsFactory.createBoxBody(physicsWorld2, bird5,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body5.setUserData("bird5");
		bird_body5.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird5,
				bird_body5, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=1439)
					{
					bird_body5.setLinearVelocity(new Vector2(-2, bird_body5
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// bird 6

		bird6 = new AnimatedSprite(3002, 120, resourcesManager.bird1, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body6 = PhysicsFactory.createBoxBody(physicsWorld2, bird6,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body6.setUserData("bird6");
		bird_body6.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird6,
				bird_body6, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=1852)
					{
					bird_body6.setLinearVelocity(new Vector2(-2, bird_body6
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// bird7

		bird7 = new AnimatedSprite(3325, 100, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body7 = PhysicsFactory.createBoxBody(physicsWorld2, bird7,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body7.setUserData("bird7");
		bird_body7.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird7,
				bird_body7, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=2179)
					{
					bird_body7.setLinearVelocity(new Vector2(-2, bird_body7
							.getLinearVelocity().y));
					}
				}
			}

		});

		//

		// bird 8

		bird8 = new AnimatedSprite(3468, 100, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body8 = PhysicsFactory.createBoxBody(physicsWorld2, bird8,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body8.setUserData("bird8");
		bird_body8.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird8,
				bird_body8, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=2318)
					{
					bird_body8.setLinearVelocity(new Vector2(-2, bird_body8
							.getLinearVelocity().y));
					}
				}
			}

		});

		//
		// bird 9

		bird9 = new AnimatedSprite(3650, 150, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body9 = PhysicsFactory.createBoxBody(physicsWorld2, bird9,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body9.setUserData("bird9");
		bird_body9.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird9,
				bird_body9, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					if(game_player_initial.getX()>=2627)
					{
					bird_body9.setLinearVelocity(new Vector2(-2, bird_body9
							.getLinearVelocity().y));
					}
				}
			}

		});

		// bird 10

		bird10 = new AnimatedSprite(4287, 70, resourcesManager.bird1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		bird_body10 = PhysicsFactory.createBoxBody(physicsWorld2, bird10,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		bird_body10.setUserData("bird10");
		bird_body10.setFixedRotation(true);

		physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird10,
				bird_body10, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (canRun) {
					
					if(game_player_initial.getX()>=3137)
					{
					bird_body10.setLinearVelocity(new Vector2(-2, bird_body10
							.getLinearVelocity().y));
					}
				}
			}

		});
		
		
		
		// loading bird 11

				bird11 = new AnimatedSprite(4577, 150, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body11 = PhysicsFactory.createBoxBody(physicsWorld2, bird11,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body11.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird11,
						bird_body11, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							
								if(game_player_initial.getX()>=3427)
								{
							bird_body11.setLinearVelocity(new Vector2(-2, bird_body11
									.getLinearVelocity().y));
								}
							
						}
					}

				});

				//
		
		
				// loading bird 12

				bird12 = new AnimatedSprite(4879, 250, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body12 = PhysicsFactory.createBoxBody(physicsWorld2, bird12,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body12.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird12,
						bird_body12, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=3729)
							{
							bird_body12.setLinearVelocity(new Vector2(-2, bird_body12
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
		
		
				// loading bird 13

				bird13 = new AnimatedSprite(5930, 90, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body13 = PhysicsFactory.createBoxBody(physicsWorld2, bird13,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body13.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird13,
						bird_body13, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=4780)
							{
							bird_body13.setLinearVelocity(new Vector2(-2, bird_body13
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
				// loading bird 14

				bird14 = new AnimatedSprite(6195, 150, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body14 = PhysicsFactory.createBoxBody(physicsWorld2, bird14,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body14.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird14,
						bird_body14, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=5045)
							{
							bird_body14.setLinearVelocity(new Vector2(-2, bird_body14
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
				
				
				
				// loading bird 15

				bird15 = new AnimatedSprite(6704, 90, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body15 = PhysicsFactory.createBoxBody(physicsWorld2, bird15,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body15.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird15,
						bird_body15, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=5554)
							{
							bird_body15.setLinearVelocity(new Vector2(-2, bird_body15
									.getLinearVelocity().y));
							}
						}
					}

				});

				//	
		
		
				// loading bird 16

				bird16 = new AnimatedSprite(6874, 190, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body16 = PhysicsFactory.createBoxBody(physicsWorld2, bird16,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body16.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird16,
						bird_body16, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=5724)
							{
							bird_body16.setLinearVelocity(new Vector2(-2, bird_body16
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
				// loading bird 17

				bird17 = new AnimatedSprite(7320,130, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body17 = PhysicsFactory.createBoxBody(physicsWorld2, bird17,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body17.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird17,
						bird_body17, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=6186)
							{
							bird_body17.setLinearVelocity(new Vector2(-2, bird_body17
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
				// loading bird 18

				bird18 = new AnimatedSprite(7554, 70, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body18 = PhysicsFactory.createBoxBody(physicsWorld2, bird18,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body18.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird18,
						bird_body18, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=6404)
							{
							bird_body18.setLinearVelocity(new Vector2(-2, bird_body18
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
				// loading bird 19

				bird19 = new AnimatedSprite(11690, 90, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body19 = PhysicsFactory.createBoxBody(physicsWorld2, bird19,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body19.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird19,
						bird_body19, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=10490)
							{
							bird_body19.setLinearVelocity(new Vector2(-2, bird_body19
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				// loading bird 20

				bird20 = new AnimatedSprite(12067, 150, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body20 = PhysicsFactory.createBoxBody(physicsWorld2, bird20,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body20.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird20,
						bird_body20, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=10917)
							{
							bird_body20.setLinearVelocity(new Vector2(-2, bird_body20
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				
				
				
				// loading bird 21

				bird21 = new AnimatedSprite(13157, 20, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body21 = PhysicsFactory.createBoxBody(physicsWorld2, bird21,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body21.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird21,
						bird_body21, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=12007)
							{
							bird_body21.setLinearVelocity(new Vector2(-2, bird_body21
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				
				
				

				// loading bird 22

				bird22 = new AnimatedSprite(13157,190, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body22 = PhysicsFactory.createBoxBody(physicsWorld2, bird22,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body22.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird22,
						bird_body22, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=12007)
							{
							bird_body22.setLinearVelocity(new Vector2(-2, bird_body22
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				
				
				

				// loading bird 23

				bird23 = new AnimatedSprite(13595,150, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body23 = PhysicsFactory.createBoxBody(physicsWorld2, bird23,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body23.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird23,
						bird_body23, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=12445)
							{
							bird_body23.setLinearVelocity(new Vector2(-2, bird_body23
									.getLinearVelocity().y));
							}
						}
					}

				});

				//	
				
				
				
				

				// loading bird 24

				bird24 = new AnimatedSprite(13925, 190, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body24 = PhysicsFactory.createBoxBody(physicsWorld2, bird24,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body24.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird24,
						bird_body24, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							
							if(game_player_initial.getX()>=12665)
							{
							bird_body24.setLinearVelocity(new Vector2(-2, bird_body24
									.getLinearVelocity().y));
							}
						}
					}

				});

				//		
				
			

				// loading bird 25

				bird25 = new AnimatedSprite(14477, 90, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body25 = PhysicsFactory.createBoxBody(physicsWorld2, bird25,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body25.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird25,
						bird_body25, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=13327)
							{
							bird_body25.setLinearVelocity(new Vector2(-2, bird_body25
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				

				// loading bird 26

				bird26 = new AnimatedSprite(14707, 150, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body26 = PhysicsFactory.createBoxBody(physicsWorld2, bird26,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body26.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird26,
						bird_body26, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=13557)
							{
							bird_body26.setLinearVelocity(new Vector2(-2, bird_body26
									.getLinearVelocity().y));
							}
						}
					}

				});

				//	
				
				

				// loading bird 27

				bird27 = new AnimatedSprite(14707, 20, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body27 = PhysicsFactory.createBoxBody(physicsWorld2, bird27,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body27.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird27,
						bird_body27, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=13557)
							{
							bird_body27.setLinearVelocity(new Vector2(-2, bird_body27
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				

				// loading bird 28

				bird28 = new AnimatedSprite(15390, 70, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body28 = PhysicsFactory.createBoxBody(physicsWorld2, bird28,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body28.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird28,
						bird_body28, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=14240)
							{
							bird_body28.setLinearVelocity(new Vector2(-2, bird_body28
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				

				// loading bird 29

				bird29 = new AnimatedSprite(15764, 160, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body29 = PhysicsFactory.createBoxBody(physicsWorld2, bird29,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body29.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird29,
						bird_body29, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=14614)
							{
							bird_body29.setLinearVelocity(new Vector2(-2, bird_body29
									.getLinearVelocity().y));
							}
						}
					}

				});

				//	
				
				

				// loading bird 30

				bird30 = new AnimatedSprite(15764, 20, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body30 = PhysicsFactory.createBoxBody(physicsWorld2, bird30,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body30.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird30,
						bird_body30, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=14614)
							{
							bird_body30.setLinearVelocity(new Vector2(-2, bird_body30
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				
				// loading bird 31

				bird31 = new AnimatedSprite(16069, 70, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body31 = PhysicsFactory.createBoxBody(physicsWorld2, bird31,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body31.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird31,
						bird_body31, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=14919)
							{
							bird_body31.setLinearVelocity(new Vector2(-2, bird_body31
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
					
				
				
				// loading bird 32

				bird32 = new AnimatedSprite(16069, 140, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body32 = PhysicsFactory.createBoxBody(physicsWorld2, bird32,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body32.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird32,
						bird_body32, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=14919)
							{
							bird_body32.setLinearVelocity(new Vector2(-2, bird_body32
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
		
				// loading bird 33

				bird33 = new AnimatedSprite(16674,140, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body33 = PhysicsFactory.createBoxBody(physicsWorld2, bird33,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body33.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird33,
						bird_body33, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=15524)
							{
							bird_body33.setLinearVelocity(new Vector2(-2, bird_body33
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
				
				// loading bird 34

				bird34 = new AnimatedSprite(16894, 170, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body34 = PhysicsFactory.createBoxBody(physicsWorld2, bird34,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body34.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird34,
						bird_body34, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=15774)
							{
							bird_body34.setLinearVelocity(new Vector2(-2, bird_body34
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
				
			
				// loading bird 35

				bird35 = new AnimatedSprite(17202, 130, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body35 = PhysicsFactory.createBoxBody(physicsWorld2, bird35,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body35.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird35,
						bird_body35, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=16052)
							{
							bird_body35.setLinearVelocity(new Vector2(-2, bird_body35
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
							
				
				// loading bird 36

				bird36 = new AnimatedSprite(17477, 170, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body36 = PhysicsFactory.createBoxBody(physicsWorld2, bird36,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body36.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird36,
						bird_body36, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=16327)
							{
							bird_body36.setLinearVelocity(new Vector2(-2, bird_body36
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
						
		
				// loading bird 37

				bird37 = new AnimatedSprite(17755, 130, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body37 = PhysicsFactory.createBoxBody(physicsWorld2, bird37,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body37.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird37,
						bird_body37, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=16605)
							{
							bird_body37.setLinearVelocity(new Vector2(-2, bird_body37
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
					
				
				// loading bird 38

				bird38 = new AnimatedSprite(18029, 160, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body38 = PhysicsFactory.createBoxBody(physicsWorld2, bird38,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body38.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird38,
						bird_body38, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=16879)
							{
							bird_body38.setLinearVelocity(new Vector2(-2, bird_body38
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
					
				
				// loading bird 39

				bird39 = new AnimatedSprite(18307, 90, resourcesManager.bird1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				// loading game sound

				//

				bird_body39 = PhysicsFactory.createBoxBody(physicsWorld2, bird39,
						BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

			
				bird_body39.setFixedRotation(true);

				physicsWorld2.registerPhysicsConnector(new PhysicsConnector(bird39,
						bird_body39, true, false) {
					@Override
					public void onUpdate(float pSecondsElapsed) {
						super.onUpdate(pSecondsElapsed);
						camera.onUpdate(0.1f);

						if (canRun) {
							if(game_player_initial.getX()>=17157)
							{
							bird_body39.setLinearVelocity(new Vector2(-2, bird_body39
									.getLinearVelocity().y));
							}
						}
					}

				});

				//
		
		
				
				
				
				
				
				
		
		

		//

		// house 1 setup

		Sprite house1_1 = new Sprite(0, 0, resourcesManager.house1, vbom)

		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// house 2 setup

		Sprite house2_1 = new Sprite(0, 0, resourcesManager.house2, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		// hurdle 3 type

		Sprite house3_1 = new Sprite(0, 0, resourcesManager.house3, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 4 type
		Sprite house4_1 = new Sprite(0, 0, resourcesManager.house4, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 5 type

		Sprite house5_1 = new Sprite(0, 0, resourcesManager.house5, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house5_2 = new Sprite(0, 0, resourcesManager.house5, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house5_3 = new Sprite(0, 0, resourcesManager.house5, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};
		
		
		
		
		// hurdle 6 type

		Sprite house6_1 = new Sprite(0, 0, resourcesManager.house6, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 7 type

		Sprite house7_1 = new Sprite(0, 0, resourcesManager.house7, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house7_2 = new Sprite(0, 0, resourcesManager.house7, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house7_3 = new Sprite(0, 0, resourcesManager.house7, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house7_4 = new Sprite(0, 0, resourcesManager.house7, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};
		
		
		
		
		
		// house 8
		Sprite house8_1 = new Sprite(0, 0, resourcesManager.house8, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house8_2 = new Sprite(0, 0, resourcesManager.house8, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 9 type

		Sprite house9_1 = new Sprite(0, 0, resourcesManager.house9, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 10 type

		Sprite house10_1 = new Sprite(0, 0, resourcesManager.house10, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// hurdle 11 type

		Sprite house11_1 = new Sprite(0, 0, resourcesManager.house11, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite house11_2 = new Sprite(0, 0, resourcesManager.house11, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// paid tree1
		Sprite pad1_1 = new Sprite(0, 0, resourcesManager.pad1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite pad1_2 = new Sprite(0, 0, resourcesManager.pad1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// pad tree 2

		Sprite pad2_1 = new Sprite(0, 0, resourcesManager.pad2, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// paed tree 3

		Sprite pad3_1 = new Sprite(0, 0, resourcesManager.pad3, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		Sprite pad3_2 = new Sprite(0, 0, resourcesManager.pad3, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		// paed 4 tree 4

		Sprite pad4_1 = new Sprite(0, 0, resourcesManager.pad4, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// car position

		// car 1 type

		Sprite car1_1 = new Sprite(0, 0, resourcesManager.car1, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		// moving car 1

		// car2 type

		Sprite car2_1 = new Sprite(0, 0, resourcesManager.car2, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {

					if (!gameOverDisplayed) {
						displayGameOverText();
						game_player_initial.setIgnoreUpdate(true);
					}
				}
			}
		};

		//

		// roads setup

		// road 1
		Sprite road1_1 = new Sprite(0, 0, resourcesManager.road1, vbom) ;
			
		// road 2

		Sprite road1_2 = new Sprite(0, 0, resourcesManager.road1, vbom) ;
		
		// road 3
				Sprite road1_3 = new Sprite(0, 0, resourcesManager.road1, vbom);
				
				// road 4
				Sprite road1_4 = new Sprite(0, 0, resourcesManager.road1, vbom) ;
		
		//
				
				
				
				
				
	// sea region
				
				Sprite seaStart = new Sprite(0, 0, resourcesManager.seaStart, vbom) ;
				Sprite seaStart1 = new Sprite(0, 0, resourcesManager.seaStart, vbom) ;
				
				Sprite palm = new Sprite(0, 0, resourcesManager.palm, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};

				
				Sprite seaBackground1_1= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				
				
				Sprite seaBackground2_1= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				
				Sprite seaBackground2_2= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_3= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_4= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_5= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_6= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_7= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_8= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_9= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				Sprite seaBackground2_10= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				
				Sprite seaBackground2_11= new Sprite(0, 0, resourcesManager.seaBackground2, vbom) ;
				
           // ship1
				
				Sprite ship1_1 = new Sprite(0, 0, resourcesManager.ship1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				

				Sprite ship1_2 = new Sprite(0, 0, resourcesManager.ship1, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
					
				
				
				
				// ship2
				
				Sprite ship2_1 = new Sprite(0, 0, resourcesManager.ship2, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
 
				Sprite ship2_2 = new Sprite(0, 0, resourcesManager.ship2, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				// ship3
				
				Sprite ship3_1 = new Sprite(0, 0, resourcesManager.ship3, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				

				
				Sprite ship3_2 = new Sprite(0, 0, resourcesManager.ship3, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				// ship4
				
				Sprite ship4_1 = new Sprite(0, 0, resourcesManager.ship4, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				Sprite ship4_2 = new Sprite(0, 0, resourcesManager.ship4, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				
// ship 5
				
				Sprite ship5_1 = new Sprite(0, 0, resourcesManager.ship5, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};				
				
				Sprite ship5_2 = new Sprite(0, 0, resourcesManager.ship5, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};	
// ship6
				
				Sprite ship6_1 = new Sprite(0, 0, resourcesManager.ship6, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				Sprite ship6_2 = new Sprite(0, 0, resourcesManager.ship6, vbom) {
					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {

							if (!gameOverDisplayed) {
								displayGameOverText();
								game_player_initial.setIgnoreUpdate(true);
							}
						}
					}
				};
				
				
				
				
				
				//
				
				
				
				
				
				
				

		// coin setup

		// coin 1

		Sprite coin1_1 = new Sprite(0, 0, resourcesManager.coin, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 2

		Sprite coin1_2 = new Sprite(0, 0, resourcesManager.coin, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 3

		Sprite coin1_3 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 4

		Sprite coin1_4 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 5

		Sprite coin1_5 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 6

		Sprite coin1_6 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 7

		Sprite coin1_7 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 8

		Sprite coin1_8 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 9

		Sprite coin1_9 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 10

		Sprite coin1_10 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 11

		Sprite coin1_11 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 12

		Sprite coin1_12 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		//

		// coin 13

		Sprite coin1_13 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 14

		Sprite coin1_14 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 15

		Sprite coin1_15 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		// coin 16

		Sprite coin1_16 = new Sprite(0, 0, resourcesManager.coin, vbom) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (game_player_initial.collidesWith(this)) {
					coinTaking.play();
					addToScore(10);
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
		};

		
		// coin 17

				Sprite coin1_17 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
		
				// coin 18

				Sprite coin1_18 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
		
				// coin 19

				Sprite coin1_19 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
		
				// coin 20

				Sprite coin1_20 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
		
		
				// coin 21

				Sprite coin1_21 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
		
				// coin 22

				Sprite coin1_22 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
				
				
				// coin 23

				Sprite coin1_23 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
				
				// coin 24

				Sprite coin1_24 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
				
				// coin 22

				Sprite coin1_25 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
				// coin 26

				Sprite coin1_26= new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
				// coin 27

				Sprite coin1_27 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
				
				// coin 28

				Sprite coin1_28 = new Sprite(0, 0, resourcesManager.coin, vbom) {

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						super.onManagedUpdate(pSecondsElapsed);

						if (game_player_initial.collidesWith(this)) {
							coinTaking.play();
							addToScore(10);
							this.setVisible(false);
							this.setIgnoreUpdate(true);
						}
					}
				};
		
				
		// platform position
				
			
				
				
				//
				
				
				
				
				
				
				
				
				
		// setting house 1 positon

		house1_1.setPosition(700, camera.getHeight() - house1_1.getHeight());

		// house 2
		// house 2
		house2_1.setPosition(1053, camera.getHeight() - house2_1.getHeight());

		// coins

		coin1_1.setPosition(1160,
				(camera.getHeight() - house2_1.getHeight() - 2 * (coin1_1
						.getHeight())));
		//

		//

		// house 3

		house3_1.setPosition(1556, camera.getHeight() - house3_1.getHeight());

		//

		// house 4
		house4_1.setPosition(3118, camera.getHeight() - house4_1.getHeight());

		//

		// house 5

		house5_1.setPosition(2239, camera.getHeight() - house5_1.getHeight());

		house5_2.setPosition(6524, camera.getHeight() - house5_2.getHeight());
		
		house5_3.setPosition(11290, camera.getHeight() - house5_3.getHeight());

		//

		// house 6

		house6_1.setPosition(5389, camera.getHeight() - house6_1.getHeight());

		//

		// house 7
		house7_1.setPosition(1420, camera.getHeight() - house7_1.getHeight());
		house7_2.setPosition(2852, camera.getHeight() - house7_2.getHeight());
		house7_3.setPosition(7204, camera.getHeight() - house7_3.getHeight());
		house7_4.setPosition(11431, camera.getHeight() - house7_4.getHeight());
		//

		// house 8
		house8_1.setPosition(1760, camera.getHeight() - house8_1.getHeight());

		// coins

		coin1_2.setPosition(1960,
				(camera.getHeight() - house8_1.getHeight() - 2 * (coin1_2
						.getHeight())));
		//

		house8_2.setPosition(6635, camera.getHeight() - house8_2.getHeight());

		// coins

		coin1_10.setPosition(6752,
				(camera.getHeight() - house8_2.getHeight() - 2 * (coin1_10
						.getHeight())));
		//
		// coins

		coin1_11.setPosition(6869,
				(camera.getHeight() - house8_2.getHeight() - 2 * (coin1_11
						.getHeight())));
		//

		// coins

		coin1_12.setPosition(6986,
				(camera.getHeight() - house8_2.getHeight() - 2 * (coin1_12
						.getHeight())));
		//

		//

		// house 9
		house9_1.setPosition(5630, camera.getHeight() - house9_1.getHeight());

		//

		// house 10
		house10_1.setPosition(6354, camera.getHeight() - house10_1.getHeight());

		//

		// house 11
		house11_1.setPosition(903, camera.getHeight() - house11_1.getHeight());
		house11_2.setPosition(3937, camera.getHeight() - house11_2.getHeight());

		//

		// paid 1

		pad1_1.setPosition(2978, camera.getHeight() - pad1_1.getHeight());
		pad1_2.setPosition(4549, camera.getHeight() - pad1_2.getHeight());

		// paid 2 tree

		// tree 2
		pad2_1.setPosition(4399, camera.getHeight() - pad2_1.getHeight());

		//

		// tree 3

		pad3_1.setPosition(2430, camera.getHeight() - pad3_1.getHeight());

		// coins

		coin1_3.setPosition(2490,
				(camera.getHeight() - pad3_1.getHeight() - 2 * (coin1_3
						.getHeight())));
		//

		pad3_2.setPosition(4227, camera.getHeight() - pad3_2.getHeight());
		// coins

		coin1_5.setPosition(4288,
				(camera.getHeight() - pad3_2.getHeight() - 2 * (coin1_5
						.getHeight())));
		//

		// paed 4 tree

		pad4_1.setPosition(4137, camera.getHeight() - pad4_1.getHeight());

		//

		// car 1

		car1_1.setPosition(3427, camera.getHeight() - car1_1.getHeight());

		coin1_4.setPosition(3477,
				(camera.getHeight() - car1_1.getHeight() - 2 * (coin1_4
						.getHeight())));

		//

		// car 2

		car2_1.setPosition(5864, camera.getHeight() - car2_1.getHeight());

		coin1_9.setPosition(5914, camera.getHeight() - car2_1.getHeight() - 2
				* (coin1_9.getHeight()));
		//

		// road position

		road1_1.setPosition(7290, camera.getHeight()
				- (3 * road1_1.getHeight()) / 2);

		coin1_13.setPosition(7540,
				(camera.getHeight() - 2 * (coin1_13.getHeight())));
		coin1_14.setPosition(8040,
				(camera.getHeight() - 2 * (coin1_13.getHeight())));
		//

		road1_2.setPosition(8290, camera.getHeight()
				- (3 * road1_2.getHeight()) / 2);

		coin1_15.setPosition(8540,
				(camera.getHeight() - 2 * (coin1_14.getHeight())));
		coin1_16.setPosition(9040,
				(camera.getHeight() - 2 * (coin1_13.getHeight())));
		
		
		// road3
		
		road1_3.setPosition(9290, camera.getHeight()
				- (3 * road1_3.getHeight()) / 2);

		coin1_17.setPosition(9540,
				(camera.getHeight() - 2 * (coin1_17.getHeight())));
		coin1_18.setPosition(10040,
				(camera.getHeight() - 2 * (coin1_18.getHeight())));
		//
		
		// road4
		
				road1_4.setPosition(10290, camera.getHeight()
						- (3 * road1_4.getHeight()) / 2);

				coin1_19.setPosition(10540,
						(camera.getHeight() - 2 * (coin1_19.getHeight())));
				coin1_20.setPosition(11040,
						(camera.getHeight() - 2 * (coin1_20.getHeight())));
				//
				
		
		
		//
				
				
	// sea region start here
				
				
				
				seaStart.setPosition(11717,camera.getHeight()-seaStart.getHeight());	
				seaStart1.setPosition(11520,camera.getHeight()-seaStart1.getHeight());
				palm.setPosition(11567,camera.getHeight()-palm.getHeight());		
				
				seaBackground1_1.setPosition(12217,camera.getHeight()-seaBackground1_1.getHeight());
				seaBackground2_1.setPosition(12807,camera.getHeight()-seaBackground2_1.getHeight());
				seaBackground2_2.setPosition(13397,camera.getHeight()-seaBackground2_2.getHeight());
				seaBackground2_3.setPosition(13987,camera.getHeight()-seaBackground2_3.getHeight());
				seaBackground2_4.setPosition(14577,camera.getHeight()-seaBackground2_4.getHeight());
				seaBackground2_5.setPosition(15167,camera.getHeight()-seaBackground2_5.getHeight());
				seaBackground2_6.setPosition(15757,camera.getHeight()-seaBackground2_6.getHeight());
				seaBackground2_7.setPosition(16347,camera.getHeight()-seaBackground2_7.getHeight());
				seaBackground2_8.setPosition(16937,camera.getHeight()-seaBackground2_8.getHeight());
				seaBackground2_9.setPosition(17527,camera.getHeight()-seaBackground2_9.getHeight());
				seaBackground2_10.setPosition(18117,camera.getHeight()-seaBackground2_10.getHeight());
				
				seaBackground2_11.setPosition(18707,camera.getHeight()-seaBackground2_11.getHeight());
			
		
				// ship 1
				
				ship1_1.setPosition(14457,camera.getHeight()-ship1_1.getHeight());
				coin1_23.setPosition(14707,
						(camera.getHeight() - 5 * (coin1_23
								.getHeight())));
				ship1_2.setPosition(16324,camera.getHeight()-ship1_2.getHeight());
				
				coin1_25.setPosition(16574,
						(camera.getHeight() - 4 * (coin1_23
								.getHeight())));
	            // ship 2
				
				ship2_1.setPosition(13565,camera.getHeight()-ship2_1.getHeight());
				coin1_22.setPosition(13839,
						(camera.getHeight() - 5 * (coin1_22
								.getHeight())));
				
				ship2_2.setPosition(17405,camera.getHeight()-ship2_2.getHeight());
				coin1_27.setPosition(17679,
						(camera.getHeight() - 5 * (coin1_27
								.getHeight())));
	            // ship 3
				
				ship3_1.setPosition(14127,camera.getHeight()-ship3_1.getHeight());
				ship3_2.setPosition(17957,camera.getHeight()-ship3_2.getHeight());
				coin1_28.setPosition(18312,
						(camera.getHeight() - 5 * (coin1_28
								.getHeight())));
				
				// ship 4
				
				ship4_1.setPosition(13245,camera.getHeight()-ship4_1.getHeight());
				ship4_2.setPosition(15719,camera.getHeight()-ship4_2.getHeight());
				coin1_24.setPosition(16064,
						(camera.getHeight() - 4 * (coin1_24
								.getHeight())));
	           // ship 5
				
				ship5_1.setPosition(12807,camera.getHeight()-ship5_1.getHeight());
				ship5_2.setPosition(16862,camera.getHeight()-ship5_2.getHeight());
				coin1_26.setPosition(17127,
						(camera.getHeight() - 4 * (coin1_24
								.getHeight())));

				// coins

				coin1_21.setPosition(13057,
						(camera.getHeight() - 4 * (coin1_21
								.getHeight())));
				//
              // ship 6
				
				ship6_1.setPosition(15040,camera.getHeight()-ship6_1.getHeight());
				
				ship6_2.setPosition(18690,camera.getHeight()-ship6_2.getHeight());
				
				

		// empty space coin position

		coin1_6.setPosition(4826,
				(camera.getHeight() - 2 * (coin1_6.getHeight())));

		coin1_7.setPosition(4964,
				(camera.getHeight() - 2 * (coin1_7.getHeight())));

		coin1_8.setPosition(5101,
				(camera.getHeight() - 2 * (coin1_8.getHeight())));

		//
	
		// attachment
		attachChild(house1_1);

		attachChild(house2_1);
		attachChild(coin1_1);

		attachChild(house3_1);

		attachChild(house4_1);

		attachChild(house5_1);

		attachChild(house5_2);
		attachChild(house5_3);

		attachChild(house6_1);

		attachChild(house7_1);
		attachChild(house7_2);
		attachChild(house7_3);
		attachChild(house7_4);
		
		
		
		attachChild(house8_1);
		attachChild(coin1_2);
		attachChild(house8_2);
		attachChild(coin1_10);
		attachChild(coin1_11);
		attachChild(coin1_12);

		attachChild(house9_1);

		attachChild(house10_1);

		attachChild(house11_1);
		attachChild(house11_2);

		attachChild(pad1_1);
		attachChild(pad1_2);

		attachChild(pad2_1);

		attachChild(pad3_1);
		attachChild(coin1_3);
		attachChild(pad3_2);
		attachChild(coin1_5);

		attachChild(pad4_1);

		attachChild(car1_1);
		attachChild(coin1_4);

		attachChild(car2_1);
		attachChild(coin1_9);

		// road
		attachChild(road1_1);

		attachChild(coin1_13);
		attachChild(coin1_14);

		attachChild(road1_2);

		attachChild(coin1_15);
		attachChild(coin1_16);

		//road3
		
		attachChild(road1_3);

		attachChild(coin1_17);
		attachChild(coin1_18);
		
		
		//
		
		// road 4
		
		attachChild(road1_4);

		attachChild(coin1_19);
		attachChild(coin1_20);
		
		//
		
		attachChild(bird1);

		attachChild(bird2);

		attachChild(bird3);

		attachChild(bird4);

		attachChild(bird5);

		attachChild(bird6);

		attachChild(bird7);

		attachChild(bird8);

		attachChild(bird9);

		attachChild(bird10);
		
		attachChild(bird11);

		attachChild(bird12);

		attachChild(bird13);

		attachChild(bird14);

		attachChild(bird15);

		attachChild(bird16);

		attachChild(bird17);

		attachChild(bird18);

		attachChild(bird19);

		attachChild(bird20);
		
		
		attachChild(bird21);

		attachChild(bird22);

		attachChild(bird23);

		attachChild(bird24);

		attachChild(bird25);

		attachChild(bird26);

		attachChild(bird27);

		attachChild(bird28);

		attachChild(bird29);

		attachChild(bird30);
		
		
		
		attachChild(bird31);

		attachChild(bird32);

		attachChild(bird33);

		attachChild(bird34);

		attachChild(bird35);

		attachChild(bird36);

		attachChild(bird37);

		attachChild(bird38);

		attachChild(bird39);


		
		

		// attach empty coin

		attachChild(coin1_6);
		attachChild(coin1_7);
		attachChild(coin1_8);

		//
		attachChild(moving_car);
		attachChild(moving_car2);
		attachChild(moving_bus);
		attachChild(moving_bus2);
		//
		
		//attach sea_region
		
		attachChild(seaStart);
		attachChild(seaStart1);
		attachChild(palm);
		
		attachChild(seaBackground1_1);
		
		attachChild(seaBackground2_1);
		attachChild(seaBackground2_2);
		attachChild(seaBackground2_3);
		attachChild(seaBackground2_4);
		attachChild(seaBackground2_5);
		attachChild(seaBackground2_6);
		attachChild(seaBackground2_7);
		attachChild(seaBackground2_8);
		attachChild(seaBackground2_9);
		attachChild(seaBackground2_10);
		attachChild(seaBackground2_11);
		

		// ship 1
		
		attachChild(ship1_1);
		attachChild(coin1_23);
		attachChild(ship1_2);
		attachChild(coin1_25);
		// ship 2
		
		attachChild(ship2_1);
		attachChild(coin1_22);
		attachChild(ship2_2);
		attachChild(coin1_27);
		// ship 3
		
		attachChild(ship3_1);
		attachChild(ship3_2);
		attachChild(coin1_28);
		//ship 4
		attachChild(ship4_1);
		attachChild(ship4_2);
		attachChild(coin1_24);

		// ship 5
		
		attachChild(ship5_1);
		attachChild(coin1_21);
		attachChild(ship5_2);
		attachChild(coin1_26);
		// ship 6
		attachChild(ship6_1);
		
		attachChild(ship6_2);
		
		
	
		
		
		
		attachChild(game_player_initial);
		attachChild(platform);

	}

	// screen touch listener

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {

			if (!firstTouch) {
				camera.setChaseEntity(game_player_initial);
				firstTouch = true;

				helicopter.setLooping(true);

				helicopter.play();

				final long[] BIRD1_ANIMATE = new long[] { 100, 100 };

				bird1.animate(BIRD1_ANIMATE, 0, 1, true);

				//

				final long[] BIRD2_ANIMATE = new long[] { 100, 100 };

				bird2.animate(BIRD2_ANIMATE, 0, 1, true);

				final long[] BIRD3_ANIMATE = new long[] { 100, 100 };

				bird3.animate(BIRD3_ANIMATE, 0, 1, true);

				final long[] BIRD4_ANIMATE = new long[] { 100, 100 };

				bird4.animate(BIRD4_ANIMATE, 0, 1, true);

				final long[] BIRD5_ANIMATE = new long[] { 100, 100 };

				bird5.animate(BIRD5_ANIMATE, 0, 1, true);

				final long[] BIRD6_ANIMATE = new long[] { 100, 100 };

				bird6.animate(BIRD6_ANIMATE, 0, 1, true);
				final long[] BIRD7_ANIMATE = new long[] { 100, 100 };

				bird7.animate(BIRD7_ANIMATE, 0, 1, true);

				final long[] BIRD8_ANIMATE = new long[] { 100, 100 };

				bird8.animate(BIRD8_ANIMATE, 0, 1, true);

				final long[] BIRD9_ANIMATE = new long[] { 100, 100 };

				bird9.animate(BIRD9_ANIMATE, 0, 1, true);

				final long[] BIRD10_ANIMATE = new long[] { 100, 100 };

				bird10.animate(BIRD10_ANIMATE, 0, 1, true);
				
				//
				
				final long[] BIRD11_ANIMATE = new long[] { 100, 100 };

				bird11.animate(BIRD11_ANIMATE, 0, 1, true);

		

				final long[] BIRD12_ANIMATE = new long[] { 100, 100 };

				bird12.animate(BIRD12_ANIMATE, 0, 1, true);

				final long[] BIRD13_ANIMATE = new long[] { 100, 100 };

				bird13.animate(BIRD13_ANIMATE, 0, 1, true);

				final long[] BIRD14_ANIMATE = new long[] { 100, 100 };

				bird14.animate(BIRD14_ANIMATE, 0, 1, true);

				final long[] BIRD15_ANIMATE = new long[] { 100, 100 };

				bird15.animate(BIRD15_ANIMATE, 0, 1, true);

				final long[] BIRD16_ANIMATE = new long[] { 100, 100 };

				bird16.animate(BIRD16_ANIMATE, 0, 1, true);
				final long[] BIRD17_ANIMATE = new long[] { 100, 100 };

				bird17.animate(BIRD17_ANIMATE, 0, 1, true);

				final long[] BIRD18_ANIMATE = new long[] { 100, 100 };

				bird18.animate(BIRD18_ANIMATE, 0, 1, true);

				final long[] BIRD19_ANIMATE = new long[] { 100, 100 };

				bird19.animate(BIRD19_ANIMATE, 0, 1, true);

				final long[] BIRD20_ANIMATE = new long[] { 100, 100 };

				bird20.animate(BIRD20_ANIMATE, 0, 1, true);
				
				
				
	//
				
				final long[] BIRD21_ANIMATE = new long[] { 100, 100 };

				bird21.animate(BIRD21_ANIMATE, 0, 1, true);

		

				final long[] BIRD22_ANIMATE = new long[] { 100, 100 };

				bird22.animate(BIRD22_ANIMATE, 0, 1, true);

				final long[] BIRD23_ANIMATE = new long[] { 100, 100 };

				bird23.animate(BIRD23_ANIMATE, 0, 1, true);

				final long[] BIRD24_ANIMATE = new long[] { 100, 100 };

				bird24.animate(BIRD24_ANIMATE, 0, 1, true);

				final long[] BIRD25_ANIMATE = new long[] { 100, 100 };

				bird25.animate(BIRD25_ANIMATE, 0, 1, true);

				final long[] BIRD26_ANIMATE = new long[] { 100, 100 };

				bird26.animate(BIRD26_ANIMATE, 0, 1, true);
				final long[] BIRD27_ANIMATE = new long[] { 100, 100 };

				bird27.animate(BIRD27_ANIMATE, 0, 1, true);

				final long[] BIRD28_ANIMATE = new long[] { 100, 100 };

				bird28.animate(BIRD28_ANIMATE, 0, 1, true);

				final long[] BIRD29_ANIMATE = new long[] { 100, 100 };

				bird29.animate(BIRD29_ANIMATE, 0, 1, true);

				final long[] BIRD30_ANIMATE = new long[] { 100, 100 };

				bird30.animate(BIRD30_ANIMATE, 0, 1, true);
				
				
				
//
				
				final long[] BIRD31_ANIMATE = new long[] { 100, 100 };

				bird31.animate(BIRD31_ANIMATE, 0, 1, true);

		

				final long[] BIRD32_ANIMATE = new long[] { 100, 100 };

				bird32.animate(BIRD32_ANIMATE, 0, 1, true);

				final long[] BIRD33_ANIMATE = new long[] { 100, 100 };

				bird33.animate(BIRD33_ANIMATE, 0, 1, true);

				final long[] BIRD34_ANIMATE = new long[] { 100, 100 };

				bird34.animate(BIRD34_ANIMATE, 0, 1, true);

				final long[] BIRD35_ANIMATE = new long[] { 100, 100 };

				bird35.animate(BIRD35_ANIMATE, 0, 1, true);

				final long[] BIRD36_ANIMATE = new long[] { 100, 100 };

				bird36.animate(BIRD36_ANIMATE, 0, 1, true);
				final long[] BIRD37_ANIMATE = new long[] { 100, 100 };

				bird37.animate(BIRD37_ANIMATE, 0, 1, true);

				final long[] BIRD38_ANIMATE = new long[] { 100, 100 };

				bird38.animate(BIRD38_ANIMATE, 0, 1, true);

				final long[] BIRD39_ANIMATE = new long[] { 100, 100 };

				bird39.animate(BIRD39_ANIMATE, 0, 1, true);

				
				
				
				
				
				
				
				

				canRun = true;
			} else {
				// jump function

				if (game_player_initial.getY() > 0) {

					body.setLinearVelocity(new Vector2(
							body.getLinearVelocity().x, -3));

				}

			}
			// end of jump function
		}

		return false;
	}

	//

	// other function

	// game over message

	private void createGameOverText() {
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over !!!", vbom);
	}

	private void displayGameOverText() {
		helicopter.stop();
		collide.play();
		updateHighScore();
		camera.setChaseEntity(null);

		gameOverText.setPosition(camera.getCenterX() - 70,
				camera.getCenterY() - 40);
		attachChild(gameOverText);
		gameOverDisplayed = true;

		unregisterUpdateHandler(physicsWorld);
		 unregisterUpdateHandler(physicsWorld2);
		 
			SceneManager.getInstance().loadMenuScene(engine);
		 

	}

	// displaying winner at the end
	
	
	private void createWinnerText() {
		winnerText = new Text(0, 0, resourcesManager.font, "......W I N N E R.......", vbom);
		
		
	}

	private void winnerText() {
		helicopter.stop();

		updateHighScore();
		 camera.setChaseEntity(null);

		winnerText.setPosition(camera.getCenterX()-190,
				camera.getCenterY() - 40);
		attachChild(winnerText);
		winnerTextDisplayed = true;

		 unregisterUpdateHandler(physicsWorld);
		 unregisterUpdateHandler(physicsWorld2);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void pauseGame() {
		unregisterUpdateHandler(physicsWorld);
		unregisterUpdateHandler(physicsWorld2);
		createMenuChildScene();

	}

	//

	public void unpauseGame() {

		menuChildScene.setVisible(false);
		registerUpdateHandler(physicsWorld);
		registerUpdateHandler(physicsWorld2);

	}

	//
	public void pauseMusic() {

		

			helicopter.pause();
		

	}

	//

	public void resumeMusic() {

		
			helicopter.resume();

		

	}

	// load Audio
	public void loadAudio() {
		helicopter = resourcesManager.helicopter;
		coinTaking = resourcesManager.coinTaking;
		collide = resourcesManager.collide;

		helicopter.setVolume(0.15f);
		
		helicopter.setLooping(true);
		collide.setLooping(false);
		coinTaking.setLooping(false);
	}

	//

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(30, 30);

		final IMenuItem resumeGame = new ScaleMenuItemDecorator(
				new SpriteMenuItem(Resume_play, resourcesManager.resume, vbom),
				1.2f, 1);

		final IMenuItem loadMenu = new ScaleMenuItemDecorator(
				new SpriteMenuItem(mainMenu, resourcesManager.main_menu, vbom),
				1.2f, 1);

		menuChildScene.addMenuItem(resumeGame);
		menuChildScene.addMenuItem(loadMenu);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		resumeGame.setPosition(resumeGame.getX(), loadMenu.getY());
		loadMenu.setPosition(resumeGame.getX(), loadMenu.getY() + 70);
		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case Resume_play:
			unpauseGame();
			resumeMusic();

			return true;
		case mainMenu:
			updateHighScore();
			SceneManager.getInstance().loadMenuScene(engine);

			return true;
		default:
			return false;
		}
	}

	// update high score
	public void updateHighScore() {

		Read_High_Score();

		if (score > highScore) {

			WriteHighScore();
		}

	}

	// writing high Score;;;;'

	public void WriteHighScore() {
		FileOutputStream fos = null;
		DataOutputStream dos;

		try {
			fos = activity.openFileOutput("High_Score", Context.MODE_PRIVATE);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

		}

		// writing

		try {
			dos = new DataOutputStream(fos);

			// dos.write(unlockedLevelId, 0, Integer.SIZE)
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

	// reading High Score
	//
	public void Read_High_Score() {

		FileInputStream fos = null;
		DataInputStream dos;
		dos = new DataInputStream(fos);

		try {
			fos = activity.openFileInput("High_Score");
		}

		catch (FileNotFoundException e) {

			return;

		}

		// reading file

		try {
			dos = new DataInputStream(fos);
			int t = -1;
			t = dos.readInt();
			highScore = t;
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

}
