package net.zaharenko424.a_changed.event.custom;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

/**
 * This event is called after initializing CustomModelManager. It is only called once! <p> Registering phase is assumed to be done, so DeferredHolders can be used.
 */
public class LoadModelsToCacheEvent extends Event implements IModBusEvent {}