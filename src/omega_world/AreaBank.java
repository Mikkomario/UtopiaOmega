package omega_world;

import arc_bank.Bank;
import arc_bank.BankBank;
import arc_bank.BankBankInitializer;
import arc_bank.BankObjectConstructor;
import arc_bank.GamePhaseBank;
import arc_bank.MultiMediaHolder;
import arc_bank.ResourceInitializationException;
import arc_resource.GamePhase;

/**
 * AreaBank is a static accessor to the area resource.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public class AreaBank
{
	// CONSTRUCTOR	--------------------------
	
	private AreaBank()
	{
		// The constructor is hidden since the interface is static
	}
	
	
	// OTHER METHODS	----------------------
	
	/**
	 * Retrieves an areaBank from the currently active banks
	 * @param bankName The name of the areaBank
	 * @return An areaBank with the given name
	 */
	@SuppressWarnings("unchecked")
	public Bank<Area> getAreaBank(String bankName)
	{
		return (Bank<Area>) MultiMediaHolder.getBank(OmegaResourceType.AREA, bankName);
	}
	
	/**
	 * Retrieves an area from an active area bank
	 * @param bankName The name of the areaBank that contains the area
	 * @param areaName The name of the area in the bank
	 * @return An area from the bank
	 */
	public Area getArea(String bankName, String areaName)
	{
		return getAreaBank(bankName).get(areaName);
	}
	
	/**
	 * Activates all areas that have been initialized
	 */
	public static void activateAreas()
	{
		for (String bankName : MultiMediaHolder.getBankNames(OmegaResourceType.AREA))
		{
			MultiMediaHolder.activateBank(OmegaResourceType.AREA, bankName, false);
		}
	}
	
	/**
	 * Initializes the area resources. The GamePhases should be initialized before this method 
	 * is called (which means that areas can't be used in the GamePhases).
	 * @param fileName The name of the file that contains construction information 
	 * ("data/" automatically included). The file should be formatted as follows:<br>
	 * &bankName1<br>
	 * areaName1#phaseName1 (phaseBankName1#phaseName1 and 
	 * phaseBankName1#phaseName1#areaObjectFile are also possible)<br>
	 * areaName2#phaseName2<br>
	 * ...<br>
	 * @param handlerConstructor The object that will construct the required handlers
	 * @param objectConstructorProvider The object that will provide the created 
	 * AreaObjectCreators with suitable object constructors
	 * @param activateAreas Should the areas be activated at this time so that they can 
	 * freely be accessed and used.
	 */
	public static void initializeAreaResources(String fileName, 
			AreaHandlerConstructor handlerConstructor, 
			AreaObjectConstructorProvider objectConstructorProvider, boolean activateAreas)
	{
		MultiMediaHolder.initializeResourceDatabase(createAreaBankBank(fileName, 
				handlerConstructor, objectConstructorProvider));
		
		if (activateAreas)
			activateAreas();
	}
	
	/**
	 * Creates a new "AreaRelay relay" that handles different areaBanks.
	 * @param fileName The name of the file that contains construction information 
	 * ("data/" automatically included). The file should be formatted as follows:<br>
	 * &bankName1<br>
	 * areaName1#phaseName1 (phaseBankName1#phaseName1 and 
	 * phaseBankName1#phaseName1#areaObjectFile are also possible)<br>
	 * areaName2#phaseName2<br>
	 * ...<br>
	 * &bankName2<br>
	 * ...<br>
	 * @param handlerConstructor The object that will construct the required handlers
	 * @param objectConstructorProvider The object that will provide the created 
	 * AreaObjectCreators with suitable object constructors
	 * @return A bankBank for areas
	 */
	public static BankBank<Area> createAreaBankBank(String fileName, 
			AreaHandlerConstructor handlerConstructor, 
			AreaObjectConstructorProvider objectConstructorProvider)
	{
		return new BankBank<>(new BankBankInitializer<>(fileName, new AreaBankConstructor(), 
				new AreaConstructor(handlerConstructor, objectConstructorProvider)), 
				OmegaResourceType.AREA);
	}
	
	
	// SUBCLASSES	--------------------------
	
	private static class AreaConstructor implements BankObjectConstructor<Area>
	{
		// ATTRIBUTES	----------------------
		
		private AreaHandlerConstructor handlerConstructor;
		private AreaObjectConstructorProvider objectConstructorProvider;
		
		
		// CONSTRUCTOR	----------------------
		
		public AreaConstructor(AreaHandlerConstructor handlerConstructor, 
				AreaObjectConstructorProvider objectConstructorProvider)
		{
			this.handlerConstructor = handlerConstructor;
			this.objectConstructorProvider = objectConstructorProvider;
		}
		
		
		// IMPLEMENTED METHODS	---------------
		
		@Override
		public void construct(String line, Bank<Area> bank)
		{
			// Line contains information:
			// objectName#phaseName
			// OR objectName#phaseBankName#phaseName
			// OR objectName#phaseBankName#phaseName#objectConstructorFileName
			String[] arguments = line.split("#");
			
			if (arguments.length < 2)
				throw new ResourceInitializationException("Can't construct an area from line: " 
						+ line + ". The line has too few arguments.");
			
			GamePhase phase = null;
			
			if (arguments.length >= 3)
				phase = GamePhaseBank.getGamePhase(arguments[1], arguments[2]);
			else
				phase = GamePhaseBank.getGamePhase(arguments[1]);
			
			Area newArea = new Area(arguments[0], phase, 
					this.handlerConstructor.constructRelay(arguments[1]));
			bank.put(arguments[0], newArea);
			
			// Creates an objectCreator if possible
			if (arguments.length >= 4)
				new AreaObjectCreator(
						this.objectConstructorProvider.getConstructor(arguments[0]), 
						arguments[3], newArea);
		}
	}
	
	private static class AreaBankConstructor implements BankObjectConstructor<Bank<Area>>
	{
		@Override
		public void construct(String line, Bank<Bank<Area>> bank)
		{
			bank.put(line, new Bank<>());
		}	
	}
}