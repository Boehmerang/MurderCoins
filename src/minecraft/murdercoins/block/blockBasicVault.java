package murdercoins.block;

import java.util.Random;

import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityBasicVault;
import murdercoins.tileentity.tileEntityManPress;
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

public class blockBasicVault  extends BlockAdvanced
	{
		private String owner;
		private Icon bVaultTop;
		private Icon bVaultFront;

		public blockBasicVault(int id)
		{
			super(id, Material.iron);
			this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
			this.setStepSound(soundMetalFootstep);
		}

		public void registerIcons(IconRegister par1IconRegister)
		{
			this.bVaultTop = par1IconRegister.registerIcon("MurderCoins:vaultTop");
			this.bVaultFront = par1IconRegister.registerIcon("MurderCoins:vaultFront");
		}
		@Override
		public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
		{
			int metadata = world.getBlockMetadata(x, y, z);
			if (side == metadata + 2) // front
			{
				return this.bVaultFront;
			}
			else
			// sides
			{
				return this.bVaultTop;
			}
		}
		@Override
		public Icon getIcon(int side, int metadata)
		{
			if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) // front
			{
				return this.bVaultFront;
			}
			else					// sides
			{
				return this.bVaultTop;
			}
		}

		@Override
		public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
		{
			int metadata = par1World.getBlockMetadata(x, y, z);
			System.out.println(this.owner);
			if (!par1World.isRemote)
			{
				if (par5EntityPlayer.getEntityName() == this.owner)
				{
					par5EntityPlayer.openGui(MurderCoins.instance, 0, par1World, x, y, z);
					return true;
				}
			}
			return true;
		}

		/**
		 * Called when the block is placed in the world.
		 */
		/**
		 * Called when the block is placed in the world.
		 */
		@Override
		public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
		{
			this.owner = par5EntityLiving.getEntityName();
			System.out.println(this.owner);
			int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			switch (angle)
			{
				case 0:
					par1World.setBlock(x, y, z, this.blockID, 0, 0);
					break;
				case 1:
					par1World.setBlock(x, y, z, this.blockID, 3, 0);
					break;
				case 2:
					par1World.setBlock(x, y, z, this.blockID, 1, 0);
					break;
				case 3:
					par1World.setBlock(x, y, z, this.blockID, 2, 0);
					break;
			}

			((TileEntity) par1World.getBlockTileEntity(x, y, z)).updateEntity();
			par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		}

		@Override
		public void breakBlock(World world, int x, int y, int z, int i, int j)
		{
			dropItems(world, x, y, z);
			super.breakBlock(world, x, y, z, i, j);
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

			par1World.setBlock(x, y, z, this.blockID, change, 0);
			par1World.markBlockForRenderUpdate(x, y, z);

			((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

			return true;
		}

		@Override
		public TileEntity createNewTileEntity(World var1)
		{
			return new tileEntityBasicVault();
		}
	}

