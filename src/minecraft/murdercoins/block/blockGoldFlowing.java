package murdercoins.block;

import murdercoins.common.MurderCoins;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockGoldFlowing extends BlockFluid implements ILiquid
{
    public blockGoldFlowing(int par1, Material par2Material)
    {
        super(par1, par2Material);
        setHardness(100F);
    	setLightOpacity(3);
		setUnlocalizedName("GoldFlowing");
    }
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.blockIcon = par1IconRegister.registerIcon("MurderCoins:goldFluid");
    }
    @SideOnly(Side.CLIENT)

    @Override
    public int stillLiquidId()
    {
        return this.blockID;
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

}
