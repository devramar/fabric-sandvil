package dev.ramar.sandvil.mixins;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.AbstractBlock.Settings;

import dev.ramar.sandvil.SandvilMod;
import net.minecraft.block.AnvilBlock;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.Block;


import java.util.Map;
import java.util.HashMap;


@Mixin(AnvilBlock.class)
public class SandvilMixin
{
	private static class BlockIDs
	{
		public static Identifier ANVIL = new Identifier("minecraft", "anvil");
	}
	private static final Map<String, Identifier> crushConversion = new HashMap<>();
	static
	{
		crushConversion.put("block.minecraft.stone", new Identifier("minecraft", "cobblestone"));
		crushConversion.put("block.minecraft.cobblestone", new Identifier("minecraft", "gravel"));
		crushConversion.put("block.minecraft.gravel", new Identifier("minecraft", "sand"));
	}

	private static double CRUSH_DISTANCE = 3.0;


	public boolean isDamagedAnvil(BlockState bs)
	{
		return bs.getBlock().getTranslationKey().equals("block.minecraft.chipped_anvil");
	}

	/*
	method
	public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity)
	
	expanded:	
	onLanding(
	Lnet/minecraft/world/World;
	Lnet/minecraft/util/math/BlockPos;
	Lnet/minecraft/block/BlockState;
	Lnet/minecraft/block/BlockState;
	Lnet/minecraft/entity/FallingBlockEntity;)V
	*/

	@Inject(at = @At("HEAD"), method = "onLanding(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/FallingBlockEntity;)V")
	public void onLanding_swapBlock(World w, BlockPos p, BlockState fbState, BlockState pState, FallingBlockEntity fbe, CallbackInfo cbi)
	{
		double startY = 0.0;

		double endY = 0.0;
		try
		{
			if( this.crushBlock(w, p, fbState, pState, fbe, cbi) )
			{
				SandvilMod.LOGGER.info("crushBlock!");
				// we need to make sure that if we did crush the block, that the anvil didn't get damaged. i don't know how to do that normally soooo
				BlockState bs = w.getBlockState(p);
				if( this.isDamagedAnvil(bs) )
				{
					SandvilMod.LOGGER.info("replacing crushed anvil! ");
					BlockState freshAnvil = Registry.BLOCK.get(new Identifier("minecraft", "anvil")).getDefaultState();
					SandvilMod.LOGGER.info("default anvil: " + freshAnvil);
					w.setBlockState(p, freshAnvil);
					// SandvilMod.LOGGER.info("fresh anvil: " + freshAnvil);

				}
			}
		}
		// yeah... this is epic
		catch(Exception e) {}
	}


	public BlockState convertBlockState(BlockState bs)
	{	
		BlockState out = null;

		Identifier newID = SandvilMixin.crushConversion.get(bs.getBlock().getTranslationKey());
		if( newID != null )
		{
			Block block = Registry.BLOCK.get(newID);
			if( block != null )
				out = block.getDefaultState();
		}
		return out;
	}



	private boolean crushBlock(World w, BlockPos p, BlockState fbState, BlockState pState, FallingBlockEntity fbe, CallbackInfo cbi)
	{
		boolean success = false;
		SandvilMod.LOGGER.info("crushBlock(" + p + ")");


		BlockState toCrush = w.getBlockState(p.add(0, -1, 0));
		// Registry.register()

		BlockState convertedBlock = this.convertBlockState(toCrush);
		if( convertedBlock != null )
		{
			w.setBlockState(p.add(0, -1, 0), convertedBlock);
			success = true;
		}

		return success;
	}
}
