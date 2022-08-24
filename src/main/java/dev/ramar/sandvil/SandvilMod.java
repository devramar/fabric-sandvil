package dev.ramar.sandvil;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandvilMod implements ModInitializer, DedicatedServerModInitializer
{

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	// public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final Logger LOGGER = LoggerFactory.getLogger("sandvil");

	@Override
	public void onInitialize() 
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		SandvilMod.LOGGER.info("Sandvil initialisation!");
	}

	@Override
	public void onInitializeServer()
	{
		SandvilMod.LOGGER.info("Sandvil initialisation... but server!");
	}
}
