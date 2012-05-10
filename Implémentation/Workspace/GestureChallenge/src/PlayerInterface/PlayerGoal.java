package PlayerInterface;

import org.jbox2d.dynamics.World;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import physicsShapes.PhysicsCircle;
import processing.core.PApplet;

public class PlayerGoal extends PhysicsCircle {

	public PlayerGoal(PApplet applet, Vector3D centerPoint,
			World world,
			float worldScale, MTColor color) {
		super(applet, centerPoint, 40, world, 0f, 0f, 0f,
				worldScale);
		//MTColor col1 = new MTColor(ToolsMath.getRandom(60, 255),ToolsMath.getRandom(60, 255),ToolsMath.getRandom(60, 255));
		this.setFillColor(color);
		color.setColor(color.getR()-50, color.getG()-30, color.getB()-30);
		this.setStrokeColor(color);
		this.setStrokeWeight(4);
	}

}
