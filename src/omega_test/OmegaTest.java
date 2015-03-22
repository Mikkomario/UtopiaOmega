package omega_test;

import genesis_event.ActorHandler;
import genesis_event.DrawableHandler;
import genesis_event.HandlerRelay;
import genesis_event.KeyListenerHandler;
import genesis_event.MouseListenerHandler;
import genesis_util.Vector3D;
import genesis_video.GamePanel;
import genesis_video.GameWindow;

/**
 * OmegaTest tests the simple transformations
 * 
 * @author Mikko Hilpinen
 * @since 24.12.2014
 */
public class OmegaTest
{
	// CONSTRUCTOR	----------------------------
	
	private OmegaTest()
	{
		// The constructor is hidden since the interface is static
	}

	
	// MAIN METHOD	----------------------------
	
	/**
	 * Starts the test
	 * @param args Not used
	 */
	public static void main(String[] args)
	{
		// Creates the window
		GameWindow window = new GameWindow(new Vector3D(300, 300), "Omega Test", true, 120, 
				20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Creates the handlers
		HandlerRelay handlers = new HandlerRelay();
		handlers.addHandler(new DrawableHandler(true, panel.getDrawer()));
		handlers.addHandler(new KeyListenerHandler(true, window.getHandlerRelay()));
		handlers.addHandler(new MouseListenerHandler(true, window.getHandlerRelay()));
		handlers.addHandler(new ActorHandler(true, window.getStepHandler()));
		
		// Creates the test objects
		IndependentTestObject o = new IndependentTestObject(handlers, new Vector3D(150, 150));
		new DependentTestObject(o, handlers);
		
		new TestTransformationInputObject(handlers, new Vector3D(150, 150));
	}
}
