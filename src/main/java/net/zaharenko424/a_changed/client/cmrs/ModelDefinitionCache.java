package net.zaharenko424.a_changed.client.cmrs;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.DummyModel;
import net.zaharenko424.a_changed.client.model.*;
import net.zaharenko424.a_changed.client.renderer.SyringeProjectileRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.*;
import net.zaharenko424.a_changed.client.renderer.misc.ChairRenderer;
import net.zaharenko424.a_changed.event.custom.RegisterModelDefinitionsEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ParametersAreNonnullByDefault
public final class ModelDefinitionCache {

    public static final ModelDefinitionCache INSTANCE = new ModelDefinitionCache();
    private final HashMap<ModelLayerLocation, ModelDefinition> modelCache = new HashMap<>();

    private ModelDefinitionCache(){
        modelCache.put(CannedOrangesRenderer.LAYER, CannedOrangesRenderer.bodyLayer());
        modelCache.put(CryoChamberRenderer.LAYER, CryoChamberRenderer.bodyLayer());
        modelCache.put(DNAExtractorRenderer.LAYER, DNAExtractorRenderer.bodyLayer());
        modelCache.put(LaserEmitterRenderer.LAYER, LaserEmitterRenderer.bodyLayer());
        modelCache.put(LatexEncoderRenderer.LAYER, LatexEncoderRenderer.bodyLayer());
        modelCache.put(PileOfOrangesRenderer.LAYER, PileOfOrangesRenderer.bodyLayer());

        modelCache.put(ChairRenderer.LAYER, ChairRenderer.bodyLayer());

        modelCache.put(SyringeProjectileRenderer.LAYER, SyringeProjectileRenderer.bodyLayer());

        modelCache.put(DummyModel.bodyLayer, DummyModel.bodyLayer());


        modelCache.put(MilkPuddingModel.bodyLayer, MilkPuddingModel.bodyLayer());
        modelCache.put(RoombaModel.bodyLayer, RoombaModel.bodyLayer());

        modelCache.put(BeiFengModel.bodyLayer, BeiFengModel.bodyLayer());

        modelCache.put(HypnoCatModel.bodyLayer, HypnoCatModel.bodyLayer());

        modelCache.put(SnowLeopardFemaleModel.bodyLayer, SnowLeopardFemaleModel.bodyLayer());
        modelCache.put(SnowLeopardMaleModel.bodyLayer, SnowLeopardMaleModel.bodyLayer());

        modelCache.put(LatexSharkFemaleModel.bodyLayer, LatexSharkFemaleModel.bodyLayer());
        modelCache.put(LatexSharkMaleModel.bodyLayer, LatexSharkMaleModel.bodyLayer());

        modelCache.put(LatexWolfFemaleModel.bodyLayer, LatexWolfFemaleModel.bodyLayer());
        modelCache.put(LatexWolfMaleModel.bodyLayer, LatexWolfMaleModel.bodyLayer());

        modelCache.put(YufengDragonModel.bodyLayer, YufengDragonModel.bodyLayer());

        ModLoader.get().postEvent(new RegisterModelDefinitionsEvent(modelCache));
    }

    public ModelPart bake(ModelLayerLocation location){
        ModelDefinition model = modelCache.get(location);
        if(model == null) throw new NoSuchElementException("No model found for key " + location);
        return model.bake();
    }
}