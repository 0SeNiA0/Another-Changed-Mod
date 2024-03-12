package net.zaharenko424.a_changed.client.cmrs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.DummyModel;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.client.renderer.SyringeProjectileRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.CryoChamberRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.DNAExtractorRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LaserEmitterRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LatexEncoderRenderer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ParametersAreNonnullByDefault
public final class ModelPartCache implements ResourceManagerReloadListener {

    public static final ModelPartCache INSTANCE = new ModelPartCache();
    private ImmutableMap<ModelLayerLocation, ModelDefinition> modelCache = ImmutableMap.of();

    private ModelPartCache(){
        onResourceManagerReload(null);
    }

    public ModelPart bake(ModelLayerLocation location){
        ModelDefinition model = modelCache.get(location);
        if(model == null) throw new NoSuchElementException("No model found for key " + location);
        return model.bake();
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_10758_) {
        HashMap<ModelLayerLocation, ModelDefinition> map = new HashMap<>();
        map.put(CryoChamberRenderer.LAYER, CryoChamberRenderer.bodyLayer());
        map.put(DNAExtractorRenderer.LAYER, DNAExtractorRenderer.bodyLayer());
        map.put(LaserEmitterRenderer.LAYER, LaserEmitterRenderer.bodyLayer());
        map.put(LatexEncoderRenderer.LAYER, LatexEncoderRenderer.bodyLayer());

        map.put(SyringeProjectileRenderer.LAYER, SyringeProjectileRenderer.bodyLayer());

        map.put(DummyModel.bodyLayer, DummyModel.bodyLayer());

        map.put(BeiFengModel.bodyLayer, BeiFengModel.bodyLayer());
        map.put(LatexWolfFemaleModel.bodyLayer, LatexWolfFemaleModel.bodyLayer());
        map.put(LatexWolfMaleModel.bodyLayer, LatexWolfMaleModel.bodyLayer());

        modelCache = ImmutableMap.copyOf(map);
    }
}