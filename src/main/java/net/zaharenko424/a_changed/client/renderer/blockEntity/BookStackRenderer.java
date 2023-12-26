package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.BookStackEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class BookStackRenderer implements BlockEntityRenderer<BookStackEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.BOOK_STACK_ENTITY.getId(), "book");
    private static final ResourceLocation BOOK_0 = AChanged.textureLoc("block/book_stack/book_0");
    private static final ResourceLocation BOOK_1 = AChanged.textureLoc("block/book_stack/book_1");
    private static final ResourceLocation BOOK_2 = AChanged.textureLoc("block/book_stack/book_2");
    private static final ResourceLocation BOOK_3 = AChanged.textureLoc("block/book_stack/book_3");
    private static final ImmutableMap<Integer,ResourceLocation> TEXTURE_BY_ID = ImmutableMap.of(0,BOOK_0,1,BOOK_1,2,BOOK_2,3,BOOK_3);
    private final ModelPart book;

    public BookStackRenderer(BlockEntityRendererProvider.Context context){
        book=context.bakeLayer(LAYER).getChild("book");
    }

    public static @NotNull LayerDefinition bodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("book", CubeListBuilder.create().texOffs(0, 19).addBox(-7.0F, 3.0F, -9.0F, 14.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, 0.0F, -9.0F, 14.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-7.0F, 1.0F, -8.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-7.0F, 1.0F, -9.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, 1.0F, 8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(BookStackEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        NonNullList<BookStackEntity.Book> list=p_112307_.getBooks();
        if(list.isEmpty()) return;
        p_112309_.translate(.5,0,.5);
        BookStackEntity.Book book;
        for(int i=0;i<list.size();i++){
            this.book.resetPose();
            this.book.xScale=.5f;
            this.book.yScale=.5f;
            this.book.zScale=.5f;
            book=list.get(i);
            this.book.yRot=book.rotation();
            this.book.y=i*2;
            this.book.render(p_112309_,p_112310_.getBuffer(RenderType.entitySolid(TEXTURE_BY_ID.get(book.modelId()))),p_112311_,p_112312_);
        }
    }
}