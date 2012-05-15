package PlayerInterface;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import physicsShapes.PhysicsShield;

import GameModel.Constants;
import GestureChallengeScene.GestureChallengeScene;

public class PlayerInterface {

	MTColor myColor;
	int myNumber;
	GestureChallengeScene myGCS;
	PlayerGoal myPG;
	PhysicsShield myPS;
	PlayerBulletLoader myBL;
	float myAngle;
	int playerNumber;
	int myCollisionID;
	int myBulletMask;
	PlayerMovableShieldArea mySA;
	
	
	
	public GestureChallengeScene getMyGCS() {
		return myGCS;
	}



	public PlayerGoal getMyPG() {
		return myPG;
	}



	public int getMyBulletMask() {
		return myBulletMask;
	}



	public MTColor getMyColor() {
		return myColor;
	}



	public int getMyCollisionID() {
		return myCollisionID;
	}



	public PlayerInterface(MTColor color, int playerID,float angle,GestureChallengeScene gCS,int playerNumber){
		
		//init
		myColor=color;
		myNumber=playerID;
		myGCS = gCS;
		myAngle=angle;
		this.playerNumber = playerNumber;
		
		

		
		
		//creation Goal
		float x = myGCS.getMTApplication().width/2f +(float) (Math.cos(angle)*Constants.radiusCenterGoals);
		float y = myGCS.getMTApplication().height/2f +(float) (Math.sin(angle)*Constants.radiusCenterGoals);
		
		//Creation mobileShield area
		mySA = new PlayerMovableShieldArea(this, new Vector3D(x,y));
		this.myGCS.getPhysicsContainer().addChild(mySA);
		
		myPG=new PlayerGoal(myGCS.getMTApplication(), new Vector3D(x,y), myGCS.getWorld(), myGCS.getScale(), myColor,this.myNumber+1,angle);
		myGCS.getPhysicsContainer().addChild(myPG);

		//creation shields
		
		x = x -(float) (Math.cos(angle)*Constants.shieldDistance);
		y = y -(float) (Math.sin(angle)*Constants.shieldDistance);
		float coveredAngle = (float) Math.toRadians(180/(float)playerNumber);
		myPS=new PhysicsShield(Constants.shieldBigRadius,Constants.shieldSmallRadius,Constants.shieldSmallDef,Constants.shieldBigDef,coveredAngle,new Vector3D(x,y),myGCS.getMTApplication(),myGCS.getWorld(),0f,0f,0f,myGCS.getScale(),color);
		myGCS.getPhysicsContainer().addChild(myPS);		
		myPS.getBody().setXForm(
				myPS.getBody().getPosition(), (float) (angle+Math.PI/2f)
		);
		
		//Rotating shield gesture listener
		myPG.registerInputProcessor(new TapProcessor(myGCS.getMTApplication()));
		myPG.addGestureListener(TapProcessor.class, new IGestureEventListener(){

			@Override
			public boolean processGestureEvent(MTGestureEvent arg0) {
				TapEvent te = (TapEvent) arg0;
				if(te!=null){
					if(te.isTapped()){						
						float angle = (float) (myPS.getBody().getAngle()+Math.PI/2f/*(float) (myPS.getBody().getAngle()+ Math.toRadians(90))*/);
						System.out.println("angle:"+angle);
						System.out.println("trig: "+Math.cos(angle)+"/"+Math.sin(angle));
						Vec2 newPos = new Vec2();
						Vec2 diff = new Vec2();
						diff = myPG.getBody().getPosition().sub(myPS.getBody().getPosition());
						newPos.x+=myPG.getBody().getPosition().x+Math.cos(angle)*diff.length();
						newPos.y+=myPG.getBody().getPosition().y+Math.sin(angle)*diff.length();
						float test = myPS.getBody().getAngle();
						myPS.getBody().setXForm(newPos, (float) (test+ Math.toRadians(10)));
					}					
				}
				return false;
			}
			
		});

		
		//Avoid collision between playerInterface and its own bullets
		

		myCollisionID = (int) Math.pow(2, this.myNumber+1);
		
		myBulletMask =0;
		for(int i =0;i<=playerNumber;i++){
			myBulletMask+=(int) Math.pow(2, i);
		}
		
		myBulletMask-=myCollisionID;
		
		System.out.println("P"+myNumber+" mask:"+myBulletMask);
		
		
		myPS.getBody().getShapeList().m_filter.categoryBits=myCollisionID;
		myPS.getBody().getShapeList().m_filter.maskBits=myCollisionID;
		myPS.getBody().getShapeList().m_filter.groupIndex=0;
		System.out.println("PS"+myNumber+" cat("+myPS.getBody().getShapeList().m_filter.categoryBits+")/mask("+myPS.getBody().getShapeList().m_filter.maskBits+")/group("+myPS.getBody().getShapeList().m_filter.groupIndex+")");
		System.out.println("PS"+myNumber+" cat("+myPS.getBody().m_shapeList.m_filter.categoryBits+")/mask("+myPS.getBody().m_shapeList.m_filter.maskBits+")/group("+myPS.getBody().m_shapeList.m_filter.groupIndex+")");
		Shape shape;
		shape=myPS.getBody().getShapeList();
		for (Shape s = myPS.getBody().getShapeList();
			     s != null;
			     s = s.getNext()){
			s.m_filter.categoryBits=myCollisionID;
			s.m_filter.maskBits=myCollisionID;
			s.m_filter.groupIndex=0;
			//System.out.println(i);
		}
		
		myPG.getBody().getShapeList().m_filter.categoryBits=myCollisionID;
		myPG.getBody().getShapeList().m_filter.maskBits=myCollisionID;
		myPG.getBody().getShapeList().m_filter.groupIndex=0;
		System.out.println("PG"+myNumber+" cat("+myPG.getBody().getShapeList().m_filter.categoryBits+")/mask("+myPG.getBody().getShapeList().m_filter.maskBits+")");
	

		
		//Creation bullets
		myBL = new PlayerBulletLoader(this);
		



	
	}
}
