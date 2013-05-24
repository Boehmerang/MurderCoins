package murdercoins.block;

import murdercoins.common.MurderCoins;
import net.minecraft.block.BlockFlowing;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockGoldFlowing extends BlockFlowing implements ILiquid
{
    public blockGoldFlowing(int par1, Material par2Material)
    {
        super(par1, par2Material);
        this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
        this.setHardness(100F);
        this.setLightOpacity(3);
        //this.setUnlocalizedName("GoldFlowing");
        //this.setTickRandomly(true);
    }
   /* @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.theIcon = new Icon[] {par1IconRegister.registerIcon("MurderCoins:goldFluid"),par1IconRegister.registerIcon("MurderCoins:goldFluid")};
    	//this.blockIcon = par1IconRegister.registerIcon("MurderCoins:goldFluid");
    }*/


    @Override
    public int stillLiquidId()
    {
        return this.blockID+1;
    }

    @Override
    public boolean isMetaSensitive()
    {
        return false;
    }

    @Override
    public int stillLiquidMeta()
    {
        return 0; //this.blockID;
    }

	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k)
	{
		return true;
	}
	
    @Override
    public int getRenderType()
    {
    	return 4;
    }
}
