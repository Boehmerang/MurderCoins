package murder.MurderCoins;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;

public class BlockGoldStill extends BlockStationary
{
	protected BlockGoldStill(int ID)
	{
		super(ID, Material.lava);
		this.blockHardness = 100f;
		this.setLightOpacity(3);
		this.disableStats();
		
	}
}
