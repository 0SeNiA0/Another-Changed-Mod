package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.cmrs.api.BufferSourceAccess;
import net.zaharenko424.cmrs.client.geom.*;
import net.zaharenko424.a_changed.entity.block.AbstractStackEntity;
import net.zaharenko424.a_changed.entity.block.BookStackEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BookStackRenderer implements BlockEntityRenderer<BookStackEntity> {

    private static final ResourceLocation BOOK_0 = AChanged.textureLoc("block/book_stack/book_0");
    private static final ResourceLocation BOOK_1 = AChanged.textureLoc("block/book_stack/book_1");
    private static final ResourceLocation BOOK_2 = AChanged.textureLoc("block/book_stack/book_2");
    private static final ResourceLocation BOOK_3 = AChanged.textureLoc("block/book_stack/book_3");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{BOOK_0, BOOK_1, BOOK_2, BOOK_3};
    private final ModelPart book;

    public BookStackRenderer(){
        book = bodyLayer().bake().getDirectChild("book");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("book", GroupBuilder.create()
                .addBox(-4f, 1.5f, -5.5f, 8, 0.5f, 11, new CubeUV().down(26, 18, 18, 30).north(14, 31, 0, 30).up(28, 18, 14, 0).west(18, 28, 0, 27).east(18, 27, 0, 26).south(14, 32, 0, 31))
                .addBox(-4f, 0f, -5.5f, 8, 0.5f, 11, new CubeUV().down(14, 0, 0, 18).north(28, 31, 14, 30).up(26, 30, 18, 18).west(18, 30, 0, 29).east(18, 29, 0, 28).south(28, 32, 14, 31))
                .addBox(-3.5f, 0.5f, -5f, 7.5f, 1, 10, new CubeUV().north(13, 24, 0, 22).west(16, 22, 0, 20).east(16, 20, 0, 18).south(13, 26, 0, 24))
                .addBox(3f, 0.5f, -5.5f, 1, 1, 0.5f, new CubeUV().north(17, 19, 16, 18).west(18, 21, 17, 20).east(18, 19, 17, 18))
                .addBox(3f, 0.5f, 5f, 1, 1, 0.5f, new CubeUV().west(18, 20, 17, 19).east(17, 21, 16, 20).south(17, 20, 16, 19)));

        return ModelDefinition.create(modelBuilder, 32, 32);
    }

    @Override
    public void render(BookStackEntity entity, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<AbstractStackEntity.Entry> list = entity.entries();
        if(list.isEmpty()) return;
        stack.translate(.5,0,.5);
        BufferSourceAccess access = BufferSourceAccess.get();
        AbstractStackEntity.Entry entry;
        int id;
        for(int i = 0; i < list.size(); i++){
            book.resetPose();
            book.y = i * 2;

            entry = list.get(i);
            book.yRot = entry.rotation();

            id = entry.modelId();
            if(id > TEXTURES.length - 1) id = TEXTURES.length - 1;
            book.render(stack, access.cmrs$getBuffer(RenderType.entitySolid(TEXTURES[id]), 0), packedLight, packedOverlay);
        }
        access.cmrs$finishBatched();
    }
}