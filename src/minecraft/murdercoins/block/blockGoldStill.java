package murdercoins.block;

import murdercoins.common.MurderCoins;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import net.minecraftforge.liquids.LiquidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockGoldStill extends BlockFluid implements ILiquid
{

	
    public blockGoldStill(int par1, Material par2Material)
    {
        super(par1, par2Material);
        this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
        this.setHardness(100F);
        this.setLightOpacity(3);
        
        //this.setUnlocalizedName("GoldStill");
		//this.disableStats();
        
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.theIcon = new Icon[] {par1IconRegister.registerIcon("MurderCoins:gold"),par1IconRegister.registerIcon("MurderCoins:gold_flow")};
    	//this.blockIcon = par1IconRegister.registerIcon("MurderCoins:goldFluid");
    }
    
    @Override
    public int getRenderType()
    {
    	return 4;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    /*@Override
    public Icon getIcon(int par1, int par2)//getBlockTextureFromSideAndMetadata(int side, int metadata) getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
       return par1 != 0 && par1 != 1 ? this.blockIcon : this.blockIcon;
    }*/

    @Override
    public int stillLiquidId()
    {
        return MurderCoins.GoldStill.blockID;
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
