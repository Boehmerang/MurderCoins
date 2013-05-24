package murdercoins.block;

import java.util.Random;

import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityGoldForge;
import murdercoins.tileentity.tileEntityGoldForgeTop;
import net.minecraft.block.Block;
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
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class blockFakeBlock extends BlockAdvanced
{
	private Icon fillerTexture;
	public int TeX;
	public int TeY;
	public int TeZ;

	public blockFakeBlock(int id, Material material) 
	{
		super(id, material.iron);
		this.setBlockBounds(0F, -1F, 0F, 1F, 1F, 1F);
		// TODO Auto-generated constructor stub
		
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
		int metadata = par1World.getBlockMetadata(x, y, z);
	
		if( metadata == 1)
		{
			//TileEntity tileEntity = VectorHelper.getTileEntityFromSide(par1World, new Vector3(x, y, z), ForgeDirection.DOWN);
			int block = par1World.getBlockId(x, y-1, z);
			
			if (block == MurderCoins.goldForge.blockID)
			{
				par5EntityPlayer.openGui(MurderCoins.instance, 1, par1World, x, y-1, z);
				return true;
			}
		}
	
		else if (metadata == 2)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y, z+1);
			return true;
		}
		else if (metadata == 3)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x+1, y, z+1);
			return true;
		}
		else if (metadata == 4)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x+1, y, z);
			return true;
		}
		else if (metadata == 8)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y, z-1);
			return true;
		}
		else if (metadata == 9)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x-1, y, z-1);
			return true;
		}
		else if (metadata == 10)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x-1, y, z);
			return true;
		}
		else if (metadata == 11)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y, z-1);
			return true;
		}
		else if (metadata == 12)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x+1, y, z-1);
			return true;
		}
		else if (metadata == 13)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x+1, y, z);
			return true;
		}
		return true;
		
	}
   	@Override
   	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
   	{
   		int metadata1 = par1World.getBlockMetadata(x, y, z);
   		if (metadata1==1)
   		{
   			int metadata = par1World.getBlockMetadata(x, y-1, z);
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
   		
   			par1World.setBlockMetadataWithNotify(x, y, z, 1, 1);
   			par1World.setBlockMetadataWithNotify(x, y-1, z, change, 1);
   			par1World.markBlockForRenderUpdate(x, y, z);
   		}
   		//((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

   		return true;
   	}
   	
    @Override
    public void breakBlock(World world, int x, int y, int z, int i, int j)
    {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (metadata == 1)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y-1, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 2)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x-1, y, z+1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
     	if (metadata == 3)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x+1, y, z+1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 4)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x+1, y, z-1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 5)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x+1, y, z+1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
     	if (metadata == 6)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x-1, y, z+1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 7)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x-1, y, z-1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 8)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x+1, y, z-1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
     	if (metadata == 9)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x-1, y, z-1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 10)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x-1, y, z+1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 11)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x-1, y, z-1, 0);
            world.setBlock(x-1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
     	if (metadata == 12)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x+1, y, z-1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
       	if (metadata == 13)
    	{
            dropItems(world, x, y, z);
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y, z+1, 0);
            world.setBlock(x+1, y, z+1, 0);
            world.setBlock(x+1, y, z, 0);
            super.breakBlock(world, x, y, z, i, j);
    	}
    	
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
	public TileEntity createNewTileEntity(World par1World) 
	{
			return new tileEntityGoldForgeTop();
	}
	
	public boolean isOpaqueCube()
	{
	return false;
	}

	/**
	* If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	*/
	public boolean renderAsNormalBlock()
	{
	return false;
	}
}
