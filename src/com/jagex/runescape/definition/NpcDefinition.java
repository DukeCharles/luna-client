package com.jagex.runescape.definition;// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.runescape.*;

public class NpcDefinition {

	/**
	 * Constructs a new {@code NpcDefinition} and initializes it with default values.
	 *
	 * <p>Initial values include standard scaling (128), a default tile size of 1,
	 * and setting interaction properties (clickable and minimap visibility) to {@code true}.
	 * Animation IDs, transformation IDs, and the NPC identifier are initialized to
	 * -1 to indicate they are unset until populated by {@link #loadDefinition(JagBuffer)}.</p>
	 */
	public NpcDefinition() {
		standAnimationId = -1;
		dummyBool3 = true; //dummy
		dummyInt2 = 932; //dummy
		opcode91 = -1; //opcode 91
		id = -1L; //
		modelHeight = 128;
		isClickable = true;
		modelWidth = 128;
		rotate90RightAnimation = -1;
		hasMinimapDot = true;
		opcode92 = -1; //opcode 92
		headIcon = -1;
		combatLevel = -1;
		dummyInt = 7;
		rotate90LeftAnimation = -1;
		size = 1;
		rotate180Animation = -1;
		priorityRender = false;
		walkAnimationId = -1;
		aBoolean647 = false; //dummy
		opcode90 = -1; //opcode 90
		degreesToTurn = 32;
		name = "null";
		varbitId = -1;
		varpId = -1;
		dummyBool2 = false; //dummy
	}

	//Dummy variables
	public static byte dummyByte = 6;
	public boolean aBoolean647;
	public boolean dummyBool2;
	public boolean dummyBool3;
	public int dummyInt;
	public int dummyInt2;

	//UnknownOpcodes
	public int opcode90; // Unknown (Opcode 90)
	public int opcode91; // Unknown (Opcode 91)
	public int opcode92; // Unknown (Opcode 92)

	public int standAnimationId; // The ID of the animation played when the NPC is standing still

	/**
	 * An array of NPC IDs that this NPC can transform into, depending on the
	 * value of the associated com.jagex.runescape.Varp or com.jagex.runescape.Varbit.
	 */
	public int[] transformations;


	public int[] headModelIds;
	public int[] modelIds;

	public long id;
	public static client clientInstance;
	public int modelHeight;
	public boolean isClickable;
	public int modelWidth;
	public int rotate90RightAnimation;
	public int[] originalColors;
	public static Cache modelCache = new Cache(30);
	public boolean hasMinimapDot;

	public int headIcon;
	public int combatLevel;

	public int rotate90LeftAnimation;
	public byte size;
	public int rotate180Animation;
	public boolean priorityRender;
	public int walkAnimationId;
	public String[] actions;


	public static int anInt649;
	public static int[] offsets;
	public int degreesToTurn;

	/**
	 * The name of the NPC, as displayed in-game. This is typically used in chat dialogues,
	 */
	public String name;




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

			//Exit condition for the opcode stream - opcode 0 indicates the end of this NPC's data
			if (opcode == 0) {
				return;
			}

			if (opcode == 1) {
				int modelCount = buffer.getByte();
				this.modelIds = new int[modelCount];

				for (int index = 0; index < modelCount; index++) {
					this.modelIds[index] = buffer.getShort();
				}
			} else if (opcode == 2)
				this.name = buffer.getString();
			else if (opcode == 3)
				description = buffer.getStringBytes();
			else if (opcode == 12)
				size = buffer.getSignedByte();
			else if (opcode == 13)
				standAnimationId = buffer.getShort();
			else if (opcode == 14)
				walkAnimationId = buffer.getShort();
			else if (opcode == 17) {
				walkAnimationId = buffer.getShort();
				rotate180Animation = buffer.getShort();
				rotate90LeftAnimation = buffer.getShort();
				rotate90RightAnimation = buffer.getShort();
			} else if (opcode >= 30 && opcode < 40) {
				if (actions == null) {
					actions = new String[5];
				}
				actions[opcode - 30] = buffer.getString();
				if (actions[opcode - 30].equalsIgnoreCase("hidden")) {
					actions[opcode - 30] = null;
				}
			} else if (opcode == 40) {
				int colorCount = buffer.getByte();
				originalColors = new int[colorCount];
				modifiedColors = new int[colorCount];
				for (int index = 0; index < colorCount; index++) {
					originalColors[index] = buffer.getShort();
					modifiedColors[index] = buffer.getShort();
				}
			} else if (opcode == 60) {
				int headModelCount = buffer.getByte();
				headModelIds = new int[headModelCount];
				for (int index = 0; index < headModelCount; index++) {
					headModelIds[index] = buffer.getShort();
				}
			} else if (opcode == 90)
				opcode90 = buffer.getShort();
			else if (opcode == 91)
				opcode91 = buffer.getShort();
			else if (opcode == 92)
				opcode92 = buffer.getShort();
			else if (opcode == 93)
				hasMinimapDot = false;
			else if (opcode == 95)
				combatLevel = buffer.getShort();
			else if (opcode == 97)
				modelWidth = buffer.getShort();
			else if (opcode == 98)
				modelHeight = buffer.getShort();
			else if (opcode == 99)
				priorityRender = true;
			else if (opcode == 100)
				lightDiffusion = buffer.getSignedByte();
			else if (opcode == 101)
				lightIntensity = buffer.getSignedByte() * 5;
			else if (opcode == 102)
				headIcon = buffer.getShort();
			else if (opcode == 103)
				degreesToTurn = buffer.getShort();
			else if (opcode == 106) {
				varbitId = buffer.getShort();
				if (varbitId == 65535) {
					varbitId = -1;
				}
				varpId = buffer.getShort();
				if (varpId == 65535) {
					varpId = -1;
				}
				int transformationCount = buffer.getByte();
				transformations = new int[transformationCount + 1];
				for (int index = 0; index <= transformationCount; index++) {
					transformations[index] = buffer.getShort();
					if (transformations[index] == 65535) {
						transformations[index] = -1;
					}
				}
			} else if (opcode == 107) {
				isClickable = false;
			}
			else {
				System.out.println("Error unrecognised NPC config code: " + opcode + " in NPC ID: " + id);
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
	 * <p>This method handles morphing NPCs by delegating to the current transformed
	 * definition. It verifies that all required model parts are downloaded from the
	 * cache before assembling them. If multiple model parts exist, they are merged into
	 * a single mesh. Finally, any NPC-specific color overrides are applied.</p>
	 *
	 * @return The constructed and recolored {@link Model} of the head,
	 *         or {@code null} if the NPC has no head models or if the required assets
	 *         are still downloading.
	 */
	public Model getHeadModel() {
		// Handle NPC transformations (morphing NPCs like quest-dependent characters)
		if (transformations != null) {
			NpcDefinition transformedDefinition = getTransformedDefinition();
			if (transformedDefinition == null) {
				return null;
			}
			else {
				return transformedDefinition.getHeadModel();
			}
		}

		// Early exit if no head models are defined for this NPC config
		if (headModelIds == null)
			return null;

		// Ensure all model parts are available in memory before construction
		boolean assetsMissing = false;
        for (int headModelId : headModelIds) {
            if (!Model.isDownloaded(headModelId)) {
                assetsMissing = true;
            }
        }

		if (assetsMissing) {
			return null;
		}

		// Fetch the individual model parts from the model cache/provider
		Model[] partModels = new Model[headModelIds.length];
		for (int index = 0; index < headModelIds.length; index++) {
			partModels[index] = Model.forId(headModelIds[index]);
		}

		// Combine parts into a single model instance
		Model headModel;
		if (partModels.length == 1) {
			headModel = partModels[0];
		}
		else {
			headModel = new Model(partModels.length, partModels);
		}

		// Apply HSL color overrides (e.g., changing basic human models into unique NPCs)
		if (originalColors != null) {
			for (int i = 0; i < originalColors.length; i++)
				headModel.replaceColor(originalColors[i], modifiedColors[i]);

		}
		return headModel;
	}

	/**
	 * Determines if this NPC is currently visible based on its transformation settings.
	 *
	 * <p>For NPCs that change appearance (e.g., based on quest progress), this checks
	 * the current value of the associated {@link Varbit} or {@link Varp}. If the
	 * transformation results in an ID of -1, the NPC is considered invisible.</p>
	 *
	 * @return {@code true} if the NPC has no transformations or transforms into a
	 *         valid NPC ID; {@code false} if it currently transforms into an empty slot.
	 */
	public boolean isVisible() {

		if (transformations == null) {
			return true;
		}

		int packedValue = -1;

		if (varbitId != -1) {
			Varbit varbit = Varbit.varbitTable[varbitId];
			int varpId = varbit.varpId;
			int leastSignificantBit = varbit.leastSignificantBit;
			int mostSignificantBit = varbit.mostSignificantBit;
			int bitfieldMaxValue = client.BITFIELD_MAX_VALUES[mostSignificantBit - leastSignificantBit];
			packedValue = clientInstance.localVarps[varpId] >> leastSignificantBit & bitfieldMaxValue;
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
		if (size == 1)
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
