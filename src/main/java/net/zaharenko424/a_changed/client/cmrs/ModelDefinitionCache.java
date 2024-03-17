package net.zaharenko424.a_changed.client.cmrs;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.DummyModel;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.client.renderer.SyringeProjectileRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.CryoChamberRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.DNAExtractorRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LaserEmitterRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LatexEncoderRenderer;
import net.zaharenko424.a_changed.event.RegisterModelDefinitionsEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ParametersAreNonnullByDefault
public final class ModelDefinitionCache {

    public static final ModelDefinitionCache INSTANCE = new ModelDefinitionCache();
    private final HashMap<ModelLayerLocation, ModelDefinition> modelCache = new HashMap<>();

    private ModelDefinitionCache(){
        modelCache.put(CryoChamberRenderer.LAYER, CryoChamberRenderer.bodyLayer());
        modelCache.put(DNAExtractorRenderer.LAYER, DNAExtractorRenderer.bodyLayer());
        modelCache.put(LaserEmitterRenderer.LAYER, LaserEmitterRenderer.bodyLayer());
        modelCache.put(LatexEncoderRenderer.LAYER, LatexEncoderRenderer.bodyLayer());

        modelCache.put(SyringeProjectileRenderer.LAYER, SyringeProjectileRenderer.bodyLayer());

        modelCache.put(DummyModel.bodyLayer, DummyModel.bodyLayer());

        modelCache.put(BeiFengModel.bodyLayer, BeiFengModel.bodyLayer());
        modelCache.put(HypnoCatModel.bodyLayer, HypnoCatModel.bodyLayer());
        modelCache.put(LatexWolfFemaleModel.bodyLayer, LatexWolfFemaleModel.bodyLayer());
        modelCache.put(LatexWolfMaleModel.bodyLayer, LatexWolfMaleModel.bodyLayer());

        modelCache.put(Protogen.bodyLayer, Protogen.bodyLayer());

        ModLoader.get().postEvent(new RegisterModelDefinitionsEvent(modelCache));
    }

    public ModelPart bake(ModelLayerLocation location){
        ModelDefinition model = modelCache.get(location);
        if(model == null) throw new NoSuchElementException("No model found for key " + location);
        return model.bake();
    }
}