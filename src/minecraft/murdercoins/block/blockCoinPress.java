package murdercoins.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityPulverisor;
import murdercoins.client.Render.BlockRenderingHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class blockCoinPress extends BlockAdvanced
{
	private Icon cPTop;
	private Icon cPSide;
	private Icon cPOn;
	private Icon cPPow;
	private Icon cPOff;

	public blockCoinPress(int id)
	{
		super(id, Material.iron);
		this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
		this.setStepSound(soundMetalFootstep);
	}

	public void registerIcons(IconRegister par1IconRegister)
	{
		this.cPTop = par1IconRegister.registerIcon("MurderCoins:coinPress-Top");
		this.cPSide = par1IconRegister.registerIcon("MurderCoins:coinPress-Side");
		this.cPOn = par1IconRegister.registerIcon("MurderCoins:coinPress-On");
		this.cPOff = par1IconRegister.registerIcon("MurderCoins:coinPress-Off");
		this.cPPow = par1IconRegister.registerIcon("MurderCoins:coinPress-Pow");
	}
	
	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y, z);
			return true;
		}
		return true;
	}
	
	/**
	 * Called to see if a block can be placed in the world.
	 */
	@Override
	public boolean canPlaceBlockAt(World par1World, int x, int y, int z)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		EntityLiving par5EntityLiving = par1World.getClosestPlayer(x, y, z, 10);
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
							
		if(angle == 0)
		{
			if (par1World.getBlockId(x, y, z-1) == 0 && par1World.getBlockId(x-1, y, z-1) == 0 && par1World.getBlockId(x-1, y, z) == 0)
			{
				return true;
			}
		}
		if(angle == 1)
		{
			if (par1World.getBlockId(x, y, z-1) == 0 && par1World.getBlockId(x+1, y, z-1) == 0 && par1World.getBlockId(x+1, y, z) == 0)
			{
				return true;
			}
		}
		if(angle == 2)
		{
			if (par1World.getBlockId(x, y, z+1) == 0 && par1World.getBlockId(x+1, y, z+1) == 0 && par1World.getBlockId(x+1, y, z) == 0)
			{
				return true;
			}
		}
		if(angle == 3)
		{
			if (par1World.getBlockId(x, y, z+1) == 0 && par1World.getBlockId(x-1, y, z+1) == 0 && par1World.getBlockId(x-1, y, z) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
	{
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch (angle)
		{
			case 0:
				par1World.setBlock(x, y, z, this.blockID, 0, 1);
				par1World.setBlock(x, y, z-1, MurderCoins.cpBoundingBlock.blockID, 2, 1);
				par1World.setBlock(x-1, y, z-1, MurderCoins.cpBoundingBlock.blockID, 3, 1);
				par1World.setBlock(x-1, y, z, MurderCoins.cpBoundingBlock.blockID, 4, 1);
				break;
			case 1:
				par1World.setBlock(x, y, z, this.blockID, 3, 1);
				par1World.setBlock(x, y, z-1, MurderCoins.cpBoundingBlock.blockID, 5, 1);
				par1World.setBlock(x+1, y, z-1, MurderCoins.cpBoundingBlock.blockID, 6, 1);
				par1World.setBlock(x+1, y, z, MurderCoins.cpBoundingBlock.blockID, 7, 1);
				break;
			case 2:
				par1World.setBlock(x, y, z, this.blockID, 1, 1);
				par1World.setBlock(x, y, z+1, MurderCoins.cpBoundingBlock.blockID, 8, 1);
				par1World.setBlock(x+1, y, z+1, MurderCoins.cpBoundingBlock.blockID, 9, 1);
				par1World.setBlock(x+1, y, z, MurderCoins.cpBoundingBlock.blockID, 10, 1);
				break;
			case 3:
				par1World.setBlock(x, y, z, this.blockID, 2, 1);
				par1World.setBlock(x, y, z+1, MurderCoins.cpBoundingBlock.blockID, 11, 1);
				par1World.setBlock(x-1, y, z+1, MurderCoins.cpBoundingBlock.blockID, 12, 1);
				par1World.setBlock(x-1, y, z, MurderCoins.cpBoundingBlock.blockID, 13, 1);
				break;
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int i, int j)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		breakConnected(world, x, y, z, metadata);		
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, i, j);
	}

	public void breakConnected(World world, int x, int y, int z, int meta)
	{
		if (meta == 0)
    	{
			int tempblock = world.getBlockId(x, y, z-1);
			if(tempblock == MurderCoins.cpBoundingBlock.blockID)
			{
            world.setBlock(x, y, z-1, 0);
            world.setBlock(x-1, y, z-1, 0);
            world.setBlock(x-1, y, z, 0);
			}
       	}
     	if (meta == 1)
    	{
     		int tempblock = world.getBlockId(x, y, z+1);
			if(tempblock == MurderCoins.cpBoundingBlock.blockID)
			{
            world.setBlock(x, y, z+1, 0);
			}  
    	}
       	if (meta == 2)
    	{
       		int tempblock = world.getBlockId(x-1, y, z+1);
			if(tempblock == MurderCoins.cpBoundingBlock.blockID)
			{
            world.setBlock(x-1, y, z+1, 0);
			}
        }
       	if (meta == 3)
    	{
       		int tempblock = world.getBlockId(x+1, y, z-1);
			if(tempblock == MurderCoins.cpBoundingBlock.blockID)
			{
            world.setBlock(x+1, y, z-1, 0);
			}
    	}
	}
	
	private void dropItems(World world, int x, int y, int z)
	{
		Random rand = new Random();

		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

		if (!(tile_entity instanceof IInventory))
		{
			return;
		}

		IInventory inventory = (IInventory) tile_entity;

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0)
			{
				float rx = rand.nextFloat() * 0.6F + 0.1F;
				float ry = rand.nextFloat() * 0.6F + 0.1F;
				float rz = rand.nextFloat() * 0.6F + 0.1F;

				EntityItem entity_item = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
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
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new tileEntityCoinPress();
	}

	/**
	* The type of render function that is called for this block
	*/
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
	return BlockRenderingHandler.ID;
	}

	/**
	* Is this block (a) opaque and (B) a full 1m cube? This determines whether or not to render the shared face of two
	* adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	*/
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
