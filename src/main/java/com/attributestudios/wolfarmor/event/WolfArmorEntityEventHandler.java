package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.capabilities.IWolfArmor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Contains all forge subscribed events for entities
 */
public class WolfArmorEntityEventHandler {
    //region Public / Protected Methods
    
    //attach the armor capability to the wolf entity
    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
    	if (event.getObject() instanceof EntityWolf) 
    		//Now, attach the armor ability here
    		event.addCapability(new ResourceLocation("Wolf_armor"), new WolfArmorAttachment((EntityWolf)event.getObject()));
    		
    }
    
    private class WolfArmorAttachment implements ICapabilitySerializable<NBTTagCompound> {
    	
		private final CapabilityWolfArmor wolfArmor;
		
		public WolfArmorAttachment(EntityWolf wolf) {
			this.wolfArmor = new CapabilityWolfArmor(wolf);
		}
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityWolfArmor.WOLFARMMOR;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityWolfArmor.WOLFARMMOR)
				return (T)wolfArmor;
			
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return wolfArmor.writeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			wolfArmor.readNBT(nbt);		
		}
    }
    
    @SubscribeEvent
    public void WolfHurtEvent(LivingHurtEvent event) {
    	if (event.getEntity() instanceof EntityWolf) {
    		IWolfArmor wolfArmmor = event.getEntity().getCapability(CapabilityWolfArmor.WOLFARMMOR, null);
    		if (wolfArmmor != null) 
    			wolfArmmor.damageArmor(event.getAmount());
    	}
    }
    
    @SubscribeEvent
    public void WolfDropEvent(LivingDropsEvent event) {
    	if (event.getEntity() instanceof EntityWolf) {
    		IWolfArmor wolfArmmor = event.getEntity().getCapability(CapabilityWolfArmor.WOLFARMMOR, null);
    		if (wolfArmmor != null) {
    			DamageSource Source = event.getSource();
    			wolfArmmor.dropEquipment(Source != null && Source.getEntity() instanceof EntityPlayer, event.getLootingLevel());
    		}
    	}
    }
    
    @SubscribeEvent
    public void PlayerIntractEvent(PlayerInteractEvent.EntityInteract event) {
   
    	if (event.getTarget() instanceof EntityWolf) {
    		IWolfArmor wolfArmmor = event.getTarget().getCapability(CapabilityWolfArmor.WOLFARMMOR, null);
    		if (wolfArmmor != null) {
    			EntityPlayer player = event.getEntityPlayer();
    			EnumHand hand = EnumHand.MAIN_HAND;
    			ItemStack stack = player.getHeldItemMainhand();
    			if (stack == null && (stack = player.getHeldItemOffhand()) != null) 
    				hand = EnumHand.OFF_HAND;
    			//use for open gui
    			if (wolfArmmor.processInteract(player, hand, stack))
    				event.setResult(Result.DENY);
    		}
    	}
    }
    //endregion Public / Protected Methods
}
