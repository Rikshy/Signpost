package gollorum.signpost.network.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import gollorum.signpost.management.ConfigHandler;
import gollorum.signpost.management.ConfigHandler.RecipeCost;
import gollorum.signpost.management.ConfigHandler.SecurityLevel;
import gollorum.signpost.management.PostHandler;
import gollorum.signpost.util.BaseInfo;
import gollorum.signpost.util.StonedHashSet;
import io.netty.buffer.ByteBuf;

public class InitPlayerResponseMessage implements IMessage{

	public StonedHashSet allWaystones = new StonedHashSet();

	public boolean deactivateTeleportation;
	public boolean interdimensional;
	public int maxDist;
	public String paymentItem;
	public int costMult;
	
	public RecipeCost signRec;
	public RecipeCost waysRec;

	public SecurityLevel securityLevelWaystone;
	public SecurityLevel securityLevelSignpost;

	public boolean disableVillageGeneration;
	public int villageWaystonesWeight;
	public int villageMaxSignposts;
	public int villageSignpostsWeight;
	public boolean onlyVillageTargets;

	public InitPlayerResponseMessage(){
		if(!ConfigHandler.isDeactivateTeleportation()){
			allWaystones = PostHandler.getNativeWaystones();
		}
		deactivateTeleportation = ConfigHandler.isDeactivateTeleportation();
		interdimensional = ConfigHandler.isInterdimensional();
		maxDist = ConfigHandler.getMaxDist();
		paymentItem = ConfigHandler.getPaymentItem();
		costMult = ConfigHandler.getCostMult();
		signRec = ConfigHandler.getSignRec();
		waysRec = ConfigHandler.getWaysRec();
		securityLevelWaystone = ConfigHandler.getSecurityLevelWaystone();
		securityLevelSignpost = ConfigHandler.getSecurityLevelSignpost();
		disableVillageGeneration = ConfigHandler.isDisableVillageGeneration();
		villageMaxSignposts = ConfigHandler.getVillageMaxSignposts();
		villageSignpostsWeight = ConfigHandler.getVillageSignpostsWeight();
		villageWaystonesWeight = ConfigHandler.getVillageWaystonesWeight();
		onlyVillageTargets = ConfigHandler.isOnlyVillageTargets();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(deactivateTeleportation);
		if(!ConfigHandler.isDeactivateTeleportation()){
			buf.writeInt(allWaystones.size());
			for(BaseInfo now:allWaystones){
				now.toBytes(buf);
			}
		}
		buf.writeBoolean(interdimensional);
		buf.writeInt(maxDist);
		ByteBufUtils.writeUTF8String(buf, paymentItem);
		buf.writeInt(costMult);
		ByteBufUtils.writeUTF8String(buf, securityLevelWaystone.name());
		ByteBufUtils.writeUTF8String(buf, securityLevelSignpost.name());
		ByteBufUtils.writeUTF8String(buf, signRec.name());
		ByteBufUtils.writeUTF8String(buf, waysRec.name());
		buf.writeBoolean(disableVillageGeneration);
		buf.writeInt(villageMaxSignposts);
		buf.writeInt(villageSignpostsWeight);
		buf.writeInt(villageWaystonesWeight);
		buf.writeBoolean(onlyVillageTargets);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		deactivateTeleportation = buf.readBoolean();
		if(!deactivateTeleportation){
			allWaystones = new StonedHashSet();
			int c = buf.readInt();
			for(int i=0; i<c; i++){
				allWaystones.add(BaseInfo.fromBytes(buf));
			}
		}
		interdimensional = buf.readBoolean();
		maxDist = buf.readInt();
		paymentItem = ByteBufUtils.readUTF8String(buf);
		costMult = buf.readInt();
		securityLevelWaystone = SecurityLevel.valueOf(ByteBufUtils.readUTF8String(buf));
		securityLevelSignpost = SecurityLevel.valueOf(ByteBufUtils.readUTF8String(buf));
		signRec = RecipeCost.valueOf(ByteBufUtils.readUTF8String(buf));
		waysRec = RecipeCost.valueOf(ByteBufUtils.readUTF8String(buf));
		disableVillageGeneration = buf.readBoolean();
		villageMaxSignposts = buf.readInt();
		villageSignpostsWeight = buf.readInt();
		villageWaystonesWeight = buf.readInt();
		onlyVillageTargets = buf.readBoolean();
	}

}
