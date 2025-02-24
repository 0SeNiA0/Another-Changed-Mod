package net.zaharenko424.a_changed.datagen;

import com.google.common.hash.Hashing;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class HeartConverter implements DataProvider {

    protected final File templateDirectory;
    protected final PackOutput out;
    protected final String modId;
    protected final File assetsDirectory;
    protected Triple<BufferedImage, BufferedImage, String>[] templates;
    protected final float[] hsb = new float[3];
    protected final float[] latexHSB = new float[3];
    protected final float[] secondaryLatexHSB = new float[3];

    public HeartConverter(File templateDirectory, PackOutput output, String modId){
        assert templateDirectory.isDirectory();
        this.templateDirectory = templateDirectory;
        this.out = output;
        this.modId = modId;
        this.assetsDirectory = new File(output.getOutputFolder().toFile(), "assets\\" + modId + "\\textures\\gui\\heart");
    }

    protected void convert(CachedOutput output){
        TransfurRegistry.TRANSFUR_TYPES.getEntries().forEach(transfurType ->
                convertHeartsForTFType(transfurType, output));
    }

    protected void convertHeartsForTFType(DeferredHolder<TransfurType, ? extends TransfurType> transfurType, CachedOutput output){
        if(!transfurType.getId().getNamespace().equals(modId)) throw new RuntimeException("Trying to generate textures for other mod");
        collectTemplates();
        File subDir = new File(assetsDirectory, transfurType.getId().getPath());

        int color = transfurType.get().getPrimaryColor();
        Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), latexHSB);
        color = transfurType.get().getSecondaryColor();
        Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), secondaryLatexHSB);
        convertHeartTextures(templates, (bytes, name) -> {
            File file = new File(subDir, name);
            try {
                output.writeIfNeeded(file.toPath(), bytes, Hashing.sha1().hashBytes(bytes));
            } catch (IOException e) {
                throw new RuntimeException("Failed to write image " + file, e);
            }
        });
    }

    protected void collectTemplates(){
        if(templates != null) return;

        File templateDirectory = new File(this.templateDirectory, "heart");
        File[] files = templateDirectory.listFiles();

        if(files == null || files.length == 0) {
            templates = new Triple[0];
            return;
        }

        File masks = new File(this.templateDirectory, "heart_mask");

        Triple<BufferedImage, BufferedImage, String>[] arr = new Triple[files.length];
        File file;
        for(int i = 0; i < files.length; i++){
            file = files[i];
            arr[i] = Triple.of(readImage(file), readImage(new File(masks, file.getName())), file.getName());
        }
        templates = arr;
    }

    protected BufferedImage readImage(File file){
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Image " + file, e);
        }
    }

    protected void convertHeartTextures(Triple<BufferedImage, BufferedImage, String>[] filesIn, BiConsumer<byte[], String> out){
        BufferedImage newImage, template, mask;
        int readColor;
        for(Triple<BufferedImage, BufferedImage, String> img : filesIn){
            template = img.getLeft();
            newImage = new BufferedImage(template.getWidth(), template.getHeight(), template.getType());
            mask = img.getMiddle();

            for(int x = 0; x < newImage.getWidth(); x++){
                for(int y = 0; y < newImage.getHeight(); y++){
                    readColor = template.getRGB(x, y);

                    if(FastColor.ARGB32.alpha(mask.getRGB(x, y)) != 0){
                        int color = mask.getRGB(x, y);
                        Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), hsb);
                        newImage.setRGB(x, y, Color.HSBtoRGB(secondaryLatexHSB[0], secondaryLatexHSB[1] * hsb[1], secondaryLatexHSB[2] * hsb[2]));
                        continue;
                    }

                    if(FastColor.ARGB32.alpha(readColor) == 0) {
                        newImage.setRGB(x, y, readColor);
                        continue;
                    }

                    Color.RGBtoHSB(FastColor.ARGB32.red(readColor), FastColor.ARGB32.green(readColor), FastColor.ARGB32.blue(readColor), hsb);
                    newImage.setRGB(x, y, Color.HSBtoRGB(latexHSB[0], latexHSB[1] * hsb[1], latexHSB[2] * hsb[2]));
                }
            }

            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(newImage, "png", stream);
                out.accept(stream.toByteArray(), img.getRight());
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return CompletableFuture.runAsync(() -> {
            convert(output);
            templates = null;
        });
    }

    @Override
    public @NotNull String getName() {
        return "Another Changed Mod Heart Sprites";
    }
}
