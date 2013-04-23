package mekanism.api;

/**
 * Implement this if your tile entity can store some form of gas.  If you want your item to store gas, implement IStorageTank
 * instead.
 * @author AidanBrady
 *
 */
public interface IGasStorage
{
	/**
	 * Get the gas of a declared type.
	 * @param type - type of gas
	 * @param data - ItemStack parameter if necessary
	 * @return gas stored
	 */
	public int getGas(EnumGas type, Object... data);
	
	/**
	 * Set the gas of a declared type to a new amount;
	 * @param type - type of gas
	 * @param data - ItemStack parameter if necessary
	 * @param amount - amount to store
	 */
	public void setGas(EnumGas type, int amount, Object... data);
	
	/**
	 * Gets the maximum amount of gas this tile entity can store.
	 * @param type - type of gas
	 * @param data - ItemStack parameter if necessary
	 * @return maximum gas
	 */
	public int getMaxGas(EnumGas type, Object... data);
}
