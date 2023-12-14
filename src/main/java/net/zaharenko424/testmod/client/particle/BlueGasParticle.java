package net.zaharenko424.testmod.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class BlueGasParticle extends TextureSheetParticle {

    protected BlueGasParticle(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, xSpeed, ySpeed, zSpeed);

        quadSize=.1f;
        friction=1f;
        xd=xSpeed;
        yd=ySpeed;
        zd=zSpeed;
        lifetime=20;
        setSpriteFromAge(sprites);

        rCol=1f;
        gCol=1f;
        bCol=1f;
    }

    @Override
    public void tick() {
        super.tick();
        alpha=(1- (float) age/lifetime);
        quadSize+=.1f+ (float) age/50;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @ParametersAreNonnullByDefault
    public static class Provider implements ParticleProvider<SimpleParticleType>{
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites){
            this.sprites=sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType p_107421_, ClientLevel p_107422_, double p_107423_, double p_107424_, double p_107425_, double p_107426_, double p_107427_, double p_107428_) {
            return new BlueGasParticle(p_107422_,p_107423_,p_107424_,p_107425_,p_107426_,p_107427_,p_107428_,sprites);
        }
    }
}