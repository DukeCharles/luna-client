// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class NpcDefinition {

	public NpcDefinition() {
		anInt621 = -1;
		aBoolean623 = true;
		anInt624 = 932;
		anInt627 = -1;
		id = -1L;
		anInt630 = 128;
		aBoolean631 = true;
		anInt632 = 128;
		anInt633 = -1;
		aBoolean636 = true;
		anInt637 = -1;
		anInt638 = -1;
		anInt639 = -1;
		anInt640 = 7;
		anInt641 = -1;
		aByte642 = 1;
		anInt643 = -1;
		aBoolean644 = false;
		anInt645 = -1;
		aBoolean647 = false;
		anInt648 = -1;
		anInt651 = 32;
		aString652 = "null";
		anInt654 = -1;
		anInt659 = -1;
		aBoolean662 = false;
	}

	public int anInt621;
	public int[] transformations;
	public boolean aBoolean623;
	public int anInt624;
	public int[] headModelIds;
	public int[] anIntArray626;
	public int anInt627;
	public long id;
	public static client aClient629;
	public int anInt630;
	public boolean aBoolean631;
	public int anInt632;
	public int anInt633;
	public int[] anIntArray634;
	public static LruHashTable modelCache = new LruHashTable(30);
	public boolean aBoolean636;
	public int anInt637;
	public int anInt638;
	public int anInt639;
	public int anInt640;
	public int anInt641;
	public byte aByte642;
	public int anInt643;
	public boolean aBoolean644;
	public int anInt645;
	public String aStringArray646[];
	public boolean aBoolean647;
	public int anInt648;
	public static int anInt649;
	public static int offsets[];
	public int anInt651;
	public String aString652;
	public static byte aByte653 = 6;
	public int anInt654;
	public static NpcDefinition cache[];
	public int anIntArray656[];
	public static JagBuffer dataBuffer;
	public int anInt658;
	public int anInt659;
	public byte aByteArray660[];
	public static int anInt661;
	public boolean aBoolean662;
	public int anInt663;

	public void init(byte byte0, JagBuffer class50_sub1_sub2) {
		if(byte0 != 6)
			throw new NullPointerException();

		do {
			int i = class50_sub1_sub2.getByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = class50_sub1_sub2.getByte();
				anIntArray626 = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					anIntArray626[j1] = class50_sub1_sub2.getShort();

			} else if (i == 2)
				aString652 = class50_sub1_sub2.getString();
			else if (i == 3)
				aByteArray660 = class50_sub1_sub2.getStringBytes();
			else if (i == 12)
				aByte642 = class50_sub1_sub2.getSignedByte();
			else if (i == 13)
				anInt621 = class50_sub1_sub2.getShort();
			else if (i == 14)
				anInt645 = class50_sub1_sub2.getShort();
			else if (i == 17) {
				anInt645 = class50_sub1_sub2.getShort();
				anInt643 = class50_sub1_sub2.getShort();
				anInt641 = class50_sub1_sub2.getShort();
				anInt633 = class50_sub1_sub2.getShort();
			} else if (i >= 30 && i < 40) {
				if (aStringArray646 == null)
					aStringArray646 = new String[5];
				aStringArray646[i - 30] = class50_sub1_sub2.getString();
				if (aStringArray646[i - 30].equalsIgnoreCase("hidden"))
					aStringArray646[i - 30] = null;
			} else if (i == 40) {
				int k = class50_sub1_sub2.getByte();
				anIntArray634 = new int[k];
				anIntArray656 = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					anIntArray634[k1] = class50_sub1_sub2.getShort();
					anIntArray656[k1] = class50_sub1_sub2.getShort();
				}

			} else if (i == 60) {
				int l = class50_sub1_sub2.getByte();
				headModelIds = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					headModelIds[l1] = class50_sub1_sub2.getShort();

			} else if (i == 90)
				anInt648 = class50_sub1_sub2.getShort();
			else if (i == 91)
				anInt627 = class50_sub1_sub2.getShort();
			else if (i == 92)
				anInt637 = class50_sub1_sub2.getShort();
			else if (i == 93)
				aBoolean636 = false;
			else if (i == 95)
				anInt639 = class50_sub1_sub2.getShort();
			else if (i == 97)
				anInt632 = class50_sub1_sub2.getShort();
			else if (i == 98)
				anInt630 = class50_sub1_sub2.getShort();
			else if (i == 99)
				aBoolean644 = true;
			else if (i == 100)
				anInt663 = class50_sub1_sub2.getSignedByte();
			else if (i == 101)
				anInt658 = class50_sub1_sub2.getSignedByte() * 5;
			else if (i == 102)
				anInt638 = class50_sub1_sub2.getShort();
			else if (i == 103)
				anInt651 = class50_sub1_sub2.getShort();
			else if (i == 106) {
				anInt654 = class50_sub1_sub2.getShort();
				if (anInt654 == 65535)
					anInt654 = -1;
				anInt659 = class50_sub1_sub2.getShort();
				if (anInt659 == 65535)
					anInt659 = -1;
				int i1 = class50_sub1_sub2.getByte();
				transformations = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					transformations[i2] = class50_sub1_sub2.getShort();
					if (transformations[i2] == 65535)
						transformations[i2] = -1;
				}

			} else if (i == 107)
				aBoolean631 = false;
		} while (true);
	}

	/**
	 * Disposes of the static NPC definition data by nullifying caches and buffers.
	 * This is typically called when the client is cleaning up resources or shutting down.
	 */
	public static void dispose() {
		modelCache = null; // The LRU cache for NPC models
		offsets = null; // The array containing offsets for npc.dat
		cache = null; // The static cache of NpcDefinition instances
		dataBuffer = null; // The raw data buffer for npc.dat
	}

	/**
	 * Retrieves the 3D model for this NPC's head, typically used in chat dialogues.
	 *
	 * @return The constructed {@link Model} of the head, or {@code null} if not available or downloaded.
	 */
	public Model getHeadModel() {
		// Handle NPC transformations (e.g., morphing NPCs or those dependent on player state)
		if (transformations != null) {
			NpcDefinition transformedDef = method363(false);
			if (transformedDef == null)
				return null;
			else
				return transformedDef.getHeadModel();
		}

		// If no head models are defined for this NPC, return null
		if (headModelIds == null)
			return null;

		// Ensure all model parts are downloaded before attempting to construct
		boolean flag = false;
		for (int i = 0; i < headModelIds.length; i++) {
			if (!Model.isDownloaded(headModelIds[i])) {
				flag = true;
			}
		}

		if (flag) {
			return null;
		}

		Model aclass50_sub1_sub4_sub4[] = new Model[headModelIds.length];
		for (int l = 0; l < headModelIds.length; l++)
			aclass50_sub1_sub4_sub4[l] = Model.forId(headModelIds[l]);

		Model class50_sub1_sub4_sub4;
		if (aclass50_sub1_sub4_sub4.length == 1)
			class50_sub1_sub4_sub4 = aclass50_sub1_sub4_sub4[0];
		else
			class50_sub1_sub4_sub4 = new Model(aclass50_sub1_sub4_sub4.length,
					aclass50_sub1_sub4_sub4);
		if (anIntArray634 != null) {
			for (int i1 = 0; i1 < anIntArray634.length; i1++)
				class50_sub1_sub4_sub4.replaceColor(anIntArray634[i1], anIntArray656[i1]);

		}
		return class50_sub1_sub4_sub4;
	}

	public boolean method360(int i) {
		while (i >= 0)
			aBoolean662 = !aBoolean662;

		if (transformations == null)
			return true;
		int packedValue = -1;
		if (anInt654 != -1) {
			Varbit varbit = Varbit.varbitTable[anInt654];
			int k = varbit.varpId;
			int l = varbit.leastSignificantBit;
			int i1 = varbit.mostSignificantBit;
			int j1 = client.BITFIELD_MAX_VALUES[i1 - l];
			packedValue = aClient629.localVarps[k] >> l & j1;
		} else if (anInt659 != -1)
			packedValue = aClient629.localVarps[anInt659];
		return packedValue >= 0 && packedValue < transformations.length && transformations[packedValue] != -1;
	}

	public static void unpack(Archive class2) {
		dataBuffer = new JagBuffer(class2.get("npc.dat"));
		JagBuffer class50_sub1_sub2 = new JagBuffer(class2.get("npc.idx"));
		anInt649 = class50_sub1_sub2.getShort();
		offsets = new int[anInt649];
		int i = 2;
		for (int j = 0; j < anInt649; j++) {
			offsets[j] = i;
			i += class50_sub1_sub2.getShort();
		}

		cache = new NpcDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new NpcDefinition();

	}

	public Model method362(int i, int j, int k, int ai[]) {
		if (transformations != null) {
			NpcDefinition class37 = method363(false);
			if (class37 == null)
				return null;
			else
				return class37.method362(i, j, 0, ai);
		}
		Model class50_sub1_sub4_sub4 = (Model) modelCache.get(id);
		if (class50_sub1_sub4_sub4 == null) {
			boolean flag = false;
			for (int l = 0; l < anIntArray626.length; l++)
				if (!Model.isDownloaded(anIntArray626[l]))
					flag = true;

			if (flag)
				return null;
			Model aclass50_sub1_sub4_sub4[] = new Model[anIntArray626.length];
			for (int i1 = 0; i1 < anIntArray626.length; i1++)
				aclass50_sub1_sub4_sub4[i1] = Model.forId(anIntArray626[i1]);

			if (aclass50_sub1_sub4_sub4.length == 1)
				class50_sub1_sub4_sub4 = aclass50_sub1_sub4_sub4[0];
			else
				class50_sub1_sub4_sub4 = new Model(aclass50_sub1_sub4_sub4.length,
						aclass50_sub1_sub4_sub4);
			if (anIntArray634 != null) {
				for (int j1 = 0; j1 < anIntArray634.length; j1++)
					class50_sub1_sub4_sub4.replaceColor(anIntArray634[j1], anIntArray656[j1]);

			}
			class50_sub1_sub4_sub4.groupIndicesByTransform();
			class50_sub1_sub4_sub4.initLighting(64 + anInt663, 850 + anInt658, -30, -50, -30, true);
			modelCache.put(class50_sub1_sub4_sub4, id);
		}
		Model class50_sub1_sub4_sub4_1 = Model.SCRATCH_MODEL;
		if (k != 0)
			aBoolean647 = !aBoolean647;
		class50_sub1_sub4_sub4_1.copyFrom(AnimationFrame.isFrameTransparent(i) & AnimationFrame.isFrameTransparent(j),
				class50_sub1_sub4_sub4);
		if (i != -1 && j != -1)
			class50_sub1_sub4_sub4_1.applyBlendedAnimation(j, 0, i, ai);
		else if (i != -1)
			class50_sub1_sub4_sub4_1.applyAnimation(i, (byte) 6);
		if (anInt632 != 128 || anInt630 != 128)
			class50_sub1_sub4_sub4_1.resizeModel(anInt632, anInt630, anInt632);
		class50_sub1_sub4_sub4_1.calculateRadius();
		class50_sub1_sub4_sub4_1.faceIndicesByBone = null;
		class50_sub1_sub4_sub4_1.vertexIndicesByBone = null;
		if (aByte642 == 1)
			class50_sub1_sub4_sub4_1.isPriorityPicking = true;
		return class50_sub1_sub4_sub4_1;
	}

	public NpcDefinition method363(boolean flag) {
		if (flag)
			anInt640 = -212;
		int i = -1;
		if (anInt654 != -1) {
			Varbit class49 = Varbit.varbitTable[anInt654];
			int j = class49.varpId;
			int k = class49.leastSignificantBit;
			int l = class49.mostSignificantBit;
			int i1 = client.BITFIELD_MAX_VALUES[l - k];
			i = aClient629.localVarps[j] >> k & i1;
		} else if (anInt659 != -1)
			i = aClient629.localVarps[anInt659];
		if (i < 0 || i >= transformations.length || transformations[i] == -1)
			return null;
		else
			return forId(transformations[i]);
	}

	public static NpcDefinition forId(int id) {
		for (int j = 0; j < 20; j++)
			if (cache[j].id == id)
				return cache[j];

		anInt661 = (anInt661 + 1) % 20;
		NpcDefinition def = cache[anInt661] = new NpcDefinition();
		dataBuffer.position = offsets[id];
		def.id = id;
		def.init(aByte653, dataBuffer);
		return def;
	}

}
