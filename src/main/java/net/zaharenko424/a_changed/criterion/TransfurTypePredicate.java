package net.zaharenko424.a_changed.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public record TransfurTypePredicate(Optional<TransfurType> transfurType, Optional<Type> type) {

    public static final Codec<TransfurTypePredicate> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                    Codec.optionalField("transfur_type", ResourceLocation.CODEC.xmap(TransfurManager::getTransfurType, tfType -> tfType.id), false).forGetter(TransfurTypePredicate::transfurType),
                    Codec.optionalField("type", Codec.stringResolver(Type::toString, Type::valueOf), false).forGetter(TransfurTypePredicate::type)
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
        CAT(type -> type.abilities.contains(AbilityRegistry.CAT_PASSIVE.get())),
        SWIMMING(type -> type.abilities.contains(AbilityRegistry.FISH_PASSIVE.get())),
        FLYING(type -> type.abilities.contains(AbilityRegistry.FALL_FLYING_PASSIVE.get()));

        private final Predicate<TransfurType> check;

        Type(Predicate<TransfurType> check){
            this.check = check;
        }

        public boolean test(TransfurType transfurType){
            return check.test(transfurType);
        }
    }
}