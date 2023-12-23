package net.zaharenko424.a_changed.client.model.geom;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record UVData(float u1,float v1, float u2, float v2) {}