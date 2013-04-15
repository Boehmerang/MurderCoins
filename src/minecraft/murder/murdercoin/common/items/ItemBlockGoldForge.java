package murder.murdercoin.common.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;

public class ItemBlockGoldForge extends ItemBlock implements universalelectricity.core.item.IItemElectric
{
	public ItemBlockGoldForge(int par1) {
		super(par1);
		// TODO Auto-generated constructor stub
	}
	/*public Block metaBlock;
	public int forgeid;

	public ItemBlockGoldForge(int id, Block block)
	{
		super(id);
		this.forgeid = id+256;
		this.setIconIndex(Block.blocksList[id+256].getBlockTextureFromSide(2));
		setHasSubtypes(false);
		setMaxStackSize(1);
		setNoRepair();
	}

	public String getUnlocalizedName(ItemStack itemstack)
	{
		String name = "";
		switch(itemstack.getItemDamage())
		{
			case 0:
				name = "GoldForge";
				break;
			default:
				name = "Unknown";
				break;
		}
		return getUnlocalizedName(itemstack) + "." + name;
	}
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, Boolean flag)
	{
		double energy = getJoules();

		list.add("Stored Energy: " + ElectricInfo.getDisplayShort(energy, ElectricUnit.JOULES));
	}

	@Override
	public double getJoules(Object... data)
	{
	    if ((data[0] instanceof ItemStack))
	    {
	      ItemStack itemStack = (ItemStack)data[0];

	      if (itemStack.stackTagCompound == null)
	      {
	        return 0.0D;
	      }

	      double electricityStored = 0.0D;

	      if ((itemStack.stackTagCompound.getTag("electricity") instanceof NBTTagFloat))
	      {
	        electricityStored = itemStack.stackTagCompound.getFloat("electricity");
	      }
	      else
	      {
	        electricityStored = itemStack.stackTagCompound.getDouble("electricity");
	      }

	      return electricityStored;
	    }
	    return -1.0D;
	}
	@Override
	public void setJoules(double joules, Object... data) {
	    if ((data[0] instanceof ItemStack))
	    {
	      ItemStack itemStack = (ItemStack)data[0];
	  	if (itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		double electricityStored = Math.max(Math.min(joules, getMaxJoules(itemStack)), 0);
		itemStack.stackTagCompound.setDouble("electricity", electricityStored);
	    }
	}

	@Override
	public double getMaxJoules(Object... data) {
		if ((data[0] instanceof ItemStack))
	    {
	      ItemStack itemStack = (ItemStack)data[0];

	      return TileGoldForge.maxJoules;
	    }
		return 150000.0D;
	}

	@Override
	public double getVoltage(Object... data) {
		return 120.0D;
	}

	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
	    double rejectedElectricity = Math.max(getJoules(new Object[] { itemStack }) + ElectricInfo.getJoules(amps, voltage, 1.0D) - getMaxJoules(new Object[] { itemStack }), 0.0D);
	    setJoules(getJoules(new Object[] { itemStack }) + ElectricInfo.getJoules(amps, voltage, 1.0D) - rejectedElectricity, new Object[] { itemStack });
	    return rejectedElectricity;
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
	    double electricityToUse = Math.min(getJoules(new Object[] { itemStack }), joulesNeeded);
	    setJoules(getJoules(new Object[] { itemStack }) - electricityToUse, new Object[] { itemStack });
	    return electricityToUse;
	}

	public boolean canUse(ItemStack itemStack, int amount)
	{
		return getJoules(new Object[] {itemStack}) >= amount;
	}

	public int getChargedItemId()
	{
		return itemID;
	}

	public int getEmptyItemId()
	{
		return itemID;
	}

	@Override
	public boolean canReceiveElectricity() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canProduceElectricity() {
		// TODO Auto-generated method stub
		return false;
	}*/

	@Override
	public double getJoules(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJoules(double joules, ItemStack itemStack) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMaxJoules(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getVoltage(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ElectricityPack onReceive(ElectricityPack electricityPack,
			ItemStack itemStack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricityPack onProvide(ElectricityPack electricityPack,
			ItemStack itemStack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricityPack getReceiveRequest(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricityPack getProvideRequest(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return null;
	}
}
