package net.zaharenko424.a_changed.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractWaterLatex;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public record TransfurTypePredicate(Optional<TransfurType> transfurType, Optional<Type> type) {

    public static final Codec<TransfurTypePredicate> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                    ExtraCodecs.strictOptionalField(ResourceLocation.CODEC.xmap(TransfurManager::getTransfurType, tfType -> tfType.id), "transfur_type").forGetter(TransfurTypePredicate::transfurType),
                    ExtraCodecs.strictOptionalField(ExtraCodecs.stringResolverCodec(Type::toString, Type::valueOf), "type").forGetter(TransfurTypePredicate::type)
            ).apply(builder, TransfurTypePredicate::new));

    public boolean matches(TransfurType transfurType){
        if(type.isPresent() && !type.get().test(transfurType)) return false;

        return transfurType().isEmpty() || transfurType().get() == transfurType;
    }

    @Contract("_ -> new")
    public static @NotNull TransfurTypePredicate of(TransfurType transfurType){
        return new TransfurTypePredicate(Optional.of(transfurType), Optional.empty());
    }

    @Contract("_ -> new")
    public static @NotNull TransfurTypePredicate of(Type type){
        return new TransfurTypePredicate(Optional.empty(), Optional.of(type));
    }

    public enum Type {
        CAT(type -> type instanceof AbstractLatexCat),
        SWIMMING(type -> type instanceof AbstractWaterLatex),
        FLYING(type -> type.abilities.contains(AbilityRegistry.FALL_FLYING_ABILITY.get()));

        private final Predicate<TransfurType> check;

        Type(Predicate<TransfurType> check){
            this.check = check;
        }

        public boolean test(TransfurType transfurType){
            return check.test(transfurType);
        }
    }
}