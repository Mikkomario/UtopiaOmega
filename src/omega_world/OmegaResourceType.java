package omega_world;

import arc_resource.ResourceType;

/**
 * The different types of resources introduced in the omega module
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public enum OmegaResourceType implements ResourceType
{
	/**
	 * Areas contain different GameObjects. Areas should not be introduced in a GamePhase 
	 * since they control GamePhases and require them in order to work.
	 */
	AREA;
}
