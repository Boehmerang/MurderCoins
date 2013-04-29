package murdercoins.block;

import java.util.Random;

import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityGoldForgeTop;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;

public class blockCPBoundingBlock extends BlockAdvanced
{
	private Icon fillerTexture;

	public blockCPBoundingBlock(int id, Material material) 
	{
		super(id, material.iron);
		this.setBlockBounds(0F, -1F, 0F, 1F, 1F, 1F);
	}
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.fillerTexture = par1IconRegister.registerIcon("MurderCoins:filler");
	}
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		return this.fillerTexture;
	}
	
	@Override
	public Icon getIcon(int side, int metadata)//getBlockTextureFromSideAndMetadata(int side, int metadata) 
	{
		return this.fillerTexture;
	}
	

	
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		//TileEntity tileEntity = VectorHelper.getTileEntityFromSide(par1World, new Vector3(x, y, z), ForgeDirection.DOWN);
		int block = par1World.getBlockId(x, y-1, z);
		
		if (block == MurderCoins.coinPress.blockID)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y-1, z);
			return true;
		}
		return true;
	
	}
   	@Override
   	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
   	{
   		int metadata = par1World.getBlockMetadata(x, y, z);

   		int change = 0;

   		// Re-orient the block
   		switch (metadata)
   		{
   			case 0:
   				change = 3;
   				break;
   			case 3:
   				change = 1;
   				break;
   			case 1:
   				change = 2;
   				break;
   			case 2:
   				change = 0;
   				break;
   		}

   		par1World.setBlockMetadataWithNotify(x, y, z, change, 0);
   		par1World.setBlockMetadataWithNotify(x, y-1, z, change, 0);
   		par1World.markBlockForRenderUpdate(x, y, z);

   		//((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

   		return true;
   	}
   	
    @Override
    public void breakBlock(World world, int x, int y, int z, int i, int j)
    {
            dropItems(world, x, y, z);
            world.setBlock(x, y-1, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    }
    private void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();

        TileEntity tile_entity = world.getBlockTileEntity(x, y-1, z);

        if(!(tile_entity instanceof IInventory)){
                return;
        }

        IInventory inventory = (IInventory) tile_entity;

        for(int i = 0; i < inventory.getSizeInventory(); i++){
                ItemStack item = inventory.getStackInSlot(i);

                if(item != null && item.stackSize > 0){
                float rx = rand.nextFloat() * 0.6F + 0.1F;
                float ry = rand.nextFloat() * 0.6F + 0.1F;
                float rz = rand.nextFloat() * 0.6F + 0.1F;

                EntityItem entity_item = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                if(item.hasTagCompound()){
                        entity_item.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.5F;

                entity_item.motionX = rand.nextGaussian() * factor;
                entity_item.motionY = rand.nextGaussian() * factor + 0.2F;
                entity_item.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entity_item);
                item.stackSize = 0;
                }
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new tileEntityGoldForgeTop();
	}
}
