// Date: 4/27/2013 8:27:25 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package murdercoins.client.Model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGoldForge extends ModelBase
{
  //fields
    ModelRenderer LeftsideMBbot;
    ModelRenderer RightsideMBbot;
    ModelRenderer BacksideMBbot;
    ModelRenderer TopcoverMBBot;
    ModelRenderer BottomMBbot;
    ModelRenderer MiddleBody;
    ModelRenderer Cornerpillar1;
    ModelRenderer Cornerpillar2;
    ModelRenderer Cornerpillar3;
    ModelRenderer Cornerpillar4;
    ModelRenderer GlassPlateTank;
    ModelRenderer GlassPlateLeftPillar;
    ModelRenderer GlassPlateRightPillar;
    ModelRenderer GlassPlateMiddlePillar;
    ModelRenderer LatchMiddle;
    ModelRenderer LatchRight;
    ModelRenderer LatchLeft;
    ModelRenderer TopBodyMain;
    ModelRenderer TopBodyFrontLeft;
    ModelRenderer TopBodyFrontRight;
    ModelRenderer TopBodyFrontPanel;
  
  public ModelGoldForge()
  {
    textureWidth = 256;
    textureHeight = 256;
    
      LeftsideMBbot = new ModelRenderer(this, 41, 200);
      LeftsideMBbot.addBox(0F, 0F, 0F, 1, 10, 15);
      LeftsideMBbot.setRotationPoint(-8F, 13F, -8F);
      LeftsideMBbot.setTextureSize(64, 32);
      LeftsideMBbot.mirror = true;
      setRotation(LeftsideMBbot, 0F, 0F, 0F);
      RightsideMBbot = new ModelRenderer(this, 111, 200);
      RightsideMBbot.addBox(0F, 0F, 0F, 1, 10, 15);
      RightsideMBbot.setRotationPoint(7F, 13F, -8F);
      RightsideMBbot.setTextureSize(64, 32);
      RightsideMBbot.mirror = true;
      setRotation(RightsideMBbot, 0F, 0F, 0F);
      BacksideMBbot = new ModelRenderer(this, 75, 200);
      BacksideMBbot.addBox(0F, 0F, 0F, 16, 10, 1);
      BacksideMBbot.setRotationPoint(-8F, 13F, 7F);
      BacksideMBbot.setTextureSize(64, 32);
      BacksideMBbot.mirror = true;
      setRotation(BacksideMBbot, 0F, 0F, 0F);
      TopcoverMBBot = new ModelRenderer(this, 60, 179);
      TopcoverMBBot.addBox(0F, 0F, 0F, 16, 1, 16);
      TopcoverMBBot.setRotationPoint(-8F, 12F, -8F);
      TopcoverMBBot.setTextureSize(64, 32);
      TopcoverMBBot.mirror = true;
      setRotation(TopcoverMBBot, 0F, 0F, 0F);
      BottomMBbot = new ModelRenderer(this, 60, 229);
      BottomMBbot.addBox(0F, 0F, 0F, 16, 1, 16);
      BottomMBbot.setRotationPoint(-8F, 23F, -8F);
      BottomMBbot.setTextureSize(64, 32);
      BottomMBbot.mirror = true;
      setRotation(BottomMBbot, 0F, 0F, 0F);
      MiddleBody = new ModelRenderer(this, 67, 152);
      MiddleBody.addBox(0F, 0F, 0F, 13, 8, 13);
      MiddleBody.setRotationPoint(-6.5F, 4F, -6.5F);
      MiddleBody.setTextureSize(64, 32);
      MiddleBody.mirror = true;
      setRotation(MiddleBody, 0F, 0F, 0F);
      Cornerpillar1 = new ModelRenderer(this, 56, 147);
      Cornerpillar1.addBox(0F, 0F, 0F, 2, 10, 2);
      Cornerpillar1.setRotationPoint(-7F, 2F, 5F);
      Cornerpillar1.setTextureSize(64, 32);
      Cornerpillar1.mirror = true;
      setRotation(Cornerpillar1, 0F, 0F, 0F);
      Cornerpillar2 = new ModelRenderer(this, 122, 147);
      Cornerpillar2.addBox(0F, 0F, 0F, 2, 10, 2);
      Cornerpillar2.setRotationPoint(5F, 2F, 5F);
      Cornerpillar2.setTextureSize(64, 32);
      Cornerpillar2.mirror = true;
      setRotation(Cornerpillar2, 0F, 0F, 0F);
      Cornerpillar3 = new ModelRenderer(this, 56, 163);
      Cornerpillar3.addBox(0F, 0F, 0F, 2, 10, 2);
      Cornerpillar3.setRotationPoint(-7F, 2F, -7F);
      Cornerpillar3.setTextureSize(64, 32);
      Cornerpillar3.mirror = true;
      setRotation(Cornerpillar3, 0F, 0F, 0F);
      Cornerpillar4 = new ModelRenderer(this, 122, 163);
      Cornerpillar4.addBox(0F, 0F, 0F, 2, 10, 2);
      Cornerpillar4.setRotationPoint(5F, 2F, -7F);
      Cornerpillar4.setTextureSize(64, 32);
      Cornerpillar4.mirror = true;
      setRotation(Cornerpillar4, 0F, 0F, 0F);
      GlassPlateTank = new ModelRenderer(this, 192, 200);
      GlassPlateTank.addBox(0F, 0F, 0F, 14, 10, 0);
      GlassPlateTank.setRotationPoint(-7F, 13F, -7.5F);
      GlassPlateTank.setTextureSize(64, 32);
      GlassPlateTank.mirror = true;
      setRotation(GlassPlateTank, 0F, 0F, 0F);
      GlassPlateLeftPillar = new ModelRenderer(this, 184, 200);
      GlassPlateLeftPillar.addBox(0F, 0F, 0F, 2, 10, 1);
      GlassPlateLeftPillar.setRotationPoint(-7F, 13F, -8F);
      GlassPlateLeftPillar.setTextureSize(64, 32);
      GlassPlateLeftPillar.mirror = true;
      setRotation(GlassPlateLeftPillar, 0F, 0F, 0F);
      GlassPlateRightPillar = new ModelRenderer(this, 202, 187);
      GlassPlateRightPillar.addBox(0F, 0F, 0F, 2, 10, 1);
      GlassPlateRightPillar.setRotationPoint(5F, 13F, -8F);
      GlassPlateRightPillar.setTextureSize(64, 32);
      GlassPlateRightPillar.mirror = true;
      setRotation(GlassPlateRightPillar, 0F, 0F, 0F);
      GlassPlateMiddlePillar = new ModelRenderer(this, 223, 200);
      GlassPlateMiddlePillar.addBox(0F, 0F, 0F, 2, 10, 1);
      GlassPlateMiddlePillar.setRotationPoint(-1F, 13F, -8F);
      GlassPlateMiddlePillar.setTextureSize(64, 32);
      GlassPlateMiddlePillar.mirror = true;
      setRotation(GlassPlateMiddlePillar, 0F, 0F, 0F);
      LatchMiddle = new ModelRenderer(this, 155, 160);
      LatchMiddle.addBox(0F, 0F, 0F, 5, 6, 1);
      LatchMiddle.setRotationPoint(-2.5F, 7F, -9F);
      LatchMiddle.setTextureSize(64, 32);
      LatchMiddle.mirror = true;
      setRotation(LatchMiddle, 0.5410521F, 0F, 0F);
      LatchRight = new ModelRenderer(this, 170, 160);
      LatchRight.addBox(0F, 0F, 0F, 1, 5, 4);
      LatchRight.setRotationPoint(2F, 7F, -9F);
      LatchRight.setTextureSize(64, 32);
      LatchRight.mirror = true;
      setRotation(LatchRight, 0.5410521F, 0.0174533F, 0F);
      LatchLeft = new ModelRenderer(this, 141, 160);
      LatchLeft.addBox(0F, 0F, 0F, 1, 5, 4);
      LatchLeft.setRotationPoint(-3F, 7F, -9F);
      LatchLeft.setTextureSize(64, 32);
      LatchLeft.mirror = true;
      setRotation(LatchLeft, 0.5410521F, 0.0174533F, 0F);
      TopBodyMain = new ModelRenderer(this, 63, 99);
      TopBodyMain.addBox(0F, 0F, 0F, 16, 12, 14);
      TopBodyMain.setRotationPoint(-8F, -8F, -6F);
      TopBodyMain.setTextureSize(64, 32);
      TopBodyMain.mirror = true;
      setRotation(TopBodyMain, 0F, 0F, 0F);
      TopBodyFrontLeft = new ModelRenderer(this, 56, 128);
      TopBodyFrontLeft.addBox(0F, 0F, 0F, 5, 12, 2);
      TopBodyFrontLeft.setRotationPoint(-8F, -8F, -8F);
      TopBodyFrontLeft.setTextureSize(64, 32);
      TopBodyFrontLeft.mirror = true;
      setRotation(TopBodyFrontLeft, 0F, 0F, 0F);
      TopBodyFrontRight = new ModelRenderer(this, 116, 128);
      TopBodyFrontRight.addBox(0F, 0F, 0F, 5, 12, 2);
      TopBodyFrontRight.setRotationPoint(3F, -8F, -8F);
      TopBodyFrontRight.setTextureSize(64, 32);
      TopBodyFrontRight.mirror = true;
      setRotation(TopBodyFrontRight, 0F, 0F, 0F);
      TopBodyFrontPanel = new ModelRenderer(this, 85, 128);
      TopBodyFrontPanel.addBox(0F, 0F, 0F, 6, 9, 1);
      TopBodyFrontPanel.setRotationPoint(-3F, -8F, -7F);
      TopBodyFrontPanel.setTextureSize(64, 32);
      TopBodyFrontPanel.mirror = true;
      setRotation(TopBodyFrontPanel, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    LeftsideMBbot.render(f5);
    RightsideMBbot.render(f5);
    BacksideMBbot.render(f5);
    TopcoverMBBot.render(f5);
    BottomMBbot.render(f5);
    MiddleBody.render(f5);
    Cornerpillar1.render(f5);
    Cornerpillar2.render(f5);
    Cornerpillar3.render(f5);
    Cornerpillar4.render(f5);
    GlassPlateTank.render(f5);
    GlassPlateLeftPillar.render(f5);
    GlassPlateRightPillar.render(f5);
    GlassPlateMiddlePillar.render(f5);
    LatchMiddle.render(f5);
    LatchRight.render(f5);
    LatchLeft.render(f5);
    TopBodyMain.render(f5);
    TopBodyFrontLeft.render(f5);
    TopBodyFrontRight.render(f5);
    TopBodyFrontPanel.render(f5);
  }
  
  public void render(float f5)
  {
	    LeftsideMBbot.render(f5);
	    RightsideMBbot.render(f5);
	    BacksideMBbot.render(f5);
	    TopcoverMBBot.render(f5);
	    BottomMBbot.render(f5);
	    MiddleBody.render(f5);
	    Cornerpillar1.render(f5);
	    Cornerpillar2.render(f5);
	    Cornerpillar3.render(f5);
	    Cornerpillar4.render(f5);
	    GlassPlateTank.render(f5);
	    GlassPlateLeftPillar.render(f5);
	    GlassPlateRightPillar.render(f5);
	    GlassPlateMiddlePillar.render(f5);
	    LatchMiddle.render(f5);
	    LatchRight.render(f5);
	    LatchLeft.render(f5);
	    TopBodyMain.render(f5);
	    TopBodyFrontLeft.render(f5);
	    TopBodyFrontRight.render(f5);
	    TopBodyFrontPanel.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
