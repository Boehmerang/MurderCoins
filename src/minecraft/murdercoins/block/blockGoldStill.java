package murdercoins.block;

import murdercoins.common.MurderCoins;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.liquids.ILiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockGoldStill extends BlockFluid implements ILiquid
{
    public blockGoldStill(int par1)
    {
        super(par1, Material.water);
        this.setCreativeTab(MurderCoins.murderTab);
    	setLightOpacity(3);
		//setUnlocalizedName("GoldStill");
    }
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.blockIcon = par1IconRegister.registerIcon("MurderCoins:goldFluid");
    }
    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)//getBlockTextureFromSideAndMetadata(int side, int metadata) getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.blockIcon : this.blockIcon;
    }

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
}
