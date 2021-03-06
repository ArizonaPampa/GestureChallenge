package gesture.threefingersgesture.examples;

import gesture.threefingersgesture.ThreeFingersAverageGestureEvent;
import gesture.threefingersgesture.ThreeFingersAverageGestureProcessor;
import gesture.threefingersgesture.ThreeFingersGestureEvent;
import gesture.threefingersgesture.ThreeFingersGestureProcessor;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.logging.ILogger;
import org.mt4j.util.logging.MTLoggerFactory;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLFBO;


public class My_Scene_Average extends AbstractScene{


	/** The app. */
	private AbstractMTApplication app;

	/** The has fbo. */
	private boolean hasFBO;

	/** The switch directly to scene. */
	private boolean switchDirectlyToScene = false;

	private MTRectangle myPlot;
	private MTEllipse myEllipse;

	public My_Scene_Average(final AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.hasFBO = GLFBO.isSupported(app);
		this.switchDirectlyToScene = !this.hasFBO || switchDirectlyToScene;
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		AbstractShape.createDefaultGestures = false;

		myPlot = new MTRectangle(700, 700, mtApplication);
		myPlot.setPositionGlobal(new Vector3D(app.width/2f,app.height/2f));
		//myPlot.setStrokeColor(MTColor.GREEN);
		myPlot.setStrokeWeight(4);
		myPlot.setStrokeColor(MTColor.WHITE);
		myPlot.setFillColor(MTColor.LIME);
		this.getCanvas().addChild(myPlot);
		MTTextArea tA = new MTTextArea(myPlot.getWidthXY(TransformSpace.GLOBAL)/2f-230,myPlot.getHeightXY(TransformSpace.GLOBAL)/2f-25,460f,50f,FontManager.getInstance().createFont(app, "arial", 40),app);
		tA.setText("Rotate me with 5 fingers !");
		tA.setNoFill(true);
		tA.removeAllGestureEventListeners();
		tA.setPickable(false);
		tA.setNoStroke(true);
		myPlot.addChild(tA);





		myPlot.registerInputProcessor(new ThreeFingersAverageGestureProcessor(mtApplication));
		myPlot.addGestureListener(ThreeFingersAverageGestureProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				ThreeFingersAverageGestureEvent evt = (ThreeFingersAverageGestureEvent) ge;
				float angle = evt.getRotationAngleDegree();
				System.out.println(angle);
				myPlot.rotateZ(myPlot.getCenterPointGlobal(), angle);
				return false;
			}
		});
	}

}