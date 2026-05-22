package com.jagex.runescape.definition;// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.runescape.*;


import static com.jagex.runescape.opcodes.NpcOpcode.*;

public class NpcDefinition {

	public NpcDefinition() {
		anInt621 = -1;
		aBoolean623 = true;
		anInt624 = 932;
		anInt627 = -1;
		id = -1L;
		modelHeight = 128;
		isClickable = true;
		modelWidth = 128;
		anInt633 = -1;
		hasMinimapDot = true;
		anInt637 = -1;
		headIcon = -1;
		combatLevel = -1;
		anInt640 = 7;
		anInt641 = -1;
		aByte642 = 1;
		anInt643 = -1;
		priorityRender = false;
		anInt645 = -1;
		aBoolean647 = false;
		anInt648 = -1;
		degreesToTurn = 32;
		name = "null";
		varbitId = -1;
		varpId = -1;
		aBoolean662 = false;
	}

	public int anInt621; // The ID of the animation played when the NPC is standing still

	/**
	 * An array of NPC IDs that this NPC can transform into, depending on the
	 * value of the associated com.jagex.runescape.Varp or com.jagex.runescape.Varbit.
	 */
	public int[] transformations;
	public boolean aBoolean623;
	public int anInt624;
	public int[] headModelIds;
	public int[] modelIds;
	public int anInt627;
	public long id;
	public static client clientInstance;
	public int modelHeight;
	public boolean isClickable;
	public int modelWidth;
	public int anInt633;
	public int[] originalColors;
	public static Cache modelCache = new Cache(30);
	public boolean hasMinimapDot;
	public int anInt637;
	public int headIcon;
	public int combatLevel;
	public int anInt640;
	public int anInt641;
	public byte aByte642;
	public int anInt643;
	public boolean priorityRender;
	public int anInt645;
	public String[] actions;
	public boolean aBoolean647;
	public int anInt648;
	public static int anInt649;
	public static int[] offsets;
	public int degreesToTurn;
	public String name;
	public static byte dummyByte = 6;

	/**
	 * The ID of the {@link Varbit} used to determine which transformation index
	 * to use from the {@link #transformations} array.
	 */
	public int varbitId;
	public static NpcDefinition[] cache;
	public int[] modifiedColors;
	public static JagBuffer dataBuffer;
	public int lightIntensity;

	/**
	 * The ID of the {@link Varp} used to determine the transformation index if
	 * {@link #varbitId} is not set.
	 */
	public int varpId;
	public byte[] description;
	public static int bufferIndex;
	public boolean aBoolean662;
	public int lightDiffusion;

	/**
	 * Retrieves the {@link NpcDefinition} for a specific NPC ID.
	 *
	 * <p>This method first checks a small cyclic cache of the most recently used
	 * definitions. If the requested definition is not cached, it retrieves the
	 * binary data from the {@link #dataBuffer} using pre-calculated {@link #offsets}
	 * and decodes it into a new instance.</p>
	 *
	 * @param id The unique identifier of the NPC to load.
	 * @return The populated {@link NpcDefinition} instance.
	 */
	public static NpcDefinition getDefinition(int id) {
		// Check the cyclic cache for an existing instance
        for (NpcDefinition npcDefinition : cache) {
            if (npcDefinition.id == id) {
                return npcDefinition;
            }
        }

		// Move to the next slot in the cyclic cache
		bufferIndex = (bufferIndex + 1) % 20;
		NpcDefinition defintion = cache[bufferIndex] = new NpcDefinition();

		// Position the buffer and decode the NPC properties
		dataBuffer.position = offsets[id];
		defintion.id = id;
		defintion.loadDefinition(dataBuffer);

		return defintion;
	}

	/**
	 * Populates the NPC definition by parsing attribute data from the provided buffer.
	 *
	 * <p>This method reads a stream of opcodes and values from {@code npc.dat}.
	 * Each opcode corresponds to a specific property of the NPC, such as its name,
	 * models, animations, combat level, and recoloring information.</p>
	 *
	 * @param buffer The {@link JagBuffer} containing the raw binary NPC configuration
	 *               data retrieved from the cache.
	 * @throws NullPointerException if the {@code dummy} value is not 6.
	 */
	public void loadDefinition(JagBuffer buffer) {


		do {
			int opcode = buffer.getByte();

			//System.out.println("NPC Config Opcode: " + opcode); //TODO REMOVE

			if(opcode == 0) {
				return;
			}

			switch(opcode) {
				case MODEL_IDS:
					int modelCount = buffer.getByte();
					modelIds = new int[modelCount];
					for (int index = 0; index < modelCount; index++) {
						modelIds[index] = buffer.getShort();
					}
					break;
					case NAME:
						name = buffer.getString();
						break;
					case DESCRIPTION:
						description = buffer.getStringBytes();
						break;
					case SIZE:
						aByte642 = buffer.getSignedByte();
						break;
					case STAND_ANIMATION:
						anInt621 = buffer.getShort();
						break;
					case WALK_ANIMATION:
						anInt645 = buffer.getShort();
						break;
					case WALK_ANIMATIONS:
						anInt645 = buffer.getShort();
						anInt643 = buffer.getShort();
						anInt641 = buffer.getShort();
						anInt633 = buffer.getShort();
						break;
					case ACTIONS: //Was i >= 30 && i < 40
						if (actions == null) {
							actions = new String[5];
						}
						actions[opcode - 30] = buffer.getString();
						if (actions[opcode - 30].equalsIgnoreCase("hidden")) {
							actions[opcode - 30] = null;
						}
						break;
					case RECOLOR:
						int colorCount = buffer.getByte();
						originalColors = new int[colorCount];
						modifiedColors = new int[colorCount];
						for (int index = 0; index < colorCount; index++) {
							originalColors[index] = buffer.getShort();
							modifiedColors[index] = buffer.getShort();
						}
						break;
					case HEAD_MODEL_IDS:
						int headModelCount = buffer.getByte();
						headModelIds = new int[headModelCount];
						for (int index = 0; index < headModelCount; index++) {
							headModelIds[index] = buffer.getShort();
						}
						break;
					case UNKNOWN_SHORT_90:
						anInt648 = buffer.getShort();
						break;
					case UNKNOWN_SHORT_91:
						anInt627 = buffer.getShort();
						break;
					case UNKNOWN_SHORT_92:
						anInt637 = buffer.getShort();
						break;
					case NO_MINIMAP_DOT:
						hasMinimapDot = false;
						break;
					case COMBAT_LEVEL:
						combatLevel = buffer.getShort();
						break;
					case MODEL_WIDTH:
						modelWidth = buffer.getShort();
						break;
					case MODEL_HEIGHT:
						modelHeight = buffer.getShort();
						break;
					case PRIORITY_RENDER:
						priorityRender = true;
						break;
					case LIGHT_DIFFUSION:
						lightDiffusion = buffer.getSignedByte();
						break;
					case LIGHT_INTENSITY:
						lightIntensity = buffer.getSignedByte() * 5;
						break;
					case HEAD_ICON:
						headIcon = buffer.getShort();
						break;
					case DEGREES_TO_TURN:
						degreesToTurn = buffer.getShort();
						break;
					case TRANSFORMATIONS:
						varbitId = buffer.getShort();
						if (varbitId == 65535)
							varbitId = -1;
						varpId = buffer.getShort();
						if (varpId == 65535)
							varpId = -1;
						int transformationCount = buffer.getByte();
						transformations = new int[transformationCount + 1];
						for (int index = 0; index <= transformationCount; index++) {
							transformations[index] = buffer.getShort();
							if (transformations[index] == 65535)
								transformations[index] = -1;
						}
						break;
					case NOT_CLICKABLE:
						isClickable = false;
						break;
					default:
						System.out.println("Error unrecognised npc config code: " + opcode);
						break;
			}
		} while (true);
	}

	/**
	 * Disposes of the static NPC com.jagex.runescape.com.jagex.runescape.definition data by nullifying caches and buffers.
	 * This is typically called when the com.jagex.runescape.client is cleaning up resources or shutting down.
	 */
	public static void dispose() {
		modelCache = null; // The LRU cache for NPC models
		offsets = null; // The array containing offsets for npc.dat
		cache = null; // The static cache of com.jagex.runescape.definition.NpcDefinition instances
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
			NpcDefinition transformedDef = getTransformedDefinition();
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
		if (originalColors != null) {
			for (int i1 = 0; i1 < originalColors.length; i1++)
				class50_sub1_sub4_sub4.replaceColor(originalColors[i1], modifiedColors[i1]);

		}
		return class50_sub1_sub4_sub4;
	}

	public boolean method360(int i) {
		while (i >= 0)
			aBoolean662 = !aBoolean662;

		if (transformations == null)
			return true;
		int packedValue = -1;
		if (varbitId != -1) {
			Varbit varbit = Varbit.varbitTable[varbitId];
			int k = varbit.varpId;
			int l = varbit.leastSignificantBit;
			int i1 = varbit.mostSignificantBit;
			int j1 = client.BITFIELD_MAX_VALUES[i1 - l];
			packedValue = clientInstance.localVarps[k] >> l & j1;
		} else if (varpId != -1)
			packedValue = clientInstance.localVarps[varpId];
		return packedValue >= 0 && packedValue < transformations.length && transformations[packedValue] != -1;
	}

	public static void unpack(Archive archive) {
		dataBuffer = new JagBuffer(archive.get("npc.dat"));
		JagBuffer class50_sub1_sub2 = new JagBuffer(archive.get("npc.idx"));
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

	public Model method362(int i, int j, int k, int[] ai) {
		if (transformations != null) {
			NpcDefinition class37 = getTransformedDefinition();
			if (class37 == null)
				return null;
			else
				return class37.method362(i, j, 0, ai);
		}
		Model class50_sub1_sub4_sub4 = (Model) modelCache.get(id);
		if (class50_sub1_sub4_sub4 == null) {
			boolean flag = false;
			for (int l = 0; l < modelIds.length; l++)
				if (!Model.isDownloaded(modelIds[l]))
					flag = true;

			if (flag)
				return null;
			Model aclass50_sub1_sub4_sub4[] = new Model[modelIds.length];
			for (int i1 = 0; i1 < modelIds.length; i1++)
				aclass50_sub1_sub4_sub4[i1] = Model.forId(modelIds[i1]);

			if (aclass50_sub1_sub4_sub4.length == 1)
				class50_sub1_sub4_sub4 = aclass50_sub1_sub4_sub4[0];
			else
				class50_sub1_sub4_sub4 = new Model(aclass50_sub1_sub4_sub4.length,
						aclass50_sub1_sub4_sub4);
			if (originalColors != null) {
				for (int j1 = 0; j1 < originalColors.length; j1++)
					class50_sub1_sub4_sub4.replaceColor(originalColors[j1], modifiedColors[j1]);

			}
			class50_sub1_sub4_sub4.groupIndicesByTransform();
			class50_sub1_sub4_sub4.initLighting(64 + lightDiffusion, 850 + lightIntensity, -30, -50, -30, true);
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
		if (modelWidth != 128 || modelHeight != 128)
			class50_sub1_sub4_sub4_1.resizeModel(modelWidth, modelHeight, modelWidth);
		class50_sub1_sub4_sub4_1.calculateRadius();
		class50_sub1_sub4_sub4_1.faceIndicesByBone = null;
		class50_sub1_sub4_sub4_1.vertexIndicesByBone = null;
		if (aByte642 == 1)
			class50_sub1_sub4_sub4_1.isPriorityPicking = true;
		return class50_sub1_sub4_sub4_1;
	}

	/**
	 * Selects the appropriate {@link NpcDefinition} for this NPC based on the player's
	 * current state (e.g., quest progress, setting toggles, or world events) stored
	 * in Varps or Varbits.
	 * <p>
	 * This mechanism allows a single NPC index to "morph" into different NPC IDs.
	 * Common examples include quest NPCs that change appearance as you progress
	 * or farming patches that change based on growth stages.
	 *
	 * @return The {@link NpcDefinition} of the NPC this instance has transformed into,
	 * or {@code null} if the transformation index is out of bounds or results
	 * in an invalid NPC ID.
	 */
	public NpcDefinition getTransformedDefinition() {

		int index = -1;

		if (varbitId != -1) {
			// Pull the bit-packing metadata
			Varbit varbit = Varbit.varbitTable[varbitId];
			int parentVarpId = varbit.varpId;
			int lsb = varbit.leastSignificantBit;
			int msb = varbit.mostSignificantBit;
			int mask = client.BITFIELD_MAX_VALUES[msb - lsb];

			// Extract the specific value from the bit-packed com.jagex.runescape.Varp
			index = clientInstance.localVarps[parentVarpId] >> lsb & mask;

		} else if (varpId != -1) {
			// Directly use the integer value from the com.jagex.runescape.Varp
			index = clientInstance.localVarps[varpId];
		}

		// Validate the index against the transformation array
		if (index < 0 || index >= transformations.length || transformations[index] == -1)
			return null;
		else {
			return getDefinition(transformations[index]);
		}
	}

}
