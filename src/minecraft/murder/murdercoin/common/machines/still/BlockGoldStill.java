package murder.murdercoin.common.machines.still;

import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraftforge.liquids.ILiquid;
import murder.murdercoin.common.MurderCoins;

public class BlockGoldStill extends BlockFluid implements ILiquid
{
    public BlockGoldStill(int par1)
    {
        super(par1, Material.lava);
        this.setCreativeTab(MurderCoins.murderTab);
    }
  // @Override
  // public void registerIcons(IconRegister par1IconRegister)
  // {
  //	 this.blockIcon = par1IconRegister.registerIcon("MurderCoins:goldFluid");
  // }
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
        return this.blockID;
    }
}
